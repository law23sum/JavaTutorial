package com.tutorial.oop;

import java.util.Objects;

/**
 * Java Classes & Objects — compact, runnable tour with practical notes.
 *
 * What this file shows
 *  1) Defining classes: fields, constructors (overload/chain), methods, access levels
 *  2) Encapsulation: private state, validated setters, defensive copies
 *  3) Static vs instance: constants, counters, factories, init order
 *  4) Immutability pattern: value objects that never change (thread-safe by construction)
 *  5) Equality contracts: equals/hashCode/toString and identity vs. logical equality
 *  6) Inheritance & overriding: super/this, final methods, when to prefer composition
 *  7) Builder pattern: readable construction for multi-field objects
 *  8) Method overloading vs overriding; fluent APIs (return this)
 *
 * Build & run (JDK 17+ recommended):
 *   javac com/tutorial/foundation/ClassesObjectsTour.java && java com.tutorial.foundation.ClassesObjectsTour
 */
public class ClassesObjects {

    // =========================================================================
    // 1) SIMPLE VALUE: Address (mutable, but encapsulated)
    //    - Shows private fields + getters/setters + validation
    //    - copy() illustrates defensive copying to avoid leaking internals
    // =========================================================================
    public static class Address {
        private String line1;
        private String city;
        private String country;

        // Instance init block → runs before every constructor
        { this.country = "USA"; } // default; can be overridden by ctor

        public Address(String line1, String city, String country) {
            setLine1(line1);
            setCity(city);
            if (country != null && !country.isBlank()) this.country = country;
        }

        // Copy constructor
        public Address(Address other) {
            this(other.line1, other.city, other.country);
        }

        public String getLine1() { return line1; }
        public void setLine1(String line1) {
            if (line1 == null || line1.isBlank()) throw new IllegalArgumentException("line1 required");
            this.line1 = line1;
        }

        public String getCity() { return city; }
        public void setCity(String city) {
            if (city == null || city.isBlank()) throw new IllegalArgumentException("city required");
            this.city = city;
        }

        public String getCountry() { return country; }
        public void setCountry(String country) {
            if (country == null || country.isBlank()) throw new IllegalArgumentException("country required");
            this.country = country;
        }

        // Defensive copy to hand out a safe duplicate
        public Address copy() { return new Address(this); }

        @Override public String toString() {
            return line1 + ", " + city + ", " + country;
        }
    }

    // =========================================================================
    // 2) PERSON: encapsulation, ctor chaining, static members, equals/hashCode
    //    - Demonstrates identity semantics (id) vs representation (name/address)
    //    - Static init block: runs once when class loads
    // =========================================================================
    public static class Person {
        public static final int MAX_NAME_LEN = 100; // constant (shared)
        private static int population;              // static counter (shared)
        static { population = 0; }                  // static init runs once

        private final int id;       // final → assigned once in ctor
        private String firstName;
        private String lastName;
        private Address address;    // mutable subobject; be careful when exposing

        // Telescoping constructors → chain to single validating primary ctor
        public Person(int id, String firstName, String lastName, Address address) {
            if (id < 0) throw new IllegalArgumentException("id >= 0");
            setFirstName(firstName);
            setLastName(lastName);
            this.id = id;
            // store a defensive copy to protect internal state from external mutation
            this.address = address == null ? null : address.copy();
            population++;
        }

        public Person(int id, String first, String last) {
            this(id, first, last, null);
        }

        // Static factory: gives a name (document intent more clearly than overloading)
        public static Person ofName(int id, String fullName) {
            String[] parts = fullName.trim().split("\\s+", 2);
            String fn = parts[0];
            String ln = (parts.length > 1) ? parts[1] : "";
            return new Person(id, fn, ln, null);
        }

        public static int population() { return population; }

        // Getters/setters (validated). Setters return 'this' for fluent API.
        public int getId() { return id; }

