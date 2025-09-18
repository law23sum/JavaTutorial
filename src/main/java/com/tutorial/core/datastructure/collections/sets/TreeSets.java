package com.tutorial.core.datastructure.collections.sets;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * TreeSetDeepDive — a practical tour of TreeSet / NavigableSet with richly commented examples.
 *
 * BUILD & RUN:
 *   javac com/tutorial/core/collections/sets/TreeSetDeepDive.java && \
 *   java com.tutorial.core.collections.sets.TreeSetDeepDive
 */
public class TreeSets {

    // =========================================================================
    // 0) CONSTRUCTORS & COMPARATORS
    //    TreeSet()                       -> natural ordering (elements must be Comparable)
    //    TreeSet(Comparator<? super E>)  -> custom comparator
    //    TreeSet(Collection<? extends E> c)
    //    TreeSet(SortedSet<E> s)
    //
    // NOTE: In TreeSet, "uniqueness" is decided by comparator comparison == 0
    // (not by equals()). So your comparator should be consistent with equals
    // to avoid surprising dedup behavior.
    // =========================================================================
    static void constructors() {
        System.out.println("=== CONSTRUCTORS & COMPARATORS");

        // Natural ordering (String implements Comparable by lexicographic order)
        TreeSet<String> natural = new TreeSet<>();
        natural.addAll(List.of("delta","alpha","charlie","bravo"));
        System.out.println("natural: " + natural); // [alpha, bravo, charlie, delta]

        // Custom comparator: case-insensitive, then tie-break by real case
        Comparator<String> ciThenReal =
                Comparator.comparing((String s) -> s.toLowerCase()).thenComparing(Comparator.naturalOrder());
        TreeSet<String> custom = new TreeSet<>(ciThenReal);
        custom.addAll(List.of("Ada","ada","Babbage","babbage","Turing"));
        System.out.println("custom (ciThenReal): " + custom);

        // From collection (uses natural order of Strings)
        TreeSet<String> fromCollection = new TreeSet<>(List.of("z","y","x"));
        System.out.println("fromCollection: " + fromCollection);

        // From existing SortedSet (copy)
        TreeSet<String> fromSorted = new TreeSet<>(custom);
        System.out.println("fromSorted(copy of custom): " + fromSorted);
    }

    // =========================================================================
    // 1) BASICS — add, contains, remove, size, isEmpty, clear; addAll/containsAll
    // =========================================================================
    static void basics() {
        System.out.println("\n=== BASICS");
        TreeSet<Integer> ts = new TreeSet<>(); // natural numeric order (ascending)

        // add(E): inserts if not considered equal to existing (compare==0)
        ts.add(10); ts.add(5); ts.add(7); ts.add(10); // second 10 ignored
        System.out.println("after adds: " + ts);

        // contains(Object): O(log n)
        System.out.println("contains 7? " + ts.contains(7));

        // remove(Object): O(log n)
        ts.remove(5);
        System.out.println("after remove 5: " + ts);

        // bulk ops
        ts.addAll(List.of(20, 30, 40));
        System.out.println("after addAll: " + ts);
        System.out.println("containsAll {7,20}? " + ts.containsAll(Set.of(7, 20)));

        // size, isEmpty, clear
        System.out.println("size=" + ts.size() + ", isEmpty=" + ts.isEmpty());
        ts.clear();
        System.out.println("after clear: size=" + ts.size() + ", set=" + ts);
    }

    // =========================================================================
    // 2) ITERATION — ascending, descending, Iterator (with remove), forEach, spliterator
    // =========================================================================
    static void iteration() {
        System.out.println("=== ITERATION");

        // Build from a List (allows duplicates), then TreeSet will sort + dedup.
        List<Integer> src = List.of(3, 1, 4, 1, 5, 9);
        TreeSet<Integer> ts = new TreeSet<>(src);

        // Forward iteration (ascending)
        System.out.print("asc : ");
        for (Integer v : ts) System.out.print(v + " ");
        System.out.println();

        // Descending iteration
        System.out.print("desc: ");
        for (Iterator<Integer> it = ts.descendingIterator(); it.hasNext(); )
            System.out.print(it.next() + " ");
        System.out.println();

        // forEach, higher/lower, floor/ceiling samples
        System.out.println("first=" + ts.first() + ", last=" + ts.last());
        System.out.println("floor(4)=" + ts.floor(4) + ", ceiling(4)=" + ts.ceiling(4));
        System.out.println("lower(4)=" + ts.lower(4) + ", higher(4)=" + ts.higher(4));
    }


