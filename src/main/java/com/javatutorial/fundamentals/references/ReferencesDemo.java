package com.javatutorial.fundamentals.references;

import java.util.Arrays;

/**
 * <h2>Reference Types</h2>
 * Anything that points to an object on the heap: classes, interfaces, arrays,
 * enums, and parameterized (generic) types. The variable holds a <i>reference</i>
 * (a pointer), not the object itself.
 *
 * <p>Key consequences:
 * <ul>
 *   <li>{@code ==} compares references (same object?), {@code .equals()} compares content.</li>
 *   <li>{@code null} is a valid reference value — calling a method on it throws NPE.</li>
 *   <li>Passing a reference to a method shares the underlying object (mutations are visible).</li>
 * </ul>
 */
public class ReferencesDemo {

    public static void main(String[] args) {
        // Same content, different objects on the heap:
        String a = new String("hello");
        String b = new String("hello");
        System.out.println("a == b      : " + (a == b));        // false: distinct refs
        System.out.println("a.equals(b) : " + a.equals(b));     // true:  same content

        // String literals are interned, so references can coincide:
        String c = "hello";
        String d = "hello";
        System.out.println("c == d      : " + (c == d));        // true (interned literal pool)

        // null reference:
        String missing = null;
        System.out.println("missing == null : " + (missing == null));

        // Pass-by-value of a reference: the *reference* is copied, but it points
        // at the same array, so mutations through it are visible to the caller.
        int[] nums = {1, 2, 3};
        mutate(nums);
        System.out.println("nums after mutate(): " + Arrays.toString(nums)); // [99, 2, 3]
    }

    private static void mutate(int[] arr) { arr[0] = 99; }
}
