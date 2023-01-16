package core.datastructure.linkedlist;

import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

public class LinkedListClass {
    static void nextLinkedList() {
        LinkedList<String> node = new LinkedList<>();
        Collections.addAll(node, words);
        node.addLast(node.get(node.size() - 1));
        for (Object no : node) {
            System.out.println(no);
        }
    }

    static void getLinkedList() {
        LinkedList<String> nodes = new LinkedList<>();
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
        AtomicReference<LinkedList<Character>> common = new AtomicReference<>(new LinkedList<Character>());
        common.get().removeFirst();
        common.get().removeLast();
        common.get().addFirst('a');
        common.get().addLast('z');
        common.get().getFirst();
        common.get().get(1);
        common.get().set(2, 'c');
        common.get().getLast();
        common.get().clear();
        common.get().size();
    }


    static String[] words = {null, null, null, null, null};

    public static void main(String[] args) {
        getLinkedList();
        nextLinkedList();

    }
}