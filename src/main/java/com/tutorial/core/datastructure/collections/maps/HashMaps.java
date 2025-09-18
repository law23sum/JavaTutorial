package com.tutorial.core.datastructure.collections.maps;

import java.util.*;

/**
 * HashMapDemo — demonstrates common and advanced usage of HashMap.
 *
 * Key facts:
 *  • Stores key→value pairs (unique keys, one value per key).
 *  • Allows null key (at most one) and multiple null values.
 *  • Backed by hash table; average O(1) put/get/remove/contains.
 *  • Iteration order is unspecified (unlike LinkedHashMap).
 *
 * COVERED:
 *  1) Construction & basics (put, get, remove, size)
 *  2) Null keys & null values
 *  3) containsKey / containsValue
 *  4) Iteration styles (entrySet, keySet, values)
 *  5) replace, replaceAll, putIfAbsent, getOrDefault
 *  6) Bulk operations (putAll, clear, isEmpty)
 *  7) Compute APIs (compute, computeIfAbsent, computeIfPresent, merge)
 *  8) Advanced: map views to Set/List, sorting by key/value
 */
public class HashMaps {

    // =========================================================================
    // 1) Basics — put, get, remove
    // =========================================================================
    static void basics() {
        System.out.println("=== BASICS");
        HashMap<String, Integer> map = new HashMap<>();

        // put(key, value): inserts or overwrites
        map.put("apple", 3);
        map.put("banana", 5);
        map.put("cherry", 7);

        // get(key): returns value or null if absent
        System.out.println("get(apple) = " + map.get("apple"));

        // remove(key): deletes mapping
        map.remove("banana");
        System.out.println("after remove banana: " + map);

        // size()
        System.out.println("size = " + map.size());
    }

    // =========================================================================
    // 2) Null keys & values
    // =========================================================================
    static void nullsDemo() {
        System.out.println("\n=== NULL KEYS & VALUES");
        HashMap<String, String> map = new HashMap<>();
        map.put(null, "nullKey");  // one null key allowed
        map.put("x", null);        // multiple null values allowed
        map.put("y", null);

        System.out.println("map with nulls: " + map);
    }

    // =========================================================================
    // 3) containsKey / containsValue
    // =========================================================================
    static void containsDemo() {
        System.out.println("\n=== CONTAINS");
        HashMap<String, Integer> map = new HashMap<>(Map.of("a", 1, "b", 2));
        System.out.println("containsKey('a')? " + map.containsKey("a"));
        System.out.println("containsValue(2)? " + map.containsValue(2));
    }

    // =========================================================================
    // 4) Iteration — entrySet, keySet, values
    // =========================================================================
    static void iterationDemo() {
        System.out.println("\n=== ITERATION");
        HashMap<String, Integer> map = new HashMap<>(Map.of("dog", 1, "cat", 2, "bird", 3));

        // entrySet() — iterate key+value
        System.out.println("entrySet iteration:");
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            System.out.println("  " + e.getKey() + " → " + e.getValue());
        }

        // keySet() — iterate keys only
        System.out.println("keySet iteration:");
        for (String k : map.keySet()) System.out.println("  key=" + k);

        // values() — iterate values only
        System.out.println("values iteration:");
        for (Integer v : map.values()) System.out.println("  value=" + v);
    }

    // =========================================================================
    // 5) replace, replaceAll, putIfAbsent, getOrDefault
    // =========================================================================
    static void updateOps() {
        System.out.println("\n=== UPDATE OPS");
        HashMap<String, Integer> map = new HashMap<>(Map.of("a", 1, "b", 2));

        // replace(key, newVal)
        map.replace("a", 100);
        // replace(key, oldVal, newVal) → only replaces if oldVal matches
        map.replace("b", 2, 200);

        // putIfAbsent(key, val) → insert only if missing
        map.putIfAbsent("c", 300);

        // getOrDefault(key, defVal)
        int v = map.getOrDefault("d", -1);

        System.out.println("map after updates: " + map + ", getOrDefault(d,-1)=" + v);

        // replaceAll(BiFunction) → apply to every entry
        map.replaceAll((k, val) -> val * 2);
        System.out.println("after replaceAll(*2): " + map);
    }

    // =========================================================================
    // 6) Bulk ops — putAll, clear, isEmpty
    // =========================================================================
    static void bulkOps() {
        System.out.println("\n=== BULK OPS");
        HashMap<String, Integer> map1 = new HashMap<>(Map.of("x", 1, "y", 2));
        HashMap<String, Integer> map2 = new HashMap<>();
        map2.putAll(map1); // copy entries
        System.out.println("map2 after putAll(map1): " + map2);

        map2.clear();
        System.out.println("map2 after clear: " + map2 + ", isEmpty=" + map2.isEmpty());
    }

    // =========================================================================
    // 7) Compute APIs — compute, computeIfAbsent, computeIfPresent, merge
    // =========================================================================
    static void computeOps() {
        System.out.println("\n=== COMPUTE OPS");
        HashMap<String, Integer> map = new HashMap<>(Map.of("a", 1, "b", 2));

        // compute(key, remappingFunction)
        map.compute("a", (k, v) -> v == null ? 0 : v + 10);

        // computeIfAbsent(key, mappingFunction) → only if missing
        map.computeIfAbsent("c", k -> 99);

        // computeIfPresent(key, remappingFunction) → only if key exists
        map.computeIfPresent("b", (k, v) -> v * 100);

        // merge(key, value, remappingFunction)
        // If key absent → put(key,value). If present → merge old+new.
        map.merge("c", 1, Integer::sum);

        System.out.println("after compute ops: " + map);
    }

    // =========================================================================
    // 8) Advanced — sorting by key/value, convert views
    // =========================================================================
    static void advanced() {
        System.out.println("\n=== ADVANCED");
        HashMap<String, Integer> map = new HashMap<>(Map.of("z", 26, "a", 1, "m", 13));

        // Sort entries by key
        List<Map.Entry<String, Integer>> byKey = new ArrayList<>(map.entrySet());
        byKey.sort(Map.Entry.comparingByKey());
        System.out.println("sorted by key: " + byKey);

        // Sort entries by value
        List<Map.Entry<String, Integer>> byVal = new ArrayList<>(map.entrySet());
        byVal.sort(Map.Entry.comparingByValue());
        System.out.println("sorted by value: " + byVal);

        // Convert to other collections
        Set<String> keys = map.keySet();
        Collection<Integer> vals = map.values();
        System.out.println("keys = " + keys + ", values = " + vals);
    }

    // =========================================================================
    // main — run everything
    // =========================================================================
    public static void main(String[] args) {
        basics();
        nullsDemo();
        containsDemo();
        iterationDemo();
        updateOps();
        bulkOps();
        computeOps();
        advanced();
    }
}