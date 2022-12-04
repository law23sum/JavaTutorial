package packagee.classs.clas;

import static packagee.classs.clas.MethodTypes.Overload.SetterGetter.Constructors.AccessModifiers.accessModifyType;

public class MethodTypes {
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

            class AnnotationTypes {
               AnnotationTypes(AnnotationTypes objectOne) {
                  this.objectOne = objectOne;
               }

               // use a deprecated method and tell
               // compiler not to generate a warning
               @SuppressWarnings("unchecked")
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

            public class AccessModifiers {
               public static void world() {
                  accessModifyType = new String[]{"public"};
               }

               static void subClass() {                                                                           // package accessible
                  accessModifyType = new String[]{"public", "protected"};
               }

               protected static void packagee() {                                                                                   // class accessible
                  accessModifyType = new String[]{"public", "protected", "none"};
               }

               private static void classs() {
                  accessModifyType = new String[]{"public", "protected", "none", "private"};
               }

               static String[] accessModifyType = new String[4];
            }
         }

         public static void main(String[] args) {
            Overload load = new Overload();
            Overload.SetterGetter setget = new SetterGetter();
            Overload.SetterGetter.Constructors construct = new Constructors();
            Overload.SetterGetter.Constructors.AnnotationTypes annotate = construct.new AnnotationTypes(objectOne);
            Overload.SetterGetter.Staticc staticc = new SetterGetter.Staticc();
            setget.setValue(100);
            System.out.println("\n\t\t" + setget.getValue());
            load.overLoad();
            load.overLoad(9);
            staticc.nonStaticMethod();
            Overload.SetterGetter.Staticc.staticMethod();
            annotate.useDeprecatedMethod();
            for (String type : accessModifyType) {
               System.out.println(type + "\n");
            }
         }
      }
   }
}