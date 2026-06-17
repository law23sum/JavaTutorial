package com.javatutorial.algorithms.strings;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongestSubstringNoRepeatTest {
    @Test void abcabcbb() { assertEquals(3, LongestSubstringNoRepeat.lengthOfLongestSubstring("abcabcbb")); }
    @Test void bbbbb()    { assertEquals(1, LongestSubstringNoRepeat.lengthOfLongestSubstring("bbbbb")); }
    @Test void pwwkew()   { assertEquals(3, LongestSubstringNoRepeat.lengthOfLongestSubstring("pwwkew")); }
    @Test void empty()    { assertEquals(0, LongestSubstringNoRepeat.lengthOfLongestSubstring("")); }
}
