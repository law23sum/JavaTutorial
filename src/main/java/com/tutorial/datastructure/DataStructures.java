package com.tutorial.datastructure;

import java.util.*;

/**
 * Java Data Structures — “one-file tour” with focused explanations.
 *
 * Covered:
 *  1) Primitive types (ranges, promotion footnotes)
 *  2) Arrays (1D/2D) + basic ops
 *  3) Strings (immutability) & StringBuilder (mutable)
 *  4) Lists (ArrayList vs LinkedList)
 *  5) Stacks & Queues (ArrayDeque-based; LIFO/FIFO)
 *  6) Binary Tree (generic Node + traversals)
 *  7) Heaps (PriorityQueue as min-/max-heap)
 *  8) Hash Tables (HashMap / HashSet)
 *  9) Binary Search Tree (BST insert/search/inorder)
 * 10) Non-polymorphic vs polymorphic processing (how/why)
 * 11) Interface vs concrete declarations (List vs ArrayList, Map vs HashMap, etc.)
 *
 * Build & run (JDK 17+):
 *   javac com/tutorial/core/datastructure/DataStructures.java && \
 *   java com.tutorial.core.datastructure.DataStructures
 */
public class DataStructures {

    // =========================================================================
    // 1) Primitive types — atomic values (not objects), stored by value.
    // =========================================================================
    static void primitivesDemo() {
        System.out.println("=== PRIMITIVES");
        byte b = 120;                      // 8-bit, -128..127
        short s = 30000;                   // 16-bit
        int i = 2_000_000_000;             // 32-bit
        long l = 9_000_000_000L;           // 64-bit

        float f = 3.14f;                   // 32-bit IEEE 754 (≈7 digits precision)
        double d = 2.718281828;            // 64-bit IEEE 754 (≈15–16 digits)

        char c = 'Ω';                      // 16-bit UTF-16 code unit
        boolean bool = true;               // logical

        System.out.println("byte   range: " + Byte.MIN_VALUE + ".." + Byte.MAX_VALUE);
        System.out.println("short  range: " + Short.MIN_VALUE + ".." + Short.MAX_VALUE);
        System.out.println("int    range: " + Integer.MIN_VALUE + ".." + Integer.MAX_VALUE);
        System.out.println("long   range: " + Long.MIN_VALUE + ".." + Long.MAX_VALUE);
        System.out.println("float  has ~7 digits precision; double ~15-16");
        System.out.println("sample values -> " + b + ", " + s + ", " + i + ", " + l + ", " + f + ", " + d + ", " + c + ", " + bool);
        // Note: byte/short/char promote to int in arithmetic; watch for overflow.
    }

    // =========================================================================
    // 2) Arrays — fixed-size, contiguous memory, O(1) indexed access.
    // =========================================================================
    static void arraysDemo() {
        System.out.println("\n=== ARRAYS");
        int[] a = {1, 2, 3, 4};           // literal construction
        a[2] = 99;                        // O(1) update
        System.out.println("a.length=" + a.length + " a[2]=" + a[2]);

        // 2D array (array-of-arrays). Rows may differ in length.
        int[][] grid = {
                {1, 2, 3},
                {4, 5, 6}
        };
        System.out.println("grid[1][0] = " + grid[1][0]);

        for (int v : a) System.out.print(v + " ");
        System.out.println();
    }

    // =========================================================================
    // 3) Strings (immutable) & StringBuilder (mutable).
    // =========================================================================
    static void stringsDemo() {
        System.out.println("\n=== STRINGS");
        String s1 = "Hello";
        String s2 = s1.replace("H", "Y");         // new String; s1 unchanged
        System.out.println("s1=" + s1 + " | s2=" + s2);

        StringBuilder sb = new StringBuilder("Hello"); // mutable buffer
        sb.append(" ").append("World");                // in-place edits
        System.out.println("StringBuilder -> " + sb);
    }

