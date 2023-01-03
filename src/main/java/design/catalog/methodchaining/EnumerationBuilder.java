package design.catalog.methodchaining;

import java.util.regex.Pattern;

public class EnumerationBuilder {
   private Pattern pattern;

   public EnumerationBuilder setPattern(Pattern pattern) {
      this.pattern = pattern;
      return this;
   }

   public Enumeration createEnumeration() {
      return new Enumeration(pattern);
   }
}