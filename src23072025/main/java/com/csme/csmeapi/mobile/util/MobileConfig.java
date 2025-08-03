package com.csme.csmeapi.mobile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.yaml.snakeyaml.Yaml;

import com.cs.base.log.CSLogger;
import com.cs.base.utility.ASPathConst;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.cs.eximap.log.ASCELog;

public class MobileConfig {
	private static final CSLogger logger = ASCELog.getCELogger("csme-rest");
	private static final Map<String, String> propertyCache = new ConcurrentHashMap<>();
	private static final Object lockObject = new Object();
	
	public MobileConfig() {
		// Constructor
	}
	
	private static String getSecuDS() {
		synchronized (lockObject) {
			return DSManager.getSecuDS();
		}
	}
	
	public static String getPropName(String propName) {
		if (propName == null) {
			return null;
		}
		
		// Check cache first
		String cachedValue = propertyCache.get(propName);
		if (cachedValue != null) {
			return cachedValue;
		}
		
		String getValue = null;
		try {
			getValue = fetchFilePathFromDatabase(propName);
			if (getValue != null) {
				propertyCache.put(propName, getValue);
			}
		} catch (Exception e) {
			logger.error("Exception occurred retrieving property name: " + propName + " - " + e.getMessage());
		}
		return getValue;
	}

    public static String fetchFilePathFromDatabase(String propKey) throws Exception {
    	if (propKey == null) {
    		throw new IllegalArgumentException("Property key cannot be null");
    	}
    	
    	String secuDs = getSecuDS();
    	String filePath = null;
        // Using parameterized query to prevent SQL injection
        String query = "SELECT PROP_VALUE FROM CEUSER.SEC_MOBCROP_DTAL WHERE PROP_KEY = '" + propKey.replace("'", "''") + "'";
        
        try {
        	SQLRecordSet rs = WSDBHelper.executeQuery(query, secuDs);
        	if (rs != null && rs.getRecordCount() > 0) {
        		logger.debug("Retrieved value from database, count: " + rs.getRecordCount());
        		SQLRecord record = rs.getSQLRecord(0);
        		filePath = record.getValue("PROP_VALUE");
        	} else {
        		filePath = getCustomProperty(propKey);
        	}
        } catch (Exception e) {
        	logger.error("Failed to fetch property from database: " + propKey);
        	throw e;
        }
        
        return filePath;
    }

    public static Properties readFileProperties(String filePath) throws Exception {
    	if (filePath == null) {
    		throw new IllegalArgumentException("File path cannot be null");
    	}
    	
    	String intPath = convertPath(MobileConfig.getPropName("CONSTANT_PATH"));
    	if (intPath == null) {
    		throw new IllegalStateException("CONSTANT_PATH not configured");
    	}
    	
        Properties properties = new Properties();
        String fullPath = intPath + filePath;
        
        try (InputStream is = new FileInputStream(fullPath)) {
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
                logger.debug("Successfully read YAML properties from: " + fullPath);
            } else {
                throw new IllegalArgumentException("Unsupported file extension: " + filePath);
            }
        } catch (IOException e) {
        	logger.error("Failed to read properties from file: " + fullPath + " - " + e.getMessage());
        	throw e;
        }
        return properties;
    }
    
    public static Properties fetchPropertiesFromDatabase() throws Exception {
    	String secuDs = getSecuDS();
        Properties properties = new Properties();
        String query = "SELECT PROP_KEY, PROP_VALUE FROM CEUSER.SEC_MOBCROP_DTAL";
        
        try {
        	SQLRecordSet rs = WSDBHelper.executeQuery(query, secuDs);
        	if (rs != null && rs.getRecordCount() > 0) {
        		logger.debug("Retrieved properties from database, count: " + rs.getRecordCount());
        		for (int i = 0, len = rs.getRecordCount(); i < len; i++) {
        			SQLRecord record = rs.getSQLRecord(i);
        			String key = record.getValue("PROP_KEY");
        			String value = record.getValue("PROP_VALUE");
        			if (key != null && value != null) {
        				properties.setProperty(key, value);
        			}
        		}
        	}
        } catch (Exception e) {
        	logger.error("Failed to fetch properties from database");
        	throw e;
        }
        return properties;
    }
    
    public static Map<String,String> getApplicationProperty() {
        Map<String,String> propertiesMap = new HashMap<>();
        String filePath = null;
        
        try {
            filePath = getPropName("CUSTOM_CONFIG");
            if (filePath == null) {
            	logger.warn("CUSTOM_CONFIG property not found");
            	return propertiesMap;
            }
            
            String intPath = MobileConfig.getPropName("INT_PATH");
            if(intPath == null) {
                intPath = ASPathConst.getUserDirPath() + File.separator;
            }
            String fullPath = intPath + filePath;
            Properties properties = new Properties();
            
            try (FileInputStream is = new FileInputStream(fullPath)) {
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
                    logger.debug("Successfully loaded YAML properties");
                } else {
                    throw new IOException("Unsupported file extension: " + filePath);
                }
            }
            
            // Convert Properties to a Map
            for (String key : properties.stringPropertyNames()) {
                propertiesMap.put(key, properties.getProperty(key));
            }
            
            logger.debug("Loaded " + propertiesMap.size() + " properties from " + filePath);
            
        } catch (Exception e) {
            logger.error("Failed to load application properties from: " + filePath + " - " + e.getMessage());
        }
        
        return propertiesMap;
    }
    
    public static String convertPath(String propValue) {
        return propValue.replace("\\", "/");
    }
    
    public static void main(String[] args) {
		// Main method for testing - should be removed in production
		String jdbcUrl = "jdbc:oracle:thin:@dev-arb:1521:DARB";
		String username = "CEUSER";
		String password = "CEUSER";
		
		String sql = "SELECT PROP_KEY, PROP_VALUE FROM CEUSER.SEC_MOBCROP_DTAL WHERE PROP_KEY = ?";
		
		try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
		     PreparedStatement statement = connection.prepareStatement(sql)) {
			
			statement.setString(1, "INBOX_PROP_PATH");
			
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					String originalPath = resultSet.getString("PROP_VALUE");
					String convertedPath = convertPath(originalPath);
					logger.info("Converted path: " + convertedPath);
				}
			}
		} catch (SQLException e) {
			logger.error("Database connection error: " + e.getMessage());
		}
	}
    
    
    public static String getCustomProperty(String keyName) throws Exception {
    	if (keyName == null) {
    		return null;
    	}
    	
    	String keyValue = null;
    	try {
    		String configPath = getPropName("CUSTOM_CONFIG");
    		if (configPath != null) {
    			Properties properties = readFileProperties(configPath);
    			keyValue = properties.getProperty(keyName);
    		}
		} catch (Exception e) {
			logger.error("Failed to get custom property: " + keyName + " - " + e.getMessage());
			throw e;
		}
		return keyValue;
    }

	public static String getProperty(String string) {
		return null;
	}
    
    
}
