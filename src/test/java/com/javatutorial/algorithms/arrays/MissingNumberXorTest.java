package com.javatutorial.algorithms.arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MissingNumberXorTest {
    @Test void findsMissingMiddle() { assertEquals(2, MissingNumberXor.missingNumber(new int[]{3, 0, 1})); }
    @Test void findsMissingLast()   { assertEquals(2, MissingNumberXor.missingNumber(new int[]{0, 1})); }
    @Test void findsMissingZero()   { assertEquals(0, MissingNumberXor.missingNumber(new int[]{1})); }
}
