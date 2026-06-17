package com.javatutorial.oop.interfaces;

/**
 * <h2>Interface</h2>
 * Pure abstraction — a contract describing <i>what</i> implementers can do
 * without dictating <i>how</i>. Classes opt in with the {@code implements}
 * keyword. A class can implement many interfaces but extend only one class.
 *
 * <p>Different family trees can promise the same behavior (a {@code Bird}
 * and an {@code Airplane} can both be {@code Flyable}).
 */
public interface Flyable {
    void fly();

    /** Default method: a free implementation that implementers may override. */
    default String describeFlight() { return "Flight in progress"; }
}
