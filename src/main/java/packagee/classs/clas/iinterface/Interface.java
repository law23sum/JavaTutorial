
package packagee.classs.clas.iinterface;
                                              // cannot have constructors
public interface Interface {                  // contain only constants, method signatures, default methods, static methods, and nested types

final static int DIGIT = 0;              // contain only constants
final static String WORD = "Aye";        // contain only constants

void changeState(int newState);     //method signatures, abstract method is a method without a body

void incrementState();              //method signatures

void decrementState();              //method signatures

void changeBehaviour(char newBehavior); //method signatures
}