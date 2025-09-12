package com.tutorial.core.datastructure.collections.lists.linkedlist;

public class Node {
    Node(int dataNode) {
        this.nodeData = dataNode;
        this.next = null;
    }

    Node next;  // pointerToNextNode
    Node prior;
    int nodeData;  // NodeValue
}