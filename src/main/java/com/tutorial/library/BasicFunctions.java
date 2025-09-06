package com.tutorial.library;

public class BasicFunctions {
   public static double randomNumberGenerator() {
      return Math.random() * (10000 + 1);
   }

   public static int[] randomNumberGeneratorArray(int amount) {
      int i = 1;
      int[] array = new int[amount];
      int n = array.length;
      do {
         array[i++ - 1] = (int) BasicFunctions.randomNumberGenerator();
         //DEBUG: System.out.print(array[i - 2] + "\t");
      } while (i <= n);
      //DEBUG:System.out.print("\n" + array[n - 1]);
      return array;
   }
}