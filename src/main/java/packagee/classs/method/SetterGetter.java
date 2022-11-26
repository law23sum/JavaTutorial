package packagee.classs.method;

public class SetterGetter {  //types: getters & setters
    public SetterGetter() {

    }

    public void setValue(int num) {
        this.value = num;
    }

    public int getValue() {
        return this.value;
    }

    private int value;

    public static void main(String[] args) {
        SetterGetter setget = new SetterGetter();
        setget.setValue(100);
        System.out.println("\n\t\t" + setget.getValue());
    }
}