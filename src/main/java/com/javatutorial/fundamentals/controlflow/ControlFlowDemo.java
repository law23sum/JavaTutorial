package com.javatutorial.fundamentals.controlflow;

/**
 * <h2>Control Flow</h2>
 * Conditionals, loops, and exception handling — the imperative scaffolding
 * every algorithm is built on.
 */
public class ControlFlowDemo {

    public static void main(String[] args) {
        // if / else if / else
        int score = 82;
        String grade;
        if (score >= 90)      grade = "A";
        else if (score >= 80) grade = "B";
        else if (score >= 70) grade = "C";
        else                  grade = "F";
        System.out.println("grade=" + grade);

        // switch (modern arrow form)
        String day = switch (3) {
            case 1 -> "Mon";
            case 2 -> "Tue";
            case 3 -> "Wed";
            default -> "?";
        };
        System.out.println("day=" + day);

        // for, while, do-while
        for (int i = 0; i < 3; i++) System.out.print("for " + i + " ");
        System.out.println();

        int j = 0;
        while (j < 3) { System.out.print("while " + j + " "); j++; }
        System.out.println();

        // for-each over an array
        int[] nums = {10, 20, 30};
        int sum = 0;
        for (int n : nums) sum += n;
        System.out.println("sum=" + sum);

        // try / catch / finally
        try {
            int bad = Integer.parseInt("not-a-number");
            System.out.println(bad);
        } catch (NumberFormatException e) {
            System.out.println("caught: " + e.getMessage());
        } finally {
            System.out.println("finally always runs");
        }
    }
}
