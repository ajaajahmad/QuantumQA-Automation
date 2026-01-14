package com.quantumqa.pages.reports;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.quantumqa.base.BasePage;
import com.quantumqa.pages.components.MainMenuComponent;
import com.quantumqa.pages.components.ViewByComponent;
import com.quantumqa.utils.HtmlReportUtil;
import com.quantumqa.utils.ScreenshotUtils;

public class SmsAdvancedPage extends BasePage {

	private final ScreenshotUtils screenshot;
	private final MainMenuComponent menu;
	private final ViewByComponent viewBy;

	private final String[] viewOptions = { "Date & Campaign", "Date", "Campaign" };
	private final By summaryTable = By.className("data-table");

	public SmsAdvancedPage(WebDriver driver) {
		super(driver);
		this.screenshot = new ScreenshotUtils();
		this.menu = new MainMenuComponent(driver);
		this.viewBy = new ViewByComponent(driver);
	}

	@FindBy(xpath = "(//input[@placeholder='Select Date'])[1]")
	private WebElement startDate;

	@FindBy(xpath = "(//input[@placeholder='Select Date'])[2]")
	private WebElement endDate;

	@FindBy(xpath = "//button[contains(text(),'Search')]")
	private WebElement searchButton;

	public void openSmsSummary() {
		menu.navigate("Reports", "SMS", "Advanced");
		sleep();
		startDate.clear();
		startDate.sendKeys("2026-01-02");
		startDate.sendKeys(Keys.ENTER);

		endDate.clear();
		endDate.sendKeys("2026-01-14");
		endDate.sendKeys(Keys.ENTER);

		searchButton.click();
	}

	public void applyAllViewByOptions(String fileTitle) throws IOException {

		for (String option : viewOptions) {
			viewBy.selectViewByTab(option);

			WebElement table = driver.findElement(summaryTable);

			HtmlReportUtil.createHtmlTable(table, fileTitle + " - " + option, "reports/sms-summary-panel",
					option.replaceAll("\\W+", "_"));

			screenshot.getScreenshot(driver, fileTitle + "_" + option);
		}

		for (int i = 0; i < 11; i++) {
			boolean selected = viewBy.selectMoreViewByOption(i);
			if (!selected)
				break;

			WebElement table = driver.findElement(summaryTable);

			HtmlReportUtil.createHtmlTable(table, fileTitle + " - More Option " + i, "reports/sms-summary-panel",
					"More_Option_" + i);

			screenshot.getScreenshot(driver, fileTitle + "_option_" + i);
		}
	}
}