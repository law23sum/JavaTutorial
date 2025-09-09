package com.tutorial.packagee.object.object0.shapes;

/*
 * Implements Relatable by comparing AREA (πr²).
 * Also supports comparison against Rectangle.
 */
public class Circle implements Relatable {
   public Point origin;
   private final int radius;

   public Circle(Point origin, int radius) {
      this.origin = origin;
      this.radius = radius;
   }

   public Circle(int radius) {
      this(new Point(0, 0), radius);
   }

   public int getRadius() {
      return radius;
   }

   public double getArea() {
      return Math.PI * radius * radius;
   }

   public void move(int dx, int dy) {
      origin.x += dx;
      origin.y += dy;
   }

   @Override
   public int isLargerThan(Relatable other) {
      double a1 = this.getArea();
      double a2 = areaOf(other);
      return Double.compare(a1, a2);
   }

   private static double areaOf(Relatable r) {
      if (r instanceof Circle c)    return c.getArea();
      if (r instanceof Rectangle r2) return r2.getArea();
      throw new IllegalArgumentException("Unsupported comparable type: " + r.getClass().getName());
   }
}
