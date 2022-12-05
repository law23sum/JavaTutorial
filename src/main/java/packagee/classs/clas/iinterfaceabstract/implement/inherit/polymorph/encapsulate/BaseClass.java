package packagee.classs.clas.iinterfaceabstract.implement.inherit.polymorph.encapsulate;

import packagee.classs.clas.iinterfaceabstract.implement.inherit.polymorph.SubClass;

public class BaseClass extends SubClass {
    public static void main(String[] args) {
        BaseClass object1 = new BaseClass();
        object1.setNewState(21);                             // encapsulation implemented
        int tempState;                                       // encapsulation implemented
        tempState = object1.getNewState();
        object1.setNewEvent(15);                             // encapsulation implemented
        object1.incrementEvent();
        int tempEvent;                                       // encapsulation implemented
        tempEvent = object1.getNewEvent();
        object1.change("Event", 1);
        object1.incrementState();
        object1.change( 2);
        object1.decrementEvent();
        object1.decrementState();
        object1.displayCurrentEvent();
        object1.displayCurrentState();
    }
}