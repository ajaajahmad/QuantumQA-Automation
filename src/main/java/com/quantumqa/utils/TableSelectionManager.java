package com.quantumqa.utils;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TableSelectionManager {

	private final WebDriver driver;

	public TableSelectionManager(WebDriver driver) {
		this.driver = driver;
	}

	public void selectContactListByName(String listName) {

		JavascriptExecutor js = (JavascriptExecutor) driver;

		sleep(1500);

		for (int attempt = 1; attempt <= 3; attempt++) {
			try {
				List<WebElement> tableRows = driver.findElements(By.xpath("//tbody//tr"));

				for (WebElement row : tableRows) {

					WebElement nameCell = row
							.findElement(By.xpath(".//td[2]//span[contains(@class,'table-data-row')]"));

					if (nameCell.getText().trim().equalsIgnoreCase(listName)) {

						WebElement checkboxTouchTarget = row
								.findElement(By.xpath(".//div[contains(@class,'mat-mdc-checkbox-touch-target')]"));

						js.executeScript("arguments[0].scrollIntoView({block:'center'});", checkboxTouchTarget);
						js.executeScript("arguments[0].click();", checkboxTouchTarget);

						sleep(300);

						WebElement freshCheckbox = driver.findElement(
								By.xpath("//tbody//tr[.//span[normalize-space()='" + listName + "']]//mat-checkbox"));

						if (freshCheckbox.getAttribute("class").contains("mat-mdc-checkbox-checked")) {
							return;
						}

						js.executeScript("arguments[0].click();", checkboxTouchTarget);
						sleep(300);
						return;
					}
				}

			} catch (StaleElementReferenceException ignored) {
				sleep(500);
			}
		}

		throw new RuntimeException("Contact list not selected: " + listName);
	}

	public void selectTemplateByName(String templateName) {

		JavascriptExecutor js = (JavascriptExecutor) driver;

		sleep(1500);

		for (int attempt = 1; attempt <= 3; attempt++) {
			try {
				List<WebElement> tableRows = driver.findElements(By.xpath("//tbody//tr"));

				for (WebElement row : tableRows) {

					WebElement nameCell = row
							.findElement(By.xpath(".//td[2]//span[contains(@class,'table-data-row')]"));

					if (nameCell.getText().trim().equalsIgnoreCase(templateName)) {

						WebElement radioTouchTarget = row
								.findElement(By.xpath(".//div[contains(@class,'mat-mdc-radio-touch-target')]"));

						js.executeScript("arguments[0].scrollIntoView({block:'center'});", radioTouchTarget);
						js.executeScript("arguments[0].click();", radioTouchTarget);

						sleep(300);

						WebElement freshRadio = driver.findElement(By.xpath("//tbody//tr[.//span[normalize-space()='"
								+ templateName + "']]//input[@type='radio']"));

						if ("true".equalsIgnoreCase(freshRadio.getAttribute("aria-checked"))) {
							return;
						}

						js.executeScript("arguments[0].click();", radioTouchTarget);
						sleep(300);
						return;
					}
				}

			} catch (StaleElementReferenceException ignored) {
				sleep(500);
			}
		}

		throw new RuntimeException("Template not selected: " + templateName);
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}