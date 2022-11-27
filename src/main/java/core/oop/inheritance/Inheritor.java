package core.oop.inheritance;

public class Inheritor extends Inheritance {
    public Inheritor(int trait) {
        super(trait);
        this.attributeName = trait;
    }

    public void setTrait(char newTrait) {
        this.attributeName = newTrait;
    }

    public int getTrait(){
        return attributeName;
    }

    private int attributeName;
}
