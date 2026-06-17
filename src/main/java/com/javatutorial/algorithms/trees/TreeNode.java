package com.javatutorial.algorithms.trees;

/** Minimal binary-tree node used by {@link LevelOrderTraversal}. */
public class TreeNode {
    public int val;
    public TreeNode left, right;

    public TreeNode(int val) { this.val = val; }
    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val; this.left = left; this.right = right;
    }
}
