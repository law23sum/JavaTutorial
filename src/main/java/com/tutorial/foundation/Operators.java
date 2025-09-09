package com.tutorial.foundation;

import java.math.BigInteger;

/**
 * Java Operators — compact, runnable tour with pragmatic notes:
 *
 * Groups covered
 *  1) Unary: +  -  !  ~  ++  --
 *  2) Arithmetic: +  -  *  /  %  (+ also concatenates Strings)
 *  3) Assignment & Compound: =  +=  -=  *=  /=  %=  &=  |=  ^=  <<=  >>=  >>>=
 *  4) Relational & Equality: <  <=  >  >=  ==  !=  (with primitive vs object notes)
 *  5) Logical (boolean): &&  ||  !   vs non-short-circuit &  |
 *  6) Bitwise (integral): &  |  ^  ~
 *  7) Shifts (integral): <<  >>  >>>  (signed vs unsigned)
 *  8) Ternary: condition ? ifTrue : ifFalse
 *  9) instanceof (pattern matching), precedence pitfalls, numeric promotion, overflow
 *
 * Build & run (JDK 17+ recommended):
 *   javac com/tutorial/foundation/OperatorsTour.java && java com.tutorial.foundation.OperatorsTour
 */
public class Operators {

    // ----------------------------- 1) Unary -----------------------------------
    void unaryDemo() {
        int x = 5;
        int pre  = ++x;     // pre-increment: x becomes 6, then expression is 6
        int post = x++;     // post-increment: expression is 6, then x becomes 7

        int y = -x;         // unary minus flips sign
        int z = +y;         // unary plus is a no-op for numbers (but triggers numeric promotion in some contexts)

        boolean b = false;
        boolean nb = !b;    // logical NOT: true

        int mask = 0b0101_1100;
        int notMask = ~mask; // bitwise NOT (ones' complement)

        System.out.println("--- Unary");
        System.out.println("pre=" + pre + ", post=" + post + ", final x=" + x);
        System.out.println("y=" + y + ", z=" + z);
        System.out.println("!false=" + nb);
        System.out.println("~0b0101_1100 = " + Integer.toBinaryString(notMask));
    }

    // -------------------------- 2) Arithmetic ---------------------------------
    void arithmeticDemo() {
        int a = 7, b = 3;
        int sum = a + b;             // 10
        int diff = a - b;            // 4
        int prod = a * b;            // 21
        int quo = a / b;             // 2 (integer division truncates toward zero)
        int rem = a % b;             // 1

        double dquo = 7.0 / 3;       // 2.333...
        double drem = 7.0 % 3.0;     // 1.0

        // String + (concatenation) is left-associative:
        String s = "Ans=" + a + b;       // "Ans=73"
        String s2 = "Ans=" + (a + b);    // "Ans=10"

        // NaN corner: comparisons with NaN are false (except !=)
        double nan = Double.NaN;
        System.out.println("--- Arithmetic");
        System.out.println("sum=" + sum + ", diff=" + diff + ", prod=" + prod + ", quo=" + quo + ", rem=" + rem);
        System.out.println("dquo=" + dquo + ", drem=" + drem);
        System.out.println("concat: " + s + " | " + s2);
        System.out.println("nan==nan? " + (nan == nan) + ", nan!=nan? " + (nan != nan));
    }

    // ------------------- 3) Assignment & Compound Ops -------------------------
    void assignmentDemo() {
        // Compound ops auto-cast on the LHS type (careful: may truncate!)
        short s = 1;
        // s = s + 1;   // compile error (int result assigned to short)
        s += 1;         // OK: compound does implicit cast after add
        // Equivalent to: s = (short)(s + 1);

        int x = 5;
        x *= 3 + 2;     // same as x = x * (3 + 2) → 25 due to precedence of * over =
        int y = 1;
        y <<= 2;        // y = y << 2 → 4

        System.out.println("--- Assignment & Compound");
        System.out.println("short s after s+=1 -> " + s);
        System.out.println("x after x*=3+2 -> " + x);
        System.out.println("y after y<<=2 -> " + y);
    }

    // --------------- 4) Relational & Equality (== vs equals) ------------------
    void relationalEqualityDemo() {
        int a = 10, b = 20;
        System.out.println("--- Relational & Equality");
        System.out.println("a<b? " + (a < b) + ", a>=b? " + (a >= b) + ", a==b? " + (a == b));

        // For objects: '==' compares references; equals() compares logical content (when overridden)
        String p = "hello";
        String q = "hello";
        String r = new String("hello");  // distinct object

        System.out.println("p==q? " + (p == q));           // true (interned literal)
        System.out.println("p==r? " + (p == r));           // false (different object)
        System.out.println("p.equals(r)? " + p.equals(r)); // true (same characters)
    }

    // ------------------ 5) Logical vs Bitwise on booleans ---------------------
    void logicalDemo() {
        // && and || are short-circuiting; & and | (on booleans) evaluate both sides.
        boolean leftTrue = true;
        boolean leftFalse = false;

        boolean shortCircuit = leftTrue || sideEffect("OR: right evaluated?"); // right NOT evaluated
        boolean nonShort = leftTrue | sideEffect("OR(|): right evaluated?");   // right IS evaluated

        boolean scAnd = leftFalse && sideEffect("AND: right evaluated?");      // right NOT evaluated
        boolean nonScAnd = leftFalse & sideEffect("AND(&): right evaluated?"); // right IS evaluated

        System.out.println("--- Logical (short-circuit vs non)");
        System.out.println("shortCircuit( || )=" + shortCircuit + ", nonShort( | )=" + nonShort);
        System.out.println("scAnd( && )=" + scAnd + ", nonScAnd( & )=" + nonScAnd);
    }

