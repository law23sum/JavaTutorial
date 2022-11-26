package packagee.object.object0;

import packagee.object.object0.shapes.Circle;
import packagee.object.object0.shapes.Point;
import packagee.object.object0.shapes.Rectangle;

public class ShapesInstance {
    private static void rectangleCreate2Move() {
        Point originOne = new Point(0, 0);
        Rectangle[] rect = new Rectangle[2];
        rect[0] = new Rectangle(originOne, 10, 20);
        rect[1] = new Rectangle(5, 10);

        System.out.println("rectangle0 " + "\n\t\tWeight " + rect[0].width);                                               // rect[0]: width, height, and area
        System.out.println("\t\tHeight " + rect[0].height);
        System.out.println("\t\tArea " + rect[0].getArea());
        rect[1].origin = originOne;                                                                                     // rect[1]: set position
        System.out.println("rectangle1 origin\n\t\tPosition X " + rect[1].origin.x);
        System.out.println("\t\tPosition Y " + rect[1].origin.y);
        rect[1].move(40, 72);                                                                                    // move rect[1] and display its new position
        System.out.println("rectangle1 shifted\n\t\tPosition X " + rect[1].origin.x);
        System.out.println("\t\tPosition Y " + rect[1].origin.y);
    }

    private static void circleCreate2Move() {
        Point originOne = new Point(23, 94);
        Circle[] circ = new Circle[2];
        circ[0] = new Circle(originOne, 100, 200);
        circ[1] = new Circle(50, 100);

        System.out.println("circle0 " + "\n\t\tPosition X " + circ[0].x);                                               // rect[0]: width, height, and area
        System.out.println("\t\tPosition Y " + circ[0].x);
        System.out.println("\t\tArea " + circ[0].getRadius());
        circ[1].origin = originOne;
        System.out.println("circle1 origin\n\t\tPosition X " + circ[1].origin.x);
        System.out.println("\t\tPosition Y " + circ[1].origin.y);
        circ[1].move(circ[0], 40, 72);
        System.out.println("circle1 shifted\n\t\tPosition X " + circ[1].origin.x);
        System.out.println("\t\tPosition Y " + circ[1].origin.y);
    }

   public static void main(String[] args) {
      rectangleCreate2Move();
      circleCreate2Move();
   }
}