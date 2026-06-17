package com.javatutorial.oop.inheritance;

/**
 * <h2>Inheritance</h2>
 * {@code Dog extends Animal} → reuses fields/methods and may override behavior.
 * Use {@code super(...)} to delegate to the parent constructor.
 */
public class Dog extends Animal {

    public Dog(String name) { super(name); }

    @Override
    public void speak() { System.out.println(name + " says: Woof!"); }

    public static void main(String[] args) {
        Animal generic = new Animal("Animal");
        Animal rex     = new Dog("Rex");      // upcast — variable type is Animal
        generic.speak();                        // → "Animal makes a generic sound"
        rex.speak();                            // → "Rex says: Woof!"  (runtime dispatch)
    }
}
