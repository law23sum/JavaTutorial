package com.javatutorial.datastructures.sets;

import java.util.HashSet;
import java.util.Set;

/**
 * <h2>HashSet</h2>
 * Unordered, unique elements. Backed by a hash table: {@code hashCode()} picks
 * the bucket, {@code equals()} decides duplicate-or-distinct.
 *
 * <p>Average O(1) for {@code add}, {@code contains}, {@code remove}.
 * Iteration order is <b>not defined</b>.
 */
public class HashSetDemo {

    public static void main(String[] args) {
        Set<String> products = new HashSet<>();
        products.add("Apple");
        products.add("Banana");
        products.add("Apple");        // duplicate ignored
        System.out.println("size      : " + products.size());      // 2
        System.out.println("contains  : " + products.contains("Apple"));
        System.out.println("iteration : " + products);             // order undefined
    }
}
