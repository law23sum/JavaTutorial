package com.javatutorial.fundamentals.operators;

/**
 * <h2>Operators</h2>
 * Quick tour of arithmetic, comparison, logical, bitwise, and assignment
 * operators that show up constantly in interview-style code.
 */
public class OperatorsDemo {

    public static void main(String[] args) {
        // Arithmetic:
        System.out.println("7 / 2  = " + (7 / 2));    // 3 (integer division)
        System.out.println("7 % 2  = " + (7 % 2));    // 1 (modulo)
        System.out.println("7.0/2  = " + (7.0 / 2));  // 3.5

        // Comparison + logical:
        int n = 42;
        boolean inRange = n >= 0 && n < 100;
        System.out.println("inRange: " + inRange);

        // Short-circuit avoids divide-by-zero:
        int d = 0;
        boolean safe = d != 0 && (10 / d) > 0;
        System.out.println("safe   : " + safe);

        // Bitwise:
        System.out.println("5 & 3 = " + (5 & 3));     // AND -> 1
        System.out.println("5 | 3 = " + (5 | 3));     // OR  -> 7
        System.out.println("5 ^ 3 = " + (5 ^ 3));     // XOR -> 6
        System.out.println("~5    = " + (~5));        // NOT -> -6
        System.out.println("1 << 4= " + (1 << 4));    // 16

        // Compound assignment:
        int x = 10;
        x += 5; x *= 2; x -= 1;
        System.out.println("x = " + x);               // 29
    }
}
