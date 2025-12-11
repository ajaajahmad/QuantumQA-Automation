package com.quantumqa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.quantumqa.base.BasePage;

public class SmsCampaignPage extends BasePage {

	public SmsCampaignPage(WebDriver driver) {
		super(driver);
	}

	@FindBy(xpath = "//span[@class='menu-heading active-menu'][contains(text(),'SMS')]")
	private WebElement smsMenu;
	@FindBy(xpath = "//span[normalize-space()='Sender IDs']/ancestor::li[1]/preceding-sibling::li[1]//span[normalize-space()='Campaigns']")
	private WebElement smsCampaignsMenu;
	@FindBy(xpath = "//button[contains(text(),'OK')]")
	private WebElement smsCampaignsDltPopup;
	@FindBy(xpath = "//button[@class='btn btn-design btn-send ng-star-inserted']//span[contains(text(),'Create Campaign')]")
	private WebElement smsCreateCampaign;
	@FindBy(xpath = "//div[@class='col-md-3 campaign-name ng-star-inserted']//input[@type='text']")
	private WebElement smsCampaignName;
	@FindBy(xpath = "//div[@class='ui floating dropdown labeled icon button ng-star-inserted']//span[contains(text(),'Select')]")
	private WebElement smsCampaignCategory;
	@FindBy(xpath = "//input[@placeholder='Create New']")
	private WebElement smsCampaignCreateCategoryInput;
	@FindBy(xpath = "//*[@id='Layer_1']")
	private WebElement smsCampaignCreateCategoryAddIcon;
	@FindBy(xpath = "//div[@class='scrolling menu']//div[@title='AutoSMS']")
	private WebElement smsCampaignSelectCategory;
	@FindBy(xpath = "//input[@placeholder='Search ...']")
	private WebElement smsCampaignCategorySearchInput;
}