package com.quantumqa.pages.reports;

import org.openqa.selenium.WebDriver;

import com.quantumqa.base.BasePage;
import com.quantumqa.pages.components.MainMenuComponent;
import com.quantumqa.pages.components.ViewByComponent;

public class SmsSummaryPage extends BasePage {

	private final MainMenuComponent menu;
	private final ViewByComponent viewBy;

	private final String[] viewOptions = { "Date & Campaign", "Date", "Campaign" };

	public SmsSummaryPage(WebDriver driver) {
		super(driver);
		this.menu = new MainMenuComponent(driver);
		this.viewBy = new ViewByComponent(driver);
	}

	public void openSmsSummary() {
		menu.navigate("Reports", "SMS", "Summary");
		sleep();
	}

	public void applyAllViewByOptions() {

		for (String option : viewOptions) {
			viewBy.selectViewByTab(option);
		}

		for (int i = 0; i < 11; i++) {
			boolean selected = viewBy.selectMoreViewByOption(i);
			if (!selected) {
				break;
			}
		}
	}
}