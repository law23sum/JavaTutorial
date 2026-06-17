package com.javatutorial.oop.interfaces;

/** A {@link Flyable} from the engineered world — same contract, different family. */
public class Airplane implements Flyable {

    @Override
    public void fly() { System.out.println("Airplane fires jet engines"); }

    @Override
    public String describeFlight() { return "Cruising at 35,000 ft"; }

    public static void main(String[] args) {
        Flyable[] fleet = { new Bird(), new Airplane() };
        for (Flyable f : fleet) {
            f.fly();
            System.out.println("  → " + f.describeFlight());
        }
    }
}
