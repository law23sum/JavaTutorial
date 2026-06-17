package com.javatutorial.oop.classes;

/**
 * <h2>Sub Class (Derived / Child)</h2>
 * Inherits the fields and methods of {@link BaseClass} and may extend or
 * override them.
 *
 * <pre>
 *   RootClass  ← BaseClass  ← SubClass
 *   (root)        (super)        (sub)
 * </pre>
 */
public class SubClass extends BaseClass {

    private final int level;

    public SubClass(String name, int level) {
        super(name);          // delegate to superclass constructor
        this.level = level;
    }

    @Override
    public String describe() { return "SubClass(name=" + name + ", level=" + level + ")"; }

    public static void main(String[] args) {
        RootClass r = new RootClass();
        BaseClass b = new BaseClass("base");
        SubClass  s = new SubClass("sub", 7);

        System.out.println(r.describe());
        System.out.println(b.describe());
        System.out.println(s.describe());
    }
}
