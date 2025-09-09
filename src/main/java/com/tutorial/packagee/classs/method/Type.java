package com.tutorial.packagee.classs.method;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Type System Sampler
 *
 * What this file demonstrates:
 * 1) Method Overloading (compile-time polymorphism).
 * 2) Nested Types (static nested classes) and visibility.
 * 3) Access Modifiers (public, protected, package-private, private via example).
 * 4) Special Kinds of Types: annotations, functional interfaces (lambdas), generics, arrays.
 * 5) Class vs. Object Lifecycle: static vs. instance initialization blocks.
 * 6) Constructor Overloading & Chaining.
 * 7) A single runnable demo that walks each concept.
 *
 * Keep this file as a reference—each section is intentionally small and explicit.
 */
public class Type {

    // Runs once when Type is loaded by the JVM.
    static {
        System.out.println("[Type] static init");
    }

    /** Grouping for overloading + supporting nested demos. */
    public static class Overload {

        // -----------------------
        // 1) METHOD OVERLOADING
        // -----------------------
        void overLoad() {
            System.out.println("[Overload] no-arg");
        }

        void overLoad(int digit) {
            System.out.println("[Overload] int-arg -> " + digit);
        }

        // ------------------------------------------------
        // 2) NESTED TYPES + ENCAPSULATION + LIFECYCLES
        // ------------------------------------------------
        public static class SetterGetter {

            // Static reference to illustrate type visibility across nested classes.
            static Constructors.AnnotationTypes objectOne;

            // Encapsulated state.
            private int value;

            public void setValue(int num) { this.value = num; }
            public int getValue() { return value; }

            /** Class vs. Object lifecycle demo. */
            public static class StaticInitDemo {
                static {
                    System.out.println("[StaticInitDemo] static init");
                    staticMethod();
                }

                { // instance init
                    System.out.println("[StaticInitDemo] instance init");
                    instanceMethod();
                }

                static void staticMethod() {
                    System.out.println("[StaticInitDemo] static method");
                }

                void instanceMethod() {
                    System.out.println("[StaticInitDemo] instance method");
                }
            }

            /** Constructor overloading + chaining. */
            public static class Constructors {
                String label;
                int digit;
                float decimal;

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

                /** Shows annotation usage and deprecated API signaling. */
                static class AnnotationTypes {
                    AnnotationTypes objectOne; // self-reference (type referencing its own type)

                    AnnotationTypes(AnnotationTypes objectOne) {
                        this.objectOne = objectOne;
                    }

                    void useDeprecatedMethod() {
                        deprecatedMethod(); // compiles; warns callers this is legacy
                    }

                    /** @deprecated Demonstration only; avoid in new code. */
                    @Deprecated
                    static void deprecatedMethod() {
                        // no-op
                    }
                }

                /** Access modifier examples using a String[] as a simple container. */
                public static class AccessModifiers {
                    public static String[] accessLevels = new String[4];

                    // PUBLIC: visible everywhere.
                    public static void world() {
                        accessLevels = new String[] {"public"};
                    }

                    // PACKAGE-PRIVATE: visible only within the package.
                    static void subClass() {
                        accessLevels = new String[] {"public", "protected"};
                    }

                    // PROTECTED: visible to same-package + subclasses.
                    protected static void packageLevel() {
                        accessLevels = new String[] {"public", "protected", "package-private"};
                    }

                    // PRIVATE (conceptual): we can’t actually call a private method from outside.
                    // For demonstration, we keep this package-private but name it “classLevel”.
                    static void classLevel() {
                        accessLevels = new String[] {"public", "protected", "package-private", "private"};
                    }
                }
            }

            // -----------------------------------------
            // 4) FUNCTIONAL INTERFACE + LAMBDAS/THREADS
            // -----------------------------------------
            void functionalInterfaceDemo() {
                Runnable r1 = () ->
                        System.out.println("[Thread] old name -> " + Thread.currentThread().getName());
                Runnable r2 = () ->
                        System.out.println("[Thread] new name -> " + Thread.currentThread().getName());

                Thread t1 = new Thread(r1, "Demo-1");
                Thread t2 = new Thread(r2, "Demo-2");
                t1.start();
                t2.start();
            }
        }

        // -----------------------------------------
        // 5) GENERICS + ASYNC (CompletableFuture<T>)
        // -----------------------------------------
        public void runAsync() throws InterruptedException, ExecutionException {
            CompletableFuture<Void> future =
                    CompletableFuture.runAsync(() ->
                            System.out.println("[CompletableFuture] runAsync side-effect only"));

            // Blocks until complete; returns null for Void.
            System.out.println("[CompletableFuture] get -> " + future.get());
        }

        // -----------------------
        // 6) WALK THE WHOLE DEMO
        // -----------------------
        public static void runDemo() {
            // Overloading
            Overload load = new Overload();
            load.overLoad();
            load.overLoad(9);

            // Encapsulation + accessors
            SetterGetter sg = new SetterGetter();
            sg.setValue(100);
            System.out.println("[SetterGetter] value -> " + sg.getValue());

            // Constructor chaining
            SetterGetter.Constructors constructors = new SetterGetter.Constructors();
            System.out.println("[Constructors] label=" + constructors.label
                    + ", digit=" + constructors.digit + ", decimal=" + constructors.decimal);

            // Annotation/deprecation demo
            SetterGetter.Constructors.AnnotationTypes at =
                    new SetterGetter.Constructors.AnnotationTypes(SetterGetter.objectOne);
            at.useDeprecatedMethod();

            // Static vs. instance initialization
            SetterGetter.StaticInitDemo demo = new SetterGetter.StaticInitDemo();
            demo.instanceMethod();
            SetterGetter.StaticInitDemo.staticMethod();

            // Access modifier progression (array as a typed container of Strings)
            SetterGetter.Constructors.AccessModifiers.classLevel();
            for (String lvl : SetterGetter.Constructors.AccessModifiers.accessLevels) {
                System.out.println("[Access] " + lvl);
            }

            // Functional interface + lambdas
            sg.functionalInterfaceDemo();

            // Generics + async
            try {
                load.runAsync();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
