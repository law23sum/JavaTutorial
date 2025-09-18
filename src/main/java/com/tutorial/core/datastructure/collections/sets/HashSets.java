package com.tutorial.core.datastructure.collections.sets;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * HashSet — “nearly full API tour” with common + advanced patterns.
 *
 * BUILD & RUN:
 *   javac com/tutorial/core/collections/sets/HashSetDeepDive.java && \
 *   java com.tutorial.core.collections.sets.HashSetDeepDive
 */
public class HashSets {

    // =========================================================================
    // 0) CONSTRUCTORS & CONFIG
    //    - HashSet()
    //    - HashSet(int initialCapacity)
    //    - HashSet(int initialCapacity, float loadFactor)
    //    - HashSet(Collection<? extends E> c)
    //
    // Notes:
    //   * initialCapacity is a hint for the backing hash table size.
    //   * loadFactor (default 0.75f) controls resize threshold (size > capacity*loadFactor).
    //   * order is NOT guaranteed; use LinkedHashSet for insertion-order; TreeSet for sorted.
    // =========================================================================
    static void constructors() {
        System.out.println("=== CONSTRUCTORS");
        HashSet<String> a = new HashSet<>();                    // default
        HashSet<String> b = new HashSet<>(128);                 // capacity hint
        HashSet<String> c = new HashSet<>(256, 0.75f);          // capacity + load factor
        HashSet<String> d = new HashSet<>(List.of("a","b","c"));// copy from collection

        a.add("x"); b.add("y"); c.add("z");
        System.out.println("a=" + a + " b=" + b + " c=" + c + " d=" + d);
    }

    // =========================================================================
    // 1) BASICS — add, contains, remove, size, isEmpty, clear
    // =========================================================================
    static void basics() {
        System.out.println("\n=== BASICS");
        HashSet<String> set = new HashSet<>();

        // add(E) — inserts if absent; returns true iff element was not already present
        set.add("alpha");
        set.add("beta");
        set.add("gamma");
        boolean addedAgain = set.add("alpha"); // false (duplicate ignored)
        System.out.println("After adds: " + set + " | addedAgain? " + addedAgain);

        // contains(Object) — membership test (O(1) average)
        System.out.println("contains 'beta'? " + set.contains("beta"));

        // remove(Object) — remove if present; returns true iff found
        boolean removed = set.remove("gamma");
        System.out.println("removed 'gamma'? " + removed + " -> " + set);

        // size() / isEmpty()
        System.out.println("size=" + set.size() + " isEmpty=" + set.isEmpty());

        // clear() — remove everything
        set.clear();
        System.out.println("after clear -> size=" + set.size() + " isEmpty=" + set.isEmpty());
    }

    // =========================================================================
    // 2) BULK OPS — addAll, containsAll, removeAll, retainAll
    // =========================================================================
    static void bulkOps() {
        System.out.println("\n=== BULK OPS");
        HashSet<String> A = new HashSet<>(Set.of("a","b","c"));
        HashSet<String> B = new HashSet<>(Set.of("b","c","d"));

        // containsAll(Collection) — subset test
        System.out.println("A containsAll {b,c}? " + A.containsAll(Set.of("b","c")));

        // addAll(Collection) — union (in-place)
        HashSet<String> union = new HashSet<>(A);
        union.addAll(B);
        System.out.println("union A∪B: " + union);

        // retainAll(Collection) — intersection (in-place)
        HashSet<String> inter = new HashSet<>(A);
        inter.retainAll(B);
        System.out.println("intersection A∩B: " + inter);

        // removeAll(Collection) — difference (in-place)
        HashSet<String> diff = new HashSet<>(A);
        diff.removeAll(B);
        System.out.println("difference A−B: " + diff);
    }

    // =========================================================================
    // 3) ITERATION — enhanced-for, Iterator (with remove), forEach, spliterator
    // =========================================================================
    static void iteration() {
        System.out.println("\n=== ITERATION");
        HashSet<Integer> set = new HashSet<>(Set.of(1,2,3,4,5,6));

        // Enhanced-for (read-only)
        System.out.print("for-each: ");
        for (int v : set) System.out.print(v + " ");
        System.out.println();

        // Iterator — safe structural removal during traversal
        System.out.print("iterator (remove evens): ");
        for (Iterator<Integer> it = set.iterator(); it.hasNext();) {
            int v = it.next();
            if (v % 2 == 0) it.remove(); // legal and safe
        }
        System.out.println(set);

        // forEach(Consumer) — visit elements (unordered)
        System.out.print("forEach print: ");
        set.forEach(x -> System.out.print(x + " "));
        System.out.println();

        // spliterator() — low-level traversal support (can split for parallel)
        System.out.print("spliterator tryAdvance: ");
        Spliterator<Integer> sp = set.spliterator();
        while (sp.tryAdvance(x -> System.out.print(x + " "))) { /* iterate */ }
        System.out.println();
    }

    // =========================================================================
    // 4) removeIf — predicate-based filtering in-place
    // =========================================================================
    static void removeIfDemo() {
        System.out.println("\n=== removeIf");
        HashSet<String> words = new HashSet<>(Set.of("ALPHA","beta","Gamma","delta","OMEGA"));

        // Predicate: remove strings that are not all upper-case (keep “constants” style)
        Predicate<String> notAllUpper = s -> !s.equals(s.toUpperCase());
        boolean changed = words.removeIf(notAllUpper); // returns true if set changed
        System.out.println("changed=" + changed + " -> " + words);
    }

