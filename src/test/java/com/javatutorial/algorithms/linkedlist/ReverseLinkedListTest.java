package com.javatutorial.algorithms.linkedlist;

import com.javatutorial.datastructures.linkedlist.ListNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReverseLinkedListTest {

    @Test void reversesFiveNodes() {
        ListNode head = ListNode.of(1, 2, 3, 4, 5);
        ListNode rev  = ReverseLinkedList.reverse(head);
        assertEquals("5 -> 4 -> 3 -> 2 -> 1 -> null", ListNode.toString(rev));
    }

    @Test void reversesSingleNode() {
        ListNode head = ListNode.of(42);
        ListNode rev  = ReverseLinkedList.reverse(head);
        assertEquals("42 -> null", ListNode.toString(rev));
    }

    @Test void reversesEmpty() {
        assertNull(ReverseLinkedList.reverse(null));
    }
}
