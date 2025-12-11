package com.quantumqa.base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.quantumqa.pages.ErrorValidationPage;
import com.quantumqa.pages.LoginPage;
import com.quantumqa.utils.ConfigReader;

public class BaseTest {

	protected WebDriver driver;
	protected LoginPage loginPage;
	protected ErrorValidationPage errorValidationPage;

	@BeforeSuite
	public void setUp() {
		try {
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));

			String appUrl = ConfigReader.get("app_url");
			driver.get(appUrl);

			loginPage = new LoginPage(driver);
			errorValidationPage = new ErrorValidationPage(driver);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@AfterSuite
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
