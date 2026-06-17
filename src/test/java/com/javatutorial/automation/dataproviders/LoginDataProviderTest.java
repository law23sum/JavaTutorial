package com.javatutorial.automation.dataproviders;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * <h2>{@code @DataProvider}</h2>
 * Run the same test method many times with different inputs. The provider
 * returns an {@code Object[][]}: one row per invocation, one column per
 * parameter.
 */
public class LoginDataProviderTest {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][]{
                {"admin", "password123"},
                {"user",  "welcome@1"},
                {"qa",    "letMeIn!"}
        };
    }

    @Test(dataProvider = "loginData")
    public void verifyLogin(String username, String password) {
        Assert.assertNotNull(username);
        Assert.assertTrue(password.length() >= 6, "weak password: " + password);
        System.out.println("login user=" + username + " passLen=" + password.length());
    }
}
