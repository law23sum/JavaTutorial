package com.javatutorial.oop.encapsulation;

/**
 * <h2>Encapsulation</h2>
 * Bundle data + behavior, then control access. Fields are {@code private}; the
 * outside world only touches them through public methods that <i>preserve
 * invariants</i> (here: balance must never go negative).
 */
public class BankAccount {

    private final String owner;
    private double balance;

    public BankAccount(String owner, double openingDeposit) {
        if (openingDeposit < 0) throw new IllegalArgumentException("openingDeposit < 0");
        this.owner = owner;
        this.balance = openingDeposit;
    }

    public String getOwner()   { return owner; }
    public double getBalance() { return balance; }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must be positive");
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0)         throw new IllegalArgumentException("amount must be positive");
        if (amount > balance)    throw new IllegalStateException("insufficient funds");
        balance -= amount;
    }

    public static void main(String[] args) {
        BankAccount acct = new BankAccount("Ada", 100.0);
        acct.deposit(50);
        acct.withdraw(30);
        System.out.println(acct.getOwner() + " balance = " + acct.getBalance());
    }
}
