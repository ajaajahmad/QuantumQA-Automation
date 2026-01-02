package com.quantumqa.base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.quantumqa.pages.components.LogoutComponent;
import com.quantumqa.pages.login.ErrorValidationPage;
import com.quantumqa.pages.login.LoginPage;
import com.quantumqa.pages.reports.SmsSummaryPage;
import com.quantumqa.pages.sms.SmsCampaignPage;
import com.quantumqa.pages.whatsapp.WhatsAppCampaignPage;
import com.quantumqa.utils.ConfigReader;

public class BaseTest {

	protected WebDriver driver;
	protected LoginPage loginPage;
	protected ErrorValidationPage errorValidationPage;
	protected SmsCampaignPage smsCampaignPage;
	protected WhatsAppCampaignPage wabaCampaignPage;
	protected LogoutComponent logoutComponent;
	protected SmsSummaryPage smsSummaryPage;

	@BeforeSuite
	public void setUp() {
		try {

			String browserName = ConfigReader.get("browser");

			if (browserName.equalsIgnoreCase("chrome")) {
				driver = new ChromeDriver();
			} else if (browserName.equalsIgnoreCase("firefox")) {
				driver = new FirefoxDriver();
			} else if (browserName.equalsIgnoreCase("edge")) {
				driver = new EdgeDriver();
			} else {
				throw new RuntimeException("Browser not supported: " + browserName);
			}

			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));

			String appUrl = ConfigReader.get("app_url");
			driver.get(appUrl);

			loginPage = new LoginPage(driver);
			errorValidationPage = new ErrorValidationPage(driver);
			smsCampaignPage = new SmsCampaignPage(driver);
			wabaCampaignPage = new WhatsAppCampaignPage(driver);
			logoutComponent = new LogoutComponent(driver);
			smsSummaryPage = new SmsSummaryPage(driver);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@AfterSuite
	public void tearDown() {
		logoutComponent.userLogout();
		if (driver != null) {
			driver.quit();
		}
	}
}