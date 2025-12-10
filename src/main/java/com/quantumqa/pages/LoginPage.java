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
		userName.sendKeys(username);
	}

	public void enterPassword(String password) {
		passWord.sendKeys(password);
	}

	public void clickLoginButton() {
		loginButton.click();
	}

	public void userLogin(String username, String password) {
		enterUsername(username);
		enterPassword(password);
		clickLoginButton();
	}

	public boolean isLoginPageDisplayed() {
		return userName.isDisplayed() && passWord.isDisplayed() && loginButton.isDisplayed();
	}
}
