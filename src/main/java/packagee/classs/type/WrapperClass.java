package packagee.classs.type;

public class WrapperClass {
public void StringClass() {
   String setOfChars = "   abcdefghijklmnop";
   int setSize = setOfChars.length();
   String remainingChars = "qrstuvwxyz   ";
   setOfChars = setOfChars + remainingChars;
   boolean match = setOfChars.equalsIgnoreCase(remainingChars);
   char letter = setOfChars.charAt(5);
   setOfChars = setOfChars.toUpperCase().trim().replace('Z','z');
   System.out.println(setOfChars);
   String[] segments = setOfChars.split("OP");
   System.out.println(segments[0]);
   System.out.println(segments[1]);
   segments[0].startsWith("ABC");
   segments[1].endsWith("z");
   int index = setOfChars.indexOf("E");
   int lastIndex = setOfChars.lastIndexOf("Y");
   System.out.println(index);
   System.out.println(lastIndex);
   remainingChars = setOfChars.substring(index,lastIndex);
   System.out.println(remainingChars);
   remainingChars.contains("OPQ");

}

public static void main(String[] args) {
     WrapperClass temp = new packagee.classs.type.WrapperClass();
      temp.StringClass();
}
}