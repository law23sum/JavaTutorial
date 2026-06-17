package com.javatutorial.algorithms.linkedlist;

import com.javatutorial.datastructures.linkedlist.ListNode;

/**
 * <h2>Reverse a Singly Linked List</h2>
 * Walk the list once, flipping each node's {@code next} pointer to the
 * previous node.
 *
 * <pre>
 *   prev = null, curr = head
 *   while curr != null:
 *     next      = curr.next   // remember
 *     curr.next = prev        // reverse
 *     prev      = curr        // shift
 *     curr      = next
 *   return prev               // new head
 *
 *   Time : O(n)   Space : O(1)
 * </pre>
 */
public class ReverseLinkedList {

    public static ListNode reverse(ListNode head) {
        ListNode prev = null, curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }

    public static void main(String[] args) {
        ListNode head = ListNode.of(1, 2, 3, 4, 5);
        System.out.println("before: " + ListNode.toString(head));
        ListNode reversed = reverse(head);
        System.out.println("after : " + ListNode.toString(reversed));
    }
}
