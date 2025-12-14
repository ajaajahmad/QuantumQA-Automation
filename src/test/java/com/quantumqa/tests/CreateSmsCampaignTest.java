package com.quantumqa.tests;

import org.testng.annotations.Test;

import com.quantumqa.base.BaseTest;
import com.quantumqa.dataprovider.TestDataProvider;

public class CreateSmsCampaignTest extends BaseTest {

	@Test(dataProvider = "loginData", dataProviderClass = TestDataProvider.class)
	public void verifySmsCampaignCreation(String username, String password) {
		loginPage.userLogin(username, password);
		smsCampaignPage.clickOnSmsMenu();
		smsCampaignPage.clickOnCampaignsSubMenu();
		smsCampaignPage.acknowledgeDltPopupWindow();
		smsCampaignPage.clickOnCreateCampaignButton();
		smsCampaignPage.enterCampaignName("Selenium Flow");
		smsCampaignPage.clickOnCanpaignCategory();
		smsCampaignPage.selectCampaingCategory();
		smsCampaignPage.selectCampaignTyep();
	}
}
