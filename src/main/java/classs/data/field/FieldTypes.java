package classs.data.field;

public class FieldTypes {
   public static final double TRANSCENDENT = Math.sqrt(2);
   private static String personal = "hidden";
   protected final int UNCHANGEABLE = 42;
   public int digit = 23;                                                                                              // project accessible
   public float repeatable = 1 / 3;
   protected char statute = 's';                                                                                       // package accessible
   private boolean status = false;                                                                                     // class accessible

   public static void main(String[] args) {
      FieldTypes field = new FieldTypes();
      field.booleanField();
      field.stringField();
      field.integerField();

   }

   void booleanField() {
      boolean innerStatus = true;
      status = innerStatus;
      System.out.println(status);
   }

   void stringField() {
      String exchange = "interact";
      personal = exchange;
      System.out.println(personal);
   }

   void integerField() {
      int calculation = (int) (((UNCHANGEABLE / (1 - UNCHANGEABLE)) - (1 / (1 + TRANSCENDENT))) * digit);
      System.out.println(calculation);
   }
}