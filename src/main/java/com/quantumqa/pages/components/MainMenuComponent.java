package com.quantumqa.pages.components;

import org.openqa.selenium.*;
import com.quantumqa.base.BasePage;

public class MainMenuComponent extends BasePage {

	public MainMenuComponent(WebDriver driver) {
		super(driver);
	}

	private By sidebar = By.xpath("//div[contains(@class,'custom-new-menu-wrapper')]");

	private WebElement root() {
		sleep(5000);
		return driver.findElement(sidebar);
	}

	private WebElement expand(WebElement scope, String text) {

		WebElement li = scope.findElement(By.xpath(".//span[normalize-space()='" + text + "']/ancestor::li[1]"));

		WebElement toggle = li
				.findElement(By.xpath("./div[contains(@class,'menu-event') or contains(@class,'child-menu-event')]"));

		WebElement collapse = li.findElement(By.xpath("./div[contains(@class,'collapse')]"));

		if (!collapse.isDisplayed()) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", toggle);
			sleep(800);
			toggle.click();
			sleep(1000);
		}

		return collapse;
	}

	private void clickFinal(WebElement scope, String text) {

		WebElement target = scope.findElement(By.xpath(".//span[normalize-space()='" + text + "']"));

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", target);

		sleep(800);
		target.click();
		sleep(1000);
	}

	public void navigate(String... menuPath) {

		WebElement currentScope = root();

		for (int i = 0; i < menuPath.length - 1; i++) {
			currentScope = expand(currentScope, menuPath[i]);
		}

		clickFinal(currentScope, menuPath[menuPath.length - 1]);
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}