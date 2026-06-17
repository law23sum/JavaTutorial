package com.javatutorial.algorithms.dp;

import java.util.Arrays;

/**
 * <h2>Longest Increasing Subsequence (length)</h2>
 * Patience-sorting with binary search. Maintain {@code tails[k]} = smallest
 * possible tail of an increasing subsequence of length {@code k+1}. For each
 * new value, binary-search for the first tail ≥ value and replace it.
 *
 * <pre>
 *   Time : O(n log n)   Space : O(n)
 * </pre>
 */
public class LongestIncreasingSubsequence {

    public static int lengthOfLIS(int[] nums) {
        int[] tails = new int[nums.length];
        int size = 0;
        for (int x : nums) {
            int i = Arrays.binarySearch(tails, 0, size, x);
            if (i < 0) i = -(i + 1);            // insertion point
            tails[i] = x;
            if (i == size) size++;
        }
        return size;
    }

    public static void main(String[] args) {
        System.out.println(lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18})); // 4
    }
}
