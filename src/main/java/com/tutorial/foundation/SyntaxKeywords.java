package com.tutorial.foundation;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;
import java.util.Random;

/**
 * Compact, runnable "keywords tour".
 * Each keyword shown once, with commentary.
 *
 * Keywords & concepts covered:
 * - package / import / class
 * - interface, enum, record
 * - extends, implements, super, this
 * - public / protected / private
 * - static, final, abstract
 * - if/else, switch (statement & expression)
 * - for / while / do, break / continue (labeled)
 * - return, throw / throws
 * - try / catch / finally, try-with-resources
 * - synchronized (method + block), volatile, transient
 * - default (in interface + switch), instanceof (pattern)
 * - var (local type inference), assert
 * - @Override, @Deprecated, @FunctionalInterface
 * - strictfp
 */
public class SyntaxKeywords implements Closeable {

    // -------------------------------------------------------------------------
    // Fields & modifiers
    // -------------------------------------------------------------------------

    public static final int VERSION = 2;   // public: visible everywhere
    // static: shared across all instances
    // final: cannot be reassigned
    // Typically used for constants

    private volatile int counter;          // volatile: updates visible across threads
    // Used for shared flags/counters in concurrency

    private transient String cached;       // transient: skipped during serialization
    // Useful when caching expensive but non-essential data

    // static initializer → runs once when the class is loaded
    static {
        if (VERSION < 1) throw new AssertionError("Impossible");
    }

    // instance initializer → runs before constructors for each new object
    {
        cached = "init";
    }

    // -------------------------------------------------------------------------
    // Nested types
    // -------------------------------------------------------------------------

    @FunctionalInterface
    interface Greeter {
        // interface: defines a contract
        String greet(String name);

        // default: provide a usable implementation
        default String hello() { return "Hello"; }

        // static factory method: common in interfaces
        static Greeter ofPrefix(String pfx) { return name -> pfx + name; }
    }

    enum Level { LOW, MEDIUM, HIGH } // enum: finite set of constants, type-safe

    public record User(int id, String name) {
        // record (Java 16+): immutable data carrier; generates ctor/getters/hash/equals/toString
        public User {
            if (id < 0) throw new IllegalArgumentException("id >= 0");
        }
    }

    // abstract: cannot be instantiated directly, defines template
    static abstract class Being {
        protected final String kind;            // protected: visible in subclasses
        protected Being(String kind) { this.kind = kind; }
        abstract String say();                  // abstract method: must be implemented
        @Override public String toString() { return "Being(kind=" + kind + ")"; }
    }

    // extends: concrete subclass
    static final class Person extends Being {
        private final String name;
        Person(String name) { super("person"); this.name = name; }
        @Override String say() { return "I am " + name; }
        @Override public String toString() { return super.toString() + " name=" + name; }
    }

    // static nested class: no outer instance reference
    static class Util {
        static int clamp(int v, int lo, int hi) { return Math.max(lo, Math.min(hi, v)); }
    }

    // inner class: has reference to outer instance (`SyntaxKeywords.this`)
    class CounterBox {
        int get() { return SyntaxKeywords.this.counter; }
    }

    // AutoCloseable: allows try-with-resources
    static final class Tick implements AutoCloseable {
        private boolean open = true;
        @Override public void close() { open = false; }
        boolean open() { return open; }
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public SyntaxKeywords() { this(0); }   // this(): chain constructors
    public SyntaxKeywords(int start) { this.counter = start; }

    // -------------------------------------------------------------------------
    // Control flow
    // -------------------------------------------------------------------------

    public int bump(Level level) {
        // switch expression (Java 14+): returns a value, concise
        int inc = switch (level) {
            case LOW    -> 1;
            case MEDIUM -> 2;
            case HIGH   -> 3;
        };
        return counter += inc; // return: send result back
    }

    public void classicSwitch(int code) {
        // switch statement: older form, supports fall-through
        switch (code) {
            case 200:
                cached = "OK";
                break; // break: exit switch
            case 500:
            case 503:
                cached = "SERVER_ERR";
                break;
            default: // default: runs if no case matches
                cached = "UNKNOWN";
        }
    }

