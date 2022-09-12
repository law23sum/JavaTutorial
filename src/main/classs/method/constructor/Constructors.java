package src.main.classs.method.constructor;

public class Constructors {
    public static void main(String[] args) {
        Constructors a = new Constructors();
        Constructors b = new Constructors("Person", 1);
        Constructors c = new Constructors("Individual", 2, 5.55F);
        a.display();
        b.display();
        c.display();
    }

    public Constructors() {                                                                                             // default values assigned
        label = "label";
        digit = 55;
        decimal = 1.0F;
    }

    public Constructors(String label) {                                                                                 // assign unique values
        this.label = label;
    }

    public Constructors(String label, int digit) {
        this(label);
        this.digit = digit;
    }

    public Constructors(String label, int digit, float decimal) {
        this(label, digit);
        this.decimal = decimal;
    }

    public void display() {
        System.out.println(this.label + "\t" + this.digit + "\t" + this.gate + "\t" + this.decimal);
    }

    String label;
    int digit;
    float decimal;
    final boolean gate = false;
}
