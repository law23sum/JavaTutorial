package com.javatutorial.algorithms.searching;

import java.util.Arrays;

/**
 * Linear Search — scan every element until the target is found.
 *
 * <p>The simplest search. Works on ANY array (sorted or not) because it makes no
 * assumptions about ordering. When data is sorted, prefer {@link BinarySearch}.
 *
 * <p>JDK equivalents: there is no direct "linear search by value" for arrays, but
 * {@code List.indexOf(Object)} and {@code Stream.anyMatch(..)} are linear scans.
 *
 * Time:  O(n)   — worst/average case touches every element once.
 * Space: O(1)   — only a loop index is used.
 */
public final class LinearSearch {

    private LinearSearch() { }

    // ───────────────────────────── 1) CORE ALGORITHM ─────────────────────────────

    /**
     * Returns the index of {@code target} in {@code data}, or -1 if absent.
     *
     * Time:  O(n)   Space: O(1)
     */
    public static int indexOf(int[] data, int target) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == target) {
                return i;                 // found — return immediately (best case O(1))
            }
        }
        return -1;                        // not found after scanning all n elements
    }

    // ───────────────────────────── 2) DEMO ─────────────────────────────

    public static void main(String[] args) {
        int[] data = {9, 3, 7, 1, 8, 2, 5};   // intentionally unsorted
        System.out.println("[LinearSearch] data           = " + Arrays.toString(data));
        System.out.println("[LinearSearch] indexOf(8)      = " + indexOf(data, 8));  // 4
        System.out.println("[LinearSearch] indexOf(9)      = " + indexOf(data, 9));  // 0 (best case)
        System.out.println("[LinearSearch] indexOf(42)     = " + indexOf(data, 42)); // -1 (worst case)
    }
}
