package src.main.classs.data.field;

public class FieldTypes {
    public static void main(String[] args) {
        FieldTypes a = new FieldTypes();
        a.booleanField();
        a.stringField();
        a.integerField();

    }

    void booleanField() {
        boolean innerStatus = true;
        status = innerStatus;
        System.out.println(status);
    }

    void stringField() {
        String exchange = "interact";
        personal = exchange;
        System.out.println(personal);
    }

    void integerField() {
        int calculation = (int) (((UNCHANGEABLE / (1 - UNCHANGEABLE)) - (1 / (1 + TRANSCENDENT))) * digit);
        System.out.println(calculation);
    }

    public int digit = 23;
    public float repeatable = 1 / 3;
    public static final double TRANSCENDENT = Math.sqrt(2);

    protected char statute = 's';
    protected final int UNCHANGEABLE = 42;

    private boolean status = false;
    private static String personal = "hidden";
}
