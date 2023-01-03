// Static method is a method that belongs to the class, not to the instance of the class
//               access without creating the object of the class
//               early binding prevents  Static methods to be overridden
// NonStatic method
//               Non-Static methods can be overridden because of runtime binding
// Constructors
//               initializes an object & constructors have no explicit return type
//               Types : no-arg constructor, and parameterized
//               Constructor Overloading, different data types + sequence of parameters
// ComputableFuture
//               implements the Future interface; means can be use for future implementation but with additional logic
//               static methods unAsync and supplyAsync allow us to create a ComputableFuture instance out of Runnable and Supplier functional types

package packagee.classs.clas.method;

import org.junit.Test;
import packagee.classs.clas.method.Type.Overload.SetterGetter.Constructors.AccessModifiers;

import java.util.concurrent.ExecutionException;

import static packagee.classs.clas.method.Type.Overload.SetterGetter.*;


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
        
        runAsync() throws InterruptedException, ExecutionException {
            java.util.concurrent.CompletableFuture<Void> future = java.util.concurrent.CompletableFuture.runAsync(() -> System.out.println("runAsync method doesn not return any value"));
            System.out.println(future.get());
        }
        
        public void main(String[] args) {
            
            public static class SetterGetter {
                static Constructors.AnnotationTypes objectOne;
                private int value;
                
                public int getValue() {
                    return value;
                }
                
                public void setValue(int num) {
                    value = num;
                }
                
                face() {
                    // Implementing Runnable using old way
                    Runnable runnable = () -> System.out.println("Old Thread name : " + Thread.currentThread().getName());
                    Thread thread1 = new Thread(runnable);
                    Runnable runnable_new = () -> {  // Implementing Runnable using Lambda Expression
                        System.out.println("New Thread name : " + Thread.currentThread().getName());
                    };
                    Thread thread_new = new Thread(runnable_new);
                    thread1.start();          // Start Threads
                    thread_new.start();
                }
            }
            
            @Tes
            
            public static class Staticc {
                static {
                    System.out.println("this is an static initialization block");
                    staticMethod();
                }
                
                {
                    System.out.println("this is a initialization block");
                    nonStaticMethod();
                }
                
                static void staticMethod() {
                    System.out.println("called without instantiating");
                }
                
                void nonStaticMethod() {
                    System.out.println("called with instantiating");
                }
            }
            unctionalInter public static class Constructors {
                String label;
                int digit;
                float decimal;
                
                public Constructors() {                                                                                             // default values assigned
                    this("label", 0, 1.0F);
                }
                
                public Constructors(String label) {                                                                                 // assign unique values
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
                
                static class AnnotationTypes {
                    AnnotationTypes objectOne;
                    
                    AnnotationTypes(AnnotationTypes objectOne) {
                        this.objectOne = objectOne;
                    }
                    
                    // Javadoc comment follow
                    
                    /**
                     * @deprecated explanation of why it was deprecated
                     */
                    @Deprecated
                    static void deprecatedMethod() {
                    }
                    
                    // use a deprecated method and tell
                    // compiler not to generate a warning
                    void useDeprecatedMethod() {
                        // deprecation warning
                        // - suppressed
                        deprecatedMethod();
                    }
                }
                
                public static class AccessModifiers {
                    public static String[] accessModifyType = new String[4];
    
                    public static void world() {
                        accessModifyType = new String[]{"public"};
                    }
    
                    static void subClass() {                                                                           // package accessible
                        accessModifyType = new String[]{"public", "protected"};
                    }
    
                    protected static void packagee() {                                                                                   // class accessible
                        accessModifyType = new String[]{"public", "protected", "none"};
                    }
    
                    static void classs() {
                        accessModifyType = new String[]{"public", "protected", "none", "private"};
                    }
                }
            }
            
            void ft public void Overload load = new Overload();
            Overload.SetterGetter setget = new SetterGetter();
            Constructors construct = new Constructors();
            Constructors.AnnotationTypes annotate = new Constructors.AnnotationTypes(objectOne);
            Staticc staticc = new Staticc();
            AccessModifiers modify = new AccessModifiers();
            setget.setValue(100);
            System.out.println("\n\t\t" + setget.getValue());
            load.overLoad();
            load.overLoad(9);
            staticc.nonStaticMethod();
            Staticc.staticMethod();
            annotate.useDeprecatedMethod();
            for (String type : AccessModifiers.accessModifyType) {
                System.out.println(type + "\n");
            }
            setget.functionalInterface();
            try {
                runAsync();
            } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }
}