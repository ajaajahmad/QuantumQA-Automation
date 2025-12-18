package com.quantumqa.pages.whatsapp;

import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.quantumqa.base.BasePage;
import com.quantumqa.utils.TableSelectionUtil;

public class WhatsAppCampaignPage extends BasePage {

	private TableSelectionUtil tableSelectionUtil;

	public WhatsAppCampaignPage(WebDriver driver) {
		super(driver);
		this.tableSelectionUtil = new TableSelectionUtil(driver, wait);
	}

	@FindBy(xpath = "//div[contains(@class, 'menu-event') and .//span[contains(@class, 'icon-Menu_WhatsApp')]]")
	private WebElement whatsappMenu;

	@FindBy(xpath = "//span[normalize-space()='Templates']/ancestor::li[1]/preceding-sibling::li[1]//span[normalize-space()='Campaigns']")
	private WebElement campaignsSubMenu;

	@FindBy(xpath = "//button[contains(text(),'Import Contacts')]")
	private WebElement wabaCampaignImportContacts;

	@FindBy(xpath = "//span[@class='mdc-tab__text-label'][contains(text(),'Lists')]")
	private WebElement wabaCampaignListTab;

	@FindBy(xpath = "//input[@placeholder='Search List']")
	private WebElement wabaCampaignListSearchInput;

	@FindBy(xpath = "//button[contains(text(),'Go')]")
	private WebElement wabaCampaignListGoBtn;

	@FindBy(xpath = "//button[contains(@class,'process-file') and contains(normalize-space(),'Import')]")
	private WebElement wabaCampaignContactsImportBtn;

	@FindBy(xpath = "//button[contains(@class,'btn-design') and contains(text(),'Choose Template')]")
	private WebElement wabaCampaignChooseTemplateBtn;

	@FindBy(xpath = "//input[contains(@class,'filter-search')]")
	private WebElement wabaCampaingSearchTemplate;

	@FindBy(id = "mat-radio-28-input")
	private WebElement wabaCampaignChooseTemplateRadioBtn;

	@FindBy(xpath = "//tbody//tr")
	private List<WebElement> templateTableRows;

	@FindBy(xpath = ".//td[2]//span[contains(@class,'table-data-row')]")
	private WebElement row;

	@FindBy(xpath = ".//div[contains(@class,'mat-mdc-radio-touch-target')]")
	private WebElement radioTouchTarget;

	@FindBy(css = "button.import-btn.btn-design")
	private WebElement wabaCampaignTempmateSaveBtn;

	@FindBy(css = "button.import-btn.btn-design-white")
	private WebElement wabaCampaignTemplateCancelBtn;

	@FindBy(css = "button.btn-design.btn-send")
	private WebElement wabaCampaingSendBtn;

	@FindBy(xpath = "//button[@type='button' and contains(normalize-space(.),'Send Now')]")
	private WebElement wabaCampaignSendNowBtn;

	public void clickOnWabaMenu() {
		click(whatsappMenu);
	}

	public void clickOnCampaignsSubMenu() {
		click(campaignsSubMenu);
	}
	
//	public void clickOnCreateCampaignButton() {
//		campaignCommonComponent.clickOnCreateCampaignButton();
//	}
//	
//	public void selectCampaignCategory(String categoryText) {
//		campaignCommonComponent.selectCampaignCategory(categoryText);
//	}

	public void clickOnImportContacts() {
		click(wabaCampaignImportContacts);
	}

	public void clickOnListTab() {
		click(wabaCampaignListTab);
	}

	public void searchContactList(String listName) {
		type(wabaCampaignListSearchInput, listName);
	}

	public void selectContactList(String contactListName) {
		tableSelectionUtil.selectContactListByName(contactListName);
	}

	public void clickOnGoButton() {
		click(wabaCampaignListGoBtn);
	}

	public void clickOnImportButton() {
		click(wabaCampaignContactsImportBtn);
	}

	public void clickOnChooseTemplate() {
		click(wabaCampaignChooseTemplateBtn);
	}

	public void searchTemplate(String templateName) {
		click(wabaCampaingSearchTemplate);
		type(wabaCampaingSearchTemplate, templateName);
		wabaCampaingSearchTemplate.sendKeys(Keys.ENTER);
	}

	public void selectTemplate(String templateName) {
		tableSelectionUtil.selectTemplateByName(templateName);
	}

	public void clickOnSaveButton() {
		click(wabaCampaignTempmateSaveBtn);
	}

	public void clickOnSendButton() {
		click(wabaCampaingSendBtn);
	}

	public void clickOnSendNowButton() {
		click(wabaCampaignSendNowBtn);
	}

}