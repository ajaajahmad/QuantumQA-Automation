package com.quantumqa.pages.whatsapp;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import com.quantumqa.base.BasePage;
import com.quantumqa.pages.components.CampaignCommonComponent;
import com.quantumqa.pages.components.MainMenuComponent;
import com.quantumqa.utils.TableSelectionManager;

public class WhatsAppCampaignPage extends BasePage {

	private TableSelectionManager tableSelectionManager;
	private MainMenuComponent mainMenuComponent;
	private CampaignCommonComponent campaignCommonComponent;

	public WhatsAppCampaignPage(WebDriver driver) {
		super(driver);
		this.tableSelectionManager = new TableSelectionManager(driver, wait);
		this.campaignCommonComponent = new CampaignCommonComponent(driver, wait);
		this.mainMenuComponent = new MainMenuComponent(driver);
	}

	@FindBy(xpath = "//div[contains(@class, 'menu-event') and .//span[contains(@class, 'icon-Menu_WhatsApp')]]")
	private WebElement whatsappMenu;

	@FindBy(xpath = "//span[normalize-space()='Templates']/ancestor::li[1]/preceding-sibling::li[1]//span[normalize-space()='Campaigns']")
	private WebElement campaignsSubMenu;

	@FindBy(css = ".wa-business-number")
	private WebElement wabaNumberDropdown;

	@FindBy(xpath = "//span[normalize-space()='+91-8448098743 (TestDG)']")
	private WebElement chooseWabaNumber;

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

	@FindBy(xpath = "//span[@class='ellipsis-text custom-placement-ltr'][contains(text(),'Select')]")
	private WebElement selectLanguageDropdown;

	@FindBy(xpath = "//div[contains(@class, 'drop-item') and .//span[contains(text(), 'English')]]")
	private WebElement selectEnglishOption;

	@FindBy(xpath = "//a[contains(@class,'variable-text')][1]")
	private WebElement variable;

	@FindBy(xpath = "//span[contains(text(),'Personalise')]")
	private WebElement personaliseText;

	@FindBy(xpath = "//span[normalize-space()='All Variables']")
	private WebElement allVariableText;

	@FindBy(xpath = "//input[@placeholder='Static text goes here']")
	private WebElement variableInputBox;

	@FindBy(xpath = "//button[normalize-space()='Save' and not(contains(@class,'import-btn'))]")
	private WebElement saveButton;

	@FindBy(css = "button.btn-design.btn-send")
	private WebElement campaignSendButton;

	@FindBy(xpath = "//button[@type='button' and contains(normalize-space(.),'Send Now')]")
	private WebElement campaignSendNowButton;

	public void clickOnWabaMenu() {
		mainMenuComponent.clickMainMenu("WhatsApp");
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

	public void selectWhatsappBusinessNumber() {
		click(wabaNumberDropdown);
		click(chooseWabaNumber);
	}

	public void importContactsList(String listName, String contactListName) {
		click(importContactsButton);
		click(contactsListTab);
		type(contactsListSearchInput, listName);
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

	public void searchTemplate(String templateName) throws InterruptedException {
		click(searchTemplate);
		type(searchTemplate, templateName);
		searchTemplate.sendKeys(Keys.ENTER);
		Thread.sleep(2000);
	}

	public void selectTemplate(String templateName) {
		tableSelectionManager.selectTemplateByName(templateName);
	}

	public void clickOnSaveButton() {
		click(templateSaveButton);
	}

	public void selectLanguage() {
		click(selectLanguageDropdown);
		click(selectEnglishOption);

	}

	public void variablePersonalisation(String value) throws InterruptedException {
		Actions action = new Actions(driver);
		action.contextClick(variable).perform();
		action.moveToElement(personaliseText).perform();
		Thread.sleep(3000);
		action.moveToElement(allVariableText).click().perform();
		type(variableInputBox, value);
		click(saveButton);

	}

	public void clickOnSendButton() {
		click(campaignSendButton);
	}

	public void clickOnSendNowButton() {
		click(campaignSendNowButton);
	}

}