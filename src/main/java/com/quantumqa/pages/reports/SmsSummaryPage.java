package com.quantumqa.pages.reports;

import org.openqa.selenium.WebDriver;

import com.quantumqa.base.BasePage;
import com.quantumqa.pages.components.MainMenuComponent;

public class SmsSummaryPage extends BasePage {

	private MainMenuComponent menu;

	public SmsSummaryPage(WebDriver driver) {
		super(driver);
		this.menu = new MainMenuComponent(driver);
	}
	
	public void clickOnSmsSummary() {
		menu.navigate("Reports", "SMS", "Summary");
	}
}