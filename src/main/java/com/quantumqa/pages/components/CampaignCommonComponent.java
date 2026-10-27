package com.quantumqa.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.quantumqa.base.BasePage;

public class CampaignCommonComponent extends BasePage {

	public CampaignCommonComponent(WebDriver driver) {
		super(driver);
	}

	// ========== LOCATORS ==========

	private By createCampaignButton = By
			.xpath("//button[contains(@class,'btn-send')]//span[normalize-space()='Create Campaign']");

	private By campaignNameInputBox = By.xpath("//div[contains(@class,'campaign-name')]//input[@type='text']");

	private By categoryDropdown = By.xpath(
			"//div[contains(@class,'dropdown') and contains(@class,'labeled')]//span[normalize-space()='Select']");

	private By categorySearchInputBox = By.xpath("//input[@placeholder='Search ...']");

	private By selectSearchedCategory = By.xpath("//div[@title='automation']");

	// ========== ACTIONS ==========

	public void clickOnCreateCampaignButton() {
		click(createCampaignButton);
	}

	public void enterCampaignName(String campaignName) {
		type(campaignNameInputBox, campaignName);
	}

	public void selectCampaignCategory(String categoryText) {
		click(categoryDropdown);
		type(categorySearchInputBox, categoryText);
		click(selectSearchedCategory);
	}
}