package com.tutorial.foundation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * Java Control Flow — compact, runnable tour with implementation notes.
 *
 * What “control flow” means here:
 *   The constructs that decide *what runs* and *in what order*: branching, looping,
 *   early exits, and exception paths. We’ll also touch resource-handling (try-with-resources).
 *
 * Covered
 *  1) if / else if / else (guards, nesting, readability tips)
 *  2) switch (classic statements: int, String, enum; fall-through & breaks)
 *  3) switch expressions (arrow labels, yield, exhaustiveness)
 *  4) Loops: for, enhanced-for, while, do-while (with break / continue)
 *  5) Labeled break/continue for nested loops
 *  6) Early exits: return and throw (guard clauses vs deep nesting)
 *  7) Exceptions as flow: try / catch / finally, multi-catch, rethrow
 *  8) Try-with-resources (AutoCloseable), and finally-ordering gotchas
 *  9) Assertions (disabled by default; enable with -ea)
 *
 * Build & run (JDK 17+ recommended):
 *   javac com/tutorial/foundation/ControlFlowTour.java && java com.tutorial.foundation.ControlFlowTour
 */
public class ControlFlow {

    // -------------------------------------------------------------------------
    // 1) if / else if / else — the everyday branch
    //    Tips:
    //      - Prefer "guard clauses" (early returns) to reduce nesting.
    //      - Keep conditions small and named; extract boolean helpers when complex.
    // -------------------------------------------------------------------------
    void ifElseDemo(int score) {
        System.out.println("--- if / else");

        // Guard clause stops early on invalid input (flatter control flow):
        if (score < 0 || score > 100) {
            System.out.println("Invalid score: " + score);
            return;
        }

        // Straightforward branching:
        if (score >= 90) {
            System.out.println("Grade: A");
        } else if (score >= 80) {
            System.out.println("Grade: B");
        } else if (score >= 70) {
            System.out.println("Grade: C");
        } else if (score >= 60) {
            System.out.println("Grade: D");
        } else {
            System.out.println("Grade: F");
        }

        // Nested if: sometimes necessary, but try to keep nesting shallow:
        boolean honors = score >= 90;
        if (honors) {
            if (score >= 98) {
                System.out.println("Honors with distinction");
            } else {
                System.out.println("Honors");
            }
        }
    }

    // -------------------------------------------------------------------------
    // 2) Classic switch statement
    //    - Works with byte/short/char/int, String, enum.
    //    - Fall-through: execution continues until a break/return/throw.
    //      Use intentionally; otherwise remember your breaks.
    // -------------------------------------------------------------------------
    enum Day { MON, TUE, WED, THU, FRI, SAT, SUN }

    void classicSwitchDemo(String command, Day day) {
        System.out.println("--- classic switch");

        // Switch on String
        switch (command.toLowerCase()) {
            case "start":
                System.out.println("Starting...");
                break; // prevent fall-through

            case "stop":
                System.out.println("Stopping...");
                break;

            case "status":
                System.out.println("All systems nominal.");
                break;

            default:
                System.out.println("Unknown command: " + command);
        }

        // Switch on enum, illustrating intentional fall-through grouping:
        switch (day) {
            case MON:
            case TUE:
            case WED:
            case THU:
            case FRI:
                System.out.println("Weekday");
                break;
            case SAT:
            case SUN:
                System.out.println("Weekend");
                break;
        }
    }

    // -------------------------------------------------------------------------
    // 3) Switch expression (Java 14+)
    //    - Arrow labels (no fall-through), returns a value.
    //    - 'yield' is available for block cases needing statements.
    //    - Compile-time exhaustiveness for enums (if all constants covered).
    // -------------------------------------------------------------------------
    int switchExpressionDemo(Day d) {
        System.out.println("--- switch expression");

        // Simple arrow form (no fall-through):
        String vibe = switch (d) {
            case SAT, SUN -> "rest";
            case MON -> "coffee";
            case TUE, WED, THU -> "work";
            case FRI -> "celebrate";
        };
        System.out.println("Vibe: " + vibe);

        // Using a block with 'yield' to compute:
        int energy = switch (d) {
            case SAT, SUN -> 100;
            case MON -> {
                int base = 20;
                int bonus = 5;
                yield base + bonus; // block requires yield for a value
            }
            case TUE, WED, THU -> 60;
            case FRI -> 80;
        };
        System.out.println("Energy: " + energy);
        return energy;
    }

    // -------------------------------------------------------------------------
    // 4) Loops: for / enhanced-for / while / do-while
    //    - Use enhanced-for for read-only iteration of collections/arrays.
    //    - Use classic for when you need index arithmetic or mutation.
    //    - while checks before; do-while checks after (runs at least once).
    //    - break exits current loop; continue jumps to next iteration.
    // -------------------------------------------------------------------------
    void loopsDemo() {
        System.out.println("--- loops");

        // Classic for: index-based access
        int sum = 0;
        for (int i = 0; i < 5; i++) {
            if (i == 2) continue; // skip 2
            sum += i;
        }
        System.out.println("sum (skip 2) = " + sum);

        // Enhanced for: read-only iteration
        for (String s : List.of("alpha", "beta", "gamma")) {
            System.out.print(s + " ");
        }
        System.out.println();

        // while: pre-check
        int n = 3;
        while (n > 0) {
            System.out.print(n + " ");
            n--;
        }
        System.out.println();

        // do-while: post-check (runs at least once)
        int x = 0;
        do {
            System.out.println("do-while ran; x=" + x);
            x++;
        } while (x < 1);
    }

