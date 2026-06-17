package com.javatutorial.algorithms.trees;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelOrderTraversalTest {

    @Test void groupsByLevel() {
        TreeNode root = new TreeNode(3,
                new TreeNode(9),
                new TreeNode(20, new TreeNode(15), new TreeNode(7)));
        List<List<Integer>> levels = LevelOrderTraversal.levelOrder(root);
        assertEquals(List.of(List.of(3), List.of(9, 20), List.of(15, 7)), levels);
    }

    @Test void emptyTree() {
        assertTrue(LevelOrderTraversal.levelOrder(null).isEmpty());
    }
}
