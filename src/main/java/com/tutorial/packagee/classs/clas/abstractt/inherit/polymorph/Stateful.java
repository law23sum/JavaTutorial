package com.tutorial.packagee.classs.clas.abstractt.inherit.polymorph;

// NOTE: Interface-based polymorphism: callers can hold references as Stateful
// and invoke behavior without knowing the concrete class.
public interface Stateful {
    void incrementState();
    void decrementState();
    int getNewState();
    void displayCurrentState();
}
