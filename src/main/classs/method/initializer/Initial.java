package src.main.classs.method.initializer;

public class Initial {
    public static void main(String[] args) {
        Initial init = new Initial();
    }

    {       // initializer
        System.out.println("initializer");
    }
}
