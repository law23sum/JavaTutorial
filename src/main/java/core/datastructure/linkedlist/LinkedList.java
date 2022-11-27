package core.datastructure.linkedlist;

import static core.datastructure.linkedlist.NodesPointers.*;

public class LinkedList {

   static void singleLinkedList() {
      insertHead(10);
      insertHead(20);
      insertHead(30);
      insertTail(0);
      insertTail(-10);
      insertNode(10, 9);
      insertNode(9, 3);
      insertNode(3, 2);
      insertNode(2, 1);
      insertNode(20, 16);
      insertNode(16, 13);
      deleteNode(9);
      deleteNode(16);
      print();
   }

   public static void main(String[] args) {
      singleLinkedList();
   }
}