    // =========================================================================
    // 4) Lists — ordered, indexable collections.
    // =========================================================================
    static void listsDemo() {
        System.out.println("\n=== LISTS");

        // ✅ Best practice: program to the interface (flexible, polymorphic).
        // You can swap the concrete type later without changing call sites.
        List<String> arr = new ArrayList<>();   // declared as List, constructed as ArrayList
        arr.add("alpha");
        arr.add("beta");
        arr.add(1, "insert");                   // shifts tail → O(n)
        System.out.println("ArrayList via List: " + arr + " get(2)=" + arr.get(2));

        // If you need class-specific methods (e.g., ensureCapacity), you must use the concrete type.
        ArrayList<String> concrete = new ArrayList<>();
        concrete.ensureCapacity(100);           // ❗ only exists on ArrayList
        concrete.addAll(List.of("x","y","z"));
        System.out.println("ArrayList (concrete) : " + concrete);

        // Another List implementation
        List<String> link = new LinkedList<>(List.of("first", "second"));
        link.add(1, "middle");
        System.out.println("LinkedList via List: " + link + " get(2)=" + link.get(2)); // random access O(n)
    }

    // =========================================================================
    // 5) Stacks & Queues — use ArrayDeque for both (fast, resizable, no sync).
    // =========================================================================
    static void stacksQueuesDemo() {
        System.out.println("\n=== STACKS & QUEUES (ArrayDeque)");

        // Deque interface reference → flexible: can switch to another Deque impl later.
        Deque<Integer> stack = new ArrayDeque<>();   // LIFO
        stack.push(10); stack.push(20); stack.push(30);
        System.out.println("stack peek=" + stack.peek());
        System.out.println("stack pop=" + stack.pop());
        System.out.println("stack after pop: " + stack);

        Deque<String> queue = new ArrayDeque<>();    // FIFO
        queue.addLast("A"); queue.addLast("B"); queue.addLast("C");
        System.out.println("queue peek=" + queue.peekFirst());
        System.out.println("queue poll=" + queue.pollFirst());
        System.out.println("queue after poll: " + queue);
    }

    // =========================================================================
    // 6) Binary Tree — generic shape + traversals (not ordered).
    // =========================================================================
    static final class BTNode<T> {
        T val;
        BTNode<T> left, right;
        BTNode(T v) { this.val = v; }
    }
    static <T> void preorder(BTNode<T> n) { if (n==null) return; System.out.print(n.val+" "); preorder(n.left);  preorder(n.right); }
    static <T> void inorder (BTNode<T> n) { if (n==null) return; inorder(n.left); System.out.print(n.val+" ");   inorder(n.right);  }
    static <T> void postorder(BTNode<T> n){ if (n==null) return; postorder(n.left); postorder(n.right); System.out.print(n.val+" "); }
    static <T> void levelOrder(BTNode<T> root) {
        if (root == null) return;
        Deque<BTNode<T>> q = new ArrayDeque<>();
        q.add(root);
        while (!q.isEmpty()) {
            BTNode<T> cur = q.poll();
            System.out.print(cur.val + " ");
            if (cur.left != null) q.add(cur.left);
            if (cur.right != null) q.add(cur.right);
        }
    }
    static void binaryTreeDemo() {
        System.out.println("\n=== BINARY TREE (shape + traversals)");
        //      A
        //     / \
        //    B   C
        //   / \   \
        //  D   E   F
        BTNode<String> A = new BTNode<>("A");
        A.left = new BTNode<>("B");
        A.right = new BTNode<>("C");
        A.left.left = new BTNode<>("D");
        A.left.right = new BTNode<>("E");
        A.right.right = new BTNode<>("F");
        System.out.print("preorder:   "); preorder(A);   System.out.println();
        System.out.print("inorder:    "); inorder(A);    System.out.println();
        System.out.print("postorder:  "); postorder(A);  System.out.println();
        System.out.print("levelOrder: "); levelOrder(A); System.out.println();
    }

    // =========================================================================
    // 7) Heaps — PriorityQueue implements a binary heap (min-heap by default).
    // =========================================================================
    static void heapsDemo() {
        System.out.println("\n=== HEAPS (PriorityQueue)");
        PriorityQueue<Integer> min = new PriorityQueue<>();
        min.addAll(List.of(5, 1, 9, 2, 7));
        System.out.print("min-heap poll order: ");
        while (!min.isEmpty()) System.out.print(min.poll() + " ");
        System.out.println();

        PriorityQueue<Integer> max = new PriorityQueue<>(Comparator.reverseOrder());
        max.addAll(List.of(5, 1, 9, 2, 7));
        System.out.print("max-heap poll order: ");
        while (!max.isEmpty()) System.out.print(max.poll() + " ");
        System.out.println();
    }

