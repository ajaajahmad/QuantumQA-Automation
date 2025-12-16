package com.quantumqa.dataprovider;

import org.testng.annotations.DataProvider;
import com.quantumqa.utils.ExcelUtils;
import java.util.stream.Stream;

public class TestDataProvider {

	@DataProvider(name = "loginData")
	public Object[][] loginData() {
		Object[][] allData = ExcelUtils.getExcelData();

		Stream<Object[]> validDataStream = Stream.of(allData).filter(this::isValidLoginRow);

		Object[][] validData = validDataStream.toArray(Object[][]::new);

		if (validData.length == 0) {
			throw new RuntimeException(
					"No valid login data found in Excel. Please check the Excel file and ensure username and password are not blank.");
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

		return true;
	}
}