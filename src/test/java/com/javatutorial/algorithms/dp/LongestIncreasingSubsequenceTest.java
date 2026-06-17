package com.javatutorial.algorithms.dp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongestIncreasingSubsequenceTest {
    @Test void mixed()  { assertEquals(4, LongestIncreasingSubsequence.lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18})); }
    @Test void allEq()  { assertEquals(1, LongestIncreasingSubsequence.lengthOfLIS(new int[]{7, 7, 7, 7})); }
    @Test void single() { assertEquals(1, LongestIncreasingSubsequence.lengthOfLIS(new int[]{1})); }
}
