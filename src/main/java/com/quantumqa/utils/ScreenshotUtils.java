package com.quantumqa.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtils {

	public String getScreenshot(WebDriver driver, String datetime) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);

		String path = System.getProperty("user.dir") + "//reports//screenshots//" + datetime + ".png";
		File destination = new File(path);
		FileUtils.copyFile(source, destination);

		return path;
	}

}