    // =========================================================================
    // 3) NAVIGABLESET SUPERPOWERS — first/last, lower/higher, floor/ceiling, poll*
    // =========================================================================
    static void navigableOps() {
        System.out.println("\n=== NAVIGABLE OPS");
        TreeSet<Integer> ts = new TreeSet<>(Set.of(10,20,30,40));

        // Boundary scans
        System.out.println("first=" + ts.first() + ", last=" + ts.last());

        // Strict neighbors
        System.out.println("lower(20)=" + ts.lower(20));   // <  20 -> 10
        System.out.println("higher(20)=" + ts.higher(20)); // >  20 -> 30

        // Weak neighbors
        System.out.println("floor(25)=" + ts.floor(25));   // <= 25 -> 20
        System.out.println("ceiling(25)=" + ts.ceiling(25)); // >= 25 -> 30

        // Poll (remove + return)
        System.out.println("pollFirst=" + ts.pollFirst()); // removes 10
        System.out.println("pollLast=" + ts.pollLast());   // removes 40
        System.out.println("after polls -> " + ts);        // [20,30]
    }

    // =========================================================================
    // 4) RANGE VIEWS — headSet, tailSet, subSet (with inclusive flags), descendingSet
    //    These return live VIEWS backed by the original set (changes reflect both ways).
    // =========================================================================
    static void rangeViews() {
        System.out.println("\n=== RANGE VIEWS");
        TreeSet<Integer> ts = new TreeSet<>(Set.of(10,20,30,40,50,60));

        // half-open style by default (toElement exclusive)
        SortedSet<Integer> head = ts.headSet(40);       // <40
        SortedSet<Integer> tail = ts.tailSet(30);       // >=30
        SortedSet<Integer> mid  = ts.subSet(20, 50);    // [20..50)

        System.out.println("head <40: " + head);
        System.out.println("tail >=30: " + tail);
        System.out.println("mid [20..50): " + mid);

        // Inclusive flags (NavigableSet versions)
        NavigableSet<Integer> headInc = ts.headSet(40, true);  // <=40
        NavigableSet<Integer> subInc  = ts.subSet(20, true, 50, true); // [20..50]
        System.out.println("head <=40: " + headInc);
        System.out.println("sub [20..50]: " + subInc);

        // Descending view
        NavigableSet<Integer> desc = ts.descendingSet();
        System.out.println("descending view: " + desc);

        // Live view: mutate view, original reflects change
        head.add(15); // adding 15 is valid (<40)
        System.out.println("after head.add(15) -> ts=" + ts);
        try {
            head.add(100); // invalid (>=40) -> throws IllegalArgumentException
        } catch (IllegalArgumentException ex) {
            System.out.println("head.add(100) -> " + ex);
        }
    }

    // =========================================================================
    // 5) removeIf — in-place filter using order-aware predicate if needed
    // =========================================================================
    static void removeIfDemo() {
        System.out.println("\n=== removeIf");
        TreeSet<String> names = new TreeSet<>(List.of("Ada","Babbage","Turing","Lovelace","Knuth"));

        // Keep only names with length <= 5
        boolean changed = names.removeIf(s -> s.length() > 5);
        System.out.println("changed=" + changed + " -> " + names);
    }

    // =========================================================================
    // 6) CONVERSIONS — toArray(), toArray(T[])
    // =========================================================================
    static void toArrayDemo() {
        System.out.println("\n=== toArray");
        TreeSet<String> ts = new TreeSet<>(List.of("b","a","c"));

        Object[] a1 = ts.toArray();
        System.out.println("Object[]: " + Arrays.toString(a1));

        String[] a2 = ts.toArray(new String[0]);
        System.out.println("String[]: " + Arrays.toString(a2));
    }

    // =========================================================================
    // 7) EQUALITY & DEDUP — comparator defines duplicates; equals() is Set semantics
    //    - equals() ignores order (set semantics).
    //    - BUT insertion suppression uses compare()==0; two distinct objects
    //      that compare equal (even if !equals) cannot coexist in the set.
    // =========================================================================
    static void equalityAndDedup() {
        System.out.println("\n=== EQUALITY & DEDUP");
        // Case-insensitive comparator: "Ada" and "ada" are considered the same key
        Comparator<String> ci = String::compareToIgnoreCase;
        TreeSet<String> ts = new TreeSet<>(ci);
        ts.add("Ada");
        ts.add("ada"); // suppressed because compareIgnoreCase==0
        ts.add("Turing");
        System.out.println("ts (ci): " + ts + "  size=" + ts.size());

        Set<String> s1 = new TreeSet<>(List.of("a","b","c"));
        Set<String> s2 = new TreeSet<>(List.of("c","b","a"));
        System.out.println("s1.equals(s2)? " + s1.equals(s2)); // true (set semantics)
        System.out.println("hashCodes equal? " + (s1.hashCode() == s2.hashCode()));
    }

