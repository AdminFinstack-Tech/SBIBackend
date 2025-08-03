/**
 * The GlobalSearchApiService class provides functionality for conducting global search inquiries.
 * It handles building inquiry responses, setting paging information, populating inquiry information,
 * and executing SQL queries based on the search criteria.
 */
package com.csme.csmeapi.fin.services;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.text.StringSubstitutor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.cs.base.xml.XMLManager;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XGlobalSearchRequest;
import com.csme.csmeapi.fin.models.XInquireListObj;
import com.csme.csmeapi.fin.models.XInquiryListResponse;
import com.csme.csmeapi.fin.models.XproductInfoList;
import com.csme.csmeapi.fin.models.XproductInfoListNofTransaction;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The GlobalSearchApiService class provides functionality for conducting global search inquiries.
 * It handles building inquiry responses, setting paging information, populating inquiry information,
 * and executing SQL queries based on the search criteria.
 */
public class GlobalSearchApiService {
	
	FinUtil finUtil = new FinUtil();
	/**
	 * Logger object for logging messages related to CSMEMobile operations.
	 */
	Logger logger = LogManager.getLogger("CSMEMobile");
	/**
	 * The security datasource string for database connections.
	 */
	private String secuDs = null;
	/**
	 * The language code for localization and internationalization.
	 */
	public String Lang = null;

