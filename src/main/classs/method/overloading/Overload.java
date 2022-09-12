package src.main.classs.method.overloading;

public class Overload {
    public static void main(String[] args) {
        Overload load = new Overload();
        load.overLoad();
        load.overLoad("nine");
        load.overLoad(9);
    }

    void overLoad() {                                                                                                   // same method name different args
    }

    void overLoad(String label) {
        System.out.println(label);
    }

    void overLoad(int digit) {
        System.out.println(digit);
    }
}
