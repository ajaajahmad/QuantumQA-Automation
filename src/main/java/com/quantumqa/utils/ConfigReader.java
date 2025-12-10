package com.quantumqa.utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

	private static Properties prop;

	static {
		try {
			prop = new Properties();
			InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties");
			prop.load(inputStream);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load config.properties file", e);
		}
	}

	public static String get(String key) {
		return prop.getProperty(key);
	}
}