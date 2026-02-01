package com.quantumqa.pages.mis;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.quantumqa.base.BasePage;
import com.quantumqa.pages.components.DatePickerComponent;
import com.quantumqa.pages.components.MainMenuComponent;
import com.quantumqa.pages.components.ViewByComponent;
import com.quantumqa.utils.HtmlReportUtil;
import com.quantumqa.utils.ScreenshotUtils;

public class RcsSummaryApiPage extends BasePage {

	private final ScreenshotUtils screenshot;
	private final MainMenuComponent menu;
	private final ViewByComponent viewBy;
	private final DatePickerComponent date;

	private final String[] viewOptions = { "Date" };
	private final By summaryTable = By.className("data-table");

	public RcsSummaryApiPage(WebDriver driver) {
		super(driver);
		this.screenshot = new ScreenshotUtils();
		this.menu = new MainMenuComponent(driver);
		this.viewBy = new ViewByComponent(driver);
		this.date = new DatePickerComponent(driver);
	}

	private By viaApiElement = By.xpath("//span[contains(text(),'Via API')]");

	public void openRcsSummary(String dateValue) {
		menu.navigate("Reports", "RCS", "Summary");
		sleep();
		click(viaApiElement);
		date.chooseDateOnSummaryPage(dateValue);
	}

	public void applyAllViewByOptions(String fileTitle) throws IOException {

		for (String option : viewOptions) {
			viewBy.selectViewByTab(option);

			WebElement table = driver.findElement(summaryTable);

			HtmlReportUtil.createHtmlTable(table, fileTitle + " - " + option, "reports/rcs-summary-api",
					option.replaceAll("\\W+", "_"));

			screenshot.getScreenshot(driver, fileTitle + "_" + option);
		}

		for (int i = 0; i < 11; i++) {
			boolean selected = viewBy.selectMoreViewByOption(i);
			if (!selected)
				break;

			WebElement table = driver.findElement(summaryTable);

			HtmlReportUtil.createHtmlTable(table, fileTitle + " - More Option " + i, "reports/rcs-summary-api",
					"More_Option_" + i);

			screenshot.getScreenshot(driver, fileTitle + "_option_" + i);
		}
	}
}