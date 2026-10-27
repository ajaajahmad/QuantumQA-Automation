package com.quantumqa.pages.campaign;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import com.quantumqa.base.BasePage;
import com.quantumqa.pages.components.CampaignCommonComponent;
import com.quantumqa.pages.components.MenuComponent;
import com.quantumqa.utils.TableSelectionManager;

public class WhatsAppCampaignPage extends BasePage {

	private TableSelectionManager tableSelectionManager;
	private MenuComponent menu;
	private CampaignCommonComponent campaignCommonComponent;

	public WhatsAppCampaignPage(WebDriver driver) {
		super(driver);
		this.tableSelectionManager = new TableSelectionManager(driver);
		this.campaignCommonComponent = new CampaignCommonComponent(driver);
		this.menu = new MenuComponent(driver);
	}

	private By wabaNumberDropdown = By.cssSelector(".wa-business-number");
	private By chooseWabaNumber = By.xpath("//span[normalize-space()='+91-8448098743 (TestDG)']");
	private By importContactsButton = By.xpath("//button[normalize-space()='Import Contacts']");
	private By contactsListTab = By
			.xpath("//span[contains(@class,'mdc-tab__text-label') and normalize-space()='Lists']");
	private By contactsListSearchInput = By.xpath("//input[@placeholder='Search List']");
	private By contactsListGoButton = By.xpath("//button[normalize-space()='Go']");
	private By contactsImportButton = By
			.xpath("//button[contains(@class,'process-file') and normalize-space()='Import']");
	private By chooseTemplateButton = By
			.xpath("//button[contains(@class,'btn-design') and normalize-space()='Choose Template']");
	private By searchTemplate = By.xpath("//input[contains(@class,'filter-search')]");
	private By templateSaveButton = By.cssSelector("button.import-btn.btn-design");
	// private By templateCancelButton =
	// By.cssSelector("button.import-btn.btn-design-white");
	private By selectLanguageDropdown = By
			.xpath("//span[contains(@class,'ellipsis-text') and normalize-space()='Select']");
	private By selectEnglishOption = By.xpath("//div[contains(@class,'drop-item')]//span[normalize-space()='English']");
	private By variable = By.xpath("(//a[contains(@class,'variable-text')])[1]");
	private By personaliseText = By.xpath("//span[normalize-space()='Personalise']");
	private By allVariableText = By.xpath("//span[normalize-space()='All Variables']");
	private By variableInputBox = By.xpath("//input[@placeholder='Static text goes here']");
	private By saveButton = By.xpath("//button[normalize-space()='Save' and not(contains(@class,'import-btn'))]");
	private By campaignSendButton = By.cssSelector("button.btn-design.btn-send");
	private By campaignSendNowButton = By.xpath("//button[@type='button' and normalize-space()='Send Now']");

	public void clickOnWabaMenu() {
		menu.navigate("WhatsApp");
	}

	public void clickOnCampaignsSubMenu() {
		menu.navigate("WhatsApp", "Campaigns");
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

	public void searchTemplate(String templateName) {
		typeAndEnter(searchTemplate, templateName);
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

	public void variablePersonalisation(String value) {
		Actions actions = new Actions(driver);

		actions.contextClick(waitForVisible(variable)).perform();
		sleep();
		actions.moveToElement(waitForVisible(personaliseText)).perform();
		sleep();
		actions.moveToElement(waitForVisible(allVariableText)).click().perform();

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