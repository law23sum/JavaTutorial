package com.javatutorial.oop.shapes;

/**
 * <h2>Shape</h2>
 * Abstract base showing how a class can:
 * <ul>
 *   <li>declare an <b>abstract</b> method that subclasses must implement,</li>
 *   <li>provide a <b>concrete</b> method that all subclasses inherit, and</li>
 *   <li>act as a polymorphic supertype.</li>
 * </ul>
 */
public abstract class Shape {

    public abstract double area();

    /** Concrete helper inherited by every subclass. */
    public String describe() { return getClass().getSimpleName() + " area=" + area(); }
}
