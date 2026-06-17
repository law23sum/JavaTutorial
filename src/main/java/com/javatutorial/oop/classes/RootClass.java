package com.javatutorial.oop.classes;

/**
 * <h2>Root Class</h2>
 * The conceptual top of a class hierarchy. In Java every class implicitly
 * extends {@link Object}, which provides the universal methods like
 * {@code toString()}, {@code equals()}, {@code hashCode()}.
 *
 * <p>Here we model a project-local "root" — the most general thing every
 * other class in this little hierarchy will inherit from.
 */
public class RootClass {
    /** Every domain object should be able to describe itself. */
    public String describe() { return "I am a RootClass"; }
}
