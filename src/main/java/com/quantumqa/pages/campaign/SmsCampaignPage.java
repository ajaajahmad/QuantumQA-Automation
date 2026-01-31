package com.quantumqa.pages.campaign;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.quantumqa.base.BasePage;
import com.quantumqa.pages.components.CampaignCommonComponent;
import com.quantumqa.utils.TableSelectionManager;

public class SmsCampaignPage extends BasePage {

	private TableSelectionManager tableSelectionManager;
	private CampaignCommonComponent campaignCommonComponent;

	public SmsCampaignPage(WebDriver driver) {
		super(driver);
		this.tableSelectionManager = new TableSelectionManager(driver);
		this.campaignCommonComponent = new CampaignCommonComponent(driver);
	}

	// ================= LOCATORS =================

	private By smsMenu = By
			.xpath("//div[contains(@class, 'menu-event') and .//span[contains(@class, 'icon-Menu_SMS')]]");

	private By senderIdsElement = By.xpath("//span[normalize-space()='Sender IDs']");

	private By campaignsElement = By.xpath("//span[normalize-space()='Campaigns']");

	private By campaignsDltPopup = By.xpath("//button[contains(text(),'OK')]");

	private By createCampaign = By.xpath(
			"//button[@class='btn btn-design btn-send ng-star-inserted']//span[contains(text(),'Create Campaign')]");

	private By campaignType = By.xpath("//input[@value='Personalised']");

	private By importContacts = By.xpath("//button[contains(text(),'Import Contacts')]");

	private By contactsListTab = By.xpath("//span[@class='mdc-tab__text-label'][contains(text(),'Lists')]");

	private By contactsListSearchInput = By.xpath("//input[@placeholder='Search List']");

	private By contactsListGoButton = By.xpath("//button[contains(text(),'Go')]");

	private By contactsImportButton = By
			.xpath("//button[contains(@class,'process-file') and contains(normalize-space(),'Import')]");

	private By chooseTemplateButton = By
			.xpath("//button[contains(@class,'btn-design') and contains(text(),'Choose Template')]");

	private By searchTemplate = By.xpath("//input[contains(@class,'filter-search')]");

	//private By chooseTemplateRadioButton = By.id("mat-radio-28-input");

	private By templateSaveButton = By.cssSelector("button.import-btn.btn-design");

	//private By templateCancelButton = By.cssSelector("button.import-btn.btn-design-white");

	private By campaignSendButton = By.cssSelector("button.btn-design.btn-send");

	private By campaignSendNowButton = By.xpath("//button[@type='button' and contains(normalize-space(.),'Send Now')]");

	// ================= ACTIONS =================

	public void clickOnSmsMenu() {
		click(smsMenu);
	}

	public WebElement getCampaignsSubMenu() {
		return driver.findElement(with(campaignsElement).above(senderIdsElement));
	}

	public void clickOnCampaignsSubMenu() {
		getCampaignsSubMenu().click();
	}

	public void acknowledgeDltPopupWindow() {
		click(campaignsDltPopup);
	}

	public void clickOnCreateCampaignButton() {
		click(createCampaign);
	}

	public void enterCampaignName(String campaignName) {
		campaignCommonComponent.enterCampaignName(campaignName);
	}

	public void selectCampaignCategory(String categoryText) {
		campaignCommonComponent.selectCampaignCategory(categoryText);
	}

	public void selectCampaignType() {
		click(campaignType);
	}

	public void clickOnImportContacts() {
		click(importContacts);
	}

	public void clickOnListTab() {
		click(contactsListTab);
	}

	public void searchContactList(String listName) {
		type(contactsListSearchInput, listName);
	}

	public void selectContactList(String contactListName) {
		tableSelectionManager.selectContactListByName(contactListName);
	}

	public void clickOnGoButton() {
		click(contactsListGoButton);
	}

	public void clickOnImportButton() {
		click(contactsImportButton);
	}

	public void clickOnChooseTemplate() {
		click(chooseTemplateButton);
	}

	public void searchTemplate(String templateName) {
		click(searchTemplate);
		type(searchTemplate, templateName);
		driver.findElement(searchTemplate).sendKeys(Keys.ENTER);
	}

	public void selectTemplate(String templateName) {
		tableSelectionManager.selectTemplateByName(templateName);
	}

	public void clickOnSaveButton() {
		click(templateSaveButton);
	}

	public void clickOnSendButton() {
		click(campaignSendButton);
	}

	public void clickOnSendNowButton() {
		click(campaignSendNowButton);
	}
}