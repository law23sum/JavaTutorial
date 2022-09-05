package src.main.classs.method.access;

public class AccessModifiers {
        public static void main(String[] arg){
                accessible();
                restriction();
                hidden();
        }

        public static void accessible() {
          // visible to public
        }

        protected static void restriction() {
          // visible within package
        }

        private static void hidden() {
          // visible within class
        }

}

