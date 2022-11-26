package packagee.object.object0.shapes;

public class Rectangle implements Relatable {
   public int width = 0;
   public int height = 0;
   public Point origin;

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

   public int isLargerThan(Relatable other) {
      Rectangle otherRect
              = (Rectangle)other;
      return Integer.compare(this.getArea(), otherRect.getArea());
   }

    @Override
    public Object findLargest(Object object1, Object object2) {
        return null;
    }
}
