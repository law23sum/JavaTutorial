package design.patterns.classs.behavioral.interpreter;

public abstract class NumberExpression implements Expression {
    private final int number;
    
    public NumberExpression(int number) {
        this.number = number;
    }
    
    public int interpret() {
        return number;
    }
}