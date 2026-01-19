package com.quantumqa.base;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.quantumqa.pages.campaign.SmsCampaignPage;
import com.quantumqa.pages.campaign.WhatsAppCampaignPage;
import com.quantumqa.pages.components.LogoutComponent;
import com.quantumqa.pages.login.ErrorValidationPage;
import com.quantumqa.pages.login.LoginPage;
import com.quantumqa.pages.mis.RcsAdvancedApiPage;
import com.quantumqa.pages.mis.RcsAdvancedPanelPage;
import com.quantumqa.pages.mis.RcsSummaryApiPage;
import com.quantumqa.pages.mis.RcsSummaryPanelPage;
import com.quantumqa.pages.mis.SmsAdvancedApiPage;
import com.quantumqa.pages.mis.SmsAdvancedPanelPage;
import com.quantumqa.pages.mis.SmsSummaryApiPage;
import com.quantumqa.pages.mis.SmsSummaryPanelPage;
import com.quantumqa.pages.mis.TruecallerAdvancedApiPage;
import com.quantumqa.pages.mis.TruecallerAdvancedPanelPage;
import com.quantumqa.pages.mis.TruecallerSummaryApiPage;
import com.quantumqa.pages.mis.TruecallerSummaryPanelPage;
import com.quantumqa.pages.mis.WhatsAppAdvancedApiPage;
import com.quantumqa.pages.mis.WhatsAppAdvancedPanelPage;
import com.quantumqa.pages.mis.WhatsAppSummaryApiPage;
import com.quantumqa.pages.mis.WhatsAppSummaryPanelPage;
import com.quantumqa.utils.ConfigReader;

public class BaseTest {

	static {
		LogManager.getLogManager().reset();
		Logger.getLogger("").setLevel(Level.SEVERE);
	}

	protected WebDriver driver;
	protected LoginPage loginPage;
	protected ErrorValidationPage errorValidationPage;
	protected SmsCampaignPage smsCampaignPage;
	protected WhatsAppCampaignPage wabaCampaignPage;
	protected LogoutComponent logoutComponent;
	protected SmsSummaryPanelPage smsSummaryPanelPage;
	protected SmsSummaryApiPage smsSummaryApiPage;
	protected SmsAdvancedPanelPage smsAdvancedPanelPage;
	protected SmsAdvancedApiPage smsAdvancedApiPage;
	protected WhatsAppSummaryPanelPage whatsAppSummaryPanelPage;
	protected WhatsAppSummaryApiPage whatsAppSummaryApiPage;
	protected WhatsAppAdvancedPanelPage whatsAppAdvancedPanelPage;
	protected WhatsAppAdvancedApiPage whatsAppAdvancedApiPage;
	protected TruecallerSummaryPanelPage truecallerSummaryPanelPage;
	protected TruecallerSummaryApiPage truecallerSummaryApiPage;
	protected TruecallerAdvancedPanelPage truecallerAdvancedPanelPage;
	protected TruecallerAdvancedApiPage truecallerAdvancedApiPage;
	protected RcsSummaryPanelPage rcsSummaryPanelPage;
	protected RcsSummaryApiPage rcsSummaryApiPage;
	protected RcsAdvancedPanelPage rcsAdvancedPanelPage;
	protected RcsAdvancedApiPage rcsAdvancedApiPage;

	@BeforeSuite(alwaysRun = true)
	public void setUp() {

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

		driver.get(ConfigReader.get("app_url"));

		loginPage = new LoginPage(driver);
		errorValidationPage = new ErrorValidationPage(driver);
		smsCampaignPage = new SmsCampaignPage(driver);
		wabaCampaignPage = new WhatsAppCampaignPage(driver);
		logoutComponent = new LogoutComponent(driver);
		smsSummaryPanelPage = new SmsSummaryPanelPage(driver);
		smsSummaryApiPage = new SmsSummaryApiPage(driver);
		smsAdvancedPanelPage = new SmsAdvancedPanelPage(driver);
		smsAdvancedApiPage = new SmsAdvancedApiPage(driver);
		whatsAppSummaryPanelPage = new WhatsAppSummaryPanelPage(driver);
		whatsAppSummaryApiPage = new WhatsAppSummaryApiPage(driver);
		whatsAppAdvancedPanelPage = new WhatsAppAdvancedPanelPage(driver);
		whatsAppAdvancedApiPage = new WhatsAppAdvancedApiPage(driver);
		truecallerSummaryPanelPage = new TruecallerSummaryPanelPage(driver);
		truecallerSummaryApiPage = new TruecallerSummaryApiPage(driver);
		truecallerAdvancedPanelPage = new TruecallerAdvancedPanelPage(driver);
		truecallerAdvancedApiPage = new TruecallerAdvancedApiPage(driver);
		rcsSummaryPanelPage = new RcsSummaryPanelPage(driver);
		rcsSummaryApiPage = new RcsSummaryApiPage(driver);
		rcsAdvancedPanelPage = new RcsAdvancedPanelPage(driver);
		rcsAdvancedApiPage = new RcsAdvancedApiPage(driver);
	}

	@AfterSuite(alwaysRun = true)
	public void tearDown() {
		try {
			if (logoutComponent != null) {
				logoutComponent.userLogout();
			}
		} finally {
			if (driver != null) {
				driver.quit();
			}
		}
	}
}