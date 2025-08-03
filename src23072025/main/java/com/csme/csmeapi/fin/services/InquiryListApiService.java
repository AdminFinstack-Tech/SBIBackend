/**
 * Provides services for handling inquiries and responses related to financial transactions.
 * This class interacts with databases and XML files to retrieve and process transaction information.
 */
package com.csme.csmeapi.fin.services;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

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

import com.cs.base.xml.XMLManager;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinConfig;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XInfoListObj;
import com.csme.csmeapi.fin.models.XInquireListObj;
import com.csme.csmeapi.fin.models.XInquiryListRequest;
import com.csme.csmeapi.fin.models.XInquiryListResponse;
import com.csme.csmeapi.fin.models.XproductInfoList;
import com.csme.csmeapi.fin.models.XproductInfoListNofTransaction;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provides services for handling inquiries and responses related to financial transactions.
 * This class interacts with databases and XML files to retrieve and process transaction information.
 */
public class InquiryListApiService {

	FinUtil finUtil = new FinUtil();

	/**
	 * Logger for logging API-related events and information.
	 */
	Logger logger = LogManager.getLogger("CSMEMobile");

	/**
	 * Security data source identifier used for database connections.
	 */
	private String secuDs = null;

	/**
	 * Language setting for the API service.
	 */
	public String Lang = null;

	/**
	 * Constructor for the InquiryListApiService class.
	 * Initializes the security data source and handles exceptions.
	 */
	public InquiryListApiService() {
		try {
			secuDs = FinUtil.SECDS; // Initialize the security data source
		} catch (Exception e) {
			e.printStackTrace(); // Print stack trace for any initialization exceptions
		}
	}


	/**
	 * Retrieves inquiry information based on the provided request parameters.
	 * 
	 * @param body The request body containing inquiry parameters.
	 * @param requestId The unique identifier for the request.
	 * @param sequence The sequence number for tracking the request order.
	 * @return An XInquiryListResponse object containing the inquiry response.
	 */
	public XInquiryListResponse getInquiryInfo(@Valid XInquiryListRequest body, UUID requestId, Long sequence) {
		XInquiryListResponse xInquiryListResponse = new XInquiryListResponse();
		try {
			// Get and set the language based on corporate ID and user ID from the request body
			Lang = FinUtil.getLang(body.getCorporateId(), body.getUserId());
			// Build the inquiry response based on the request parameters
			buildInquiryResponse(body, requestId, sequence, xInquiryListResponse);
		} catch (Exception e) {
			// Handle any exceptions by setting the response status to indicate an error
			handleException(xInquiryListResponse, e);
		}
		return xInquiryListResponse;
	}


	/**
	 * Builds the inquiry response based on the provided request parameters.
	 * 
	 * @param body The validated XInquiryListRequest object containing inquiry parameters.
	 * @param requestId The unique identifier for the request.
	 * @param sequence The sequence number for tracking the request order.
	 * @param xInquiryResponse The XInquiryListResponse object to be populated with inquiry data.
	 */
	private void buildInquiryResponse(@Valid XInquiryListRequest body, UUID requestId, Long sequence,
			XInquiryListResponse xInquiryResponse) {
		try {
			// Set the request ID and message ID in the response
			xInquiryResponse.setRequestId(requestId.toString());
			xInquiryResponse.setMessageId(sequence.toString());
			// Set the timestamp in the response
			setTimestamp(xInquiryResponse);

			// Extract page and page size information from the request body
			int pageNo = body.getPage();
			int pageSize = body.getPerPage();
			// Split module names separated by semicolons into an array
			String[] moduleList = body.getModule().split(";");

			// Construct the file path for the common XML document based on request type
			String filePath = FinUtil.intPath + "Common" + body.getType() + ".xml";
			// Parse the common XML document into a DOM object
			Document commonXmlDoc = XMLManager.xmlFileToDom(filePath);
			logger.info("Convert Inquiry Dom into String is : " + XMLManager.convertToString(commonXmlDoc));

			// Generate the SQL query for the inquiry based on request parameters and common XML
			StringBuilder commonSqlBuilder = getIboxQuery(body, commonXmlDoc);
			String commonSqlQuery = commonSqlBuilder.toString();
			logger.info("Execute SqlQuery is " + commonSqlQuery);

			// Execute the SQL query and retrieve the result set
			if (commonSqlQuery != null && !commonSqlQuery.isEmpty()) {
				SQLRecordSet rs = WSDBHelper.executeQuery(commonSqlQuery, secuDs);
				logger.info("Execute SqlQuery result count is : " + rs.getRecordCount());

				// Set paging information in the response
				setPagingInfo(xInquiryResponse, pageNo, pageSize, rs);
				// Populate product list information in the response
				setProductList(xInquiryResponse, moduleList, body, commonXmlDoc);
				// Populate inquiry information in the response
				populateInquiryInfo(xInquiryResponse, rs, commonXmlDoc, moduleList, body);
				logger.info("final Value is   : " + xInquiryResponse);
			}
		} catch (Exception e) {
			// Handle any exceptions encountered during inquiry response building
			handleException(xInquiryResponse, e);
		}
	}

