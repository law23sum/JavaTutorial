package com.apps.launcher;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Text-based launcher that discovers every main method on the classpath and lets the user choose one.
 */
public final class DemoLauncher {
    private DemoLauncher() {
    }

    public static void main(String[] args) {
        List<DemoDescriptor> demos = DemoScanner.discover();
        if (demos.isEmpty()) {
            System.out.println("No demos with a main method were discovered on the classpath.");
            return;
        }

        if (args != null && args.length > 0) {
            DemoDescriptor requested = resolveFromArgument(demos, args[0]);
            if (requested != null) {
                String[] forwarded = Arrays.copyOfRange(args, 1, args.length);
                System.out.printf("Running %s...%n", requested.className());
                requested.run(forwarded);
                return;
            }
            System.out.printf("Could not resolve '%s'. Falling back to interactive menu...%n", args[0]);
        }

        runInteractiveMenu(demos);
    }

    private static DemoDescriptor resolveFromArgument(List<DemoDescriptor> demos, String selector) {
        if (selector == null || selector.isBlank()) {
            return null;
        }
        String normalized = selector.trim().toLowerCase(Locale.ROOT);
        for (DemoDescriptor demo : demos) {
            if (demo.className().toLowerCase(Locale.ROOT).equals(normalized)
                    || demo.simpleName().toLowerCase(Locale.ROOT).equals(normalized)) {
                return demo;
            }
        }
        // allow prefix matching for convenience
        List<DemoDescriptor> matches = demos.stream()
                .filter(demo -> demo.className().toLowerCase(Locale.ROOT).contains(normalized))
                .collect(Collectors.toList());
        return matches.size() == 1 ? matches.get(0) : null;
    }

    private static void runInteractiveMenu(List<DemoDescriptor> demos) {
        Scanner scanner = new Scanner(System.in);
        List<DemoDescriptor> filtered = new ArrayList<>(demos);
        while (true) {
            printMenu(filtered);
            System.out.print("Enter # to run, 'filter <text>' to narrow, 'all' to reset, or 'q' to exit: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q") || input.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye!");
                return;
            }
            if (input.equalsIgnoreCase("all")) {
                filtered = new ArrayList<>(demos);
                continue;
            }
            if (input.toLowerCase(Locale.ROOT).startsWith("filter")) {
                String[] tokens = input.split("\\s+", 2);
                if (tokens.length == 2 && !tokens[1].isBlank()) {
                    String query = tokens[1].trim().toLowerCase(Locale.ROOT);
                    filtered = demos.stream()
                            .filter(demo -> demo.className().toLowerCase(Locale.ROOT).contains(query))
                            .collect(Collectors.toList());
                } else {
                    System.out.println("Provide a search term after 'filter'.");
                }
                continue;
            }
            if (filtered.isEmpty()) {
                System.out.println("No demos match the current filter. Type 'all' to reset.");
                continue;
            }
            Integer choice = parseNumber(input);
            if (choice == null) {
                DemoDescriptor guess = resolveFromArgument(filtered, input);
                if (guess != null) {
                    runDemo(scanner, guess);
                } else {
                    System.out.printf("Input '%s' was not understood.%n", input);
                }
                continue;
            }
            int index = choice - 1;
            if (index < 0 || index >= filtered.size()) {
                System.out.println("Number is out of range for the visible list.");
                continue;
            }
            runDemo(scanner, filtered.get(index));
        }
    }

    private static void runDemo(Scanner scanner, DemoDescriptor descriptor) {
        System.out.printf("\n--- Running %s ---%n%n", descriptor.className());
        try {
            descriptor.run(new String[0]);
        } catch (DemoExecutionException ex) {
            System.out.printf("Demo threw an exception: %s%n", ex.getMessage());
            ex.printStackTrace(System.out);
        }
        System.out.println("\n--- Demo finished. Press Enter to return to the menu ---");
        scanner.nextLine();
    }

    private static void printMenu(List<DemoDescriptor> demos) {
        System.out.printf("%nDiscovered %d demos with a main method.%n", demos.size());
        for (int i = 0; i < demos.size(); i++) {
            DemoDescriptor descriptor = demos.get(i);
            System.out.printf("%4d) %s%n", i + 1, descriptor.className());
        }
        if (demos.isEmpty()) {
            System.out.println("(Use 'all' to reset the filter.)");
        }
    }

    private static Integer parseNumber(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
