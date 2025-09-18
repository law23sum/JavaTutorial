package com.tutorial.core.datastructure.collections.maps;

import java.util.*;
import java.util.Collection;

/**
 * LinkedHashMapDeepDive — ordered hash map with optional access-order (LRU-friendly).
 *
 * Key facts:
 *  • Hash table + doubly-linked list → O(1) avg put/get/remove with **predictable iteration order**.
 *  • Default order = **insertion order**.
 *  • Optional **access order** when constructed with (initialCapacity, loadFactor, accessOrder=true).
 *  • Allows **one null key** and many null values.
 *  • Same Map/Compute APIs as HashMap (replace/replaceAll/putIfAbsent/getOrDefault/compute/merge).
        *  • Handy for caches: override removeEldestEntry to evict oldest
 *    (often oldest-accessed with accessOrder=true).
        *
        * BUILD & RUN:
        *   javac com/tutorial/core/datastructure/collections/maps/LinkedHashMapDeepDive.java && \
        *   java com.tutorial.core.datastructure.collections.maps.LinkedHashMapDeepDive
 */

        public class LinkedHashMaps {

            // =========================================================================
            // 1) BASICS — insertion order, null handling, core ops
            // =========================================================================
            static void basics() {
                System.out.println("=== BASICS (insertion order)");
                LinkedHashMap<String, Integer> m = new LinkedHashMap<>(); // insertion-ordered

                // put(k,v): insert or overwrite. Keys must be unique; one null key allowed.
                m.put("banana", 5);
                m.put("apple", 3);
                m.put("cherry", 7);
                m.put(null, 99);      // null key (at most one)
                m.put("dragonfruit", null); // null value

                // get(k): returns value or null if absent.
                System.out.println("get(apple) = " + m.get("apple"));

                // containsKey / containsValue
                System.out.println("containsKey('cherry') = " + m.containsKey("cherry"));
                System.out.println("containsValue(42)     = " + m.containsValue(42));

                // remove(k)
                m.remove("banana");

                // Iteration order is **insertion order**.
                System.out.println("map (insertion order): " + m);

                // size / isEmpty
                System.out.println("size=" + m.size() + ", isEmpty=" + m.isEmpty());
            }

            // =========================================================================
            // 2) ITERATION — entrySet / keySet / values (order preserved)
            // =========================================================================
            static void iteration() {
                System.out.println("\n=== ITERATION (insertion order preserved)");
                LinkedHashMap<String, Integer> m = new LinkedHashMap<>();
                m.put("A", 1); m.put("B", 2); m.put("C", 3);

                System.out.println("entrySet:");
                for (Map.Entry<String,Integer> e : m.entrySet()) {
                    System.out.println("  " + e.getKey() + " → " + e.getValue());
                }

                System.out.println("keySet:");
                for (String k : m.keySet()) System.out.println("  key=" + k);

                System.out.println("values:");
                for (Integer v : m.values()) System.out.println("  value=" + v);
            }

            // =========================================================================
            // 3) UPDATE OPS — replace, replaceAll, putIfAbsent, getOrDefault
            // =========================================================================
            static void updateOps() {
                System.out.println("\n=== UPDATE OPS");
                LinkedHashMap<String, Integer> m = new LinkedHashMap<>(Map.of("a", 1, "b", 2));

                // replace(k, newVal) and replace(k, oldVal, newVal)
                m.replace("a", 100);
                m.replace("b", 2, 200);

                // putIfAbsent(k, v): only inserts if missing
                m.putIfAbsent("c", 300);

                // getOrDefault(k, default)
                int d = m.getOrDefault("z", -1);

                System.out.println("after replace/putIfAbsent: " + m + ", getOrDefault(z) = " + d);

                // replaceAll(BiFunction): transform values in place
                m.replaceAll((k, val) -> val * 2);
                System.out.println("after replaceAll(*2): " + m);
            }

            // =========================================================================
            // 4) BULK OPS — putAll, clear, isEmpty
            // =========================================================================
            static void bulkOps() {
                System.out.println("\n=== BULK OPS");
                LinkedHashMap<String, Integer> a = new LinkedHashMap<>(Map.of("x", 1, "y", 2));
                LinkedHashMap<String, Integer> b = new LinkedHashMap<>();
                b.putAll(a); // copy
                System.out.println("b after putAll(a): " + b);
                b.clear();
                System.out.println("b after clear: " + b + ", isEmpty=" + b.isEmpty());
            }

            // =========================================================================
            // 5) COMPUTE APIS — compute, computeIfAbsent, computeIfPresent, merge
            // =========================================================================
            static void computeOps() {
                System.out.println("\n=== COMPUTE OPS");
                LinkedHashMap<String, Integer> m = new LinkedHashMap<>(Map.of("alpha", 1, "beta", 2));

                // compute(k, remap): even if present/absent; null result removes entry
                m.compute("alpha", (k, v) -> (v == null ? 0 : v) + 10);

                // computeIfAbsent(k, map): only if missing
                m.computeIfAbsent("gamma", k -> 99);

                // computeIfPresent(k, remap): only if present
                m.computeIfPresent("beta", (k, v) -> v * 100);

                // merge(k, val, remap): if absent put(val); else merge old+new
                m.merge("gamma", 1, Integer::sum);

                System.out.println("after compute ops: " + m);
            }

            // =========================================================================
            // 6) ACCESS ORDER — rebuild on access; great for LRU patterns
            //     Constructor: new LinkedHashMap<>(initialCapacity, loadFactor, accessOrder=true)
            //     Access order = most-recently-accessed at tail of iteration.
            // =========================================================================
            static void accessOrderDemo() {
                System.out.println("\n=== ACCESS ORDER (accessOrder=true)");
                LinkedHashMap<String, Integer> m =
                        new LinkedHashMap<>(16, 0.75f, true); // access-order

                m.put("A", 1); m.put("B", 2); m.put("C", 3); m.put("D", 4);
                System.out.println("start : " + m.keySet()); // [A, B, C, D]

                // Access B, then A → they become most-recent and move to tail.
                m.get("B");
                m.get("A");
                System.out.println("after get(B), get(A): " + m.keySet()); // [C, D, B, A]

                // put() also counts as an access for that key.
                m.put("C", 33);
                System.out.println("after put(C,33): " + m.keySet()); // [D, B, A, C]
            }

            // =========================================================================
            // 7) LRU CACHE — fixed capacity using removeEldestEntry override
            //     With accessOrder=true, the eldest is the least-recently-used.
            // =========================================================================
            static class LruCache<K,V> extends LinkedHashMap<K,V> {
                private final int capacity;
                LruCache(int capacity) {
                    super(capacity, 0.75f, true); // access-order
                    this.capacity = capacity;
                }
                @Override
                protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
                    // Return true to evict when size exceeds capacity.
                    return size() > capacity;
                }
            }

            static void lruDemo() {
                System.out.println("\n=== LRU CACHE (capacity=3)");
                LruCache<String, Integer> cache = new LruCache<>(3);

                // Fill 3
                cache.put("A", 1); cache.put("B", 2); cache.put("C", 3);
                System.out.println("fill   : " + cache.keySet()); // [A, B, C]

                // Touch A to make it MRU: order becomes [B, C, A]
                cache.get("A");
                System.out.println("touch A: " + cache.keySet());

                // Add D → evicts LRU (B)
                cache.put("D", 4);
                System.out.println("add D  : " + cache.keySet()); // [C, A, D]

                // Add E after touching C → evicts A
                cache.get("C");               // [A, D, C]
                cache.put("E", 5);            // evict A → [D, C, E]
                System.out.println("final   : " + cache.keySet());
            }

            // =========================================================================
            // 8) ADVANCED — stable iteration + sorting snapshot by value
            //     (LinkedHashMap itself doesn’t auto-sort; take a snapshot List and sort it.)
            // =========================================================================
            static void advanced() {
                System.out.println("\n=== ADVANCED (sort by value snapshot)");
                LinkedHashMap<String, Integer> m = new LinkedHashMap<>();
                m.put("z", 26); m.put("a", 1); m.put("m", 13);

                // Snapshot & sort entries by value (ascending)
                List<Map.Entry<String,Integer>> byVal = new ArrayList<>(m.entrySet());
                byVal.sort(Map.Entry.comparingByValue());
                System.out.println("entries sorted by value: " + byVal);

                // Convert order-preserving keys/values to other collections
                Set<String> keys = m.keySet();              // preserves insertion order
                Collection<Integer> vals = m.values();      // same order as keys
                System.out.println("keys=" + keys + ", values=" + vals);
            }

            // =========================================================================
            // main — run the tour
            // =========================================================================
            public static void main(String[] args) {
                basics();
                iteration();
                updateOps();
                bulkOps();
                computeOps();
                accessOrderDemo();
                lruDemo();
                advanced();
            }
        }
