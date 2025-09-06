package com.tutorial.foundation;

import java.math.*;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.*;
import java.util.stream.*;

/**
 * One-file tour of Java **Data Types** — from primitives to architect-grade patterns.
 * Run this single file. Each section is a tiny, self-contained demo you can scan.
 * Target: Java 17+ (records, text blocks, sealed types, pattern matching in instanceof comments).
 */
public class DataTypes {

    // ───────────────────────────── 1) PRIMITIVES & LITERALS ─────────────────────────────
    static void primitivesAndLiterals() {
        // Integer family
        byte by = 0x7F;                 // 127 (hex literal)
        short sh = 010;                 // 8   (octal literal)
        int i = 0b1010_1010;            // 170 (binary with underscores)
        long lg = 1_000_000_000L;       // long suffix

        // Floating family
        float f = 3.14f;                // float suffix
        double d = 6.022e23;            // scientific notation

        // Char & boolean
        char pi = '\u03C0';            // π
        boolean ok = true;

        // Overflow is defined (wrap-around)
        byte x = 127; x++;              // now -128

        System.out.printf("[Primitives] by=%d sh=%d i=%d lg=%d f=%.2f d=%s pi=%s ok=%b xOverflow=%d%n",
                by, sh, i, lg, f, d, pi, ok, x);
    }

    // Numeric promotion & casts
    static void promotionAndCasting() {
        byte a = 40, b = 50;          // arithmetic promotes to int
        int sum = a + b;              // 90
        short back = (short) (a + b); // explicit cast back
        double z = 1/2;               // int division => 0 => widened to 0.0
        double zFix = 1/2.0;          // 0.5
        System.out.printf("[Promotion] sum=%d back=%d z=%f zFix=%f%n", sum, back, z, zFix);
    }

    // ───────────────────────────── 2) WRAPPERS & AUTOBOXING ─────────────────────────────
    static void wrappersAndAutoboxing() {
        Integer X = 10;        // autoboxing
        int y = X;             // unboxing
        List<Integer> xs = new ArrayList<>();
        for (int k = 0; k < 3; k++) xs.add(k); // boxing in loop

        // Beware: unboxing null -> NPE
        Integer mayNull = null;
        boolean npe = false;
        try { int bad = mayNull; } catch (NullPointerException e) { npe = true; }

        // Wrapper equality pitfalls (cache -128..127)
        Integer a = 127, b = 127, c = 128, d = 128;
        System.out.printf("[Wrappers] xs=%s npeOnUnbox=%b (127==127)=%b (128==128)=%b equals128=%b%n",
                xs, npe, (a==b), (c==d), c.equals(d));
    }

    // ─────────────────── 3) BIG NUMBERS: BigInteger / BigDecimal & Money policy ───────────────────
    record Money(BigDecimal amount, String currency) {
        public Money {
            amount = amount.setScale(2, RoundingMode.HALF_EVEN);
            if (!currency.matches("^[A-Z]{3}$")) throw new IllegalArgumentException("ISO 4217 code");
        }
        Money add(Money other){
            if (!currency.equals(other.currency)) throw new IllegalArgumentException("currency mismatch");
            return new Money(amount.add(other.amount), currency);
        }
    }

    static void bigNumbersAndMoney() {
        BigInteger bi = new BigInteger("123456789012345678901234567890");
        BigDecimal a = new BigDecimal("0.1"); // String ctor for exactness
        BigDecimal b = new BigDecimal("0.2");
        BigDecimal exact = a.add(b);          // 0.3 exactly

        Money m1 = new Money(new BigDecimal("1.00"), "USD");
        Money m2 = new Money(new BigDecimal("2.50"), "USD");
        Money total = m1.add(m2);

        System.out.printf("[Big] bi=%s exact=%.1f moneyTotal=%s %s%n", bi, exact, total.amount(), total.currency());
    }

    // ───────────────────────────── 4) STRINGS & TEXT ─────────────────────────────
    static void stringsAndText() {
        String s = "Ada";
        String internedEq = ("Ada" == s) ? "sameRef" : "diffRef"; // literals are interned
        boolean valueEq = new String("Ada").equals(s);

        // concat vs StringBuilder
        String a = "a"; for (int i=0;i<5;i++) a += i; // many temps
        StringBuilder sb = new StringBuilder("a"); for (int i=0;i<5;i++) sb.append(i);

        // text block
        String json = """
            {"name":"Ada","lang":"Java"}
            """;

        // bytes/charset
        byte[] bytes = "π".getBytes(StandardCharsets.UTF_8);

        System.out.printf("[Strings] interned=%s valueEq=%b concat=%s builder=%s jsonLen=%d bytes=%s%n",
                internedEq, valueEq, a, sb, json.length(), Arrays.toString(bytes));
    }

    // ───────────────────────────── 5) ARRAYS, LISTS, PRIMITIVE STREAMS ─────────────────────────────
    static void arraysAndCollections() {
        int[] xs = {1,2,3};
        int len = xs.length;
        String arr = Arrays.toString(xs);

        // Arrays are covariant; Lists (generics) are invariant.
        Number[] arrNum = new Integer[2];
        boolean threw = false;
        try { arrNum[0] = 3.14; } catch (ArrayStoreException e) { threw = true; }

        int sum = IntStream.of(1,2,3).sum();
        double avg = IntStream.rangeClosed(1,4).average().orElse(0);
        System.out.printf("[Arrays] len=%d %s covariantStoreError=%b sum=%d avg=%.1f%n", len, arr, threw, sum, avg);
    }

