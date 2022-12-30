package packagee.classs.clas.abstractt;

// cannot have constructors
public interface Interface {                            // contain only constants, method signatures, default methods, static methods, and nested types

    String WORD = "Aye";                                // contain only constants

    void incrementState();                              // method signatures, abstract method is a method without a body


    void decrementState();                              // Polymorphism defines at interface implemented by classes interfacing to it

    void displayCurrentState();

    void change(String type, int state);

    void incrementEvent();

    void decrementEvent();

    void displayCurrentEvent();

}