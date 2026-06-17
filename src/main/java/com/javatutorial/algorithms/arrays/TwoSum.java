package com.javatutorial.algorithms.arrays;

import java.util.HashMap;
import java.util.Map;

/**
 * <h2>Two Sum</h2>
 * Given {@code nums} and {@code target}, return indices of the two numbers
 * that add to {@code target}. If no pair exists, return {@code [-1, -1]}.
 *
 * <p>One-pass HashMap of {@code value -> index}: for each element compute
 * the complement and check if we've already seen it.
 *
 * <pre>
 *   Time : O(n)   Space : O(n)
 * </pre>
 */
public class TwoSum {

    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int need = target - nums[i];
            if (seen.containsKey(need)) return new int[]{seen.get(need), i};
            seen.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }

    public static void main(String[] args) {
        int[] r = twoSum(new int[]{2, 7, 11, 15}, 9);
        System.out.println("[" + r[0] + ", " + r[1] + "]"); // [0, 1]
    }
}
