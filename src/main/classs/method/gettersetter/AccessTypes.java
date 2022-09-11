package src.main.classs.method.gettersetter;

public class AccessTypes {  //types: getters & setters
    public static void main(String[] args){
        AccessTypes setget = new AccessTypes();
        setget.setValue(100);
        setget.print();
    }

    public void setValue(int num) {
        this.value = num;
    }

    public int getValue() {
        return this.value;
    }

    public void print(){
        System.out.println(this.value);
    }

    private int value;
}
