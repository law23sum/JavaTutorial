package com.tutorial.packagee.object.object0.shapes;

import static java.lang.Math.sqrt;

public class Circle {
   public int x = 0;
   protected int y = 0;
   public Point origin;

   public Circle() {
      origin = new Point(0, 0);
   }

   public Circle(Point p) {
      origin = p;
   }

   public Circle(int x, int y) {
      origin = new Point(0, 0);
      this.x = x;
      this.y = y;
   }

   public Circle(Point p, int x, int y) {
      origin = p;
   }

   public void move(Circle circle, int deltaX, int deltaY) {
      circle.setX(circle.getX() + deltaX);                    // code to move origin of circle to x+deltaX, y+deltaY
      circle.setY(circle.getY() + deltaY);
      circle = new Circle(0, 0);                        // code to assign a new reference to circle
   }

   public int getRadius() {
      return (int) sqrt((x * x) + (y * y));
   }

   public int getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int y) {
      this.y = y;
   }
}