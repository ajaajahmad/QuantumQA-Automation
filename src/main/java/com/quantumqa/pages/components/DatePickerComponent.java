package com.quantumqa.pages.components;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.quantumqa.base.BasePage;

public class DatePickerComponent extends BasePage {

	public DatePickerComponent(WebDriver driver) {
		super(driver);
	}

	@FindBy(xpath = "//input[@placeholder='Select Date Range']")
	private WebElement rangeDatePickerInput;

	@FindBy(xpath = "(//input[@placeholder='Select Date'])[1]")
	private WebElement startDateInput;

	@FindBy(xpath = "(//input[@placeholder='Select Date'])[2]")
	private WebElement endDateInput;

	public void chooseDateOnSummaryPage(String dateValue) {
		sleep();
		rangeDatePickerInput.clear();
		rangeDatePickerInput.sendKeys(dateValue);
		rangeDatePickerInput.sendKeys(Keys.ENTER);
		sleep();
	}

	public void chooseDateOnAdvancedPage(String startValue, String endValue) {
		sleep();

		startDateInput.clear();
		startDateInput.sendKeys(startValue);
		startDateInput.sendKeys(Keys.ENTER);

		endDateInput.clear();
		endDateInput.sendKeys(endValue);
		endDateInput.sendKeys(Keys.ENTER);

		sleep();
	}
}