package com.advance.catalog.methodchaining;
// Java code to demonstrate method chaining

final class ExampleBuilder {
   // instance fields
   private int id;
   private String name;
   private String address;

   // Setter Methods
   // Note that all setters method
   // return this reference
   public ExampleBuilder setId(int id) {
      this.id = id;
      return this;
   }

   public ExampleBuilder setName(String name) {
      this.name = name;
      return this;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   @Override
   public String toString() {
      return " id = " + this.id + ", name = " + this.name + ", address = " + this.address;
   }
}