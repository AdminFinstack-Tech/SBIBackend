/**
 * Provides services related to financial transactions and operations.
 * This package contains classes and components for handling financial inquiries,
 * executing database operations, and processing transaction information.
 */
package com.csme.csmeapi.fin.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.exec.SQLDao;
import com.cs.core.dao.exec.SQLGenTool;
import com.cs.core.dao.exec.SQLGenToolHelper;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinUtil;

/**
 * Provides common data access methods for the mobile application.
 */
public class MobileAppCommonDao<T> {
	
	FinUtil finUtil = new FinUtil();
	
	/**
     * Logger for logging information and errors.
     */
    private static final Logger logger = LogManager.getLogger("CSMEMobile");

    /**
     * Date format for timestamped operations.
     */
    public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * The secured data source string.
     */
    private static String secuDs;

    /**
     * The database type.
     */
    private static String dbType;

    /**
     * Properties for configuration settings.
     */
    public static Properties prop;

    /**
     * Initializes the MobileAppCommonDao and loads properties.
     *
     * @throws Exception if there is an error loading properties.
     */
	public MobileAppCommonDao() throws Exception {
		secuDs = FinUtil.SECDS;
		dbType = FinUtil.SECDBTYPE;
	}

	 /**
     * Saves an audit record.
     *
     * @param requestMsg the request message.
     * @param requestId  the request ID.
     * @param msgType    the message type.
     * @return the ID of the saved audit record.
     */
	public Long saveAuditRecord(String requestMsg, String requestId, String msgType) {
		try {
			SQLDao sqlDao = new SQLDao();
		    sqlDao.setDataSource(secuDs);
			Long seq = getSequence();
			if (seq == null)
				throw new Exception("Not able to generate sequence "); 
			String reportDate = df.format(new Date());
			SQLGenTool assignSQLTool1 = SQLGenToolHelper.getInsertTool(dbType);
		    String table = DSManager.getSchemaedTableName(secuDs, "EE_API_MSG");
		    assignSQLTool1.setTable(table);
			assignSQLTool1.addField("EE_API_MSG_ID", seq.longValue() + "", -5);
			assignSQLTool1.addField("REQUEST_DATA", requestMsg, 12);
			assignSQLTool1.addField("MSG_TYPE", msgType, 12);
			assignSQLTool1.addField("REQUEST_ID", requestId, 12);
			assignSQLTool1.addField("RCVD_TIME", reportDate, 93);
			sqlDao.addSqlStatement(assignSQLTool1.getSqlStatement());
			logger.info("Query to insert the Audit Record : " + assignSQLTool1.getSqlStatement());
			int update = WSDBHelper.executeUpdate(sqlDao);
			logger.info("Audit Record Successfully Inserted. Count is:  " + update);
			return Long.valueOf(seq.longValue());
		} catch (Exception e) {
			logger.info("Error While Insert Audit Record " + e);
			FinUtil.getErrorStackTrace(e);
			return null;
		} 
	}
	/**
     * Updates an audit record with response information.
     *
     * @param eeAPIMsgId the ID of the audit record to update.
     * @param respMsg    the response message.
     * @param errorMsg   the error message, if any.
     * @param httpStatus the HTTP status code.
     * @return a string representation of the update count.
     */
	public String updateAuditRecord(Long eeAPIMsgId, String respMsg, String errorMsg, String httpStatus) {
		try {
			SQLDao sqlDao = new SQLDao();
		    sqlDao.setDataSource(secuDs);
			if (eeAPIMsgId == null)
				throw new Exception("Sequence is null "); 
			logger.info("Updating Audit Record for EE_API_MSG_ID:" + eeAPIMsgId.longValue());
			String reportDate = df.format(new Date());
			SQLGenTool updateTool = SQLGenToolHelper.getUpdateTool(dbType);
		    String table = DSManager.getSchemaedTableName(secuDs, "EE_API_MSG");
		    updateTool.setTable(table);
			//String tableWithSchema = DSManager.getSchemaedTableName(secuDs, "EE_API_MSG");
			//ASGenSQLTool updateTool = new ASGenSQLTool(2, tableWithSchema, null, null, dbType);
			updateTool.addField("RESPONSE_DATA", respMsg, 2005);
			updateTool.addField("RESP_TIME", reportDate, 93);
			updateTool.addField("HTTP_STATUS_CODE", httpStatus, 12);
			if (errorMsg == null) {
				updateTool.addField("STATUS", "SUCCESS", 12);
			} else {
				updateTool.addField("STATUS", "FAIL", 12);
				updateTool.addField("FAILED_REASON", errorMsg, 12);
			} 
			updateTool.appendClause("WHERE ");
			updateTool.appendClause("EE_API_MSG_ID =" + eeAPIMsgId);
			sqlDao.addSqlStatement(updateTool.getSqlStatement());
			logger.info("Query to Update the Audit Record : " + updateTool.getSqlStatement());
			int update = WSDBHelper.executeUpdate(sqlDao);
			logger.info("Audit Record Successfully Updated. Count is:  " + update);
			return update + "";
		} catch (Exception e) {
			logger.info("Cannot Update Audit Record" + e);
			FinUtil.getErrorStackTrace(e);
			return "";
		} 
	}

