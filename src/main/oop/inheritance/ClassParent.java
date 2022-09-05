package src.main.oop.inheritance;

public class ClassParent {
    public static void main(String[] args) {
        System.out.println("No instantiation for Class Junior: ");
        ClassJunior.methodJunior();
        ClassJunior.hideJunior();
        System.out.println(tab + "Trial");
    }

    static final String IDENTIFIER = "assignee";
    private static char tab = '\t';
}