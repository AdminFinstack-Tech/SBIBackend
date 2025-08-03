package com.csme.csmeapi.mobile.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import com.cs.base.log.CSLogger;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.cs.eximap.log.ASCELog;
import com.cs.eximap.utility.ASGenSQLTool;

/**
 * Service class to DAO operations
 */
public class MobileAppCommonDao {

	private static CSLogger logger = ASCELog.getCELogger("csme-rest");

	public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static String secuDs;
	private static String dbType;
	public static Properties prop;

	public MobileAppCommonDao() throws Exception {
		loadProperties();
		secuDs = DSManager.getSecuDS();
		dbType = DSManager.getDBType(secuDs);

	}

	public Long saveAuditRecord(String requestMsg, String requestId, String msgType) {
		try {
			Long seq = getSequence();
			if (seq == null)
				throw new Exception("Not able to generate sequence ");

			String reportDate = df.format(new Date());
			String tableWithSchema = DSManager.getSchemaedTableName(secuDs, "EE_API_MSG");
			ASGenSQLTool assignSQLTool1 = new ASGenSQLTool(ASGenSQLTool.I_SQL_TYPE_INSERT, tableWithSchema, null, null,
					dbType);
			assignSQLTool1.addField("EE_API_MSG_ID", Long.toString(seq.longValue()), Types.BIGINT);
			assignSQLTool1.addField("REQUEST_DATA", requestMsg, Types.VARCHAR);
			assignSQLTool1.addField("MSG_TYPE", msgType, Types.VARCHAR);
			assignSQLTool1.addField("REQUEST_ID", requestId, Types.VARCHAR);
			assignSQLTool1.addField("RCVD_TIME", reportDate, Types.TIMESTAMP);
			String query1 = assignSQLTool1.genSqlString();
			logger.info("Query to Insert the details in EE_API_MSG table: " + query1);
			int update = WSDBHelper.executeUpdate(query1, secuDs);
			logger.info("Audit Record Successfully Inserted. Count is:  " + update);
			return seq.longValue();
		} catch (Exception e) {
			logger.info("Error While Insert Audit Record " + e);
			return null;
		}
	}

	public String updateAuditRecord(Long eeAPIMsgId, String respMsg, String errorMsg, String httpStatus) {
		try {
			if (eeAPIMsgId == null) {
				throw new Exception("Sequence is null ");
			}
			logger.info("Updating Audit Record for EE_API_MSG_ID:" + eeAPIMsgId.longValue());

			String reportDate = df.format(new Date());
			String tableWithSchema = DSManager.getSchemaedTableName(secuDs, "EE_API_MSG");
			ASGenSQLTool updateTool = new ASGenSQLTool(ASGenSQLTool.I_SQL_TYPE_UPDATE, tableWithSchema, null, null,
					dbType);
			updateTool.addField("RESPONSE_DATA", respMsg, Types.VARCHAR);
			updateTool.addField("RESP_TIME", reportDate, Types.TIMESTAMP);
			updateTool.addField("HTTP_STATUS_CODE", httpStatus, Types.TIMESTAMP);
			if (errorMsg == null) {
				updateTool.addField("STATUS", "SUCCESS", Types.VARCHAR);
			} else {
				updateTool.addField("STATUS", "FAIL", Types.VARCHAR);
				updateTool.addField("FAILED_REASON", errorMsg, Types.VARCHAR);
			}
			updateTool.appendClause("WHERE ");
			updateTool.appendClause("EE_API_MSG_ID =" + eeAPIMsgId);

			String updateQuey = updateTool.genSqlString();
			logger.info("Query to Update the Audit Record : " + updateQuey);
			int update = WSDBHelper.executeUpdate(updateQuey, secuDs);
			logger.info("Audit Record Successfully Updated. Count is:  " + update);
			return Integer.toString(update);
		} catch (Exception e) {
			logger.info("Cannot Update Audit Record" + e);
			return "";
		}
	}

