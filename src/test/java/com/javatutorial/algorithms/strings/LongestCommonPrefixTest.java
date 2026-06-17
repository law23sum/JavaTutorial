package com.javatutorial.algorithms.strings;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongestCommonPrefixTest {
    @Test void commonFl()   { assertEquals("fl", LongestCommonPrefix.longestCommonPrefix(new String[]{"flower", "flow", "flight"})); }
    @Test void noCommon()   { assertEquals("",   LongestCommonPrefix.longestCommonPrefix(new String[]{"dog", "racecar", "car"})); }
    @Test void singleWord() { assertEquals("hi", LongestCommonPrefix.longestCommonPrefix(new String[]{"hi"})); }
    @Test void empty()      { assertEquals("",   LongestCommonPrefix.longestCommonPrefix(new String[]{})); }
}
