package com.tutorial.core.datastructure.collections.sets;

import java.util.*;

/**
 * LinkedHashSetDemo — demonstrates insertion-order Set behavior.
 *
 * Key facts:
 *  • Implements Set — no duplicates allowed.
 *  • Backed by HashMap + doubly-linked list for order-preserving iteration.
 *  • Time complexity same as HashSet (O(1) add/remove/contains).
 *  • Differs from TreeSet (which sorts) and HashSet (which is unordered).
 *
 * COVERED:
 *  1) Construction & basics
 *  2) Duplicate handling & insertion order
 *  3) Null elements
 *  4) Iteration styles
 *  5) Core methods (add, remove, contains, clear, size, isEmpty)
 *  6) Bulk ops (addAll, removeAll, retainAll, containsAll)
 *  7) Advanced: converting to list, set algebra (union, intersection, difference)
 */
public class LinkedHashSets {

    // =========================================================================
    // 1) Basics — construction, add, duplicates
    // =========================================================================
    static void basics() {
        System.out.println("=== BASICS (insertion order preserved)");
        LinkedHashSet<String> lhs = new LinkedHashSet<>();

        lhs.add("apple");
        lhs.add("banana");
        lhs.add("cherry");
        lhs.add("banana");  // duplicate → ignored

        System.out.println("lhs = " + lhs); // order preserved: [apple, banana, cherry]
    }

    // =========================================================================
    // 2) Nulls — allowed (at most one null)
    // =========================================================================
    static void nullsDemo() {
        System.out.println("\n=== NULL ELEMENTS");
        LinkedHashSet<String> lhs = new LinkedHashSet<>();
        lhs.add(null);
        lhs.add("first");
        lhs.add(null); // duplicate null ignored
        System.out.println("lhs with null: " + lhs);
    }

    // =========================================================================
    // 3) Iteration styles
    // =========================================================================
    static void iterationDemo() {
        System.out.println("\n=== ITERATION");
        LinkedHashSet<Integer> nums = new LinkedHashSet<>(List.of(10, 20, 30, 40));

        // Enhanced for
        System.out.print("enhanced-for: ");
        for (int n : nums) System.out.print(n + " ");
        System.out.println();

        // Iterator
        System.out.print("iterator: ");
        for (Iterator<Integer> it = nums.iterator(); it.hasNext(); )
            System.out.print(it.next() + " ");
        System.out.println();

        // forEach (Java 8+)
        System.out.print("forEach lambda: ");
        nums.forEach(n -> System.out.print(n + " "));
        System.out.println();
    }

    // =========================================================================
    // 4) Core methods — add, remove, contains, clear, size, isEmpty
    // =========================================================================
    static void coreMethods() {
        System.out.println("\n=== CORE METHODS");
        LinkedHashSet<String> set = new LinkedHashSet<>();

        set.add("A");
        set.add("B");
        set.add("C");

        System.out.println("set = " + set);
        System.out.println("contains 'B'? " + set.contains("B"));
        set.remove("B");
        System.out.println("after remove 'B': " + set);
        System.out.println("size = " + set.size());
        System.out.println("isEmpty = " + set.isEmpty());

        set.clear();
        System.out.println("after clear: " + set);
    }

    // =========================================================================
    // 5) Bulk operations — addAll, removeAll, retainAll, containsAll
    // =========================================================================
    static void bulkOps() {
        System.out.println("\n=== BULK OPERATIONS");
        LinkedHashSet<Integer> s1 = new LinkedHashSet<>(List.of(1, 2, 3, 4, 5));
        LinkedHashSet<Integer> s2 = new LinkedHashSet<>(List.of(4, 5, 6, 7));

        // addAll → union
        LinkedHashSet<Integer> union = new LinkedHashSet<>(s1);
        union.addAll(s2);
        System.out.println("union (s1 ∪ s2) = " + union);

        // retainAll → intersection
        LinkedHashSet<Integer> inter = new LinkedHashSet<>(s1);
        inter.retainAll(s2);
        System.out.println("intersection (s1 ∩ s2) = " + inter);

        // removeAll → difference
        LinkedHashSet<Integer> diff = new LinkedHashSet<>(s1);
        diff.removeAll(s2);
        System.out.println("difference (s1 - s2) = " + diff);

        // containsAll
        System.out.println("s1 containsAll {1,2}? " + s1.containsAll(List.of(1,2)));
    }

    // =========================================================================
    // 6) Conversion to List (for indexed access)
    // =========================================================================
    static void toListDemo() {
        System.out.println("\n=== CONVERT TO LIST");
        LinkedHashSet<String> lhs = new LinkedHashSet<>(List.of("dog", "cat", "bird"));
        List<String> asList = new ArrayList<>(lhs); // preserves insertion order
        System.out.println("list = " + asList);
        System.out.println("get(1) = " + asList.get(1));
    }

    // =========================================================================
    // main — run all demos
    // =========================================================================
    public static void main(String[] args) {
        basics();
        nullsDemo();
        iterationDemo();
        coreMethods();
        bulkOps();
        toListDemo();
    }
}