	/**
	 * Constructs a new GlobalSearchApiService instance.
	 * Initializes the service by retrieving the secure data source (secuDs) using DSManager.
	 */
	public GlobalSearchApiService() {
		try {
			secuDs = DSManager.getSecuDS();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves inquiry information based on the provided search request.
	 * 
	 * @param body The search request containing parameters like corporate ID, user ID, search criteria, page number, and page size.
	 * @param requestId The unique identifier for the search request.
	 * @param sequence The sequence number associated with the search request.
	 * @return An XInquiryListResponse object containing the inquiry information.
	 */
	public XInquiryListResponse getInquiryInfo(@Valid XGlobalSearchRequest body, UUID requestId, Long sequence) {
		XInquiryListResponse xInquiryListResponse = new XInquiryListResponse();
		try {
			Lang = FinUtil.getLang(body.getCorporateId(), body.getUserId());
			buildInquiryResponse(body, requestId, sequence, xInquiryListResponse);
			logger.info("Built Inquiry Response successfully.");
		} catch (Exception e) {
			handleException(xInquiryListResponse, e);
			logger.error("Error building Inquiry Response: " + e.getMessage(), e);
		}
		return xInquiryListResponse;
	}

	/**
	 * Builds the inquiry response based on the provided search request.
	 * 
	 * @param body The search request containing parameters like corporate ID, user ID, search criteria, page number, and page size.
	 * @param requestId The unique identifier for the search request.
	 * @param sequence The sequence number associated with the search request.
	 * @param xInquiryResponse The XInquiryListResponse object to populate with inquiry information.
	 */
	private void buildInquiryResponse(@Valid XGlobalSearchRequest body, UUID requestId, Long sequence,
			XInquiryListResponse xInquiryResponse) {
		try {
			xInquiryResponse.setRequestId(requestId.toString());
			xInquiryResponse.setMessageId(sequence.toString());
			setTimestamp(xInquiryResponse);

			int pageNo = body.getPage();
			int pageSize = body.getPerPage();
			String[] moduleList = body.getModule().split(";");

			String filePath = FinUtil.intPath+"Common"+body.getType()+".xml";
			Document commonXmlDoc = XMLManager.xmlFileToDom(filePath);
			logger.info("Convert Inquiry Dom into String is : " + XMLManager.convertToString(commonXmlDoc));
			StringBuilder commonSqlBuilder = getIboxQuery(body, commonXmlDoc);
			String commonSqlQuery = commonSqlBuilder.toString();
			logger.info("Execute SqlQuery is " + commonSqlQuery);
			if(commonSqlQuery != null && commonSqlQuery != null) {
				SQLRecordSet rs = WSDBHelper.executeQuery(commonSqlQuery, secuDs);
				logger.info("Execute SqlQuery result count is : " + rs.getRecordCount());
				setPagingInfo(xInquiryResponse, pageNo, pageSize, rs);
				setProductList(xInquiryResponse, moduleList,body,commonXmlDoc);
				populateInquiryInfo(xInquiryResponse, rs, commonXmlDoc,moduleList,body);
				logger.info("final Value is   : " + xInquiryResponse);
			}
		} catch (Exception e) {
			handleException(xInquiryResponse, e);
		}
	}

	/**
	 * Sets the product list in the inquiry response based on the provided search request and module list.
	 *
	 * @param xInquiryResponse The XInquiryListResponse object to populate with the product list.
	 * @param moduleList The list of modules specified in the search request.
	 * @param body The search request containing parameters like corporate ID, user ID, search criteria, page number, and page size.
	 * @param commonXmlDoc The XML document representing the common inquiry structure.
	 * @throws Exception If an error occurs during setting the product list.
	 */
	private void setProductList(XInquiryListResponse xInquiryResponse, String[] moduleList,@Valid XGlobalSearchRequest body, Document commonXmlDoc) throws Exception {
		List<XproductInfoList> productsAccessList = new ArrayList<XproductInfoList>();
		Document iconDescDoc = FinUtil.getIconDescriptionDoc();
		for (String module : moduleList) {
			XproductInfoList XproductInfoList = new XproductInfoList();
			XproductInfoList.setModule(module);
			String icon = FinUtil.getIconLink(iconDescDoc, module);
			XproductInfoList.setIcon(icon);
			String moduleDesc = FinUtil.getDBFieldDesc(module, this.Lang);
			if(moduleDesc != null && moduleDesc != "") {
				XproductInfoList.setDescription(moduleDesc);
			} else {
				String desc = FinUtil.getModuleDescription(iconDescDoc, module);
				XproductInfoList.setDescription(desc);
			}
			JSONObject style = FinUtil.getModuleStyle(iconDescDoc, module);
			Map<String, Object> styleMap = new HashMap<>();
			if(style != null && style.length() > 0) {
				Iterator<?> keys = style.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					Object value = style.get(key);
					styleMap.put(key, value);
				}
				XproductInfoList.setStyle(styleMap);
			}
			XproductInfoListNofTransaction nofTransaction = new XproductInfoListNofTransaction();
			nofTransaction.setPendingCount(Integer.toString(getPendingCountTrx(body, module, "P")));
			nofTransaction.setTotalCount(Integer.toString(getCountTransaction(body, module, "",commonXmlDoc)));
			XproductInfoList.setNofTransaction(nofTransaction);
			productsAccessList.add(XproductInfoList);
		}
		xInquiryResponse.setProductsAccessList(productsAccessList);
	}

	/**
	 * Retrieves the count of transactions based on the provided search request, module, and status.
	 *
	 * @param body The search request containing parameters like corporate ID, user ID, search criteria, page number, and page size.
	 * @param module The module for which the transaction count is retrieved.
	 * @param status The status of transactions to consider.
	 * @param commonXmlDoc The XML document representing the common inquiry structure.
	 * @return The count of transactions based on the specified parameters.
	 */

	private int getCountTransaction(@Valid XGlobalSearchRequest body, String module, String status, Document commonXmlDoc) {
		int count = 0;
		try {
			String moduleQuery = null;
			if(body.getType() != null && body.getType().toString() == "AUTH") {
				moduleQuery = getModuleQueryCount(module, commonXmlDoc);
			} else 
			{
				moduleQuery = getModuleQuery(module, commonXmlDoc);
			}
			Map<String, String> valuesMap = new HashMap<String, String>();
			valuesMap.put("unitcode", body.getCorporateId());
			valuesMap.put("module", module);
			StringSubstitutor substitutor = new StringSubstitutor(valuesMap);
			moduleQuery = substitutor.replace(moduleQuery);
			SQLRecordSet rs = WSDBHelper.executeQuery(moduleQuery, secuDs);
			count = rs.getRecordCount();
			logger.info("Count transaction is " + count);
		} catch (Exception e) {
			logger.error("Exception occurred in getCountTransaction function: " + e);
		}
		return count;
	}

	/**
	 * Retrieves the count of pending transactions based on the provided search request and module.
	 *
	 * @param body The search request containing parameters like corporate ID, user ID, search criteria, page number, and page size.
	 * @param module The module for which the pending transaction count is retrieved.
	 * @param status The status of transactions to consider (e.g., 'P' for pending).
	 * @return The count of pending transactions.
	 */
	private int getPendingCountTrx(@Valid XGlobalSearchRequest body, String module, String status) {
		int count = 0;
		try {
			String sqlQuery = "SELECT C_TRX_STATUS FROM CETRX.TRX_INBOX WHERE C_UNIT_CODE = '" + body.getCorporateId() + "'";
			if (!module.equalsIgnoreCase("ALL")) {
				sqlQuery += " AND C_MODULE = '" + module + "'";
			}

			if (status != null && (status.equalsIgnoreCase("P") || status.equalsIgnoreCase("T"))) {
				sqlQuery += " AND C_TRX_STATUS IN ('P','T','S') ";
			} 

			logger.info("Excecute SqlQuery is " + sqlQuery);
			SQLRecordSet rs = WSDBHelper.executeQuery(sqlQuery, secuDs);
			count = rs.getRecordCount();
			logger.info("Count transaction is " + count);
		} catch (Exception e) {
			logger.error("Exception occurred in getCountTransaction function: " + e);
		}
		return count;
	}

	/**
	 * Sets the paging information in the inquiry response.
	 *
	 * @param xInquiryListResponse The XInquiryListResponse object to set paging information.
	 * @param pageNo The current page number.
	 * @param pageSize The number of items per page.
	 * @param rs The SQLRecordSet containing the query result set.
	 */

	private void setTimestamp(XInquiryListResponse xInquiryResponse) {
		String setTimeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new java.util.Date());
		xInquiryResponse.setTimestamp(setTimeStamp);
	}

