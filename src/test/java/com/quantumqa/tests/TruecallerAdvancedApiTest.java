package com.quantumqa.tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.quantumqa.base.BaseTest;
import com.quantumqa.dataprovider.TestDataProvider;
import com.quantumqa.utils.DateTimeManager;

public class TruecallerAdvancedApiTest extends BaseTest {

	private String fileTitle;

	@BeforeTest
	public void generateScreenshotText() {
		fileTitle = DateTimeManager.appendLocalDateTime("truecaller_advanced_api");
	}

	@Test(groups = "user_login", dataProvider = "excelUserData", dataProviderClass = TestDataProvider.class)
	public void userLogin(String username, String password) throws IOException {
		try {
			loginPage.userLogin(username, password);
		} catch (IllegalArgumentException e) {
			Assert.fail("Login failed: " + e.getMessage());
		}
	}

	@Test()
	public void verifyTruecallerAdvancedApi() throws InterruptedException, IOException {

		truecallerAdvancedApiPage.openTruecallerAdvanced("2026-01-01", "2026-01-15");
		truecallerAdvancedApiPage.applyAllViewByOptions(fileTitle);

	}
}