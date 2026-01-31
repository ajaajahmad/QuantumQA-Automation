package com.quantumqa.base;

import java.time.Duration;
import java.util.List;

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
	private static final long HARD_SLEEP_MS = 300;

	public BasePage(WebDriver driver) {
		this.driver = driver;
		BasePage.log = new LogUtils();
		this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
	}

	// ================= WAITS =================

	protected WebElement waitForVisible(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	protected List<WebElement> waitForVisibleElements(By locator) {
		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	protected WebElement waitForClickable(By locator) {
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	protected boolean waitForInvisible(By locator) {
		return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	// ================= FIND =================

	protected WebElement getElement(By locator) {
		return driver.findElement(locator);
	}

	protected List<WebElement> getElements(By locator) {
		return driver.findElements(locator);
	}

	// ================= VALIDATION =================

	protected boolean isElementPresent(By locator) {
		return !driver.findElements(locator).isEmpty();
	}

	protected boolean isElementDisplayed(By locator) {
		List<WebElement> elements = driver.findElements(locator);
		return !elements.isEmpty() && elements.get(0).isDisplayed();
	}

	// ================= ACTIONS =================

	protected void click(By locator) {
		sleep();
		waitForClickable(locator).click();
		sleep();
	}

	protected void type(By locator, String text) {
		sleep();
		WebElement element = waitForVisible(locator);
		element.clear();
		element.sendKeys(text);
		sleep();
	}

	protected void typeAndEnter(By locator, String text) {
		WebElement element = waitForVisible(locator);
		element.clear();
		element.sendKeys(text, Keys.ENTER);
	}

	protected String getText(By locator) {
		return waitForVisible(locator).getText();
	}

	// ================= UTILS =================

	protected void sleep() {
		try {
			Thread.sleep(HARD_SLEEP_MS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}