package com.quantumqa.utils;

import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.*;

public class ExcelUtils {
	public static Object[][] getExcelData() {

		String filePath = ExcelUtils.class.getClassLoader().getResource("testdata/userdata.xlsx").getPath();

		String sheetName = ConfigReader.get("excel_sheet_name");

		try (FileInputStream fis = new FileInputStream(new File(filePath));
				Workbook workbook = WorkbookFactory.create(fis)) {

			Sheet sheet = workbook.getSheet(sheetName);
			DataFormatter formatter = new DataFormatter();

			int lastRow = sheet.getLastRowNum();
			int colCount = sheet.getRow(0).getLastCellNum();

			int actualRowCount = 0;
			for (int i = 1; i <= lastRow; i++) {
				if (sheet.getRow(i) != null) {
					actualRowCount++;
				}
			}

			Object[][] data = new Object[actualRowCount][colCount];
			int dataIndex = 0;

			for (int i = 1; i <= lastRow; i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				for (int j = 0; j < colCount; j++) {
					Cell cell = row.getCell(j);
					data[dataIndex][j] = (cell == null) ? "" : formatter.formatCellValue(cell);
				}
				dataIndex++;
			}

			return data;

		} catch (Exception e) {
			throw new RuntimeException("Failed to read Excel data", e);
		}
	}
}