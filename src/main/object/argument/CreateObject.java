package src.main.object.argument;

public class CreateObject {
    public static void main(String[] args) {
        rectangleCreate2Move();
        circleCreate2Move();
    }

    private static void rectangleCreate2Move() {
        Point originOne = new Point(23, 94);                                                                      // point object
        Rectangle[] rect = new Rectangle[2];                                                                            // object array
        rect[0] = new Rectangle(originOne, 100, 200);                                                            // object array index: element 0 & 1
        rect[1] = new Rectangle(50, 100);

        System.out.print("Width of rectangle0: " + rect[0].width + "\t");                                               // rect[0]: width, height, and area
        System.out.print("Height of rectangle0: " + rect[0].height + "\t");
        System.out.println("Area of rectangle0: " + rect[0].getArea());

        rect[1].origin = originOne;                                                                                     // rect[1]: set position
        System.out.print("\t\t" + "X Position of rectangle1: " + rect[1].origin.x + "\t");
        System.out.println("\t\t" + "Y Position of rectangle1: " + rect[1].origin.y + "\t");

        rect[1].move(40, 72);                                                                                    // move rect[1] and display its new position
        System.out.print("Shifted X Position of rectangle1: " + rect[1].origin.x + "\t");
        System.out.println("Shifted Y Position of rectangle1: " + rect[1].origin.y + "\t\n");
    }

    private static void circleCreate2Move() {
        Point originOne = new Point(23, 94);
        Circle[] circ = new Circle[2];
        circ[0] = new Circle(originOne, 100, 200);
        circ[1] = new Circle(50, 100);

        System.out.print("X of circle0: " + circ[0].x + "\t");
        System.out.print("Y of circle0: " + circ[0].x + "\t");
        System.out.println("Radius of circle0: " + circ[0].getRadius());

        circ[1].origin = originOne;
        System.out.print("\t\t" + "X of circle1: " + circ[1].origin.x + "\t");
        System.out.println("\t\t" + "Y of circle1: " + circ[1].origin.y + "\t");

        circ[1].move(circ[0], 40, 72);
        System.out.print("Shifted X of circle1: " + circ[1].origin.x + "\t");
        System.out.print("Shifted Y of circle1: " + circ[1].origin.y + "\t");
    }
}
