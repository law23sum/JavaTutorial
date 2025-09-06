package com.advance.catalog.methodchaining;

// Driver class
public class MethodChainingDemo {
   public static void main(String[] args) {
      ExampleBuilder exampleBuilder1 = new ExampleBuilder();
      ExampleBuilder exampleBuilder2 = new ExampleBuilder();

      exampleBuilder1.setId(1)
              .setName(" Ram & quot;")
              .setAddress(" Noida & quot;");
      exampleBuilder2.setId(2)
              .setName(" Shyam & quot;")
              .setAddress(" Delhi & quot;");

      System.out.println(exampleBuilder1);
      System.out.println(exampleBuilder2);
   }
}