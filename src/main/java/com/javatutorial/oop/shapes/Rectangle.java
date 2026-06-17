package com.javatutorial.oop.shapes;

public class Rectangle extends Shape {
    private final double width, height;
    public Rectangle(double width, double height) { this.width = width; this.height = height; }

    @Override
    public double area() { return width * height; }

    public static void main(String[] args) {
        Shape[] shapes = { new Circle(2), new Rectangle(3, 4) };
        for (Shape s : shapes) System.out.println(s.describe());
    }
}
