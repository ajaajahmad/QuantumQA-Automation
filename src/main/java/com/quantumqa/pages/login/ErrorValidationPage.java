package com.quantumqa.pages.login;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import com.quantumqa.base.BasePage;

public class ErrorValidationPage extends BasePage {

	public ErrorValidationPage(WebDriver driver) {
		super(driver);
	}

	@FindBy(xpath = "//span[contains(text(),'Username is required')]")
	private WebElement errorTextUsername;
	@FindBy(xpath = "//span[contains(text(),'Password is required')]")
	private WebElement errorTextPassword;
	@FindBy(xpath = "//div[contains(@class, 'snack-bar-label')]")
	private WebElement errorMessaageLogin;

	public void verifyWithEmptyUsername() {
		String errorTextU = errorTextUsername.getText();
		Assert.assertEquals(errorTextU, "Username is required");
		System.out.println(errorTextU);
	}

	public void verifyWithEmptyPassword() {
		String errorTextP = errorTextPassword.getText();
		Assert.assertEquals(errorTextP, "Password is required");
		System.out.println(errorTextP);
	}

	public void verifyWithInvalidCred() {
		String errorMessageLogin = errorMessaageLogin.getText();
		Assert.assertEquals(errorMessageLogin, "Combination of username and password is incorrect.");
		System.out.println(errorMessageLogin);
	}

}