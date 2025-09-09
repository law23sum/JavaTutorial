package com.tutorial.core.datastructure;

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
 *
 * Build & run (JDK 17+):
 *   javac com/tutorial/foundation/DataStructuresAllInOne.java && \
 *   java com.tutorial.foundation.DataStructuresAllInOne
 */
public class DataStructures {

    // =========================================================================
    // 1) Primitive types — atomic values (not objects). Stored by value.
    //    Ranges are fixed; arithmetic promotes smaller types to int.
    // =========================================================================
    static void primitivesDemo() {
        System.out.println("=== PRIMITIVES");
        byte b = 120;                      // 8-bit, -128..127
        short s = 30000;                   // 16-bit
        int i = 2_000_000_000;             // 32-bit
        long l = 9_000_000_000L;           // 64-bit

        float f = 3.14f;                   // 32-bit IEEE 754
        double d = 2.718281828;            // 64-bit IEEE 754

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
    // 2) Arrays — fixed-size, contiguous, O(1) index access. Resizing => copy.
    // =========================================================================
    static void arraysDemo() {
        System.out.println("\n=== ARRAYS");
        int[] a = {1, 2, 3, 4};
        a[2] = 99;
        System.out.println("a.length=" + a.length + " a[2]=" + a[2]);

        // 2D array (array-of-arrays)
        int[][] grid = {
                {1, 2, 3},
                {4, 5, 6}
        };
        System.out.println("grid[1][0] = " + grid[1][0]);

        // Iterate
        for (int v : a) System.out.print(v + " ");
        System.out.println();
    }

    // =========================================================================
    // 3) Strings (immutable) & StringBuilder (mutable char buffer)
    // =========================================================================
    static void stringsDemo() {
        System.out.println("\n=== STRINGS");
        String s1 = "Hello";
        String s2 = s1.replace("H", "Y");         // creates a new String; s1 unchanged
        System.out.println("s1=" + s1 + " | s2=" + s2);

        StringBuilder sb = new StringBuilder("Hello");
        sb.append(" ").append("World");           // mutates buffer in place
        System.out.println("StringBuilder -> " + sb.toString());
    }

    // =========================================================================
    // 4) Lists — ordered, indexable collections.
    //    ArrayList: dynamic array (O(1) amortized append; O(n) middle insert/remove).
    //    LinkedList: doubly-linked (O(1) add/remove via iterator; O(n) random access).
    // =========================================================================
    static void listsDemo() {
        System.out.println("\n=== LISTS");
        List<String> arr = new ArrayList<>();
        arr.add("alpha");
        arr.add("beta");
        arr.add(1, "insert");                 // shifts tail
        System.out.println("ArrayList: " + arr + " get(2)=" + arr.get(2));

        List<String> link = new LinkedList<>(List.of("first", "second"));
        link.add(1, "middle");
        System.out.println("LinkedList: " + link + " get(2)=" + link.get(2));
    }

    // =========================================================================
    // 5) Stacks & Queues — use ArrayDeque for both (fast, resizable).
    //    Stack = LIFO; Queue = FIFO. Deque = double-ended queue.
    // =========================================================================
    static void stacksQueuesDemo() {
        System.out.println("\n=== STACKS & QUEUES (ArrayDeque)");

        // Stack (LIFO)
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(10); stack.push(20); stack.push(30); // push adds to front
        System.out.println("stack peek=" + stack.peek()); // 30
        System.out.println("stack pop=" + stack.pop());   // 30
        System.out.println("stack after pop: " + stack);

        // Queue (FIFO)
        Deque<String> queue = new ArrayDeque<>();
        queue.addLast("A"); queue.addLast("B"); queue.addLast("C"); // enqueue at tail
        System.out.println("queue peek=" + queue.peekFirst()); // A
        System.out.println("queue poll=" + queue.pollFirst()); // A
        System.out.println("queue after poll: " + queue);
    }

    // =========================================================================
    // 6) Binary Tree — generic Node + traversals
    //    Not necessarily ordered; demonstration of shape + recursion.
    // =========================================================================
    static final class BTNode<T> {
        T val;
        BTNode<T> left, right;
        BTNode(T v) { this.val = v; }
    }

    static <T> void preorder(BTNode<T> n) {   // root, left, right
        if (n == null) return;
        System.out.print(n.val + " ");
        preorder(n.left);
        preorder(n.right);
    }
    static <T> void inorder(BTNode<T> n) {    // left, root, right
        if (n == null) return;
        inorder(n.left);
        System.out.print(n.val + " ");
        inorder(n.right);
    }
    static <T> void postorder(BTNode<T> n) {  // left, right, root
        if (n == null) return;
        postorder(n.left);
        postorder(n.right);
        System.out.print(n.val + " ");
    }
    static <T> void levelOrder(BTNode<T> root) { // BFS using queue
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
    // 7) Heaps — PriorityQueue is a binary heap (min-heap by default).
    //    For max-heap, invert the comparator.
    //    Ops: add O(log n), peek O(1), poll O(log n).
    // =========================================================================
    static void heapsDemo() {
        System.out.println("\n=== HEAPS (PriorityQueue)");

        // Min-heap (default)
        PriorityQueue<Integer> min = new PriorityQueue<>();
        min.addAll(List.of(5, 1, 9, 2, 7));
        System.out.print("min-heap poll order: ");
        while (!min.isEmpty()) System.out.print(min.poll() + " ");
        System.out.println();

        // Max-heap via reverse order
        PriorityQueue<Integer> max = new PriorityQueue<>(Comparator.reverseOrder());
        max.addAll(List.of(5, 1, 9, 2, 7));
        System.out.print("max-heap poll order: ");
        while (!max.isEmpty()) System.out.print(max.poll() + " ");
        System.out.println();
    }

    // =========================================================================
    // 8) Hash Tables — HashMap (key->value), HashSet (unique keys).
    //    Average-case O(1) for put/get/contains; order not guaranteed (unless LinkedHash*).
    // =========================================================================
    static void hashTablesDemo() {
        System.out.println("\n=== HASH TABLES (HashMap / HashSet)");

        Map<String, Integer> freq = new HashMap<>();
        for (String w : List.of("a","b","a","c","b","a")) {
            freq.put(w, freq.getOrDefault(w, 0) + 1);
        }
        System.out.println("HashMap freq: " + freq);

        Set<Integer> unique = new HashSet<>(List.of(3,1,2,2,1,3,4));
        System.out.println("HashSet unique: " + unique);

        // If you need insertion order, use LinkedHashMap/LinkedHashSet.
        Map<String, Integer> lruLike = new LinkedHashMap<>(16, 0.75f, true); // access-ordered
        lruLike.put("A", 1); lruLike.put("B", 2); lruLike.put("C", 3);
        lruLike.get("A"); lruLike.get("B");       // touching A,B moves them to the end
        System.out.println("LinkedHashMap (access-ordered): " + lruLike);
    }

    // =========================================================================
    // 9) Binary Search Tree (BST) — ordered tree (left < root < right).
    //    Average: O(log n) search/insert; Worst-case: O(n) if unbalanced.
    // =========================================================================
    static final class BST {
        static final class Node {
            int key;
            Node left, right;
            Node(int k) { key = k; }
        }
        Node root;

        // Insert (no duplicates for simplicity)
        void insert(int k) { root = insertRec(root, k); }
        private Node insertRec(Node n, int k) {
            if (n == null) return new Node(k);
            if (k < n.key) n.left = insertRec(n.left, k);
            else if (k > n.key) n.right = insertRec(n.right, k);
            return n;
        }

        boolean contains(int k) { return containsRec(root, k); }
        private boolean containsRec(Node n, int k) {
            if (n == null) return false;
            if (k == n.key) return true;
            return k < n.key ? containsRec(n.left, k) : containsRec(n.right, k);
        }

        // In-order traversal yields sorted keys
        void inorderPrint() { inorderRec(root); System.out.println(); }
        private void inorderRec(Node n) {
            if (n == null) return;
            inorderRec(n.left);
            System.out.print(n.key + " ");
            inorderRec(n.right);
        }

        // Optional: delete (not shown to keep the demo concise)
    }

    static void bstDemo() {
        System.out.println("\n=== BINARY SEARCH TREE (BST)");
        BST bst = new BST();
        int[] keys = {8, 3, 10, 1, 6, 14, 4, 7, 13};
        for (int k : keys) bst.insert(k);

        System.out.print("inorder (sorted): ");
        bst.inorderPrint();

        System.out.println("contains 7?  " + bst.contains(7));
        System.out.println("contains 99? " + bst.contains(99));
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
    }
}
