package com.quantumqa.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.quantumqa.base.BasePage;

public class MainMenuComponent extends BasePage {

	public MainMenuComponent(WebDriver driver, WebDriverWait wait) {
		super(driver);
	}

	@FindBy(css = "div.custom-new-menu-wrapper")
	private WebElement sideBar;

	public void clickMainMenu(String menuName) {
		WebElement menu = sideBar
				.findElement(By.xpath(".//span[@class='menu-heading' and normalize-space()='" + menuName + "']"));
		menu.click();
	}

	public void clickSubMenu(String parentMenu, String childMenu) {
		WebElement subMenu = sideBar.findElement(By.xpath(
				".//span[normalize-space()='" + parentMenu + "']" + "/ancestor::li[contains(@class,'menu-list-items')]"
						+ "//span[normalize-space()='" + childMenu + "']"));
		subMenu.click();
	}
}