    // =========================================================================
    // 5) toArray() / toArray(T[]) — conversion to arrays (typed + untyped)
    // =========================================================================
    static void toArrayDemo() {
        System.out.println("\n=== toArray");
        HashSet<String> set = new HashSet<>(Set.of("a","b","c"));

        // Object[] toArray()
        Object[] arr1 = set.toArray();
        System.out.println("Object[]: " + Arrays.toString(arr1));

        // <T> T[] toArray(T[]) — type-safe array (size exact or zero for dynamic sizing)
        String[] arr2 = set.toArray(new String[0]); // common idiom
        System.out.println("String[]: " + Arrays.toString(arr2));
    }

    // =========================================================================
    // 6) equals & hashCode — set semantics (order-insensitive)
    // =========================================================================
    static void equalsHashCodeDemo() {
        System.out.println("\n=== equals & hashCode");
        Set<String> s1 = new HashSet<>(List.of("x","y","z"));
        Set<String> s2 = new HashSet<>(List.of("z","y","x"));

        // equals is order-insensitive for sets
        System.out.println("s1.equals(s2)? " + s1.equals(s2));
        System.out.println("s1.hashCode()==s2.hashCode()? " + (s1.hashCode() == s2.hashCode()));
    }

    // =========================================================================
    // 7) clone() — shallow copy (elements are not cloned)
    // =========================================================================
    @SuppressWarnings("unchecked")
    static void cloneDemo() {
        System.out.println("\n=== clone()");
        HashSet<String> original = new HashSet<>(Set.of("a","b","c"));
        HashSet<String> copy = (HashSet<String>) original.clone(); // shallow clone
        original.add("d"); // modifying original does not change 'copy' membership
        System.out.println("original=" + original + " | clone=" + copy);
    }

    // =========================================================================
    // 8) Streams — stream(), parallelStream() (dedupe, set algebra, grouping)
    // =========================================================================
    static void streamsDemo() {
        System.out.println("\n=== Streams");

        HashSet<String> set = new HashSet<>(List.of(
                "Ada","Grace","Turing","Ada","Babbage","grace"));

        // Lower-case dedup via stream + collectors
        Set<String> lower = set.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(HashSet::new));
        System.out.println("lower-dedup: " + lower);

        // Parallel stream — heavy computation example (toy: expensive scoring)
        int score = set.parallelStream()
                .mapToInt(s -> expensiveScore(s))
                .sum();
        System.out.println("parallel score = " + score);

        // Set algebra via streams: A ∩ B
        Set<Integer> A = new HashSet<>(Set.of(1,2,3,4,5));
        Set<Integer> B = new HashSet<>(Set.of(3,4,5,6,7));
        Set<Integer> inter = A.stream()
                .filter(B::contains)
                .collect(Collectors.toSet());
        System.out.println("intersection via stream: " + inter);
    }

    // Mock expensive function (CPU work stand-in)
    static int expensiveScore(String s) {
        int acc = 0;
        for (int i = 0; i < 100_000; i++) acc ^= (s.hashCode() + i);
        return acc & 0xFF;
    }

    // =========================================================================
    // 9) Variants & Polymorphism — HashSet vs LinkedHashSet vs TreeSet
    // =========================================================================
    static void variants() {
        System.out.println("\n=== Variants & Polymorphism");
        // Program to the interface where possible:
        Set<String> hash = new HashSet<>(List.of("c","a","b"));         // arbitrary order
        Set<String> link = new LinkedHashSet<>(List.of("c","a","b"));   // insertion order
        Set<String> tree = new TreeSet<>(List.of("c","a","b"));         // sorted order
        System.out.println("HashSet       : " + hash);
        System.out.println("LinkedHashSet : " + link);
        System.out.println("TreeSet       : " + tree);
    }

    // =========================================================================
    // 10) Custom Objects — equals/hashCode correctness matters
    // =========================================================================
    static final class Person {
        final String name;
        final int age;
        Person(String n, int a){ name=n; age=a; }

        // Define identity for set semantics. Here, (name,age) pair defines uniqueness.
        @Override public boolean equals(Object o){
            if (this == o) return true;
            if (!(o instanceof Person)) return false;
            Person p = (Person) o;
            return age == p.age && Objects.equals(name, p.name);
        }
        @Override public int hashCode(){ return Objects.hash(name, age); }
        @Override public String toString(){ return name + "(" + age + ")"; }
    }

    static void customObjects() {
        System.out.println("\n=== Custom Objects");
        Set<Person> people = new HashSet<>();
        people.add(new Person("Ada", 36));
        people.add(new Person("Ada", 36)); // deduped by equals/hashCode
        people.add(new Person("Grace", 85));
        System.out.println("people=" + people);

        // removeIf with custom predicate: remove all age < 50
        people.removeIf(p -> p.age < 50);
        System.out.println("age>=50 -> " + people);
    }

    // =========================================================================
    // main — run the whole tour
    // =========================================================================
    public static void main(String[] args) {
        constructors();
        basics();
        bulkOps();
        iteration();
        removeIfDemo();
        toArrayDemo();
        equalsHashCodeDemo();
        cloneDemo();
        streamsDemo();
        variants();
        customObjects();
    }
}
