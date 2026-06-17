package com.javatutorial.algorithms.arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class MoveZerosToEndTest {

    @Test void shiftsZerosWhilePreservingOrder() {
        int[] a = {0, 1, 0, 3, 12};
        MoveZerosToEnd.moveZeros(a);
        assertArrayEquals(new int[]{1, 3, 12, 0, 0}, a);
    }

    @Test void allZeros() {
        int[] a = {0, 0, 0};
        MoveZerosToEnd.moveZeros(a);
        assertArrayEquals(new int[]{0, 0, 0}, a);
    }

    @Test void noZeros() {
        int[] a = {1, 2, 3};
        MoveZerosToEnd.moveZeros(a);
        assertArrayEquals(new int[]{1, 2, 3}, a);
    }
}
