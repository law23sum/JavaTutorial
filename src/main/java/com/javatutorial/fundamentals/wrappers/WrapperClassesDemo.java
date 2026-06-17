package com.javatutorial.fundamentals.wrappers;

/**
 * <h2>Wrapper Classes</h2>
 * Each primitive has a matching object wrapper:
 * <pre>
 *   byte→Byte  short→Short  int→Integer  long→Long
 *   float→Float  double→Double  char→Character  boolean→Boolean
 * </pre>
 *
 * Wrappers let primitives play in the object world (collections, generics,
 * nullable values, utility methods like {@link Integer#parseInt}).
 *
 * <h3>Two gotchas to know cold</h3>
 * <ol>
 *   <li><b>Autoboxing/unboxing</b>: {@code Integer x = 5; x++;} silently
 *       unboxes, increments, and re-boxes — creating a new object.</li>
 *   <li><b>Integer cache</b>: values in <code>[-128, 127]</code> are cached, so
 *       {@code ==} on two boxed 100s is {@code true} but {@code ==} on two
 *       boxed 200s is {@code false}. Always use {@code .equals()}.</li>
 * </ol>
 */
public class WrapperClassesDemo {

    public static void main(String[] args) {
        // Boxing/unboxing:
        int      primitive = 10;
        Integer  boxed     = primitive;          // autoboxing
        int      unboxed   = boxed;              // unboxing
        Integer  explicit  = Integer.valueOf(10);
        System.out.println("boxed=" + boxed + " unboxed=" + unboxed + " explicit=" + explicit);

        // Integer cache pitfall:
        Integer cachedA = 100,  cachedB = 100;   // inside cache window → same object
        Integer bigA    = 200,  bigB    = 200;   // outside cache window → new objects
        System.out.println("100 == 100 : " + (cachedA == cachedB));        // true
        System.out.println("200 == 200 : " + (bigA == bigB));              // false (!)
        System.out.println("200.equals(200) : " + bigA.equals(bigB));      // true ✅

        // Useful utility methods:
        System.out.println("max(3,5)        = " + Integer.max(3, 5));
        System.out.println("compare(3,5)    = " + Integer.compare(3, 5));
        System.out.println("bitCount(7)     = " + Integer.bitCount(7));     // 3
        System.out.println("parseInt(\"FF\",16) = " + Integer.parseInt("FF", 16));
        System.out.println("toBinaryString(10) = " + Integer.toBinaryString(10));

        // String <-> int conversions:
        int parsed = Integer.parseInt("123");
        String back = String.valueOf(parsed);
        System.out.println("parsed=" + parsed + " back=" + back);
    }
}
