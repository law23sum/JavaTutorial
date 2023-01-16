package core.datastructure.linkedlist;

public class PointerToNode {
   public static void headInsertion(int dataNode) {
      Node now = new Node(dataNode);         // set value to node
      now.next = head;                       // set front pointer to node next
      head = now;                            // set node pointer (with data) to head pointer

      /*     null:head  |>-point@r> `    now              |>-point@r>       now.next     |>-point@r>    tail:null
       *     null:head  <-@point-<|      now.pair.next  <-point@-<|       now.pair     <-point@r<|   tail:null
       *             // eneral mapping relationship between associates.
       *    point@r head;
       *    point@r now;
       *    point@r now.next;
       *    poin@r tail;
       *  */
   }

   public static Node head = null;
   public static Node front = null;
   static Node back = null;
   static Node tail = null;

   public static void main(String[] args) {
      headInsertion(0);
   }
}