    private boolean sideEffect(String label) {
        System.out.println("  side-effect -> " + label);
        return true;
    }

    // -------------------- 6) Bitwise: &  |  ^  ~  (integral) ------------------
    void bitwiseDemo() {
        int a = 0b0101_1100; // 0x5C
        int b = 0b0011_0011; // 0x33
        int and = a & b;     // 0b0001_0000
        int or  = a | b;     // 0b0111_1111
        int xor = a ^ b;     // bits differ → 1
        int not = ~a;        // invert all bits

        System.out.println("--- Bitwise");
        System.out.println("a:   " + bin(a));
        System.out.println("b:   " + bin(b));
        System.out.println("a&b: " + bin(and));
        System.out.println("a|b: " + bin(or));
        System.out.println("a^b: " + bin(xor));
        System.out.println("~a : " + bin(not));

        // Masking example: isolate lower 4 bits
        int lower4 = a & 0b1111;
        System.out.println("lower4(a)=" + bin(lower4));
    }

    // ------------------ 7) Shifts: <<  >>  >>>  (signed/unsigned) --------------
    void shiftDemo() {
        int pos = 0b0000_0000_0000_0000_0000_0000_1001_0110;         // 150
        int neg = -150;

        int left = pos << 2;   // multiply by 4
        int rightSigned = neg >> 3; // arithmetic shift keeps sign (fills with sign bit)
        int rightUnsigned = neg >>> 3; // logical shift fills with zeros

        System.out.println("--- Shifts");
        System.out.println("pos      : " + bin(pos));
        System.out.println("pos<<2   : " + bin(left));
        System.out.println("neg      : " + bin(neg));
        System.out.println("neg>>3   : " + bin(rightSigned));
        System.out.println("neg>>>3  : " + bin(rightUnsigned));
    }

    // ------------------------- 8) Ternary operator -----------------------------
    void ternaryDemo() {
        int n = 7;
        String parity = (n % 2 == 0) ? "even" : "odd";
        // Ternary is an *expression* (has a value) — useful inside initializers/arguments.
        System.out.println("--- Ternary");
        System.out.println("n=" + n + " is " + parity);
    }

    // ------------- 9) instanceof (pattern matching) + precedence ----------------
    void patternInstanceofAndPrecedenceDemo() {
        Object obj = "Ada Lovelace";
        // Pattern matching (Java 16+/17+): binds a typed pattern variable if the check succeeds.
        if (obj instanceof String s && s.length() > 3) {
            System.out.println("--- instanceof (pattern)");
            System.out.println("String of len " + s.length() + ": " + s.toUpperCase());
        }

        // Precedence pitfall: + binds more tightly than << ? No — SHIFT has lower precedence than +/*.
        int a = 3, b = 5;
        int p1 = a + b << 2;        // interpreted as (a + b) << 2  → (8) << 2 = 32
        int p2 = a + (b << 2);      // 3 + 20 = 23 (different)
        System.out.println("--- Precedence");
        System.out.println("(a + b) << 2 = " + p1 + " | a + (b << 2) = " + p2);

        // Another precedence note: assignment (=) is right-associative
        int x, y;
        x = y = 10; // y=10, then x=y
        System.out.println("x=" + x + ", y=" + y);
    }

    // ----------------- Numeric promotion & overflow notes ----------------------
    void promotionAndOverflowDemo() {
        // byte/short/char are promoted to int for arithmetic
        byte b1 = 100, b2 = 27;
        int sum = b1 + b2;          // result is int, not byte
        // byte bsum = b1 + b2;     // compile error
        byte bsumCast = (byte)(b1 + b2); // narrows (may overflow/truncate)

        System.out.println("--- Numeric promotion & overflow");
        System.out.println("sum(int)=" + sum + ", cast back to byte=" + bsumCast);

        // Overflow with primitives wraps around (two's complement)
        int max = Integer.MAX_VALUE;
        int wrapped = max + 1; // overflows to Integer.MIN_VALUE
        System.out.println("overflow wrap: " + max + " + 1 = " + wrapped);

        // Use Math.*Exact to detect overflow
        try {
            int exact = Math.addExact(max, 1);
            System.out.println("addExact result=" + exact); // never reached
        } catch (ArithmeticException ex) {
            System.out.println("addExact threw: " + ex);
        }

        // BigInteger for arbitrary precision arithmetic (no overflow)
        BigInteger bigA = new BigInteger("9".repeat(50));
        BigInteger bigB = new BigInteger("1".repeat(50));
        System.out.println("BigInteger sum has digits=" + bigA.add(bigB).toString().length());
    }

    // ------------------------------- Helpers -----------------------------------
    private static String bin(int v) {
        // 32-bit padded binary string for clear visualization
        String s = Integer.toBinaryString(v);
        if (s.length() < 32) s = "0".repeat(32 - s.length()) + s;
        return s.substring(0, 8) + "_" + s.substring(8, 16) + "_" + s.substring(16, 24) + "_" + s.substring(24);
    }

    // --------------------------------- main ------------------------------------
    public static void main(String[] args) {
        Operators t = new Operators();
        t.unaryDemo();
        t.arithmeticDemo();
        t.assignmentDemo();
        t.relationalEqualityDemo();
        t.logicalDemo();
        t.bitwiseDemo();
        t.shiftDemo();
        t.ternaryDemo();
        t.patternInstanceofAndPrecedenceDemo();
        t.promotionAndOverflowDemo();
    }
}
