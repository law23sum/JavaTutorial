// Accessor pattern is a design pattern that provides a way to access the internal state of an object through methods,
// rather than accessing the object's state directly. This pattern can be used to encapsulate
// the internal state of an object and protect it from unintended modifications.

package design.patterns.objectt.creation;

public class Accessor {
    private int x;

    public int getX() {
        return x;
    }

    public void setX(int xValue) {
        this.x = xValue;
    }
}