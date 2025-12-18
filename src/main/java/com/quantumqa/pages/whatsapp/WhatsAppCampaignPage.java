package com.quantumqa.pages.whatsapp;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.quantumqa.base.BasePage;
import com.quantumqa.pages.components.CampaignCommonComponent;
import com.quantumqa.utils.TableSelectionUtil;

public class WhatsAppCampaignPage extends BasePage {

	private TableSelectionUtil tableSelectionUtil;

	public CampaignCommonComponent campaignCommonComponent;

	public WhatsAppCampaignPage(WebDriver driver) {
		super(driver);
		this.tableSelectionUtil = new TableSelectionUtil(driver, wait);
		this.campaignCommonComponent = new CampaignCommonComponent(driver, wait);
	}

	@FindBy(xpath = "//div[contains(@class, 'menu-event') and .//span[contains(@class, 'icon-Menu_WhatsApp')]]")
	private WebElement whatsappMenu;

	@FindBy(xpath = "//span[normalize-space()='Templates']/ancestor::li[1]/preceding-sibling::li[1]//span[normalize-space()='Campaigns']")
	private WebElement campaignsSubMenu;

	@FindBy(css = ".wa-business-number")
	private WebElement whatsappBusinessNumber;

	@FindBy(xpath = "//button[contains(text(),'Import Contacts')]")
	private WebElement importContactsButton;

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

	@FindBy(css = "button.import-btn.btn-design")
	private WebElement templateSaveButton;

	@FindBy(css = "button.import-btn.btn-design-white")
	private WebElement templateCancelButton;

	@FindBy(css = "button.btn-design.btn-send")
	private WebElement campaignSendButton;

	@FindBy(xpath = "//button[@type='button' and contains(normalize-space(.),'Send Now')]")
	private WebElement campaignSendNowButton;

	public void clickOnWabaMenu() {
		click(whatsappMenu);
	}

	public void clickOnCampaignsSubMenu() {
		click(campaignsSubMenu);
	}

	public void clickOnCreateCampaignButton() {
		campaignCommonComponent.clickOnCreateCampaignButton();
	}

	public void enterCampaignName(String campaignName) {
		campaignCommonComponent.enterCampaignName(campaignName);
	}

	public void selectCampaignCategory(String categoryText) {
		campaignCommonComponent.selectCampaignCategory(categoryText);
	}

	public void clickOnImportContacts() {
		click(importContactsButton);
	}

	public void clickOnListTab() {
		click(contactsListTab);
	}

	public void searchContactList(String listName) {
		type(contactsListSearchInput, listName);
	}

	public void selectContactList(String contactListName) {
		tableSelectionUtil.selectContactListByName(contactListName);
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
		tableSelectionUtil.selectTemplateByName(templateName);
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