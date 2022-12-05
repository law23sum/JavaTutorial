package packagee.classs.clas.iinterfaceabstract.implement.inherit.polymorph;


import packagee.classs.clas.iinterfaceabstract.implement.inherit.SuperClass;

public abstract class SubClass extends SuperClass {          //child class
    private int newState = 0;

    public void change(int newState) {     //  Polymorphism Implemented
        setNewState(getNewState()+newState);
    }

    /**
     *
     */
    public void incrementState() {
        setNewState(getNewState()+1);
    }

    /**
     *
     */
    public void decrementState() {
        setNewState(getNewState()-1);
    }

    public void setNewState(int newState) {
        this.newState = newState;
    }

    public int getNewState() {
        return newState;
    }

    public void displayCurrentState() {
        System.out.println(WORD + ", Event Details! " + getNewState());
    }
}