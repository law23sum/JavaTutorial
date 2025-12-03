package com.tutorial.datastructure.collections.lists.arraylist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayLists {
    public static void main(String[] args) {
        // Start with A, B, C, D
        List<String> a = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        System.out.println("Start:      " + a);             // [A, B, C, D]

        // Append at end (amortized O(1))
        a.add("E");
        System.out.println("Append E:   " + a);             // [A, B, C, D, E]

        // Get and Set (O(1))
        String got = a.get(2);                               // "C"
        System.out.println("get(2):     " + got);
        a.set(2, "Z");                                       // overwrite index 2
        System.out.println("set(2,Z):   " + a);             // [A, B, Z, D, E]

        // Insert at index (O(n - i)): shifts tail right
        a.add(2, "X");
        System.out.println("add(2,X):   " + a);             // [A, B, X, Z, D, E]

        // Remove at index (O(n - i)): shifts tail left
        a.remove(1);
        System.out.println("remove(1):  " + a);             // [A, X, Z, D, E]

        // Bonus: remove by value vs index (common pitfall with Integer lists)
        List<Integer> nums = new ArrayList<>(Arrays.asList(10, 20, 30, 20));
        nums.remove(Integer.valueOf(20));                    // removes the *value* 20 (first match)
        System.out.println("remove val: " + nums);          // [10, 30, 20]
        nums.remove(1);                                      // removes index 1 (value 30)
        System.out.println("remove idx: " + nums);          // [10, 20]
    }
}
