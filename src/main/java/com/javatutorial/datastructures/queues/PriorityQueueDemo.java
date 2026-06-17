package com.javatutorial.datastructures.queues;

import java.util.PriorityQueue;

/**
 * <h2>PriorityQueue</h2>
 * Heap-backed queue that always pops the highest-priority element (smallest
 * by natural ordering, by default). Use a {@link java.util.Comparator} for
 * custom priority.
 *
 * <pre>
 *   offer(e) / add(e)   O(log n)
 *   poll() / remove()   O(log n)
 *   peek()              O(1)
 * </pre>
 */
public class PriorityQueueDemo {

    public static void main(String[] args) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.offer(5); pq.offer(1); pq.offer(3); pq.offer(2);
        while (!pq.isEmpty()) System.out.print(pq.poll() + " ");   // 1 2 3 5
        System.out.println();

        // Custom: highest first
        PriorityQueue<String> byLength =
                new PriorityQueue<>((a, b) -> b.length() - a.length());
        byLength.offer("a");
        byLength.offer("bbb");
        byLength.offer("cc");
        while (!byLength.isEmpty()) System.out.print(byLength.poll() + " "); // bbb cc a
    }
}
