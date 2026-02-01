package com.quantumqa.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.quantumqa.base.BasePage;

public class LogoutComponent extends BasePage {

	public LogoutComponent(WebDriver driver) {
		super(driver);
	}

	private By profileIcon = By.cssSelector(".user-name-alias");
	private By logoutButton = By.xpath("//a[normalize-space()='Logout']");

	public void userLogout() {
		click(profileIcon);
		click(logoutButton);
	}
}