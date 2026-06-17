package com.javatutorial.datastructures.queues;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * <h2>ArrayDeque</h2>
 * A resizable-array double-ended queue. Push/pop at <i>both</i> ends in O(1).
 * Use it as a stack ({@code push/pop}) or a queue ({@code offer/poll}).
 */
public class ArrayDequeDemo {

    public static void main(String[] args) {
        Deque<String> deque = new ArrayDeque<>();
        deque.offerLast("first");           // queue-style enqueue
        deque.offerLast("second");
        deque.offerFirst("zeroth");         // push at the head

        System.out.println("peek first : " + deque.peekFirst()); // zeroth
        System.out.println("peek last  : " + deque.peekLast());  // second
        System.out.println("poll first : " + deque.pollFirst()); // zeroth (FIFO)

        // Stack-style:
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(1); stack.push(2); stack.push(3);
        System.out.println("pop=" + stack.pop() + " pop=" + stack.pop()); // 3, 2
    }
}
