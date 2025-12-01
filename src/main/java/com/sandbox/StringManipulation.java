package com.sandbox;

import java.util.Comparator;

public class StringManipulation {
    public static void main(String[] args) {
        String str1 = "abABCDabcdABCDabcdcdABCDabcd";

        String sorted = str1.chars()
                .mapToObj(c -> (char) c)
                .sorted(
                        Comparator
                                // lowercase block first, then uppercase
                                .comparing((Character c) -> Character.isLowerCase(c) ? 0 : 1)
                                // within each block, sort by letter (case-insensitive)
                                .thenComparing(Character::toLowerCase)
                                // for same letter, put lowercase before uppercase
                                .thenComparing(c -> c)
                )
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();

        System.out.println(sorted); // aaaaabbbbccccddddAAAABBBBCCCCDD
    }
}
