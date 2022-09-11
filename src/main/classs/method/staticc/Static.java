package src.main.classs.method.staticc;

public class Static {
    public static void main(String[] args) {
        staticMethod();
        Static a = new Static();
        a.nonStaticMethod();
    }

    static void staticMethod() {
        System.out.println("called without instantiating");
    }

    void nonStaticMethod() {
        System.out.println("called with instantiating");
    }
}