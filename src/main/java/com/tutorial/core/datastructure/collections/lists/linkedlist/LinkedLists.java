package com.tutorial.core.datastructure.collections.lists.linkedlist;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;

public class LinkedLists {
    public static void main(String[] args) {
        LinkedList<String> ll = new LinkedList<>(Arrays.asList("A","B","C","D"));
        System.out.println("Start:        " + ll);          // [A, B, C, D]

        // Ends: O(1)
        ll.addFirst("HEAD");
        ll.addLast("TAIL");
        System.out.println("Ends +/-:     " + ll);          // [HEAD, A, B, C, D, TAIL]
        ll.removeFirst(); ll.removeLast();
        System.out.println("Ends removed: " + ll);          // [A, B, C, D]

        // Index ops: locate O(n), write O(1)
        String got = ll.get(2);                              // walk to index 2
        System.out.println("get(2):       " + got);         // C
        ll.set(2, "Z");
        System.out.println("set(2,Z):     " + ll);          // [A, B, Z, D]

        // Insert/remove by index: O(n) locate + O(1) splice
        ll.add(2, "X");
        System.out.println("add(2,X):     " + ll);          // [A, B, X, Z, D]
        ll.remove(1);
        System.out.println("remove(1):    " + ll);          // [A, X, Z, D]

        // The *real* superpower: ListIterator O(1) insert/remove at cursor
        ListIterator<String> it = ll.listIterator(); // cursor before A
        it.next();               // at A
        it.add("α");            // insert after A (O(1))
        it.next();               // at X
        it.remove();            // remove X (O(1))
        System.out.println("Iterator ops: " + ll);          // [A, α, Z, D]
    }
}
