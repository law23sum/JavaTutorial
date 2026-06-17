package com.javatutorial.datastructures.lists;

import java.util.LinkedList;

/**
 * <h2>LinkedList</h2>
 * A doubly-linked chain of nodes. Cheap inserts/removals at the ends; index
 * access requires walking the list.
 *
 * <pre>
 *   addFirst/addLast/removeFirst/removeLast  O(1)
 *   add(i, e) / remove(i) / get(i)            O(n)   (must traverse to i)
 * </pre>
 */
public class LinkedListDemo {

    public static void main(String[] args) {
        LinkedList<String> nav = new LinkedList<>();
        nav.add("/home");
        nav.add("/profile");
        nav.addFirst("/login");      // O(1) at head
        nav.addLast("/logout");      // O(1) at tail
        nav.removeLast();            // O(1)

        System.out.println("first=" + nav.getFirst() + " last=" + nav.getLast());
        System.out.println("nav  =" + nav);
    }
}
