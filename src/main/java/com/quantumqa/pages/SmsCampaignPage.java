package com.quantumqa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.quantumqa.base.BasePage;

public class SmsCampaignPage extends BasePage {

	public SmsCampaignPage(WebDriver driver) {
		super(driver);
	}

	@FindBy(xpath = "//div[contains(@class, 'menu-event') and .//span[contains(@class, 'icon-Menu_SMS')]]")
	private WebElement smsMenu;

	@FindBy(xpath = "//span[normalize-space()='Sender IDs']/ancestor::li[1]/preceding-sibling::li[1]//span[normalize-space()='Campaigns']")
	private WebElement smsCampaignsSubMenu;

	@FindBy(xpath = "//button[contains(text(),'OK')]")
	private WebElement smsCampaignsDltPopup;

	@FindBy(xpath = "//button[@class='btn btn-design btn-send ng-star-inserted']//span[contains(text(),'Create Campaign')]")
	private WebElement smsCreateCampaign;

	@FindBy(xpath = "//div[@class='col-md-3 campaign-name ng-star-inserted']//input[@type='text']")
	private WebElement smsCampaignName;

	@FindBy(xpath = "//div[@class='ui floating dropdown labeled icon button ng-star-inserted']//span[contains(text(),'Select')]")
	private WebElement smsCampaignCategory;

	@FindBy(xpath = "//div[@class='scrolling menu']//div[@title='AutoSMS']")
	private WebElement smsCampaignSelectCategory;

	@FindBy(xpath = "//input[@value='Personalised']")
	private WebElement smsCampaignType;

	@FindBy(xpath = "//button[contains(text(),'Import Contacts')]")
	private WebElement smsCampaignImportContacts;

	@FindBy(xpath = "//span[@class='mdc-tab__text-label'][contains(text(),'Lists')]")
	private WebElement smsCampaignListTab;

	@FindBy(xpath = "//input[@placeholder='Search List']")
	private WebElement smsCampaignListSearchInput;

	@FindBy(xpath = "//button[contains(text(),'Go')]")
	private WebElement smsCampaignListGoBtn;

	@FindBy(xpath = "//button[contains(@class,'process-file') and contains(normalize-space(),'Import')]")
	private WebElement smsCampaignContactsImportBtn;

	public void clickOnSmsMenu() {
		click(smsMenu);
	}

	public void clickOnCampaignsSubMenu() {
		click(smsCampaignsSubMenu);
	}

	public void acknowledgeDltPopupWindow() {
		click(smsCampaignsDltPopup);
	}

	public void clickOnCreateCampaignButton() {
		click(smsCreateCampaign);
	}

	public void enterCampaignName(String campaignName) {
		type(smsCampaignName, campaignName);
	}

	public void clickOnCampaignCategory() {
		click(smsCampaignCategory);
	}

	public void selectCampaignCategory() {
		click(smsCampaignSelectCategory);
	}

	public void selectCampaignType() {
		click(smsCampaignType);
	}

	public void clickOnImportContacts() {
		click(smsCampaignImportContacts);
	}

	public void clickOnListTab() {
		click(smsCampaignListTab);
	}

	public void enterListName(String listName) {
		type(smsCampaignListSearchInput, listName);
	}

	public void clickOnGoButton() {
		click(smsCampaignListGoBtn);
	}

	public void selectListByName(String listName) {
		String checkboxXpath = String.format("//span[normalize-space()='%s']/ancestor::tr//mat-checkbox", listName);
		WebElement checkbox = wait.until(driver -> driver.findElement(By.xpath(checkboxXpath)));
		safeClick(checkbox);
	}

	public boolean isListSelected(String listName) {
		String checkboxXpath = String.format("//span[normalize-space()='%s']/ancestor::tr//mat-checkbox", listName);
		WebElement checkbox = driver.findElement(By.xpath(checkboxXpath));
		return checkbox.getAttribute("class").contains("mat-mdc-checkbox-checked");
	}

	public void clickOnImportButton() {
		click(smsCampaignContactsImportBtn);
	}

	private void safeClick(WebElement element) {
		((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}
}