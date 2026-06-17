package com.javatutorial.oop.inheritance;

/** Shared "is-a" base for the {@link Dog} subclass. */
public class Animal {
    protected final String name;

    public Animal(String name) { this.name = name; }

    public void speak() { System.out.println(name + " makes a generic sound"); }
}
