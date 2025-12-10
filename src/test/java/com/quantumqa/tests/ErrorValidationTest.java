package com.quantumqa.tests;

import org.testng.annotations.Test;
import com.quantumqa.base.BaseTest;
import com.quantumqa.dataprovider.TestDataProvider;

public class ErrorValidationTest extends BaseTest {

	@Test(dataProvider = "loginTestData", dataProviderClass = TestDataProvider.class)
	public void verifyWithInvalidCredentials(String username, String password) {

		loginPage.enterUsername(username);
		loginPage.enterPassword(password);
		loginPage.clickLoginButton();

		// validate based on invalid creds
		errorValidation.verifyWithInvalidCred();
	}
}