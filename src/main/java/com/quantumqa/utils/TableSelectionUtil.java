package com.quantumqa.utils;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TableSelectionUtil {

	private WebDriver driver;

	public TableSelectionUtil(WebDriver driver, WebDriverWait webDriverWait) {
		this.driver = driver;
	}

	public void selectContactListByName(String expectedRowText) {

		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

		FluentWait<WebDriver> tableFluentWait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(10))
				.pollingEvery(Duration.ofSeconds(2)).ignoring(StaleElementReferenceException.class);

		WebElement targetRow = tableFluentWait.until(driverInstance -> {

			List<WebElement> tableRows = driverInstance.findElements(By.xpath("//tbody//tr"));

			for (WebElement row : tableRows) {
				try {
					WebElement nameColumn = row
							.findElement(By.xpath(".//td[2]//span[contains(@class,'table-data-row')]"));

					if (nameColumn.getText().trim().equalsIgnoreCase(expectedRowText)) {
						return row;
					}
				} catch (StaleElementReferenceException ignored) {
				}
			}
			return null;
		});

		if (targetRow == null) {
			throw new RuntimeException("Contact list not found in table: " + expectedRowText);
		}

		WebElement checkboxTouchTarget = targetRow
				.findElement(By.xpath(".//div[contains(@class,'mat-mdc-checkbox-touch-target')]"));

		jsExecutor.executeScript("arguments[0].scrollIntoView({block:'center'});", checkboxTouchTarget);
		jsExecutor.executeScript("arguments[0].click();", checkboxTouchTarget);

		tableFluentWait.until(driverInstance -> {
			try {
				WebElement checkbox = driverInstance.findElement(
						By.xpath("//tbody//tr[.//span[normalize-space()='" + expectedRowText + "']]//mat-checkbox"));
				return checkbox.getAttribute("class").contains("mat-mdc-checkbox-checked");
			} catch (StaleElementReferenceException e) {
				return false;
			}
		});
	}

	public void selectTemplateByName(String expectedRowText) {

		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

		FluentWait<WebDriver> tableFluentWait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(15))
				.pollingEvery(Duration.ofMillis(500)).ignoring(StaleElementReferenceException.class);

		WebElement targetRow = tableFluentWait.until(driverInstance -> {

			List<WebElement> tableRows = driverInstance.findElements(By.xpath("//tbody//tr"));

			for (WebElement row : tableRows) {
				try {
					WebElement nameColumn = row
							.findElement(By.xpath(".//td[2]//span[contains(@class,'table-data-row')]"));

					if (nameColumn.getText().trim().equalsIgnoreCase(expectedRowText)) {
						return row;
					}

				} catch (StaleElementReferenceException ignored) {
				}
			}
			return null;
		});

		if (targetRow == null) {
			throw new RuntimeException("Template not found in table: " + expectedRowText);
		}

		WebElement radioTouchTarget = targetRow
				.findElement(By.xpath(".//div[contains(@class,'mat-mdc-radio-touch-target')]"));

		jsExecutor.executeScript("arguments[0].scrollIntoView({block:'center'});", radioTouchTarget);
		jsExecutor.executeScript("arguments[0].click();", radioTouchTarget);

		tableFluentWait.until(driverInstance -> {
			try {
				WebElement radioInput = driverInstance.findElement(By.xpath(
						"//tbody//tr[.//span[normalize-space()='" + expectedRowText + "']]//input[@type='radio']"));
				return radioInput.isSelected();
			} catch (StaleElementReferenceException e) {
				return false;
			}
		});
	}
}