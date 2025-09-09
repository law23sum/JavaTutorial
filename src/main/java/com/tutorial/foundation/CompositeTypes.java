package com.tutorial.foundation;

import java.util.*;
import java.util.function.Function;

/**
 * Composite Types — single-file tour with implementation notes.
 *
 * What "composite" means here:
 *   A composite type is *built from other types* (fields, members, or contained elements).
 *   Primitives (int, double, boolean) are atomic; composite types organize them into structures.
 *
 * Covered:
 * - Arrays                   → contiguous, fixed-size sequences of same-typed elements
 * - Classes                  → fields + methods + nested types; the bread-and-butter composite
 * - Records (Java 16+)       → concise, immutable data carriers (auto ctor/accessors/equals/hash/toString)
 * - Enums                    → finite set of named instances; can carry state/behavior ("enum-as-strategy")
 * - Interfaces               → contracts; can include default/static methods (since Java 8)
 * - Generics                 → parameterized types; type-safe containers & algorithms (PECS, bounds)
 * - Abstract classes         → shared state + "template method" style hooks
 * - Sealed types (Java 17+)  → closed hierarchies; compiler-enforced set of permitted subtypes
 *
 * How to read:
 *   Each section prints something and includes comments with WHY/HOW you’d use it
 *   and common gotchas (mutability, variance, API intent).
 */
public class CompositeTypes {

    // -------------------------------------------------------------------------
    // 1) Arrays: composite of same-type elements
    //    - Fixed length (length is part of the instance, not the type).
    //    - Contiguous memory; O(1) random access by index.
    //    - Covariant in Java (String[] is an Object[]), which is convenient but can throw
    //      ArrayStoreException at runtime if misused. Prefer generics collections for safety.
    // -------------------------------------------------------------------------
    public void arrayDemo() {
        int[] nums = {1, 2, 3, 4};
        String[] names = {"Ada", "Turing", "Grace"};
        System.out.println("Array length = " + nums.length);
        for (String n : names) System.out.println("Name: " + n);

        // NOTE: arrays know their own length; resizing means allocating a new array and copying.
        // For dynamic sizing, use ArrayList<T>.
    }

    // -------------------------------------------------------------------------
    // 2) Class: fields + methods (+ nested types) → the most flexible composite
    //    - Static nested class: does NOT capture an outer instance; behaves like a top-level class.
    //    - Inner class: implicitly holds a reference to the outer instance (CompositeTypes.this).
    //
    //    Why both?
    //      *Use static nested* for helpers that don’t need outer state (clearer, lighter).
    //      *Use inner* when behavior truly depends on the outer instance’s fields/methods.
    // -------------------------------------------------------------------------
    static class Point {
        int x, y;                       // state: the fields that compose this type

        Point(int x, int y) {           // constructor ties inputs to state
            this.x = x;
            this.y = y;
        }

        int dist() {                    // behavior derived from the fields
            return (int) Math.round(Math.hypot(x, y));
        }

        @Override
        public String toString() {      // make logs/devtools readable
            return "(" + x + "," + y + ")";
        }
    }

    // Static nested *generic* composite:
    // - Type parameters (T, U) let one class hold arbitrary paired types.
    // - map(...) demonstrates higher-order functions with java.util.function.Function.
    static class Pair<T, U> {
        final T first;
        final U second;

        Pair(T f, U s) {
            this.first = f;
            this.second = s;
        }

        @Override
        public String toString() {
            return "Pair[" + first + ", " + second + "]";
        }

        static <A, B, R> R map(Pair<A, B> p, Function<? super Pair<A, B>, ? extends R> fn) {
            return fn.apply(p);
        }
    }

    // Inner class (non-static): captures the outer CompositeTypes instance.
    // When you instantiate it, you need an outer: CompositeTypes outer = new CompositeTypes();
    // then Counter c = outer.new Counter();
    class Counter {
        int get() { return count; }     // reads outer field transparently
        void inc() { count++; }         // mutates outer field
    }

    private int count = 0;              // outer state observed/mutated by Counter

    public void classAndNestedDemo() {
        Point p = new Point(3, 4);
        System.out.println("Point " + p + " dist=" + p.dist());

        Pair<String, Integer> px = new Pair<>("age", 42);
        String mapped = Pair.map(px, pair -> pair.first + "=" + pair.second);
        System.out.println("Mapped pair: " + mapped);

        Counter c = new Counter();      // inner class uses implicit CompositeTypes.this
        c.inc(); c.inc();
        System.out.println("Inner Counter.get() = " + c.get());
    }

