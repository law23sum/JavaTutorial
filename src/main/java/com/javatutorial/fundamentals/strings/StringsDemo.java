package com.javatutorial.fundamentals.strings;

/**
 * <h2>Strings</h2>
 * {@link String} is an <b>immutable</b> reference type. Every "modification"
 * actually returns a brand new String object. The original is unchanged.
 *
 * <p>Common methods you should be fluent in:
 * <pre>
 *   length(), isEmpty(), charAt(i)
 *   substring(start), substring(start, end)
 *   equals(s), equalsIgnoreCase(s), compareTo(s)
 *   toUpperCase(), toLowerCase(), trim()
 *   replace(char,char), replace(seq,seq)
 *   contains(seq), startsWith(p), endsWith(s), indexOf(c)
 *   split(regex), String.join(delim, parts...)
 * </pre>
 */
public class StringsDemo {

    public static void main(String[] args) {
        String s = "Hello, World";

        System.out.println("length       : " + s.length());
        System.out.println("charAt(0)    : " + s.charAt(0));
        System.out.println("substring(7) : " + s.substring(7));
        System.out.println("substring(0,5)+" + s.substring(0, 5));
        System.out.println("upper        : " + s.toUpperCase());
        System.out.println("replace 'l'  : " + s.replace('l', 'L'));
        System.out.println("contains W   : " + s.contains("World"));

        // Equality: content vs reference
        String a = "hi";
        String b = new String("hi");
        System.out.println("a == b       : " + (a == b));           // false (b is heap-fresh)
        System.out.println("a.equals(b)  : " + a.equals(b));        // true

        // Joining:
        System.out.println("join         : " + String.join(", ", "a", "b", "c"));
    }
}
