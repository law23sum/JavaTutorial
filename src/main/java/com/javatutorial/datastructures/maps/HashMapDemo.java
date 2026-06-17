package com.javatutorial.datastructures.maps;

import java.util.HashMap;
import java.util.Map;

/**
 * <h2>HashMap</h2>
 * Key→value store backed by a hash table. Average O(1) for get/put/remove.
 * Iteration order is <b>not defined</b>. One {@code null} key is allowed;
 * many {@code null} values are allowed. {@code put} on an existing key
 * overwrites and returns the previous value.
 */
public class HashMapDemo {

    public static void main(String[] args) {
        Map<String, Integer> stock = new HashMap<>();
        stock.put("apple", 10);
        stock.put("banana", 5);
        Integer prev = stock.put("apple", 12);     // overwrite
        System.out.println("prev apple count : " + prev);

        System.out.println("apple        : " + stock.get("apple"));
        System.out.println("missing/-1   : " + stock.getOrDefault("kiwi", -1));
        System.out.println("contains key : " + stock.containsKey("banana"));

        stock.forEach((k, v) -> System.out.println(k + " = " + v));
    }
}
