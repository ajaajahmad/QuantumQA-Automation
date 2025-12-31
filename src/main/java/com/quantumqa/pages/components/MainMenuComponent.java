package com.quantumqa.pages.components;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.quantumqa.base.BasePage;

public class MainMenuComponent extends BasePage {

	public MainMenuComponent(WebDriver driver) {
		super(driver);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	private By sideBar = By.xpath("//div[contains(@class,'custom-new-menu-wrapper')]");

	private WebElement getSidebar() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(sideBar));
	}

	public void clickMainMenu(String menuName) {
		WebElement menu = getSidebar()
				.findElement(By.xpath(".//span[@class='menu-heading' and normalize-space()='" + menuName + "']"));
		menu.click();
	}

	public void clickSubMenu(String parentMenu, String childMenu) {
		WebElement subMenu = getSidebar().findElement(By.xpath(
				".//span[normalize-space()='" + parentMenu + "']" + "/ancestor::li[contains(@class,'menu-list-items')]"
						+ "//span[normalize-space()='" + childMenu + "']"));
		subMenu.click();
	}
}