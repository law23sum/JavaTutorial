// Java Project
// Java Topic (Inheritance, Encapsulation, Polymorphism)
// Java Notes
//
//      Polymorphism include 2 types
//                                      method overloading & method overriding
//          method overloading is static b/c of compile time
//
//          method overriding is dynamic do to runtime time
//                 ~ Dynamic Method Dispatch occurs when the subClass
//                 contains the same method name as their parent.
//                 rules are the argument list of subclass method must match the parent class method.
//                 argument data types need also match between subclass & parents method
//                 subclass access modifier is less restrictive than its parent
package packagee.classs.clas.abstractt.inherit;


import packagee.classs.clas.implement.RootClass;

public abstract class SuperClass extends RootClass {            //      Inheritor  parent class
    private int newEvent = 0;                                   //      Encapsulation States getters/setters modifiers be public

    public void change(String type, int newEvent) {             //      Polymorphism Implemented: method overloading
        if (type.matches("Event"))
            setNewEvent(getNewEvent()+newEvent);
    }

    public void incrementEvent() {
      setNewEvent(getNewEvent()+1);
    }

    public void decrementEvent() {
        setNewEvent(getNewEvent()-1);
    }

    public void setNewEvent(int newEvent) {                      //       Encapsulation States getters/setters modifiers be public
        this.newEvent = newEvent;
    }

    public int getNewEvent() {                                    //       Encapsulation States getters/setters modifiers be public
        return this.newEvent;
    }

    public void displayCurrentEvent() {
        System.out.println(WORD + ", Event Details! " + getNewEvent());
    }
}