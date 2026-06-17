package com.javatutorial.datastructures.maps;

import java.util.LinkedHashMap;

/**
 * <h2>LinkedHashMap</h2>
 * HashMap + a doubly-linked list of entries → predictable iteration order.
 * Default mode is insertion order; access-order mode (constructor arg
 * {@code accessOrder=true}) moves the most-recently-used entry to the tail —
 * the foundation for an LRU cache via {@code removeEldestEntry}.
 */
public class LinkedHashMapDemo {

    public static void main(String[] args) {
        LinkedHashMap<String, String> payload = new LinkedHashMap<>();
        payload.put("username", "admin");
        payload.put("password", "secret");
        payload.put("role",     "QA");
        payload.forEach((k, v) -> System.out.println(k + " = " + v));

        // Tiny LRU cache (capacity 3):
        LinkedHashMap<Integer, String> lru = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(java.util.Map.Entry<Integer, String> e) {
                return size() > 3;
            }
        };
        lru.put(1, "a"); lru.put(2, "b"); lru.put(3, "c");
        lru.get(1);                 // touch 1 -> moves to tail
        lru.put(4, "d");            // evicts least-recently used (2)
        System.out.println("LRU keys: " + lru.keySet()); // [3, 1, 4]
    }
}
