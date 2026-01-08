package com.quantumqa.tests;

import java.io.IOException;

import org.testng.annotations.Test;
import com.quantumqa.base.BaseTest;
import com.quantumqa.dataprovider.TestDataProvider;

public class ErrorValidationTest extends BaseTest {

	@Test(dataProvider = "loginData", dataProviderClass = TestDataProvider.class)
	public void verifyWithInvalidCredentials(String username, String password) throws IOException {

		loginPage.enterUsername(username);
		loginPage.enterPassword(password);
		loginPage.clickLoginButton();

		errorValidationPage.verifyWithInvalidCred();
	}
}