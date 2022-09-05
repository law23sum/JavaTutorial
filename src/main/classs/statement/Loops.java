package src.main.classs.statement;

public class Loops {
    public static void main(String[] args) {
        loopFor();
    }
    private static void loopFor() {
        System.out.println("Liftoff T minus ");
        for (int count = 10; count >= 0; System.out.println((count==7) ? "7 ... Ignite" : count + " ..."), count--);
        System.out.println("Liftoff! We have liftoff!");
    }
}