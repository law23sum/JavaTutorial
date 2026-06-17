package com.javatutorial.algorithms.searching;

/**
 * <h2>Binary Search</h2>
 * Find a target in a <b>sorted</b> array by halving the search space at each
 * step.
 *
 * <pre>
 *   Time : O(log n)   Space : O(1) iterative
 * </pre>
 */
public class BinarySearch {

    public static int search(int[] sorted, int target) {
        int lo = 0, hi = sorted.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;       // avoids int overflow
            if      (sorted[mid] == target) return mid;
            else if (sorted[mid] <  target) lo = mid + 1;
            else                            hi = mid - 1;
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] sorted = {1, 3, 5, 7, 9, 11};
        System.out.println(search(sorted, 7));   // 3
        System.out.println(search(sorted, 4));   // -1
    }
}
