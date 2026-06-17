package com.javatutorial.algorithms.arrays;

/**
 * <h2>Missing Number (XOR trick)</h2>
 * Given an array of length {@code n} containing distinct integers from
 * {@code 0..n} with exactly one missing, return the missing number.
 *
 * <p>XOR every value 0..n with every element. Each present number cancels its
 * own index; the leftover XOR is the missing one.
 *
 * <pre>
 *   Time : O(n)   Space : O(1)
 * </pre>
 */
public class MissingNumberXor {

    public static int missingNumber(int[] nums) {
        int xor = nums.length;
        for (int i = 0; i < nums.length; i++) xor ^= i ^ nums[i];
        return xor;
    }

    public static void main(String[] args) {
        System.out.println(missingNumber(new int[]{3, 0, 1}));   // 2
        System.out.println(missingNumber(new int[]{0, 1}));      // 2
    }
}
