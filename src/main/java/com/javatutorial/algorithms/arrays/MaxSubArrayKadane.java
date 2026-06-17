package com.javatutorial.algorithms.arrays;

/**
 * <h2>Maximum Subarray (Kadane's Algorithm)</h2>
 * Find the maximum sum of any contiguous subarray (≥1 element).
 *
 * <p>At each index keep the best sum that <i>ends here</i>. Either start fresh
 * or extend the previous best:
 * <pre>
 *   curr = max(nums[i], curr + nums[i])
 *   best = max(best, curr)
 * </pre>
 *
 * <pre>
 *   Time : O(n)   Space : O(1)
 * </pre>
 */
public class MaxSubArrayKadane {

    public static int kadane(int[] nums) {
        int curr = nums[0];
        int best = nums[0];
        for (int i = 1; i < nums.length; i++) {
            curr = Math.max(nums[i], curr + nums[i]);
            best = Math.max(best, curr);
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(kadane(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4})); // 6
    }
}
