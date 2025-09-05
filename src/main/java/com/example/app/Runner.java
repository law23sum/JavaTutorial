package com.example.app;

import com.tutorial.core.datastructure.array.BinarySearch;
import com.tutorial.core.datastructure.array.InsertionSort;
// import other classes here as needed

import java.util.Scanner;

public class Runner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose which demo to run:");
        System.out.println("1. Binary Search");
        // add other options here
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                InsertionSort Run = new InsertionSort();
                Run.print();
                int[] sortedArray = Run.run();
                Run.print(sortedArray);
                System.out.println("attempts " + BinarySearch.execute(sortedArray));
                break;

            // add more cases for other demos
            default:
                System.out.println("Invalid choice.");
        }
    }
}
