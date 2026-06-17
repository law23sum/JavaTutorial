package com.javatutorial.algorithms.strings;

import java.util.HashMap;
import java.util.Map;

/**
 * <h2>Longest Substring Without Repeating Characters</h2>
 * Sliding window with two pointers and a {@code char -> lastIndex} map. When
 * we see a character already inside the window, jump {@code left} just past
 * its previous position.
 *
 * <pre>
 *   Time : O(n)   Space : O(min(n, alphabet))
 * </pre>
 */
public class LongestSubstringNoRepeat {

    public static int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> lastIndex = new HashMap<>();
        int left = 0, best = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (lastIndex.containsKey(c) && lastIndex.get(c) >= left) {
                left = lastIndex.get(c) + 1;
            }
            lastIndex.put(c, right);
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring("abcabcbb")); // 3
        System.out.println(lengthOfLongestSubstring("bbbbb"));    // 1
        System.out.println(lengthOfLongestSubstring("pwwkew"));   // 3
    }
}
