package com.tutorial.packagee.classs.method;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

import static com.tutorial.packagee.classs.method.Type.Overload.SetterGetter.objectOne;

// Notes:
// - Static method: belongs to the class, accessed without an instance; early binding (cannot be overridden).
// - Non-static method: instance-bound; can be overridden via runtime binding.
// - Constructors: initialize objects; no explicit return type. Overloading varies parameter types/order.
// - CompletableFuture: implements Future with extra composition tools; runAsync/supplyAsync create tasks from Runnable/Supplier.

@RunWith(Enclosed.class)
public class Type {
    static {
        System.out.println("\n\t\t to initialize");
    }

    public static class Overload {
        void overLoad() {
            System.out.println("nine");
        }

        void overLoad(int digit) {
            System.out.println(digit);
        }

        public static class SetterGetter {
            static Constructors.AnnotationTypes objectOne;

            private int value;

            public void setValue(int num) {
                value = num;
            }

            public int getValue() {
                return value;
            }

            public static class Staticc {
                static {
                    System.out.println("this is a static initialization block");
                    staticMethod();
                }

                {
                    System.out.println("this is an instance initialization block");
                    nonStaticMethod();
                }

                static void staticMethod() {
                    System.out.println("called without instantiating");
                }

                void nonStaticMethod() {
                    System.out.println("called with instantiating");
                }
            }

            public static class Constructors {
                public Constructors() {                                      // default values assigned
                    this("label", 0, 1.0F);
                }

                public Constructors(String label) {                          // assign unique values
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

                static class AnnotationTypes {
                    AnnotationTypes(AnnotationTypes objectOne) {
                        this.objectOne = objectOne;
                    }

                    void useDeprecatedMethod() {
                        // deprecation warning suppressed by @Deprecated on the method definition
                        deprecatedMethod();
                    }

                    /** @deprecated explanation of why it was deprecated */
                    @Deprecated
                    static void deprecatedMethod() {
                        // intentionally empty
                    }

                    AnnotationTypes objectOne;
                }

                public static class AccessModifiers {
                    public static String[] accessModifyType = new String[4];

                    public static void world() {
                        accessModifyType = new String[]{"public"};
                    }

                    static void subClass() {                                  // package-accessible
                        accessModifyType = new String[]{"public", "protected"};
                    }

                    protected static void packagee() {                         // subclass-accessible (and package)
                        accessModifyType = new String[]{"public", "protected", "none"};
                    }

                    static void classs() {
                        accessModifyType = new String[]{"public", "protected", "none", "private"};
                    }
                }
            }

            void functionalInterface() {
                // Implementing Runnable using lambda
                Runnable runnable = () -> System.out.println("Old Thread name : " + Thread.currentThread().getName());
                Thread thread1 = new Thread(runnable);

                Runnable runnableNew = () -> System.out.println("New Thread name : " + Thread.currentThread().getName());
                Thread threadNew = new Thread(runnableNew);

                thread1.start();
                threadNew.start();
            }
        }

        @Test
        public void runAsync() throws InterruptedException, ExecutionException {
            java.util.concurrent.CompletableFuture<Void> future =
                    java.util.concurrent.CompletableFuture.runAsync(
                            () -> System.out.println("runAsync method does not return any value"));
            System.out.println(future.get());
        }

        // ===== Demo entry point for Runner =====
        public static void runDemo() {
            Overload load = new Overload();
            SetterGetter setget = new SetterGetter();
            SetterGetter.Constructors construct = new SetterGetter.Constructors();
            SetterGetter.Constructors.AnnotationTypes annotate =
                    new SetterGetter.Constructors.AnnotationTypes(objectOne);
            SetterGetter.Staticc staticc = new SetterGetter.Staticc();
            SetterGetter.Constructors.AccessModifiers modify =
                    new SetterGetter.Constructors.AccessModifiers();

            setget.setValue(100);
            System.out.println("\n\t\tvalue = " + setget.getValue());

            load.overLoad();
            load.overLoad(9);

            // instance vs static
            staticc.nonStaticMethod();
            SetterGetter.Staticc.staticMethod();

            // deprecated usage via wrapper
            annotate.useDeprecatedMethod();

            // populate access modifiers then print
            SetterGetter.Constructors.AccessModifiers.classs();
            for (String type : SetterGetter.Constructors.AccessModifiers.accessModifyType) {
                System.out.println(type + "\n");
            }

            // threads demo
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
