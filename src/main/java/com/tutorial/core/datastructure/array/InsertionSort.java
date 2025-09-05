package com.tutorial.core.datastructure.array;

public class InsertionSort {
   private final int length = 7000;
   private final int[] valueCalender = platform.library.BasicFunctions.randomNumberGeneratorArray(length + 1);
   private int i = 1;
   private int k = 1;

   public int[] run() {
      while (i <= length) {
         //print();
         int state = valueCalender[i];
         int j = i - 1;
         //DEBUG: System.out.print("hold-" + state + "   \t");
         while (j >= 0 && valueCalender[j] > state) {                                                                // compare each index pair then flip, else next index
            valueCalender[j + 1] = valueCalender[j];                                                                // original index holds value per next index
            j = j - 1;
         }
         //DEBUG: System.out.print("j" + j + " i" + i + "\t  replace-" + valueCalender[j + 1] + "\n");
         valueCalender[j + 1] = state;
         i++;
      }
      return valueCalender;
   }

   public void print() {
      System.out.println();
      while (k <= length) {
         System.out.print(valueCalender[k - 1] + " ");
         k++;
      }
      k = 1;
   }

   public void print(int[] printSet) {
      System.out.println();
      while (k <= length) {
         System.out.print(printSet[k - 1] + " ");
         k++;
      }
      k = 1;
   }
}