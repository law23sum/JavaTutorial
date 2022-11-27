package core.datastructure.stackqueue;

import java.util.Stack;

import static core.datastructure.stackqueue.StacksClass.*;

public class StackArray {
    static void stacksArray() {
        int amount = 3;
        StacksClass<Integer> stackArr = new StacksClass<>(amount);
        StacksClass<Integer> stackArray;
        stackArr.push(5);
        stackArr.push(4);
        stackArr.push(3);
        if (stackArr.top == stackArr.size - 1) {
            amount *= amount;
            System.out.println("\n\tincrease size of stack array\t" + amount);
            stackArray = new StacksClass<>(amount);
            stackArr.top = stackArr.top + 1;
            for (Integer i : stackArr.A) stackArray.push(i);
            stackArray.push(2);
            stackArray.push(1);
            System.out.println("\t" + stackArray);
        }
    }

    static void example0() {
        StacksClass<Integer> s1 = new StacksClass<>(3);
        s1.push(10);                                                             // Pushing elements to integer Stacks - s1
        s1.push(20);                                                             // Element 1 - 10
        s1.push(30);                                                             // Element 3 - 30
        System.out.println("s1 after pushing 10, 20 and 30 :\n" + s1);              // Print the Stacks elements after pushing the
        s1.pop();                                                                   // Now, pop from Stacks s1
        System.out.println("s1 after pop :\n" + s1);

        StacksClass<String> s2 = new StacksClass<>(3);
        s2.push("hello");
        s2.push("world");
        s2.push("java");
        System.out.println("\ns2 after pushing 3 elements :\n" + s2);
        System.out.println("s2 after pushing 4th element :");
        s2.push("GFG");

        StacksClass<Float> s3 = new StacksClass<>(2);
        s3.push(100.0f);
        s3.push(200.0f);
        System.out.println("\ns3 after pushing 2 elements :\n" + s3);
        System.out.println("top element of s3:\n" + s3.top());
    }

    static void example() {
        Stack<Integer> stack = new Stack<>();
        stack_push(stack);
        stack_pop(stack);
        stack_push(stack);
        stack_peek(stack);
        stack_search(stack, 2);
        stack_search(stack, 6);
    }

    public static void main(String[] args) {
        example();
        example0();
        stacksArray();
    }
}