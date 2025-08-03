package com.csme.csmeapi.fin.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.validation.Valid;

import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.SaveFBTokenRequestPost;
import com.csme.csmeapi.fin.models.SaveFBTokenResponsePost;
import com.csme.csmeapi.fin.util.CSMEMobLogUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * Service class for saving FireBase tokens.
 */
public class SaveFBTokenApiService {
	
	FinUtil finUtil = new FinUtil();

	/**
	 * The security data source used for database operations.
	 */
	static String secuDs = null;

	/**
	 * The database type used for database operations.
	 */
	String dbType = null;

	/**
	 * Timestamp used for logging and record management.
	 */
	String setTimeStamp = null;

	/**
	 * Logger instance for logging messages.
	 */
	private final static CSMEMobLogUtil logger = new CSMEMobLogUtil();

	/**
	 * Object mapper for handling JSON data.
	 */
	static ObjectMapper objectMapper = new ObjectMapper();
	/**
	 * Initializes the SaveFBTokenApiService.
	 */
	public SaveFBTokenApiService() {
		try {
			secuDs = FinUtil.SECDS;
			dbType = FinUtil.SECDBTYPE;
			setTimeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
		} catch (Exception e) {
			handleException(e, "Initializing SavepreferenceApiService");
		}
	}

	/**
	 * Saves FireBase tokens based on the request.
	 *
	 * @param body      The request body containing the FireBase token information.
	 * @param requestId The unique ID of the request.
	 * @param sequence  The sequence number of the request.
	 * @return The response containing the status of the token saving operation.
	 */
	public SaveFBTokenResponsePost saveFireBaseToken(@Valid SaveFBTokenRequestPost body, UUID requestId, Long sequence) {
		SaveFBTokenResponsePost response = new SaveFBTokenResponsePost();
		try {
			// Set message and request IDs
			response.setMessageId(sequence.toString());
			response.setRequestId(requestId.toString());
			String setTimeStamp = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).format(new Date());
			response.setTimestamp(setTimeStamp);
			// Extract token information from the request body
			JsonNode requestBody = objectMapper.valueToTree(body);
			JsonNode saveFBTokenJNode = requestBody
					.path("SaveFBTokenInfo")
					.path(0)
					.path("TokenListInfo");
			// Iterate through the token information and handle each field
			Iterator < Map.Entry < String, JsonNode >> fieldsIterator = saveFBTokenJNode.fields();
			while (fieldsIterator.hasNext()) {
				Map.Entry < String, JsonNode > entry = fieldsIterator.next();
				handleField(body, entry);
			}
			// Set success response status
			response.setStatusCode("00");
			response.setStatusDescription("Success");

		} catch (Exception e) {
			// Handle exceptions and set error response status
			handleException(response, e, "Save Fire Base Token");
		}
		return response;
	}

	/**
	 * Handles exceptions by setting an error response and logging the exception details.
	 *
	 * @param response The response object to be modified with error status.
	 * @param e        The exception object containing the error details.
	 * @param action   The action or operation during which the exception occurred.
	 */
	private void handleException(SaveFBTokenResponsePost response, Exception e, String action) {
		// Set error response status code and description
		response.setStatusCode("99");
		response.setStatusDescription("Exception has occurred during " + action);

		// Log the exception details
		logger.info("Exception occurred during " + action + ": " + e.getMessage());
	}

	public static void main(String[] args) throws JsonProcessingException {
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.registerModule(new JacksonXmlModule());
		Object s = "{\"SEARCH\":[{\"CRITERIA\":{\"BENE_ID\":\"ILYAS\",\"AMOUNT\":1000},\"MODULE\":\"IMLC\",\"ID\":0,\"TYPE\":\"INQ\"},{\"CRITERIA\":{\"AMOUNT\":1000},\"MODULE\":\"OWGT\",\"ID\":1,\"TYPE\":\"AUTH\"},{\"CRITERIA\":{\"AMOUNT\":1000},\"MODULE\":\"IMLC\",\"ID\":2,\"TYPE\":\"AUTH\"}]}";
		String xmlString = xmlMapper.writeValueAsString(s);
		System.out.println(xmlString);
	}

	/**
	 * Handles a non-search field by checking if the record exists and updating or inserting it accordingly.
	 *
	 * @param corporateId The corporate ID associated with the record.
	 * @param userId      The user ID associated with the record.
	 * @param fieldName   The name of the field to be updated or inserted.
	 * @param fieldValue  The value of the field to be updated or inserted.
	 */
	private static void handleNonSearchField(String corporateId, String userId, String fieldName, JsonNode fieldValue) {
		// Extract the value as a string
		String value = fieldValue.asText();

		// Check if the record exists
		if (recordExists(corporateId, userId, fieldName)) {
			// Update the existing record
			updateRecord(corporateId, userId, fieldName, value);
		} else {
			// Insert a new record
			insertRecord(corporateId, userId, fieldName, value);
		}
	}

	/**
	 * Inserts a new record into the database.
	 *
	 * @param corporateId The corporate ID associated with the record.
	 * @param userId      The user ID associated with the record.
	 * @param fieldName   The name of the field in the record.
	 * @param value       The value to be inserted into the record.
	 */
	private static void insertRecord(String corporateId, String userId, String fieldName, String value) {
		try {
			// Construct the SQL insert query
			String insertQuery = String.format("INSERT INTO CEUSER.PROFILE_INFO (C_USER_ID, C_UNIT_CODE, PREFRENCE_KEY, PREFRENCE_VALUE) VALUES('%s', '%s', '%s', '%s')",
					userId, corporateId, fieldName, value);

			// Execute the insert query and get the number of affected rows
			int rowCount = WSDBHelper.executeUpdate(insertQuery, secuDs);

			// Check if the record was successfully inserted
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
	 * Updates an existing record in the database.
	 *
	 * @param corporateId The corporate ID associated with the record.
	 * @param userId      The user ID associated with the record.
	 * @param fieldName   The name of the field in the record.
	 * @param value       The new value to be updated in the record.
	 */
	private static void updateRecord(String corporateId, String userId, String fieldName, String value) {
		try {
			// Construct the SQL update query
			String updateQuery = String.format("UPDATE CEUSER.PROFILE_INFO SET PREFRENCE_VALUE = '%s' WHERE C_USER_ID = '%s' AND C_UNIT_CODE = '%s' AND PREFRENCE_KEY = '%s'",
					value, userId, corporateId, fieldName);

			// Execute the update query and get the number of affected rows
			int rowCount = WSDBHelper.executeUpdate(updateQuery, secuDs);

			// Check if the record was successfully updated
			if (rowCount > 0) {
				logger.info("Record updated successfully");
			} else {
				logger.info("No record found for update");
			}
		} catch (Exception e) {
			// Handle any exceptions that occur during update
			handleException(e, "Update Record");
		}
	}

	/**
	 * Checks if a record exists in the database based on the provided criteria.
	 *
	 * @param corporateId The corporate ID associated with the record.
	 * @param userId      The user ID associated with the record.
	 * @param fieldName   The name of the field in the record.
	 * @return true if the record exists, false otherwise.
	 */
	private static boolean recordExists(String corporateId, String userId, String fieldName) {
		try {
			// Construct the SQL select query to check record existence
			String selectQuery = String.format("SELECT * FROM CEUSER.PROFILE_INFO WHERE C_USER_ID = '%s' AND C_UNIT_CODE = '%s' AND PREFRENCE_KEY = '%s'",
					userId, corporateId, fieldName);

			// Execute the select query and get the result set
			SQLRecordSet rs = WSDBHelper.executeQuery(selectQuery, secuDs);

			// Check if the result set contains any records
			return rs.getRecordCount() > 0;
		} catch (Exception e) {
			// Handle any exceptions that occur during record existence check
			handleException(e, "Check Record Existence");
			return false;
		}
	}
	/**
	 * Handles exceptions by logging the exception message and stack trace.
	 *
	 * @param e      The exception that occurred.
	 * @param action The action during which the exception occurred.
	 */
	private static void handleException(Exception e, String action) {
		// Log the exception message and action
		logger.info("Exception occurred during " + action + ": " + e.getMessage());
		// Print the stack trace to standard error
		e.printStackTrace();
	}

	/**
	 * Handles a field entry in the SaveFBTokenRequestPost body.
	 *
	 * @param body        The SaveFBTokenRequestPost body.
	 * @param fieldEntry  The entry representing a field and its value.
	 */
	private static void handleField(@Valid SaveFBTokenRequestPost body, Entry < String, JsonNode > fieldEntry) {
		// Extract the field name and value from the entry
		String fieldName = fieldEntry.getKey();
		JsonNode fieldValue = fieldEntry.getValue();

		// Delegate handling based on field type
		handleNonSearchField(body.getCorporateId(), body.getUserId(), fieldName, fieldValue);
	}

}