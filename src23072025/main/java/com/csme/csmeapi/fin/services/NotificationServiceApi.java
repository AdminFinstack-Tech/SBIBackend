/**
 * This package contains services related to financial transactions and notifications
 * for the CSME API.
 */
package com.csme.csmeapi.fin.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.text.StringSubstitutor;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cs.base.xml.XMLManager;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XNotificationListObj;
import com.csme.csmeapi.fin.models.XNotificationRequest;
import com.csme.csmeapi.fin.models.XNotificationResponse;
import com.csme.csmeapi.fin.util.CSMEMobLogUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Service for handling notifications.
 */
@Component
public class NotificationServiceApi {
	
	FinUtil finUtil = new FinUtil();
	
	// Secure data source
	String secuDs = null;
	// Logger for logging service operations
	private final CSMEMobLogUtil logger = new CSMEMobLogUtil();
	// ObjectMapper for JSON processing
	ObjectMapper mapper = new ObjectMapper();

	/**
	 * Constructor to initialize the secure data source.
	 */
	public NotificationServiceApi() {
		this.secuDs = FinUtil.SECDS;
	}

	/**
	 * Retrieves notifications based on the request.
	 *
	 * @param body      The notification request body.
	 * @param requestId The unique request ID.
	 * @param sequence  The sequence number.
	 * @return The notification response.
	 * @throws Exception If an error occurs during processing.
	 */
	public XNotificationResponse getNotification(@Valid XNotificationRequest body, UUID requestId, Long sequence) throws Exception {
		XNotificationResponse notificationResponse= new XNotificationResponse();
		try {
			// Set message ID, request ID, and timestamp
			notificationResponse.setMessageId(sequence.toString());
			notificationResponse.setRequestId(requestId.toString());
			String setTimeStamp = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).format(new Date());
			notificationResponse.setTimestamp(setTimeStamp);
			// Retrieve SQL query from XML file
			String filePath = FinUtil.intPath+"Notification.xml";
			Document inquiryXml = XMLManager.xmlFileToDom(filePath);
			Element rootElement =  inquiryXml.getDocumentElement();
			String inquirySqlQuery = FinUtil.getValueByTagName(rootElement, "SqlQuery");
			// Substitute values in the SQL query
			Map<String, String> valuesMap = getValuesMap(body);
			StringSubstitutor substitutor = new StringSubstitutor(valuesMap);
			inquirySqlQuery = substitutor.replace(inquirySqlQuery);
			// Execute SQL query
			SQLRecordSet rs = WSDBHelper.executeQuery(inquirySqlQuery, this.secuDs);
			if (rs.getRecordCount() > 0) {
				List<XNotificationListObj> info = new ArrayList<XNotificationListObj>();
				List<String> notifyId = new ArrayList<String>();
				for (int i = 0; i < rs.getRecordCount(); i++) {
					SQLRecord sqlRecord = rs.getSQLRecord(i);
					XNotificationListObj xNotificationListObj = new XNotificationListObj();

					if (sqlRecord != null) {
						String notification  = sqlRecord.getValue("C_NOTI_CONT");
						notifyId.add(sqlRecord.getValue("C_NOTI_ID"));
						Object jsonObject = XML.toJSONObject(notification);
						JsonNode jsonNode = mapper.readTree(jsonObject.toString());
						xNotificationListObj.setNotificationListInfo(jsonNode);
					}
					info.add(xNotificationListObj);
				}
				notificationResponse.setInfo(info);
				updateNotificationStatus(body, notifyId);
				notificationResponse.setStatusCode("00");
				notificationResponse.setStatusDescription("Success");
			} else {
				notificationResponse.setStatusCode("15");
				notificationResponse.setStatusDescription("No record is exist");
			}

		} catch (Exception e) {
			notificationResponse.setStatusCode("99");
			notificationResponse.setStatusDescription("General Exception");
			logger.error("Error in getNotification: " + e.getMessage());
		}

		return notificationResponse;
	}
	/**
     * Converts XML string to JSON format.
     *
     * @param xmlString The XML string to convert.
     * @return The JSON string.
     * @throws JSONException If an error occurs during JSON conversion.
     */
	public static String convertXmlToJson(String xmlString) throws JSONException {
		JSONObject jsonObject = XML.toJSONObject(xmlString);
		 return jsonObject.toString(4); // 4-space indentation for pretty printing
	}

	/**
     * Retrieves values map from the notification request.
     *
     * @param body The notification request body.
     * @return The values map.
     */
	private Map<String, String> getValuesMap(@Valid XNotificationRequest body) {
		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put("bankgroup",	"CSME");
		valuesMap.put("unitcode", body.getCorporateId());
		valuesMap.put("userid", body.getUserId());
		return valuesMap;
	}
	 /**
     * Updates notification status in the database.
     *
     * @param body     The notification request body.
     * @param notifyId The list of notification IDs to update.
     */
	public void updateNotificationStatus(@Valid XNotificationRequest body, List<String> notifyId) {
		try {
			String notifyIdList = String.join("','", notifyId);
			String updateQuery = String.format("UPDATE CETRX.TRX_NOTIFICATION_MOBILE SET C_READ_STATUS = '%s' WHERE C_USER_ID = '%s' AND C_UNIT_CODE = '%s' AND C_NOTI_ID IN ('%s') ",
					"Y", body.getUserId(), body.getCorporateId(), notifyIdList);
			int rowCount = WSDBHelper.executeUpdate(updateQuery, "CET");

			if (rowCount > 0) {
				logger.info("Record updated successfully");
			} else {
				logger.info("No record found for update");
			}
		} catch (Exception e) { 
			logger.error("Error in updateNotificationStatus: " + e.getMessage());
        }
	}

}
