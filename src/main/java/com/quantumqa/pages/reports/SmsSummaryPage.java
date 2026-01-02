package com.quantumqa.pages.reports;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.quantumqa.base.BasePage;
import com.quantumqa.pages.components.MainMenuComponent;

public class SmsSummaryPage extends BasePage {

	private MainMenuComponent menu;

	public SmsSummaryPage(WebDriver driver) {
		super(driver);
		this.menu = new MainMenuComponent(driver);
	}

	private By header = By.xpath("//div[contains(@class,'mis_table_header')]");

	private By viewOption(String view) {
		return By.xpath(".//li[span[normalize-space()='" + view + "']]");
	}

	public void clickOnSmsSummary() {
		menu.navigate("Reports", "SMS", "Summary");
	}

	public void clickOnViewBy(String view) {
		WebElement headerEl = driver.findElement(header);
		WebElement option = headerEl.findElement(viewOption(view));
		click(option);
	}
}
