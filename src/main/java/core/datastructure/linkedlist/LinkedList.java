package core.datastructure.linkedlist;

import static core.datastructure.linkedlist.Nodes.*;

public class LinkedList {
   static void singleLinkedList() {
      insertTail(-10);
      insertTail(0);
      insertHead(10);
      insertHead(20);
      insertHead(30);
      insertNode(10, 9);
      insertNode(9, 3);
      insertNode(3, 2);
      insertNode(2, 1);
      insertNode(20, 16);
      insertNode(16, 13);
      deleteNode(9, 10);
      deleteNode(16, 20);
      printLinkedList();
   }

   public static void main(String[] args) {
      singleLinkedList();

   }
}