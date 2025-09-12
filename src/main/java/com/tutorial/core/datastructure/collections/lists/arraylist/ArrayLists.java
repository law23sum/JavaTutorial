package com.tutorial.core.datastructure.collections.lists.arraylist;

import java.util.*;

/**
 * ArrayList Demo — common + advanced usage with deep, step-by-step notes.
 *
 * WHAT THIS FILE IS:
 *  - A compact, runnable tour of ArrayList with commentary that explains:
 *      • what each method does,
 *      • what arguments it receives (and acceptable shapes),
 *      • and the logical sequence of operations (why this line comes before/after).
 *
 * COVERED:
 *  1) Construction & basics (add, get, set, remove, size)
 *  2) Iteration styles (for, enhanced-for, Iterator, ListIterator)
 *  3) Searching & membership (contains/indexOf/lastIndexOf)
 *  4) Sorting & custom comparators (stable sorts, composite keys)
 *  5) Bulk operations (addAll, removeAll, retainAll, clear)
 *  6) Capacity tuning (initialCapacity, ensureCapacity, trimToSize)
 *  7) SubList views (backed by parent list) — caveats
 *  8) Custom objects in ArrayList (records/classes) + comparators
 *  9) Streams (filter/map/reduce) — functional style on top of ArrayList
 * 10) Polymorphism: using ArrayList via its interfaces
 *
 * BUILD & RUN:
 *   javac com/tutorial/core/collections/ArrayListTour.java && \
 *   java com.tutorial.core.collections.ArrayListTour
 */
public class ArrayLists {

    // =========================================================================
    // 1) BASICS — Construction, add, get, set, remove, size
    // =========================================================================
    static void basics() {
        System.out.println("=== BASICS");

        // CONSTRUCTORS:
        // - new ArrayList<>()                → empty list; grows as you add elements.
        // - new ArrayList<>(initialCapacity) → empty list with internal array sized upfront.
        // - new ArrayList<>(Collection c)    → copy elements of c in iteration order.
        java.util.ArrayList<String> list = new java.util.ArrayList<>();

        // add(E e): appends element to the end; returns boolean (always true for ArrayList).
        // ARG: e (any String here). SIDE EFFECT: grows list size by 1.
        list.add("alpha");
        list.add("beta");

        // add(int index, E e): inserts at index; shifts the tail to the right.
        // ARGS: index (0..size), e (String). Throws IndexOutOfBoundsException if index invalid.
        list.add(1, "insert");

        // LOGIC: after two appends and one insert at position 1, order is:
        // [ "alpha", "insert", "beta" ]
        System.out.println("List after adds: " + list);

        // get(int index): O(1) random access.
        // ARG: index (0..size-1). Throws if out-of-range.
        System.out.println("get(0) = " + list.get(0));

        // set(int index, E newValue): replaces element at index; returns old value.
        // Use to update in place (no size change).
        list.set(0, "ALPHA");
        System.out.println("After set: " + list);

        // remove(int index): removes element at index; shifts the tail left by one.
        list.remove(1);

        // remove(Object o): removes first occurrence equal to o (via equals), returns boolean.
        list.remove("beta");

        // LOGIC: after both removes, only "ALPHA" remains.
        System.out.println("After removes: " + list);

        // size(): current element count.
        System.out.println("size = " + list.size());
    }

    // =========================================================================
    // 2) ITERATION — classic for, enhanced-for, Iterator, ListIterator
    // =========================================================================
    static void iteration() {
        System.out.println("\n=== ITERATION");
        ArrayList<Integer> nums = new ArrayList<>(List.of(1,2,3,4,5));

        // classic for: index-based; safe for read/write via set(i, x); DO NOT structurally
        // modify via add/remove here unless you understand shifting.
        System.out.print("classic for: ");
        for (int i = 0; i < nums.size(); i++) {
            System.out.print(nums.get(i) + " ");
        }
        System.out.println();

        // enhanced-for: simplest read-only iteration; avoids index math; no direct index.
        System.out.print("enhanced for: ");
        for (int n : nums) {
            System.out.print(n + " ");
        }
        System.out.println();

        // Iterator: supports safe removal during iteration via iterator.remove().
        // SEQUENCE:
        //  1) obtain iterator()
        //  2) loop while hasNext()
        //  3) fetch next()
        //  4) optional: remove() removes the last returned element
        System.out.print("iterator: ");
        for (Iterator<Integer> it = nums.iterator(); it.hasNext(); ) {
            int v = it.next();
            // if (v % 2 == 0) it.remove(); // example of safe removal
            System.out.print(v + " ");
        }
        System.out.println();

        // ListIterator: bidirectional; can iterate backwards; supports add/set at cursor.
        // ARGS: starting index (here we start at size for reverse walk).
        System.out.print("listIterator (reverse): ");
        ListIterator<Integer> lit = nums.listIterator(nums.size());
        while (lit.hasPrevious()) {
            System.out.print(lit.previous() + " ");
        }
        System.out.println();
    }

