// File: src/edu/practice/override/Soccer.java
package edu.practice.override;

/**
 * Subclass that *overrides* methods from Sports.
 * @Override ensures the signature matches a superclass method.
 */
public class Soccer extends Sports {

    /** Overrides Sports.getName(): same name + params; chosen at runtime. */
    @Override
    String getName() {
        return "Soccer Class";
    }

    /** Overrides Sports.getNumberOfTeamMembers(); calls the overridden getName(). */
    @Override
    void getNumberOfTeamMembers() {
        System.out.println("Each team has " + 11 + " players in " + getName());
        // Use 'super.getName()' to call the base version if needed.
    }

    public static void main(String[] args) {
        Sports base = new Sports();    // base type, base impl
        Soccer derived = new Soccer(); // derived type, overridden impl
        Sports poly = new Soccer();    // reference is Sports, object is Soccer

        base.getNumberOfTeamMembers();   // calls Sports version
        derived.getNumberOfTeamMembers(); // calls Soccer version
        poly.getNumberOfTeamMembers();    // calls Soccer version (dynamic dispatch)
    }
}
