package com.csme.csmeapi.fin.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.exec.SQLGenTool;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.LogoffRequest;
import com.csme.csmeapi.fin.models.LogoffResponse;

/**
 * Service class for handling logoff requests.
 */
public class LogoffApiServices {
		
	FinUtil finUtil = new FinUtil();
	
	/**
     * The logger for logging messages related to this class.
     */
	Logger logger = LogManager.getLogger("CSMEMobile");

    /**
     * The current login time, initially set to null.
     */
    private String currLoginTime = null;
    
	/**
	 * Processes a logoff request.
	 *
	 * @param request   The logoff request.
	 * @param requestId The request ID.
	 * @param sequence  The sequence number.
	 * @return The logoff response.
	 */
	public LogoffResponse getLogoffInfo(@Valid LogoffRequest request, UUID requestId, Long sequence) {
		LogoffResponse resp = new LogoffResponse();
		try {
			if (checkRealLogin(request.getCorporateId(), request.getUserId())) {
				setResponse(resp, sequence.toString(), requestId.toString(), "00", "Logoff Successfully");
			} else {
				setResponse(resp, sequence.toString(), requestId.toString(), "04", "Logoff Failed");
			} 
		} catch (Exception e) {
			setResponse(resp, sequence.toString(), requestId.toString(), "99", "General Exception");
			this.logger.error("Exception occured in get Login info function " + e);
		} 
		return resp;
	}

	/**
	 * Checks if the user is logged in.
	 *
	 * @param oriBu     The original business unit.
	 * @param oriUserId The original user ID.
	 * @return True if the user is logged in; false otherwise.
	 */
	private boolean checkRealLogin(String oriBu, String oriUserId) {
		boolean isValid = false;
		try {
			String secuDs = DSManager.getSecuDS();
			String dbType = DSManager.getDBType(secuDs);
			String userInfoTable = DSManager.getSchemaedTableName(secuDs, "SEC_USER_INFO");
			SQLGenTool loginUserSQLToo = new SQLGenTool(4, userInfoTable, dbType);
			loginUserSQLToo.setFields("C_USER_ID,C_UNIT_CODE");
			loginUserSQLToo.appendClause("WHERE");
			loginUserSQLToo.appendClause("C_USER_ID", 12, "=", oriUserId);
			loginUserSQLToo.appendClause("AND");
			loginUserSQLToo.appendClause("C_UNIT_CODE", 12, "=", oriBu);
			SQLRecordSet rs = WSDBHelper.executeQuery(loginUserSQLToo.getSqlStatement(), secuDs);
			if (rs.isSuccess()) {
				SQLRecord loginRcd = rs.getFirstRecord();
				currLoginTime = loginRcd.getValue("T_CURR_LOGIN_TIME");
				logger.info("Current Time is :"+currLoginTime);
				isValid = true;
			} else {
				isValid = false;
			} 
		} catch (Exception e) {
			this.logger.error("Exception occured in checkRealLogin function " + e);
		} 
		return isValid;
	}
	/**
	 * Sets the response parameters.
	 *
	 * @param response  The logoff response.
	 * @param sequence  The sequence number.
	 * @param requestId The request ID.
	 * @param code      The status code.
	 * @param desc      The status description.
	 */
	private void setResponse(LogoffResponse response, String sequence, String requestId, String code, String desc) {
		response.setMessageId(sequence);
		response.setRequestId(requestId);
		String setTimeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
		response.setStatusCode(code);
		response.setStatusDescription(desc);
		response.setTimestamp(setTimeStamp);
	}
}
