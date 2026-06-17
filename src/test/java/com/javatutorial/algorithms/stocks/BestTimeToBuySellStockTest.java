package com.javatutorial.algorithms.stocks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BestTimeToBuySellStockTest {
    @Test void profitable()  { assertEquals(5, BestTimeToBuySellStock.maxProfit(new int[]{7, 1, 5, 3, 6, 4})); }
    @Test void monotonic()   { assertEquals(0, BestTimeToBuySellStock.maxProfit(new int[]{7, 6, 4, 3, 1})); }
    @Test void single()      { assertEquals(0, BestTimeToBuySellStock.maxProfit(new int[]{5})); }
}
