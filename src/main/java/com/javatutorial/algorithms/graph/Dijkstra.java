package com.javatutorial.algorithms.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * <h2>Dijkstra's Shortest Path (non-negative weights)</h2>
 * Pop the node with the smallest tentative distance, relax its outgoing
 * edges, repeat. Powered by a min-heap of {@code (distance, node)} pairs.
 *
 * <pre>
 *   Time : O((V + E) log V)   Space : O(V + E)
 * </pre>
 */
public class Dijkstra {

    /** Adjacency list edge: target node + weight. */
    public record Edge(int to, int weight) {}

    public static int[] shortestPaths(List<List<Edge>> graph, int src) {
        int n = graph.size();
        int[] dist = new int[n];
        boolean[] visited = new boolean[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, src});

        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int d = top[0], u = top[1];
            if (visited[u]) continue;
            visited[u] = true;

            for (Edge e : graph.get(u)) {
                if (visited[e.to]) continue;
                int alt = d + e.weight;
                if (alt < dist[e.to]) {
                    dist[e.to] = alt;
                    pq.offer(new int[]{alt, e.to});
                }
            }
        }
        return dist;
    }

    public static void main(String[] args) {
        // 4-node graph:
        //   0 --1--> 1 --2--> 2
        //   0 --4--> 2
        //   1 --7--> 3
        //   2 --1--> 3
        List<List<Edge>> g = new ArrayList<>();
        for (int i = 0; i < 4; i++) g.add(new ArrayList<>());
        g.get(0).add(new Edge(1, 1));
        g.get(0).add(new Edge(2, 4));
        g.get(1).add(new Edge(2, 2));
        g.get(1).add(new Edge(3, 7));
        g.get(2).add(new Edge(3, 1));

        System.out.println(Arrays.toString(shortestPaths(g, 0))); // [0, 1, 3, 4]
    }
}
