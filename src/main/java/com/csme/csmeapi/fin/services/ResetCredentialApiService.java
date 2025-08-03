package com.csme.csmeapi.fin.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;

import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.exec.SQLDao;
import com.cs.core.dao.exec.SQLGenTool;
import com.cs.core.dao.exec.SQLGenToolHelper;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XResetCredentialRequest;
import com.csme.csmeapi.fin.models.XResetCredentialResponse;
import com.csme.csmeapi.fin.util.CSMEMobLogUtil;

/**
 * Service class for resetting user credentials.
 */
public class ResetCredentialApiService {
	
	FinUtil finUtil = new FinUtil();
	
	/**
	 * Logger instance for logging messages.
	 */
	private final CSMEMobLogUtil logger = new CSMEMobLogUtil();
	/**
	 * Date format for formatting timestamps.
	 */
	public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	/**
	 * The security data source used for database operations.
	 */
	private static String secuDs;
	/**
	 * The database type used for database operations.
	 */
	private static String dbType;

	/**
     * Initializes the service by obtaining the security datasource and database type.
     *
     * @throws Exception if initialization fails.
     */
	public ResetCredentialApiService() throws Exception {
		secuDs = FinUtil.SECDS;
		dbType = FinUtil.SECDBTYPE;
	}
	 /**
     * Resets the user's password based on the provided request.
     *
     * @param body      The request body containing reset password information.
     * @param requestId The unique identifier for the request.
     * @param sequence  The sequence number for the request.
     * @return The response containing the status of the password reset operation.
     */
	public XResetCredentialResponse resetPassword(@Valid XResetCredentialRequest body, UUID requestId, Long sequence) {
		XResetCredentialResponse resetCredentialResponse = new XResetCredentialResponse();
		try {
			resetCredentialResponse.setMessageId(sequence.toString());
			resetCredentialResponse.setRequestId(requestId.toString());
			String setTimeStamp = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).format(new Date());
			resetCredentialResponse.setTimestamp(setTimeStamp);
			boolean isOTPValid = validateOTP(body);
			if (isOTPValid) {
				// OTP is valid, proceed with password reset
				SQLRecordSet rs = resetCredential(body);
				if (rs.getRecordCount() > 0) {
					SQLRecord loginRcd = rs.getFirstRecord();
					String corporateId = loginRcd.getValue("C_UNIT_CODE");
					String userId = loginRcd.getValue("C_USER_ID");
					resetCredentialResponse.setCorporateId(corporateId);
					resetCredentialResponse.setUserId(userId);
					resetCredentialResponse.setStatusCode("00");
					resetCredentialResponse.setStatusDescription("Success");
				} else {
					resetCredentialResponse.setStatusCode("99");
					resetCredentialResponse.setStatusDescription("Failure Unable to reterive");
				}
			} else {
				resetCredentialResponse.setStatusCode("12");
				resetCredentialResponse.setStatusDescription("Invalid OTP");
			}
		} catch (Exception e) {
			resetCredentialResponse.setStatusCode("99");
			resetCredentialResponse.setStatusDescription("Internal server error");
			logger.error("Error resetting password: " + e.getMessage());
		}
		return resetCredentialResponse;
	}
	/**
     * Validates the OTP (One-Time Password) provided in the request.
     *
     * @param body The request body containing the OTP to validate.
     * @return true if the OTP is valid, false otherwise.
     */
	private boolean validateOTP(@Valid XResetCredentialRequest body) {
	    try {
	        // Retrieve the OTP associated with the user from the database
	        String storedOTP = getStoredOTPFromDB(body);
	        // Compare the provided OTP with the stored OTP
	        return body.getOTP().equals(storedOTP);
	    } catch (Exception e) {
	        logger.error("Error validating OTP: " + e.getMessage());
	        return false;
	    }
	}
	/**
     * Retrieves the stored OTP (One-Time Password) associated with the user from the database.
     *
     * @param body The request body containing user information.
     * @return The stored OTP.
     * @throws Exception if an error occurs during database access.
     */
	private String getStoredOTPFromDB(@Valid XResetCredentialRequest body) throws Exception {
	    SQLDao dao = new SQLDao();
	    dao.setDataSource(secuDs);
	    SQLGenTool sqlGenTool = SQLGenToolHelper.getQueryTool(dbType);
	    sqlGenTool.setTable(DSManager.getSchemaedTableName(secuDs, "OTP_TABLE"));
	    sqlGenTool.setFields("OTP");
	    sqlGenTool.appendClause("WHERE");
	    sqlGenTool.appendClause("MobileNumber", 12, body.getMobileNo());
	    sqlGenTool.appendClause("AND");
	    sqlGenTool.appendClause("EmailId", 12, body.getEmail());
	    sqlGenTool.appendClause("AND");
	    sqlGenTool.appendClause("OTP", 12, body.getOTP());
	    SQLRecordSet rs = WSDBHelper.executeQuery(sqlGenTool.getSqlStatement(), secuDs);
	    if (rs != null && rs.isSuccess() && rs.getRecordCount() > 0) {
	        return rs.getFirstRecord().getValue("OTP");
	    } else {
	        throw new Exception("OTP not found for user");
	    }
	}
	/**
     * Retrieves user credentials from the database based on the provided request.
     *
     * @param body The request body containing user information.
     * @return The record set containing user details.
     */
	public SQLRecordSet resetCredential(@Valid XResetCredentialRequest body) {
		SQLRecordSet rs = null;
		try {
			SQLDao dao = new SQLDao();
		    dao.setDataSource(secuDs);
		    SQLGenTool sqlGenTool = SQLGenToolHelper.getQueryTool(dbType);
		    sqlGenTool.setTable(DSManager.getSchemaedTableName(secuDs, "SEC_USER_INFO"));
		    sqlGenTool.addQueryField("C_USER_ID");
		    sqlGenTool.addQueryField("C_USER_DESC");
		    sqlGenTool.addQueryField("C_UNIT_CODE");
		    sqlGenTool.appendClause("WHERE");
		    sqlGenTool.appendClause("EMAIL_ID", 12, "=", body.getEmail());
		    sqlGenTool.appendClause("AND");
		    sqlGenTool.appendClause("MOBILE", 12, "=", body.getMobileNo());
		    rs = WSDBHelper.executeQuery(sqlGenTool.getSqlStatement(), secuDs);
		    if (rs != null && rs.isSuccess() && rs.getRecordCount() > 0) {
		        return rs;
		    } else {
		        throw new Exception("User Id and Corporate Id not found in database for mobile number and email");
		    }
		} catch (Exception e) {
            logger.error("Error resetting credentials: " + e.getMessage()); // Improved logger usage
        }
		return rs;

	}
}
