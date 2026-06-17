package com.javatutorial.algorithms.strings;

import java.util.Arrays;

/**
 * <h2>Valid Anagram</h2>
 * {@code t} is an anagram of {@code s} iff their multisets of characters
 * match. Sorting both and comparing is the simplest correct approach.
 *
 * <pre>
 *   Time : O(n log n)   Space : O(n)
 * </pre>
 */
public class ValidAnagram {

    public static boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        char[] a = s.toCharArray();
        char[] b = t.toCharArray();
        Arrays.sort(a);
        Arrays.sort(b);
        return Arrays.equals(a, b);
    }

    public static void main(String[] args) {
        System.out.println(isAnagram("anagram", "nagaram")); // true
        System.out.println(isAnagram("rat",     "car"));     // false
    }
}