	/**
	 * Sets the product list information in the inquiry response based on the provided module list.
	 * This method populates the product information including module icons, descriptions, styles, and transaction counts.
	 * If a module description is not found in the database, it uses the description from the icon description document.
	 * 
	 * @param xInquiryResponse The XInquiryListResponse object to be populated with product information.
	 * @param moduleList The array of module names for which product information is to be included.
	 * @param body The validated XInquiryListRequest object containing inquiry parameters.
	 * @param commonXmlDoc The common XML document used for retrieving product descriptions.
	 * @throws Exception If an error occurs while setting the product list.
	 */
	@SuppressWarnings("unchecked")
	private void setProductList(XInquiryListResponse xInquiryResponse, String[] moduleList, @Valid XInquiryListRequest body, Document commonXmlDoc) throws Exception {
		// Initialize an empty list to hold product information
		List<XproductInfoList> productsAccessList = new ArrayList<XproductInfoList>();
		// Get the icon description document
		Document iconDescDoc = FinUtil.getIconDescriptionDoc();
		// Loop through each module in the module list
		for (String module : moduleList) {
			// Create a new XproductInfoList object for each module
			XproductInfoList XproductInfoList = new XproductInfoList();
			// Set the module name in the XproductInfoList
			XproductInfoList.setModule(module);
			// Get the icon link for the module from the icon description document
			String icon = FinUtil.getIconLink(iconDescDoc, module);
			XproductInfoList.setIcon(icon);
			// Get the module description in the current language
			String moduleDesc = FinUtil.getDBFieldDesc(module, this.Lang);
			// If the module description is null or empty, use the description from the icon description document
			if (moduleDesc != null && !moduleDesc.isEmpty()) {
				XproductInfoList.setDescription(moduleDesc);
			} else {
				String desc = FinUtil.getModuleDescription(iconDescDoc, module);
				XproductInfoList.setDescription(desc);
			}
			// Get the style information for the module
			JSONObject style = FinUtil.getModuleStyle(iconDescDoc, module);
			Map<String, Object> styleMap = new HashMap<>();
			// Iterate over the style keys and put them into the style map
			if (style != null && style.length() > 0) {
				Iterator<String> keys = style.keys();
				while (keys.hasNext()) {
					String key = keys.next();
					Object value = style.get(key);
					styleMap.put(key, value);
				}
				XproductInfoList.setStyle(styleMap);
			}
			// Set the number of pending and total transactions for the module
			XproductInfoListNofTransaction nofTransaction = new XproductInfoListNofTransaction();
			nofTransaction.setPendingCount(Integer.toString(getPendingCountTrx(body, module, "P")));
			nofTransaction.setTotalCount(Integer.toString(getCountTransaction(body, module, "", commonXmlDoc)));
			XproductInfoList.setNofTransaction(nofTransaction);
			// Add the XproductInfoList to the products access list
			productsAccessList.add(XproductInfoList);
		}
		// Set the products access list in the inquiry response
		xInquiryResponse.setProductsAccessList(productsAccessList);
	}

	/**
	 * Sets the timestamp in the inquiry response.
	 * 
	 * @param xInquiryResponse The response object to set the timestamp.
	 */
	private void setTimestamp(XInquiryListResponse xInquiryResponse) {
		// Generate the timestamp in the format "yyyy-MM-dd'T'HH:mm:ss'Z'"
		String setTimeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new java.util.Date());

