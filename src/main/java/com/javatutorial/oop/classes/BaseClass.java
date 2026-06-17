package com.javatutorial.oop.classes;

/**
 * <h2>Base / Super Class</h2>
 * A more specific class that builds on {@link RootClass}. Other classes will
 * extend this one — from <i>their</i> perspective, this is their <b>superclass</b>.
 * Same relationship, different word.
 */
public class BaseClass extends RootClass {

    protected final String name;

    public BaseClass(String name) { this.name = name; }

    @Override
    public String describe() { return "BaseClass(name=" + name + ")"; }
}
