package com.quantumqa.dataprovider;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.DataProvider;

import com.quantumqa.utils.ExcelDataReader;
import com.quantumqa.utils.JsonDataReader;

public class TestDataProvider {

	@DataProvider(name = "excelUserData")
	public Object[][] excelUserData() {
		Object[][] data = ExcelDataReader.getExcelData();

		if (data.length == 0) {
			throw new RuntimeException("No valid login data found in Excel. Please check the Excel file.");
		}
		return data;
	}

	@DataProvider(name = "smsSummaryViewPanelData")
	public Object[][] smsSummaryViewPanelData() {
		Object[][] data = ExcelDataReader.getSmsSummaryViewPanelData();

		if (data.length == 0) {
			throw new RuntimeException("No valid SMS summary view panel data found in Excel.");
		}
		return data;
	}

	@DataProvider(name = "jsonUserData")
	public Object[][] getJsonUserData() throws IOException {

		JsonDataReader jsonReader = new JsonDataReader();
		String filePath = System.getProperty("user.dir") + "/src/test/resources/testdata/userdata.json";

		List<HashMap<String, String>> dataList = jsonReader.getJsonDataToMap(filePath);

		Object[][] dataArray = new Object[dataList.size()][2];

		for (int i = 0; i < dataList.size(); i++) {
			HashMap<String, String> userData = dataList.get(i);
			dataArray[i][0] = userData.get("username");
			dataArray[i][1] = userData.get("password");
		}

		return dataArray;
	}
}