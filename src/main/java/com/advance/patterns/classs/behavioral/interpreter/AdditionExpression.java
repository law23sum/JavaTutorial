package com.advance.patterns.classs.behavioral.interpreter;


public class AdditionExpression implements Expression {
    private final Expression left;
    private final Expression right;
    
    public AdditionExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
    
    public int interpret() {
        return left.interpret() + right.interpret();
    }}