    // ───────────────────────────── 6) TIME TYPES (`java.time`) ─────────────────────────────
    static void timeTypes() {
        LocalDate d = LocalDate.of(2025, 9, 6);
        LocalTime t = LocalTime.of(12, 34, 56);
        ZonedDateTime z = ZonedDateTime.now(ZoneId.of("America/New_York"));
        System.out.printf("[Time] date=%s time=%s offset=%s%n", d, t, z.getOffset());
    }

    // ───────────────────────────── 7) OPTIONAL & NULL-OBJECT ─────────────────────────────
    static Optional<Integer> parseInt(String s){
        try { return Optional.of(Integer.parseInt(s)); }
        catch (NumberFormatException e){ return Optional.empty(); }
    }

    interface Discount { int apply(int price); }
    static final class NoDiscount implements Discount { public int apply(int price){ return price; } }
    static final class TenPercent implements Discount { public int apply(int price){ return (int)(price * 0.9); } }

    static void optionalsAndNullObject() {
        int good = parseInt("42").orElse(0);
        int fallback = parseInt("xx").orElseGet(() -> 7);
        Discount d = new NoDiscount();
        System.out.printf("[Optional] good=%d fallback=%d noDiscount(100)=%d%n", good, fallback, d.apply(100));
    }

    // ───────────────────────────── 8) ENUMS, RECORDS, SEALED (type system pieces) ─────────────────────────────
    enum Unit { BYTES, KILOBYTES, MEGABYTES }
    static long toBytes(long n, Unit u){
        return switch (u) {
            case BYTES -> n;
            case KILOBYTES -> n * 1024L;
            case MEGABYTES -> n * 1024L * 1024L;
        };
    }

    public record Email(String value) {
        public Email {
            if (!value.matches("^[^@]+@[^@]+\\.[^@]+$")) throw new IllegalArgumentException("bad email");
        }
    }

    sealed interface Quantity permits Count, Weight {}
    static final class Count implements Quantity { final int n; Count(int n){this.n=n;} }
    static final class Weight implements Quantity { final double kg; Weight(double kg){this.kg=kg;} }

    static void typeSystemPieces() {
        long bytes = toBytes(2, Unit.MEGABYTES);
        Email e = new Email("ada@lovelace.org");
        Quantity q1 = new Count(3);
        Quantity q2 = new Weight(1.5);
        System.out.printf("[Types] bytes=%d email=%s q1=%s q2=%.1fkg%n", bytes, e.value(), q1.getClass().getSimpleName(), ((Weight)q2).kg);
    }

    // ───────────────────────────── 9) GENERICS & VARIANCE (PECS) ─────────────────────────────
    static class Box<T> { private T v; void set(T v){this.v=v;} T get(){return v;} }

    static double sum(Collection<? extends Number> xs){ double s=0; for (Number n: xs) s+=n.doubleValue(); return s; }
    static void addInts(List<? super Integer> xs){ xs.add(1); xs.add(2); }

    static void genericsAndVariance() {
        Box<String> b = new Box<>(); b.set("x");
        double s = sum(List.of(1,2,3));
        List<Number> out = new ArrayList<>(); addInts(out);
        System.out.printf("[Generics] box=%s sum=%.0f out=%s%n", b.get(), s, out);
    }

    // ───────────────────────────── 10) ARCHITECT: VALUE OBJECTS & DEFENSIVE COPIES ─────────────────────────────
    public static final class CustomerId {
        private final String value;
        private CustomerId(String v){ this.value=v; }
        public static CustomerId of(String v){
            Objects.requireNonNull(v);
            if (!v.matches("^[A-Z0-9]{6,}$")) throw new IllegalArgumentException("id format");
            return new CustomerId(v);
        }
        public String value(){ return value; }
        @Override public String toString(){ return value; }
        @Override public boolean equals(Object o){ return o instanceof CustomerId c && value.equals(c.value); }
        @Override public int hashCode(){ return value.hashCode(); }
    }

    public static final class OrderLines {
        private final List<String> lines;
        public OrderLines(List<String> lines){ this.lines = List.copyOf(lines); } // unmodifiable defensive copy
        public List<String> lines(){ return lines; }
    }

    static void architectPatterns() {
        CustomerId id = CustomerId.of("ABC123");
        OrderLines ol = new OrderLines(List.of("a","b"));
        // ol.lines().add("c"); // would throw UnsupportedOperationException — safe API
        System.out.printf("[Architect] id=%s lines=%s%n", id, ol.lines());
    }

    // ───────────────────────────── MAIN ─────────────────────────────
    public static void main(String[] args) {
        primitivesAndLiterals();
        promotionAndCasting();
        wrappersAndAutoboxing();
        bigNumbersAndMoney();
        stringsAndText();
        arraysAndCollections();
        timeTypes();
        optionalsAndNullObject();
        typeSystemPieces();
        genericsAndVariance();
        architectPatterns();
    }
}
