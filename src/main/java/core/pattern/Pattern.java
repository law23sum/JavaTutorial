package core.pattern;


enum PatternCategory {
   DESIGN,
}

public class Pattern {
    PatternCategory PatternGenerate;

   public Pattern() {
   }

   void types(PatternCategory Category) {
      this.PatternGenerate = PatternCategory.DESIGN;
   }

   static {
      System.out.print("each pattern's design");
   }

   void createPattern() {     // f
      new Pattern();
   }
}