package object.argument;

public class Rectangle {
   protected int width = 0, height = 0;
   protected Point origin;

   public Rectangle() {                                                                                                // 4 constructors
      origin = new Point(0, 0);
   }

   public Rectangle(Point p) {
      origin = p;
   }

   public Rectangle(int w, int h) {
      origin = new Point(0, 0);
      width = w;
      height = h;
   }

   public Rectangle(Point p, int w, int h) {
      origin = p;
      width = w;
      height = h;
   }

   public void move(int x, int y) {                                                                                    // a method for moving the rectangle
      origin.x = x;
      origin.y = y;
   }

   public int getArea() {                                                                                              // a method for computing the area of the rectangle
      return width * height;
   }
}