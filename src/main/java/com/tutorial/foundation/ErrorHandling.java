package com.tutorial.foundation;

import java.io.IOException;

/**
 * Java Error Handling — “one-file tour” with explanations.
 *
 * Concepts covered
 *  1) try–catch–finally
 *  2) throw
 *  3) throws
 *  4) Multi-catch
 *  5) Try-with-resources
 *  6) Custom exceptions (checked vs unchecked)
 */
public class ErrorHandling {

    // -------------------------------------------------------------------------
    // 1) try–catch–finally
    // -------------------------------------------------------------------------
    static void tryCatchFinallyDemo() {
        System.out.println("=== try–catch–finally");

        try {
            int x = 10 / 0; // triggers ArithmeticException
            System.out.println("unreachable: " + x);
        } catch (ArithmeticException ex) {
            System.out.println("Caught ArithmeticException: " + ex.getMessage());
        } finally {
            System.out.println("finally runs no matter what");
        }
    }

    // -------------------------------------------------------------------------
    // 2) throw — explicitly create and raise an exception
    // -------------------------------------------------------------------------
    static void throwDemo(int age) {
        System.out.println("\n=== throw");
        if (age < 18) {
            throw new IllegalArgumentException("Age must be >= 18");
        }
        System.out.println("Valid age: " + age);
    }

    // -------------------------------------------------------------------------
    // 3) throws — method declares checked exception
    // -------------------------------------------------------------------------
    static void throwsDemo() throws IOException {
        System.out.println("\n=== throws");
        throw new IOException("Simulated I/O problem");
    }

    // -------------------------------------------------------------------------
    // 4) Multi-catch
    // -------------------------------------------------------------------------
    static void multiCatchDemo(String s) {
        System.out.println("\n=== multi-catch");

        try {
            if (s.equals("null")) {
                throw new NullPointerException("demo");
            } else if (s.equals("io")) {
                throw new IOException("demo");
            }
        } catch (IOException | NullPointerException ex) {
            System.out.println("Caught in multi-catch: " + ex);
        }
    }

    // -------------------------------------------------------------------------
    // 5) Try-with-resources
    // -------------------------------------------------------------------------
    static void tryWithResourcesDemo() {
        System.out.println("\n=== try-with-resources");

        class Dummy implements AutoCloseable {
            public void action() { System.out.println("action inside Dummy"); }
            @Override public void close() {
                System.out.println("Dummy.close() auto-called");
            }
        }

        try (Dummy d = new Dummy()) {
            d.action();
        }
    }

    // -------------------------------------------------------------------------
    // 6) Custom Exceptions
    //    - Define your own to express domain-specific errors.
    //    - Checked: extend Exception (must handle or declare).
    //    - Unchecked: extend RuntimeException (optional to handle).
    // -------------------------------------------------------------------------
    // Checked custom exception
    static class InvalidAgeException extends Exception {
        public InvalidAgeException(String msg) { super(msg); }
    }

    // Unchecked custom exception
    static class DataCorruptionException extends RuntimeException {
        public DataCorruptionException(String msg) { super(msg); }
    }

    static void customExceptionsDemo() {
        System.out.println("\n=== custom exceptions");

        // Checked: must declare or catch
        try {
            validateAge(15);
        } catch (InvalidAgeException e) {
            System.out.println("Handled custom checked exception: " + e.getMessage());
        }

        // Unchecked: no compiler requirement to handle
        try {
            corruptData();
        } catch (DataCorruptionException e) {
            System.out.println("Handled custom unchecked exception: " + e.getMessage());
        }
    }

    static void validateAge(int age) throws InvalidAgeException {
        if (age < 18) throw new InvalidAgeException("Age < 18 not allowed");
        System.out.println("Age OK: " + age);
    }

    static void corruptData() {
        throw new DataCorruptionException("Database checksum mismatch!");
    }

    // -------------------------------------------------------------------------
    // main — run all demos
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        tryCatchFinallyDemo();

        try {
            throwDemo(15);
        } catch (IllegalArgumentException ex) {
            System.out.println("Handled throw: " + ex.getMessage());
        }

        try {
            throwsDemo();
        } catch (IOException ex) {
            System.out.println("Handled throws: " + ex.getMessage());
        }

        multiCatchDemo("io");
        multiCatchDemo("null");
        tryWithResourcesDemo();
        customExceptionsDemo();
    }
}
