package com.javatutorial.algorithms.arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MaxSubArrayKadaneTest {

    @Test void classicMixedArray() {
        assertEquals(6, MaxSubArrayKadane.kadane(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
    }

    @Test void allNegativesReturnsLargestSingleElement() {
        assertEquals(-1, MaxSubArrayKadane.kadane(new int[]{-3, -1, -2}));
    }

    @Test void singleElement() {
        assertEquals(5, MaxSubArrayKadane.kadane(new int[]{5}));
    }
}
