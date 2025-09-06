package com.tutorial.packagee.classs.method;

import java.util.concurrent.ExecutionException;

import static com.tutorial.packagee.classs.method.Type.Overload.SetterGetter.objectOne;

/*
 * WHAT THIS FILE TEACHES ABOUT "TYPES" IN JAVA
 * --------------------------------------------
 * 1) Overloaded Method Signatures (compile-time polymorphism):
 *    - Same method name, different parameter *types/arity* ⇒ the compiler chooses a target by the signature.
 *
 * 2) Declaring and Using Nested Types:
 *    - Top-level class (Type)
 *    - Static nested classes (Type.Overload, Overload.SetterGetter, etc.)
 *    - Instance members vs. static members inside those types.
 *
 * 3) Member Visibility and Where a Type/Member is Legal to Access:
 *    - public / protected / package-private (no keyword) / private (demonstrated via comments + arrays)
 *
 * 4) Special Kinds of Types:
 *    - Annotation types (e.g., @Deprecated) and how they affect API contracts
 *    - Functional interface type (Runnable) used with lambdas
 *    - Generic types (CompletableFuture<Void>) and parameterized return/argument types
 *    - Array types (String[]) as first-class reference types
 *
 * 5) Object Lifecycle vs. Class Lifecycle:
 *    - Static initialization blocks (run once when the *type* is loaded)
 *    - Instance initialization blocks (run per *object* construction)
 *
 * 6) Constructor Overloading & Chaining:
 *    - Multiple constructors with different parameter types/arity, delegating via this(...)
 *
 * 7) Testing Type Scopes:
 *    - @RunWith(Enclosed.class) treats nested classes as independent test containers.
 *
 * NOTE: This is a "type system in practice" sampler—meant to make the surfaces of the type system visible.
 */

public class Type {

    // Class (type) initialization: executed once when the Type class is loaded by the JVM.
    static {
        System.out.println("\n\t\t to initialize");
    }

    // === A static nested class used to group "overloading" examples and supporting nested types ===
    public static class Overload {

        // --- OVERLOADING: same method name, different parameter lists (signatures) ---
        // The *type* (and/or number) of parameters differentiates these methods.
        void overLoad() {
            System.out.println("nine");
        }

        void overLoad(int digit) {
            System.out.println(digit);
        }

        // === Nested type grouping "setter/getter", init blocks, constructors, modifiers, lambdas ===
        public static class SetterGetter {
            // A static reference to another type instance (to show static field typing and visibility)
            static Constructors.AnnotationTypes objectOne;

            // Encapsulation: private state is only accessible through typed accessors.
            private int value;

            // Mutator uses an int (primitive type) parameter.
            public void setValue(int num) {
                value = num;
            }

            // Accessor returns an int (primitive type).
            public int getValue() {
                return value;
            }

            // === Static vs. instance initialization illustrates class vs. object "lifetimes" ===
            public static class Staticc {
                // Static init: belongs to the *class* (Type Staticc), runs once at class-load time.
                static {
                    System.out.println("this is a static initialization block");
                    staticMethod();  // legal: calling a static member from a static context
                }

                // Instance init: runs *each time* you construct a new object of this type.
                {
                    System.out.println("this is an instance initialization block");
                    nonStaticMethod(); // legal here because 'this' exists
                }

                // Static method: no receiver (belongs to the type itself).
                static void staticMethod() {
                    System.out.println("called without instantiating");
                }

                // Instance method: requires a receiver object of type Staticc.
                void nonStaticMethod() {
                    System.out.println("called with instantiating");
                }
            }

            // === Constructor overloading + chaining to reduce duplication ===
            public static class Constructors {

                // Fields demonstrate simple member types.
                String label;
                int digit;
                float decimal;

                // No-arg constructor delegates to the most specific one.
                public Constructors() {
                    this("label", 0, 1.0F);
                }

                public Constructors(String label) {
                    this.label = label;
                }

                public Constructors(String label, int digit) {
                    this(label);
                    this.digit = digit;
                }

                public Constructors(String label, int digit, float decimal) {
                    this(label, digit);
                    this.decimal = decimal;
                }

                // --- A nested helper to show annotation usage and API evolution contracts ---
                static class AnnotationTypes {
                    AnnotationTypes(AnnotationTypes objectOne) {
                        this.objectOne = objectOne;
                    }