    // =========================================================================
    // 8) clone, stream, parallelStream
    // =========================================================================
    @SuppressWarnings("unchecked")
    static void cloneAndStreams() {
        System.out.println("\n=== clone & streams");
        TreeSet<Integer> ts = new TreeSet<>(Set.of(5,1,4,2,3));

        // clone(): shallow copy
        TreeSet<Integer> copy = (TreeSet<Integer>) ts.clone();
        ts.add(6);
        System.out.println("orig=" + ts + " | clone=" + copy);

        // stream(): range filter + map + collect
        Set<Integer> squaresGE3 = ts.stream()
                .filter(x -> x >= 3)
                .map(x -> x*x)
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.println("squares >=3: " + squaresGE3);

        // parallelStream(): demonstrate on larger data
        int sum = new TreeSet<>(ts) // copy for clarity
                .parallelStream()
                .mapToInt(Integer::intValue)
                .sum();
        System.out.println("parallel sum: " + sum);
    }

    // =========================================================================
    // 9) COMPLEX EXAMPLE A — Leaderboard top-K using descendingSet() and poll
    // =========================================================================
    static void leaderboardTopK() {
        System.out.println("\n=== Leaderboard (top-K)");
        // Score record: higher score first, then name
        record Score(String name, int score) {}
        Comparator<Score> byScoreDesc =
                Comparator.<Score>comparingInt(s -> s.score).reversed()
                        .thenComparing(s -> s.name);

        TreeSet<Score> board = new TreeSet<>(byScoreDesc);
        board.addAll(List.of(
                new Score("Ada", 92),
                new Score("Turing", 98),
                new Score("Grace", 98),
                new Score("Knuth", 89),
                new Score("Babbage", 91)
        ));

        // Top-3 snapshot
        List<Score> top3 = board.stream().limit(3).toList();
        System.out.println("top3: " + top3);

        // Pop the very best (e.g., award then remove)
        Score winner = board.pollFirst(); // highest due to reversed comparator
        System.out.println("winner: " + winner);
        System.out.println("board after pollFirst: " + board);
    }

    // =========================================================================
    // 10) COMPLEX EXAMPLE B — Prefix search (autocomplete) using subSet boundaries.
    //      For strings with natural lexicographic order, all strings with prefix P
    //      lie in [P, P + highSentinel).
    // =========================================================================
    static void prefixSearch() {
        System.out.println("\n=== Prefix search (autocomplete)");
        TreeSet<String> dict = new TreeSet<>(List.of(
                "apple","applet","apply","apt","banana","band","bandit","bank"));

        String prefix = "app";
        // Build an exclusive upper bound by appending a high sentinel character.
        // For ASCII, '{' is after 'z'. For general Unicode, use prefix + '\uFFFF'.
        String upper = prefix + '{';

        NavigableSet<String> view = dict.subSet(prefix, true, upper, false);
        System.out.println("words with prefix '" + prefix + "': " + view);
    }

    // =========================================================================
    // 11) CUSTOM OBJECTS — comparator consistency with equals
    //      If comparator considers only 'id', distinct objects with same id will dedupe.
    // =========================================================================
    static final class Person {
        final int id;
        final String name;
        Person(int id, String name){ this.id=id; this.name=name; }
        @Override public String toString(){ return name + "#" + id; }
        // equals/hashCode left as Object's defaults for demo purposes
    }

    static void comparatorConsistency() {
        System.out.println("\n=== Comparator consistency with equals (gotcha)");
        // Comparator that uses ONLY id -> names will be ignored for uniqueness
        Comparator<Person> byId = Comparator.comparingInt(p -> p.id);
        TreeSet<Person> people = new TreeSet<>(byId);
        people.add(new Person(1, "Ada"));
        people.add(new Person(1, "NotAda")); // compare==0 -> suppressed
        people.add(new Person(2, "Grace"));
        System.out.println("people(byId): " + people);
        System.out.println("Note: Different names with same id collapsed because byId says compare==0");
    }

    // =========================================================================
    // 12) ADVANCED: removeIf on a view + subrange logic with inclusivity flags
    // =========================================================================
    static void viewFiltering() {
        System.out.println("\n=== View filtering");
        TreeSet<Integer> nums = new TreeSet<>();
        nums.addAll(Arrays.asList(2,3,5,7,11,13,17,19,23,29));

        // Work only within [10..20] and remove odds not divisible by 5
        NavigableSet<Integer> mid = nums.subSet(10, true, 20, true);
        boolean changed = mid.removeIf(n -> (n % 2 == 1) && (n % 5 != 0));
        System.out.println("changed=" + changed + " mid=" + mid + " nums=" + nums);
    }

    // =========================================================================
    // main — run the whole tour
    // =========================================================================
    public static void main(String[] args) {
        constructors();
        basics();
        iteration();
        navigableOps();
        rangeViews();
        removeIfDemo();
        toArrayDemo();
        equalityAndDedup();
        cloneAndStreams();
        leaderboardTopK();
        prefixSearch();
        comparatorConsistency();
        viewFiltering();
    }
}