	private void setPagingInfo(XInquiryListResponse xInquiryListResponse, int pageNo, int pageSize, SQLRecordSet rs) {
		xInquiryListResponse.setHasNext(Long.valueOf(pageNo + 1));
		xInquiryListResponse.setHasPrev(Long.valueOf(pageNo - 1));
		xInquiryListResponse.setMessageId(xInquiryListResponse.getRequestId());
		xInquiryListResponse.setPage(Long.valueOf(pageNo));
		xInquiryListResponse.setPerPage(Long.valueOf(pageSize));
		xInquiryListResponse.setTotal(Long.valueOf(rs.getTotalDBRecords()));
	}

	/**
	 * Populates the inquiry information in the inquiry response based on the SQL query result.
	 * 
	 * @param xInquiryListResponse The XInquiryListResponse object to populate with inquiry information.
	 * @param rs The SQLRecordSet containing the query result set.
	 * @param inquiryXml The XML document representing the inquiry structure.
	 * @param moduleList The list of modules specified in the search request.
	 * @param body The search request containing parameters like corporate ID, user ID, search criteria, page number, and page size.
	 * @throws Exception If an error occurs during the population of inquiry information.
	 */
	private void populateInquiryInfo(XInquiryListResponse xInquiryListResponse, SQLRecordSet rs, Document inquiryXml, String[] moduleList, @Valid XGlobalSearchRequest body) throws Exception {
		HashMap<String, Document> moduleXml = getModuleListDocument(moduleList,body);
		List<XInquireListObj> inquireListObjs = new ArrayList<XInquireListObj>();
		if (rs.getRecordCount() > 0) {
			for (int i = 0; i < rs.getRecordCount(); i++) {
				SQLRecord sqlRecord = rs.getSQLRecord(i);
				if (sqlRecord != null) {
					XInquireListObj xInquireListObj = new XInquireListObj();
					JSONObject commonJson = buildCommonInfoList(sqlRecord, inquiryXml);
					logger.info("Common JSON value is  : " + commonJson);
					JSONObject dynamicJson = buildDynamicInfoList(sqlRecord, inquiryXml, moduleXml);
					logger.info("Dynamic JSON value is  : " + dynamicJson);

					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode commonJsonNode = objectMapper.readTree(commonJson.toString());
					JsonNode dynamicJsonNode = objectMapper.readTree(dynamicJson.toString());
					xInquireListObj.setCommonInfoList(commonJsonNode);
					xInquireListObj.setDynamicInfoList(dynamicJsonNode);
					inquireListObjs.add(xInquireListObj);
				}
			}
			xInquiryListResponse.setInquireListInfo(inquireListObjs);
			xInquiryListResponse.setStatusCode("00");
			xInquiryListResponse.setStatusDescription("Success");
		} else {
			xInquiryListResponse.setStatusCode("06");
			xInquiryListResponse.setStatusDescription("No Record exist Inquiry Transaction");
		}
	}

