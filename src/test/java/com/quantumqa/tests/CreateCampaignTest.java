package com.quantumqa.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.quantumqa.base.BaseTest;
import com.quantumqa.dataprovider.TestDataProvider;
import com.quantumqa.utils.ConfigReader;
import com.quantumqa.utils.DateTimeManager;

public class CreateCampaignTest extends BaseTest {

	private String campaignName;
	private String smsListName = ConfigReader.get("sms_list_name");
	private String smsTemplateName = ConfigReader.get("sms_template_name");
	private String wabaListName = ConfigReader.get("waba_list_name");
	private String wabaTemplateName = ConfigReader.get("waba_template_name");

	@BeforeMethod
	public void generateCampaignName() {
		campaignName = DateTimeManager.appendLocalDateTime("Selenium_Flow");
	}

	@Test(groups = {
			"sms_campaign" }, enabled = false, dataProvider = "excelUserData", dataProviderClass = TestDataProvider.class)
	public void verifySmsCampaignCreation(String username, String password) {

		try {
			loginPage.userLogin(username, password);
		} catch (IllegalArgumentException e) {
			Assert.fail("Login failed: " + e.getMessage());
		}

		smsCampaignPage.clickOnSmsMenu();
		smsCampaignPage.clickOnCampaignsSubMenu();
		smsCampaignPage.acknowledgeDltPopupWindow();
		smsCampaignPage.clickOnCreateCampaignButton();
		smsCampaignPage.enterCampaignName(campaignName);
		smsCampaignPage.selectCampaignCategory("automation");
		smsCampaignPage.selectCampaignType();
		smsCampaignPage.clickOnImportContacts();
		smsCampaignPage.clickOnListTab();
		smsCampaignPage.searchContactList(smsListName);
		smsCampaignPage.selectContactList(smsListName);
		smsCampaignPage.clickOnGoButton();
		smsCampaignPage.clickOnImportButton();
		smsCampaignPage.clickOnChooseTemplate();
		smsCampaignPage.searchTemplate(smsTemplateName);
		smsCampaignPage.selectTemplate(smsTemplateName);
		smsCampaignPage.clickOnSaveButton();
		smsCampaignPage.clickOnSendButton();
		smsCampaignPage.clickOnSendNowButton();
	}

	@Test(groups = {
			"whatsapp_campaign" }, enabled = false, dataProvider = "excelUserData", dataProviderClass = TestDataProvider.class)
	public void verifyWhatsappCampaignCreation(String username, String password) throws InterruptedException {
		try {
			loginPage.userLogin(username, password);
		} catch (IllegalArgumentException e) {
			Assert.fail("Login failed: " + e.getMessage());
		}

		wabaCampaignPage.clickOnWabaMenu();
		wabaCampaignPage.clickOnCampaignsSubMenu();
		wabaCampaignPage.clickOnCreateCampaignButton();
		wabaCampaignPage.enterCampaignName(campaignName);
		wabaCampaignPage.selectCampaignCategory("automation");
		wabaCampaignPage.selectWhatsappBusinessNumber();
		wabaCampaignPage.importContactsList(wabaListName, wabaListName);
		wabaCampaignPage.clickOnGoButton();
		wabaCampaignPage.clickOnImportButton();
		wabaCampaignPage.clickOnChooseTemplate();
		wabaCampaignPage.searchTemplate(wabaTemplateName);
		wabaCampaignPage.selectTemplate(wabaTemplateName);
		wabaCampaignPage.clickOnSaveButton();
		wabaCampaignPage.selectLanguage();
		wabaCampaignPage.variablePersonalisation("test");
		wabaCampaignPage.clickOnSendButton();
		wabaCampaignPage.clickOnSendNowButton();
	}

	@Test(groups = {
			"rcs_campaign" }, enabled = false, dataProvider = "loginData", dataProviderClass = TestDataProvider.class)
	public void verifyRcsCampaignCreation(String username, String password) {
		try {
			loginPage.userLogin(username, password);
		} catch (IllegalArgumentException e) {
			Assert.fail("Login failed: " + e.getMessage());
		}

	}

	@Test(groups = {
			"truecaller_campaign" }, enabled = false, dataProvider = "loginData", dataProviderClass = TestDataProvider.class)
	public void verifyTruecallerCampaignCreation(String username, String password) {
		try {
			loginPage.userLogin(username, password);
		} catch (IllegalArgumentException e) {
			Assert.fail("Login failed: " + e.getMessage());
		}

	}
}