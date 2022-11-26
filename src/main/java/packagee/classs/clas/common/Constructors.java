package packagee.classs.clas.common;

public class Constructors {
    public Constructors() {                                                                                             // default values assigned
        this("label",0, 1.0F);
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

    boolean gate = false;
    String label;
    int digit;
    float decimal;
}