	/**
	 * Retrieves a map of module names to their corresponding XML documents.
	 * This method reads XML files for each module in the module list and stores them in a map.
	 *
	 * @param moduleList The list of modules for which XML documents are to be retrieved.
	 * @param body The search request containing parameters like corporate ID, user ID, etc.
	 * @return A HashMap containing module names as keys and their XML documents as values.
	 * @throws Exception If there is an error in retrieving or processing XML documents.
	 */
	private HashMap<String, Document> getModuleListDocument(String[] moduleList, @Valid XGlobalSearchRequest body) throws Exception {
		HashMap<String, Document> mapDoc= new HashMap<String, Document>();
		for (int i = 0; i < moduleList.length; i++) {
			String filePath = FinUtil.intPath+moduleList[i]+body.getType()+".xml";
			Document moduleDoc = XMLManager.xmlFileToDom(filePath);
			mapDoc.put(moduleList[i], moduleDoc);
		}
		return mapDoc;
	}

	/**
	 * Builds the common information list based on the provided SQL record and XML document.
	 * This method processes the CommonInquiry section of the XML document to extract attributes
	 * and values from the SQL record and populate them into a JSON object.
	 *
	 * @param sqlRecord The SQLRecord containing data to populate into the JSON object.
	 * @param inquiryXml The XML document representing the inquiry structure.
	 * @return A JSONObject containing the common information list.
	 * @throws Exception If there is an error in building the common information list.
	 */
	private JSONObject buildCommonInfoList(SQLRecord sqlRecord, Document inquiryXml) throws Exception {
		JSONObject commonJson = new JSONObject();
		NodeList commonNdList = inquiryXml.getElementsByTagName("CommonInquiry");
		Node chCmNd = commonNdList.item(0);
		Node[] chCmNdList = getChildNodes(chCmNd);

		if (chCmNdList.length > 0) {
			for (Node childNodeCm : chCmNdList) {
				if (childNodeCm.getNodeType() == 1) {
					processNode(commonJson, childNodeCm, sqlRecord);
				}
			}
		}
		return commonJson;
	}

