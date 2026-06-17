package com.javatutorial.algorithms.arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class TwoSumTest {

    @Test void findsPairAtStart() {
        assertArrayEquals(new int[]{0, 1}, TwoSum.twoSum(new int[]{2, 7, 11, 15}, 9));
    }

    @Test void findsPairInMiddle() {
        assertArrayEquals(new int[]{1, 2}, TwoSum.twoSum(new int[]{3, 2, 4}, 6));
    }

    @Test void noPairReturnsMinusOnes() {
        assertArrayEquals(new int[]{-1, -1}, TwoSum.twoSum(new int[]{1, 2, 3}, 100));
    }
}
