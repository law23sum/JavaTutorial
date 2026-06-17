package com.javatutorial.datastructures.linkedlist;

/**
 * Minimal singly-linked list node — used by the {@link
 * com.javatutorial.algorithms.linkedlist.ReverseLinkedList} algorithm.
 *
 * <pre>
 *   head -> [val|next] -> [val|next] -> ... -> null
 * </pre>
 */
public class ListNode {
    public int val;
    public ListNode next;

    public ListNode() {}
    public ListNode(int val) { this.val = val; }
    public ListNode(int val, ListNode next) { this.val = val; this.next = next; }

    /** Convenience: build a list from {@code 1, 2, 3, 4} → 1→2→3→4→null. */
    public static ListNode of(int... values) {
        ListNode dummy = new ListNode();
        ListNode tail = dummy;
        for (int v : values) {
            tail.next = new ListNode(v);
            tail = tail.next;
        }
        return dummy.next;
    }

    /** Convenience: render as "1 -> 2 -> 3 -> null". */
    public static String toString(ListNode head) {
        StringBuilder sb = new StringBuilder();
        for (ListNode n = head; n != null; n = n.next) sb.append(n.val).append(" -> ");
        return sb.append("null").toString();
    }
}
