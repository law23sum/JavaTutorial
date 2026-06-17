package com.javatutorial.datastructures.maps;

import java.util.TreeMap;

/**
 * <h2>TreeMap</h2>
 * Red-black tree keyed map → keys always iterate in sorted order, all
 * operations O(log n). Supports navigation ({@code lower/higher,
 * floor/ceiling}) and range views ({@code subMap, headMap, tailMap}).
 */
public class TreeMapDemo {

    public static void main(String[] args) {
        TreeMap<String, Integer> grades = new TreeMap<>();
        grades.put("Bob", 88);
        grades.put("Ada", 95);
        grades.put("Cleo", 72);

        System.out.println("first    : " + grades.firstEntry());   // Ada=95
        System.out.println("last     : " + grades.lastEntry());    // Cleo=72
        System.out.println("ceil B   : " + grades.ceilingKey("B")); // Bob
        System.out.println("subMap   : " + grades.subMap("A", "C")); // {Ada=95, Bob=88}
        grades.forEach((k, v) -> System.out.println(k + " -> " + v)); // sorted
    }
}