    // =========================================================================
    // 8) Hash Tables — HashMap/HashSet, plus interface vs class examples.
    // =========================================================================
    static void hashTablesDemo() {
        System.out.println("\n=== HASH TABLES (HashMap / HashSet)");

        // ✅ Prefer interface in variables: Map<K,V> for flexibility.
        Map<String, Integer> freq = new HashMap<>(); // can swap to TreeMap/LinkedHashMap later
        for (String w : List.of("a","b","a","c","b","a")) {
            freq.put(w, freq.getOrDefault(w, 0) + 1);
        }
        System.out.println("HashMap via Map: " + freq);

        // If you need a HashMap-specific method (rare), use the concrete type.
        HashMap<String,Integer> concreteMap = new HashMap<>();
        concreteMap.put("x", 1);
        // concreteMap.replaceAll(...) exists on Map anyway; class-specific needs are uncommon.
        System.out.println("HashMap (concrete): " + concreteMap);

        // Set example (interface vs concrete)
        Set<Integer> unique = new HashSet<>(List.of(3,1,2,2,1,3,4)); // interface Set
        System.out.println("HashSet via Set: " + unique);

        // LinkedHashMap access-order (LRU-like) — still referenced by the Map interface.
        Map<String, Integer> lruLike = new LinkedHashMap<>(16, 0.75f, true);
        lruLike.put("A", 1); lruLike.put("B", 2); lruLike.put("C", 3);
        lruLike.get("A"); lruLike.get("B");
        System.out.println("LinkedHashMap (access-ordered via Map): " + lruLike);
    }

    // =========================================================================
    // 9) Binary Search Tree (BST) — ordered tree (left < root < right).
    // =========================================================================
    static final class BST {
        static final class Node { int key; Node left, right; Node(int k){ key=k; } }
        Node root;
        void insert(int k){ root = insertRec(root,k); }
        private Node insertRec(Node n,int k){
            if(n==null) return new Node(k);
            if(k<n.key) n.left = insertRec(n.left,k);
            else if(k>n.key) n.right = insertRec(n.right,k);
            return n;
        }
        boolean contains(int k){ return containsRec(root,k); }
        private boolean containsRec(Node n,int k){
            if(n==null) return false;
            if(k==n.key) return true;
            return k<n.key ? containsRec(n.left,k) : containsRec(n.right,k);
        }
        void inorderPrint(){ inorderRec(root); System.out.println(); }
        private void inorderRec(Node n){
            if(n==null) return;
            inorderRec(n.left); System.out.print(n.key+" "); inorderRec(n.right);
        }
    }
    static void bstDemo() {
        System.out.println("\n=== BINARY SEARCH TREE (BST)");
        BST bst = new BST();
        int[] keys = {8,3,10,1,6,14,4,7,13};
        for (int k : keys) bst.insert(k);
        System.out.print("inorder (sorted): "); bst.inorderPrint();
        System.out.println("contains 7?  " + bst.contains(7));
        System.out.println("contains 99? " + bst.contains(99));
    }

    // =========================================================================
    // 10) Non-polymorphic vs polymorphic examples (duplication vs reuse).
    // =========================================================================
    // A) Without polymorphism: separate methods per concrete type (brittle).
    static int sumArrayList(ArrayList<Integer> xs) {
        int s = 0; for (int i = 0; i < xs.size(); i++) s += xs.get(i); return s;
    }
    static int sumLinkedList(LinkedList<Integer> xs) {
        int s = 0; for (Integer v : xs) s += v; return s;
    }

    // B) With polymorphism: one method for all List impls (robust).
    static int sumList(List<Integer> xs) {
        int s = 0; for (Integer v : xs) s += v; return s;
    }

    static void nonPolymorphicDemo() {
        System.out.println("\n=== NON-POLYMORPHIC DEMO");
        ArrayList<Integer> a = new ArrayList<>(List.of(1,2,3));
        LinkedList<Integer> b = new LinkedList<>(List.of(4,5,6));
        System.out.println("sumArrayList  : " + sumArrayList(a)); // only ArrayList
        System.out.println("sumLinkedList : " + sumLinkedList(b)); // only LinkedList
        // Add a new list type? You'd write another sumXxx method → duplication.
    }
    static void polymorphicDemo() {
        System.out.println("\n=== POLYMORPHIC DEMO");
        List<Integer> a = new ArrayList<>(List.of(1,2,3));
        List<Integer> b = new LinkedList<>(List.of(4,5,6));
        System.out.println("sumList(ArrayList) : " + sumList(a));
        System.out.println("sumList(LinkedList): " + sumList(b));
        // Add CopyOnWriteArrayList later? sumList keeps working without changes.
    }

