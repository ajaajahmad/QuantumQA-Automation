package com.quantumqa.pages.reports;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.quantumqa.base.BasePage;
import com.quantumqa.pages.components.MainMenuComponent;
import com.quantumqa.pages.components.ViewByComponent;
import com.quantumqa.utils.HtmlReportUtil;
import com.quantumqa.utils.ScreenshotUtils;

public class SmsSummaryPage extends BasePage {

	private final ScreenshotUtils screenshot;
	private final MainMenuComponent menu;
	private final ViewByComponent viewBy;

	private final String[] viewOptions = { "Date & Campaign", "Date", "Campaign" };
	private final By summaryTable = By.className("data-table");

	public SmsSummaryPage(WebDriver driver) {
		super(driver);
		this.screenshot = new ScreenshotUtils();
		this.menu = new MainMenuComponent(driver);
		this.viewBy = new ViewByComponent(driver);
	}

	public void openSmsSummary() {
		menu.navigate("Reports", "SMS", "Summary");
		sleep();
	}

	public void applyAllViewByOptions(String screenshotTitle) throws IOException {

		// Standard View By tabs
		for (String option : viewOptions) {
			viewBy.selectViewByTab(option);

			WebElement table = driver.findElement(summaryTable);

			HtmlReportUtil.createHtmlTable(table, screenshotTitle + " - " + option, "reports/sms-summary",
					option.replaceAll("\\W+", "_"));

			screenshot.getScreenshot(driver, screenshotTitle + "_" + option);
		}

		// More dropdown options
		for (int i = 0; i < 11; i++) {
			boolean selected = viewBy.selectMoreViewByOption(i);
			if (!selected)
				break;

			WebElement table = driver.findElement(summaryTable);

			HtmlReportUtil.createHtmlTable(table, screenshotTitle + " - More Option " + i, "reports/sms-summary",
					"More_Option_" + i);

			screenshot.getScreenshot(driver, screenshotTitle + "_option_" + i);
		}
	}
}