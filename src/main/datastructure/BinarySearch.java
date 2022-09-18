package src.main.datastructure;

import src.main.datastructure.InsertionSort;

import java.util.ArrayList;
import java.util.Arrays;

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
        int index_max = sortedArray.length;
        int index_min = 0;
        int index_mid = (int) (Math.floor(index_max + index_min) / 2);
        int guess = sortedArray[index_mid];
        int counter = 0;
        do {
            ++counter;
            index_mid = (int) Math.floor((index_max + index_min) / 2);
            guess = sortedArray[index_mid];
            System.out.print(guess + " : " + answer + "\t\t\t");
            if (guess < answer) {
                System.out.print("too low" + "\n" + "min " + index_min + "\t");
                index_min = index_mid - 1;
            } else if (guess > answer) {
                System.out.print("too high" + "\n" + "max " + index_max + "\t");
                index_max = index_mid + 1;
            } else {
                System.out.println("min " + index_min + " < " + "mid " + index_mid + " < " + "max " + index_max);
                return counter;
            }
            System.out.print("\t");
        } while (guess != answer);
        return 0;
    }

}