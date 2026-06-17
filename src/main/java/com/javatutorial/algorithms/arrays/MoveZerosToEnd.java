package com.javatutorial.algorithms.arrays;

import java.util.Arrays;

/**
 * <h2>Move Zeros to End</h2>
 * In-place: shift all non-zero values left, preserving relative order, then
 * fill the tail with zeros.
 *
 * <pre>
 *   Time : O(n)   Space : O(1)
 * </pre>
 */
public class MoveZerosToEnd {

    public static void moveZeros(int[] nums) {
        int insertPos = 0;
        for (int n : nums) if (n != 0) nums[insertPos++] = n;
        while (insertPos < nums.length) nums[insertPos++] = 0;
    }

    public static void main(String[] args) {
        int[] a = {0, 1, 0, 3, 12};
        moveZeros(a);
        System.out.println(Arrays.toString(a)); // [1, 3, 12, 0, 0]
    }
}
