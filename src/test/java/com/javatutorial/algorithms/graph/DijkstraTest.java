package com.javatutorial.algorithms.graph;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class DijkstraTest {

    @Test void shortestPathsFromSource() {
        List<List<Dijkstra.Edge>> g = new ArrayList<>();
        for (int i = 0; i < 4; i++) g.add(new ArrayList<>());
        g.get(0).add(new Dijkstra.Edge(1, 1));
        g.get(0).add(new Dijkstra.Edge(2, 4));
        g.get(1).add(new Dijkstra.Edge(2, 2));
        g.get(1).add(new Dijkstra.Edge(3, 7));
        g.get(2).add(new Dijkstra.Edge(3, 1));

        assertArrayEquals(new int[]{0, 1, 3, 4}, Dijkstra.shortestPaths(g, 0));
    }

    @Test void unreachableNodeStaysInfinity() {
        List<List<Dijkstra.Edge>> g = new ArrayList<>();
        for (int i = 0; i < 3; i++) g.add(new ArrayList<>());
        g.get(0).add(new Dijkstra.Edge(1, 5));   // 2 is unreachable

        int[] dist = Dijkstra.shortestPaths(g, 0);
        assertArrayEquals(new int[]{0, 5, Integer.MAX_VALUE}, dist);
    }
}
