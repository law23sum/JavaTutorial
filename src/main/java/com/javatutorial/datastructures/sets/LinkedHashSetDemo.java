package com.javatutorial.datastructures.sets;

import java.util.LinkedHashSet;

/**
 * <h2>LinkedHashSet</h2>
 * HashSet plus a doubly-linked list of entries → predictable <b>insertion
 * order</b> with the same average O(1) operations. Re-adding an existing
 * element does not change its position.
 */
public class LinkedHashSetDemo {

    public static void main(String[] args) {
        LinkedHashSet<String> log = new LinkedHashSet<>();
        log.add("Login");
        log.add("AddToCart");
        log.add("Checkout");
        log.add("Login");                // duplicate ignored, order unchanged

        log.forEach(System.out::println); // Login, AddToCart, Checkout
    }
}
