package com.quantumqa.utils;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.quantumqa.base.BasePage;

public class TableSelectionUtil extends BasePage {

	public TableSelectionUtil(WebDriver driver) {
		super(driver);
	}

	public void selectContactListByName(String displayedName) {

		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

		By tableRowLocator = By.xpath("//tbody//tr");

		wait.until(d -> !d.findElements(tableRowLocator).isEmpty());

		List<WebElement> tableRows = driver.findElements(tableRowLocator);

		for (WebElement tableRow : tableRows) {
			try {
				WebElement nameColumn = tableRow
						.findElement(By.xpath(".//td[2]//span[contains(@class,'table-data-row')]"));

				if (nameColumn.getText().trim().equalsIgnoreCase(displayedName)) {

					WebElement checkboxTouchTarget = tableRow
							.findElement(By.xpath(".//div[contains(@class,'mat-mdc-checkbox-touch-target')]"));

					jsExecutor.executeScript("arguments[0].scrollIntoView({block:'center'});", checkboxTouchTarget);

					jsExecutor.executeScript("arguments[0].click();", checkboxTouchTarget);

					wait.until(driverInstance -> {
						try {
							WebElement checkbox = driverInstance.findElement(By.xpath(
									"//tbody//tr[.//span[normalize-space()='" + displayedName + "']]//mat-checkbox"));
							return checkbox.getAttribute("class").contains("mat-mdc-checkbox-checked");
						} catch (StaleElementReferenceException exception) {
							return false;
						}
					});

					return;
				}

			} catch (StaleElementReferenceException exception) {
				selectContactListByName(displayedName);
				return;
			}
		}

		throw new RuntimeException("Row not found in table for name: " + displayedName);
	}

	public void selectTemplateByName(String displayedName) {

		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

		By tableRowLocator = By.xpath("//tbody//tr");

		wait.until(d -> !d.findElements(tableRowLocator).isEmpty());

		List<WebElement> tableRows = driver.findElements(tableRowLocator);

		for (WebElement tableRow : tableRows) {
			try {
				WebElement nameColumn = tableRow
						.findElement(By.xpath(".//td[2]//span[contains(@class,'table-data-row')]"));

				if (nameColumn.getText().trim().equalsIgnoreCase(displayedName)) {

					WebElement radioTouchTarget = tableRow
							.findElement(By.xpath(".//div[contains(@class,'mat-mdc-radio-touch-target')]"));

					jsExecutor.executeScript("arguments[0].scrollIntoView({block:'center'});", radioTouchTarget);

					jsExecutor.executeScript("arguments[0].click();", radioTouchTarget);

					wait.until(driverInstance -> {
						try {
							WebElement radioInput = driverInstance
									.findElement(By.xpath("//tbody//tr[.//span[normalize-space()='" + displayedName
											+ "']]//input[@type='radio']"));
							return radioInput.isSelected();
						} catch (StaleElementReferenceException exception) {
							return false;
						}
					});

					return;
				}

			} catch (StaleElementReferenceException exception) {
				selectTemplateByName(displayedName);
				return;
			}
		}

		throw new RuntimeException("Row not found in table for name: " + displayedName);
	}
}