package com.tutorial.core.datastructure.stackqueue;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

public class StacksClass<T> extends Vector<T> {
    StacksClass(int size) {
        this.size = size;
        this.A = new ArrayList<>(size);                                                   // Creating array of Size = size
    }

    // @Override
    public String toString() {
        StringBuilder Ans = new StringBuilder();

        for (int i = 0; i < top; i++) {
            Ans.append(A.get(i)).append("->");
        }
        Ans.append(A.get(top));
        return Ans.toString();
    }

    void push(T X) {                                                               // To push generic element into Stacks
        if (top + 1 == size) {                                                      // Checking if array is full
            System.out.println("Stack Overflow");                                  // Display message when array is full
        } else {
            top = top + 1;                                                        // Increment top to go to next position
            if (A.size() > top) A.set(top, X);                                     // Over-writing existing element
            else A.add(X);                                                         // Creating new element
        }
    }

    void pop() {                                                                      // To delete last element of Stacks
        if (top == -1) {                                                            // If Stacks is empty
            System.out.println("Stack Underflow");
        } else
            top--;                                                                  // Delete the last element // by decrementing the top
    }

    T top() {                                                                   // To return topmost element of Stacks
        if (top == -1) {                                                        // If Stacks is empty
            System.out.println("Stack Underflow");                              // Display message when there are no elements in
            return null;
        } else return A.get(top);                                                   // else elements are present so
        // return the topmost element
    }


    static void stack_push(Stack<Integer> stack) {
        for (int i = 0; i < 5; i++) {
            stack.push(i);
        }
    }

    static void stack_search(Stack<Integer> stack, int element) {                                 // Searching element in the StacksArray
        int pos = stack.search(element);
        if (pos == -1) System.out.println("Element not found");
        else System.out.println("Element is found at position: " + pos + "\n");
    }

    static void stack_peek(Stack<Integer> stack) {                                                 // Displaying element on the top of the StacksArray
        Integer element = stack.peek();
        System.out.println("Element on StacksArray top: " + element);
    }

    static void stack_pop(Stack<Integer> stack) {                                           // Popping element from the top of the StacksArray
        System.out.println("Pop Operation:");
        for (int i = 0; i < 5; i++) {
            Integer y = stack.pop();
            System.out.println(y);
        }
    }

    public ArrayList<T> A;
    public int top = -1;
    public int size;
}