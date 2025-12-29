package com.quantumqa.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeManager {

	private DateTimeManager() {

	}

	public static String getLocalDateTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
		return LocalDateTime.now().format(formatter);
	}

	public static String appendLocalDateTime(String text) {
		return text + "_" + getLocalDateTime();
	}
}