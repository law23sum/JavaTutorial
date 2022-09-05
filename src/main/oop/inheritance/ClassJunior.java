package src.main.oop.inheritance;

public class ClassJunior extends ClassParent {
    static void methodJunior() {
        System.out.println("output: " + IDENTIFIER);
    }

    static void hideJunior() {
        System.out.println("output: " + personal);
    }

    private static final String personal = "hidden";
}
