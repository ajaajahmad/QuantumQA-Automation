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

	@FindBy(xpath = "//input[@formcontrolname='username']")
	private WebElement usernameInputBox;
	@FindBy(xpath = "//input[@formcontrolname='password']")
	private WebElement passwordInputBox;
	@FindBy(css = "button.submit")
	private WebElement loginButton;
	
	By usernameLocator = By.xpath("//input[@formcontrolname='username']");

	public boolean isLoginPageDisplayed() {
		return isElementDisplayed(usernameLocator);
	}

	public void enterUsername(String username) {
		type(usernameInputBox, username);
	}

	public void enterPassword(String password) {
		type(passwordInputBox, password);
	}

	public void clickLoginButton() {
		click(loginButton);
	}

	public void userLogin(String username, String password) {

		if (username == null || username.isBlank() || password == null || password.isBlank()) {
			throw new IllegalArgumentException("Username or Password cannot be empty");
		}

		if (isLoginPageDisplayed()) {
			enterUsername(username);
			enterPassword(password);
			clickLoginButton();
		} else {
			System.out.println("Already logged in, skipping login");
		}
	}
}