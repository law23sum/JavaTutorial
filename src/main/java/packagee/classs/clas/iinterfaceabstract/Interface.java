
package packagee.classs.clas.iinterfaceabstract;

// cannot have constructors
public interface Interface {                  // contain only constants, method signatures, default methods, static methods, and nested types

    final static String WORD = "Aye";        // contain only constants

    void incrementState();              //method signatures

    void decrementState();              //method signatures

    void displayCurrentState();

    void change(String type, int state);     //method signatures, abstract method is a method without a body

    void incrementEvent();              //method signatures

    void decrementEvent();              //method signatures

    void displayCurrentEvent();

}