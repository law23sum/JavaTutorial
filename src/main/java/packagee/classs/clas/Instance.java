package packagee.classs.clas;

import packagee.classs.clas.implement.SuperClass;

public class Instance {
    public static void main(String[] args){
        SuperClass object1 = new SuperClass();
        object1.setState(1);
        object1.incrementState();
        object1.changeState(2);
        object1.setBehaviour('b');
    }
}
