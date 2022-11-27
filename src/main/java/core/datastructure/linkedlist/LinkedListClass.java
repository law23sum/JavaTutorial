package core.datastructure.linkedlist;

import java.util.Collections;
import java.util.LinkedList;

public class LinkedListClass {
    static void nextLinkedList() {
        LinkedList node = new LinkedList();
        Collections.addAll(node, words);
        node.addLast(node.get(node.size() - 1));
        for (Object no : node) {
            System.out.println(no);
        }
    }

    static void getLinkedList() {
        LinkedList nodes = new LinkedList();
        nodes.addFirst("first");
        nodes.add(1, "second");
        nodes.add(2, "third");
        nodes.addLast("last");

        for (Object node : nodes) {
            System.out.print(node + "\t\t");
            System.out.println("pointer-" + node);
        }
        System.out.println();
    }

    static void commonLinkedList() {
        LinkedList common = new LinkedList();
        common.removeFirst();
        common.removeLast();
        common.addFirst('a');
        common.addLast('z');
        common.getFirst();
        common.get(1);
        common.set(2, 'c');
        common.getLast();
        common.clear();
        common.size();
    }


    static String[] words = {null, null, null, null, null};

    public static void main(String[] args) {
        getLinkedList();
        nextLinkedList();
    }
}