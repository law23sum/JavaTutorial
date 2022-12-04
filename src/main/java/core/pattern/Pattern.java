package core.pattern;


enum PatternCategory {
   DESIGN,
}

public class Pattern implements PatternType {
PatternCategory PatternGenerate;

public Pattern() {
}

public void types(PatternCategory Category) {
   this.PatternGenerate = PatternCategory.DESIGN;
}

static {
   System.out.print("each pattern's design");
}

public void createPattern() {     // f
   new Pattern();
}
}