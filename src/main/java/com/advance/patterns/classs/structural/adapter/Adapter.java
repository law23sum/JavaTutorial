// adapter pattern is a structural design pattern that allows two incompatible interfaces to work together by wrapping an object of one interface with an adapter object that belongs to the other interface.


package com.advance.patterns.classs.structural.adapter;

public class Adapter {
    private final Adaptee adaptee;
    
    public Adapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }
    
    public void request() {
        adaptee.specificRequest();
    }
}