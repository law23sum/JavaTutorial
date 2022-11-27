package packagee.classs.clas.common;

public class AccessModifiers {
    public static void world() {
        accessModifyType = new String[]{"public"};
    }

    static void subClass() {                                                                           // package accessible
        accessModifyType = new String[]{"public", "protected"};
    }

    protected static void packagee() {                                                                                   // class accessible
        accessModifyType = new String[]{"public", "protected", "none"};
    }

    private static void classs() {
        accessModifyType = new String[]{"public", "protected", "none", "private"};
    }

    static String[] accessModifyType = new String[4];
}