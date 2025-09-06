package com.tutorial.packagee.object.object0;

import com.tutorial.packagee.object.object0.shapes.Circle;
import com.tutorial.packagee.object.object0.shapes.Point;
import com.tutorial.packagee.object.object0.shapes.Rectangle;
import com.tutorial.packagee.object.object0.shapes.Relatable;

public class ShapesInstance {

    // NOTE: Using Relatable's default methods via any Relatable instance.
    // findLargest/findSmallest return Object (per your interface), so cast back.

    private static void rectangleCreate2Move() {
        Point originOne = new Point(0, 0);
        Rectangle[] rect = new Rectangle[2];
        rect[0] = new Rectangle(originOne, 10, 20);
        rect[1] = new Rectangle(5, 10);

        System.out.println("rectangle0 "
                + "\n\t\tWidth  " + rect[0].width
                + "\n\t\tHeight " + rect[0].height
                + "\n\t\tArea   " + rect[0].getArea());

        rect[1].origin = originOne; // place rect1 at same origin
        System.out.println("rectangle1 origin"
                + "\n\t\tPosition X " + rect[1].origin.x
                + "\n\t\tPosition Y " + rect[1].origin.y);

        rect[1].move(40, 72);
        System.out.println("rectangle1 shifted"
                + "\n\t\tPosition X " + rect[1].origin.x
                + "\n\t\tPosition Y " + rect[1].origin.y);

        // --- Relatable comparisons ---
        Relatable tool = rect[0]; // any Relatable can use the default helpers
        Rectangle bigger = (Rectangle) tool.findLargest(rect[0], rect[1]);
        Rectangle smaller = (Rectangle) tool.findSmallest(rect[0], rect[1]);
        boolean equal = tool.isEqual(rect[0], rect[1]);

        System.out.println("Largest rectangle area: " + bigger.getArea());
        System.out.println("Smallest rectangle area: " + smaller.toString());
        System.out.println("Rectangles equal by area? " + equal);
    }

    private static void circleCreate2Move() {
        Point originOne = new Point(23, 94);
        Circle[] circ = new Circle[2];
        circ[0] = new Circle(originOne, 100); // radius = 100
        circ[1] = new Circle(50);             // radius = 50

        System.out.println("circle0 "
                + "\n\t\tPosition X " + circ[0].origin.x
                + "\n\t\tPosition Y " + circ[0].origin.y
                + "\n\t\tRadius     " + circ[0].getRadius()
                + "\n\t\tArea       " + circ[0].getArea());

        circ[1].origin = originOne;
        System.out.println("circle1 origin"
                + "\n\t\tPosition X " + circ[1].origin.x
                + "\n\t\tPosition Y " + circ[1].origin.y);

        circ[1].move(40, 72);
        System.out.println("circle1 shifted"
                + "\n\t\tPosition X " + circ[1].origin.x
                + "\n\t\tPosition Y " + circ[1].origin.y);

        // --- Relatable comparisons (circles vs rectangles, still works) ---
        Relatable tool = circ[0];
        Relatable largest = (Relatable) tool.findLargest(circ[0], circ[1]);
        System.out.println("Largest circle area: "
                + (largest instanceof Circle c ? c.getArea() : "n/a"));
    }

    public static void main(String[] args) {
        rectangleCreate2Move();
        circleCreate2Move();
    }
}
