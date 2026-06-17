package com.javatutorial.fundamentals.strings;

/**
 * <h2>StringBuilder</h2>
 * Because {@link String} is immutable, doing repeated {@code +} concatenation
 * inside a loop creates a fresh String object every iteration → quadratic time
 * and lots of GC pressure.
 *
 * <p>Use {@link StringBuilder} when you build a String incrementally:
 * <pre>
 *   StringBuilder sb = new StringBuilder();
 *   for (char c : chars) sb.append(c);
 *   String result = sb.toString();
 * </pre>
 */
public class StringBuilderDemo {

    public static void main(String[] args) {
        char[] letters = {'j', 'a', 'v', 'a'};

        StringBuilder sb = new StringBuilder();
        for (char c : letters) sb.append(Character.toUpperCase(c));

        sb.insert(0, "<<").append(">>");          // builders are mutable
        System.out.println(sb.toString());        // <<JAVA>>
        System.out.println("length: " + sb.length());
        System.out.println("reversed: " + sb.reverse());
    }
}