	/**
	 * Builds the dynamic information list based on the provided SQL record and XML document.
	 * This method processes the DynamicInquiry section of the XML document to extract attributes
	 * and values from the SQL record and populate them into a JSON object.
	 *
	 * @param sqlRecord The SQLRecord containing data to populate into the JSON object.
	 * @param inquiryXml The XML document representing the inquiry structure.
	 * @param moduleXml The HashMap containing module-specific XML documents.
	 * @return A JSONObject containing the dynamic information list.
	 * @throws Exception If there is an error in building the dynamic information list.
	 */
	private JSONObject buildDynamicInfoList(SQLRecord sqlRecord, Document inquiryXml, HashMap<String, Document> moduleXml) throws Exception {
		JSONObject dynamicJson = new JSONObject();
		String module = null;
		if(moduleXml.containsKey("ALL")) {
			module = "ALL";
		} else {
			module = sqlRecord.getValue("C_MODULE");
		}
		Document newInquiryXml = moduleXml.get(module);
		NodeList dynamicNdList = newInquiryXml.getElementsByTagName("DynamicInquiry");
		Node chDynamicNd = dynamicNdList.item(0);
		Node[] chDynamicNdList = getChildNodes(chDynamicNd);

		if (chDynamicNdList.length > 0) {
			for (Node childNodeCm : chDynamicNdList) {
				if (childNodeCm.getNodeType() == 1) {
					processNode(dynamicJson, childNodeCm, sqlRecord);
				}
			}
		}
		return dynamicJson;
	}

	/**
	 * Processes an XML node and converts its attributes into a JSON object.
	 *
	 * @param json The JSON object to populate with attribute values.
	 * @param childNode The XML node to process.
	 * @param sqlRecord The SQLRecord containing data to fill into the JSON object.
	 * @throws Exception If there is an error in processing the XML node.
	 */
	private void processNode(JSONObject json, Node childNode, SQLRecord sqlRecord) throws Exception {
		String fieldName = childNode.getNodeName();
		NamedNodeMap childCmNodeAttr = childNode.getAttributes();
		JSONObject valueInJson = new JSONObject();

		for (int k = 0; k < childCmNodeAttr.getLength(); k++) {
			Node attribute = childCmNodeAttr.item(k);
			String attributeName = attribute.getNodeName();
			String attributeValue = attribute.getNodeValue();

			if (attributeName.equalsIgnoreCase("value")) {
				attributeValue = sqlRecord.getValue(attributeValue);
				valueInJson.put(attributeName, attributeValue);
			} else if (attributeName.equalsIgnoreCase("style")) {
				JSONObject styleInJson = new JSONObject();
				processStyleAttribute(styleInJson, attribute);
				valueInJson.put(attributeName, styleInJson);
			} else if(attributeName.equalsIgnoreCase("labelname")){
				String fieldDesc = FinUtil.getDBFieldDesc(fieldName, this.Lang);
				if(fieldDesc != null && fieldDesc != "") {
					valueInJson.put(attributeName, fieldDesc);
				} else {
					valueInJson.put(attributeName, attributeValue);
				}
			} else {
				valueInJson.put(attributeName, attributeValue);
			}
		}
		json.put(fieldName, valueInJson);
	}

	/**
	 * Processes the style attribute of an XML node and converts it into a JSON object.
	 *
	 * @param styleInJson The JSON object to populate with style attributes.
	 * @param attribute The XML attribute node containing style information.
	 * @throws JSONException If there is an error in processing the style attribute.
	 */
	private void processStyleAttribute(JSONObject styleInJson, Node attribute) throws JSONException {
		String[] styleValue = attribute.getNodeValue().split(";");
		for (String value : styleValue) {
			String[] newStyleValue = value.split(":");
			styleInJson.put(newStyleValue[0], newStyleValue[1]);
		}
	}

	/**
	 * Retrieves and returns the child nodes of a given XML node.
	 *
	 * @param node The XML node whose child nodes are to be retrieved.
	 * @return An array of child nodes of the specified XML node.
	 */
	private Node[] getChildNodes(Node node) {
		NodeList nodeList = node.getChildNodes();
		List<Node> childNodes = new ArrayList<>();

		for (int j = 0; j < nodeList.getLength(); j++) {
			Node childNode = nodeList.item(j);
			if (childNode.getNodeType() == 1) {
				childNodes.add(childNode);
			}
		}
		return childNodes.toArray(new Node[0]);
	}

