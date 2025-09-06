package com.tutorial.testing.testng.dataprovider;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataProviderTest {

    @DataProvider(name = "numbers")
    public Object[][] numbers() {
        return new Object[][]{
                {2, 2, 4},
                {3, 5, 8},
                {-1, 1, 0}
        };
    }

    @Test(dataProvider = "numbers")
    public void add_isCorrect(int a, int b, int expected) {
        Assert.assertEquals(a + b, expected);
    }
}

