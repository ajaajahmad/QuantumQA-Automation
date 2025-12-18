package com.quantumqa.pages.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.quantumqa.base.BasePage;

public class CampaignCommonComponent extends BasePage {

	public CampaignCommonComponent(WebDriver driver, WebDriverWait wait) {
		super(driver);
	}

	@FindBy(xpath = "//button[@class='btn btn-design btn-send ng-star-inserted']//span[contains(text(),'Create Campaign')]")
	private WebElement createCampaignButton;

	@FindBy(xpath = "//div[@class='col-md-3 campaign-name ng-star-inserted']//input[@type='text']")
	private WebElement campaignNameInputBox;

	@FindBy(xpath = "//div[@class='ui floating dropdown labeled icon button ng-star-inserted']//span[contains(text(),'Select')]")
	private WebElement campaignCategoryDropdown;

	@FindBy(xpath = "//input[@placeholder='Create New']")
	private WebElement campaignCreateCategoryInput;

	@FindBy(xpath = "//*[@id='Layer_1']")
	private WebElement campaignCreateCategoryAddIcon;

	@FindBy(xpath = "//div[@class='scrolling menu']//div[@title='AutowSMS']")
	private WebElement campaignSelectCategory;

	@FindBy(xpath = "//input[@placeholder='Search ...']")
	private WebElement campaignCategorySearchInputBox;

	@FindBy(xpath = "//div[@title='Auto SMS']")
	private WebElement campaignSelectSearchedCategory;

	public void clickOnCreateCampaignButton() {
		click(createCampaignButton);
	}

	public void enterCampaignName(String campaignName) {
		type(campaignNameInputBox, campaignName);
	}

	public void selectCampaignCategory(String categoryText) {
		click(campaignCategoryDropdown);
		type(campaignCreateCategoryInput, categoryText);
		click(campaignCreateCategoryAddIcon);
	}

}
