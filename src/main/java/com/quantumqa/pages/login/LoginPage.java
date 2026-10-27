package com.quantumqa.pages.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.quantumqa.base.BasePage;

public class LoginPage extends BasePage {

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	private By darkThemeButton = By.xpath("//button[text()='Ok']");

	private By usernameInput = By.xpath("//input[@formcontrolname='username']");

	private By passwordInput = By.xpath("//input[@formcontrolname='password']");

	private By loginButton = By.cssSelector("button.submit");

	private By authenticatedUser = By.cssSelector(".user-name-alias");

	public void clickDarkThemeButtonIfVisible() {
		if (isElementDisplayed(darkThemeButton)) {
			click(darkThemeButton);
		}
	}

	public boolean isElementPresent() {
		return isElementPresent(usernameInput);
	}

	public void enterUsername(String username) {
		type(usernameInput, username);
	}

	public void enterPassword(String password) {
		type(passwordInput, password);
	}

	public void clickLoginButton() {
		click(loginButton);
	}

	public void userLogin(String username, String password) {

		if (username == null || username.isBlank() || password == null || password.isBlank()) {
			throw new IllegalArgumentException("Username or Password cannot be empty");
		}

		wait.until(driver -> isElementDisplayed(darkThemeButton)
				|| isElementDisplayed(usernameInput)
				|| isElementDisplayed(authenticatedUser));

		clickDarkThemeButtonIfVisible();
		wait.until(driver -> isElementDisplayed(usernameInput) || isElementDisplayed(authenticatedUser));

		if (isElementDisplayed(usernameInput)) {
			enterUsername(username);
			enterPassword(password);
			clickLoginButton();
		} else {
			log.info("Already logged in, skipping login");
		}
	}
}