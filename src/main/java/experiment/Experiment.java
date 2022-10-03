package experiment;

public class Experiment {
   private static final String str1 = "Hello Worlds";
   private static final String str2 = "Hello Friend";
   private static Boolean strMatch = false;

   public static void main(String[] args) {
      stringCompare();
   }

   protected static void stringCompare() {
      if (str1.length() != str2.length()) {
         strMatch = false;
      } else {
         int i = 0;
         do {
            System.out.print(str1.charAt(i) + "?" + str2.charAt(i) + "\t");
            if (i == str1.length()) {
               strMatch = true;
            }
         }
         while (i < str1.length() && str1.charAt(i) == str2.charAt(i++));
      }
      System.out.print("\n" + strMatch);
   }
}