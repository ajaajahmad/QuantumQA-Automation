package com.quantumqa.pages.reports;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.quantumqa.base.BasePage;
import com.quantumqa.pages.components.MainMenuComponent;

public class SmsSummaryPage extends BasePage {

	private MainMenuComponent menu;

	private String[] viewOptions = { "Date & Campaign", "Date", "Campaign" };

	public SmsSummaryPage(WebDriver driver) {
		super(driver);
		this.menu = new MainMenuComponent(driver);
	}

	@FindBy(id = "more_con")
	private WebElement dropdown;

	public void openSmsSummary() {
		menu.navigate("Reports", "SMS", "Summary");
		sleep();
	}

	public void selectViewOptionsAndDropdowns() throws InterruptedException {

		for (String viewOption : viewOptions) {
			clickViewOption(viewOption);
		}

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		for (int i = 0; i < 11; i++) {

			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("loader-wrapper")));
			click(dropdown);

			List<WebElement> options = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("ul.bottom_drop li")));

			options = driver.findElements(By.cssSelector("ul.bottom_drop li"));

			if (options.size() > i) {
				WebElement option = options.get(i);
				option.click();
				sleep();
			} else {
				System.out.println("Index " + i + " out of bounds for the options list");
				break;
			}

			sleep();
		}
	}

	private void clickViewOption(String option) throws InterruptedException {
		WebElement viewOptionElement = driver
				.findElement(By.xpath("//li[contains(@class, 'ng-star-inserted')]/span[text()='" + option + "']"));
		viewOptionElement.click();
		sleep();
	}
}