// Java Project
// Topics: Inheritance, Encapsulation, Polymorphism
//
// Polymorphism has two common forms:
//   • Method overloading  → compile-time (static binding)
//   • Method overriding   → runtime (dynamic dispatch)
//     - Dynamic Method Dispatch occurs when a subclass provides a method
//       with the same name, parameters, and compatible return type as the parent.
//     - Parameter lists and types must match to override (not just the name).
//     - A subclass cannot reduce visibility (it may be less restrictive).
//
package com.tutorial.packagee.classs.clas.abstractt.inherit;

import com.tutorial.packagee.classs.clas.concrete.RootClass;

public abstract class SuperClass extends RootClass {   // Parent (abstract) class

    // Provide a label used by display methods (visible to subclasses)
    protected static final String WORD = "SuperClass";

    // Encapsulated state
    private int event = 0;

    // -------------------------
    // Overloading (compile-time)
    // -------------------------

    /** Default change of +1 */
    public void change() {
        change(1);
    }

    /** Change by a delta */
    public void change(int delta) {
        setNewEvent(getNewEvent() + delta);
    }

    /** Overloaded variant with a qualifier */
    public void change(String type, int delta) {
        if ("Event".equals(type)) {
            change(delta);
        }
        // else: ignore or throw IllegalArgumentException based on your design
    }

    // Convenience mutators
    public void incrementEvent() { change(1); }
    public void decrementEvent() { change(-1); }

    // -------------------------
    // Encapsulation
    // -------------------------

    /** Final setters/getters to keep the lifecycle consistent across subclasses */
    public final void setNewEvent(int newEvent) {
        int old = this.event;
        this.event = newEvent;
        onEventChanged(old, newEvent);   // Polymorphic hook (runtime overriding)
    }

    public final int getNewEvent() {
        return this.event;
    }

    // -------------------------
    // Display
    // -------------------------

    public void displayCurrentEvent() {
        System.out.println(WORD + ", Event Details! " + getNewEvent());
    }

    // -------------------------
    // Overriding (runtime)
    // -------------------------

    /**
     * Polymorphic hook: subclasses define what happens whenever the event changes.
     * This is the primary override point showing dynamic dispatch.
     */
    protected abstract void onEventChanged(int oldEvent, int newEvent);
}
