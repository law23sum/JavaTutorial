package com.tutorial.foundation;

import java.util.*;

/**
 * One-file tour of Java **Wrapper Classes** — object equivalents of primitives.
 * Demonstrates boxing, unboxing, comparison, null safety, and numeric conversions.
 * Target: Java 17+
 */
public class ReferenceType {

    // ───────────────────────────── 1) WRAPPER BASICS ─────────────────────────────
    static void wrapperBasics() {
        Integer i = Integer.valueOf(42);
        Double d = Double.valueOf(3.14159);
        Character c = Character.valueOf('π');
        Boolean b = Boolean.TRUE;

        System.out.printf("[Basics] Integer=%d Double=%.3f Character=%s Boolean=%b%n", i, d, c, b);
    }

    // ───────────────────────────── 2) AUTOBOXING & UNBOXING ─────────────────────────────
    static void autoboxingAndUnboxing() {
        int primitive = 10;
        Integer boxed = primitive;    // autoboxing
        int unboxed = boxed;          // unboxing

        System.out.printf("[Autobox] primitive=%d boxed=%d unboxed=%d%n", primitive, boxed, unboxed);
    }

    // ───────────────────────────── 3) NULL SAFETY ─────────────────────────────
    static void nullSafety() {
        Integer safe = 100;
        Integer unsafe = null;

        try {
            int bad = unsafe; // ❌ unboxing null
        } catch (NullPointerException e) {
            System.out.println("[NullSafety] NullPointerException caught when unboxing null Integer");
        }

        int fallback = Optional.ofNullable(unsafe).orElse(-1);
        System.out.printf("[NullSafety] Safe=%d Fallback=%d%n", safe, fallback);
    }

    // ───────────────────────────── 4) VALUE COMPARISON vs IDENTITY ─────────────────────────────
    static void comparison() {
        Integer a = 127, b = 127;     // cached
        Integer x = 128, y = 128;     // new objects
        System.out.printf("[Compare] 127==127:%b | 128==128:%b | equals128:%b%n", (a == b), (x == y), x.equals(y));
    }

    // ───────────────────────────── 5) PARSING & STRING CONVERSION ─────────────────────────────
    static void parsingAndString() {
        String numStr = "256";
        Integer parsed = Integer.parseInt(numStr);
        String back = parsed.toString();

        System.out.printf("[Parsing] parsed=%d back=%s%n", parsed, back);
    }

    // ───────────────────────────── 6) ARITHMETIC WITH WRAPPERS ─────────────────────────────
    static void arithmetic() {
        Integer a = 10, b = 20;
        Integer sum = a + b; // autounboxed -> added -> reboxed
        Double result = sum.doubleValue() / 3.0;
        System.out.printf("[Arithmetic] a=%d b=%d sum=%d result=%.2f%n", a, b, sum, result);
    }

    // ───────────────────────────── 7) TYPE CONVERSION & METHODS ─────────────────────────────
    static void conversion() {
        Double d = 12.75;
        int rounded = d.intValue();
        long longVal = d.longValue();
        System.out.printf("[Conversion] double=%.2f int=%d long=%d%n", d, rounded, longVal);
    }

    // ───────────────────────────── 8) WRAPPER CLASS UTILITY METHODS ─────────────────────────────
    static void utilities() {
        int max = Integer.max(15, 40);
        double pow = Math.pow(2, 8);
        boolean digit = Character.isDigit('9');
        boolean letter = Character.isLetter('π');
        System.out.printf("[Utilities] max=%d pow=%.0f digit=%b letter=%b%n", max, pow, digit, letter);
    }

    // ───────────────────────────── 9) WRAPPER ARRAYS ─────────────────────────────
    static void wrapperArrays() {
        Integer[] nums = {1, 2, 3, 4};
        int total = 0;
        for (Integer n : nums) total += n; // unboxed in loop
        System.out.printf("[Arrays] nums=%s sum=%d%n", Arrays.toString(nums), total);
    }

    // ───────────────────────────── 10) GENERIC WRAPPERS ─────────────────────────────
    static class Box<T> {
        private T value;

        void set(T v) {
            value = v;
        }

        T get() {
            return value;
        }
    }

    static void genericWrappers() {
        Box<Integer> iBox = new Box<>();
        Box<Double> dBox = new Box<>();
        iBox.set(42);
        dBox.set(3.1415);
        System.out.printf("[Generics] iBox=%d dBox=%.4f%n", iBox.get(), dBox.get());
    }

    // ───────────────────────────── 11) WRAPPERS TABLE ─────────────────────────────
    static void printWrapperTable() {
        System.out.println("\n| Primitive | Wrapper     |");
        System.out.println("| ---------- | ----------- |");
        System.out.println("| int        | Integer     |");
        System.out.println("| double     | Double      |");
        System.out.println("| char       | Character   |");
        System.out.println("| boolean    | Boolean     |");
        System.out.println("| long       | Long        |");
        System.out.println("| float      | Float       |");
        System.out.println("| byte       | Byte        |");
        System.out.println("| short      | Short       |");
    }

    // ───────────────────────────── MAIN ─────────────────────────────
    public static void main(String[] args) {
        wrapperBasics();
        autoboxingAndUnboxing();
        nullSafety();
        comparison();
        parsingAndString();
        arithmetic();
        conversion();
        utilities();
        wrapperArrays();
        genericWrappers();
        printWrapperTable();
    }
}
