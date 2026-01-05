package com.quantumqa.pages.components;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.quantumqa.base.BasePage;

public class ViewByComponent extends BasePage {

	public ViewByComponent(WebDriver driver) {
		super(driver);
	}

	@FindBy(id = "mis_sel")
	private WebElement viewByContainer;

	@FindBy(id = "more_con")
	private WebElement moreButton;

	public boolean selectViewByIfPresent(String excelViewBy) {

		log.info("Excel View By value: " + excelViewBy);

		if (excelViewBy.equalsIgnoreCase("More")) {
			log.info("Skipping 'More' (not selectable)");
			return false;
		}

		List<String> uiViewByOptions = getAllViewByFromUI();

		if (!uiViewByOptions.contains(excelViewBy)) {
			log.info("UI does not contain View By: " + excelViewBy);
			return false;
		}

		clickViewBy(excelViewBy);
		return true;
	}

	private void clickViewBy(String option) {

		List<WebElement> topLevel = viewByContainer
				.findElements(By.xpath(".//ul/li/span[normalize-space()='" + option + "']"));

		if (!topLevel.isEmpty()) {
			WebElement li = topLevel.get(0).findElement(By.xpath("./parent::li"));
			click(li);
			sleep();
			verifyTopLevelActive(option);
			return;
		}

		click(moreButton);
		sleep();

		List<WebElement> dropdown = viewByContainer.findElements(
				By.xpath(".//ul[contains(@class,'bottom_drop')]//li[normalize-space()='" + option + "']"));

		if (!dropdown.isEmpty()) {
			click(dropdown.get(0));
			sleep();
			verifyDropdownApplied(option);
			return;
		}
	}

	private List<String> getAllViewByFromUI() {

		List<String> values = new ArrayList<>();

		List<WebElement> topLevel = viewByContainer.findElements(By.xpath(".//ul/li/span"));

		for (WebElement e : topLevel) {
			String text = e.getText().trim();
			if (!text.isEmpty() && !text.equalsIgnoreCase("View By :")) {
				values.add(text);
			}
		}

		click(moreButton);
		sleep();

		List<WebElement> dropdown = viewByContainer.findElements(By.xpath(".//ul[contains(@class,'bottom_drop')]//li"));

		for (WebElement e : dropdown) {
			String text = e.getText().trim();
			if (!text.isEmpty()) {
				values.add(text);
			}
		}

		click(moreButton);
		sleep();

		return values;
	}

	private void verifyTopLevelActive(String option) {
		log.info("Verifying active top-level View By: " + option);

		boolean active = !viewByContainer
				.findElements(By.xpath(".//li[contains(@class,'active')]/span[normalize-space()='" + option + "']"))
				.isEmpty();

		if (!active) {
			log.warn("WARNING -> View By not visually active: " + option);
		}
	}

	private void verifyDropdownApplied(String option) {
		log.info("Verifying dropdown View By applied: " + option);

		boolean dropdownClosed = viewByContainer.findElements(By.xpath(".//ul[contains(@class,'bottom_drop')]"))
				.isEmpty();

		if (!dropdownClosed) {
			log.warn("WARNING -> Dropdown still open after selection");
		}
	}

}