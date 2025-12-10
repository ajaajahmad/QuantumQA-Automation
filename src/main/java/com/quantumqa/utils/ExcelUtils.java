package com.quantumqa.utils;

import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.*;
import org.testng.annotations.Test;

public class ExcelUtils {

	@Test
	public static Object[][] getExcelData(String filePath, String sheetName) {

		filePath = ExcelUtils.class.getClassLoader().getResource("testdata/userdata.xlsx").getPath();

		sheetName = ConfigReader.get("excel_sheet_name");

		Object[][] data = null;

		try {
			FileInputStream fis = new FileInputStream(new File(filePath));
			Workbook workbook = WorkbookFactory.create(fis);
			Sheet sheet = workbook.getSheet(sheetName);

			int rowCount = sheet.getLastRowNum();

			int colCount = sheet.getRow(0).getLastCellNum();

			data = new Object[rowCount][colCount];

			DataFormatter formatter = new DataFormatter();

			for (int i = 1; i <= rowCount; i++) {
				Row row = sheet.getRow(i);

				for (int j = 0; j < colCount; j++) {
					Cell cell = row.getCell(j);

					if (cell != null) {
						data[i - 1][j] = formatter.formatCellValue(cell);
					} else {
						data[i - 1][j] = "";
					}
				}
			}

			workbook.close();
			fis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}
}