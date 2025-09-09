package com.tutorial.packagee.classs.clas.abstractt;

/**
 * Interface: contracts only (no constructors).
 *
 * What an interface may contain:
 *  - Constants (implicitly public static final)
 *  - Abstract method signatures (implicitly public abstract)
 *  - Default methods (with bodies; Java 8+)
 *  - Static methods
 *  - Nested types
 *
 * Design notes:
 *  - We keep the original event/state API you defined.
 *  - We add minimal getters/setters so default helpers can operate without
 *    duplicating logic in every implementor.
 *  - Default methods demonstrate compile-time polymorphism (overloading),
 *    while implementors provide runtime polymorphism (overriding behaviors).
 */
public interface Interface {

    // -------------------------
    // Constants
    // -------------------------

    // Implicitly: public static final
    String WORD = "Aye";


    // -------------------------
    // State API (required)
    // -------------------------

    /** Increment internal state by one unit. */
    void incrementState();

    /** Decrement internal state by one unit. */
    void decrementState();

    /** Display current state to an output sink (e.g., console, logger, UI). */
    void displayCurrentState();

    /**
     * Change behavior with a qualifier.
     * By convention, "type" may route to different change domains (e.g., "Event").
     * Implementations decide how to interpret the qualifier.
     */
    void change(String type, int state);


    // -------------------------
    // Event API (required)
    // -------------------------

    /** Increment internal event count by one unit. */
    void incrementEvent();

    /** Decrement internal event count by one unit. */
    void decrementEvent();

    /** Display current event info to an output sink. */
    void displayCurrentEvent();


    // -------------------------
    // Minimal Accessors (required)
    // These enable default helpers below without forcing implementors
    // to duplicate arithmetic/validation in every class.
    // -------------------------

    /** Set the current state value. */
    void setNewState(int newState);

    /** Get the current state value. */
    int getNewState();

    /** Set the current event value. */
    void setNewEvent(int newEvent);

    /** Get the current event value. */
    int getNewEvent();


    // -------------------------
    // Default Helpers (compile-time polymorphism)
    // Implementors inherit these; they can be overridden if needed.
    // -------------------------

    /** Convenience: change state by +1. */
    default void change() {
        change(1);
    }

    /** Convenience: change state by an arbitrary delta (positive/negative). */
    default void change(int delta) {
        setNewState(getNewState() + delta);
    }

    /** Convenience: bump event by +1. */
    default void changeEvent() {
        changeEvent(1);
    }

    /** Convenience: change event by an arbitrary delta (positive/negative). */
    default void changeEvent(int delta) {
        setNewEvent(getNewEvent() + delta);
    }

    /**
     * Optional: clamp state to be non-negative after applying a delta.
     * Implementors may override for domain-specific rules (e.g., max caps).
     */
    default void changeClamped(int delta) {
        int next = getNewState() + delta;
        setNewState(Math.max(0, next));
    }
}
