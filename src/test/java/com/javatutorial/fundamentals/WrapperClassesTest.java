package com.javatutorial.fundamentals;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Pinned-down Integer cache behavior so the wrappers tutorial never gets
 * surprised by autoboxing identity quirks.
 */
class WrapperClassesTest {

    @Test void cachedInRange() {
        Integer a = 100, b = 100;
        assertSame(a, b, "values in [-128,127] should be cached");
    }

    @Test void notCachedOutOfRange() {
        Integer a = 200, b = 200;
        assertNotSame(a, b, "values outside cache must be distinct objects");
        assertEquals(a, b, "but equals() compares by value");
    }

    @Test void parseHex() {
        assertEquals(255, Integer.parseInt("FF", 16));
    }
}
