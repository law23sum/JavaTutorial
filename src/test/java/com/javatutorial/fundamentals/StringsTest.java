package com.javatutorial.fundamentals;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringsTest {

    @Test void contentEqualsButReferencesDiffer() {
        String a = "hi";
        String b = new String("hi");
        assertNotSame(a, b);
        assertEquals(a, b);
    }

    @Test void substringIsHalfOpen() {
        assertEquals("ell", "hello".substring(1, 4));
    }

    @Test void stringBuilderBuildsEfficiently() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) sb.append('x');
        assertEquals("xxxxx", sb.toString());
        assertTrue(sb.length() == 5);
    }
}
