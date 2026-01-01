package com.quantumqa.pages.sms;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
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

	@FindBy(xpath = "//div[contains(@class, 'menu-event') and .//span[contains(@class, 'icon-Menu_SMS')]]")
	private WebElement smsMenu;

	@FindBy(xpath = "//span[normalize-space()='Sender IDs']/ancestor::li[1]/preceding-sibling::li[1]//span[normalize-space()='Campaigns']")
	private WebElement campaignsSubMenu;

	@FindBy(xpath = "//button[contains(text(),'OK')]")
	private WebElement campaignsDltPopup;

	@FindBy(xpath = "//button[@class='btn btn-design btn-send ng-star-inserted']//span[contains(text(),'Create Campaign')]")
	private WebElement createCampaign;

	@FindBy(xpath = "//input[@value='Personalised']")
	private WebElement campaignType;

	@FindBy(xpath = "//button[contains(text(),'Import Contacts')]")
	private WebElement importContacts;

	@FindBy(xpath = "//span[@class='mdc-tab__text-label'][contains(text(),'Lists')]")
	private WebElement contactsListTab;

	@FindBy(xpath = "//input[@placeholder='Search List']")
	private WebElement contactsListSearchInput;

	@FindBy(xpath = "//button[contains(text(),'Go')]")
	private WebElement contactsListGoButton;

	@FindBy(xpath = "//button[contains(@class,'process-file') and contains(normalize-space(),'Import')]")
	private WebElement contactsImportButton;

	@FindBy(xpath = "//button[contains(@class,'btn-design') and contains(text(),'Choose Template')]")
	private WebElement chooseTemplateButton;

	@FindBy(xpath = "//input[contains(@class,'filter-search')]")
	private WebElement searchTemplate;

	@FindBy(id = "mat-radio-28-input")
	private WebElement chooseTemplateRadioButton;

	@FindBy(css = "button.import-btn.btn-design")
	private WebElement tempmateSaveButton;

	@FindBy(css = "button.import-btn.btn-design-white")
	private WebElement templateCancelButton;

	@FindBy(css = "button.btn-design.btn-send")
	private WebElement campaingSendButton;

	@FindBy(xpath = "//button[@type='button' and contains(normalize-space(.),'Send Now')]")
	private WebElement campaignSendNowButton;

	public void clickOnSmsMenu() {
		click(smsMenu);
	}

	public void clickOnCampaignsSubMenu() {
		click(campaignsSubMenu);
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
		searchTemplate.sendKeys(Keys.ENTER);
	}

	public void selectTemplate(String templateName) {
		tableSelectionManager.selectTemplateByName(templateName);
	}

	public void clickOnSaveButton() {
		click(tempmateSaveButton);
	}

	public void clickOnSendButton() {
		click(campaingSendButton);
	}

	public void clickOnSendNowButton() {
		click(campaignSendNowButton);
	}

}