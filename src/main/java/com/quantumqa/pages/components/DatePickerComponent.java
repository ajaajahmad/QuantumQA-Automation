package com.quantumqa.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.quantumqa.base.BasePage;

public class DatePickerComponent extends BasePage {

	public DatePickerComponent(WebDriver driver) {
		super(driver);
	}

	private By rangeDatePickerInput = By.xpath("//input[@placeholder='Select Date Range']");
	private By startDateInput = By.xpath("(//input[@placeholder='Select Date'])[1]");
	private By endDateInput = By.xpath("(//input[@placeholder='Select Date'])[2]");

	public void chooseDateOnSummaryPage(String dateValue) {
		typeAndEnter(rangeDatePickerInput, dateValue);
	}

	public void chooseDateOnAdvancedPage(String startValue, String endValue) {
		typeAndEnter(startDateInput, startValue);
		typeAndEnter(endDateInput, endValue);
	}
}