package com.csme.csmeapi.mobile.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
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
	static CSLogger logger = ASCELog.getCELogger("csme-rest");

	public static String logConfFilePath = ASPathConst.getUserDirPath() + File.pathSeparator + "CE_SYS"  + File.pathSeparator + "CE_Log_Config.xml";
	public static String SECDS;
	public static String CNTY;
	public static String BKGRP;
	public static String TRXDS;
	public static String TRXDBTYPE;
	public static String SECDBTYPE;
	public static Properties CONSTANTS = null;
	public static String intPath = null; 

	public MobileUtil() {
		init();
	}

	@PostConstruct
	public static void init() {
		try {
			SessionContext stx = SessionUtil.getOldSessionContext();
			intPath = MobileConfig.getPropName("INT_PATH");
			if(intPath == null) {
				intPath = ASPathConst.getUserDirPath() + File.pathSeparator ;
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
			e.printStackTrace();
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
			exc.printStackTrace();
		} 
		System.out.println("jsonString:===&gt;" + jsonString);
		return jsonString;
	}

	public static LocalDate parse(String text) {
		LocalDate localDate = null;
		if (text != null && text != "")
			localDate = LocalDate.parse(text); 
		return localDate;
	}

	public static java.time.LocalDate parseJavaDate(String text) {
		java.time.LocalDate localDate = null;
		if (text != null && text != "")
			localDate = java.time.LocalDate.parse(text); 
		return localDate;
	}

	public static String getErrorStackTrace(Throwable e) {
		String err = "";
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintWriter pw = new PrintWriter(os);
			e.printStackTrace(pw);
			os.close();
			pw.close();
			err = os.toString();
			os = null;
			pw = null;
		} catch (Exception exception) {}
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
		Document iconDesc = XMLManager.xmlFileToDom(MobileUtil.intPath+"ModuleIconList.xml");
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

	public static String getCompanyAssignedModules(String companyId,String userId) throws Exception {
		Set<String> modules = new HashSet<>();
		String modulesAsString = "";
		HashMap<String, String> moduleNamesMap = getfunctionModuleMap(
				getConstant("DEFAULT_C_BK_GROUP_ID"), getConstant("DEFAULT_C_CNTY_CODE"));
		//String query = "select  distinct C_FUNC_ID from CEUSER.SEC_UNIT_FUNC WHERE C_UNIT_CODE='" + companyId + "'";
		String query = "select  distinct C_FUNC_ID from CEUSER.SEC_OP_FUNC WHERE C_UNIT_CODE='" + companyId + "' AND C_USER_ID = '"+userId+"'";
		logger.info("Query to get the Functions for the Company Id: " + query);
		SQLRecordSet rs = WSDBHelper.executeQuery(query, SECDS);
		if (rs.isSuccess() && rs.getRecordCount() > 0)
			for (int i = 0; i < rs.getRecordCount(); i++)
				modules.add(moduleNamesMap.get(rs.getRecord(i).getValue("C_FUNC_ID")));  
		modulesAsString = String.join(",", (Iterable<? extends CharSequence>)modules);
		return modulesAsString;
	}

	public static String getLang(String corporateId, String userId) {
		String preKey = null;
		try {
			String selectQuery = String.format("SELECT * FROM CEUSER.PROFILE_INFO WHERE C_USER_ID = '%s' AND C_UNIT_CODE = '%s' AND PREFRENCE_KEY = '%s'",
					userId, corporateId, "LANG");
			SQLRecordSet rs = WSDBHelper.executeQuery(selectQuery, SECDS);
			if(rs.getRecordCount() > 0) {
				preKey = rs.getFirstRecord().getValue("PREFRENCE_VALUE");
			} else {
				preKey = "EN";
			}
		} catch (Exception e) {
			logger.info("Exception has occurred in getLang");
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
		// Flatten nested map
		Map<String, Object> flatMap = flattenMap(mapBody, null);

		// Replace place holders
		for (Map.Entry<String, Object> entry : flatMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			templateString = templateString.replace("{" + key + "}", value != null ? value.toString() : "");
		}
		if(templateString != null)
		{
			templateString.replaceAll("\\{[^{}]*\\}", "null");
		}
		return templateString;
	}

	public static Map<String, Object> flattenMap(Map<String, Object> map, String parentKey) {
		Map<String, Object> flatMap = new HashMap<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = (parentKey != null ? parentKey + "." : "") + entry.getKey();
			Object value = entry.getValue();
			if (value instanceof Map) {
				flatMap.putAll(flattenMap((Map<String, Object>) value, key));
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
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Object value = field.get(obj);

				if (value != null) {
					if (isPrimitiveOrWrapper(value.getClass()) || value instanceof String) {
						map.put(field.getName(), value);
					} else if (value instanceof List) {
						List<?> list = (List<?>) value;
						map.put(field.getName(), list.stream()
								.map(item -> convertToMap(item, visited))
								.collect(Collectors.toList()));
					} else if (value instanceof Map) {
						map.put(field.getName(), value);
					} else {
						map.put(field.getName(), convertToMap(value, visited));
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while converting object to map", e);
		}
		visited.remove(obj); // Remove the object from visited set once processed
		return map;
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