    // -------------------------------------------------------------------------
    // 3) Record (Java 16+): immutable data composite
    //    - The canonical ctor validates invariants.
    //    - Records generate: private final fields, accessor methods, equals, hashCode, toString.
    //    - Prefer records for identity-by-state DTOs; avoid setters to keep invariants stable.
    // -------------------------------------------------------------------------
    public record Person(int id, String name) {
        public Person {
            if (id < 0) throw new IllegalArgumentException("id >= 0");
            // Validation lives here; fields are implicitly assigned after checks.
        }
    }

    public void recordDemo() {
        Person p = new Person(1, "Ada");
        System.out.println("Record -> " + p); // Person[id=1, name=Ada]
    }

    // -------------------------------------------------------------------------
    // 4) Enum: finite composite set with optional state/behavior
    //    - Each constant is a singleton instance; you can attach fields/methods to them.
    //    - Great for closed strategy sets (e.g., pricing modes, difficulty levels).
    // -------------------------------------------------------------------------
    enum Level {
        LOW(1), MEDIUM(2), HIGH(3);

        private final int severity;

        Level(int s) { this.severity = s; }

        public int severity() { return severity; }

        public boolean worseThan(Level other) { return this.severity < other.severity; }
    }

    public void enumDemo() {
        Level a = Level.MEDIUM, b = Level.HIGH;
        System.out.println(a + " worseThan " + b + "? " + a.worseThan(b));
        // TIP: switch over enums is exhaustive; compiler warns on missing cases (with --enable-preview for pattern switches on newer Java).
    }

    // -------------------------------------------------------------------------
    // 5) Interface: contract composite
    //    - default methods = evolutionary design (add behavior without breaking implementors).
    //    - static methods = factory/utilities co-located with the contract.
    //    - Lambdas can implement single-abstract-method (SAM) interfaces.
    // -------------------------------------------------------------------------
    interface Shape {
        double area();

        default boolean isBig() {                 // reusable derived behavior
            return area() > 100.0;
        }

        static Shape ofCircle(double r) {         // small factory → hides lambda details
            return () -> Math.PI * r * r;         // SAM implemented by a lambda
        }
    }

    public void interfaceDemo() {
        Shape c = Shape.ofCircle(10);
        System.out.println("Circle area=" + c.area() + " isBig? " + c.isBig());
    }

    // -------------------------------------------------------------------------
    // 6) Generics: parameterized composites with bounds & wildcards
    //
    //   Box<T> shows a mutable generic container; typically you expose minimal mutation.
    //   Bounds:
    //     <T extends Number>        → T must be Number or subtype (upper bound).
    //     List<? extends Number>    → producer; you can read as Number (covariant-like).
    //     List<? super Integer>     → consumer; you can write Integers into it (contravariant-like).
    //
    //   PECS mnemonic:
    //     Producer Extends, Consumer Super.
    // -------------------------------------------------------------------------
    static class Box<T> {
        private T value;

        Box(T v) { this.value = v; }

        public T get() { return value; }

        public void set(T v) { this.value = v; }  // consider immutability for thread-safety/API clarity

        @Override
        public String toString() { return "Box(" + value + ")"; }
    }

    // Reads from producer → ? extends Number
    static double sumNumbers(List<? extends Number> nums) {
        double s = 0;
        for (Number n : nums) s += n.doubleValue();
        return s;
    }

    // Writes into consumer → ? super Integer
    static void addIntegers(List<? super Integer> sink, int... vals) {
        for (int v : vals) sink.add(v);
    }

    // Type parameter with bound and Comparable contract
    static <T extends Comparable<T>> T maxOf(Box<T> a, Box<T> b) {
        return (a.get().compareTo(b.get()) >= 0) ? a.get() : b.get();
    }

    public void genericsDemo() {
        List<Integer> ints = new ArrayList<>(List.of(1, 2, 3));
        List<Double> dbls = List.of(2.5, 7.5);
        System.out.println("sum ints=" + sumNumbers(ints));
        System.out.println("sum dbls=" + sumNumbers(dbls));

        List<Number> sink = new ArrayList<>();
        addIntegers(sink, 10, 20, 30); // legal via ? super Integer
        System.out.println("sink after addIntegers = " + sink);

        Box<Integer> bx1 = new Box<>(5);
        Box<Integer> bx2 = new Box<>(9);
        System.out.println("maxOf boxes = " + maxOf(bx1, bx2));
    }

