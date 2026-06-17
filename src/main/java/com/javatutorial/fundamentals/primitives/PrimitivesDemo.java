package com.javatutorial.fundamentals.primitives;

/**
 * <h2>Primitive Types</h2>
 * The eight raw, value-based building blocks of Java. Stored directly in memory
 * (on the stack/locals or inline in objects) — never on the heap as objects.
 *
 * <pre>
 *   Integer family : byte, short, int, long
 *   Floating point : float, double
 *   Character      : char
 *   Truth value    : boolean
 * </pre>
 *
 * Primitives have a fixed size, a default zero-like value, and are passed
 * <i>by value</i>. They have NO methods — to call methods you wrap them in
 * the corresponding {@link Integer}/{@link Double}/etc. wrapper class.
 */
public class PrimitivesDemo {

    public static void main(String[] args) {
        // Integer family — sizes and ranges:
        byte  b = 127;                  // 8-bit  : [-128, 127]
        short s = 32_000;               // 16-bit : [-32_768, 32_767]
        int   i = 1_000_000;            // 32-bit : ~±2.1 billion
        long  l = 9_000_000_000L;       // 64-bit : note the L suffix

        // Floating point:
        float  f = 3.14f;               // 32-bit : note the f suffix
        double d = 3.141592653589793;   // 64-bit : the default for decimals

        // Character & boolean:
        char    c    = 'A';             // 16-bit unsigned Unicode code unit
        boolean flag = true;            // true | false (only)

        System.out.println("byte=" + b + " short=" + s + " int=" + i + " long=" + l);
        System.out.println("float=" + f + " double=" + d);
        System.out.println("char=" + c + " (numeric value=" + (int) c + ")");
        System.out.println("boolean=" + flag);

        // Pass-by-value: increment(x) does NOT mutate the caller's variable.
        int x = 10;
        increment(x);
        System.out.println("x after increment(): " + x); // still 10
    }

    private static void increment(int n) { n++; }
}
