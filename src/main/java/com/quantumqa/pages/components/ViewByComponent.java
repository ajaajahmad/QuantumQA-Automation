package com.quantumqa.pages.components;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.quantumqa.base.BasePage;

public class ViewByComponent extends BasePage {

	public ViewByComponent(WebDriver driver) {
		super(driver);
	}

	@FindBy(id = "more_con")
	private WebElement moreViewByDropdown;

	public void selectViewByTab(String viewByText) {
		WebElement viewByTab = driver.findElement(By.xpath("//li//span[normalize-space()='" + viewByText + "']"));
		click(viewByTab);
		sleep();
	}

	public boolean selectMoreViewByOption(int index) {

		WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(10));

		explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("loader-wrapper")));

		click(moreViewByDropdown);

		List<WebElement> options = explicitWait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("ul.bottom_drop li")));

		if (options.size() > index) {
			options.get(index).click();
			sleep();
			return true;
		}

		return false;
	}
}