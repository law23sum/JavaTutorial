package main.method;

public class AccessModifiers {
        public static void main(String[] arg){
                accessible();
                restriction();
                hidden();
        }

        public static void accessible() {
          // visible within src
        }

        protected static void restriction() {
          // visible within package
        }

        private static void hidden() {
          // visible within class
        }

}

