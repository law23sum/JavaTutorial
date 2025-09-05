// Java Project
// Java Topic (The Collections Framework)
// Java Notes
//
//           Iterator Interface shown on usage. Iterator, & Collections are interfaces while ArrayList are its
//           implementations, hence usage of iterators methods.
package com.tutorial.platform.framework;

import java.util.ArrayList;
import java.util.Iterator;

public class Collectiion {

public static void IteratorUsage() {
   ArrayList<String> cars = new ArrayList<>();
   cars.add("Adui");
   cars.add("BMW");
   cars.add("Toyota");
   Collectiion iterate = new Collectiion();
   Iterator<String> it = cars.iterator();
   System.out.println(it.hasNext());
   String temp = "Toyota";
   while (it.hasNext()) {
      if (temp.equals(it.next())) {
         it.remove();
      } else System.out.println(it.next());
   }
}

public static void main(String[] args) {
 IteratorUsage();
}
}