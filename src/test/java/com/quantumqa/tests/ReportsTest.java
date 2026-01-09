package com.quantumqa.tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.quantumqa.base.BaseTest;
import com.quantumqa.dataprovider.TestDataProvider;
import com.quantumqa.utils.DateTimeManager;

public class ReportsTest extends BaseTest {

	private String screenshotTitle;

	@BeforeTest
	public void generateScreenshotText() {
		screenshotTitle = DateTimeManager.appendLocalDateTime("screenshot");
	}

	@Test(groups = "user_login", dataProvider = "excelUserData", dataProviderClass = TestDataProvider.class)
	public void userLogin(String username, String password) throws IOException {
		try {
			loginPage.userLogin(username, password);
		} catch (IllegalArgumentException e) {
			Assert.fail("Login failed: " + e.getMessage());
		}
	}

	@Test(groups = "sms_summary_viewby", dependsOnGroups = "user_login")
	public void verifySmsSummaryViewBy() throws InterruptedException, IOException {

		smsSummaryPage.openSmsSummary();
		smsSummaryPage.applyAllViewByOptions(screenshotTitle);

	}
}