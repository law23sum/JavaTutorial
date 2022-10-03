package classs.method.gettersetter;

public class AccessTypes {  //types: getters & setters
   private int value;

   public static void main(String[] args) {
      AccessTypes setget = new AccessTypes();
      setget.setValue(100);
      setget.print();
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int num) {
      this.value = num;
   }

   public void print() {
      System.out.println(this.value);
   }
}