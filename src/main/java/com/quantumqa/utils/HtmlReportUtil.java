package com.quantumqa.utils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public final class HtmlReportUtil {

	private HtmlReportUtil() {
	}

	public static void createHtmlTable(WebElement table, String title, String outputDir, String filePrefix)
			throws IOException {

		createDir(outputDir);

		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

		String filePath = outputDir + "/" + filePrefix + "_" + timestamp + ".html";

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			writer.write(buildHtml(table, title));
		}
	}

	private static String buildHtml(WebElement table, String title) {
		StringBuilder html = new StringBuilder("""
				    <html><head>
				    <style>
				        table { border-collapse: collapse; width: 100%; }
				        th, td { border: 1px solid #333; padding: 8px; }
				        th { background: #f2f2f2; }
				    </style>
				    </head><body>
				""");

		html.append("<h2>").append(title).append("</h2>");
		html.append("<table>");

		List<WebElement> rows = table.findElements(By.tagName("tr"));
		for (WebElement row : rows) {
			html.append("<tr>");
			for (WebElement cell : row.findElements(By.xpath("./th|./td"))) {
				html.append("<").append(cell.getTagName()).append(">").append(cell.getText()).append("</")
						.append(cell.getTagName()).append(">");
			}
			html.append("</tr>");
		}

		html.append("</table></body></html>");
		return html.toString();
	}

	private static void createDir(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
}