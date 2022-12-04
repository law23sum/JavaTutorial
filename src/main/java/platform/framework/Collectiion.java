package platform.framework;

import java.util.ArrayList;
import java.util.Iterator;

public class Collectiion {
public ArrayList<String> IteratorUsage() {
   ArrayList<String> cars = new ArrayList<>();
   cars.add("Adui");
   cars.add("BMW");
   cars.add("Toyota");
   return cars;
}

public static void main(String[] args) {
   Collectiion iterate = new Collectiion();
   ArrayList<String> arr = iterate.IteratorUsage();
   Iterator<String> it = arr.iterator();
   System.out.println(it.hasNext());
   String temp = "Toyota";
   while (it.hasNext()) {
      if (temp.equals(it.next())) {
         it.remove();
      } else System.out.println(it.next());
   }
}
}