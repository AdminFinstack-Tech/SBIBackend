package com.csme.csmeapi.fin.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.ce.core.helper.PasswordHelper;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.exec.SQLDao;
import com.cs.core.dao.exec.SQLGenTool;
import com.cs.core.dao.exec.SQLGenToolHelper;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XResetPasswordRequest;
import com.csme.csmeapi.fin.models.XResetPasswordResponse;
import com.csme.csmeapi.fin.util.CSMEMobLogUtil;
/**
 * Service class for handling password reset requests.
 */
public class ResetPasswordApiService {
	
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
	public ResetPasswordApiService() throws Exception {
		secuDs = FinUtil.SECDS;
		dbType = FinUtil.SECDBTYPE;
	}
	
	/**
     * Resets the password based on the provided request.
     *
     * @param body     The request body containing the necessary information.
     * @param requestId The unique ID of the request.
     * @param sequence  The sequence number of the request.
     * @return The response containing the status of the password reset operation.
     */
	public XResetPasswordResponse resetPassword(@Valid XResetPasswordRequest body, UUID requestId, Long sequence) {
		XResetPasswordResponse XResetPasswordResponse = new XResetPasswordResponse();
		try {
			XResetPasswordResponse.setMessageId(sequence.toString());
			XResetPasswordResponse.setRequestId(requestId.toString());
			String setTimeStamp = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).format(new Date());
			XResetPasswordResponse.setTimestamp(setTimeStamp);
			boolean isOTPValid = validateOTP(body);
			if (isOTPValid) {
				// OTP is valid, proceed with password reset
				int rowsUpdated = resetPasswordDB(body);
				if (rowsUpdated > 0) {
					XResetPasswordResponse.setStatusCode("00");
					XResetPasswordResponse.setStatusDescription("Password reset successful");
				} else {
					XResetPasswordResponse.setStatusCode("99");
					XResetPasswordResponse.setStatusDescription("Password reset failed");
				}
			} else {
				XResetPasswordResponse.setStatusCode("12");
				XResetPasswordResponse.setStatusDescription("Invalid OTP");
			}
		} catch (Exception e) {
			XResetPasswordResponse.setStatusCode("99");
			XResetPasswordResponse.setStatusDescription("Internal server error");
			logger.error("Error resetting password: " + e.getMessage());
		}
		return XResetPasswordResponse;
	}
	 /**
     * Validates the OTP (One-Time Password) provided in the request.
     *
     * @param body The request body containing the OTP.
     * @return true if the OTP is valid, false otherwise.
     */
	private boolean validateOTP(XResetPasswordRequest body) {
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
     * @throws Exception if an error occurs while retrieving the OTP.
     */
	private String getStoredOTPFromDB(XResetPasswordRequest body) throws Exception {
	    SQLDao dao = new SQLDao();
	    dao.setDataSource(secuDs);
	    SQLGenTool sqlGenTool = SQLGenToolHelper.getQueryTool(dbType);
	    sqlGenTool.setTable(DSManager.getSchemaedTableName(secuDs, "OTP_TABLE"));
	    sqlGenTool.setFields("OTP");
	    sqlGenTool.appendClause("WHERE");
	    sqlGenTool.appendClause("UserId", 12, body.getUserId());
	    sqlGenTool.appendClause("AND");
	    sqlGenTool.appendClause("CorporateId", 12, body.getCorporateId());
	    sqlGenTool.appendClause("AND");
	    sqlGenTool.appendClause("OTP", 12, body.getOTP());
	    SQLRecordSet rs = WSDBHelper.executeQuery(sqlGenTool.getSqlStatement(), secuDs);
	    if (rs != null && rs.isSuccess() && rs.getRecordCount() > 0) {
	        return rs.getFirstRecord().getValue("OTP");
	    } else {
	        throw new Exception("OTP not found for user: " + body.getUserId());
	    }
	}
	 /**
     * Resets the password in the database based on the provided request.
     *
     * @param body The request body containing user information and new password.
     * @return The number of rows updated in the database.
     */
	public int resetPasswordDB(@Valid XResetPasswordRequest body) {
		int count = 0 ;
		try {
			String userId = body.getUserId();
			String unitCode = body.getCorporateId();
			String password = body.getPassword();

			SQLDao dao = new SQLDao();
			dao.setDataSource(DSManager.getSecuDS());
			String sUserTable = DSManager.getSchemaedTableName(DSManager.getSecuDS(), "SEC_USER_INFO");
			String sTempTable = DSManager.getSchemaedTableName(DSManager.getSecuDS(), "SEC_USER_INFO_TEMP");

			SQLGenTool genSql = SQLGenToolHelper.getUpdateTool(secuDs);
			genSql.setTable(sUserTable);
			genSql.appendClause("WHERE");
			genSql.appendClause("C_BK_GROUP_ID", 1, "CSBANK").appendClause("AND");
			genSql.appendClause("C_UNIT_CODE", 1, unitCode).appendClause("AND");
			genSql.appendClause("C_USER_ID", 1, userId);
			genSql.addField("C_PASSWORD", PasswordHelper.encryptPassword(password, unitCode, userId), 1);
			genSql.addField("C_TRX_STATUS", "R", 1);
			genSql.addField("C_LOCKED_FLAG", "F", 1);
			genSql.addField("C_CREATED_BY", userId, 12);
			genSql.addField("C_CREATED_BU", unitCode, 12);

			dao.addSqlStatement(genSql.getSqlStatement());
			@SuppressWarnings("deprecation")
			SQLGenTool genSqlForTemp = genSql.cloneTool();
			genSqlForTemp.setTable(sTempTable);
			dao.addSqlStatement(genSqlForTemp.getSqlStatement());
			count = WSDBHelper.executeUpdate(dao);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;

	}
}
