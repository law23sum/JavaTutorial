// Java Project
// Java Topic (If,Else-If,Else, Switch Statements, For, Enhanced For,  While, Do While Loops, Try Catch Exceptions)
// Java Notes
//          while loop first check the condition then enter the body whereas
//          do-while first enter the body and then check the condition
//
//
//
package packagee.classs.method;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConditionsExceptionsLoopsStatements {
    static String searchMe = "peter piper picked a " + "peck of pickled peppers";
    static String searchMeToo = "Look for a substring in me";
    static String substring = "sub";
    static int[] arrayOfInts = {32, 87, 3, 589, 12, 1076, 2000, 8, 622, 127};
    static int[][] arrayOfIntsMulti = {{32, 87, 3, 589}, {12, 1076, 2000, 8}, {622, 127, 77, 955}};
    static int searchFor = 12;
    static int i;
    static int j;
    static int max = searchMe.length();
    static int numPs = 0;
    static int maxy = searchMeToo.length() - substring.length();
    static boolean foundIt = false;

    static void conditionalStatements() {
        IfElseIfSwitchConditionalsLogic();
        IfElseIfSwitchConditionalsLogic2nd();
        IfElseIfSwitchConditionalsLogic3rd();
        IfElseIfSwitchConditionalsLogic4th();
    }

    static void IfElseIfSwitchConditionalsLogic() {
        for (i = 0; i < arrayOfInts.length; i++) {
            if (arrayOfInts[i] == searchFor) {
                foundIt = true;
                break;
            }
        }
        if (foundIt) {
            System.out.println("Found " + searchFor + " at index " + i);
        } else {
            System.out.println(searchFor + " not in the array");
        }
    }

    static void IfElseIfSwitchConditionalsLogic2nd() {
        search:
        for (i = 0; i < arrayOfIntsMulti.length; i++) {
            for (j = 0; j < arrayOfIntsMulti[i].length; j++) {
                if (arrayOfIntsMulti[i][j] == searchFor) {
                    foundIt = true;
                    break search;
                }
            }
        }
        if (foundIt) {
            System.out.println("Found " + searchFor + " at " + i + ", " + j);
        } else {
            System.out.println(searchFor + " not in the array");
        }
    }

    static void IfElseIfSwitchConditionalsLogic3rd() {
        for (int i = 0; i < max; i++) {
            if (searchMe.charAt(i) != 'p')              // interested only in p's
                continue;                               // process p's
            numPs++;
        }
        System.out.println("Found " + numPs + " p's in the string.");
    }

    static void IfElseIfSwitchConditionalsLogic4th() {
        test:
        for (int i = 0; i <= maxy; i++) {
            int n = substring.length();
            int j = i;
            int k = 0;
            while (n-- != 0) {
                if (searchMeToo.charAt(j++) != substring.charAt(k++)) {
                    continue test;
                }
            }
            foundIt = true;
        }
        System.out.println(foundIt ? "Found it" : "Didn't find it");
    }

    private static void loopFor() {
        System.out.println("Liftoff T minus ");
        for (int count = 10; count >= 0; System.out.println((count == 7) ? "7 ... Ignite" : count + " ..."), count--) ;
        System.out.println("Liftoff! We have liftoff!");
    }

    static void tryCatchExceptions() {
        divideNum(userNum());
        try {
            openFile();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File Non-existent");
        }
    }

    static void divideNum(int num0) {
        try {
            System.out.println("Output: " + 100 / num0);
        } catch (ArithmeticException e) {
            System.out.println("Error: dividing by zero is NA");
        }
    }

    static int userNum() {
        int num = 0;
        Scanner scan = new Scanner(System.in);
        try {
            System.out.print("Enter Value: ");
            num = scan.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Error: not an integer type");
        } catch (Exception e) {
            e.printStackTrace(System.out); // continues execution post handling generic exceptions
        } finally {
            System.out.println("Errors: Check if there were");
        }
        scan.close();
        return num;
    }

    static void openFile() throws FileNotFoundException {
        FileInputStream f = new FileInputStream("src/file/file.txt");
    }

    public static void main(String[] args) {
        conditionalStatements();
        loopFor();
        tryCatchExceptions();
    }
}