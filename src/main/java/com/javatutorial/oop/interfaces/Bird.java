package com.javatutorial.oop.interfaces;

/** A {@link Flyable} from the animal kingdom. */
public class Bird implements Flyable {
    @Override
    public void fly() { System.out.println("Bird flaps wings"); }
}
