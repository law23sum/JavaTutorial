package com.tutorial.packagee.classs.clas.concrete.encapsulate;

import com.tutorial.packagee.classs.clas.abstractt.inherit.polymorph.SubClass;

public class BaseClass extends SubClass {

    // Public demo entry-point (callable from Runner)
    public static void runDemo() {
        BaseClass object1 = new BaseClass();

        object1.setNewState(21);     // encapsulation: setter used, internal field hidden
        int tempState = object1.getNewState();

        object1.setNewEvent(15);     // encapsulation on "event"
        object1.incrementEvent();
        int tempEvent = object1.getNewEvent();

        object1.change("Event", 1);  // polymorphic/overloaded behavior
        object1.incrementState();
        object1.change(2);           // overload: delta
        object1.decrementEvent();
        object1.decrementState();

        object1.displayCurrentEvent();
        object1.displayCurrentState();

        // Optional: quick echo so you can see captured temps
        System.out.println("Snapshot — state=" + tempState + ", event=" + tempEvent);
    }

    @Override
    protected void onStateChanged(int oldState, int newState) {
        System.out.println("[onStateChanged] " + oldState + " -> " + newState);
    }

    @Override
    protected void onEventChanged(int oldEvent, int newEvent) {
        System.out.println("[onEventChanged] " + oldEvent + " -> " + newEvent);
    }
}