	/**
     * Retrieves the next sequence value for EE_API_MSG_ID_SEQ.
     *
     * @return the next sequence value or null if an error occurs.
     */
	public Long getSequence() throws Exception {
		long nextValue = 0L;
		try {
			String query = "select EE_API_MSG_ID_SEQ.NEXTVAL from DUAL";
			logger.info("Query to get the Sequence:" + query);
			SQLRecordSet rs = WSDBHelper.executeQuery(query, secuDs);
			if (rs.isSuccess() && rs.getRecordCount() > 0 && 
					rs.getFirstRecord().getValue("NEXTVAL") != null)
				nextValue = Integer.parseInt(rs.getFirstRecord().getValue("NEXTVAL")); 
			logger.info("Next Sequence is " + nextValue);
			return Long.valueOf(nextValue);
		} catch (Exception e) {
			logger.error("Error while Getting the sequence " + FinUtil.getErrorStackTrace(e));
			return null;
		} 
	}
	 /**
     * Checks if a request with the given ID already exists in the database.
     *
     * @param requestId the request ID to check.
     * @return true if the request exists, false otherwise or if an error occurs.
     */
	public boolean requestAlreadyExists(String requestId) throws Exception {
		try {
			String query = "SELECT REQUEST_ID from CEUSER.EE_API_MSG WHERE REQUEST_ID='" + requestId + "'";
			logger.info("Query to verify the request exist or not:" + query);
			SQLRecordSet rs = WSDBHelper.executeQuery(query, secuDs);
			if (rs.isSuccess() && rs.getRecordCount() > 0 && 
					rs.getFirstRecord().getValue("REQUEST_ID") != null)
				return true; 
			return false;
		} catch (Exception e) {
			logger.error("Error while verifying the request" + FinUtil.getErrorStackTrace(e));
			return false;
		} 
	}
	/**
     * Loads properties from the specified file.
     *
     * @throws IOException  if an I/O error occurs.
     * @throws Exception    for any other error during property loading.
     */
	private static void loadProperties() throws IOException, Exception {
        if (prop != null) {
            return;
        }
        String filePath = "INT/SSO_KEYS/sso.properties";
        try (InputStream input = MobileAppCommonDao.class.getClassLoader().getResourceAsStream(filePath)) {
            if (input == null) {
                throw new FileNotFoundException("Property file not found: " + filePath);
            }
            prop = new Properties();
            prop.load(input);
            logger.info("Loaded properties from {}" + filePath);
        } catch (IOException e) {
            logger.error("Error loading properties", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error loading properties", e);
            throw e;
        }
    }

	public static boolean hasValue1(String val) {
		if (val != null && val.length() > 0)
			return true; 
		return false;
	}

	 /**
     * Updates an audit record with response information.
     *
     * @param requestId the ID of the audit record to update.
     * @param respMsg    the response message.
     * @param errorMsg   the error message, if any.
     * @param httpStatus the HTTP status code.
     * @return a string representation of the update count.
     */
	public String updateAuditRecord(String requestId, String respMsg, String errorMsg, HttpStatus httpStatus) {
		try {
			if (requestId == null) {
                throw new IllegalArgumentException("requestId is null");
            }
			SQLDao sqlDao = new SQLDao();
		    sqlDao.setDataSource(secuDs);
		    
			String reportDate = df.format(new Date());
			
			SQLGenTool updateTool = SQLGenToolHelper.getUpdateTool(dbType);
		    String table = DSManager.getSchemaedTableName(secuDs, "EE_API_MSG");
		    updateTool.setTable(table);
		    
			updateTool.addField("RESPONSE_DATA", respMsg, 2005);
			updateTool.addField("RESP_TIME", reportDate, 93);
			updateTool.addField("HTTP_STATUS_CODE", httpStatus.toString(), 12);
			if (errorMsg == null) {
				updateTool.addField("STATUS", "SUCCESS", 12);
			} else {
				updateTool.addField("STATUS", "FAIL", 12);
				updateTool.addField("FAILED_REASON", errorMsg, 12);
			} 
			
			updateTool.appendClause("WHERE ");
			updateTool.appendClause("REQUEST_ID ='" + requestId + "'");
			sqlDao.addSqlStatement(updateTool.getSqlStatement());
			
			logger.info("Query to Update the Audit Record : " + updateTool.getSqlStatement());
			
			int updateCount = WSDBHelper.executeUpdate(sqlDao);
            logger.info("Audit Record Successfully Updated. Count: "+ updateCount);
            
            return String.valueOf(updateCount);
		} catch (IllegalArgumentException e) {
            logger.error("Invalid argument while updating audit record" + FinUtil.getErrorStackTrace(e));
            return "";
        } catch (Exception e) {
            logger.error("Error updating audit record" + FinUtil.getErrorStackTrace(e));
            return "";
        }
	}

	/**
     * Updates an audit record with response information.
     *
     * @param eeAPIMsgId the ID of the audit record to update.
     * @param respMsg    the response message.
     * @param errorMsg   the error message, if any.
     * @param httpStatus the HTTP status code.
     * @return a string representation of the update count.
     */
	public String updateAuditRecord(Long eeAPIMsgId, String respMsg, String errorMsg, HttpStatus badRequest) {
		try {
			if (eeAPIMsgId == null)
				throw new Exception("Sequence is null ");
			
			logger.info("Updating Audit Record for EE_API_MSG_ID:" + eeAPIMsgId.longValue());
			SQLDao sqlDao = new SQLDao();
		    sqlDao.setDataSource(secuDs);
			String reportDate = df.format(new Date());
			SQLGenTool updateTool = SQLGenToolHelper.getUpdateTool(dbType);
		    String table = DSManager.getSchemaedTableName(secuDs, "EE_API_MSG");
		    updateTool.setTable(table);
			updateTool.addField("RESPONSE_DATA", respMsg, 2005);
			updateTool.addField("RESP_TIME", reportDate, 93);
			updateTool.addField("HTTP_STATUS_CODE", badRequest.toString(), 12);
			if (errorMsg == null) {
				updateTool.addField("STATUS", "SUCCESS", 12);
			} else {
				updateTool.addField("STATUS", "FAIL", 12);
				updateTool.addField("FAILED_REASON", errorMsg, 12);
			} 
			updateTool.appendClause("WHERE ");
			updateTool.appendClause("EE_API_MSG_ID =" + eeAPIMsgId);
			sqlDao.addSqlStatement(updateTool.getSqlStatement());
			logger.info("Query to Update the Audit Record : " + updateTool.getSqlStatement());
			int update = WSDBHelper.executeUpdate(sqlDao);
			logger.info("Audit Record Successfully Updated. Count is:  " + update);
			return update + "";
		} catch (Exception e) {
			logger.info("Cannot Update Audit Record" + FinUtil.getErrorStackTrace(e));
			return "";
		} 
	}

	/**
	 * Checks if a string is empty (null or whitespace).
	 *
	 * @param val the string to check.
	 * @return true if the string is empty, false otherwise.
	 */
	public static boolean isEmpty(String val) {
	    return val == null || val.trim().isEmpty();
	}

	/**
     * Retrieves product information based on the main reference and unit code.
     *
     * @param mainRef  The main reference for the product.
     * @param unitCode The unit code for the product.
     * @return A map containing product information, or null if an error occurs.
     * @throws Exception If there is an error during database query execution.
     */
	public Map<String, String> getProductInformation(String mainRef, String unitCode) throws Exception {
		Map<String, String> productMap = new HashMap<>();
		try {
			String query = "SELECT * FROM CETRX.TRX_INBOX WHERE C_MAIN_REF='" + mainRef + "' AND C_UNIT_CODE = '" + unitCode + "'";
			logger.info("Query to Get the COMP_NAME :" + query);
			SQLRecordSet rs = WSDBHelper.executeQuery(query, secuDs);
			for (int i = 0; i < rs.getRecordCount(); i++) {
				 SQLRecord record = rs.getSQLRecord(i);
	                productMap.computeIfAbsent("C_PRODUCT_ID", k -> record.getValue("C_PRODUCT_ID"));
	                productMap.computeIfAbsent("C_FUNC_SHORT_NAME", k -> record.getValue("C_FUNC_SHORT_NAME"));
	                productMap.computeIfAbsent("C_FUNC_ID", k -> record.getValue("C_FUNC_ID"));
	                productMap.computeIfAbsent("C_MODULE", k -> record.getValue("C_MODULE"));
	                productMap.computeIfAbsent("C_TRX_REF", k -> record.getValue("C_TRX_REF"));
	                productMap.computeIfAbsent("C_MAIN_REF", k -> record.getValue("C_MAIN_REF"));
	                productMap.computeIfAbsent("I_EVENT_TIMES", k -> record.getValue("I_EVENT_TIMES"));
	                productMap.computeIfAbsent("PARENT_MAIN_REF", k -> record.getValue("PARENT_MAIN_REF"));
			} 
			return productMap;
		} catch (Exception e) {
			logger.error("Error while Getting the COMP_NAME for Company : " + productMap);
			return null;
		} 
	}
	
}
