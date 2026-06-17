package com.javatutorial.algorithms.trees;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * <h2>Binary Tree Level-Order Traversal (BFS)</h2>
 * Visit nodes top-to-bottom, left-to-right, grouped by depth. Use a queue;
 * the queue size at the start of each iteration tells you how many nodes
 * belong to the current level.
 *
 * <pre>
 *   Time : O(n)   Space : O(n)
 * </pre>
 */
public class LevelOrderTraversal {

    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> levels = new ArrayList<>();
        if (root == null) return levels;

        Deque<TreeNode> q = new ArrayDeque<>();
        q.offer(root);
        while (!q.isEmpty()) {
            int size = q.size();
            List<Integer> level = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                TreeNode n = q.poll();
                level.add(n.val);
                if (n.left  != null) q.offer(n.left);
                if (n.right != null) q.offer(n.right);
            }
            levels.add(level);
        }
        return levels;
    }

    public static void main(String[] args) {
        //         3
        //        / \
        //       9  20
        //          / \
        //         15  7
        TreeNode root = new TreeNode(3,
                new TreeNode(9),
                new TreeNode(20, new TreeNode(15), new TreeNode(7)));
        System.out.println(levelOrder(root)); // [[3], [9, 20], [15, 7]]
    }
}
