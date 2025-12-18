package com.quantumqa.pages.sms;

import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.quantumqa.base.BasePage;
import com.quantumqa.utils.TableSelectionUtil;

public class SmsCampaignPage extends BasePage {

	private TableSelectionUtil tableSelectionUtil;

	public SmsCampaignPage(WebDriver driver) {
		super(driver);
		this.tableSelectionUtil = new TableSelectionUtil(driver, wait);
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

	@FindBy(xpath = "//button[contains(@class,'btn-design') and contains(text(),'Choose Template')]")
	private WebElement smsCampaignChooseTemplateBtn;

	@FindBy(xpath = "//input[contains(@class,'filter-search')]")
	private WebElement smsCampaingSearchTemplate;

	@FindBy(id = "mat-radio-28-input")
	private WebElement smsCampaignChooseTemplateRadioBtn;

	@FindBy(xpath = "//tbody//tr")
	private List<WebElement> templateTableRows;

	@FindBy(xpath = ".//td[2]//span[contains(@class,'table-data-row')]")
	private WebElement row;

	@FindBy(xpath = ".//div[contains(@class,'mat-mdc-radio-touch-target')]")
	private WebElement radioTouchTarget;

	@FindBy(css = "button.import-btn.btn-design")
	private WebElement smsCampaignTempmateSaveBtn;

	@FindBy(css = "button.import-btn.btn-design-white")
	private WebElement smsCampaignTemplateCancelBtn;

	@FindBy(css = "button.btn-design.btn-send")
	private WebElement smsCampaingSendBtn;

	@FindBy(xpath = "//button[@type='button' and contains(normalize-space(.),'Send Now')]")
	private WebElement smsCampaignSendNowBtn;

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

	public void searchContactList(String listName) {
		type(smsCampaignListSearchInput, listName);
	}

	public void selectContactList(String contactListName) {
		tableSelectionUtil.selectContactListByName(contactListName);
	}

	public void clickOnGoButton() {
		click(smsCampaignListGoBtn);
	}

	public void clickOnImportButton() {
		click(smsCampaignContactsImportBtn);
	}

	public void clickOnChooseTemplate() {
		click(smsCampaignChooseTemplateBtn);
	}

	public void searchTemplate(String templateName) {
		click(smsCampaingSearchTemplate);
		type(smsCampaingSearchTemplate, templateName);
		smsCampaingSearchTemplate.sendKeys(Keys.ENTER);
	}

	public void selectTemplate(String templateName) {
		tableSelectionUtil.selectTemplateByName(templateName);
	}

	public void clickOnSaveButton() {
		click(smsCampaignTempmateSaveBtn);
	}

	public void clickOnSendButton() {
		click(smsCampaingSendBtn);
	}

	public void clickOnSendNowButton() {
		click(smsCampaignSendNowBtn);
	}

}