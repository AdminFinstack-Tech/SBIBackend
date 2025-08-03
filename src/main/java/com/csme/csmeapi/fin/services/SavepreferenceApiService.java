package com.csme.csmeapi.fin.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.text.StringSubstitutor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cs.base.xml.XMLManager;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.GetPreferenceResponse;
import com.csme.csmeapi.fin.models.PreferenceRequestPost;
import com.csme.csmeapi.fin.models.PreferenceResponsePost;
import com.csme.csmeapi.fin.models.XPreferenceResponseObj;
import com.csme.csmeapi.fin.util.CSMEMobLogUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
/**
 * Service class for saving preferences and retrieving saved preferences.
 */
public class SavepreferenceApiService {
	
	FinUtil finUtil = new FinUtil();
	
	/**
	 * Represents the database data source name.
	 */
	private static String secuDs = null;
	/**
	 * The logger instance for logging messages.
	 */
	private final static CSMEMobLogUtil logger = new CSMEMobLogUtil();

	/**
	 * The object mapper instance for JSON serialization/deserialization.
	 */
	private static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * The internal path used for file operations.
	 */
	private String intPath = FinUtil.intPath;

	/**
	 * Constructor for initializing the service.
	 */
	public SavepreferenceApiService() {
		try {
			secuDs = FinUtil.SECDS;
		} catch (Exception e) {
			handleException(e, "Initializing SavepreferenceApiService");
		}
	}
	/**
	 * Retrieves saved preferences for a user.
	 * 
	 * @param corporateId Corporate ID of the user.
	 * @param userId      User ID.
	 * @param requestId   Request ID.
	 * @param sequence    Sequence number.
	 * @return GetPreferenceResponse containing the saved preferences.
	 */
	public GetPreferenceResponse getSavePreference(String corporateId, String userId, UUID requestId, Long sequence) {
		GetPreferenceResponse getPreferenceResponse = new GetPreferenceResponse();
		try {
			getPreferenceResponse.setRequestId(requestId.toString());
			getPreferenceResponse.setMessageId(sequence.toString());
			String setTimeStamp = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).format(new Date());
			getPreferenceResponse.setTimestamp(setTimeStamp);
			String filePath = intPath + "Preference.xml";
			Document savePreferenceXML = XMLManager.xmlFileToDom(filePath);
			Element rootElement = savePreferenceXML.getDocumentElement();
			String savePreSql = FinUtil.getValueByTagName(rootElement, "SqlQuery");
			Map < String, String > valuesMap = getValuesMap(corporateId, userId);
			StringSubstitutor substitutor = new StringSubstitutor(valuesMap);
			String finalSavePreSql = substitutor.replace(savePreSql);
			SQLRecordSet rs = WSDBHelper.executeQuery(finalSavePreSql, secuDs);
			if (rs.getRecordCount() > 0) {
				logger.info("Retervie value from database count is :" + rs.getRecordCount());
				List < XPreferenceResponseObj > savePreferenceRespInfo = new ArrayList < XPreferenceResponseObj > ();
				XPreferenceResponseObj xPreferenceResponseObj = new XPreferenceResponseObj();
				JSONObject savePreJson = new JSONObject();
				for (int i = 0, len = rs.getRecordCount(); i < len; i++) {
					SQLRecord record = rs.getSQLRecord(i);
					String key = record.getValue("PREFRENCE_KEY");
					String value = record.getValue("PREFRENCE_VALUE");
					if (key != null && key.equalsIgnoreCase("SEARCH")) {
						savePreJson = handleSearchField(value, savePreJson);
					} else {
						savePreJson.put(key, value);
					}
				}
				JsonNode savePreJsonNode = objectMapper.readTree(savePreJson.toString());
				xPreferenceResponseObj.setPreferenceListInfo(savePreJsonNode);
				savePreferenceRespInfo.add(xPreferenceResponseObj);
				getPreferenceResponse.setSavePreferenceRespInfo(savePreferenceRespInfo);;
				getPreferenceResponse.setStatusCode("00");
				getPreferenceResponse.setStatusDescription("Success");
			} else {
				String defaultQuery = FinUtil.getValueByTagName(rootElement, "DefaultQuery");
				SQLRecordSet rs1 = WSDBHelper.executeQuery(defaultQuery, secuDs);
				if (rs1.getRecordCount() > 0) {
					logger.info("Retervie value from database count is :" + rs1.getRecordCount());
					List < XPreferenceResponseObj > savePreferenceRespInfo = new ArrayList < XPreferenceResponseObj > ();
					XPreferenceResponseObj xPreferenceResponseObj = new XPreferenceResponseObj();
					JSONObject savePreJson = new JSONObject();
					for (int i = 0, len = rs1.getRecordCount(); i < len; i++) {
						SQLRecord record = rs1.getSQLRecord(i);
						String key = record.getValue("PREFRENCE_KEY");
						String value = record.getValue("PREFRENCE_VALUE");
						if (key != null && key.equalsIgnoreCase("SEARCH")) {
							Object jsonObject = XML.toJSONObject(value).getJSONObject("root").get("SEARCH");
							if (jsonObject instanceof JSONArray) {
								savePreJson.put(key, jsonObject);
							} else {
								JSONArray jsonArray = new JSONArray();
								jsonArray.put(jsonObject);
								savePreJson.put(key, jsonArray);
							}
						} else {
							savePreJson.put(key, value);
						}
					}
					JsonNode savePreJsonNode = objectMapper.readTree(savePreJson.toString());
					xPreferenceResponseObj.setPreferenceListInfo(savePreJsonNode);
					savePreferenceRespInfo.add(xPreferenceResponseObj);
					getPreferenceResponse.setSavePreferenceRespInfo(savePreferenceRespInfo);;
					getPreferenceResponse.setStatusCode("00");
					getPreferenceResponse.setStatusDescription("Success");
				}
			}
		} catch (Exception e) {
			handleException(getPreferenceResponse, e, "Savepreference Response post function");
		}
		return getPreferenceResponse;
	}
	/**
	 * Handles the search field processing for preferences.
	 * 
	 * @param value      The value of the search field.
	 * @param savePreJson The JSON object to populate with search field data.
	 * @return The updated JSON object with search field data added.
	 * @throws JSONException If there's an issue with JSON parsing.
	 */
	private JSONObject handleSearchField(String value, JSONObject savePreJson) throws JSONException {
		if (value != null && !value.isEmpty()) {
			JSONObject jsonObject = XML.toJSONObject(value).optJSONObject("root");
			if (jsonObject != null) {
				Object searchObject = jsonObject.opt("SEARCH");
				if (searchObject != null) {
					if (searchObject instanceof JSONArray) {
						savePreJson.put("SEARCH", searchObject);
					} else {
						JSONArray jsonArray = new JSONArray();
						jsonArray.put(searchObject);
						savePreJson.put("SEARCH", jsonArray);
					}
				}
			}
		}
		return savePreJson;
	}
	/**
	 * Handles exceptions by logging the error message and stack trace.
	 * 
	 * @param e      The exception that occurred.
	 * @param action The action or context where the exception occurred.
	 */
	private void handleException(GetPreferenceResponse response, Exception e, String logMessage) {
		response.setStatusCode("99");
		response.setStatusDescription("Exception has occurred");
		logger.info(logMessage + e.getMessage());
		e.printStackTrace();
	}
	/**
	 * Creates and returns a map of key-value pairs for substitution in SQL queries.
	 * 
	 * @param corporateId The corporate ID to be used in the values map.
	 * @param userId      The user ID to be used in the values map.
	 * @return A map containing key-value pairs for SQL query substitution.
	 */
	private Map < String, String > getValuesMap(String corporateId, String userId) {
		Map < String, String > valuesMap = new HashMap < > ();
		valuesMap.put("bankgroup", "CSME");
		valuesMap.put("unitcode", corporateId);
		valuesMap.put("userid", userId);
		return valuesMap;
	}
	/**
	 * Saves preference data based on the request body.
	 * 
	 * @param body      The preference request body containing data to be saved.
	 * @param requestId The unique ID of the request.
	 * @param sequence  The sequence number of the request.
	 * @return A response containing the status of the save operation.
	 */
	public PreferenceResponsePost saveSavePreference(@Valid PreferenceRequestPost body, UUID requestId, Long sequence) {
		PreferenceResponsePost response = new PreferenceResponsePost();
		try {
			response.setMessageId(sequence.toString());
			response.setRequestId(requestId.toString());
			String setTimeStamp = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).format(new Date());
			response.setTimestamp(setTimeStamp);

			JsonNode requestBody = objectMapper.valueToTree(body);
			JsonNode preferenceListInfoNode = requestBody
					.path("SavePreferenceRespInfo")
					.path(0)
					.path("PreferenceListInfo");

			Iterator < Map.Entry < String, JsonNode >> fieldsIterator = preferenceListInfoNode.fields();
			while (fieldsIterator.hasNext()) {
				Map.Entry < String, JsonNode > entry = fieldsIterator.next();
				handleField(body, entry);
			}

			response.setStatusCode("00");
			response.setStatusDescription("Success");

		} catch (Exception e) {
			handleException(response, e, "Save Preference");
		}
		return response;
	}
	/**
	 * Handles exceptions by setting the response status code and description, logging the exception message, and printing the stack trace.
	 * 
	 * @param response The response object to be updated with the exception details.
	 * @param e        The exception that occurred.
	 * @param action   The action during which the exception occurred.
	 */
	private void handleException(PreferenceResponsePost response, Exception e, String action) {
		response.setStatusCode("99");
		response.setStatusDescription("Exception has occurred during " + action);
		logger.info("Exception occurred during " + action + ": " + e.getMessage());
	}
	/**
	 * Handles the search field by parsing XML data, converting it to JSON, and updating the JSON object accordingly.
	 * 
	 * @param value        The XML string containing search field data.
	 * @param savePreJson  The JSON object to be updated with the parsed search field data.
	 * @return             The updated JSON object with search field information.
	 * @throws JSONException if there is an error parsing or processing JSON data.
	 */
	private static void handleSearchField(String corporateId, String userId, String fieldName, JsonNode searchField) {
		addMissingIdField(searchField);

		String xmlString = null;
		try {
			xmlString = convertToJsonToXml(searchField);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}

		try {
			if (recordExists(corporateId, userId, fieldName)) {
				updateRecord(corporateId, userId, fieldName, xmlString);
			} else {
				insertRecord(corporateId, userId, fieldName, xmlString);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds missing 'ID' field to JSON elements in the search field if not already present.
	 * 
	 * @param searchField  The JSON node representing the search field data.
	 */
	private static void addMissingIdField(JsonNode searchField) {
		int idInt = 0;
		for (JsonNode searchElement: searchField) {
			if (searchElement.isObject() && !searchElement.has("ID")) {
				idInt++;
				((ObjectNode) searchElement).put("ID", idInt);
			}
		}
	}
	/**
	 * Converts a JSON node to an XML string representation.
	 * 
	 * @param searchField  The JSON node to be converted to XML.
	 * @return             The XML string representing the JSON data.
	 * @throws JsonProcessingException If there is an issue processing the JSON data.
	 */
	private static String convertToJsonToXml(JsonNode searchField) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.registerModule(new JacksonXmlModule());

		ObjectNode root = objectMapper.createObjectNode();
		root.set("SEARCH", searchField);

		String xmlString = xmlMapper.writeValueAsString(root);
		return xmlString.replace("ObjectNode", "root");
	}

	public static void main(String[] args) throws JsonProcessingException {
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.registerModule(new JacksonXmlModule());
		Object s = "{\"SEARCH\":[{\"CRITERIA\":{\"BENE_ID\":\"ILYAS\",\"AMOUNT\":1000},\"MODULE\":\"IMLC\",\"ID\":0,\"TYPE\":\"INQ\"},{\"CRITERIA\":{\"AMOUNT\":1000},\"MODULE\":\"OWGT\",\"ID\":1,\"TYPE\":\"AUTH\"},{\"CRITERIA\":{\"AMOUNT\":1000},\"MODULE\":\"IMLC\",\"ID\":2,\"TYPE\":\"AUTH\"}]}";
		String xmlString = xmlMapper.writeValueAsString(s);
		System.out.println(xmlString);
	}
	/**
	 * Handles the processing of a non-search field by inserting or updating the record in the database.
	 * 
	 * @param corporateId  The corporate ID associated with the record.
	 * @param userId       The user ID associated with the record.
	 * @param fieldName    The name of the field to be processed.
	 * @param fieldValue   The value of the field to be processed.
	 */
	private static void handleNonSearchField(String corporateId, String userId, String fieldName, JsonNode fieldValue) {
		String value = fieldValue.asText();
		if (recordExists(corporateId, userId, fieldName)) {
			updateRecord(corporateId, userId, fieldName, value);
		} else {
			insertRecord(corporateId, userId, fieldName, value);
		}
	}
	/**
	 * Inserts a new record into the PROFILE_INFO table in the database.
	 * 
	 * @param corporateId  The corporate ID associated with the record.
	 * @param userId       The user ID associated with the record.
	 * @param fieldName    The name of the field for the preference key.
	 * @param value        The value to be inserted for the preference key.
	 */
	private static void insertRecord(String corporateId, String userId, String fieldName, String value) {
		try {
			// Construct the SQL insert query
			String insertQuery = String.format("INSERT INTO CEUSER.PROFILE_INFO (C_USER_ID, C_UNIT_CODE, PREFRENCE_KEY, PREFRENCE_VALUE) VALUES('%s', '%s', '%s', '%s')",
					userId, corporateId, fieldName, value);
			// Execute the insert query
			int rowCount = WSDBHelper.executeUpdate(insertQuery, secuDs);
			// Log the outcome of the insertion
			if (rowCount > 0) {
				logger.info("Record inserted successfully");
			} else {
				logger.info("Failed to insert record");
			}
		} catch (Exception e) {
			// Handle any exceptions that occur during insertion
			handleException(e, "Insert Record");
		}
	}

	/**
	 * Updates an existing record in the PROFILE_INFO table in the database.
	 * 
	 * @param corporateId  The corporate ID associated with the record.
	 * @param userId       The user ID associated with the record.
	 * @param fieldName    The name of the field for the preference key.
	 * @param value        The new value to be updated for the preference key.
	 */
	private static void updateRecord(String corporateId, String userId, String fieldName, String value) {
		try {
			// Construct the SQL update query
			String updateQuery = String.format("UPDATE CEUSER.PROFILE_INFO SET PREFRENCE_VALUE = '%s' WHERE C_USER_ID = '%s' AND C_UNIT_CODE = '%s' AND PREFRENCE_KEY = '%s'",
					value, userId, corporateId, fieldName);

			// Execute the update query
			int rowCount = WSDBHelper.executeUpdate(updateQuery, secuDs);

			// Log the outcome of the update operation
			if (rowCount > 0) {
				logger.info("Record updated successfully");
			} else {
				logger.info("No record found for update");
			}
		} catch (Exception e) {
			// Handle any exceptions that occur during the update operation
			handleException(e, "Update Record");
		}
	}

	/**
	 * Checks if a record exists in the PROFILE_INFO table in the database based on corporate ID, user ID, and field name.
	 * 
	 * @param corporateId  The corporate ID associated with the record.
	 * @param userId       The user ID associated with the record.
	 * @param fieldName    The name of the field for the preference key.
	 * @return true if a record exists; false otherwise.
	 */
	private static boolean recordExists(String corporateId, String userId, String fieldName) {
		try {
			// Construct the SQL select query to check record existence
			String selectQuery = String.format("SELECT * FROM CEUSER.PROFILE_INFO WHERE C_USER_ID = '%s' AND C_UNIT_CODE = '%s' AND PREFRENCE_KEY = '%s'",
					userId, corporateId, fieldName);

			// Execute the select query and get the result set
			SQLRecordSet rs = WSDBHelper.executeQuery(selectQuery, secuDs);

			// Return true if the record count in the result set is greater than 0, indicating the record exists
			return rs.getRecordCount() > 0;
		} catch (Exception e) {
			// Handle any exceptions that occur during the record existence check
			handleException(e, "Check Record Existence");
			return false;
		}
	}

	/**
	 * Handles exceptions by logging an error message and printing the stack trace.
	 * 
	 * @param e      The exception that occurred.
	 * @param action The action or context in which the exception occurred.
	 */
	private static void handleException(Exception e, String action) {
		// Log an error message indicating the exception and the action where it occurred
		logger.info("Exception occurred during " + action + ": " + e.getMessage());

		// Print the stack trace of the exception for detailed debugging
		e.printStackTrace();
	}

	/**
	 * Handles a field entry by processing the field's key and value.
	 * If the field key is "SEARCH," calls the handleSearchField method,
	 * otherwise calls the handleNonSearchField method.
	 *
	 * @param body       The PreferenceRequestPost object containing request data.
	 * @param fieldEntry The field entry containing the field key and value.
	 */
	private static void handleField(@Valid PreferenceRequestPost body, Entry < String, JsonNode > fieldEntry) {
		String fieldName = fieldEntry.getKey();
		JsonNode fieldValue = fieldEntry.getValue();

		// Check if the field key is "SEARCH" and call the appropriate handler method
		if ("SEARCH".equalsIgnoreCase(fieldName)) {
			handleSearchField(body.getCorporateId(), body.getUserId(), fieldName, fieldValue);
		} else {
			handleNonSearchField(body.getCorporateId(), body.getUserId(), fieldName, fieldValue);
		}
	}

}