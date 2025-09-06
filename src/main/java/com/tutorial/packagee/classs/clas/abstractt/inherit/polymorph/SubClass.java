package com.tutorial.packagee.classs.clas.abstractt.inherit.polymorph;

import com.tutorial.packagee.classs.clas.abstractt.inherit.SuperClass;

/*
 * NOTE: One file, multiple type angles:
 * - Interface-based polymorphism: Stateful
 * - Abstract base with overrides: SubClass
 * - Two concrete forms as static nested classes: LoggingSubClass, ValidatingSubClass
 * - Overloading: change(), change(int), change(int, boolean)
 * - Overriding (dynamic dispatch): displayCurrentState(), onStateChanged(...)
 */
public abstract class SubClass extends SuperClass {

    // ---------- Encapsulated State ----------
    private int newState = 0;

    // ---------- Overloading (compile-time polymorphism) ----------
    /** Default change of +1 */
    public void change() { change(1); }

    /** Change by a delta (positive or negative) */
    public void change(int delta) { setNewState(getNewState() + delta); }

    /** Overloaded variant with optional clamping */
    public void change(int delta, boolean clampNonNegative) {
        int next = getNewState() + delta;
        setNewState(clampNonNegative ? Math.max(0, next) : next);
    }

    // ---------- Convenience Mutators (satisfy Stateful) ----------
    @Override public void incrementState() { this.newState += 1; }
    @Override public void decrementState() { this.newState -= 1; }

    // ---------- Encapsulation ----------
    public void setNewState(int newState) {
        int old = this.newState;
        this.newState = newState;
        onStateChanged(old, newState); // NOTE: runtime polymorphism (overridable hook)
    }

    @Override public int getNewState() { return newState; }

    // ---------- Overriding Parent Behavior ----------
    @Override
    public void displayCurrentState() {
        System.out.println(WORD + ", Event Details! " + getNewState());
    }

    // ---------- Abstract Hook for Dynamic Polymorphism ----------
    protected abstract void onStateChanged(int oldState, int newState);

    // ========================================================================
    // Concrete forms as public static nested classes (no extra .java files!)
    // ========================================================================

    /** Logs every state transition. */
    public static class LoggingSubClass extends SubClass {
        @Override
        protected void onStateChanged(int oldState, int newState) {
            System.out.println("[LoggingSubClass] " + oldState + " → " + newState);
        }

        @Override
        protected void onEventChanged(int oldEvent, int newEvent) {
            System.out.println("[LoggingSubClass] Event Changed: " + oldEvent + " → " + newEvent);
        }

        // subclass-specific helper (used via instanceof demo)
        public void setTag(String tag) {
            System.out.println("[LoggingSubClass] tag = " + tag);
        }
    }

    /** Blocks negative state unless toggled. Demonstrates override behavior. */
    public static class ValidatingSubClass extends SubClass {
        @Override
        protected void onEventChanged(int oldEvent, int newEvent) {
            // Implementation can be added based on the requirements
        }
        private boolean allowNegative = false;

        public void setAllowNegative(boolean allowNegative) {
            this.allowNegative = allowNegative;
        }

        @Override
        protected void onStateChanged(int oldState, int newState) {
            if (!allowNegative && newState < 0) {
                System.out.println("[ValidatingSubClass] negative blocked, reverting to 0");
                setNewState(0); // NOTE: will invoke onStateChanged again
            }
        }
    }
}
