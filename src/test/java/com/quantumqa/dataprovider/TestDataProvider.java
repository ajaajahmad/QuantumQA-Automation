package com.quantumqa.dataprovider;

import org.testng.annotations.DataProvider;

import com.quantumqa.utils.ExcelUtils;

public class TestDataProvider {

	@DataProvider(name = "loginData")
	public Object[][] loginData() {
		return ExcelUtils.getExcelData();
	}

}