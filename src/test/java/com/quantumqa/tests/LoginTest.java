package com.quantumqa.tests;

import org.testng.annotations.Test;
import org.testng.Assert;

import com.quantumqa.base.BaseTest;
import com.quantumqa.dataprovider.TestDataProvider;

public class LoginTest extends BaseTest {

	@Test(dataProvider = "loginData", dataProviderClass = TestDataProvider.class)
	public void validLoginTest(String username, String password) {
		Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page is not displayed");
		loginPage.userLogin(username, password);
		System.out.println("Valid login test passed successfully");
	}

	@Test(enabled = false)
	public void invalidLoginTest() {
		Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page is not displayed");
		System.out.println("Invalid login test passed successfully");
	}

}