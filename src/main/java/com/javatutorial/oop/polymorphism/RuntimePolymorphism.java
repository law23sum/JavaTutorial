package com.javatutorial.oop.polymorphism;

/**
 * <h2>Runtime Polymorphism (Method Overriding)</h2>
 * A subclass replaces a method defined in its superclass. The <b>JVM</b>
 * dispatches based on the <i>actual</i> object type at runtime, not the
 * declared variable type.
 */
public class RuntimePolymorphism {

    static class Animal { public void speak() { System.out.println("generic sound"); } }
    static class Dog extends Animal { @Override public void speak() { System.out.println("Woof!"); } }
    static class Cat extends Animal { @Override public void speak() { System.out.println("Meow"); } }

    public static void main(String[] args) {
        Animal[] zoo = { new Animal(), new Dog(), new Cat() };
        for (Animal a : zoo) a.speak();   // chooses the right override per object
    }
}
