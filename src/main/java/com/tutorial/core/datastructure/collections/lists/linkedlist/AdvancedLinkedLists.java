package com.tutorial.core.datastructure.collections.lists.linkedlist;

import java.util.*;

/**
 * Linked List Tour — custom pointers + java.util.LinkedList (one-file demo).
 *
 * WHAT THIS FILE SHOWS
 *  A) A minimal, well-commented singly linked list:
 *     - Node structure (value + next pointer)
 *     - insertHead / insertTail / insertAfter(key, value)
 *     - delete(key) / search(key) / size() / reverse() / toString()
 *     - step-by-step pointer moves (what, why, in what order)
 *
 *  B) The standard library LinkedList:
 *     - As List: indexed operations (O(n) random access)
 *     - As Deque (preferred): addFirst/addLast, removeFirst/removeLast, stack/queue use
 *     - Iteration (Iterator + ListIterator), bulk ops, polymorphic views (List/Deque/Queue)
 *
 * BUILD & RUN (JDK 17+):
 *   javac com/tutorial/core/datastructure/collections/lists/linkedlist/LinkedListTour.java && \
 *   java com.tutorial.core.datastructure.collections.lists.linkedlist.LinkedListTour
 */
public class AdvancedLinkedLists {

    // ======================================================================
    // A) CUSTOM SINGLY LINKED LIST — teaches pointer dance explicitly
    // ======================================================================

    /** Node: holds a value and a pointer to the next node (or null). */
    static final class Node<E> {
        E val;           // data payload
        Node<E> next;    // pointer to next node
        Node(E v) { this.val = v; }
    }

    /** A tiny singly linked list; head points to the first node (or null). */
    static final class MySinglyLinkedList<E> {
        private Node<E> head; // first node (null if empty)
        private int size;     // tracked for O(1) size()

        /** Insert at front. O(1). Sequence: newNode.next = head → head = newNode. */
        public void insertHead(E value) {
            Node<E> n = new Node<>(value);
            n.next = head;   // 1) link new node forward to current head
            head = n;        // 2) move head to new node
            size++;
        }

        /** Insert at tail. O(n) unless you track a tail pointer. */
        public void insertTail(E value) {
            Node<E> n = new Node<>(value);
            if (head == null) { // empty list → head becomes n
                head = n;
                size++;
                return;
            }
            Node<E> cur = head;
            while (cur.next != null) cur = cur.next; // walk to last node
            cur.next = n; // append
            size++;
        }

        /**
         * Insert AFTER the first node whose value equals 'key'.
         * Args: key (to find), value (to insert).
         * Steps: walk → find key → splice: new.next = cur.next; cur.next = new.
         */
        public boolean insertAfter(E key, E value) {
            Node<E> cur = head;
            while (cur != null && !Objects.equals(cur.val, key)) cur = cur.next;
            if (cur == null) return false; // key not found
            Node<E> n = new Node<>(value);
            n.next = cur.next;  // 1) new node points to node after 'cur'
            cur.next = n;       // 2) 'cur' now points to new node
            size++;
            return true;
        }

        /**
         * Delete the first node equal to 'key'.
         * Cases:
         *  - empty: no-op
         *  - head matches: move head forward
         *  - mid/tail: link previous.next = current.next (skip current)
         */
        public boolean delete(E key) {
            if (head == null) return false;
            if (Objects.equals(head.val, key)) {
                head = head.next;
                size--;
                return true;
            }
            Node<E> prev = head, cur = head.next;
            while (cur != null && !Objects.equals(cur.val, key)) {
                prev = cur;
                cur = cur.next;
            }
            if (cur == null) return false; // not found
            prev.next = cur.next; // unlink
            size--;
            return true;
        }

        /** Search linearly; return node or null. */
        public Node<E> search(E key) {
            Node<E> cur = head;
            while (cur != null) {
                if (Objects.equals(cur.val, key)) return cur;
                cur = cur.next;
            }
            return null;
        }

        /** Reverse the list in-place. O(n). */
        public void reverse() {
            // Three-pointer dance:
            // prev <- cur -> next
            Node<E> prev = null, cur = head;
            while (cur != null) {
                Node<E> next = cur.next; // 1) stash next
                cur.next = prev;         // 2) flip pointer
                prev = cur;              // 3) advance prev
                cur = next;              // 4) advance cur
            }
            head = prev;
        }

        public int size() { return size; }

        public boolean isEmpty() { return size == 0; }

        /** Build a string snapshot like: 30 -> 20 -> 10 -> null */
        @Override public String toString() {
            StringBuilder sb = new StringBuilder();
            Node<E> cur = head;
            while (cur != null) {
                sb.append(cur.val).append(" -> ");
                cur = cur.next;
            }
            return sb.append("null").toString();
        }
    }

