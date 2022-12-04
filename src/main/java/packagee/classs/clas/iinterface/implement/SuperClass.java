package packagee.classs.clas.iinterface.implement;

import packagee.classs.clas.iinterface.Interface;

public class SuperClass implements Interface { // parent class
    public void setState(int newState) {
        this.state = newState;
    }

    public void setBehaviour(char newBehaviour) {
        this.behavior = newBehaviour;
    }

    public void changeState(int newState) {
        this.state = this.state + newState;
    }

    @Override
    public void incrementState() {
        this.state++;
    }

    @Override
    public void decrementState() {
        this.state--;
    }

    public void changeBehaviour(char newBehavior) {
        this.behavior = String.valueOf(this.behavior).charAt(0);
    }

    int state;
    char behavior;

    {
        state = 0;
        behavior = 'a';
    }
}