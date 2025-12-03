package com.apps.launcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * Simple value object describing a class that exposes a conventional Java main method.
 */
public final class DemoDescriptor implements Comparable<DemoDescriptor> {
    private final String className;
    private final Method mainMethod;

    DemoDescriptor(String className, Method mainMethod) {
        this.className = Objects.requireNonNull(className, "className");
        this.mainMethod = Objects.requireNonNull(mainMethod, "mainMethod");
    }

    public String className() {
        return className;
    }

    public String simpleName() {
        int idx = className.lastIndexOf('.');
        return idx >= 0 ? className.substring(idx + 1) : className;
    }

    public String packageName() {
        int idx = className.lastIndexOf('.');
        return idx >= 0 ? className.substring(0, idx) : "";
    }

    public void run(String[] args) {
        try {
            String[] forwarded = args == null ? new String[0] : Arrays.copyOf(args, args.length);
            mainMethod.invoke(null, (Object) forwarded);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            Throwable cause = ex instanceof InvocationTargetException && ex.getCause() != null
                    ? ex.getCause()
                    : ex;
            throw new DemoExecutionException("Failed to execute demo " + className, cause);
        }
    }

    @Override
    public int compareTo(DemoDescriptor other) {
        return this.className.compareTo(other.className);
    }
}
