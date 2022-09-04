package main.method;

public class Generic {
    public static void main(String[] args) {
        liftOff();
    }
    private static void liftOff() {
        System.out.println("Liftoff T minus ");
        for (int count = 10; count >= 0; System.out.println((count==7) ? "7 ... Ignite" : count + " ..."), count--);
        System.out.println("Liftoff! We have liftoff!");
    }
}