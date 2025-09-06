// Java Project
// Java Topic (The Collections Framework)
// Java Notes
//
//           Iterator Interface shown on usage. Iterator, & Collections are interfaces while ArrayList are its
//           implementations, hence usage of iterators methods.
package com.tutorial.framework;

import java.util.ArrayList;
import java.util.Iterator;

public class Collectiion {

    public static void iteratorUsage() {
        ArrayList<Object> cars = new ArrayList<>();
        cars.add("Audi");
        cars.add("BMW");
        cars.add("Toyota");

        Iterator<Object> it = cars.iterator();
        String target = "Toyota";

        while (it.hasNext()) {
            String current = (String) it.next(); // advance exactly once
            if (target.equals(current)) {
                it.remove();            // safely remove the last returned element
            } else {
                System.out.println(current);
            }
        }

        System.out.println("Remaining: " + cars);
    }


    public static void main(String[] args) {
        iteratorUsage();
    }
}