package com.tutorial.design.patterns.classs.creation.factorymethod;

abstract class SetsFactory implements Group {


}

//public abstract class Animal {
//   public abstract String makeSound();
//}
//
//public class Dog extends Animal {
//   public String makeSound() {
//      return "Bark";
//   }
//}
//
//public class Cat extends Animal {
//   public String makeSound() {
//      return "Meow";
//   }
//}
//
//public class AnimalFactory {
//   public static Animal getAnimal(String type) {
//      if ("dog".equals(type)) {
//         return new Dog();
//      } else if ("cat".equals(type)) {
//         return new Cat();
//      }
//      return null;
//   }
//}
//
//// usage
//Animal dog = AnimalFactory.getAnimal("dog");
//System.out.println(dog.makeSound()); // prints "Bark"
//
//Animal cat = AnimalFactory.getAnimal("cat");
//System.out.println(cat.makeSound()); // prints "Meow"