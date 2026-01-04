package com.quantumqa.utils;

import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.*;

public class ExcelDataReader {

	public static Object[][] getExcelData() {
		String filePath = ExcelDataReader.class.getClassLoader().getResource("testdata/userdata.xlsx").getPath();
		String sheetName = ConfigReader.get("excel_sheet_name");

		try (FileInputStream fis = new FileInputStream(new File(filePath));
				Workbook workbook = WorkbookFactory.create(fis)) {

			Sheet sheet = workbook.getSheet(sheetName);
			DataFormatter formatter = new DataFormatter();

			int lastRow = sheet.getLastRowNum();
			int colCount = sheet.getRow(0).getLastCellNum();

			int validRowCount = 0;
			for (int i = 1; i <= lastRow; i++) {
				Row row = sheet.getRow(i);
				if (row != null) {
					String username = getCellValue(row.getCell(0), formatter);
					String password = getCellValue(row.getCell(1), formatter);
					String view = getCellValue(row.getCell(2), formatter);

					if (!username.isBlank() && !password.isBlank() && !view.isBlank()) {
						validRowCount++;
					}
				}
			}

			Object[][] data = new Object[validRowCount][colCount];
			int dataIndex = 0;

			for (int i = 1; i <= lastRow; i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				String username = getCellValue(row.getCell(0), formatter);
				String password = getCellValue(row.getCell(1), formatter);
				String view = getCellValue(row.getCell(2), formatter);

				if (!username.isBlank() && !password.isBlank() && !view.isBlank()) {
					for (int j = 0; j < colCount; j++) {
						Cell cell = row.getCell(j);
						data[dataIndex][j] = (cell == null) ? "" : formatter.formatCellValue(cell);
					}
					dataIndex++;
				}
			}

			return data;

		} catch (Exception e) {
			throw new RuntimeException("Failed to read Excel data", e);
		}
	}

	private static String getCellValue(Cell cell, DataFormatter formatter) {
		if (cell == null)
			return "";
		return formatter.formatCellValue(cell).trim();
	}
}