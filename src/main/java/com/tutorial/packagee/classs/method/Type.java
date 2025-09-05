package com.tutorial.packagee.classs.method;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

import static com.tutorial.packagee.classs.method.Type.Overload.SetterGetter.objectOne;

// Notes / High-level design approach:
// - This class demonstrates Java concepts like overloading, nested classes,
//   static vs. non-static behavior, constructors, access modifiers, deprecated
//   methods, lambdas, and CompletableFuture.
// - It’s structured with multiple nested classes to group related topics together
//   and to showcase how nested class scoping works in practice.

@RunWith(Enclosed.class)  // allows nested classes to be tested independently
public class Type {

    // Static initialization block: runs once when the class is loaded.
    static {
        System.out.println("\n\t\t to initialize");
    }

    // === Outer nested class to showcase method overloading ===
    public static class Overload {

        // Simple overloaded methods: same name, different parameter list.
        void overLoad() {
            System.out.println("nine");
        }

        void overLoad(int digit) {
            System.out.println(digit);
        }

        // === Nested class for demonstrating setters/getters and more ===
        public static class SetterGetter {
            static Constructors.AnnotationTypes objectOne;

            private int value;  // encapsulated field

            // Standard setter
            public void setValue(int num) {
                value = num;
            }

            // Standard getter
            public int getValue() {
                return value;
            }

            // === Inner class to demonstrate static vs. instance blocks ===
            public static class Staticc {
                // Static initialization block: executed once at class loading
                static {
                    System.out.println("this is a static initialization block");
                    staticMethod();  // can call static methods directly
                }

                // Instance initialization block: runs whenever a new object is created
                {
                    System.out.println("this is an instance initialization block");
                    nonStaticMethod();
                }

                // Static method can be called without an instance
                static void staticMethod() {
                    System.out.println("called without instantiating");
                }

                // Non-static method requires an object instance
                void nonStaticMethod() {
                    System.out.println("called with instantiating");
                }
            }

            // === Nested class to demonstrate constructor overloading ===
            public static class Constructors {
                // Constructor chaining: default constructor calls another constructor
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

                String label;
                int digit;
                float decimal;

                // === Class to demonstrate annotations and deprecation ===
                static class AnnotationTypes {
                    AnnotationTypes(AnnotationTypes objectOne) {
                        this.objectOne = objectOne;
                    }

                    // Calls a deprecated method, showing how to suppress warnings
                    void useDeprecatedMethod() {
                        deprecatedMethod();
                    }

                    /** @deprecated explanation of why it was deprecated */
                    @Deprecated
                    static void deprecatedMethod() {
                        // left intentionally blank
                    }

                    AnnotationTypes objectOne;
                }

                // === Class to showcase access modifier scopes ===
                public static class AccessModifiers {
                    public static String[] accessModifyType = new String[4];

                    public static void world() {
                        accessModifyType = new String[]{"public"};
                    }

                    static void subClass() {  // package-private
                        accessModifyType = new String[]{"public", "protected"};
                    }

                    protected static void packagee() {  // protected
                        accessModifyType = new String[]{"public", "protected", "none"};
                    }

                    static void classs() {  // private-like demonstration
                        accessModifyType = new String[]{"public", "protected", "none", "private"};
                    }
                }
            }

            // === Method demonstrating functional interfaces and lambdas ===
            void functionalInterface() {
                // Runnable with lambda: old style
                Runnable runnable = () ->
                        System.out.println("Old Thread name : " + Thread.currentThread().getName());
                Thread thread1 = new Thread(runnable);

                // Runnable with inline lambda expression
                Runnable runnableNew = () ->
                        System.out.println("New Thread name : " + Thread.currentThread().getName());
                Thread threadNew = new Thread(runnableNew);

                // Start both threads
                thread1.start();
                threadNew.start();
            }
        }

        // === Method demonstrating CompletableFuture usage ===
        @Test
        public void runAsync() throws InterruptedException, ExecutionException {
            // runAsync executes a Runnable asynchronously
            java.util.concurrent.CompletableFuture<Void> future =
                    java.util.concurrent.CompletableFuture.runAsync(
                            () -> System.out.println("runAsync method does not return any value"));

            // get() waits for the task to finish (blocking)
            System.out.println(future.get());
        }

        // === Demo entry point for Runner.java ===
        public static void runDemo() {
            // Demonstrates overloading
            Overload load = new Overload();

            // Demonstrates setters/getters
            SetterGetter setget = new SetterGetter();

            // Demonstrates constructor chaining
            SetterGetter.Constructors construct = new SetterGetter.Constructors();

            // Demonstrates annotations/deprecation
            SetterGetter.Constructors.AnnotationTypes annotate =
                    new SetterGetter.Constructors.AnnotationTypes(objectOne);

            // Demonstrates static and instance initialization
            SetterGetter.Staticc staticc = new SetterGetter.Staticc();

            // Demonstrates access modifiers setup
            SetterGetter.Constructors.AccessModifiers modify =
                    new SetterGetter.Constructors.AccessModifiers();

            // Encapsulation test: set and get value
            setget.setValue(100);
            System.out.println("\n\t\tvalue = " + setget.getValue());

            // Method overloading
            load.overLoad();
            load.overLoad(9);

            // Instance vs. static method
            staticc.nonStaticMethod();
            SetterGetter.Staticc.staticMethod();

            // Deprecated method usage
            annotate.useDeprecatedMethod();

            // Access modifier demo
            SetterGetter.Constructors.AccessModifiers.classs();
            for (String type : SetterGetter.Constructors.AccessModifiers.accessModifyType) {
                System.out.println(type + "\n");
            }

            // Functional interface (lambdas, threads)
            setget.functionalInterface();

            // CompletableFuture demo
            try {
                load.runAsync();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
