package com.tutorial.packagee.classs.type;

public class WrapperClass {

    // Original method showing off String operations
    private void stringClass() {
        String setOfChars = "   abcdefghijklmnop";
        int setSize = setOfChars.length(); // (unused, but keeps the demo consistent)

        String remainingChars = "qrstuvwxyz   ";
        setOfChars = setOfChars + remainingChars;

        boolean match = setOfChars.equalsIgnoreCase(remainingChars); // (unused, for demo)
        char letter = setOfChars.charAt(5);                          // (unused, for demo)

        // Transformations: uppercase, trim, replace Z with z
        setOfChars = setOfChars.toUpperCase().trim().replace('Z', 'z');
        System.out.println(setOfChars);

        // Splitting strings
        String[] segments = setOfChars.split("OP");
        System.out.println(segments[0]);
        System.out.println(segments[1]);

        // Checks (not printed, just called for demo)
        segments[0].startsWith("ABC");
        segments[1].endsWith("z");

        // Index searches
        int index = setOfChars.indexOf("E");
        int lastIndex = setOfChars.lastIndexOf("Y");
        System.out.println(index);
        System.out.println(lastIndex);

        // Substring extraction
        remainingChars = setOfChars.substring(index, lastIndex);
        System.out.println(remainingChars);

        // Contains check (not printed)
        remainingChars.contains("OPQ");
    }

    // ===== Demo entry point for Runner =====
    public static void runDemo() {
        WrapperClass demo = new WrapperClass();
        demo.stringClass();
    }
}
