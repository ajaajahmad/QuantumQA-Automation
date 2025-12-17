package com.quantumqa.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.quantumqa.base.BaseTest;
import com.quantumqa.dataprovider.TestDataProvider;
import com.quantumqa.utils.DateTimeUtils;

public class CreateSmsCampaignTest extends BaseTest {

	private String campaignName;

	@BeforeMethod
	public void generateCampaignName() {
		campaignName = DateTimeUtils.appendLocalDateTime("Selenium_Flow");
	}

	@Test(dataProvider = "loginData", dataProviderClass = TestDataProvider.class)
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
		smsCampaignPage.clickOnCampaignCategory();
		smsCampaignPage.selectCampaignCategory();
		smsCampaignPage.selectCampaignType();
		smsCampaignPage.clickOnImportContacts();
		smsCampaignPage.clickOnListTab();
		smsCampaignPage.enterListName("Automation");
		smsCampaignPage.selectListByName("Automation");
		smsCampaignPage.clickOnGoButton();

		boolean isSelected = smsCampaignPage.isListSelected("Automation");
		Assert.assertTrue(isSelected, "List 'Automation' is not selected.");

		smsCampaignPage.clickOnImportButton();
		smsCampaignPage.clickOnChooseTemplate();
		smsCampaignPage.searchTemplate("Order Confirmation");
		smsCampaignPage.chooseTemplateByName("Order Confirmation");
		smsCampaignPage.clickOnSaveButton();
		smsCampaignPage.clickOnSendButton();
		smsCampaignPage.clickOnSendNowButton();
	}
}