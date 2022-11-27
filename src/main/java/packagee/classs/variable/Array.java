package packagee.classs.variable;

public class Array {
   protected static void stringCompare() {
      String str1 = "Hello Worlds";
      String str2 = "Hello Friend";
      Boolean strMatch = false;
      if (str2.length() != str1.length()) {
         strMatch = false;
      } else {
         int i = 0;
         do {
            System.out.print(str1.charAt(i) + "?" + str2.charAt(i) + "\t");
            if (i == str1.length()) {
               strMatch = true;
            }
            i++;
         }
         while (i < str1.length() && str1.charAt(i) == str2.charAt(i++));
      }
      System.out.print("\n" + strMatch);
   }

   static Integer[] arrayDouble(Integer[] array) {
      int arrayLength = array.length;
      int newArrayLength = array.length * 2;
      Integer[] newArray = new Integer[newArrayLength];
      int j = 0;
      while (j < arrayLength) {
         newArray[j] = array[j];
         j++;
      }
      return newArray;
   }

   static void intArray() {
      int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
      for (int item : numbers) {
         System.out.print("Count is: " + item + "\t\t");
      }
      space("line");
   }

   static void integerArray() {
      Integer[] values = {0, 1, 2, 3, 4};
      Integer[] valuesFixed = new Integer[5];
      System.arraycopy(values, 0, valuesFixed, 0, 5);
      for (Integer value : valuesFixed) {
         System.out.print(value + " ");
      }
      space("line");
   }

   static void stringArray() {
      String[] copyFrom = {"Affogato", "Americano", "Cappuccino", "Corretto", "Cortado",
              "Doppio", "Espresso"}, copyTo = new String[8];
      System.arraycopy(copyFrom, 0, copyTo, 0, 7);
      for (String coffee : copyTo) {
         System.out.print(coffee + " ");
      }
      space("line");
   }

   static void stringArrayMulti() {
      String[][] names = {{"Mr. ", "Mrs. ", "Ms. "}, {"Smith", "Jones"}};
      System.out.print(names[0][0] + names[1][0]);
      space("tab");
      System.out.print(names[0][2] + names[1][1]);
   }

   static void space(String type) {
      switch (type) {
         case "tab":
            System.out.print("\t");
            break;
         case "line":
            System.out.println();
            break;
      }
   }

   public static void main(String[] args) {
      intArray();
      integerArray();
      stringArray();
      stringArrayMulti();
      Integer[] newArray = arrayDouble(new Integer[3]);
      stringCompare();
   }

}