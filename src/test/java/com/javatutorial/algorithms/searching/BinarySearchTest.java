package com.javatutorial.algorithms.searching;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BinarySearchTest {

    private final int[] sorted = {1, 3, 5, 7, 9, 11};

    @Test void findsMiddle()  { assertEquals(3, BinarySearch.search(sorted, 7)); }
    @Test void findsFirst()   { assertEquals(0, BinarySearch.search(sorted, 1)); }
    @Test void findsLast()    { assertEquals(5, BinarySearch.search(sorted, 11)); }
    @Test void notPresent()   { assertEquals(-1, BinarySearch.search(sorted, 4)); }
    @Test void emptyArray()   { assertEquals(-1, BinarySearch.search(new int[]{}, 1)); }
}
