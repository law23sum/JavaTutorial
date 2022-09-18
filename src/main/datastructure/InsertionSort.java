package src.main.datastructure;

import src.main.experiment.Static;

public class InsertionSort {
    public int[] run() {
        while (i <= length) {
            //print();
            int state = valueCalender[i];
            int j = i - 1;
            //DEBUG: System.out.print("hold-" + state + "   \t");
            while (j >= 0 && valueCalender[j] > state) {                                                                // compare each index pair then flip, else next index
                //DEBUG: System.out.print("j" + j + " i" + i + "\t" + valueCalender[j]);
                //DEBUG: System.out.print("-insert-" + valueCalender[j + 1] + "\t\t");
                valueCalender[j + 1] = valueCalender[j];                                                                // original index holds value per next index
                j = j - 1;
            }
            //DEBUG: System.out.print("j" + j + " i" + i + "\t  replace-" + valueCalender[j + 1] + "\n");
            valueCalender[j + 1] = state;
            i++;
        }
        return valueCalender;
    }

    public void print() {
        System.out.println();
        while (k <= length) {
            System.out.print(valueCalender[k - 1] + " ");
            k++;
        }
        k = 1;
    }
    public void print(int[] printSet) {
        System.out.println();
        while (k <= length) {
            System.out.print(printSet[k - 1] + " ");
            k++;
        }
        k = 1;
    }

    private int length = 7000, i = 1, k = 1;
    private int[] valueCalender = Static.randomNumberGeneratorArray(length + 1);
}
