package core.datastructure;

public class BinarySearch {
   public static void main(String[] args) {
      InsertionSort Run = new InsertionSort();
      Run.print();
      int[] sortedArray = Run.run();
      Run.print(sortedArray);
      System.out.println("attempts " + execute(sortedArray));
   }

   private static int execute(int[] sortedArray) {
      int random = (int) (Math.random() * (7000 + 1));
      int answer = sortedArray[random];
      int indexMax = sortedArray.length;
      int indexMin = 0;
      int indexMedium;
      int counter = 0;
      int guess = 0;
      while ((answer != guess)&&(indexMax > indexMin)) {                                                                // advanced: &&(indexMax > indexMin)
         ++counter;
         indexMedium = (int) (float) Math.floor((indexMax + indexMin) / 2F);
         guess = sortedArray[indexMedium];
         System.out.print(guess + " : " + answer + "\t\t\t");
         if (guess < answer) {
            System.out.print("too low" + "\n" + "min " + indexMin + "\t");
            indexMin = indexMedium - 1;
         } else if (guess > answer) {
            System.out.print("too high" + "\n" + "max " + indexMax + "\t");
            indexMax = indexMedium + 1;
         } else {
            System.out.println("min " + indexMin + " < " + "mid " + indexMedium + " < " + "max " + indexMax);
            return counter;
         }
         System.out.print("\t");
      }
      return -1;
   }

}