                    // Calls a deprecated API to illustrate how deprecation marks a type/member as legacy.
                    void useDeprecatedMethod() {
                        deprecatedMethod();
                    }

                    /** @deprecated This method remains to demonstrate deprecation; do not use in new code. */
                    @Deprecated
                    static void deprecatedMethod() {
                        // intentionally blank
                    }

                    AnnotationTypes objectOne; // self-referential field (type refers to itself)
                }

                // --- Access-modifier demo: what can "see" which types/members? ---
                public static class AccessModifiers {
                    // Array is a reference type; here, an array of String references.
                    public static String[] accessModifyType = new String[4];

                    // PUBLIC: visible everywhere the class is visible.
                    public static void world() {
                        accessModifyType = new String[]{"public"};
                    }

                    // PACKAGE-PRIVATE (no modifier): visible only within the same package.
                    static void subClass() {
                        accessModifyType = new String[]{"public", "protected"};
                    }

                    // PROTECTED: visible to same-package classes and subclasses.
                    protected static void packagee() {
                        accessModifyType = new String[]{"public", "protected", "none"};
                    }

                    // PRIVATE-like demonstration via comment: truly private would be invisible outside the class.
                    // Here we just show the *idea* by naming; the method itself is package-private for demo.
                    static void classs() {
                        accessModifyType = new String[]{"public", "protected", "none", "private"};
                    }
                }
            }

            // === Functional interface + lambda expressions ===
            // Runnable is a *functional interface type* (exactly one abstract method), so lambdas can target it.
            void functionalInterface() {
                // Lambda captures the current thread name at runtime; type of 'runnable' is Runnable.
                Runnable runnable = () ->
                        System.out.println("Old Thread name : " + Thread.currentThread().getName());
                Thread thread1 = new Thread(runnable);

                // Inline lambda; still of type Runnable. Demonstrates the same type via different expression.
                Runnable runnableNew = () ->
                        System.out.println("New Thread name : " + Thread.currentThread().getName());
                Thread threadNew = new Thread(runnableNew);

                // Start both threads—side effects demonstrate concurrency on typed Thread objects.
                thread1.start();
                threadNew.start();
            }
        }

        // === CompletableFuture<T> illustrates GENERIC TYPE PARAMETERS and async pipelines ===
        public void runAsync() throws InterruptedException, ExecutionException {
            // Type argument is Void (a reference type used to represent "no value" in generics).
            java.util.concurrent.CompletableFuture<Void> future =
                    java.util.concurrent.CompletableFuture.runAsync(
                            () -> System.out.println("runAsync method does not return any value"));

            // get() blocks until completion and returns a value of type Void (here, null).
            System.out.println(future.get());
        }

        // === Demo entry point for a Runner class to call; walks each concept once ===
        public static void runDemo() {
            // Overloading demo (type-based dispatch at compile time).
            Overload load = new Overload();

            // Encapsulation + accessors (primitive type field with typed getters/setters).
            SetterGetter setget = new SetterGetter();

            // Constructor chaining (different constructor *types/arity*).
            SetterGetter.Constructors construct = new SetterGetter.Constructors();

            // Annotation/deprecation demo: object acts on its own *type* API.
            SetterGetter.Constructors.AnnotationTypes annotate =
                    new SetterGetter.Constructors.AnnotationTypes(objectOne);

            // Static vs. instance initialization blocks and methods.
            SetterGetter.Staticc staticc = new SetterGetter.Staticc();

            // Access modifiers demo—populate the String[] based on which scope we “pretend” to be in.
            SetterGetter.Constructors.AccessModifiers modify =
                    new SetterGetter.Constructors.AccessModifiers();

            // Encapsulation test
            setget.setValue(100);
            System.out.println("\n\t\tvalue = " + setget.getValue());

            // Method overloading in action
            load.overLoad();
            load.overLoad(9);

            // Instance vs static method calls
            staticc.nonStaticMethod();
            SetterGetter.Staticc.staticMethod();

            // Deprecated method usage (compiles, but signals "legacy" via @Deprecated)
            annotate.useDeprecatedMethod();

            // Access modifier “progression” (array as a typed container of Strings)
            SetterGetter.Constructors.AccessModifiers.classs();
            for (String type : SetterGetter.Constructors.AccessModifiers.accessModifyType) {
                System.out.println(type + "\n");
            }

            // Functional interface + lambdas/threads
            setget.functionalInterface();

            // CompletableFuture (generic type) demo
            try {
                load.runAsync();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
