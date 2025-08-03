package com.csme.csmeapi.fin.services;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.exec.SQLDao;
import com.cs.core.dao.exec.SQLGenTool;
import com.cs.core.dao.exec.SQLGenToolHelper;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.util.CSMEMobLogUtil;
import com.google.gson.JsonObject;

/**
 * Handles batch processing of notifications for transactions.
 */
@Component
public class NotificationBatchServices {
	
	FinUtil finUtil = new FinUtil();
	
	private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static String secuDs;
	private static String dbType;
	private final CSMEMobLogUtil logger = new CSMEMobLogUtil();

	/**
	 * Constructs a NotificationBatchServiSces instance.
	 * @throws Exception if initialization fails
	 */
	public NotificationBatchServices() throws Exception {
		secuDs = FinUtil.SECDS;
		dbType = FinUtil.TRXDBTYPE;
	}

	/**
	 * Processes pending notifications.
	 * <p>
	 * This method retrieves pending notifications from the database and processes them
	 * by calling helper methods to handle each notification.
	 * </p>
	 */
	public void processingNotification() {
		try {
			// Query to fetch pending notifications
			String commonSqlQuery = "SELECT * FROM CETRX.TRX_INBOX WHERE C_TRX_STATUS IN ('P','T','S') AND MOBI_NOTIFY_FLAG IS NULL";
			logger.info("process notification query is :" + commonSqlQuery);
			SQLRecordSet rs = WSDBHelper.executeQuery(commonSqlQuery, "CET");
			logger.info("Number of notifications to process: " + rs.getRecordCount());
			processRecords(rs);
			updateNotificationFlag(rs);
		} catch (Exception e) {
			logger.error("Error processing notifications: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Processes the SQL record set containing pending notifications.
	 * <p>
	 * This method iterates through the SQLRecordSet and processes each SQLRecord
	 * by calling the processRecord method.
	 * </p>
	 *
	 * @param rs The SQLRecordSet containing pending notifications.
	 * @throws Exception if an error occurs during processing.
	 */
	private void processRecords(SQLRecordSet rs) throws Exception {
		try {
			if (rs.getRecordCount() > 0) {
				for (int i = 0; i < rs.getRecordCount(); i++) {
					SQLRecord sqlRecord = rs.getSQLRecord(i);
					if (sqlRecord != null) {
						processRecord(sqlRecord,rs.getRecordCount());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Processes a single SQL record representing a pending notification.
	 * <p>
	 * This method processes a single SQLRecord by extracting relevant information
	 * and invoking further actions based on the notification's attributes.
	 * </p>
	 *
	 * @param sqlRecord The SQLRecord representing a pending notification.
	 * @param totalCount The total count of pending notifications being processed.
	 * @throws Exception if an error occurs during processing.
	 */
	private void processRecord(SQLRecord sqlRecord, int totalCount) throws Exception {
		try {
			logger.info("Total Record is : " + totalCount);
			logger.info("Processing notification sqlRecord: " + sqlRecord);
			// Extracting relevant information from the SQLRecord
			String mainRef = sqlRecord.getValue("C_MAIN_REF");
			String trxId = sqlRecord.getValue("C_TRX_ID");
			String unitCode = sqlRecord.getValue("C_UNIT_CODE");
			String statusName = sqlRecord.getValue("C_STATUS_NAME");
			String trxStatus = sqlRecord.getValue("C_TRX_STATUS");
			String action = getAction(trxStatus);
			String eventTimes = sqlRecord.getValue("I_EVENT_TIMES");
			String moduleName = sqlRecord.getValue("C_MODULE");
			String funcId = sqlRecord.getValue("C_FUNC_ID");
			String shortName = sqlRecord.getValue("C_FUNC_SHORT_NAME");
			// Constructing SQL query to fetch user ID
			String getUserIdSql = "SELECT C_USER_ID FROM CEUSER.SEC_USER_INFO WHERE C_UNIT_CODE = '"
					+ unitCode + "' AND I_AUTH_LEVEL IN ( SELECT I_AUTH_LEVEL FROM CETRX.TRX_MATRIX_LIST WHERE  C_TRX_ID = '"
					+ trxId + "') ";
			logger.info("Fetching user ID query: " + getUserIdSql);
			// Processing user results
			SQLRecordSet userResult = WSDBHelper.executeQuery(getUserIdSql, secuDs);
			logger.info("Process notification getting user id list count: " + userResult.getRecordCount());
			processUserResults(userResult, mainRef, eventTimes, unitCode, moduleName, action, statusName, funcId, shortName,totalCount);
		} catch (Exception e) {
			logger.error("Error processing SQL record: " + e.getMessage());
			e.printStackTrace();
			throw e; // Re-throw the exception for higher-level handling
		}
	}
	/**
	 * Processes the SQLRecordSet containing user information related to a notification.
	 * <p>
	 * This method processes the SQLRecordSet to retrieve user IDs and further processes
	 * each user's information by invoking actions based on the notification.
	 * </p>
	 *
	 * @param userResult The SQLRecordSet containing user information.
	 * @param mainRef The main reference of the notification.
	 * @param eventTimes The event times related to the notification.
	 * @param unitCode The unit code associated with the notification.
	 * @param moduleName The module name related to the notification.
	 * @param action The action to be performed based on the notification.
	 * @param statusName The status name of the notification.
	 * @param funcId The function ID associated with the notification.
	 * @param shortName The short name of the function related to the notification.
	 * @param totalCount The total count of pending notifications being processed.
	 * @throws Exception if an error occurs during processing.
	 */
	private void processUserResults(SQLRecordSet userResult, String mainRef, String eventTimes, String unitCode,
			String moduleName, String action, String statusName, String funcId, String shortName, int totalCount) throws Exception {
		try {
			if (userResult.getRecordCount() > 0) {
				for (int j = 0; j < userResult.getRecordCount(); j++) {
					SQLRecord userSqlRecord = userResult.getSQLRecord(j);
					if (userSqlRecord != null) {
						logger.info("Process notification getting user id list count: " + userSqlRecord);
						String userId = userSqlRecord.getValue("C_USER_ID");
						// Generate XML content for notification
						String notiContent = generateXMLContent(mainRef, eventTimes, unitCode, moduleName, action, statusName, funcId, shortName);
						// Insert notification record
						Long notificationId = insertRecNotification(unitCode, userId, notiContent, moduleName, funcId);
						logger.info("Notification insert into table, Seq is: " + notificationId);
						// Process Firebase notification service
						processFireBaseNotificationService(unitCode, userId, notiContent, moduleName, funcId,notificationId,totalCount);
					}
				}
			} 
		} catch (Exception e) {
			logger.error("Error processing user results: " + e.getMessage());
			e.printStackTrace();
			throw e; // Re-throw the exception for higher-level handling
		}
	}

	/**
	 * Processes Firebase notifications for a user and sends the notification.
	 * <p>
	 * This method retrieves the Firebase token for the user, constructs the notification content,
	 * and sends the notification using Firebase Cloud Messaging (FCM) service.
	 * </p>
	 *
	 * @param unitCode The unit code associated with the notification.
	 * @param userId The user ID for whom the notification is processed.
	 * @param notiContent The content of the notification in XML format.
	 * @param moduleName The module name related to the notification.
	 * @param funcId The function ID associated with the notification.
	 * @param notificationId The ID of the notification record.
	 * @param totalCount The total count of pending notifications being processed.
	 * @throws Exception if an error occurs during processing.
	 */
	private void processFireBaseNotificationService(String unitCode, String userId, String notiContent,
			String moduleName, String funcId, Long notificationId, int totalCount) throws Exception {
		try{
			// Query to fetch Firebase token for the user
			String getFBTokenQuery = "SELECT * FROM CEUSER.PROFILE_INFO WHERE C_UNIT_CODE = '"+unitCode+"' AND C_USER_ID = '"+userId+"' and  PREFRENCE_KEY = 'FBToken' ";
			logger.info("Getting FireBase Token query is : " + getFBTokenQuery);
			// Fetching Firebase token
			SQLRecordSet FBTokenRs = WSDBHelper.executeQuery(getFBTokenQuery, secuDs);
			if (FBTokenRs.getRecordCount() > 0) {
				for (int j = 0; j < FBTokenRs.getRecordCount(); j++) {
					SQLRecord userSqlRecord = FBTokenRs.getSQLRecord(j);
					if (userSqlRecord != null) {
						logger.info("Processing Firebase user record: " + userSqlRecord);
						String preKey = userSqlRecord.getValue("PREFRENCE_KEY");
						if(preKey != null && preKey.equalsIgnoreCase("FBToken")) {
							String deviceToken = userSqlRecord.getValue("PREFRENCE_VALUE");
							String xpathExpression = "/Notification/Content";
							String msgContent = FinUtil.getContentValueFromXML(notiContent,xpathExpression);
							// Constructing notification object for Firebase
							JsonObject notificationObject = new JsonObject();
							notificationObject.addProperty("title", "Release Notification");
							notificationObject.addProperty("body", msgContent);
							notificationObject.addProperty("badge", totalCount);
							// Sending notification using Firebase Cloud Messaging (FCM) service
							FCMHelper fcmHelper = FCMHelper.getInstance();
							String result = fcmHelper.sendNotification(FCMHelper.TYPE_TO, deviceToken, notificationObject);
							// Processing the result and updating notification flag
							JSONObject responseObj = new JSONObject(result);
							int successCount = responseObj.getInt("success");
							if(successCount > 0)
							{
								updateNotificationFlagFB(unitCode, userId, notificationId, "Y");
							} else {
								updateNotificationFlagFB(unitCode, userId, notificationId, "F");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			updateNotificationFlagFB(unitCode, userId, notificationId, "F");
            logger.error("Error processing Firebase notification service: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw the exception for higher-level handling
        }
	}
	/**
     * Inserts a notification record into the database.
     * <p>
     * This method inserts a notification record into the database table
     * and logs the success or failure of the insertion.
     * </p>
     *
     * @param unitCode The unit code associated with the notification.
     * @param userId The user ID for whom the notification is being inserted.
     * @param notiContent The content of the notification in XML format.
     * @param moduleName The module name related to the notification.
     * @param funcId The function ID associated with the notification.
     * @return The generated sequence ID for the inserted notification record.
     */
	private Long insertRecNotification(String unitCode, String userId, String notiContent, String moduleName, String funcId) {
		Long seq = null;
		try {
			SQLDao sqlDao = new SQLDao();
			sqlDao.setDataSource("CET");
			// Generating sequence ID for the notification record
			seq = getSequence();
			if (seq == null)
				throw new Exception("Not able to generate sequence ");
			String reportDate = DF.format(new Date());
			 // Constructing SQL statement to insert the notification record
			SQLGenTool assignSQLTool1 = SQLGenToolHelper.getInsertTool(dbType);
			String table = DSManager.getSchemaedTableName("CET", "TRX_NOTIFICATION_MOBILE");
			assignSQLTool1.setTable(table);
			assignSQLTool1.addField("C_NOTI_ID", seq.longValue() + "", -5);
			assignSQLTool1.addField("C_UNIT_CODE", unitCode, 12);
			assignSQLTool1.addField("C_USER_ID", userId, 12);
			assignSQLTool1.addField("C_READ_STATUS", "", 12);
			assignSQLTool1.addField("C_CREA_DATE", reportDate, 93);
			assignSQLTool1.addField("C_NOTI_CONT", notiContent, 2005);
			assignSQLTool1.addField("C_MODULE", moduleName, 12);
			// Executing SQL statement to insert the notification record
			sqlDao.addSqlStatement(assignSQLTool1.getSqlStatement());
			logger.info("Query to insert the Audit Record: " + assignSQLTool1.getSqlStatement());
			int update = WSDBHelper.executeUpdate(sqlDao);
			logger.info("Audit Record Successfully Inserted. count is: " + update);
		} catch (Exception e) {
			logger.error("Error While Inserting Audit Record: " + e.getMessage());
			e.printStackTrace();
		}
		return seq;
	}

	/**
     * Retrieves the next sequence value for notification records.
     * <p>
     * This method retrieves the next sequence value from the database
     * for generating unique IDs for notification records.
     * </p>
     *
     * @return The next sequence value as a Long.
     * @throws Exception if an error occurs during sequence retrieval.
     */
    private Long getSequence() throws Exception {
        long nextValue = 0L;
        try {
            String query = "select CETRX.TRX_NOTIFICATION_MOBILE_SEQ.NEXTVAL from DUAL";
            logger.info("Query to get the sequence: " + query);

            // Executing query to fetch the next sequence value
            SQLRecordSet rs = WSDBHelper.executeQuery(query, "CET");

            if (rs.isSuccess() && rs.getRecordCount() > 0 && rs.getFirstRecord().getValue("NEXTVAL") != null) {
                nextValue = Long.parseLong(rs.getFirstRecord().getValue("NEXTVAL"));
            }

            logger.info("Next sequence value is: " + nextValue);
            return nextValue;
        } catch (Exception e) {
            logger.error("Error while getting the sequence: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw the exception for higher-level handling
        }
    }

    /**
     * Determines the action based on the transaction status.
     * <p>
     * This method determines the action based on the transaction status:
     * - If the status is "P", "T", or "S", the action is set to "AUTH".
     * - Otherwise, the action is set to "INQ".
     * </p>
     *
     * @param trxStatus The transaction status.
     * @return The action as a String ("AUTH" or "INQ").
     */
    private String getAction(String trxStatus) {
        String action = null;
        if (trxStatus.equalsIgnoreCase("P") || trxStatus.equalsIgnoreCase("T") || trxStatus.equalsIgnoreCase("S")) {
            action = "AUTH";
        } else {
            action = "INQ";
        }
        return action;
    }
    
    /**
     * Generates XML content for a notification.
     * <p>
     * This method creates XML content for a notification based on the provided parameters.
     * It constructs an XML document with elements such as MainRef, EventTimes, FuncID, Module,
     * Content, Action, StatusName, and ShortName.
     * </p>
     *
     * @param mainRef The main reference for the notification.
     * @param eventTimes The event times associated with the notification.
     * @param unitCode The unit code related to the notification.
     * @param moduleName The module name associated with the notification.
     * @param action The action related to the notification.
     * @param statusName The status name of the notification.
     * @param funcId The function ID associated with the notification.
     * @param shortName The short name of the notification.
     * @return The XML content as a String.
     */
    private String generateXMLContent(String mainRef, String eventTimes, String unitCode, String moduleName,
                                      String action, String statusName, String funcId, String shortName) {
        try {
            // Create a new XML document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Create the root element for the notification
            Element notificationElement = doc.createElement("Notification");
            doc.appendChild(notificationElement);

            // Create child elements for various notification details
            createElement(doc, notificationElement, "MainRef", mainRef);
            createElement(doc, notificationElement, "EventTimes", eventTimes);
            createElement(doc, notificationElement, "FuncID", funcId);
            createElement(doc, notificationElement, "Module", moduleName);
            createElement(doc, notificationElement, "Content",
                    "Transaction ID " + mainRef + " is currently pending approval and awaiting release.");
            createElement(doc, notificationElement, "Action", action);
            createElement(doc, notificationElement, "StatusName", statusName);
            createElement(doc, notificationElement, "ShortName", shortName);

            // Convert the XML document to a String
            StringWriter writer = new StringWriter();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            return writer.toString();
        } catch (ParserConfigurationException | TransformerException e) {
            logger.error("Error while generating XML content: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Creates an XML element with the specified tag name and text content
     * and appends it to the parent element in the XML document.
     * <p>
     * This method creates an XML element with the given tag name and text content,
     * and then appends it as a child element to the specified parent element in the XML document.
     * </p>
     *
     * @param doc           The XML document to which the element belongs.
     * @param parentElement The parent element to which the new element will be appended.
     * @param tagName       The tag name of the new XML element.
     * @param textContent   The text content of the new XML element.
     */
    private static void createElement(Document doc, Element parentElement, String tagName, String textContent) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textContent));
        parentElement.appendChild(element);
    }
    
    /**
     * Updates the mobile notification flag based on the SQLRecordSet.
     * <p>
     * This method updates the MOBI_NOTIFY_FLAG field in the CETRX.TRX_INBOX table
     * to 'P' for each record in the provided SQLRecordSet that meets the condition.
     * </p>
     *
     * @param rs The SQLRecordSet containing records to be processed.
     * @throws Exception If an error occurs during the database update operation.
     */
    private void updateNotificationFlag(SQLRecordSet rs) throws Exception {
        if (rs.getRecordCount() > 0) {
            for (int i = 0; i < rs.getRecordCount(); i++) {
                SQLRecord sqlRecord = rs.getSQLRecord(i);
                if (sqlRecord != null) {
                    String mainRef = sqlRecord.getValue("C_MAIN_REF");
                    String updateSql = "UPDATE CETRX.TRX_INBOX SET MOBI_NOTIFY_FLAG = 'P' WHERE C_MAIN_REF = '" + mainRef + "'";
                    logger.info("Update MOBI_NOTIFY_FLAG query: " + updateSql);
                    int updateCount = WSDBHelper.executeUpdate(updateSql, "CET");
                    logger.info("Update MOBI_NOTIFY_FLAG count: " + updateCount);
                }
            }
        }
    }


    /**
     * Updates the Firebase notification flag in the CETRX.TRX_NOTIFICATION_MOBILE table.
     * <p>
     * This method updates the FB_NOTIFY_FLAG field in the database for a specific notification
     * identified by the unit code, user ID, and notification ID.
     * </p>
     *
     * @param unitCode      The unit code associated with the notification.
     * @param userId        The user ID associated with the notification.
     * @param notificationId The ID of the notification to be updated.
     * @param notifyFlag    The new value for the FB_NOTIFY_FLAG field ('Y' for yes, 'F' for failed, 'E' for error).
     * @throws Exception If an error occurs during the database update operation.
     */
    private void updateNotificationFlagFB(String unitCode, String userId, Long notificationId, String notifyFlag) throws Exception {
        try {
            String updateSql = "UPDATE CETRX.TRX_NOTIFICATION_MOBILE SET FB_NOTIFY_FLAG = '" + notifyFlag + "' WHERE C_UNIT_CODE = '" + unitCode + "' AND C_USER_ID = '" + userId + "' AND C_NOTI_ID = '" + notificationId + "'";
            logger.info("Update FB_NOTIFY_FLAG query: " + updateSql);
            int updateCount = WSDBHelper.executeUpdate(updateSql, "CET");
            logger.info("Update FB_NOTIFY_FLAG count: " + updateCount);
        } catch (Exception e) {
            logger.error("Error updating Firebase notification flag: " + e.getMessage());
            e.printStackTrace(); // or handle the exception appropriately
        }
    }

}
