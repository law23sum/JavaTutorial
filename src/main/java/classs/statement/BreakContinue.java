package classs.statement;

public class BreakContinue {
   static String searchMe = "peter piper picked a " + "peck of pickled peppers",
           searchMeToo = "Look for a substring in me", substring = "sub";
   static int[] arrayOfInts = {32, 87, 3, 589, 12, 1076, 2000, 8, 622, 127};
   static int[][] arrayOfIntsMulti = {{32, 87, 3, 589}, {12, 1076, 2000, 8}, {622, 127, 77, 955}};
   static int searchfor = 12, i, j, max = searchMe.length(), numPs = 0, maxy = searchMeToo.length() -
           substring.length();
   static boolean foundIt = false;

   public static void main(String[] args) {
      logic();
      logic2nd();
      logic3rd();
      logic4th();
   }

   static void logic() {
      for (i = 0; i < arrayOfInts.length; i++) {
         if (arrayOfInts[i] == searchfor) {
            foundIt = true;
            break;
         }
      }
      if (foundIt) {
         System.out.println("Found " + searchfor + " at index " + i);
      } else {
         System.out.println(searchfor + " not in the array");
      }
   }

   static void logic2nd() {
      search:
      for (i = 0; i < arrayOfIntsMulti.length; i++) {
         for (j = 0; j < arrayOfIntsMulti[i].length;
              j++) {
            if (arrayOfIntsMulti[i][j] == searchfor) {
               foundIt = true;
               break search;
            }
         }
      }
      if (foundIt) {
         System.out.println("Found " + searchfor + " at " + i + ", " + j);
      } else {
         System.out.println(searchfor + " not in the array");
      }
   }

   static void logic3rd() {
      for (int i = 0; i < max; i++) {
         if (searchMe.charAt(i) != 'p')              // interested only in p's
            continue;                               // process p's
         numPs++;
      }
      System.out.println("Found " + numPs + " p's in the string.");
   }

   static void logic4th() {
      test:
      for (int i = 0; i <= maxy; i++) {
         int n = substring.length();
         int j = i;
         int k = 0;
         while (n-- != 0) {
            if (searchMeToo.charAt(j++) != substring.charAt(k++)) {
               continue test;
            }
         }
         foundIt = true;
         break test;
      }
      System.out.println(foundIt ? "Found it" : "Didn't find it");
   }
}