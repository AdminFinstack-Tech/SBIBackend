package com.csme.csmeapi.mobile.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.threeten.bp.LocalDate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.cs.base.log.CSLogger;
import com.cs.base.utility.ASPathConst;
import com.cs.base.xml.XMLManager;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.ce.env.PathUtil;
import com.cs.core.cache.CacheMLGParameterHelper;
import com.cs.core.cache.CacheSYSHelper;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.exec.SQLRecordSet;
import com.cs.core.utility.SessionContext;
import com.cs.core.utility.SessionUtil;
import com.cs.core.utility.StringUtil;
import com.cs.core.xml.object.scrn.MLGDesc;
import com.cs.core.xml.object.scrn.MLGLabel;
import com.cs.core.xml.object.syst.dbdict.DBDictionary;
import com.cs.eximap.log.ASCELog;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class MobileUtil {
	private static final CSLogger logger = ASCELog.getCELogger("csme-rest");
	private static final Object lockObject = new Object();
	private static final Map<String, Document> xmlDocumentCache = new ConcurrentHashMap<>();
	private static final Map<String, Field[]> fieldCache = new ConcurrentHashMap<>();

	public static String logConfFilePath = ASPathConst.getUserDirPath() + File.separator + "CE_SYS"  + File.separator + "CE_Log_Config.xml";
	private static volatile String SECDS;
	private static volatile String CNTY;
	private static volatile String BKGRP;
	private static volatile String TRXDS;
	private static volatile String TRXDBTYPE;
	private static volatile String SECDBTYPE;
	private static volatile Properties CONSTANTS = null;
	private static volatile String intPath = null; 

	public MobileUtil() {
		init();
	}

	@PostConstruct
	public static void init() {
		synchronized (lockObject) {
			try {
				SessionContext stx = SessionUtil.getOldSessionContext();
				intPath = MobileConfig.getPropName("INT_PATH");
				if(intPath == null) {
					intPath = ASPathConst.getUserDirPath() + File.separator;
				}
			//			logConfFilePath = MobileConfig.getPropName("LOG_PATH");
			//			if(logConfFilePath == null) {
			//				logConfFilePath = ASPathConst.getUserDirPath() + File.pathSeparator + "CE_SYS"  + File.pathSeparator + "CE_Log_Config.xml";
			//			}
			//			CONSTANTS = MobileConfig.readFileProperties(MobileConfig.getPropName("CUSTOM_CONFIG"));
			//
			//			SECDS = MobileConfig.getPropName("SEC_DS");
			//			logger.info("SECDS is :"+SECDS);
			//			if(SECDS == null) {
			//				SECDS = DSManager.getSecuDS();
			//			}
			//			logger.info("SECDS is :"+SECDS);
			//			CNTY = MobileConfig.getPropName("COUNTRY");
			//			if(CNTY == null) {
			//				CNTY =stx.getCountryCode();
			//			}
			//
			//			BKGRP = MobileConfig.getPropName("BANK_GROUP");
			//			if(BKGRP == null) {
			//				BKGRP =stx.getBankGroup();
			//			}
			//
			//			TRXDS = MobileConfig.getPropName("TRX_DS");
			//			if(TRXDS == null) {
			//				TRXDS = DSManager.getTrxDS(BKGRP, CNTY);
			//			}
			//
			//			TRXDBTYPE = MobileConfig.getPropName("TRX_DB_TYPE");
			//			if(TRXDBTYPE==null)
			//				TRXDBTYPE = DSManager.getDBType(TRXDBTYPE);
			//
			//			SECDBTYPE = MobileConfig.getPropName("SEC_DB_TYPE");
			//			if(SECDBTYPE==null)
			//				SECDBTYPE = DSManager.getDBType(SECDBTYPE);

			} catch (Exception e) {
				logger.error("Failed to initialize MobileUtil: " + e.getMessage());
			}
		}
	}



	public static Object getKeyFromValue(Map<String, String> hm, Object value) {
		Object o = null;
		for (Map.Entry<String, String> entry : hm.entrySet()) {
			if (((String)entry.getKey()).equals(value.toString().trim()))
				o = entry.getValue(); 
		} 
		return o;
	}

	public static String getJsonString(Object object) {
		String jsonString = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			if (object != null) {
				mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
				mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				mapper.enable(SerializationFeature.INDENT_OUTPUT);
				jsonString = mapper.writeValueAsString(object);
			} 
		} catch (Exception exc) {
			logger.error("Failed to convert object to JSON: " + exc.getMessage());
		}
		return jsonString;
	}

	public static LocalDate parse(String text) {
		LocalDate localDate = null;
		if (text != null && !text.isEmpty())
			localDate = LocalDate.parse(text); 
		return localDate;
	}

	public static java.time.LocalDate parseJavaDate(String text) {
		java.time.LocalDate localDate = null;
		if (text != null && !text.isEmpty())
			localDate = java.time.LocalDate.parse(text); 
		return localDate;
	}

	public static String getErrorStackTrace(Throwable e) {
		String err = "";
		try (ByteArrayOutputStream os = new ByteArrayOutputStream();
		     PrintWriter pw = new PrintWriter(os)) {
			e.printStackTrace(pw);
			pw.flush();
			err = os.toString();
		} catch (IOException exception) {
			logger.error("Failed to get error stack trace: " + exception.getMessage());
		}
		return err;
	}

	public static String getConstant(String property) {
		return CONSTANTS.getProperty(property);
	}

	public static HashMap<String, String> getfunctionModuleMap(String sBankGrp, String sCntyCode) throws Exception {
		HashMap<String, String> map = new HashMap<>();
		try {
			String path = PathUtil.getAPPath(sBankGrp, sCntyCode) + "FUNC" + File.separator + "function_root.xml";
			File file = new File(path);
			JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] { Root.class });
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Root que = (Root)jaxbUnmarshaller.unmarshal(file);
			List<Root.Func> functionlist = que.getFunc();
			for (Root.Func func : functionlist)
				map.put(func.getId(), func.getModule()); 
		} catch (Exception e) {
			logger.error("Error while Set in the Map ");
			logger.error(e);
			throw new Exception(e);
		} 
		return map;
	}

	public static String getIconLink(Document iconDescDoc,String moduleName) throws Exception {
		String iconLink = null;
		try {
			NodeList moduleList = iconDescDoc.getElementsByTagName("Module");
			for (int i = 0; i < moduleList.getLength(); i++) {
				Element moduleElement = (Element) moduleList.item(i);
				if (moduleElement.getElementsByTagName(moduleName).getLength() > 0) {
					Element targetModule = (Element) moduleElement.getElementsByTagName(moduleName).item(0);
					iconLink = targetModule.getElementsByTagName("icon").item(0).getTextContent();
					return iconLink;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return iconLink;
	}

	public static JSONObject convertStyleStringToJson(String styleString) throws JSONException {
		JSONObject styleJson = new JSONObject();
		String[] stylePairs = styleString.split(";");
		for (String stylePair : stylePairs) {
			String[] keyValue = stylePair.split(":");
			if (keyValue.length == 2) {
				String key = keyValue[0].trim();
				String value = keyValue[1].trim();
				styleJson.put(key, value);
			}
		}
		return styleJson;
	}

	public static String getModuleDescription(Document iconDescDoc,String moduleName) throws Exception {
		String moduleDesc = null;
		try {
			NodeList moduleList = iconDescDoc.getElementsByTagName(moduleName);
			if (moduleList.getLength() > 0) {
				Element moduleElement = (Element) moduleList.item(0);
				moduleDesc = moduleElement.getElementsByTagName("desc").item(0).getTextContent();
				return moduleDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return moduleDesc;
	}

	public static JSONObject getModuleStyle(Document iconDescDoc,String moduleName) throws Exception {
		JSONObject styleStringJ = null;
		try {
			NodeList moduleList = iconDescDoc.getElementsByTagName(moduleName);
			if (moduleList.getLength() > 0) {
				Element moduleElement = (Element) moduleList.item(0);
				String styleString = moduleElement.getElementsByTagName("style").item(0).getTextContent();
				styleStringJ = convertStyleStringToJson(styleString);
				return styleStringJ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return styleStringJ;
	}

	public static Document getIconDescriptionDoc() throws Exception {
		String docKey = "ModuleIconList.xml";
		Document cachedDoc = xmlDocumentCache.get(docKey);
		if (cachedDoc != null) {
			return cachedDoc;
		}
		
		Document iconDesc = XMLManager.xmlFileToDom(MobileUtil.intPath + docKey);
		xmlDocumentCache.put(docKey, iconDesc);
		return iconDesc;
	}

	public static HashMap<String, String> getProductModuleMap(String sBankGrp, String sCntyCode) throws Exception {
		HashMap<String, String> map = new HashMap<>();
		try {
			String path = PathUtil.getAPPath(sBankGrp, sCntyCode) + "FUNC" + File.separator + "function_root.xml";
			File file = new File(path);
			JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] { Root.class });
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Root que = (Root)jaxbUnmarshaller.unmarshal(file);
			List<Root.Func> functionlist = que.getFunc();
			for (Root.Func func : functionlist) {
				if (!isEmpty(func.getProduct()))
					map.put(func.getProduct(), func.getModule()); 
			} 
		} catch (Exception e) {
			logger.error("Error while Set in the Map ");
			logger.error(e);
			throw new Exception(e);
		} 
		return map;
	}

	public static boolean isEmpty(String val) {
		if (val == null)
			return true; 
		val = val.trim();
		if (val.length() < 1)
			return true; 
		return false;
	}

	public static String getCompanyAssignedModules(String companyId, String userId) throws Exception {
		if (companyId == null || userId == null) {
			throw new IllegalArgumentException("Company ID and User ID cannot be null");
		}
		
		Set<String> modules = new HashSet<>();
		String modulesAsString = "";
		HashMap<String, String> moduleNamesMap = getfunctionModuleMap(
				getConstant("DEFAULT_C_BK_GROUP_ID"), getConstant("DEFAULT_C_CNTY_CODE"));
		
		// Escape single quotes to prevent SQL injection
		String safeCompanyId = companyId.replace("'", "''");
		String safeUserId = userId.replace("'", "''");
		String query = "SELECT DISTINCT C_FUNC_ID FROM CEUSER.SEC_OP_FUNC WHERE C_UNIT_CODE = '" + safeCompanyId + "' AND C_USER_ID = '" + safeUserId + "'";
		
		String secDs = getSecureDS();
		
		try {
			logger.debug("Query to get functions for Company: " + companyId + ", User: " + userId);
			SQLRecordSet rs = WSDBHelper.executeQuery(query, secDs);
			
			if (rs != null && rs.isSuccess() && rs.getRecordCount() > 0) {
				for (int i = 0; i < rs.getRecordCount(); i++) {
					String funcId = rs.getRecord(i).getValue("C_FUNC_ID");
					String moduleName = moduleNamesMap.get(funcId);
					if (moduleName != null) {
						modules.add(moduleName);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Failed to get company assigned modules for company: " + companyId + ", user: " + userId);
			throw e;
		}
		
		modulesAsString = String.join(",", modules);
		return modulesAsString;
	}
	
	private static String getSecureDS() {
		synchronized (lockObject) {
			if (SECDS == null) {
				SECDS = DSManager.getSecuDS();
			}
			return SECDS;
		}
	}

	public static String getLang(String corporateId, String userId) {
		if (corporateId == null || userId == null) {
			return "EN"; // Default language
		}
		
		String preKey = "EN"; // Default
		// Escape single quotes to prevent SQL injection
		String safeUserId = userId.replace("'", "''");
		String safeCorporateId = corporateId.replace("'", "''");
		String query = "SELECT PREFRENCE_VALUE FROM CEUSER.PROFILE_INFO WHERE C_USER_ID = '" + safeUserId + 
		               "' AND C_UNIT_CODE = '" + safeCorporateId + "' AND PREFRENCE_KEY = 'LANG'";
		
		String secDs = getSecureDS();
		
		try {
			SQLRecordSet rs = WSDBHelper.executeQuery(query, secDs);
			if (rs != null && rs.getRecordCount() > 0) {
				String value = rs.getFirstRecord().getValue("PREFRENCE_VALUE");
				if (value != null && !value.isEmpty()) {
					preKey = value;
				}
			}
		} catch (Exception e) {
			logger.error("Exception occurred in getLang for user: " + userId + " - " + e.getMessage());
		}
		return preKey;
	}

	public static String getDBFieldDesc(String fieldName,String language) throws Exception {
		String desc = "";
		try {
			MLGLabel mlgLabel = null;
			if (!StringUtil.isEmpty(language, true))
				mlgLabel = CacheMLGParameterHelper.getObjMLGLabel(language); 
			if (mlgLabel == null)
				mlgLabel = CacheMLGParameterHelper.getDefaultObjMLGLabel(); 
			if (mlgLabel != null)
				desc = mlgLabel.getDescByName(fieldName); 
			if (StringUtil.isEmpty(desc)) {
				DBDictionary dbdct = CacheSYSHelper.getDBDictionary();
				if (dbdct != null) {
					com.cs.core.xml.object.syst.dbdict.Field field = dbdct.getFieldByName(fieldName);
					if (field != null)
						desc = field.getDesc(); 
				} 
			} 
			if (!StringUtil.isEmpty(desc))
				return desc; 
		} catch (Exception e) {
			e.printStackTrace();
			return desc; 
		}
		return desc; 
	}

	public static String replacePlaceholders(String templateString, Map<String, Object> mapBody) {
		if (templateString == null || mapBody == null) {
			return templateString;
		}
		
		// Flatten nested map
		Map<String, Object> flatMap = flattenMap(mapBody, null);

		// Replace place holders
		String result = templateString;
		for (Map.Entry<String, Object> entry : flatMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			result = result.replace("{" + key + "}", value != null ? value.toString() : "");
		}
		
		// Replace remaining placeholders with empty string
		result = result.replaceAll("\\{[^{}]*\\}", "");
		
		return result;
	}

	public static Map<String, Object> flattenMap(Map<String, Object> map, String parentKey) {
		return flattenMap(map, parentKey, 0, 10); // Max depth of 10 levels
	}
	
	private static Map<String, Object> flattenMap(Map<String, Object> map, String parentKey, int currentDepth, int maxDepth) {
		Map<String, Object> flatMap = new HashMap<>();
		
		if (map == null || currentDepth >= maxDepth) {
			return flatMap;
		}
		
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = (parentKey != null ? parentKey + "." : "") + entry.getKey();
			Object value = entry.getValue();
			
			if (value instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> nestedMap = (Map<String, Object>) value;
				flatMap.putAll(flattenMap(nestedMap, key, currentDepth + 1, maxDepth));
			} else {
				flatMap.put(key, value);
			}
		}
		return flatMap;
	}

	public static String getFieldDesc(String aFieldName,String language) {
		if (!StringUtil.isEmpty(language, true)) {
			MLGDesc mlgDesc = CacheMLGParameterHelper.getObjMLGDesc(language);
			if (mlgDesc != null)
				return mlgDesc.getDescByName(aFieldName); 
		} 
		return null;
	}

	public static String getValueByTagName(Element element, String tagName) {
		NodeList nodeList = element.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			Node node = nodeList.item(0);
			return node.getTextContent();
		}
		return null;
	}

	public static String getContentValueFromXML(String xml, String xpathExpression) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = builder.parse(is);

			XPathFactory xPathfactory = XPathFactory.newInstance();
			javax.xml.xpath.XPath xpath = xPathfactory.newXPath();
			javax.xml.xpath.XPathExpression expr = xpath.compile(xpathExpression);

			return (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (Exception e) {
			e.printStackTrace();
			return null; 
		}
	}

	public static Map<String, Object> convertToMap(Object obj) {
		return convertToMap(obj, new HashSet<>());
	}

	private static Map<String, Object> convertToMap(Object obj, Set<Object> visited) {
		if (obj == null) {
			return null;
		}

		// Avoid circular references
		if (visited.contains(obj)) {
			return Collections.singletonMap("circularReference", "Detected");
		}

		visited.add(obj);

		Map<String, Object> map = new HashMap<>();
		try {
			Class<?> clazz = obj.getClass();
			Field[] fields = getFieldsFromCache(clazz);
			
			for (Field field : fields) {
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				Object value = field.get(obj);

				if (value != null) {
					if (isPrimitiveOrWrapper(value.getClass()) || value instanceof String) {
						map.put(field.getName(), value);
					} else if (value instanceof List) {
						List<?> list = (List<?>) value;
						List<Object> convertedList = new ArrayList<>(list.size());
						for (Object item : list) {
							convertedList.add(convertToMap(item, visited));
						}
						map.put(field.getName(), convertedList);
					} else if (value instanceof Map) {
						map.put(field.getName(), value);
					} else {
						map.put(field.getName(), convertToMap(value, visited));
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error converting object to map: " + obj.getClass().getName() + " - " + e.getMessage());
			throw new RuntimeException("Error while converting object to map", e);
		} finally {
			visited.remove(obj); // Remove the object from visited set once processed
		}
		return map;
	}
	
	private static Field[] getFieldsFromCache(Class<?> clazz) {
		return fieldCache.computeIfAbsent(clazz.getName(), k -> clazz.getDeclaredFields());
	}

	private static boolean isPrimitiveOrWrapper(Class<?> type) {
		return type.isPrimitive() ||
				type.equals(String.class) ||
				type.equals(Integer.class) ||
				type.equals(Double.class) ||
				type.equals(Float.class) ||
				type.equals(Long.class) ||
				type.equals(Short.class) ||
				type.equals(Byte.class) ||
				type.equals(Boolean.class) ||
				type.equals(Character.class);
	}

}
