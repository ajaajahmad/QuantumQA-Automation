package com.quantumqa.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.quantumqa.base.BaseTest;
import com.quantumqa.dataprovider.TestDataProvider;

public class ReportsTest extends BaseTest {

	@Test(groups = {
			"sms_summary" }, enabled = true, dataProvider = "excelUserData", dataProviderClass = TestDataProvider.class)
	public void verifySmsSummary(String username, String password) {
		try {
			loginPage.userLogin(username, password);
		} catch (IllegalArgumentException e) {
			Assert.fail("Login failed: " + e.getMessage());
		}

		smsSummaryPage.clickOnSmsSummary();
		smsSummaryPage.clickOnViewBy("Date");
	}
}
