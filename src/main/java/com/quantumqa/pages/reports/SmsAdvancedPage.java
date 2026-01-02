package com.quantumqa.pages.reports;

import org.openqa.selenium.WebDriver;

import com.quantumqa.base.BasePage;
import com.quantumqa.pages.components.MainMenuComponent;

public class SmsAdvancedPage extends BasePage {

	private MainMenuComponent menu;

	public SmsAdvancedPage(WebDriver driver) {
		super(driver);
		this.menu = new MainMenuComponent(driver);
	}

	public void clickOnSmsSummary() {
		menu.navigate("Reports", "SMS", "Summary");
	}
}