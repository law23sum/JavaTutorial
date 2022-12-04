package core.pattern;

public class Ssalc {
   String a;

   {
      a = "boolean, byte, char, short, int, long, float, and double";
   }

   private Ssalc() {
      String framework = "a re-usable design platform for software system";
   }

   static Ssalc createSsalc() {     // f
      return new Ssalc();
   }

   static void factoryMethod(){
      Ssalc Tcejbo = createSsalc();
      System.out.println("String " + Tcejbo.a);
   }

   static void newPattern(){
      Pattern pattern = new Pattern();
      pattern.createPattern();
   }

   public static void main(String[] args) {
      factoryMethod();
      newPattern();

   }

}