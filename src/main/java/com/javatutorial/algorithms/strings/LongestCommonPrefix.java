package com.javatutorial.algorithms.strings;

import java.util.Arrays;

/**
 * <h2>Longest Common Prefix</h2>
 * Sort the strings lexicographically; the longest common prefix of <i>all</i>
 * the strings is just the common prefix between the first and the last.
 *
 * <pre>
 *   Time : O(n log n + L)   Space : O(1) extra
 * </pre>
 */
public class LongestCommonPrefix {

    public static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        Arrays.sort(strs);
        String first = strs[0];
        String last  = strs[strs.length - 1];
        int i = 0;
        while (i < first.length() && i < last.length() && first.charAt(i) == last.charAt(i)) i++;
        return first.substring(0, i);
    }

    public static void main(String[] args) {
        System.out.println(longestCommonPrefix(new String[]{"flower", "flow", "flight"})); // "fl"
        System.out.println(longestCommonPrefix(new String[]{"dog", "racecar", "car"}));    // ""
    }
}