    /** Demo for the custom singly linked list with clear pointer steps. */
    static void customLinkedListDemo() {
        System.out.println("=== CUSTOM SINGLY LINKED LIST (pointer walkthrough)");
        MySinglyLinkedList<Integer> list = new MySinglyLinkedList<>();

        // Build: [30, 20, 10] by inserting at head (O(1) each)
        list.insertHead(10);
        list.insertHead(20);
        list.insertHead(30);
        System.out.println("after insertHead ×3 : " + list);

        // Append tail values (O(n) each here since no tail pointer)
        list.insertTail(0);
        list.insertTail(-10);
        System.out.println("after insertTail ×2 : " + list);

        // Insert after given keys (splice in the middle)
        list.insertAfter(10, 9);
        list.insertAfter(9, 3);
        list.insertAfter(3, 2);
        list.insertAfter(2, 1);
        list.insertAfter(20, 16);
        list.insertAfter(16, 13);
        System.out.println("after insertAfter(...) : " + list);

        // Delete a few keys
        list.delete(30); // head case
        list.delete(9);  // middle case
        list.delete(-10);// tail case
        System.out.println("after deletes (30,9,-10): " + list);

        // Search & reverse
        System.out.println("search 16 -> " + (list.search(16) != null));
        list.reverse();
        System.out.println("after reverse            : " + list);
        System.out.println("size=" + list.size() + " | empty? " + list.isEmpty());
    }

    // ======================================================================
    // B) java.util.LinkedList — practical API + polymorphism
    // ======================================================================

    /**
     * As a Deque/Queue/Stack: O(1) head/tail operations.
     * NOTE: Random access get(i) is O(n); prefer index ops on ArrayList instead.
     */
    static void stdLinkedListDequeDemo() {
        System.out.println("\n=== java.util.LinkedList as Deque/Queue/Stack");

        Deque<String> dq = new LinkedList<>(); // program to the interface
        // Queue-style (FIFO)
        dq.addLast("A");  // enqueue
        dq.addLast("B");
        dq.addLast("C");
        System.out.println("queue peekFirst: " + dq.peekFirst()); // A
        System.out.println("queue pollFirst: " + dq.pollFirst()); // remove A
        System.out.println("after poll      : " + dq);

        // Stack-style (LIFO)
        dq.push("X");     // push at head
        dq.push("Y");
        System.out.println("stack peek      : " + dq.peek());      // Y
        System.out.println("stack pop       : " + dq.pop());       // remove Y
        System.out.println("after pop       : " + dq);

        // Head/tail symmetric ops
        dq.addFirst("HEAD");
        dq.addLast("TAIL");
        System.out.println("after addFirst/addLast : " + dq);
        System.out.println("removeFirst/removeLast : " + dq.removeFirst() + ", " + dq.removeLast());
        System.out.println("final deque            : " + dq);
    }

    /** As a List: legal to index, but remember get(i) is O(n). */
    static void stdLinkedListAsListDemo() {
        System.out.println("\n=== java.util.LinkedList as List (indexable, but O(n) random access)");

        List<Integer> lst = new LinkedList<>();
        lst.add(1); lst.add(2); lst.add(3);
        lst.add(1, 99);                 // insert at index (walks to index first)
        System.out.println("list after add/insert : " + lst);

        // Iteration styles
        System.out.print("Iterator forward       : ");
        for (Iterator<Integer> it = lst.iterator(); it.hasNext(); ) {
            System.out.print(it.next() + " ");
        }
        System.out.println();

        System.out.print("ListIterator backward  : ");
        ListIterator<Integer> lit = lst.listIterator(lst.size());
        while (lit.hasPrevious()) System.out.print(lit.previous() + " ");
        System.out.println();

        // Bulk ops (supported from Collection/List)
        lst.addAll(List.of(7,8,9));
        lst.removeAll(List.of(2,8));
        lst.retainAll(List.of(1,3,7,9,99));
        System.out.println("after bulk ops         : " + lst);

        // Sorting (makes sense, but O(n log n) comparisons + O(n) node walking costs)
        lst.sort(Comparator.naturalOrder());
        System.out.println("after sort             : " + lst);
    }

    /** Polymorphism: view the same LinkedList instance via different interfaces. */
    static void polymorphismViewsDemo() {
        System.out.println("\n=== Polymorphism with java.util.LinkedList");

        LinkedList<String> concrete = new LinkedList<>(List.of("alpha", "beta", "gamma"));

        // View as List — get/index operations visible
        List<String> asList = concrete;
        asList.add(1, "INS"); // inserts at index 1
        System.out.println("List view : " + asList);

        // View as Deque — head/tail ops visible
        Deque<String> asDeque = concrete;
        asDeque.addFirst("HEAD");
        asDeque.addLast("TAIL");
        System.out.println("Deque view: " + asDeque);

        // View as Queue — queue semantics (offer/poll/peek)
        Queue<String> asQueue = concrete;
        asQueue.offer("Q"); // enqueue at tail
        System.out.println("Queue view: " + asQueue);

        // All views point to the same object; operations reflect across views.
        System.out.println("Underlying concrete class: " + concrete.getClass().getName());
    }

    /** Quick performance intuition notes printed at runtime. */
    static void perfNotes() {
        System.out.println("\n=== Performance notes (rule of thumb)");
        System.out.println("- LinkedList: O(1) add/remove at ends or via iterator at position; O(n) random access.");
        System.out.println("- ArrayList : O(1) amortized append; O(1) random access; O(n) middle insert/remove (shifts).");
        System.out.println("- Choose LinkedList for many head/tail ops with iterators; otherwise prefer ArrayList.");
    }

    // ======================================================================
    // main — run all demos
    // ======================================================================
    public static void main(String[] args) {
        // A) Pointers-first learning with a small custom list
        customLinkedListDemo();

        // B) Standard library LinkedList in its best light: as a Deque
        stdLinkedListDequeDemo();

        //   Also valid as a List (but know the O(n) access cost)
        stdLinkedListAsListDemo();

        //   How polymorphism changes the visible API without changing the object
        polymorphismViewsDemo();

        // Some pragmatic guidance
        perfNotes();
    }
}
