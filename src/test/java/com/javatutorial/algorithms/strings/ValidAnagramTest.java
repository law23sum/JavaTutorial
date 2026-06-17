package com.javatutorial.algorithms.strings;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidAnagramTest {
    @Test void anagram()       { assertTrue(ValidAnagram.isAnagram("anagram", "nagaram")); }
    @Test void notAnagram()    { assertFalse(ValidAnagram.isAnagram("rat", "car")); }
    @Test void differentLen()  { assertFalse(ValidAnagram.isAnagram("a", "ab")); }
}
