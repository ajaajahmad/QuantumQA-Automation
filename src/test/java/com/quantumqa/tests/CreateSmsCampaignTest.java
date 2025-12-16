package com.quantumqa.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.quantumqa.base.BaseTest;
import com.quantumqa.dataprovider.TestDataProvider;

public class CreateSmsCampaignTest extends BaseTest {

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
		smsCampaignPage.enterCampaignName("Selenium Flow");
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
	}
}