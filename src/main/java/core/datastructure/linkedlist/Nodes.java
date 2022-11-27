package core.datastructure.linkedlist;

import org.w3c.dom.Node;

public class Nodes {
   static void printLinkedList() {
      node current = head;
      System.out.println();
      while (current != null) {
         System.out.print(current.data + " -> ");
         current = current.next;
      }
      System.out.println("NULL");
   }

   static void printNode(Integer value) {
      node current = head;
      while ((current.data != value) && (current.next != null)) {
         current = current.next;
      }
      System.out.print("node " + current.data + "\t");
   }

   static void deleteNode(int key, Integer value) {
         node current = head, previous = null;
         if (current != null && current.data == key) {
            head = current.next;                       // pointer update to head node
            return;
         }
         while (current != null && current.data != key) {
            previous = current;
            current = current.next;
         }
         if (current == null)
            return;
         previous.next = current.next;                // unlinks node from linked list
   }

   static void insertTail(Integer value) {
      node newNode = new node(value);
      if (head == null) {
         head = newNode;
         return;
      }
      node previous = head;
      while (previous.next != null) {
         previous = previous.next;
      }
      previous.next = newNode;
      printNode(previous.data);
   }

   static void insertNode(int key, Integer value) {
      node newNode = new node(value);
      if (key == head.data) {
         newNode.next = head.next;
         head.next = newNode;
         return;
      }
      node previous = head;                                                       // initial pointer from head
      while (previous.data != key) {
         previous = previous.next;                                                 // iterates through each pointer,
         // starting from head till key assigned to node
         if (previous == null) {
            return;
         }
      }
      newNode.next = previous.next;                                               // point pointer of previous node to the next new node
      previous.next = newNode;                                                    // assign node value & pointer to new node
      printNode(previous.data);
   }

   public static void insertHead(Integer value) {
      node n = new node(value);
      n.next = head;
      head = n;
      printNode(head.data);
   }

   static node head = null;                                                       //declare null linked list
}

class node {
   node(int value) {
      data = value;
      next = null;
   }

   node next; // pointer
   int data;  // value
}