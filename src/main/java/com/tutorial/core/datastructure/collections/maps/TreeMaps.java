package com.tutorial.core.datastructure.collections.maps;

import java.util.*;

/**
 * TreeMapDeepDive — ordered Map with log-time range queries.
 *
 * Key facts:
 *  • Balanced red–black tree under the hood → O(log n) put/get/remove.
 *  • Keeps keys in **sorted order** (natural comparators or your own Comparator).
 *  • Implements NavigableMap → floor/ceiling/lower/higher, subMap/headMap/tailMap, descendingMap.
 *  • **Null keys are not permitted** (values may be null).
 *  • Iteration order = key sort order (ascending by default).
 *
 * COVERED:
 *  1) Construction & basics (put, get, remove, size, contains)
 *  2) Sorting rules: natural order vs custom Comparator
 *  3) Navigable queries: floor/ceiling/lower/higher, first/last
 *  4) Views: subMap/headMap/tailMap + inclusive flags
 *  5) Descending views: descendingMap / descendingKeySet
 *  6) Polling ends: pollFirstEntry / pollLastEntry
 *  7) Update & compute APIs: replace / replaceAll / putIfAbsent / getOrDefault / compute* / merge
 *  8) Advanced: range aggregation & “top-k” via descending views
 *
 * BUILD & RUN:
 *   javac com/tutorial/core/datastructure/collections/maps/TreeMapDeepDive.java && \
 *   java  com.tutorial.core.datastructure.collections.maps.TreeMapDeepDive
 */
public class TreeMaps {

    // =========================================================================
    // 1) BASICS — ordered map with log-time operations
    // =========================================================================
    static void basics() {
        System.out.println("=== BASICS (natural key order)");
        // ARG: no comparator → natural ordering of keys (here, String lexicographic).
        TreeMap<String, Integer> tm = new TreeMap<>();

        // put(k,v): inserts or replaces; keys must be non-null and comparable.
        tm.put("banana", 5);
        tm.put("apple", 3);
        tm.put("cherry", 7);

        // get(k): O(log n), null if missing.
        System.out.println("get(apple) = " + tm.get("apple")); // 3

        // containsKey / containsValue
        System.out.println("containsKey('cherry') = " + tm.containsKey("cherry"));
        System.out.println("containsValue(42) = " + tm.containsValue(42));

        // remove(k)
        tm.remove("banana");

        // iteration order = sorted by key (apple, cherry)
        System.out.println("map (sorted): " + tm);

        // size / isEmpty
        System.out.println("size=" + tm.size() + ", isEmpty=" + tm.isEmpty());
    }

    // =========================================================================
    // 2) COMPARATORS — custom sort (by length, then alpha as tiebreaker)
    // =========================================================================
    static void comparators() {
        System.out.println("\n=== COMPARATORS (custom key order)");
        Comparator<String> byLenThenAlpha =
                Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder());

        // ARG: comparator controls all ordering & navigable behavior.
        TreeMap<String, Integer> tm = new TreeMap<>(byLenThenAlpha);
        tm.put("pear", 4);
        tm.put("fig", 2);
        tm.put("banana", 6);
        tm.put("apple", 5);
        tm.put("kiwi", 3);

