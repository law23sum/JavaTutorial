package com.tutorial.core.datastructure.linkedlist;

public class NodesPointers {
   static void print() {
      Node current = head, previous = null;
      System.out.println();
      while (current != null) {
         System.out.print(current.nodeData + " -> ");
         current = current.next;
      }
      System.out.println("NULL");
   }

   static void deleteNode(int key) {
      Node current = head, previous = null;
      if (current.nodeData == key) {
         head = current.next;                       // pointer update to head Node
         return;
      }
      while ((current != null) && (current.nodeData != key)) {
         previous = current;
         current = current.next;
      }
      if (current == null) return;
      previous.next = current.next;                // unlinks Node from linked list
   }

   static void insertNode(int key, Integer value) {
      Node newNode = new Node(value);
      if (key == head.nodeData) {
         newNode.next = head.next;
         head.next = newNode;
         return;
      }
      Node current = head;                                                       // initial pointer from head
      while ((current.nodeData != key) || current == null) {
         current = current.next;                                                 // iterates through each pointer,
      }
      newNode.next = current.next;                                               // point pointer of current Node to the next new Node
      current.next = newNode;                                                    // assign Node value & pointer to new Node
   }

   static void insertTail(Integer value) {
      Node newNode = new Node(value);
      if (head == null) {
         head = newNode;
         return;
      }
      Node previous = head;
      while (previous.next != null) {
         previous = previous.next;
      }
      previous.next = newNode;
   }

   public static void insertHead(int value) {
      Node current = new Node(value);
      current.next = head;                                                       // point head pointer from head Node to next Node
      head = current;                                                            // point;
//        Node previous = new Node(value);
//        previous.prior = current.next;
//        previous.next = previous;
//        previous = previous.next;
//        tail = previous;
   }

   static Node head = null;                                                       //declare null linked list
   static Node tail = null;
}