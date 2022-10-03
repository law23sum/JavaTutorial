package object.data.structure;

import object.argument.Point;
import object.argument.Rectangle;

public class ArrayObject {
   public static void main(String[] args) {
      Point originOne = new Point(23, 94);                                                                      // point object
      Rectangle[] rect = new Rectangle[2];                                                                            // object array
      rect[0] = new Rectangle(originOne, 100, 200);                                                            // object array index: element 0 & 1
      rect[1] = new Rectangle(50, 100);
   }
}