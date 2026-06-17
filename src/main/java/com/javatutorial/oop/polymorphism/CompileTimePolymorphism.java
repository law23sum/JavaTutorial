package com.javatutorial.oop.polymorphism;

/**
 * <h2>Compile-time Polymorphism (Method Overloading)</h2>
 * Same method name, different parameter lists. The <b>compiler</b> picks the
 * version based on the static types of the arguments.
 */
public class CompileTimePolymorphism {

    static class Printer {
        void print(String s) { System.out.println("String: " + s); }
        void print(int n)    { System.out.println("Int:    " + n); }
        void print(double d) { System.out.println("Double: " + d); }
    }

    public static void main(String[] args) {
        Printer p = new Printer();
        p.print("hello"); // -> String:
        p.print(42);      // -> Int:
        p.print(3.14);    // -> Double:
    }
}
