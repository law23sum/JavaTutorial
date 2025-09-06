package com.tutorial.app;

import com.tutorial.core.datastructure.array.BinarySearch;
import com.tutorial.core.datastructure.array.InsertionSort;
import com.tutorial.core.datastructure.array.LinearScan;
import com.tutorial.packagee.classs.clas.concrete.encapsulate.BaseClass;
import com.tutorial.packagee.classs.clas.abstractt.inherit.polymorph.Polymorphismm;
import com.tutorial.packagee.classs.method.Type;  // <-- contains Overload.runDemo()
import com.tutorial.packagee.classs.clas.concrete.RootClass;
import com.tutorial.packagee.classs.method.Attribute;
import com.tutorial.packagee.classs.method.ConditionsExceptionsLoopsStatements;
import com.tutorial.packagee.classs.type.WrapperClass;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Runner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            try {
                System.out.println("\nChoose which demo to run:");
                System.out.println("1. Binary Search");
                System.out.println("2. OOP Encapsulation Demo (BaseClass)");
                System.out.println("3. Linear Scan Demo");
                System.out.println("4. Methods & Concurrency Demo (Type.Overload)");
                System.out.println("5. Abstract Class & Interface Demo (RootClass)");
                System.out.println("6. Attributes & Arrays Demo (Attribute)");
                System.out.println("7. Conditions / Exceptions / Loops Demo");
                System.out.println("8. Wrapper Class / String Demo");
                System.out.println("9. Polymorphism Demo (SubClass variants)");
                System.out.println("0. Exit");
                System.out.print("> ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1: {
                        // Binary Search + Insertion Sort
                        InsertionSort run = new InsertionSort();
                        run.print();
                        int[] sortedArray = run.run();
                        run.print(sortedArray);
                        System.out.println("attempts " + BinarySearch.execute(sortedArray));
                        break;
                    }
                    case 2: {
                        // Encapsulation Demo
                        BaseClass.runDemo();
                        break;
                    }
                    case 3: {
                        // Linear Scan Demo
                        LinearScan.runDemo();
                        break;
                    }
                    case 4: {
                        // Methods, Overloading, Threads, CompletableFuture
                        Type.Overload.runDemo();
                        break;
                    }
                    case 5: {
                        // Abstract class + interface demo
                        RootClass.runDemo();
                        break;
                    }
                    case 6: {
                        // Attributes & Arrays Demo
                        Attribute.runDemo();
                        break;
                    }
                    case 7: {
                        // Conditions, Exceptions, Loops
                        ConditionsExceptionsLoopsStatements.runDemo();
                        break;
                    }
                    case 8: {
                        // WrapperClass & String operations
                        WrapperClass.runDemo();
                        break;
                    }
                    case 0: {
                        running = false;
                        System.out.println("Exiting Runner... Goodbye!");
                        break;
                    }
                    case 9: {
                        Polymorphismm.runDemo();
                        break;
                    }
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine(); // clear invalid input
            }
        }

        scanner.close();
    }
}