    // -------------------------------------------------------------------------
    // 7) Abstract classes: share state + enforce an algorithm's skeleton ("template method")
    //    - Use when you need partial implementation + fields + non-abstract methods.
    //    - Favor composition over inheritance, but when a true "is-a" with shared machinery exists,
    //      an abstract base can centralize invariants.
    // -------------------------------------------------------------------------
    static abstract class ShapeBase {
        private final String name;

        protected ShapeBase(String name) { this.name = name; }

        public final String name() { return name; }      // final → part of the template contract

        public abstract double area();                   // hook: subclasses must provide

        public double perimeter() { return -1; }         // optional override; -1 means "not defined"

        @Override
        public String toString() { return name + "(area=" + area() + ")"; }
    }

    static final class Rectangle extends ShapeBase {
        private final double w, h;

        Rectangle(double w, double h) {
            super("Rectangle");
            this.w = w; this.h = h;
        }

        @Override public double area() { return w * h; }

        @Override public double perimeter() { return 2 * (w + h); }
    }

    static final class Triangle extends ShapeBase {
        private final double a, b, c;

        Triangle(double a, double b, double c) {
            super("Triangle");
            this.a = a; this.b = b; this.c = c;
        }

        @Override public double area() {
            double s = (a + b + c) / 2.0;
            return Math.sqrt(s * (s - a) * (s - b) * (s - c));
        }

        @Override public double perimeter() { return a + b + c; }
    }

    public void abstractClassDemo() {
        ShapeBase r = new Rectangle(10, 5);
        ShapeBase t = new Triangle(3, 4, 5);
        System.out.println(r + " perimeter=" + r.perimeter());
        System.out.println(t + " perimeter=" + t.perimeter());
    }

    // -------------------------------------------------------------------------
    // 8) Sealed types (Java 17+): close over the hierarchy at the declaration site.
    //    - The sealed type specifies its exact permitted subtypes.
    //    - Subtypes must be: final, sealed, or non-sealed.
    //    - Great for exhaustiveness in pattern matching and for maintaining invariants across a sum type.
    //
    //    Module/package note:
    //      In unnamed/module-less builds, keep sealed interface and its permitted classes in the same package.
    // -------------------------------------------------------------------------
    sealed interface Node permits Leaf, Branch {
        int width();
    }

    static final class Leaf implements Node {
        private final int w;
        Leaf(int w) { this.w = w; }
        public int width() { return w; }
        @Override public String toString() { return "Leaf(" + w + ")"; }
    }

    // non-sealed re-opens the hierarchy beneath it (extensible branch)
    non-sealed static class Branch implements Node {
        private final List<Node> children = new ArrayList<>();

        public Branch add(Node n) {
            children.add(n);
            return this;
        }

        public int width() {
            // Composite pattern: width of a branch is the sum of its children's widths.
            return children.stream().mapToInt(Node::width).sum();
        }

        @Override public String toString() { return "Branch" + children; }
    }

    // Since Branch is non-sealed, more subtypes are allowed (not listed in 'permits').
    static final class WideBranch extends Branch {
        private final int extra;
        WideBranch(int extra) { this.extra = extra; }

        @Override public int width() { return super.width() + extra; }

        @Override public String toString() { return "Wide" + super.toString() + "+" + extra; }
    }

    public void sealedTypesDemo() {
        // Build a tiny tree: Branch( Leaf(2), WideBranch(+3 with Leaf(4), Leaf(1)) )
        Node n = new Branch()
                .add(new Leaf(2))
                .add(new WideBranch(3).add(new Leaf(4)).add(new Leaf(1)));
        System.out.println("Node width = " + n.width());
        System.out.println(n);
        // With pattern matching switch (Java 21+), you could exhaustively handle Leaf/Branch too.
    }

    // -------------------------------------------------------------------------
    // main: run all demos
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        CompositeTypes demo = new CompositeTypes();

        demo.arrayDemo();
        demo.classAndNestedDemo();
        demo.recordDemo();
        demo.enumDemo();
        demo.interfaceDemo();
        demo.genericsDemo();
        demo.abstractClassDemo();
        demo.sealedTypesDemo();
    }
}
