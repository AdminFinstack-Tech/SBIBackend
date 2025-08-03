package com.csme.csmeapi.mobile.util;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.cs.base.log.CSLogger;
import com.cs.base.utility.ASPathConst;
import com.cs.base.xml.XMLManager;
import com.cs.eximap.log.ASCELog;

public class LGInquiryConstants {
	
	private static final CSLogger logger = ASCELog.getCELogger("csme-rest");
	private static final String DEFAULT_CONSTANT_PATH = ASPathConst.USER_DIR_PATH + File.separator + "INT" + File.separator + "L" + File.separator + "LGInquiryConstant.xml";
	
	private final String tab2ConstantPath;
	private volatile String dbMappingXml;
	private volatile Map<String, String> rbConstantMap;
	private volatile boolean isWebMode = false;
	private volatile boolean initialized = false;
	
	public LGInquiryConstants() {
		this(DEFAULT_CONSTANT_PATH);
	}
	
	public LGInquiryConstants(String constantPath) {
		this.tab2ConstantPath = constantPath;
		this.rbConstantMap = new ConcurrentHashMap<>();
	}
	
	/**
	 * Lazy initialization of constants
	 */
	private synchronized void initialize() {
		if (initialized) {
			return;
		}
		
		try {
			File xmlFile = new File(tab2ConstantPath);
			if (xmlFile.exists()) {
				Document doc = XMLManager.xmlFileToDom(tab2ConstantPath);
				rbConstantMap = XMLManager.convertToHashMap(doc);
				
				// Parse dbMappingXml using DOM
				Element rootElement = doc.getDocumentElement();
				NodeList lginquiryNodes = rootElement.getElementsByTagName("lginquiry");
				if (lginquiryNodes.getLength() > 0) {
					Element lginquiryElement = (Element) lginquiryNodes.item(0);
					NodeList dbmappingxmlNodes = lginquiryElement.getElementsByTagName("dbmappingxml");
					if (dbmappingxmlNodes.getLength() > 0) {
						dbMappingXml = dbmappingxmlNodes.item(0).getTextContent();
					}
				}
				
				initialized = true;
				logger.debug("LGInquiryConstants initialized successfully from: " + tab2ConstantPath);
			} else {
				logger.warn("LG Inquiry constants file not found: " + tab2ConstantPath);
			}
		} catch (Exception e) {
			logger.error("Failed to initialize LG Inquiry constants from: " + tab2ConstantPath + " - " + e.getMessage());
		}
	}
	
	public String getDbMappingXml() {
		if (!initialized) {
			initialize();
		}
		return dbMappingXml;
	}
	
	public Map<String, String> getRbConstantMap() {
		if (!initialized) {
			initialize();
		}
		return new ConcurrentHashMap<>(rbConstantMap); // Return a copy to prevent external modification
	}
	
	public String getConstant(String key) {
		if (!initialized) {
			initialize();
		}
		return rbConstantMap.get(key);
	}
	
	public boolean isWebMode() {
		return isWebMode;
	}
	
	public void setWebMode(boolean webMode) {
		this.isWebMode = webMode;
	}
}


