package com.advance.patterns.objectt.creation;

public class Coupling {
   public Coupling() {
      Subject s = new Subject();
      s.t.understand();
   }

   static class Subject {
      Topic t = new Topic();
      public void startReading() {
         t.understand();
      }
   }

   static class Topic {
      public void understand() {
         System.out.println("Tight coupling concept");
      }
   }
}
 /* Examples 2
            class Volume
            {
                 public static void main(String args[])
                 {
                     Box b = new Box(5,5,5);
                     System.out.println(b.volume);
                 }
            }
            class Box
            {
                 public int volume;
                 Box(int length, int width, int height)
                 {
                     this.volume = length * width * height;
                 }
            }
   *./
    */