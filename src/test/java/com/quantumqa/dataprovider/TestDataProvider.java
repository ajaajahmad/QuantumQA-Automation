package com.quantumqa.dataprovider;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.testng.annotations.DataProvider;

import com.quantumqa.utils.ExcelDataReader;
import com.quantumqa.utils.JsonDataReader;

public class TestDataProvider {

	@DataProvider(name = "excelUserData")
	public Object[][] excelUserData() {
		Object[][] allData = ExcelDataReader.getExcelData();

		Stream<Object[]> validDataStream = Stream.of(allData).filter(this::isValidLoginRow);

		Object[][] validData = validDataStream.toArray(Object[][]::new);

		if (validData.length == 0) {
			throw new RuntimeException("No valid login data found in Excel. Please check the Excel file.");
		}

		return validData;
	}

	private boolean isValidLoginRow(Object[] row) {
		if (row == null || row.length < 2) {
			return false;
		}
		Object usernameObj = row[0];
		if (usernameObj == null || usernameObj.toString().isBlank()) {
			return false;
		}
		Object passwordObj = row[1];
		if (passwordObj == null || passwordObj.toString().isBlank()) {
			return false;
		}

		Object viewObj = row[1];
		if (viewObj == null || viewObj.toString().isBlank()) {
			return false;
		}
		return true;
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
			dataArray[i][2] = userData.get("view");
		}

		return dataArray;
	}
}