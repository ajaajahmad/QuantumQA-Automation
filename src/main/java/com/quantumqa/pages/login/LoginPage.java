package com.quantumqa.pages.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.quantumqa.base.BasePage;

public class LoginPage extends BasePage {

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	private final By usernameInputBox = By.xpath("//input[@formcontrolname='username']");
	private final By passwordInputBox = By.xpath("//input[@formcontrolname='password']");
	private final By otpInputBox = By.xpath("//input[@formcontrolname='otp']");

	@FindBy(css = "button.submit")
	private WebElement loginButton;

	@FindBy(xpath = "//button[contains(text(),'Validate')]")
	private WebElement validateOtpButton;

	public boolean isLoginPageDisplayed() {
		return isElementDisplayed(usernameInputBox);
	}

	public boolean isOtpPageDisplayed() {
		try {
			waitForVisible(otpInputBox);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void enterUsername(String username) {
		type(driver.findElement(usernameInputBox), username);
	}

	public void enterPassword(String password) {
		type(driver.findElement(passwordInputBox), password);
	}

	public void clickLoginButton() {
		click(loginButton);
	}

	public void enterOtpAndValidate(String otp) {
		type(driver.findElement(otpInputBox), otp);
		click(validateOtpButton);
	}

	public void userLogin(String username, String password) {
		if (username == null || username.isBlank() || password == null || password.isBlank()) {
			throw new IllegalArgumentException("Username or Password cannot be empty");
		}

		if (isLoginPageDisplayed()) {
			enterUsername(username);
			enterPassword(password);
			clickLoginButton();

			if (isOtpPageDisplayed()) {
				enterOtpAndValidate("666666");
			}
		}
	}
}