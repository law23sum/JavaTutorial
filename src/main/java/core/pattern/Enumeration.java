package core.pattern;

import java.awt.*;

public class Enumeration {
    Patterns Pattern;

   public Enumeration(java.util.regex.Pattern pattern) {
   }

   public void options() {
   }

   public void select() {
      select(null);
   }

   public void select(Color Color) {

   }

public static void main(String[]args){
      Enumeration enumm = new EnumerationBuilder().createEnumeration();
      enumm.options();
        }
        }