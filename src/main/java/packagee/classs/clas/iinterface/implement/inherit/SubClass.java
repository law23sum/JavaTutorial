package packagee.classs.clas.iinterface.implement.inherit;


import packagee.classs.clas.iinterface.implement.SuperClass;

public class SubClass extends SuperClass { //child class
    public SubClass(){ }

    @Override
    public void decrementState() {
        super.decrementState();
    }

    public static void main(String[] args){
       SuperClass object1 = new SuperClass();
        object1.setState(1);
        object1.incrementState();
        object1.changeState(2);
        object1.setBehaviour('b');
    }

}