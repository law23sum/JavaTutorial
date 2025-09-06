package com.tutorial.core.datastructure.array;

import com.library.custom.BasicFunctions;

// thorough yet inefficient
public class LinearScan { // search a sorted list
   private static final int target = 23;
   private static final int[] lockerNumbers = BasicFunctions.randomNumberGeneratorArray(target);

   public static void runDemo() {
      int i = 0;
      while (i < lockerNumbers.length) {
         lockerNumbers[22] = target; // force target at index 22
         if (lockerNumbers[i] == target) {
            System.out.println("exist");
         } else {
            System.out.print(i + ": " + lockerNumbers[i] + "  ");
         }
         i++;
      }
      System.out.println(); // line break after loop
   }
}
