package src.main.classs.method.generic;

public class Basic {
public static void main(String[] args){
    double ans = computePayment(5,5,5,5);
    System.out.println(ans);
}

    static double computePayment(double loanAmt, double rate, double futureValue, int numPeriods) {
        double interest = rate / 100.0, partial1 = Math.pow((1 + interest), -numPeriods);
        double denominator = (1 - partial1) / interest, answer = (-loanAmt / denominator) - ((futureValue * partial1) / denominator);
        return answer;
    }
}
