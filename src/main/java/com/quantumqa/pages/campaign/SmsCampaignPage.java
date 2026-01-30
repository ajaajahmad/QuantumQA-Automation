package com.quantumqa.pages.campaign;

import org.openqa.selenium.By;
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
		this.campaignCommonComponent = new CampaignCommonComponent(driver, wait);
	}

	private By smsMenu = By.xpath("//div[contains(@class,'menu-event')]");
	private By senderIdsElement = By.xpath("//span[normalize-space()='Sender IDs']");
	private By campaignsElement = By.xpath("//span[normalize-space()='Campaigns']");
	private By campaignsDltPopup = By.xpath("//button[contains(text(),'OK')]");
	private By createCampaign = By
			.xpath("//button[contains(@class,'btn-send')]//span[normalize-space()='Create Campaign']");
	private By campaignType = By.xpath("//input[@value='Personalised");
	private By importContacts = By.xpath("//button[contains(text(),'Import Contacts')]");
	private By contactsListTab = By.xpath("//span[@class='mdc-tab__text-label'][contains(text(),'Lists')]");
	private By contactsListSearchInput = By.xpath("//input[@placeholder='Search List']");
	private By contactsListGoButton = By.xpath("//button[normalize-space()='Go']");
	private By contactsImportButton = By
			.xpath("//button[contains(@class,'process-file') and normalize-space()='Import']");
	private By chooseTemplateButton = By
			.xpath("//button[contains(@class,'btn-design') and normalize-space()='Choose Template']");
	private By searchTemplate = By.xpath("//input[contains(@class,'filter-search')]");
	private By chooseTemplateRadioButton = By.id("mat-radio-28-input");
	private By templateSaveButton = By.cssSelector("button.import-btn.btn-design");
	private By templateCancelButton = By.cssSelector("button.import-btn.btn-design-white");
	private By campaignSendButton = By.cssSelector("button.btn-design.btn-send");
	private By campaignSendNowButton = By.xpath("//button[@type='button' and normalize-space()='Send Now']");

	public void clickOnSmsMenu() {
		clickBy(smsMenu);
	}

	public WebElement getCampaignsSubMenu() {
		return driver.findElement(with(campaignsElement).above(senderIdsElement));
	}

	public void clickOnCampaignsSubMenu() {
		getCampaignsSubMenu().click();
	}

	public void acknowledgeDltPopupWindow() {
		if (isElementPresent(campaignsDltPopup)) {
			clickBy(campaignsDltPopup);
		}
	}

	public void clickOnCreateCampaignButton() {
		acknowledgeDltPopupWindow();
		clickBy(createCampaign);
	}

	public void enterCampaignName(String campaignName) {
		campaignCommonComponent.enterCampaignName(campaignName);
	}

	public void selectCampaignCategory(String categoryText) {
		campaignCommonComponent.selectCampaignCategory(categoryText);
	}

	public void selectCampaignType() {
		clickBy(campaignType);
	}

	public void clickOnImportContacts() {
		clickBy(importContacts);
	}

	public void clickOnListTab() {
		clickBy(contactsListTab);
	}

	public void searchContactList(String listName) {
		typeBy(contactsListSearchInput, listName);
	}

	public void selectContactList(String contactListName) {
		tableSelectionManager.selectContactListByName(contactListName);
	}

	public void clickOnGoButton() {
		clickBy(contactsListGoButton);
	}

	public void clickOnImportButton() {
		clickBy(contactsImportButton);
	}

	public void clickOnChooseTemplate() {
		clickBy(chooseTemplateButton);
	}

	public void searchTemplate(String templateName) {
		typeAndEnter(searchTemplate, templateName);
	}

	public void selectTemplate(String templateName) {
		tableSelectionManager.selectTemplateByName(templateName);
	}

	public void clickOnSaveButton() {
		clickBy(templateSaveButton);
	}

	public void clickOnSendButton() {
		clickBy(campaignSendButton);
	}

	public void clickOnSendNowButton() {
		clickBy(campaignSendNowButton);
	}

}