    // =========================================================================
    // 3) SEARCHING — contains, indexOf, lastIndexOf
    // =========================================================================
    static void searching() {
        System.out.println("\n=== SEARCHING");
        ArrayList<String> list = new ArrayList<>(List.of("a","b","c","a","b"));

        // contains(Object o): linear scan, uses equals for comparison.
        System.out.println("list = " + list);
        System.out.println("contains 'b'? " + list.contains("b"));

        // indexOf(Object o): first index where equals(o) is true; -1 if not found.
        System.out.println("indexOf 'a' = " + list.indexOf("a"));

        // lastIndexOf(Object o): last index where equals(o) is true; -1 if not found.
        System.out.println("lastIndexOf 'a' = " + list.lastIndexOf("a"));
    }

    // =========================================================================
    // 4) SORTING — natural order, reverse, composite (thenComparing)
    // =========================================================================
    static void sorting() {
        System.out.println("\n=== SORTING");

        // Natural sort (Strings → lexicographic, Integers → numeric)
        ArrayList<String> names = new ArrayList<>(List.of("Grace","Ada","Turing"));
        // Collections.sort(List): legacy; delegates to list.sort(null).
        Collections.sort(names);
        System.out.println("Natural sort: " + names);

        // List.sort(Comparator): in-place stable sort (TimSort).
        names.sort(Comparator.reverseOrder());
        System.out.println("Reverse sort: " + names);

        // Composite comparator: first by length, then by natural order if tie.
        ArrayList<String> mixed = new ArrayList<>(List.of("banana","pear","apple"));
        // Comparator.comparingInt(func) maps String→int key (length), thenComparing as tiebreaker.
        mixed.sort(Comparator.comparingInt(String::length)
                .thenComparing(Comparator.naturalOrder()));
        System.out.println("Sort by length then alpha: " + mixed);
    }

    // =========================================================================
    // 5) BULK OPERATIONS — addAll, removeAll, retainAll, clear
    // =========================================================================
    static void bulkOps() {
        System.out.println("\n=== BULK OPERATIONS");
        ArrayList<Integer> list = new ArrayList<>(List.of(1,2,3,4,5));

        // addAll(Collection<? extends E> c): append all elements of c, preserving order.
        list.addAll(List.of(6,7,8));
        System.out.println("After addAll: " + list);

        // removeAll(Collection<?> c): remove any element that equals any member of c.
        // set semantics: this - c
        list.removeAll(List.of(2,4,6));
        System.out.println("After removeAll: " + list);

        // retainAll(Collection<?> c): keep only elements that also appear in c.
        // set semantics: this ∩ c
        list.retainAll(List.of(1,3,5,7));
        System.out.println("After retainAll: " + list);

        // clear(): remove all elements; size becomes 0.
        list.clear();
        System.out.println("After clear: " + list);
    }

    // =========================================================================
    // 6) CAPACITY TUNING — initial capacity, ensureCapacity, trimToSize
    // =========================================================================
    static void capacity() {
        System.out.println("\n=== CAPACITY");

        // ArrayList has a backing Object[] array. When full, it grows (≈1.5x).
        // If you know you’ll add ~N items, pre-sizing reduces reallocations/copies.
        ArrayList<Integer> list = new ArrayList<>(10); // initial capacity hint (not size)

        // ensureCapacity(int minCapacity): guarantees underlying array can hold at least minCapacity.
        // ARG: minCapacity (>= current size). No visible effect on size; improves future adds.
        list.ensureCapacity(100);

        // LOGIC: now add some elements; these will not trigger multiple resizes up to capacity.
        for (int i = 0; i < 20; i++) list.add(i);
        System.out.println("size=" + list.size());

        // trimToSize(): reduce internal array length to equal current size (may free memory).
        list.trimToSize();
    }

    // =========================================================================
    // 7) SUBLIST — view backed by parent list (structural coupling)
    // =========================================================================
    static void sublistDemo() {
        System.out.println("\n=== SUBLIST (backed view)");
        ArrayList<String> list = new ArrayList<>(List.of("a","b","c","d","e"));

        // subList(int fromIndex, int toIndex): half-open range [from, to).
        // ARGS: from (inclusive), to (exclusive). Throws if indices invalid or from>to.
        // IMPORTANT: the returned List is a VIEW backed by the original: modifications reflect both ways.
        List<String> sub = list.subList(1,4); // elements at indices 1,2,3 → "b","c","d"
        System.out.println("original: " + list);
        System.out.println("sublist:  " + sub);

        // set() on sub updates original at mapped index.
        sub.set(0, "B");   // replaces "b" with "B" at original index 1
        System.out.println("after sub.set -> original: " + list);

        // STRUCTURAL CAUTION:
        // Adding/removing in parent while holding a subList (or vice versa) can cause
        // ConcurrentModificationException or undefined coupling behavior. Keep lifetimes short.
    }

