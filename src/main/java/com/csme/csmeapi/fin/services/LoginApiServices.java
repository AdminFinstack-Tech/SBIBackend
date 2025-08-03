/**
 * Provides Java services related to financial operations for CSME API.
 * This package contains classes and utilities for handling financial transactions,
 * user profiles, and other financial functionalities.
 */
package com.csme.csmeapi.fin.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.cs.ce.core.helper.CustomFormatHelper;
import com.cs.ce.core.helper.PasswordHelper;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.ce.core.password.PwdResult;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.exec.SQLDao;
import com.cs.core.dao.exec.SQLGenTool;
import com.cs.core.dao.exec.SQLGenToolHelper;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.LoginRequest;
import com.csme.csmeapi.fin.models.LoginResponse;
/**
 * Provides services related to user login operations.
 */
public class LoginApiServices {
	
	FinUtil finUtil = new FinUtil();
	
	/**
	 * Logger for logging messages related to user login operations.
	 */
	Logger logger = LogManager.getLogger("CSMEMobile");
	/**
	 * Flag indicating whether a login success exists.
	 */
	private boolean loginSuccessExist;
	/**
	 * The current login time for reference during login operations.
	 */
	private String currLoginTime;

	/**
	 * Service for accessing profile-related APIs.
	 */
	ProfileApiServices prfServ = new ProfileApiServices();

	/**
	 * Retrieves login information based on the provided request.
	 * 
	 * @param request   The login request containing user credentials.
	 * @param requestId The unique identifier for the request.
	 * @param sequence  The sequence number of the request.
	 * @return A LoginResponse object containing the login status and details.
	 */
	public LoginResponse getLoginInfo(LoginRequest request, UUID requestId, Long sequence) throws Exception {
		LoginResponse resp = new LoginResponse();
		PwdResult pwdResult = new PwdResult();
		try {
			resp.setMessageId(sequence.toString());
			resp.setRequestId(requestId.toString());
			String setTimeStamp = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).format(new Date());
			resp.setTimestamp(setTimeStamp);
			PwdResult res = PasswordHelper.checkPwdExpire(request.getCorporateId(), request.getUserId());
			if (!res.isValid()) {
				resp.setStatusCode("01");
				resp.setStatusDescription("Credentail is Expired");
				return resp;
			} 
			pwdResult = PasswordHelper.checkLogonRetries(request.getCorporateId(), request.getUserId(), request.getPassword());
			if (!pwdResult.isValid()) {
				resp.setStatusCode("02");
				resp.setStatusDescription("Login Reteries Exceed maximum");
				return resp;
			} 
			if (!PasswordHelper.checkLoginPwd(request.getCorporateId(), request.getUserId(), request.getPassword())) {
				resp.setStatusCode("03");
				resp.setStatusDescription("Incorrect Credential");
				return resp;
			} 
			if (checkRealLogin(request.getCorporateId(), request.getUserId())) {
				this.loginSuccessExist = true;
				SQLDao dao = getSuccessSQLsForStmt(request.getCorporateId(), request.getUserId());
				WSDBHelper.executeUpdate(dao);
				resp.setStatusCode("00");
				resp.setStatusDescription("Login Successfully");
			} else {
				resp.setStatusCode("04");
				resp.setStatusDescription("Login Failed");
			} 
		} catch (Exception e) {
			resp.setStatusCode("99");
			resp.setStatusDescription("General Exception");
			this.logger.error("Exception occured in get Login info function " + e);
		} 
		return resp;
	}
	/**
	 * Generates SQL statements for successful login operations.
	 * 
	 * @param userId   The user ID associated with the login operation.
	 * @param unitCode The unit code associated with the login operation.
	 * @return An SQLDao object containing the SQL statements.
	 * @throws Exception If an error occurs during SQL generation.
	 */
	@SuppressWarnings("deprecation")
	private SQLDao getSuccessSQLsForStmt(String userId, String unitCode) throws Exception {
		SQLDao dao = new SQLDao();
		dao.setDataSource(DSManager.getSecuDS());
		String sUserTable = DSManager.getSchemaedTableName(DSManager.getSecuDS(), "SEC_USER_INFO");
		String sTempTable = DSManager.getSchemaedTableName(DSManager.getSecuDS(), "SEC_USER_INFO_TEMP");
		String dbType = DSManager.getDBType(DSManager.getSecuDS());
		SQLGenTool genSql = SQLGenToolHelper.getUpdateTool(dbType);
		genSql.setTable(sUserTable);
		genSql.addField("I_LOGON_RETRIES", "0", 4);
		genSql.appendClause("WHERE");
		genSql.appendClause("C_USER_ID", 1, userId).appendClause("AND");
		genSql.appendClause("C_UNIT_CODE", 1, unitCode);
		dao.addSqlStatement(genSql.getSqlStatement());
		
		SQLGenTool genSqlForTemp = genSql.cloneTool();
		genSqlForTemp.setTable(sTempTable);
		dao.addSqlStatement(genSqlForTemp.getSqlStatement());
		String loginSuccTable = DSManager.getSchemaedTableName(DSManager.getSecuDS(), "SEC_LOGIN_SUCCEED_INFO");
		String currDT = CustomFormatHelper.getSystemGMTDate("T_CURR_LOGIN_TIME", "yyyy-mm-dd hh:mm:ss.fff");
		if (this.loginSuccessExist) {
			SQLGenTool loGenSQLTool = SQLGenToolHelper.getUpdateTool(dbType);
			loGenSQLTool.setTable(loginSuccTable);
			String loginTime = CustomFormatHelper.getGMTDate("T_LAST_LOGIN_TIME", this.currLoginTime, "yyyy-mm-dd hh:mm:ss.fff");
			loGenSQLTool.addField("T_LAST_LOGIN_TIME", loginTime, 93);
			loGenSQLTool.addField("T_CURR_LOGIN_TIME", currDT, 93);
			loGenSQLTool.appendClause("WHERE");
			loGenSQLTool.appendClause("C_USER_ID", 1, userId).appendClause("AND");
			loGenSQLTool.appendClause("C_UNIT_CODE", 1, unitCode);
			dao.addSqlStatement(loGenSQLTool.getSqlStatement());
		} else {
			SQLGenTool loGenSQLTool = SQLGenToolHelper.getInsertTool(dbType);
			loGenSQLTool.setTable(loginSuccTable);
			loGenSQLTool.addField("C_UNIT_CODE", userId, 12);
			loGenSQLTool.addField("C_USER_ID", unitCode, 12);
			String strLastLoginTime = CustomFormatHelper.getSystemGMTDate("T_LAST_LOGIN_TIME", "yyyy-mm-dd hh:mm:ss.fff");
			loGenSQLTool.addField("T_LAST_LOGIN_TIME", strLastLoginTime, 93);
			loGenSQLTool.addField("T_CURR_LOGIN_TIME", currDT, 93);
			dao.addSqlStatement(loGenSQLTool.getSqlStatement());
		} 
		return dao;
	}
	/**
	 * Checks if the login operation is valid based on the provided credentials.
	 * 
	 * @param oriBu     The original business unit associated with the login attempt.
	 * @param oriUserId The original user ID associated with the login attempt.
	 * @return true if the login is valid, false otherwise.
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
				this.currLoginTime = loginRcd.getValue("T_CURR_LOGIN_TIME");
				isValid = true;
			} else {
				isValid = false;
			} 
		} catch (Exception e) {
			this.logger.error("Exception occured in checkRealLogin function " + e);
		} 
		return isValid;
	}
}
