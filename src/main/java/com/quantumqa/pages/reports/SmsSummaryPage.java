package com.quantumqa.pages.reports;

import org.openqa.selenium.WebDriver;

import com.quantumqa.base.BasePage;
import com.quantumqa.pages.components.MainMenuComponent;
import com.quantumqa.pages.components.ViewByComponent;

public class SmsSummaryPage extends BasePage {

	private MainMenuComponent menu;
	private ViewByComponent viewBy;

	public SmsSummaryPage(WebDriver driver) {
		super(driver);
		this.menu = new MainMenuComponent(driver);
		this.viewBy = new ViewByComponent(driver);
	}

	public void openSmsSummary() {
		menu.navigate("Reports", "SMS", "Summary");
		sleep();
	}

	public boolean selectViewByIfPresent(String option) {
		return viewBy.selectViewByIfPresent(option);
	}

}
