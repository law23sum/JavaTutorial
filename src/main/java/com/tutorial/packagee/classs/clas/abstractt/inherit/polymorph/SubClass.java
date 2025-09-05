package com.tutorial.packagee.classs.clas.abstractt.inherit.polymorph;

import com.tutorial.packagee.classs.clas.abstractt.inherit.SuperClass;

public abstract class SubClass extends SuperClass {   // Child class extending SuperClass

    // -------------------------
    // Encapsulated State
    // -------------------------
    private int newState = 0;   // Private variable; access controlled via getter/setter

    // -------------------------
    // Overloading (compile-time polymorphism)
    // -------------------------

    /** Default change of +1 */
    public void change() {
        change(1);  // delegates to the int overload
    }

    /** Change by a delta (positive or negative) */
    public void change(int delta) {
        setNewState(getNewState() + delta);
    }

    /** Overloaded variant with optional clamping */
    public void change(int delta, boolean clampNonNegative) {
        int next = getNewState() + delta;
        setNewState(clampNonNegative ? Math.max(0, next) : next);
    }

    // -------------------------
    // Convenience Mutators
    // -------------------------

    /** Increment state by 1 */
    public void incrementState() { this.newState += 1; }

    /** Decrement state by 1 */
    public void decrementState() { this.newState -= 1; }

    // -------------------------
    // Encapsulation (getters/setters)
    // -------------------------

    /** Setter that also triggers the polymorphic callback */
    public void setNewState(int newState) {
        int old = this.newState;
        this.newState = newState;
        onStateChanged(old, newState);   // Runtime polymorphism (dynamic dispatch)
    }

    /** Getter for the state */
    public int getNewState() { return newState; }

    // -------------------------
    // Overriding Parent Behavior
    // -------------------------

    /** Override of SuperClass display method */
    @Override
    public void displayCurrentState() {
        System.out.println(WORD + ", Event Details! " + getNewState());
    }

    // -------------------------
    // Abstract Method for Dynamic Polymorphism
    // -------------------------

    /**
     * Hook method: subclasses must define how they react whenever state changes.
     * This enforces runtime polymorphism (method overriding).
     */
    protected abstract void onStateChanged(int oldState, int newState);
}
