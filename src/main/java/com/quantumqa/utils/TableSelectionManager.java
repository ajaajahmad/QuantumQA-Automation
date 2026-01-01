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

	public void selectContactListByName(String expectedRowText) {

		try {
			Thread.sleep(2000);

			List<WebElement> rows = driver.findElements(By.xpath("//tbody//tr"));
			if (rows.isEmpty()) {
				throw new RuntimeException("Contact list table is empty");
			}

			for (WebElement row : rows) {
				try {
					WebElement nameCell = row
							.findElement(By.xpath(".//td[2]//span[contains(@class,'table-data-row')]"));

					if (nameCell.getText().trim().equalsIgnoreCase(expectedRowText)) {

						WebElement checkboxInput = row.findElement(By.cssSelector("mat-checkbox input"));

						scrollToElement(checkboxInput);
						Thread.sleep(500);

						checkboxInput.click();
						Thread.sleep(1000);

						WebElement freshCheckbox = driver.findElement(By.id(checkboxInput.getAttribute("id")));

						if (!freshCheckbox.isSelected()) {
							Thread.sleep(600);
							freshCheckbox.click();
							Thread.sleep(800);
						}

						if (!driver.findElement(By.id(checkboxInput.getAttribute("id"))).isSelected()) {
							throw new RuntimeException("Contact list checkbox reverted by Angular: " + expectedRowText);
						}

						return;
					}

				} catch (StaleElementReferenceException ignored) {
				}
			}

			throw new RuntimeException("Contact list not found in table: " + expectedRowText);

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}
	}

	public void selectTemplateByName(String templateName) {

		try {
			Thread.sleep(2500);

			List<WebElement> rows = driver.findElements(By.xpath("//tbody//tr"));
			if (rows.isEmpty()) {
				throw new RuntimeException("Template table is empty");
			}

			for (WebElement row : rows) {
				try {
					WebElement nameCell = row
							.findElement(By.xpath(".//td[2]//span[contains(@class,'table-data-row')]"));

					if (nameCell.getText().trim().equalsIgnoreCase(templateName)) {

						WebElement radioInput = row.findElement(By.cssSelector("input[type='radio']"));

						scrollToElement(radioInput);
						Thread.sleep(600);

						radioInput.click();
						Thread.sleep(300);
						radioInput.sendKeys(" "); // SPACE key
						Thread.sleep(1200);

						WebElement freshRadio = driver.findElement(By.id(radioInput.getAttribute("id")));

						if (!isRadioSelected(freshRadio)) {
							Thread.sleep(700);
							freshRadio.click();
							Thread.sleep(300);
							freshRadio.sendKeys(" ");
							Thread.sleep(1000);
						}

						if (!isRadioSelected(driver.findElement(By.id(radioInput.getAttribute("id"))))) {
							throw new RuntimeException("Template radio reverted by Angular: " + templateName);
						}

						return;
					}

				} catch (StaleElementReferenceException ignored) {
				}
			}

			throw new RuntimeException("Template not found in table: " + templateName);

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}
	}

	private void scrollToElement(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
	}

	private boolean isRadioSelected(WebElement radioInput) {
		String ariaChecked = radioInput.getAttribute("aria-checked");
		return "true".equalsIgnoreCase(ariaChecked) || radioInput.isSelected();
	}
}