    // =========================================================================
    // 11) Interface vs Concrete Declarations — focused side-by-side examples.
    //
    //   General template:
    //       ____ fieldName = new ____;
    //
    //   • Left side (declared type, the underline before fieldName):
    //       - The "lens" through which you view the object.
    //       - Controls *what methods you can call* (interface = abstract/generic, class = specific).
    //
    //   • Right side (constructed type, the underline after new):
    //       - The actual class of the object created at runtime.
    //       - Controls *what the object really is* and how it works internally.
    //
    //   Cases:
    //   A) Matching types (concrete = concrete):
    //        ArrayList<String> listConcrete = new ArrayList<>();
    //        - All ArrayList methods available (e.g., ensureCapacity, trimToSize).
    //        - Locked in to ArrayList; swapping to LinkedList later requires refactoring.
    //
    //   B) Polymorphism (interface = concrete):
    //        List<String> listIface = new ArrayList<>();
    //        - Only methods guaranteed by List are visible (add, get, remove, …).
    //        - You can swap to LinkedList/CopyOnWriteArrayList later with minimal change.
    // =========================================================================
    static void interfaceVsConcreteExamples() {
        System.out.println("\n=== INTERFACE vs CONCRETE DECLARATIONS");

        // --- LIST ---
        List<String> listIface = new ArrayList<>(); // ✅ flexible: can switch to LinkedList later
        listIface.add("a"); listIface.add("b");
        // listIface.ensureCapacity(100); // ⛔ compile error: not in List API

        ArrayList<String> listConcrete = new ArrayList<>(); // 🚧 specific: tied to ArrayList
        listConcrete.ensureCapacity(100);                   // ✅ allowed (class-specific)
        listConcrete.add("x");

        // Swap example (only easy with interface on the left)
        listIface = new LinkedList<>(listIface); // ✅ no other code changes needed

        // --- SET ---
        Set<Integer> setIface = new HashSet<>(); // ✅ flexible
        setIface.add(1); setIface.add(2);
        HashSet<Integer> setConcrete = new HashSet<>(); // 🚧 specific
        setConcrete.add(3);

        // --- MAP ---
        Map<String,Integer> mapIface = new HashMap<>(); // ✅ flexible (could be TreeMap later)
        mapIface.put("k", 1);
        HashMap<String,Integer> mapConcrete = new HashMap<>(); // 🚧 specific
        mapConcrete.put("k2", 2);

        // --- QUEUE/DEQUE ---
        Deque<String> dqIface = new ArrayDeque<>(); // ✅ flexible
        dqIface.addLast("L"); dqIface.addFirst("F");
        ArrayDeque<String> dqConcrete = new ArrayDeque<>(); // 🚧 specific
        dqConcrete.add("Z");

        System.out.println("List(iface)=" + listIface.getClass().getSimpleName() +
                " | List(concrete)=" + listConcrete.getClass().getSimpleName());
        System.out.println("Set(iface)=" + setIface.getClass().getSimpleName() +
                " | Set(concrete)=" + setConcrete.getClass().getSimpleName());
        System.out.println("Map(iface)=" + mapIface.getClass().getSimpleName() +
                " | Map(concrete)=" + mapConcrete.getClass().getSimpleName());
        System.out.println("Deque(iface)=" + dqIface.getClass().getSimpleName() +
                " | Deque(concrete)=" + dqConcrete.getClass().getSimpleName());
    }

    // =========================================================================
    // main — run the tour
    // =========================================================================
    public static void main(String[] args) {
        primitivesDemo();
        arraysDemo();
        stringsDemo();
        listsDemo();
        stacksQueuesDemo();
        binaryTreeDemo();
        heapsDemo();
        hashTablesDemo();
        bstDemo();

        nonPolymorphicDemo();
        polymorphicDemo();

        interfaceVsConcreteExamples(); // 👈 focused side-by-side comparisons
    }
}
