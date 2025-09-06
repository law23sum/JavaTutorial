package com.tutorial.foundation;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;
import java.util.Random;

/**
 * Compact, runnable "keywords tour".
 * Shows: package, import, class, interface, enum, record, extends, implements,
 * public/protected/private, static, final, abstract, new, this, super,
 * if/else, switch (statement & expression), for/while/do, break/continue (labeled),
 * return, try/catch/finally, try-with-resources, throw/throws,
 * synchronized (method + block), volatile, transient, default (interface & switch),
 * instanceof (pattern), var (local type inference), assert, strictfp, @Override, @Deprecated, @FunctionalInterface.
 */
public class SyntaxKeywords implements Closeable {

    // --- fields & modifiers
    public static final int VERSION = 2;       // public static final constant
    private volatile int counter;              // volatile → visibility across threads
    private transient String cached;           // transient → skipped by serialization (demo-only)

    // static initializer (runs once per class load)
    static {
        // Simple side-effect to show order of execution
        if (VERSION < 1) throw new AssertionError("Impossible");
    }

    // instance initializer (runs before constructors)
    {
        cached = "init";
    }

    // --- nested types -----------------------------

    @FunctionalInterface
    interface Greeter {                          // interface + default + static factory
        String greet(String name);
        default String hello() { return "Hello"; }
        static Greeter ofPrefix(String pfx) { return name -> pfx + name; }
    }

    enum Level { LOW, MEDIUM, HIGH }             // enum keyword

    public record User(int id, String name) {    // record (Java 16+)
        public User {
            if (id < 0) throw new IllegalArgumentException("id >= 0"); // compact ctor + throw
        }
    }

    // Abstract base type + inheritance + protected + super/this usage
    static abstract class Being {
        protected final String kind;            // protected
        protected Being(String kind) { this.kind = kind; }
        abstract String say();                  // abstract
        @Override public String toString() { return "Being(kind=" + kind + ")"; }
    }

    // Concrete subclass using extends + super
    static final class Person extends Being {
        private final String name;
        Person(String name) { super("person"); this.name = name; }
        @Override String say() { return "I am " + name; }
        @Override public String toString() { return super.toString() + " name=" + name; }
    }

    // Static nested vs. inner class
    static class Util {                          // static nested (no 'this' of outer)
        static int clamp(int v, int lo, int hi) { return Math.max(lo, Math.min(hi, v)); }
    }
    class CounterBox {                           // inner class (has outer 'this')
        int get() { return SyntaxKeywords.this.counter; } // uses outer instance
    }

    // AutoCloseable/Closeable to demo try-with-resources
    static final class Tick implements AutoCloseable {
        private boolean open = true;
        @Override public void close() { open = false; }
        boolean open() { return open; }
    }

    // --- constructors -----------------------------

    public SyntaxKeywords() { this(0); }         // this() chaining
    public SyntaxKeywords(int start) { this.counter = start; }

    // --- control flow & expressions ---------------

    public int bump(Level level) {
        // switch expression (arrow form). All cases covered → no 'default' needed here.
        int inc = switch (level) {
            case LOW    -> 1;
            case MEDIUM -> 2;
            case HIGH   -> 3;
        };
        return counter += inc;                    // return
    }

    public void classicSwitch(int code) {
        // switch statement (with default)
        switch (code) {
            case 200:
                cached = "OK";
                break;                            // break
            case 500:
            case 503:
                cached = "SERVER_ERR";
                break;
            default:
                cached = "UNKNOWN";
        }
    }

    public void loopsAndFlow() {
        // labeled loop + continue/break
        outer:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 1) continue;            // continue inner
                if (i == 2 && j == 2) break outer; // labeled break
                counter += i + j;
            }
        }

        // while
        int n = 0;
        while (n < 2) n++;

        // do-while (executes at least once)
        int m = 0;
        do { m++; } while (m < 1);
    }

    // --- exceptions & resources -------------------

    public void risky() throws Exception {        // throws in signature
        try {
            if (new Random().nextBoolean()) {
                throw new Exception("boom");      // throw a checked exception
            }
        } catch (Exception e) {
            cached = e.getMessage();              // handle
        } finally {                               // always runs
            cached = (cached == null) ? "ok" : cached;
        }
    }

    public void withResources(String content) throws IOException {
        // try-with-resources: resources auto-closed
        try (Tick t = new Tick();
             var reader = new BufferedReader(new StringReader(content))) {
            String first = reader.readLine();
            assert t.open() : "resource should be open here"; // assert keyword (enable with -ea)
            cached = Objects.toString(first, "empty");
        }
        // t is now closed
    }

    // --- concurrency keywords ---------------------

    public synchronized int syncBump() {          // synchronized method
        return ++counter;
    }

    public int syncBlock() {
        // synchronized block (fine-grained)
        synchronized (this) {
            return ++counter;
        }
    }

    // --- types, patterns, inference ---------------

    public String who(Object o) {
        if (o instanceof User u) {                // pattern matching for instanceof
            return "User:" + u.name();
        }
        var s = String.valueOf(o);                // 'var' local inference
        return s;
    }

    // Demonstrate strictfp on a method (floating-point strictness)
    public strictfp double sum(double a, double b) { return a + b; }

    // --- small utility demos ----------------------

    @Deprecated(since = "1.0", forRemoval = false) // annotation + @Deprecated
    public String legacyEcho(String s) { return s; }

    // --- Closeable implementation -----------------

    @Override public void close() { /* no-op; demo only */ }

    // --- top-level demo ---------------------------

    public static void main(String[] args) throws Exception {
        var demo = new SyntaxKeywords(10);

        // interface + default/static + lambda
        Greeter g = Greeter.ofPrefix("Hi, ");
        System.out.println(g.hello() + " → " + g.greet("Ada"));

        // enum + switch expression
        System.out.println("bump LOW   : " + demo.bump(Level.LOW));
        System.out.println("bump HIGH  : " + demo.bump(Level.HIGH));

        // loops & control flow
        demo.loopsAndFlow();

        // classic switch + defaults
        demo.classicSwitch(200);
        demo.classicSwitch(999);

        // try/catch/throws/throw/finally + try-with-resources + assert
        demo.risky();
        demo.withResources("first line\nsecond line");

        // synchronized (method + block)
        System.out.println("syncBump   : " + demo.syncBump());
        System.out.println("syncBlock  : " + demo.syncBlock());

        // instanceof pattern + var
        System.out.println(demo.who(new User(1, "Turing")));

        // inheritance & inner vs static-nested
        Being p = new Person("Grace");
        System.out.println(p.say());
        System.out.println(p);

        SyntaxKeywords.CounterBox box = demo.new CounterBox();
        System.out.println("CounterBox get: " + box.get());

        // strictfp
        System.out.println("sum(strictfp): " + demo.sum(0.1, 0.2));

        // deprecated method (still callable)
        System.out.println("legacyEcho: " + demo.legacyEcho("echo"));

        // assert: enable with JVM flag -ea to activate
        assert VERSION >= 2 : "Update VERSION if you change the demo";
    }
}
