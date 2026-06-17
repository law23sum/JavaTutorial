package com.javatutorial.datastructures.sets;

import java.util.TreeSet;

/**
 * <h2>TreeSet</h2>
 * Self-balancing red-black tree → elements always iterate in <b>sorted
 * order</b>. Operations cost O(log n). Supports navigation
 * ({@code lower/higher, floor/ceiling}) and range views
 * ({@code subSet, headSet, tailSet}).
 */
public class TreeSetDemo {

    public static void main(String[] args) {
        TreeSet<String> countries = new TreeSet<>();
        countries.add("US");
        countries.add("IN");
        countries.add("CA");
        countries.add("UK");

        System.out.println("sorted   : " + countries);            // [CA, IN, UK, US]
        System.out.println("first    : " + countries.first());
        System.out.println("last     : " + countries.last());
        System.out.println("floor IN : " + countries.floor("IN"));
        System.out.println("ceil  KZ : " + countries.ceiling("KZ"));
        System.out.println("subSet   : " + countries.subSet("CA", "UK"));
    }
}
