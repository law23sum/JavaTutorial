package edu.practice.overridden;

public class MotorCycle extends BiCycle {
    String define_me() {
        return "a cycle with an engine.";
    }

    MotorCycle() {
        System.out.println("Hello I am a motorcycle, I am " + define_me());

        String temp = super.define_me();  // calls overridden method from Super Class

        System.out.println("My ancestor is a cycle who is " + temp);
    }

    public static void main(String[] args) {
        MotorCycle motorcycle = new MotorCycle();
    }
}
