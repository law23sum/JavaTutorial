package core.oop.inheritance;

public class Inheritance {
    public Inheritance(int trait){
        this.traitName = trait;
    }

    protected int traitName;

    public static void main(String[] args){
        Inheritor inheritor = new Inheritor(0);
        inheritor.setTrait('a');
        System.out.println("\n\t\ttrait number " + inheritor.getTrait());
    }
}