	public Long getSequence() throws Exception {
		long nextValue = 0;
		try {
			String query = "select EE_API_MSG_ID_SEQ.NEXTVAL from DUAL";
			logger.info("Query to get the Sequence:" + query);
			SQLRecordSet rs = WSDBHelper.executeQuery(query, secuDs);
			boolean condition = rs.isSuccess() && rs.getRecordCount() > 0 && rs.getFirstRecord().getValue("NEXTVAL") != null;
			if (condition) {
				nextValue = Integer.parseInt(rs.getFirstRecord().getValue("NEXTVAL"));
			}
			logger.info("Next Sequence is " + nextValue);
			return nextValue;
		} catch (Exception e) {
			logger.error("Error while Getting the sequence ");
			return null;
		}
	}

	public boolean requestAlreadyExists(String requestId) throws Exception {
		try {
			String query = "SELECT REQUEST_ID from CEUSER.EE_API_MSG WHERE REQUEST_ID='" + requestId + "'";
			logger.info("Query to verify the request exist or not:" + query);
			SQLRecordSet rs = WSDBHelper.executeQuery(query, secuDs);
			boolean condition = rs.isSuccess() && rs.getRecordCount() > 0 && rs.getFirstRecord().getValue("REQUEST_ID") != null;
			if (condition) {
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error("Error while verifying the request");
			logger.error(e);
			return false;
		}
	}

	public boolean isCurrencyExists(String ccy) throws Exception {
		try {
			String query = "select C_CURRENCY  from CETRX.STD_CURRENCY WHERE C_CURRENCY='" + ccy + "'";
			logger.info("Query to verify the Currency exist or not:" + query);
			SQLRecordSet rs = WSDBHelper.executeQuery(query, secuDs);
			logger.info("Query Executed ");
			if (rs != null && rs.isSuccess() && rs.getRecordCount() > 0) {
				logger.info("Currency Available  ");
				if (rs.getFirstRecord().getValue("C_CURRENCY") != null) {
					return true;
				}
				logger.info("Currency Not Available  ");
				return false;
			}
			logger.info("Currency Not Available  ");
			return false;
		} catch (Exception e) {
			logger.error("Error while verifying the Currency");
			logger.error(e);
			return false;
		}
	}

	public static String saveTokenRecord(String cUnitCode, String cUserId) {
		try {
			loadProperties();
			Long seq = getTokenSequence();
			if (seq == null)
				throw new Exception("Not able to generate sequence ");
			String crtnDate = df.format(new Date());
			long timeInSecs = Calendar.getInstance().getTimeInMillis();
			// add 5 minutes
			// Date afterAdding5Mins = new Date(timeInSecs + (5 * 60 * 1000));
			Date afterAdding5Mins = new Date(timeInSecs + Integer.parseInt(prop.getProperty("TokenExpiryInSeconds")));
			String expnDate = df.format(afterAdding5Mins);
			String token = UUID.randomUUID().toString();
			String tableWithSchema = DSManager.getSchemaedTableName(secuDs, "CETRX.USER_SSO_TOKEN");
			ASGenSQLTool assignSQLTool1 = new ASGenSQLTool(ASGenSQLTool.I_SQL_TYPE_INSERT, "CETRX.USER_SSO_TOKEN", null,
					null, dbType);
			assignSQLTool1.addField("USER_SSO_TOKEN_ID", Long.toString(seq.longValue()), Types.BIGINT);
			assignSQLTool1.addField("C_UNIT_CODE", cUserId, Types.VARCHAR);
			assignSQLTool1.addField("C_USER_ID", cUnitCode, Types.VARCHAR);
			assignSQLTool1.addField("TOKEN_CRTN_DATE", crtnDate, Types.TIMESTAMP);
			assignSQLTool1.addField("TOKEN_EXPR_DATE", expnDate, Types.TIMESTAMP);
			assignSQLTool1.addField("TOKEN", token, Types.VARCHAR);
			assignSQLTool1.addField("STATUS", "GENERATED", Types.VARCHAR);

			String query1 = assignSQLTool1.genSqlString();
			logger.info("Query to Insert the details in USER_SSO_TOKEN table: " + query1);
			int update = WSDBHelper.executeUpdate(query1, secuDs);
			logger.info("USER_SSO_TOKEN Record Successfully Inserted. Count is:  " + update);
			return token;
		} catch (Exception e) {
			logger.info("Error While Insert USER_SSO_TOKEN Record " + e);
			return null;
		}
	}

	public static Long getTokenSequence() throws Exception {
		long nextValue = 0;
		try {
			String query = "select CETRX.USER_SSO_TOKEN_SEQ.NEXTVAL from DUAL";
			logger.info("Query to get the Sequence:" + query);
			SQLRecordSet rs = WSDBHelper.executeQuery(query, secuDs);
			boolean condition = rs.isSuccess() && rs.getRecordCount() > 0 && rs.getFirstRecord().getValue("NEXTVAL") != null;
			if (condition) {
				nextValue = Integer.parseInt(rs.getFirstRecord().getValue("NEXTVAL"));
			}
			logger.info("Next Sequence is " + nextValue);
			return nextValue;
		} catch (Exception e) {
			logger.error("Error while Getting the sequence ");
			return null;
		}
	}

	private static Properties loadProperties() throws Exception {
		// ClassLoader classLoader = new GetTokenFromCE().getClass().getClassLoader();
		// File file = new File(cl.getResource("resources/sso.properties").getFile());
		if (prop != null)
			return prop;
		prop = new Properties();
		//ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		//InputStream input = classLoader.getResourceAsStream("resources/config.properties");
		File file = new File("INT/SSO_KEYS/sso.properties");
		prop = new Properties();
		InputStream input = new FileInputStream(file);
		prop.load(input);
		logger.info(prop);
		return prop;
	}

	public String getCOMP_NAME(String companyId) throws Exception {
		String compId = null;
		try {
			String query = "SELECT COMP_NAME from CEUSER.SEC_BUSINESS_UNIT WHERE C_UNIT_CODE='" + companyId + "'";
			logger.info("Query to Get the COMP_NAME :" + query);
			SQLRecordSet rs = WSDBHelper.executeQuery(query, secuDs);
			if (rs.isSuccess() && rs.getRecordCount() > 0) {
				compId = rs.getFirstRecord().getValue("COMP_NAME");
			}
			return compId;
		} catch (Exception e) {
			logger.error("Error while Getting the COMP_NAME for Company : " + companyId);
			return null;
		}
	}
	
	public Map<String , String> getProductInformation(String mainRef,String unitCode) throws Exception {
		Map<String , String> productMap = new HashMap<String , String>();
		try {
			String query = "SELECT * FROM CETRX.TRX_INBOX WHERE C_MAIN_REF='" + mainRef + "' AND C_UNIT_CODE = '"+unitCode+"'";
			logger.info("Query to Get the COMP_NAME :" + query);
			SQLRecordSet rs = WSDBHelper.executeQuery(query, secuDs);
			for(int i=0;i< rs.getRecordCount() ; i++) {
				SQLRecord record = rs.getSQLRecord(i);
				productMap.put("C_PRODUCT_ID", record.getValue("C_PRODUCT_ID"));
				productMap.put("C_FUNC_SHORT_NAME", record.getValue("C_FUNC_SHORT_NAME"));
				productMap.put("C_FUNC_ID", record.getValue("C_FUNC_ID"));
				productMap.put("C_MODULE", record.getValue("C_MODULE"));
				productMap.put("C_TRX_REF", record.getValue("C_TRX_REF"));
				productMap.put("C_MAIN_REF", record.getValue("C_MAIN_REF"));
				productMap.put("I_EVENT_TIMES", record.getValue("I_EVENT_TIMES"));
				productMap.put("PARENT_MAIN_REF", record.getValue("PARENT_MAIN_REF"));
				productMap.put("C_FUNC_ID", record.getValue("C_FUNC_ID"));
			}
			return productMap;
		} catch (Exception e) {
			logger.error("Error while Getting the COMP_NAME for Company : " + productMap);
			return null;
		}
	}

	public static boolean hasValue1(String val) {
		if (val != null && val.length() > 0)
			return true;
		return false;
	}

	public String updateAuditRecord(String requestId, String respMsg, String errorMsg, org.springframework.http.HttpStatus httpStatus) {
		try {

			String reportDate = df.format(new Date());
			String tableWithSchema = DSManager.getSchemaedTableName(secuDs, "EE_API_MSG");
			ASGenSQLTool updateTool = new ASGenSQLTool(ASGenSQLTool.I_SQL_TYPE_UPDATE, tableWithSchema, null, null,
					dbType);
			if(respMsg != null && respMsg.length() > 3998) {
				updateTool.addField("RESPONSE_DATA", respMsg.substring(0, 3999), Types.VARCHAR);
			} else 
				updateTool.addField("RESPONSE_DATA", respMsg, Types.VARCHAR);

			updateTool.addField("RESP_TIME", reportDate, Types.TIMESTAMP);
			updateTool.addField("HTTP_STATUS_CODE", httpStatus.toString(), Types.TIMESTAMP);
			if (errorMsg == null) {
				updateTool.addField("STATUS", "SUCCESS", Types.VARCHAR);
			} else {
				updateTool.addField("STATUS", "FAIL", Types.VARCHAR);
				updateTool.addField("FAILED_REASON", errorMsg, Types.VARCHAR);
			}
			updateTool.appendClause("WHERE ");
			updateTool.appendClause("REQUEST_ID ='" + requestId+"'");

			String updateQuey = updateTool.genSqlString();
			logger.info("Query to Update the Audit Record : " + updateQuey);
			int update = WSDBHelper.executeUpdate(updateQuey, secuDs);
			logger.info("Audit Record Successfully Updated. Count is:  " + update);
			return Integer.toString(update);
		} catch (Exception e) {
			logger.info("Cannot Update Audit Record" + e);
			return "";
		}
	}

	public String updateAuditRecord(Long eeAPIMsgId, String respMsg, String errorMsg,
			org.springframework.http.HttpStatus badRequest) {
		try {
			if (eeAPIMsgId == null) {
				throw new Exception("Sequence is null ");
			}
			logger.info("Updating Audit Record for EE_API_MSG_ID:" + eeAPIMsgId.longValue());

			String reportDate = df.format(new Date());
			String tableWithSchema = DSManager.getSchemaedTableName(secuDs, "EE_API_MSG");
			ASGenSQLTool updateTool = new ASGenSQLTool(ASGenSQLTool.I_SQL_TYPE_UPDATE, tableWithSchema, null, null,
					dbType);
			updateTool.addField("RESPONSE_DATA", respMsg, Types.VARCHAR);
			updateTool.addField("RESP_TIME", reportDate, Types.TIMESTAMP);
			updateTool.addField("HTTP_STATUS_CODE", badRequest.toString(), Types.TIMESTAMP);
			if (errorMsg != null) {
				updateTool.addField("STATUS", "FAIL", Types.VARCHAR);
				updateTool.addField("FAILED_REASON", errorMsg, Types.VARCHAR);
			} else {
				updateTool.addField("STATUS", "SUCCESS", Types.VARCHAR);
			}
			updateTool.appendClause("WHERE ");
			updateTool.appendClause("EE_API_MSG_ID =" + eeAPIMsgId);

			String updateQuey = updateTool.genSqlString();
			logger.info("Query to Update the Audit Record : " + updateQuey);
			int update = WSDBHelper.executeUpdate(updateQuey, secuDs);
			logger.info("Audit Record Successfully Updated. Count is:  " + update);
			return Integer.toString(update);
		} catch (Exception e) {
			logger.info("Cannot Update Audit Record" + e);
			return "";
		}
	}
	
	public String getReference(String unitcode, String cRefName) throws Exception {
		// Step 1: Select I_REF_SEQ
		secuDs = DSManager.getSecuDS();
		dbType = DSManager.getDBType(secuDs);
		String iPrefix = null;
		String iSuffix = null; 
		
		logger.info("Preparing select query to get I_REF_SEQ for unitcode: " + unitcode + " and cRefName: " + cRefName);
		String selectClause = "WHERE C_UNIT_CODE = '" + unitcode + "' AND C_REF_NAME = '" + cRefName + "'";
		ASGenSQLTool selectSql = new ASGenSQLTool(ASGenSQLTool.I_SQL_TYPE_SELECT, "CETRX.STD_TRX_REF", selectClause,
				"YYYMMDD", dbType);
		selectSql.addField("I_REF_SEQ", null, null);
		selectSql.addField("C_REF_SUF", null, null);
		selectSql.addField("C_REF_PRE", null, null);
		
		String selectQuery = selectSql.genSqlString();
		logger.info("Generated select query: " + selectQuery);

		SQLRecordSet resultSet = WSDBHelper.executeQuery(selectQuery, secuDs);
		String iRefSeqFromDb = "";
		if (resultSet.isSuccess() && resultSet.getRecordCount() > 0) {
			SQLRecord sqlRecord = resultSet.getSQLRecord(0);
			iRefSeqFromDb = sqlRecord.getValue("I_REF_SEQ");
			iSuffix = sqlRecord.getValue("C_REF_SUF");
			iPrefix = sqlRecord.getValue("C_REF_PRE");
			logger.info("Received I_REF_SEQ from DB: " + iRefSeqFromDb);
		} 

		// Step 2: Update I_REF_SEQ
		logger.info("Preparing update query to increment I_REF_SEQ for unitcode: " + unitcode + " and cRefName: "
				+ cRefName);
		String updateClause = "WHERE C_UNIT_CODE = '" + unitcode + "' AND C_REF_NAME = '" + cRefName + "'";
		ASGenSQLTool updateSql = new ASGenSQLTool(ASGenSQLTool.I_SQL_TYPE_UPDATE, "CETRX.STD_TRX_REF", updateClause,
				"YYYMMDD", dbType);
		int int1 = Integer.parseInt(iRefSeqFromDb);
		int s = int1 + 1;
		updateSql.addField("I_REF_SEQ", String.valueOf(s), 4);
		String updateQuery = updateSql.genSqlString();
		logger.info("Generated update query: " + updateQuery);

		int updateCounts = WSDBHelper.executeUpdate(updateQuery, secuDs);
		logger.info("Update counts: " + updateCounts);

		String format = String.format("%07d", s);
		LocalDateTime now = LocalDateTime.now();
		int year = now.getYear();
		int lastTwoDigits = year % 100;
		
		String ref = (iPrefix != null ? iPrefix : "488") + format + (iSuffix != null ? iSuffix : "AA"+lastTwoDigits);
		logger.info("Generated Reference: " + ref);

		logger.info("Preparing insert query for new record in CETRX.STD_TRX_REFUSED");
		ASGenSQLTool insertSql = new ASGenSQLTool(ASGenSQLTool.I_SQL_TYPE_INSERT, "CETRX.STD_TRX_REFUSED", null, null,
				dbType);

		now = LocalDateTime.now();
		String nowString = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));

		insertSql.addField("C_REF_NAME", cRefName, ASGenSQLTool.I_SQL_TYPE_INSERT);
		insertSql.addField("C_UNIT_CODE", unitcode, ASGenSQLTool.I_SQL_TYPE_INSERT);
		insertSql.addField("C_REF_NO", ref, ASGenSQLTool.I_SQL_TYPE_INSERT);
		insertSql.addField("C_REF_STATUS", "T", ASGenSQLTool.I_SQL_TYPE_INSERT);
		insertSql.addField("C_CONF_BY", "LGREQAPP", ASGenSQLTool.I_SQL_TYPE_INSERT);
		insertSql.addField("C_CONF_TIME", nowString, ASGenSQLTool.I_SQL_TYPE_INSERT);
		insertSql.addField("C_MAIN_REF", ref, ASGenSQLTool.I_SQL_TYPE_INSERT);
		String insertQuery = insertSql.genSqlString();
		logger.info("Generated insert query: " + insertQuery);
		int insertCounts = WSDBHelper.executeUpdate(insertQuery, secuDs);
		logger.info("Insert counts: " + insertCounts);

		return ref;
	}
	
}
