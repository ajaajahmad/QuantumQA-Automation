package com.quantumqa.base;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

	protected WebDriver driver;
	protected WebDriverWait wait;

	private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);
	private static final long HARD_SLEEP_MS = 1000;

	public BasePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
		PageFactory.initElements(driver, this);
	}

	protected WebElement waitForVisible(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	protected WebElement waitForVisible(WebElement element) {
		return wait.until(ExpectedConditions.visibilityOf(element));
	}

	protected WebElement waitForClickable(WebElement element) {
		return wait.until(ExpectedConditions.elementToBeClickable(element));
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

	protected boolean isElementDisplayed(By locator) {
		List<WebElement> elements = driver.findElements(locator);
		return !elements.isEmpty() && elements.get(0).isDisplayed();
	}

	protected void click(WebElement element) {
		hardSleep();
		waitForClickable(element).click();
		hardSleep();
	}

	protected void type(WebElement element, String text) {
		hardSleep();
		waitForVisible(element);
		element.clear();
		element.sendKeys(text);
		hardSleep();
	}

	private void hardSleep() {
		try {
			Thread.sleep(HARD_SLEEP_MS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}