package core.datastructure.linkedlist;

public class NodesPointers {
   static void print() {
      node current = head, previous = null;
      System.out.println();
      while (current != null) {
         System.out.print(current.data + " -> ");
         current = current.next;
      }
      System.out.println("NULL");
   }

   static void deleteNode(int key) {
      node current = head, previous = null;
      if (current.data == key) {
         head = current.next;                       // pointer update to head node
         return;
      }
      while ((current != null) && (current.data != key)) {
         previous = current;
         current = current.next;
      }
      if (current == null) return;
      previous.next = current.next;                // unlinks node from linked list
   }

   static void insertNode(int key, Integer value) {
      node newNode = new node(value);
      if (key == head.data) {
         newNode.next = head.next;
         head.next = newNode;
         return;
      }
      node current = head;                                                       // initial pointer from head
      while ((current.data != key) || current == null) {
         current = current.next;                                                 // iterates through each pointer,
      }
      newNode.next = current.next;                                               // point pointer of current node to the next new node
      current.next = newNode;                                                    // assign node value & pointer to new node
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
   }

   public static void insertHead(int value) {
      node current = new node(value);
      current.next = head;                                                       // point head pointer from head node to next node
      head = current;                                                            // point;
//        node previous = new node(value);
//        previous.prior = current.next;
//        previous.next = previous;
//        previous = previous.next;
//        tail = previous;
   }

   static node head = null;                                                       //declare null linked list
   static node tail = null;
}

class node {
   node(int data) {
      this.data = data;
      this.next = null;
      this.prior = null;
   }

   node next;   // pointerToNextNode
   node prior;  // pointToPriorNode
   int data;    // NodeValue
}