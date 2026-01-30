package com.quantumqa.base;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.quantumqa.utils.LogUtils;

public class BasePage {

	protected WebDriver driver;
	protected WebDriverWait wait;
	protected static LogUtils log;

	private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);

	public BasePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
		BasePage.log = new LogUtils();
	}

	protected WebElement waitForVisible(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	protected WebElement waitForClickable(By locator) {
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	protected boolean waitForInvisible(By locator) {
		return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	protected boolean isElementPresent(By locator) {
		return !driver.findElements(locator).isEmpty();
	}

	protected void clickBy(By locator) {
		waitForClickable(locator).click();
	}

	protected void typeAndEnter(By locator, String text) {
		WebElement element = waitForVisible(locator);
		element.clear();
		element.sendKeys(text, Keys.ENTER);
	}

	protected void typeBy(By locator, String text) {
		WebElement element = waitForVisible(locator);
		element.clear();
		element.sendKeys(text);
	}
}