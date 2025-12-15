package com.quantumqa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.quantumqa.base.BasePage;

public class LoginPage extends BasePage {

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	@FindBy(xpath = "//input[@formcontrolname='username']")
	private WebElement userName;
	@FindBy(xpath = "//input[@formcontrolname='password']")
	private WebElement passWord;
	@FindBy(css = "button[class='submit']")
	private WebElement loginButton;

	public void enterUsername(String username) {
		waitForElementToAppear(userName);
		userName.sendKeys(username);
	}

	public void enterPassword(String password) {
		waitForElementToAppear(passWord);
		passWord.sendKeys(password);
	}

	public void clickLoginButton() {
		waitForElementToBeClickable(loginButton);
		loginButton.click();
	}

	public void userLogin(String username, String password) {
		if (isLoginPageDisplayed()) {
			enterUsername(username);
			enterPassword(password);
			clickLoginButton();
		} else {
			System.out.println("Already logged in, skipping login");
		}
	}

	public boolean isLoginPageDisplayed() {
		return userName.isDisplayed() && passWord.isDisplayed() && loginButton.isDisplayed();
	}
}