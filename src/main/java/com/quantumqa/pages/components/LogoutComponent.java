package com.quantumqa.pages.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.quantumqa.base.BasePage;

public class LogoutComponent extends BasePage {

	public LogoutComponent(WebDriver driver) {
		super(driver);
	}

	@FindBy(css = ".user-name-alias")
	private WebElement profileIcon;

	@FindBy(xpath = "//a[contains(text(),'Logout')]")
	private WebElement logoutButton;

	public void userLogout() {
		click(profileIcon);
		click(logoutButton);
	}

}
