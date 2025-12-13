package com.quantumqa.tests;

import org.testng.annotations.Test;

import com.quantumqa.base.BaseTest;
import com.quantumqa.dataprovider.TestDataProvider;

public class CreateSmsCampaignTest extends BaseTest {

	@Test(dataProvider = "loginData", dataProviderClass = TestDataProvider.class)
	public void verifySmsCampaignCreation(String username, String password, String campaingName) {
		loginPage.userLogin(username, password);
		smsCampaignPage.clickOnSmsMenu();
		smsCampaignPage.clickOnCampaignsSubMenu();
		smsCampaignPage.enterCampaignName("Selenium Flow");
		smsCampaignPage.clickONCanpaignCategory();
		smsCampaignPage.selectCampaignTyep();
	}
}
