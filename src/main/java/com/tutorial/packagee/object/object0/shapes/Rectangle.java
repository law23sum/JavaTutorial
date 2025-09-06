package com.tutorial.packagee.object.object0.shapes;

/*
 * Implements Relatable by comparing AREA.
 * Also supports comparison against Circle so you can mix shapes.
 */
public class Rectangle implements Relatable {
   public Point origin;
   public int width;
   public int height;

   public Rectangle(Point origin, int width, int height) {
      this.origin = origin;
      this.width = width;
      this.height = height;
   }

   public Rectangle(int width, int height) {
      this(new Point(0, 0), width, height);
   }

   public int getArea() {
      return width * height;
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

   // Helper: interpret "size" of the other Relatable
   private static double areaOf(Relatable r) {
      if (r instanceof Rectangle rec) return rec.getArea();
      if (r instanceof Circle cir)    return cir.getArea();
      throw new IllegalArgumentException("Unsupported comparable type: " + r.getClass().getName());
   }
}
