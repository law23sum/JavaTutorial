package main.method;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static jdk.jfr.consumer.EventStream.openFile;

public class Exceptions {
    public static void main(String[] args) {
        divideNum(userNum());

        try {
            openFile("src/main/file/file.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Error: File Non-existent");
        }
    }

    private static void divideNum(int num0) {
        try {
            System.out.println("Output: " + 100 / num0);
        } catch (ArithmeticException e) {
            System.out.println("Error: dividing by zero is NA");
        }
    }

    private static int userNum() {
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
    private static void openFile(String name) throws FileNotFoundException {
        FileInputStream f = new FileInputStream(name);
    }
}