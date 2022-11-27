package core.datastructure.array;

import library.Static;

// thorough yet inefficient
public class LinearScan {                                                                                             // search a sorted list
   private static final int target = 23;
   private static final int[] lockerNumbers = Static.randomNumberGeneratorArray(target);
   private static int i = 0;

   public static void main(String[] args) {
      while (i < lockerNumbers.length) {
         lockerNumbers[22] = target;
         if (lockerNumbers[i] == target)
            System.out.println("exist");
         else {
            System.out.print(i + ": " + lockerNumbers[i] + "  ");
         }
         i++;
      }
   }
}