    public void loopsAndFlow() {
        // labeled loop: rare, but handy to break/continue outer loop
        outer:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 1) continue;         // continue: skip iteration
                if (i == 2 && j == 2) break outer; // break with label
                counter += i + j;
            }
        }

        // while: condition checked before each loop
        int n = 0;
        while (n < 2) n++;

        // do-while: condition checked after → runs at least once
        int m = 0;
        do { m++; } while (m < 1);
    }

    // -------------------------------------------------------------------------
    // Exceptions & resources
    // -------------------------------------------------------------------------

    public void risky() throws Exception { // throws: declares checked exception
        try {
            if (new Random().nextBoolean()) {
                throw new Exception("boom"); // throw: raise an exception
            }
        } catch (Exception e) {
            cached = e.getMessage();        // catch: handle the problem
        } finally {
            cached = (cached == null) ? "ok" : cached; // finally: always runs
        }
    }

    public void withResources(String content) throws IOException {
        // try-with-resources: automatically closes resources implementing AutoCloseable
        try (Tick t = new Tick();
             var reader = new BufferedReader(new StringReader(content))) {
            String first = reader.readLine();
            assert t.open() : "resource should be open here"; // assert: dev-only checks, enable with -ea
            cached = Objects.toString(first, "empty");
        }
        // resources are closed here
    }

    // -------------------------------------------------------------------------
    // Concurrency
    // -------------------------------------------------------------------------

    public synchronized int syncBump() { // synchronized method: locks on this
        return ++counter;
    }

    public int syncBlock() {
        // synchronized block: finer-grained locking
        synchronized (this) {
            return ++counter;
        }
    }

    // -------------------------------------------------------------------------
    // Types, inference, patterns
    // -------------------------------------------------------------------------

    public String who(Object o) {
        if (o instanceof User u) { // instanceof pattern matching (Java 16+)
            return "User:" + u.name();
        }
        var s = String.valueOf(o); // var: local type inference (Java 10+)
        return s;
    }

    // strictfp: enforce strict IEEE 754 FP semantics across platforms
    public double sum(double a, double b) { return a + b; }

    // -------------------------------------------------------------------------
    // Miscellaneous
    // -------------------------------------------------------------------------

    @Deprecated(since = "1.0", forRemoval = false) // annotation + deprecation metadata
    public String legacyEcho(String s) { return s; }

    @Override public void close() { /* no-op */ } // implementing Closeable

    // -------------------------------------------------------------------------
    // Demo main
    // -------------------------------------------------------------------------

    public static void main(String[] args) throws Exception {
        var demo = new SyntaxKeywords(10);

        // interface + lambda + default/static method
        Greeter g = Greeter.ofPrefix("Hi, ");
        System.out.println(g.hello() + " → " + g.greet("Ada"));

        // enum + switch expression
        System.out.println("bump LOW   : " + demo.bump(Level.LOW));
        System.out.println("bump HIGH  : " + demo.bump(Level.HIGH));

        demo.loopsAndFlow();
        demo.classicSwitch(200);
        demo.classicSwitch(999);

        // exception handling
        demo.risky();
        demo.withResources("first line\nsecond line");

        // synchronized usage
        System.out.println("syncBump   : " + demo.syncBump());
        System.out.println("syncBlock  : " + demo.syncBlock());

        // pattern matching + var
        System.out.println(demo.who(new User(1, "Turing")));

        // inheritance
        Being p = new Person("Grace");
        System.out.println(p.say());
        System.out.println(p);

        // inner class usage
        SyntaxKeywords.CounterBox box = demo.new CounterBox();
        System.out.println("CounterBox get: " + box.get());

        // floating point strictness
        System.out.println("sum(strictfp): " + demo.sum(0.1, 0.2));

        // deprecated method
        System.out.println("legacyEcho: " + demo.legacyEcho("echo"));

        // assert check (enable with -ea flag)
        assert VERSION >= 2 : "Update VERSION if you change the demo";
    }
}
