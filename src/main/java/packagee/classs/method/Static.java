package packagee.classs.method;

public class Static {
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

    public static void main(String[] args) {
        new Static();
    }
}