        public String getFirstName() { return firstName; }
        public Person setFirstName(String firstName) {
            requireName(firstName);
            this.firstName = firstName;
            return this;
        }

        public String getLastName() { return lastName; }
        public Person setLastName(String lastName) {
            requireName(lastName);
            this.lastName = lastName;
            return this;
        }

        public Address getAddress() {
            // Return a copy so callers can’t mutate our internal Address accidentally
            return address == null ? null : address.copy();
        }
        public Person setAddress(Address addr) {
            this.address = (addr == null) ? null : addr.copy();
            return this;
        }

        private static void requireName(String s) {
            if (s == null || s.isBlank()) throw new IllegalArgumentException("name required");
            if (s.length() > MAX_NAME_LEN) throw new IllegalArgumentException("name too long");
        }

        // Equality: choose consistent semantics. Here: identity-by-id.
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person p)) return false;
            return id == p.id;
        }
        @Override public int hashCode() { return Integer.hashCode(id); }

        @Override public String toString() {
            return "Person#" + id + "{" + firstName + " " + lastName + (address != null ? ", " + address : "") + "}";
        }
    }

    // =========================================================================
    // 3) IMMUTABLE VALUE: Money (value object)
    //    - All fields final, no setters, operations return new instances
    //    - Thread-safe & easier to reason about; ideal for keys in maps/sets
    // =========================================================================
    public static final class Money {
        private final long cents;     // store in smallest unit to avoid FP error
        private final String currency;

        public Money(long cents, String currency) {
            if (currency == null || currency.isBlank()) throw new IllegalArgumentException("currency required");
            this.cents = cents;
            this.currency = currency;
        }

        public long cents() { return cents; }
        public String currency() { return currency; }

        public Money plus(Money other) {
            requireSameCurrency(other);
            return new Money(this.cents + other.cents, currency);
        }

        public Money minus(Money other) {
            requireSameCurrency(other);
            return new Money(this.cents - other.cents, currency);
        }

        public Money times(int k) { return new Money(this.cents * k, currency); }

        private void requireSameCurrency(Money other) {
            if (!this.currency.equals(other.currency)) {
                throw new IllegalArgumentException("currency mismatch: " + this.currency + " vs " + other.currency);
            }
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Money m)) return false;
            return cents == m.cents && currency.equals(m.currency);
        }

        @Override public int hashCode() { return Objects.hash(cents, currency); }

        @Override public String toString() {
            boolean neg = cents < 0;
            long abs = Math.abs(cents);
            return (neg ? "-" : "") + currency + (abs / 100) + "." + String.format("%02d", abs % 100);
        }
    }

    // =========================================================================
    // 4) COMPOSITION OVER INHERITANCE: BankAccount uses Person + Money internally
    //    - Fluent API (return this) for chaining
    //    - Business rules guarded in methods, not public fields
    // =========================================================================
    public static class BankAccount {
        private final String number;
        private final Person owner;
        private Money balance;

        // Example of Builder for readability with many params
        public static class Builder {
            private String number;
            private Person owner;
            private Money openingBalance = new Money(0, "USD");

            public Builder number(String n) { this.number = n; return this; }
            public Builder owner(Person p) { this.owner = p; return this; }
            public Builder opening(Money m) { this.openingBalance = m; return this; }

            public BankAccount build() {
                if (number == null || number.isBlank()) throw new IllegalArgumentException("number required");
                if (owner == null) throw new IllegalArgumentException("owner required");
                return new BankAccount(number, owner, openingBalance);
            }
        }

        // Private primary ctor forces use of Builder or factories
        private BankAccount(String number, Person owner, Money opening) {
            this.number = number;
            this.owner = owner;
            this.balance = opening;
        }

        public static Builder builder() { return new Builder(); }

        public String number() { return number; }
        public Person owner() { return owner; }
        public Money balance() { return balance; } // returning immutable value is safe

        public BankAccount deposit(Money m) {
            if (!m.currency().equals(balance.currency())) throw new IllegalArgumentException("currency mismatch");
            balance = balance.plus(m);
            return this;
        }

        public boolean withdraw(Money m) {
            if (!m.currency().equals(balance.currency())) throw new IllegalArgumentException("currency mismatch");
            Money tmp = balance.minus(m);
            if (tmp.cents() < 0) return false;
            balance = tmp;
            return true;
        }

        @Override public String toString() {
            return "Acct{" + number + ", owner=" + owner + ", balance=" + balance + "}";
        }
    }

    // =========================================================================
    // 5) INHERITANCE & OVERRIDING: Student extends Person
    //    - Use 'final' to lock methods you don’t want overridden
    //    - Prefer composition for code reuse; inherit only for true "is-a"
    // =========================================================================
    public static class Student extends Person {
        private String major;

        public Student(int id, String first, String last, String major) {
            super(id, first, last);
            this.major = major;
        }

        public String getMajor() { return major; }
        public void setMajor(String major) { this.major = major; }

        @Override public String toString() {
            return "Student{" + getId() + ", " + getFirstName() + " " + getLastName() + ", major=" + major + "}";
        }
    }

    // =========================================================================
    // 6) OVERLOADING vs OVERRIDING — same name, different parameters vs. redefining behavior
    // =========================================================================
    public static class Greeter {
        public String greet() { return "Hello"; }                 // base
        public String greet(String name) { return "Hello, " + name; } // overload
    }
    public static class PoliteGreeter extends Greeter {
        @Override public String greet() { return "Good day"; }    // override (new behavior)
        // greet(String) inherited unchanged
    }

    // =========================================================================
    // DEMOS
    // =========================================================================
    private static void personDemo() {
        System.out.println("--- Person demo (encapsulation, factories, static)");
        Person p = Person.ofName(1, "Ada Lovelace")
                .setAddress(new Address("42 Logic Ln", "London", "UK"));
        System.out.println(p);
        System.out.println("Population now: " + Person.population());

        // Identity vs equality (same id → equal)
        Person p2 = new Person(1, "Ada", "L.", null);
        System.out.println("p.equals(p2)? " + p.equals(p2) + "  hash: " + p.hashCode() + " / " + p2.hashCode());

        // Defensive copies: mutating external address does NOT change person's address
        Address ext = new Address("1 Hack Way", "Cambridge", "UK");
        p.setAddress(ext);
        ext.setCity("OXFORD"); // mutate caller-held reference
        System.out.println("Person address unaffected: " + p.getAddress());
    }

    private static void moneyAndAccountDemo() {
        System.out.println("\n--- Money & BankAccount (immutability, builder, fluent)");
        Money m10 = new Money(1000, "USD");
        Money m25 = new Money(2500, "USD");

        Person owner = new Person(2, "Alan", "Turing");
        BankAccount acct = BankAccount.builder()
                .number("ACC-001")
                .owner(owner)
                .opening(m10)
                .build();

        System.out.println(acct);
        acct.deposit(m25).withdraw(new Money(500, "USD"));
        System.out.println("After ops: " + acct);

        // Immutability: m10 unchanged after use
        System.out.println("m10 still " + m10);
    }

    private static void inheritanceDemo() {
        System.out.println("\n--- Inheritance & overriding");
        Student s = new Student(3, "Grace", "Hopper", "CS");
        System.out.println(s);

        Greeter g1 = new Greeter();
        Greeter g2 = new PoliteGreeter();
        System.out.println("Greeter.greet() = " + g1.greet());
        System.out.println("PoliteGreeter.greet() = " + g2.greet());
        System.out.println("PoliteGreeter.greet(name) = " + g2.greet("Grace"));
    }

    // =========================================================================
    // main — run all demos
    // =========================================================================
    public static void main(String[] args) {
        personDemo();
        moneyAndAccountDemo();
        inheritanceDemo();
    }
}