    // -------------------------------------------------------------------------
    // 5) Labeled break/continue
    //    - Useful to escape multiple nested loops cleanly.
    //    - Use sparingly; excessive labels can reduce readability.
    // -------------------------------------------------------------------------
    void labeledBreakContinueDemo() {
        System.out.println("--- labeled break/continue");

        outer:
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 5; col++) {
                if (row == 1 && col == 3) {
                    System.out.println("Breaking outer at row=" + row + ", col=" + col);
                    break outer; // exits both loops
                }
                if (col == 2) {
                    System.out.println("  continue outer at row=" + row + " (skip col>=2)");
                    continue outer; // jump to next iteration of the *outer* loop
                }
                System.out.println("row=" + row + ", col=" + col);
            }
        }
    }

    // -------------------------------------------------------------------------
    // 6) Early exits: return & throw
    //    - Guard clauses (early return) beat deep nesting for most business logic.
    //    - throw jumps to the nearest matching catch (or fails the method).
    // -------------------------------------------------------------------------
    int computeRatio(int a, int b) {
        // Guards
        if (a < 0) throw new IllegalArgumentException("a must be >= 0");
        if (b == 0) return Integer.MAX_VALUE; // sentinel for "infinite"

        // Main path
        int r = a / b;
        if (r < 0) return 0;      // another early exit
        return r;                 // single exit at end is also fine—choose clarity
    }

    // -------------------------------------------------------------------------
    // 7) Exceptions as control: try / catch / finally
    //    - catch can be multi-catch: catch (IOException | IllegalStateException ex) { ... }
    //    - finally always runs (even if return/throw in try), except on System.exit or fatal errors.
    //    - Avoid using exceptions for *normal* branching; reserve for exceptional conditions.
    // -------------------------------------------------------------------------
    void exceptionFlowDemo(String input) {
        System.out.println("--- try/catch/finally");

        try {
            int parsed = Integer.parseInt(input);  // may throw NumberFormatException
            System.out.println("Parsed = " + parsed);
            if (parsed == 13) {
                throw new IllegalStateException("Unlucky!"); // throw as a branch for truly exceptional case
            }
        } catch (NumberFormatException ex) {
            System.out.println("Not a number: " + input);
        } catch (IllegalStateException ex) {
            System.out.println("Special-case caught: " + ex.getMessage());
        } finally {
            System.out.println("finally always runs (cleanup, logging, metrics, etc.)");
        }
    }

    // -------------------------------------------------------------------------
    // 8) Try-with-resources (TWR)
    //    - Any AutoCloseable in the () is closed automatically in reverse order.
    //    - If both try-body and close() throw, the close() exception is *suppressed*.
    // -------------------------------------------------------------------------
    static final class DummyReader extends BufferedReader {
        DummyReader(String data) { super(new StringReader(data)); }
        @Override public void close() throws IOException {
            System.out.println("DummyReader.close()");
            super.close();
        }
    }

    void tryWithResourcesDemo(String data) {
        System.out.println("--- try-with-resources");

        // Auto-close even if an exception occurs
        try (DummyReader reader = new DummyReader(data)) {
            String line = reader.readLine();
            System.out.println("Read line: " + line);
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
            // ex.getSuppressed() would include suppressed close() failures, if any.
        }
    }

    // -------------------------------------------------------------------------
    // 9) Assertions
    //    - Runtime checks for developer assumptions.
    //    - Disabled by default; enable with:  java -ea  (or -enableassertions)
    //    - Never rely on assert for production-critical logic.
    // -------------------------------------------------------------------------
    void assertionsDemo(int[] arr) {
        System.out.println("--- assertions (enable with -ea to see failures)");
        assert arr != null : "arr must not be null";
        assert arr.length >= 1 : "arr must have at least one element";
        // If assertions are disabled (default), these do nothing.
    }

    // -------------------------------------------------------------------------
    // main: run all demos
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        ControlFlow t = new ControlFlow();

        t.ifElseDemo(94);
        t.ifElseDemo(-5);             // triggers guard
        t.classicSwitchDemo("start", Day.MON);
        t.classicSwitchDemo("noop", Day.SUN);
        t.switchExpressionDemo(Day.FRI);
        t.loopsDemo();
        t.labeledBreakContinueDemo();
        System.out.println("--- computeRatio");
        System.out.println("ratio(10,2)=" + t.computeRatio(10, 2));
        System.out.println("ratio(10,0)=" + t.computeRatio(10, 0));

        t.exceptionFlowDemo("123");
        t.exceptionFlowDemo("NaN");
        t.exceptionFlowDemo("13");

        t.tryWithResourcesDemo("hello, world");

        t.assertionsDemo(new int[]{1,2,3});
    }
}
