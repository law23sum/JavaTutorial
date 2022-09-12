package src.main.classs.method.access;

public class AccessModifiers {
        public static void main(String[] arg){
                accessible();
                restriction();
                hidden();
        }

        public static void accessible() {                                                                               // project accessible
        }

        protected static void restriction() {                                                                           // package accessible
        }

        private static void hidden() {                                                                                   // class accessible
        }
}

