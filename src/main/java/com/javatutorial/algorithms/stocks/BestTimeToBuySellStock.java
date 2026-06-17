package com.javatutorial.algorithms.stocks;

/**
 * <h2>Best Time to Buy and Sell Stock (one transaction)</h2>
 * Track the running minimum price seen so far and the best profit you could
 * have made selling on each day.
 *
 * <pre>
 *   Time : O(n)   Space : O(1)
 * </pre>
 */
public class BestTimeToBuySellStock {

    public static int maxProfit(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int best = 0;
        for (int p : prices) {
            if (p < minPrice) minPrice = p;
            else              best = Math.max(best, p - minPrice);
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(maxProfit(new int[]{7, 1, 5, 3, 6, 4})); // 5
        System.out.println(maxProfit(new int[]{7, 6, 4, 3, 1}));    // 0
    }
}
