package com.quantumqa.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.quantumqa.base.BaseTest;
import com.quantumqa.dataprovider.TestDataProvider;
import com.quantumqa.utils.DateTimeUtils;

public class CreateCampaignTest extends BaseTest {

	private String campaignName;

	@BeforeMethod
	public void generateCampaignName() {
		campaignName = DateTimeUtils.appendLocalDateTime("Selenium_Flow");
	}

	@Test(groups = {
			"sms campaign" }, enabled = false, dataProvider = "loginData", dataProviderClass = TestDataProvider.class)
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
		smsCampaignPage.searchContactList("Automation");
		smsCampaignPage.selectContactList("Automation");
		smsCampaignPage.clickOnGoButton();
		smsCampaignPage.clickOnImportButton();
		smsCampaignPage.clickOnChooseTemplate();
		smsCampaignPage.searchTemplate("Order Confirmation");
		smsCampaignPage.selectTemplate("Order Confirmation");
		smsCampaignPage.clickOnSaveButton();
		smsCampaignPage.clickOnSendButton();
		smsCampaignPage.clickOnSendNowButton();
	}

	@Test(groups = { "whatsapp campaign" }, dataProvider = "loginData", dataProviderClass = TestDataProvider.class)
	public void verifyWhatsappCampaignCreation(String username, String password) {
		try {
			loginPage.userLogin(username, password);
		} catch (IllegalArgumentException e) {
			Assert.fail("Login failed: " + e.getMessage());
		}

		wabaCampaignPage.clickOnWabaMenu();
		wabaCampaignPage.clickOnCampaignsSubMenu();
		wabaCampaignPage.clickOnCreateCampaignButton();
		wabaCampaignPage.enterCampaignName(campaignName);
		wabaCampaignPage.selectCampaignCategory("20122025");
		wabaCampaignPage.selectWhatsappBusinessNumber();
		wabaCampaignPage.importContactsList("Automation", "Automation");
		wabaCampaignPage.clickOnGoButton();
		wabaCampaignPage.clickOnImportButton();
		wabaCampaignPage.clickOnChooseTemplate();
		wabaCampaignPage.searchTemplate("sample_template_010");
		wabaCampaignPage.selectTemplate("sample_template_010");
		wabaCampaignPage.clickOnSaveButton();
		wabaCampaignPage.selectLanguage();
		wabaCampaignPage.clickOnSendButton();
		wabaCampaignPage.clickOnSendNowButton();
	}

	@Test(groups = {
			"rcs campaign" }, enabled = false, dataProvider = "loginData", dataProviderClass = TestDataProvider.class)
	public void verifyRcsCampaignCreation(String username, String password) {
		try {
			loginPage.userLogin(username, password);
		} catch (IllegalArgumentException e) {
			Assert.fail("Login failed: " + e.getMessage());
		}

	}

	@Test(groups = {
			"truecaller campaign" }, enabled = false, dataProvider = "loginData", dataProviderClass = TestDataProvider.class)
	public void verifyTruecallerCampaignCreation(String username, String password) {
		try {
			loginPage.userLogin(username, password);
		} catch (IllegalArgumentException e) {
			Assert.fail("Login failed: " + e.getMessage());
		}

	}
}