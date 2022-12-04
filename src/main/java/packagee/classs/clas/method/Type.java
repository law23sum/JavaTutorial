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

import java.util.concurrent.ExecutionException;


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

      public void setValue(int num) {
         value = num;
      }

      public int getValue() {
         return value;
      }

      private int value;

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

      public static class Constructors {
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

         String label;
         int digit;
         float decimal;

         static class AnnotationTypes {
            AnnotationTypes(AnnotationTypes objectOne) {
               this.objectOne = objectOne;
            }

            // use a deprecated method and tell
            // compiler not to generate a warning
            void useDeprecatedMethod() {
               // deprecation warning
               // - suppressed
               deprecatedMethod();
            }

            // Javadoc comment follow

            /**
             * @deprecated explanation of why it was deprecated
             */
            @Deprecated
            static void deprecatedMethod() {
            }

            AnnotationTypes objectOne;
         }

         public static class AccessModifiers {
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

            public static String[] accessModifyType = new String[4];
         }
      }

      void functionalInterface() {
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

   @Test
   public void runAsync() throws InterruptedException, ExecutionException {
      java.util.concurrent.CompletableFuture<Void> future = java.util.concurrent.CompletableFuture.runAsync(() -> System.out.println("runAsync method doesn not return any value"));
      System.out.println(future.get());
   }

   public void main(String[] args) {
      Overload load = new Overload();
      Overload.SetterGetter setget = new SetterGetter();
      Overload.SetterGetter.Constructors construct = new Type.Overload.SetterGetter.Constructors();
      Overload.SetterGetter.Constructors.AnnotationTypes annotate = new Type.Overload.SetterGetter.Constructors.AnnotationTypes(Type.Overload.SetterGetter.objectOne);
      Overload.SetterGetter.Staticc staticc = new SetterGetter.Staticc();
      Overload.SetterGetter.Constructors.AccessModifiers modify = new Type.Overload.SetterGetter.Constructors.AccessModifiers();
      setget.setValue(100);
      System.out.println("\n\t\t" + setget.getValue());
      load.overLoad();
      load.overLoad(9);
      staticc.nonStaticMethod();
      Overload.SetterGetter.Staticc.staticMethod();
      annotate.useDeprecatedMethod();
      for (String type : modify.accessModifyType) {
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