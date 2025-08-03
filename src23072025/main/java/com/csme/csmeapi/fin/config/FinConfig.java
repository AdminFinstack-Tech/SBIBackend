package com.csme.csmeapi.fin.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.cs.base.log.CSLogger;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.cs.eximap.log.ASCELog;
import com.cs.eximap.log.CELogUtility;

@Component
public class FinConfig {
	static String secuDs = null;
	public static final CSLogger logger = ASCELog.getCELogger(CELogUtility.getLoggerWithClassName(FinConfig.class, "CSMEMobile"));

	public FinConfig() {
		secuDs = DSManager.getSecuDS();
	}

	public static String getPropName(String propName) {
		String getValue = null;
		try {
			getValue = fetchFilePathFromDatabase(propName);
		}catch (Exception e) {
			logger.info("Exception has occured reterive prop name "+e.getMessage());
			e.printStackTrace();
		}
		return getValue;
	}

	public static String fetchFilePathFromDatabase(String string) throws Exception {
		secuDs = DSManager.getSecuDS();
		String filePath = null;
		String query = "SELECT PROP_VALUE FROM CEUSER.SEC_MOBILE_DTAL WHERE PROP_KEY = '"+string+"' ";
		SQLRecordSet rs = WSDBHelper.executeQuery(query, secuDs);
		if (rs.getRecordCount() > 0) {
			logger.info("Retervie value from database count is :" + rs.getRecordCount());
			for (int i = 0, len = rs.getRecordCount(); i < len; i++) {
				SQLRecord record = rs.getSQLRecord(i);
				filePath = record.getValue("PROP_VALUE");
			}
		} else {
			filePath = getCustomProperty(string);
		}
		return filePath;
	}

	public static Properties readFileProperties(String filePath) throws IOException {
		String intPath = convertPath(FinConfig.getPropName("CONSTANT_PATH"));
		Properties properties = new Properties();
		try (InputStream is = new FileInputStream(intPath + filePath)) {
			if (filePath.endsWith(".properties")) {
				properties.load(is);
			} else if (filePath.endsWith(".xml")) {
				properties.loadFromXML(is);
			} else if (filePath.endsWith(".yaml") || filePath.endsWith(".yml")) {
				Yaml yaml = new Yaml();
				Object yamlObject = yaml.load(is);
				if (yamlObject instanceof Properties) {
					properties.putAll((Properties) yamlObject);
				} else if (yamlObject instanceof Iterable) {
					for (Object item : (Iterable<?>) yamlObject) {
						if (item instanceof Properties) {
							properties.putAll((Properties) item);
						}
					}
				} else {
					throw new IOException("Unsupported YAML structure: " + filePath);
				}
				System.out.println("Reading YAML properties...");
			} else {
				throw new IOException("Unsupported file extension: " + filePath);
			}
		}
		return properties;
	}

	public static Properties fetchPropertiesFromDatabase() throws Exception {
		secuDs = DSManager.getSecuDS();
		Properties properties = new Properties();
		String query = "SELECT PROP_KEY,PROP_VALUE FROM CEUSER.SEC_MOBILE_DTAL";
		SQLRecordSet rs = WSDBHelper.executeQuery(query, secuDs);
		if (rs.getRecordCount() > 0) {
			logger.info("Retervie value from database count is :" + rs.getRecordCount());
			for (int i = 0, len = rs.getRecordCount(); i < len; i++) {
				SQLRecord record = rs.getSQLRecord(i);
				String key = record.getValue("PROP_KEY");
				String value = record.getValue("PROP_VALUE");
				properties.setProperty(key, value);
			}
		}
		return properties;
	}

	public static Map<String,String> getApplicationProperty()
	{
		Properties properties = new Properties();
		Map<String,String> propertiesMap = new HashMap<String,String>();
		try
		{
			String filePath = getPropName("CUSTOM_CONFIG");
			FileInputStream is = new FileInputStream(FinUtil.intPath+filePath);
			if (filePath.endsWith(".properties")) {
				properties.load(is);
			} else if (filePath.endsWith(".xml")) {
				properties.loadFromXML(is);
			} else if (filePath.endsWith(".yaml") || filePath.endsWith(".yml")) {
				Yaml yaml = new Yaml();
				Object yamlObject = yaml.load(is);
				if (yamlObject instanceof Properties) {
					properties.putAll((Properties) yamlObject);
				} else if (yamlObject instanceof Iterable) {
					for (Object item : (Iterable<?>) yamlObject) {
						if (item instanceof Properties) {
							properties.putAll((Properties) item);
						}
					}
				} else {
					throw new IOException("Unsupported YAML structure: " + filePath);
				}
				System.out.println("Reading YAML properties...");
			} else {
				throw new IOException("Unsupported file extension: " + filePath);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Some issue finding or loading file....!!! " + e.getMessage());

		}

		// Convert Properties to a Map
		propertiesMap = new HashMap<>();
		for (String key : properties.stringPropertyNames()) {
			propertiesMap.put(key, properties.getProperty(key));
		}

		// Print the map to verify
		for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
			System.out.println(entry.getKey() + " = " + entry.getValue());
		}

		return propertiesMap;
	}

	public static String convertPath(String propValue) {
		return propValue.replace("\\", "/");
	}

	public static void main(String[] args) {
		String jdbcUrl = "jdbc:oracle:thin:@dev-arb:1521:DARB"; // JDBC URL for Oracle
		String username = "CEUSER"; // Your Oracle username
		String password = "CEUSER"; // Your Oracle password
		try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)){
			String sql = "SELECT PROP_KEY,PROP_VALUE FROM CEUSER.SEC_MOBILE_DTAL WHERE PROP_KEY = 'INBOX_PROP_PATH'";
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
					String originalPath = resultSet.getString("PROP_VALUE");
					String convertedPath = convertPath(originalPath);
					System.out.println(convertedPath);
				}
			}
		}catch (Exception e) {
		}
	}


	public static String getCustomProperty(String keyName) throws IOException {
		String keyValue = null;
		try {
			Properties properties = readFileProperties(getPropName("CUSTOM_CONFIG"));
			keyValue = properties.getProperty(keyName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return keyValue;
	}


}