        System.out.println("order (len→alpha): " + tm.keySet());
        System.out.println("comparator is consistent with equals? " +
                (tm.comparator() == null ? "natural" : "custom"));
    }

    // =========================================================================
    // 3) NAVIGABLE QUERIES — floor/ceiling/lower/higher, first/last
    // =========================================================================
    static void navigableQueries() {
        System.out.println("\n=== NAVIGABLE QUERIES");
        TreeMap<Integer, String> tm = new TreeMap<>();
        tm.put(10, "ten");
        tm.put(20, "twenty");
        tm.put(30, "thirty");
        tm.put(40, "forty");

        // firstKey/lastKey
        System.out.println("firstKey=" + tm.firstKey() + ", lastKey=" + tm.lastKey());

        // floorKey(x): greatest key ≤ x; ceilingKey(x): smallest key ≥ x
        System.out.println("floorKey(25) = " + tm.floorKey(25));    // 20
        System.out.println("ceilingKey(25) = " + tm.ceilingKey(25));// 30

        // lowerKey(x): greatest key < x; higherKey(x): smallest key > x
        System.out.println("lowerKey(20) = " + tm.lowerKey(20));    // 10
        System.out.println("higherKey(20) = " + tm.higherKey(20));  // 30

        // You can also get the (key,value) pair variants: floorEntry, ceilingEntry, etc.
        Map.Entry<Integer, String> fe = tm.floorEntry(25);
        System.out.println("floorEntry(25) = " + (fe == null ? null : fe.getKey() + "→" + fe.getValue()));
    }

    // =========================================================================
    // 4) RANGE VIEWS — headMap / tailMap / subMap (inclusive flags)
    // =========================================================================
    static void rangeViews() {
        System.out.println("\n=== RANGE VIEWS");
        TreeMap<Integer, String> tm = new TreeMap<>(Map.of(
                1, "A", 2, "B", 3, "C", 4, "D", 5, "E", 6, "F"));

        // subMap(fromKey, fromInclusive, toKey, toInclusive)
        NavigableMap<Integer, String> mid = tm.subMap(2, true, 5, false); // [2,5)
        System.out.println("subMap[2..5) = " + mid);

        // headMap(toKey, inclusive)  → keys < toKey (or ≤ when inclusive=true)
        System.out.println("headMap(<4) = " + tm.headMap(4, false));

        // tailMap(fromKey, inclusive) → keys ≥ fromKey (or > when inclusive=false)
        System.out.println("tailMap(≥4) = " + tm.tailMap(4, true));

        // WARNING: Views are **backed** by the original map (structurally coupled).
        // Modifying tm changes the views and vice versa (within the view’s key range).
        mid.put(3, "CC"); // inside range → OK
        System.out.println("mid after put: " + mid);
        System.out.println("tm after mid change: " + tm);
    }

    // =========================================================================
    // 5) DESCENDING — reversed order views
    // =========================================================================
    static void descendingViews() {
        System.out.println("\n=== DESCENDING VIEWS");
        TreeMap<Integer, String> tm = new TreeMap<>(Map.of(10, "ten", 20, "twenty", 30, "thirty"));
        NavigableMap<Integer, String> desc = tm.descendingMap(); // live, backed

        System.out.println("ascending keys : " + tm.keySet());
        System.out.println("descending keys: " + desc.keySet());

        // descendingKeySet() alone is often enough for “top-k largest keys” scans.
        NavigableSet<Integer> dk = tm.descendingKeySet();
        System.out.println("descendingKeySet: " + dk);
    }

    // =========================================================================
    // 6) POLL ENDS — remove and return smallest/largest entry
    // =========================================================================
    static void pollEnds() {
        System.out.println("\n=== POLL FIRST/LAST ENTRY");
        TreeMap<Integer, String> tm = new TreeMap<>(Map.of(1, "A", 2, "B", 3, "C"));

        Map.Entry<Integer, String> first = tm.pollFirstEntry(); // removes 1→A
        Map.Entry<Integer, String> last  = tm.pollLastEntry();  // removes 3→C
        System.out.println("polled first=" + first + ", last=" + last);
        System.out.println("remaining map: " + tm);             // only 2→B remains
    }

    // =========================================================================
    // 7) UPDATE & COMPUTE APIS — all Map defaults work on TreeMap
    // =========================================================================
    static void updateAndCompute() {
        System.out.println("\n=== UPDATE & COMPUTE APIS");
        TreeMap<String, Integer> tm = new TreeMap<>();
        tm.put("alpha", 1);
        tm.put("beta", 2);

        // replace(k, v) & replace(k, oldV, newV)
        tm.replace("alpha", 10);
        tm.replace("beta", 2, 20);

        // putIfAbsent(k, v) — insert only if missing
        tm.putIfAbsent("gamma", 30);

        // getOrDefault(k, def)
        int v = tm.getOrDefault("delta", -1);

        System.out.println("after replace/putIfAbsent: " + tm + ", getOrDefault(delta) = " + v);

        // replaceAll(BiFunction) — update all values in place
        tm.replaceAll((k, val) -> val * 2);
        System.out.println("after replaceAll(*2): " + tm);

        // compute/computeIfAbsent/computeIfPresent/merge
        tm.compute("alpha", (k, val) -> (val == null ? 0 : val) + 1);       // 21 → 22
        tm.computeIfAbsent("delta", k -> 99);                                // creates new
        tm.computeIfPresent("beta", (k, val) -> val + 100);                  // 40 → 140
        tm.merge("gamma", 1, Integer::sum);                                  // 60 → 61
        System.out.println("after compute ops: " + tm);
    }

    // =========================================================================
    // 8) ADVANCED — range aggregation + top-k with descending views
    // =========================================================================
    static void advanced() {
        System.out.println("\n=== ADVANCED (range agg + top-k)");
        // Imagine scores keyed by timestamp; we want:
        //   * sum of scores in [t1, t2]
        //   * the top 3 latest timestamps
        TreeMap<Long, Integer> scores = new TreeMap<>();
        scores.put(1000L, 5);
        scores.put(1010L, 7);
        scores.put(1020L, 4);
        scores.put(1030L, 9);
        scores.put(1040L, 2);

        long t1 = 1005L, t2 = 1030L;
        // subMap with inclusive bounds: [t1..t2] — we need nearest floor/ceiling around gaps.
        Long from = scores.ceilingKey(t1); // smallest ≥ t1
        Long to   = scores.floorKey(t2);   // largest ≤ t2
        int rangeSum = 0;
        if (from != null && to != null && from <= to) {
            for (int v : scores.subMap(from, true, to, true).values()) {
                rangeSum += v;
            }
        }
        System.out.println("sum scores in [" + t1 + "," + t2 + "] = " + rangeSum);

        // Top-k by **latest** timestamps → just walk the descendingKeySet.
        int k = 3;
        List<Map.Entry<Long,Integer>> latestK = new ArrayList<>(k);
        for (Map.Entry<Long,Integer> e : scores.descendingMap().entrySet()) {
            if (latestK.size() == k) break;
            latestK.add(e);
        }
        System.out.println("latest " + k + " entries: " + latestK);
    }

    // =========================================================================
    // main — run the tour
    // =========================================================================
    public static void main(String[] args) {
        basics();
        comparators();
        navigableQueries();
        rangeViews();
        descendingViews();
        pollEnds();
        updateAndCompute();
        advanced();
    }
}
