package packagee.classs.variable;

import javax.lang.model.element.TypeElement;

public class Types {
    public Types() {
    }

    void primitiveTypes() {
        innerStatus = true;
        statute = '\u0000';
        repeatable = 1.0f / 3.0f;
        value = (int) (digit * repeatable);
    }

    void wrapperClasses() {
        calculation = Double.valueOf((((UNCHANGEABLE / (1 - UNCHANGEABLE)) - (1 / (1 + TRANSCENDENT.intValue()))) * digit));
        exchange = "interact";
    }

    {
        UNCHANGEABLE = 42;
        TRANSCENDENT = Math.sqrt(2.03d);
        digit = 50;
        decVal = 26;
        hexVal = 0x1a;
        binVal = 0b11010;
        d1 = 123.4;
        d2 = 1.234e2;
        f1 = 123.4f;
        creditCardNumber = 1234_5678_9012_3456L;
        socialSecurityNumber = 999_99_9999L;
        pi = 3.14_15F;
        hexBytes = 0xFF_EC_DE_5E;
        hexWords = 0xCAFE_BABE;
        maxLong = 0x7fff_ffff_ffff_ffffL;
        nybbles = 0b0010_0101;
        bytes = 0b11010010_01101001_10010100_10010010;
    }

    String exchange;
    Integer UNCHANGEABLE;
    Double TRANSCENDENT;
    int digit;
    boolean innerStatus;
    char statute;
    float repeatable;
    int value;
    Double calculation;
    int decVal;
    int hexVal;
    int binVal;
    double d1;
    double d2;
    float f1;
    long creditCardNumber = 1234_5678_9012_3456L;
    long socialSecurityNumber = 999_99_9999L;
    float pi = 3.14_15F;
    long hexBytes = 0xFF_EC_DE_5E;
    long hexWords = 0xCAFE_BABE;
    long maxLong = 0x7fff_ffff_ffff_ffffL;
    byte nybbles = 0b0010_0101;
    long bytes = 0b11010010_01101001_10010100_10010010;

    public static void main(String[] args) {
        Types Type = new Types();
        Type.primitiveTypes();
        Type.wrapperClasses();
        System.out.println(Type.calculation);
    }
}