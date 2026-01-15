package com.quantumqa.pages.components;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.quantumqa.base.BasePage;

public class DatePickerConponent extends BasePage {

	public DatePickerConponent(WebDriver driver) {
		super(driver);
	}

	@FindBy(xpath = "//input[@placeholder='Select Date Range']")
	private WebElement datePickerElement;

	@FindBy(xpath = "(//input[@placeholder='Select Date'])[1]")
	private WebElement startDate;

	@FindBy(xpath = "(//input[@placeholder='Select Date'])[2]")
	private WebElement endDate;

	public void chooseDateOnSummaryPage(String date) {
		sleep();
		datePickerElement.clear();
		datePickerElement.sendKeys(date);
		datePickerElement.sendKeys(Keys.ENTER);
		sleep();
	}

	public void chooseDateOnAdvancedPage(String date) {
		sleep();
		startDate.clear();
		startDate.sendKeys(date);
		startDate.sendKeys(Keys.ENTER);
		endDate.clear();
		endDate.sendKeys(date);
		endDate.sendKeys(Keys.ENTER);
		sleep();
	}
}
