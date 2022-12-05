package packagee.classs.clas.iinterfaceabstract.implement.inherit;

import packagee.classs.clas.iinterfaceabstract.implement.RootClass;

public abstract class SuperClass extends RootClass { // parent class
    private int newEvent = 0;

    public void change(String type, int newEvent) { //  Polymorphism Implemented
        if (type.matches("Event"))
            setNewEvent(getNewEvent()+newEvent);
    }

    public void incrementEvent() {
      setNewEvent(getNewEvent()+1);
    }

    public void decrementEvent() {
        setNewEvent(getNewEvent()-1);
    }

    public void setNewEvent(int newEvent) {
        this.newEvent = newEvent;
    }

    public int getNewEvent() {
        return this.newEvent;
    }

    public void displayCurrentEvent() {
        System.out.println(WORD + ", Event Details! " + getNewEvent());
    }
}