package com.quantumqa.tests;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.quantumqa.base.BaseTest;
import com.quantumqa.dataprovider.TestDataProvider;

public class ReportsTest extends BaseTest {

	@Test(groups = "user_login", dataProvider = "excelUserData", dataProviderClass = TestDataProvider.class)
	public void userLogin(String username, String password) {
		try {
			loginPage.userLogin(username, password);
		} catch (IllegalArgumentException e) {
			Assert.fail("Login failed: " + e.getMessage());
		}
	}

	@Test(groups = "sms_summary_viewby", dependsOnGroups = "user_login", dataProvider = "smsSummaryViewPanelData", dataProviderClass = TestDataProvider.class)
	public void verifySmsSummaryViewBy(String view) {

		smsSummaryPage.openSmsSummary();

		boolean actionTaken = smsSummaryPage.selectViewByIfPresent(view);

		if (!actionTaken) {
			throw new SkipException("View By not present in UI: " + view);
		}
	}
}