		// Set the generated timestamp in the response object
		xInquiryResponse.setTimestamp(setTimeStamp);
	}

	/**
	 * Sets paging information in the inquiry response.
	 * @param xInquiryListResponse The response object to set the paging information.
	 * @param pageNo The current page number.
	 * @param pageSize The number of items per page.
	 * @param rs The SQLRecordSet containing the query result.
	 */
	private void setPagingInfo(XInquiryListResponse xInquiryListResponse, int pageNo, int pageSize, SQLRecordSet rs) {
		xInquiryListResponse.setPage(Long.valueOf(pageNo));
		xInquiryListResponse.setPerPage(Long.valueOf(pageSize));
		xInquiryListResponse.setTotal(Long.valueOf(rs.getTotalDBRecords()));
		xInquiryListResponse.setHasNext(Long.valueOf(pageNo + 1));
		xInquiryListResponse.setHasPrev(Long.valueOf(pageNo - 1));
		xInquiryListResponse.setMessageId(xInquiryListResponse.getRequestId());
	}


	/**
	 * Populates inquiry information in the response object based on the query result.
	 * @param xInquiryListResponse The response object to populate with inquiry information.
	 * @param rs The SQLRecordSet containing the query result.
	 * @param inquiryXml The XML document for inquiry configuration.
	 * @param moduleList The list of modules for inquiry.
	 * @param body The request body for the inquiry.
	 * @throws Exception If there's an error during population.
	 */
	private void populateInquiryInfo(XInquiryListResponse xInquiryListResponse, SQLRecordSet rs, Document inquiryXml, String[] moduleList, XInquiryListRequest body) throws Exception {
		HashMap<String, Document> moduleXml = getModuleListDocument(moduleList, body);
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
	 * Retrieves module XML documents based on the provided module list and request body type.
	 * @param moduleList The list of modules for which XML documents are needed.
	 * @param body The request body containing type information.
	 * @return A HashMap containing module names as keys and corresponding XML documents as values.
	 * @throws Exception If there's an error during XML document retrieval.
	 */
	private HashMap<String, Document> getModuleListDocument(String[] moduleList, XInquiryListRequest body) throws Exception {
		HashMap<String, Document> mapDoc = new HashMap<String, Document>();
		for (String module : moduleList) {
			String filePath = FinUtil.intPath + module + body.getType() + ".xml";
			Document moduleDoc = XMLManager.xmlFileToDom(filePath);
			mapDoc.put(module, moduleDoc);
		}
		return mapDoc;
	}

	/**
	 * Builds the common information JSON object based on the SQL record and inquiry XML structure.
	 * @param sqlRecord The SQL record containing data.
	 * @param inquiryXml The XML document for inquiry configuration.
	 * @return The JSON object containing common information.
	 * @throws Exception If there's an error during JSON object creation.
	 */
	private JSONObject buildCommonInfoList(SQLRecord sqlRecord, Document inquiryXml) throws Exception {
		JSONObject commonJson = new JSONObject();
		NodeList commonNdList = inquiryXml.getElementsByTagName("CommonInquiry");
		Node chCmNd = commonNdList.item(0);
		Node[] chCmNdList = getChildNodes(chCmNd);

		if (chCmNdList.length > 0) {
			for (Node childNodeCm : chCmNdList) {
				if (childNodeCm.getNodeType() == Node.ELEMENT_NODE) {
					processNode(commonJson, childNodeCm, sqlRecord);
				}
			}
		}
		return commonJson;
	}

	/**
	 * Builds the dynamic information JSON object based on the SQL record, inquiry XML structure, and module XML documents.
	 * @param sqlRecord The SQL record containing data.
	 * @param inquiryXml The XML document for inquiry configuration.
	 * @param moduleXml The HashMap containing module names as keys and corresponding XML documents as values.
	 * @return The JSON object containing dynamic information.
	 * @throws Exception If there's an error during JSON object creation.
	 */
	private JSONObject buildDynamicInfoList(SQLRecord sqlRecord, Document inquiryXml, HashMap<String, Document> moduleXml) throws Exception {
		JSONObject dynamicJson = new JSONObject();
		String module = moduleXml.containsKey("ALL") ? "ALL" : sqlRecord.getValue("C_MODULE");
		Document newInquiryXml = moduleXml.get(module);
		NodeList dynamicNdList = newInquiryXml.getElementsByTagName("DynamicInquiry");
		Node chDynamicNd = dynamicNdList.item(0);
		Node[] chDynamicNdList = getChildNodes(chDynamicNd);

		if (chDynamicNdList.length > 0) {
			for (Node childNodeCm : chDynamicNdList) {
				if (childNodeCm.getNodeType() == Node.ELEMENT_NODE) {
					processNode(dynamicJson, childNodeCm, sqlRecord);
				}
			}
		}
		return dynamicJson;
	}

	/**
	 * Processes a node from an XML document and constructs the corresponding JSON object.
	 * @param json The JSON object to which the processed node data will be added.
	 * @param childNode The node from the XML document.
	 * @param sqlRecord The SQL record containing data to be used for value substitution.
	 * @throws Exception If there's an error during node processing.
	 */
	private void processNode(JSONObject json, Node childNode, SQLRecord sqlRecord) throws Exception {
		String fieldName = childNode.getNodeName();
		NamedNodeMap childCmNodeAttr = childNode.getAttributes();
		JSONObject valueInJson = new JSONObject();

		for (int k = 0; k < childCmNodeAttr.getLength(); k++) {
			Node attribute = childCmNodeAttr.item(k);
			String attributeName = attribute.getNodeName();
			String attributeValue = attribute.getNodeValue();

			// Check for special attribute types
			if (attributeName.equalsIgnoreCase("value")) {
				// Substitute value from SQL record if attribute is 'value'
				attributeValue = sqlRecord.getValue(attributeValue);
				valueInJson.put(attributeName, attributeValue);
			} else if (attributeName.equalsIgnoreCase("style")) {
				// Process 'style' attribute separately
				JSONObject styleInJson = new JSONObject();
				processStyleAttribute(styleInJson, attribute);
				valueInJson.put(attributeName, styleInJson);
			} else if (attributeName.equalsIgnoreCase("labelname")) {
				// Get field description if attribute is 'labelname'
				String fieldDesc = FinUtil.getDBFieldDesc(fieldName, this.Lang);
				if (fieldDesc != null && !fieldDesc.isEmpty()) {
					valueInJson.put(attributeName, fieldDesc);
				} else {
					valueInJson.put(attributeName, attributeValue);
				}
			} else {
				// Add other attributes as they are
				valueInJson.put(attributeName, attributeValue);
			}
		}
		// Add the processed node data to the main JSON object
		json.put(fieldName, valueInJson);
	}


	/**
	 * Processes a style attribute from an XML node and constructs the corresponding JSON object.
	 * @param styleInJson The JSON object to which the processed style data will be added.
	 * @param attribute The style attribute node from the XML document.
	 * @throws JSONException If there's an error during style attribute processing.
	 */
	private void processStyleAttribute(JSONObject styleInJson, Node attribute) throws JSONException {
		// Split style attribute value into individual style properties
		String[] styleValue = attribute.getNodeValue().split(";");
		for (String value : styleValue) {
			// Split style property into key-value pair
			String[] newStyleValue = value.split(":");
			// Add style property to the JSON object
			styleInJson.put(newStyleValue[0], newStyleValue[1]);
		}
	}


	/**
	 * Retrieves child nodes of a given XML node.
	 * @param node The XML node whose child nodes are to be retrieved.
	 * @return An array of child nodes.
	 */
	private Node[] getChildNodes(Node node) {
		NodeList nodeList = node.getChildNodes();
		List<Node> childNodes = new ArrayList<>();

		for (int j = 0; j < nodeList.getLength(); j++) {
			Node childNode = nodeList.item(j);
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				childNodes.add(childNode);
			}
		}
		return childNodes.toArray(new Node[0]);
	}

	/**
	 * Constructs a map of values based on the provided inquiry request.
	 * @param body The inquiry request containing necessary data.
	 * @return A map of values with keys like "bankgroup", "unitcode", etc.
	 */
	private Map<String, String> getValuesMap(@Valid XInquiryListRequest body) {
		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put("bankgroup", "CSME");
		valuesMap.put("unitcode", body.getCorporateId());
		valuesMap.put("modulename", body.getModule());
		valuesMap.put("userid", body.getUserId());
		valuesMap.put("pageNo", body.getPage().toString());
		valuesMap.put("pageSize", body.getPerPage().toString());
		return valuesMap;
	}

	/**
	 * Constructs the SQL query for ibox based on the inquiry request and XML configuration.
	 * @param body The inquiry request containing filter and module information.
	 * @param inquiryXml The XML document containing inquiry configuration.
	 * @return A StringBuilder containing the constructed SQL query for ibox.
	 * @throws Exception If there's an error during query construction.
	 */
	public StringBuilder getIboxQuery(XInquiryListRequest body, Document inquiryXml) throws Exception {
		StringBuilder iboxQueryBuilder = new StringBuilder();
		Element rootElement = inquiryXml.getDocumentElement();
		String inquirySqlQueryFirst = getValueByTagName(rootElement, "InquirySqlQueryFirst");
		String inquirySqlQueryEnd = getValueByTagName(rootElement, "InquirySqlQueryEnd");
		iboxQueryBuilder.append(inquirySqlQueryFirst);
		Map<String, String> valuesMap = getValuesMap(body);
		StringSubstitutor substitutor = new StringSubstitutor(valuesMap);
		Map<String, String> propMap = FinConfig.getApplicationProperty();
		List<XInfoListObj> filterby = body.getFilterby();
		String[] moduleList = body.getModule().split(";");
		for (int i = 0; i < moduleList.length; i++) {
			String moduleQuery = getModuleQuery(moduleList[i], inquiryXml);
			moduleQuery = substitutor.replace(moduleQuery);
			iboxQueryBuilder.append(moduleQuery);

			if (filterby.size() > 0) {
				XInfoListObj xInfoListObj = filterby.get(0);
				for (Field field : xInfoListObj.getClass().getDeclaredFields()) {
					field.setAccessible(true);
					Object values = field.get(xInfoListObj);
					String keyName = propMap.get(moduleList[i] + field.getName());

					if (values != null) {
						iboxQueryBuilder.append(" AND " + keyName + " like (");
						iboxQueryBuilder.append("'%" + values + "%'");
						iboxQueryBuilder.append(")");
					}
				}
			}

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
	 * Retrieves the count of pending transactions based on the request parameters.
	 * 
	 * @param body    The validated inquiry list request.
	 * @param module  The module for which pending transactions are counted.
	 * @param status  The status of transactions to consider ('P' for pending, 'T' for transferred).
	 * @return The count of pending transactions.
	 */
	private int getPendingCountTrx(@Valid XInquiryListRequest body, String module, String status) {
		int count = 0;
		try {
			Map<String, String> propMap = FinConfig.getApplicationProperty();
			StringBuilder iboxQueryBuilder = new StringBuilder();
			List<XInfoListObj> filterby = body.getFilterby();
			// Construct the SQL query based on request parameters
			String sqlQuery = "SELECT C_TRX_STATUS FROM CETRX.TRX_INBOX WHERE C_UNIT_CODE = '" + body.getCorporateId() + "'";
			if (!module.equalsIgnoreCase("ALL")) {
				sqlQuery += " AND C_MODULE = '" + module + "'";
			}
			if (status != null && (status.equalsIgnoreCase("P") || status.equalsIgnoreCase("T"))) {
				sqlQuery += " AND C_TRX_STATUS IN ('P', 'T') AND "
	            		+ "C_TRX_ID NOT IN (SELECT C_TRX_ID FROM CETRX.TRX_AUTH_LIST WHERE  "
	            		+ "C_GRP_CODE = '"+body.getCorporateId()+"' "
	            		+ " AND  C_USER_ID = '"+body.getUserId()+"'  "
	            		+ " AND  C_UNIT_CODE = '"+body.getCorporateId()+"' ) "
	            		+ "AND C_TRX_ID IN(SELECT C_TRX_ID FROM CETRX.TRX_MATRIX_LIST "
	            		+ "WHERE  C_UNIT_CODE = '"+body.getCorporateId()+"'  "
	            		+ " ) ORDER BY CUST_NO ";
			}
			
			iboxQueryBuilder.append(sqlQuery);

			if (filterby.size() > 0) {
				XInfoListObj xInfoListObj = filterby.get(0);
				for (Field field : xInfoListObj.getClass().getDeclaredFields()) {
					field.setAccessible(true);
					Object values = field.get(xInfoListObj);
					String keyName = propMap.get(module + field.getName());

					if (values != null) {
						iboxQueryBuilder.append(" AND " + keyName + " like (");
						iboxQueryBuilder.append("'%" + values + "%'");
						iboxQueryBuilder.append(")");
					}
				}
			}

			// Log the SQL query being executed
			logger.info("Execute SqlQuery is " + iboxQueryBuilder.toString());

			// Execute the query and get the record count
			SQLRecordSet rs = WSDBHelper.executeQuery(iboxQueryBuilder.toString(), secuDs);
			count = rs.getRecordCount();

			// Log the count of pending transactions
			logger.info("Count transaction is " + count);
		} catch (Exception e) {
			// Log any exceptions that occur during transaction counting
			logger.error("Exception occurred in getPendingCountTrx function: " + e.getMessage());
		}
		return count;
	}
	
	/**
	 * Retrieves the count of transactions based on the request parameters.
	 * 
	 * @param body        The validated inquiry list request.
	 * @param module      The module for which transactions are counted.
	 * @param status      The status of transactions to consider (unused in this method).
	 * @param commonXmlDoc The common XML document used for querying.
	 * @return The count of transactions.
	 */
	private int getCountTransaction(@Valid XInquiryListRequest body, String module, String status, Document commonXmlDoc) {
		int count = 0;
		try {
			Map<String, String> propMap = FinConfig.getApplicationProperty();
			StringBuilder iboxQueryBuilder = new StringBuilder();
			List<XInfoListObj> filterby = body.getFilterby();
			String moduleQuery = null;
			// Determine the module query based on the request type
			if (body.getType() != null && body.getType().toString().equals("AUTH")) {
				moduleQuery = getModuleQueryCount(module, commonXmlDoc);
			} else {
				moduleQuery = getModuleQuery(module, commonXmlDoc);
			}

			// Prepare values for substitution in the module query
			Map<String, String> valuesMap = new HashMap<String, String>();
			valuesMap.put("unitcode", body.getCorporateId());
			valuesMap.put("module", module);
			StringSubstitutor substitutor = new StringSubstitutor(valuesMap);
			moduleQuery = substitutor.replace(moduleQuery);
			
			iboxQueryBuilder.append(moduleQuery);

			if (filterby.size() > 0) {
				XInfoListObj xInfoListObj = filterby.get(0);
				for (Field field : xInfoListObj.getClass().getDeclaredFields()) {
					field.setAccessible(true);
					Object values = field.get(xInfoListObj);
					String keyName = propMap.get(module + field.getName());

					if (values != null) {
						iboxQueryBuilder.append(" AND " + keyName + " like (");
						iboxQueryBuilder.append("'%" + values + "%'");
						iboxQueryBuilder.append(")");
					}
				}
			}

			// Log the SQL query being executed
			logger.info("Execute SqlQuery is " + iboxQueryBuilder.toString());

			// Execute the query and get the record count
			SQLRecordSet rs = WSDBHelper.executeQuery(iboxQueryBuilder.toString(), secuDs);
			count = rs.getRecordCount();

			// Log the count of transactions
			logger.info("Count transaction is " + count);
		} catch (Exception e) {
			// Log any exceptions that occur during transaction counting
			logger.error("Exception occurred in getCountTransaction function: " + e.getMessage());
		}
		return count;
	}


	/**
	 * Retrieves the text content of an XML element by its tag name.
	 * @param element The XML element.
	 * @param tagName The tag name to search for.
	 * @return The text content of the first element found with the specified tag name.
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
	 * Handles exceptions by setting the appropriate status code and description in the inquiry response object.
	 * Also prints the exception stack trace.
	 * @param xInquiryListResponse The inquiry response object.
	 * @param e The exception that occurred.
	 */
	private void handleException(XInquiryListResponse xInquiryListResponse, Exception e) {
		xInquiryListResponse.setStatusCode("99");
		xInquiryListResponse.setStatusDescription("General Exception");
		e.printStackTrace();
	}

	/**
	 * Gets the SQL query for a specific module name from the inquiry XML document.
	 * @param moduleName The name of the module for which the query is needed.
	 * @param inquiryXml The inquiry XML document.
	 * @return The SQL query for the specified module.
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
	 * Gets the SQL query count for a specific module name from the inquiry XML document.
	 * @param moduleName The name of the module for which the query count is needed.
	 * @param inquiryXml The inquiry XML document.
	 * @return The SQL query count for the specified module.
	 */
	private static String getModuleQueryCount(String moduleName, Document inquiryXml) {
		Element rootElement = inquiryXml.getDocumentElement();
		NodeList moduleNodes = rootElement.getElementsByTagName(moduleName + "COUNT");

		if (moduleNodes.getLength() > 0) {
			Element moduleElement = (Element) moduleNodes.item(0);
			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append(moduleElement.getTextContent().trim());
			return queryBuilder.toString();
		}

		return null;
	}
}