    // =========================================================================
    // 8) CUSTOM OBJECTS — store domain types and sort/filter them
    // =========================================================================
    static void customObjects() {
        System.out.println("\n=== CUSTOM OBJECTS");

        // record Person(String name, int age): compact immutable data carrier.
        // ArrayList<Person>: holds Person references; supports all list ops.
        record Person(String name, int age) {}

        ArrayList<Person> people = new ArrayList<>();
        // add(Person): append record instances.
        people.add(new Person("Ada", 36));
        people.add(new Person("Grace", 85));
        people.add(new Person("Turing", 41));

        // sort(Comparator<? super Person>): stable, in-place.
        // ARG: Comparator mapping Person to sort keys.
        people.sort(Comparator.comparingInt(Person::age));
        for (Person p : people) System.out.println(p);

        // Composite: by name length, then lexicographically by name.
        people.sort(Comparator.comparingInt((Person p) -> p.name().length())
                .thenComparing(Person::name));
        System.out.println("Sorted by name length then name: " + people);
    }

    // =========================================================================
    // 9) STREAMS — filter/map/reduce on top of ArrayList
    // =========================================================================
    static void streamsDemo() {
        System.out.println("\n=== STREAMS");
        ArrayList<Integer> nums = new ArrayList<>(List.of(1,2,3,4,5,6,7,8,9,10));

        // Pipeline:
        //  1) nums.stream()                    → create a sequential stream over list elements
        //  2) filter(predicate)                → keep evens
        //  3) mapToInt(mapper)                 → square each even into an int
        //  4) sum()                            → reduce to a single int
        int sumSquares = nums.stream()
                .filter(n -> n % 2 == 0)
                .mapToInt(n -> n * n)
                .sum();
        System.out.println("sum of even squares = " + sumSquares);

        // Another pipeline on Strings:
        List<String> words = new ArrayList<>(List.of("apple","banana","pear","fig","kiwi"));
        // Keep words length>3, sort, uppercase, print.
        words.stream()
                .filter(w -> w.length() > 3)
                .sorted()
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }

    // =========================================================================
    // 10) POLYMORPHISM — ArrayList implements multiple interfaces.
    //
    // Interfaces implemented by ArrayList:
    //   • List<E>        → ordered collection, positional access
    //   • Collection<E>  → generic collection API (add/remove/contains)
    //   • Iterable<E>    → allows enhanced-for loops & iterator()
    //   • RandomAccess   → marker: O(1) random access supported
    //   • Cloneable, Serializable (advanced, not shown here)
    //
    // WHY: You can declare variables/parameters as these interfaces to
    // restrict available methods or to make code flexible.
    // =========================================================================
    static void polymorphismDemo() {
        System.out.println("\n=== POLYMORPHISM WITH ARRAYLIST");

        // Concrete construction
        java.util.ArrayList<String> arr = new java.util.ArrayList<>(List.of("a","b","c"));

        // 1) Use as a List
        List<String> listView = arr;
        // List guarantees positional access, add/remove by index
        listView.add(1, "insert");
        System.out.println("List view: " + listView);

        // 2) Use as a Collection
        Collection<String> collView = arr;
        // Collection guarantees add, remove, contains, size
        collView.remove("c");
        System.out.println("Collection view: " + collView);

        // 3) Use as an Iterable
        Iterable<String> iterView = arr;
        // Iterable guarantees iterator() → works in enhanced-for
        System.out.print("Iterable view (enhanced-for): ");
        for (String s : iterView) System.out.print(s + " ");
        System.out.println();

        // 4) Use as RandomAccess
        // RandomAccess is a marker interface: it has no methods, but signals
        // that get(i) is efficient. Libraries may branch logic on this.
        if (arr instanceof RandomAccess) {
            System.out.println("This list supports fast random access!");
        }

        // LOGIC:
        // All four references (listView, collView, iterView, arr) point to the same object.
        // Methods allowed depend on declared type. Example:
        //   - listView.set(0,"X") is legal (List API)
        //   - collView.set(...) is illegal (Collection API doesn't know about indexes).
        //   - iterView.add(...) is illegal (Iterable has no add method).
        // But at runtime, they all act on the same underlying ArrayList.
    }

    // =========================================================================
    // main — run all demos in a clear top→bottom sequence
    // =========================================================================
    public static void main(String[] args) {
        // 1) Build muscle memory for core list operations.
        basics();

        // 2) Learn the four common iteration styles and when to use them.
        iteration();

        // 3) Query presence and positions via equality-based search.
        searching();

        // 4) Order your data predictably using natural and custom comparators.
        sorting();

        // 5) Apply set-like changes in bulk (add/remove/retain groups).
        bulkOps();

        // 6) Tune memory/growth behavior when you know future sizes.
        capacity();

        // 7) Use subviews responsibly; understand backing semantics.
        sublistDemo();

        // 8) Store domain objects; sort/combine on multiple keys.
        customObjects();

        // 9) Layer functional pipelines over ArrayList contents.
        streamsDemo();

        // 10) See how different interface "lenses" shape what you can do.
        polymorphismDemo();
    }
}