	/**
	 * Constructs and returns a map containing key-value pairs for query substitution.
	 *
	 * @param body The search request containing parameters like corporate ID, user ID, search criteria, page number, and page size.
	 * @return A map containing key-value pairs for query substitution.
	 */
	private Map<String, String> getValuesMap(@Valid XGlobalSearchRequest body) {
		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put("bankgroup",	"CSME");
		valuesMap.put("unitcode", body.getCorporateId());
		valuesMap.put("modulename", body.getModule());
		valuesMap.put("userid", body.getUserId());
		valuesMap.put("pageNo", body.getPage().toString());
		valuesMap.put("pageSize", body.getPerPage().toString());
		return valuesMap;
	}

	/**
	 * Constructs the SQL query for the iBox (Inbox) based on the provided search request and inquiry XML.
	 *
	 * @param body       The search request containing parameters like corporate ID, user ID, search criteria, page number, and page size.
	 * @param inquiryXml The XML document representing the inquiry structure.
	 * @return A StringBuilder object containing the constructed SQL query for the iBox.
	 * @throws Exception If an error occurs during the construction of the SQL query.
	 */

	public StringBuilder getIboxQuery(@Valid XGlobalSearchRequest body, Document inquiryXml) throws Exception {
		StringBuilder iboxQueryBuilder = new StringBuilder();
		Element rootElement = inquiryXml.getDocumentElement();
		String inquirySqlQueryFirst = getValueByTagName(rootElement, "InquirySqlQueryFirst");
		String inquirySqlQueryEnd = getValueByTagName(rootElement, "InquirySqlQueryEnd");
		iboxQueryBuilder.append(inquirySqlQueryFirst); 
		Map<String, String> valuesMap = getValuesMap(body);
		StringSubstitutor substitutor = new StringSubstitutor(valuesMap);
		String[] moduleList = body.getModule().split(";");
		for (int i = 0; i < moduleList.length; i++) {
			String moduleQuery = getModuleQuery(moduleList[i], inquiryXml);
			moduleQuery = substitutor.replace(moduleQuery);
			iboxQueryBuilder.append(moduleQuery);
			iboxQueryBuilder.append(" AND ( ");
			NodeList commonNdList = inquiryXml.getElementsByTagName("CommonInquiry");
			Node chCmNd = commonNdList.item(0);
			Node[] chCmNdList = getChildNodes(chCmNd);
			if (chCmNdList.length > 0) {
				boolean isFirstAttribute = true;
				for (Node childNodeCm : chCmNdList) {
					if (childNodeCm.getNodeType() == 1) {
						NamedNodeMap childCmNodeAttr = childNodeCm.getAttributes();
						for (int k = 0; k < childCmNodeAttr.getLength(); k++) {
							Node attribute = childCmNodeAttr.item(k);
							String attributeName = attribute.getNodeName();
							String attributeValue = attribute.getNodeValue();
							if (attributeName.equalsIgnoreCase("value")) {
								if(body.getSearch() != null && body.getSearch() != "") {
									if (!isFirstAttribute) {
										iboxQueryBuilder.append(" OR ");
									}
									String mappedAttribute = mapAttribute(moduleList[i],attributeValue);
									iboxQueryBuilder.append(mappedAttribute).append(" like '%").append(body.getSearch()).append("%' ");
									isFirstAttribute = false;
								}
							}
						}
					}
				}
			}
			iboxQueryBuilder.append(") ");
			if (i < moduleList.length - 1) {
				iboxQueryBuilder.append(" UNION ALL ");
			}
		}
		String result = substitutor.replace(inquirySqlQueryEnd);
		iboxQueryBuilder.append(result);

		if (!moduleList[0].equalsIgnoreCase("ALL")) {
			iboxQueryBuilder.append(" AND C_MODULE IN (");
			for (int i = 0; i < moduleList.length; i++) {
				iboxQueryBuilder.append("'");
				iboxQueryBuilder.append(moduleList[i]);
				iboxQueryBuilder.append("'");
				// Add a comma if it's not the last element
				if (i < moduleList.length - 1) {
					iboxQueryBuilder.append(", ");
				}
			}
			iboxQueryBuilder.append(")");
		}
		return iboxQueryBuilder;
	}
	/**
	 * Retrieves the value of an XML element by its tag name.
	 *
	 * @param element The XML element from which to retrieve the value.
	 * @param tagName The tag name of the XML element whose value is to be retrieved.
	 * @return The value of the XML element with the specified tag name, or null if not found.
	 */
	private String getValueByTagName(Element element, String tagName) {
		NodeList nodeList = element.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			Node node = nodeList.item(0);
			return node.getTextContent();
		}
		return null;
	}
	/**
	 * Maps the attribute name based on the module and attribute value.
	 *
	 * @param module The module for which the attribute is being mapped.
	 * @param attributeValue The value of the attribute to be mapped.
	 * @return The mapped attribute name.
	 */
	private String mapAttribute(String module, String attributeValue) {
		if (attributeValue.equalsIgnoreCase("AMOUNT")) {
			if (module.equalsIgnoreCase("IMLC")) {
				return "LC_AMT";
			} else if (module.equalsIgnoreCase("OWGT")) {
				return "GTEE_AMT";
			} else if (module.equalsIgnoreCase("EXCO")) {
				return "COLL_AMT";
			}
		} else if (attributeValue.equalsIgnoreCase("CURRENCY")) {
			if (module.equalsIgnoreCase("IMLC")) {
				return "LC_CCY";
			} else if (module.equalsIgnoreCase("OWGT")) {
				return "GTEE_CCY";
			} else if (module.equalsIgnoreCase("EXCO")) {
				return "COLL_CCY";
			}
		} 
		return attributeValue; // Default case
	}
	/**
	 * Handles exceptions that occur during the execution of the inquiry process.
	 * 
	 * @param xInquiryListResponse The XInquiryListResponse object to set error status and description.
	 * @param e The Exception that occurred during the inquiry process.
	 */
	private void handleException(XInquiryListResponse xInquiryListResponse, Exception e) {
		xInquiryListResponse.setStatusCode("99");
		xInquiryListResponse.setStatusDescription("General Exception");
		logger.error("Exception occurred: " + e.getMessage(), e);
	}
	/**
	 * Constructs the SQL query for a specific module based on the provided module name and inquiry XML.
	 *
	 * @param moduleName The name of the module for which the SQL query is to be constructed.
	 * @param inquiryXml The XML document representing the inquiry structure.
	 * @return A String containing the constructed SQL query for the specified module.
	 */
	private static String getModuleQuery(String moduleName, Document inquiryXml) {
		Element rootElement = inquiryXml.getDocumentElement();
		NodeList moduleNodes = rootElement.getElementsByTagName(moduleName);

		if (moduleNodes.getLength() > 0) {
			Element moduleElement = (Element) moduleNodes.item(0);
			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append(moduleElement.getTextContent().trim());
			return queryBuilder.toString();
		}

		return null;
	}

	/**
	 * Constructs the SQL query to count the number of records for a specific module based on the provided search request and inquiry XML.
	 *
	 * @param body       The search request containing parameters like corporate ID, user ID, search criteria, page number, and page size.
	 * @param inquiryXml The XML document representing the inquiry structure.
	 * @return A StringBuilder object containing the constructed SQL query to count records for the specified module.
	 * @throws Exception If an error occurs during the construction of the SQL query.
	 */
	private static String getModuleQueryCount(String moduleName, Document inquiryXml) {
		Element rootElement = inquiryXml.getDocumentElement();
		NodeList moduleNodes = rootElement.getElementsByTagName(moduleName+"COUNT");

		if (moduleNodes.getLength() > 0) {
			Element moduleElement = (Element) moduleNodes.item(0);
			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append(moduleElement.getTextContent().trim());
			return queryBuilder.toString();
		}

		return null;
	}

	public static void main(String[] args) {
		try {
			String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
					"<root>\n" +
					"    <InquirySqlQueryFirst><![CDATA[ SELECT * FROM ( SELECT A.*, rownum r FROM ( ]]></InquirySqlQueryFirst>\n" +
					"    <InquirySqlQueryMiddle><![CDATA[ SELECT * FROM CETRX.TRX_INBOX  ]]></InquirySqlQueryMiddle>\n" +
					"    <InquirySqlQueryEnd><![CDATA[ ORDER BY CUST_NO ) A WHERE rownum < (( ${pageNo} * ${pageSize}) + 1 ) ) WHERE WHERE ( 1=1 ) AND C_BK_GROUP_ID = 'CSBANK' AND  C_UNIT_CODE = '${unitcode}' AND r >= ((( ${pageNo} -1) * ${pageSize}) + 1) ]]></InquirySqlQueryEnd>\n" +
					"    <IMLC><![CDATA[ SELECT * FROM CETRX.IMLC_EM_ISSUE ]]></IMLC>\n" +
					"    <GTEE><![CDATA[ SELECT * FROM CETRX.OWGT_EM_GTEEDTLS ]]></GTEE>\n" +
					"    <CommonInquiry>\n" +
					"        <C_TRX_STATUS value=\"C_TRX_STATUS\" labelname=\"Transaction Status\" style=\"fontfamily:inter;fontstyle:normal;fontweight:w600;color:#F16727;fontsize:9;\" orderby=\"1\"/>\n" +
					"        <C_MAIN_REF value=\"C_MAIN_REF\" labelname=\"Reference\" style=\"fontfamily:inter;fontstyle:normal;fontweight:w400;color:#3E3E3E;fontsize:9;\" orderby=\"3\"/>\n" +
					"        <C_MODULE value=\"C_MODULE\" labelname=\"Module\" style=\"fontfamily:inter;fontstyle:normal;fontweight:w600;color:#F16727;fontsize:9;\" orderby=\"4\"/>\n" +
					"        <C_FUNC_ID value=\"C_FUNC_ID\" labelname=\"Function Id\" style=\"fontfamily:inter;fontstyle:normal;fontweight:w400;color:#3E3E3E;fontsize:9;\" orderby=\"5\"/>\n" +
					"        <I_EVENT_TIMES value=\"I_EVENT_TIMES\" labelname=\"Event Times\" style=\"fontfamily:inter;fontstyle:normal;fontweight:w400;color:#3E3E3E;fontsize:9;\" orderby=\"6\"/>\n" +
					"        <C_BK_GROUP_ID value=\"C_BK_GROUP_ID\" labelname=\"Bank Group Id\" style=\"fontfamily:inter;fontstyle:normal;fontweight:w400;color:#3E3E3E;fontsize:9;\" orderby=\"6\"/>\n" +
					"        <C_UNIT_CODE value=\"C_UNIT_CODE\" labelname=\"Corporate Id\" style=\"fontfamily:inter;fontstyle:normal;fontweight:w400;color:#3E3E3E;fontsize:9;\" orderby=\"6\"/>\n" +
					"    </CommonInquiry>\n" +
					"</root>";

			// Convert XML string to Document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xmlString)));

			// Test getModuleQuery method
			String moduleQueryIMLC = getModuleQuery("IMLC", document);
			if (moduleQueryIMLC != null) {
				System.out.println("Module Query for IMLC:");
				System.out.println(moduleQueryIMLC);
			} else {
				System.out.println("Module not found: IMLC");
			}

			String moduleQueryGTEE = getModuleQuery("GTEE", document);
			if (moduleQueryGTEE != null) {
				System.out.println("\nModule Query for GTEE:");
				System.out.println(moduleQueryGTEE);
			} else {
				System.out.println("\nModule not found: GTEE");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
