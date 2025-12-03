package com.apps.launcher;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Scans the runtime classpath for top-level classes that expose a conventional main method.
 */
public final class DemoScanner {
    private static final Predicate<Path> CLASS_FILE = path -> path.getFileName().toString().endsWith(".class");

    private DemoScanner() {
    }

    public static List<DemoDescriptor> discover() {
        Set<String> classNames = new TreeSet<>();
        for (String entry : System.getProperty("java.class.path", "").split(java.io.File.pathSeparator)) {
            if (entry == null || entry.isBlank()) {
                continue;
            }
            Path root = Paths.get(entry.trim());
            if (!Files.isDirectory(root)) {
                continue; // skip JARs; the project demos live in directories
            }
            discoverFromDirectory(root, classNames);
        }
        if (classNames.isEmpty()) {
            return Collections.emptyList();
        }
        List<DemoDescriptor> result = new ArrayList<>();
        ClassLoader loader = DemoScanner.class.getClassLoader();
        for (String className : classNames) {
            try {
                Class<?> type = Class.forName(className, false, loader);
                Method main = findMainMethod(type);
                if (main != null) {
                    result.add(new DemoDescriptor(className, main));
                }
            } catch (ClassNotFoundException | NoClassDefFoundError ignored) {
                // class disappeared between compilation and runtime; ignore
            }
        }
        return result;
    }

    private static void discoverFromDirectory(Path root, Set<String> collector) {
        try (Stream<Path> files = Files.walk(root)) {
            files.filter(CLASS_FILE)
                    .map(path -> asClassName(root, path))
                    .filter(name -> name != null && !name.contains("$"))
                    .forEach(collector::add);
        } catch (IOException ignored) {
            // I/O errors are non-fatal for discovery
        }
    }

    private static String asClassName(Path root, Path classFile) {
        Path relative = root.relativize(classFile);
        if (relative.startsWith("META-INF")) {
            return null;
        }
        String candidate = relative.toString();
        if (!candidate.endsWith(".class")) {
            return null;
        }
        String withoutExtension = candidate.substring(0, candidate.length() - ".class".length());
        return withoutExtension.replace('/', '.').replace('\\', '.');
    }

    private static Method findMainMethod(Class<?> type) {
        try {
            Method method = type.getDeclaredMethod("main", String[].class);
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                return method;
            }
        } catch (NoSuchMethodException ignored) {
            // no-op
        }
        return null;
    }
}
