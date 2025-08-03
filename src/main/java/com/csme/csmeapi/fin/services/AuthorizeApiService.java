package com.csme.csmeapi.fin.services;

import java.util.Map;

import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.cs.ce.core.helper.ContextHelper;
import com.cs.ce.core.helper.RecordConverter;
import com.cs.ceap.ejb.business.CETrxInBoxManagerRecBean;
import com.cs.core.cache.CacheAPParameterHelper;
import com.cs.core.cache.CacheSYSHelper;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.exec.SQLDao;
import com.cs.core.dao.exec.SQLDaoResult;
import com.cs.core.dao.exec.SQLGenTool;
import com.cs.core.dao.exec.SQLGenToolHelper;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.cs.core.dao.exec.SQLStatement;
import com.cs.core.err.ErrorHandling;
import com.cs.core.err.WSException;
import com.cs.core.intf.IReadMap;
import com.cs.core.pojo.UserInfo;
import com.cs.core.pojo.bo.FuncData;
import com.cs.core.pojo.bo.SimpleDO;
import com.cs.core.pojo.buinfo.Company;
import com.cs.core.pojo.buinfo.CompanyGroup;
import com.cs.core.pojo.buinfo.SysInfo;
import com.cs.core.pojo.swift.SwiftInfo;
import com.cs.core.request.Request;
import com.cs.core.request.TrxRequest;
import com.cs.core.result.FinalStatus;
import com.cs.core.result.GapiInfo;
import com.cs.core.result.Result;
import com.cs.core.result.TrxResult;
import com.cs.core.utility.SessionContext;
import com.cs.core.utility.SessionUtil;
import com.cs.core.utility.StringUtil;
import com.cs.core.xml.object.func.Func;
import com.cs.core.xml.object.funcattr.FuncAttr;
import com.cs.core.xml.object.funcattr.component.Component;
import com.cs.eximap.busiintf.Busiintf;
import com.cs.eximap.busiintf.TrxReAssigned;
import com.cs.eximap.ejb.ctrl.RelAuthCheckProcess;
import com.cs.eximap.ejb.ctrl.ReleaseNotification;
import com.cs.eximap.log.CELogSTDExec;
import com.cs.eximap.log.CELogSTPExec;
import com.cs.eximap.miscintf.delegate.TrxLedgerAddRec;
import com.cs.eximap.miscintf.delegate.TrxManagerImage;
import com.cs.eximap.utility.ASDBHelper;
import com.cs.eximap.utility.TrxRequestHelper;
import com.cs.eximap.utility.TrxResultHelper;
import com.cs.eximap.utility.helper.GapiHelper;
import com.cs.eximap.utility.helper.SwiftHelper;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XAuthorizeRequest;

@SuppressWarnings("deprecation")
/**
 * Provides authorization functionality for API users.
 */
public class AuthorizeApiService {
	
	FinUtil finUtil = new FinUtil();
	
	// Initialize logger for logging messages related to CSMEMobile.
	private Logger logger = LogManager.getLogger("CSMEMobile");

	// Initialize variables for background group, country, security data source, transaction data source,
	// and transaction database type using values from FinUtil class.
	private String strBKGP = FinUtil.BKGRP; // Background group variable.
	private String strCNTY = FinUtil.CNTY; // Country variable.
	protected String SecuDS = FinUtil.SECDS; // Security data source variable.
	protected String TrxDS = FinUtil.TRXDS; // Transaction data source variable.
	protected String TrxDBType = FinUtil.TRXDBTYPE; // Transaction database type variable.

	// Declare DAO object for common mobile app operations.
	MobileAppCommonDao<?> commonDAO = null;

	// Flag to indicate whether image uploads to the database are enabled.
	boolean imgUpload2DB;

	/**
	 * Default constructor for AuthorizeApiService class.
	 * Initializes imgUpload2DB to false and creates a new instance of MobileAppCommonDao.
	 */
	public AuthorizeApiService() {
		this.imgUpload2DB = false;
		try {
			this.commonDAO = new MobileAppCommonDao<>();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Retrieves function data based on the provided authorization request.
	 *
	 * @param body The authorization request containing necessary information.
	 * @return Result object containing the retrieved function data.
	 * @throws Exception If an error occurs during the retrieval process.
	 */
	public Result getfunction(@Valid XAuthorizeRequest body) throws Exception {
		try {
			// Get product information based on the main reference and corporate ID from the DAO.
			Map<String, String> productMap = this.commonDAO.getProductInformation(body.getMainRef(), body.getCorporateId());
			
			// Create data objects for function and catalog data.
			FuncData funcData = new FuncData();
			SimpleDO cataData = new SimpleDO();
			
			// Populate catalog data with product information.
			cataData.put("C_TRX_REF", productMap.get("C_TRX_REF"));
			cataData.put("C_MAIN_REF", productMap.get("C_MAIN_REF"));
			cataData.put("I_EVENT_TIMES", productMap.get("I_EVENT_TIMES"));
			cataData.put("C_PRODUCT_ID", productMap.get("C_PRODUCT_ID"));
			cataData.put("PARENT_MAIN_REF", productMap.get("PARENT_MAIN_REF"));
			cataData.put("C_TRX_STATUS", productMap.get("C_TRX_STATUS"));
			cataData.put("C_EVENT_STATUS", productMap.get("C_EVENT_STATUS"));
			cataData.put("C_FUNC_SHORT_NAME", productMap.get("C_FUNC_SHORT_NAME"));
			cataData.put("C_FUNC_ID", productMap.get("C_FUNC_ID"));
			
			// Determine the action and set related catalog data fields accordingly.
			String action = body.getAction().toString();
			if(action.equalsIgnoreCase("REJECTED") || action.equalsIgnoreCase("Rejected")) {
				cataData.put("C_IS_AGREE", "N");
				cataData.put("C_REFUSE_REASON",body.getRejectedReason());
			} else {
				cataData.put("C_IS_AGREE", "Y");
			}
			
			// Set additional catalog data fields.
			cataData.put("C_CREA_BY", body.getUserId());
			cataData.put("C_UNIT_CODE", body.getCorporateId());
			
			 // Set catalog and screen data in FuncData object.
			funcData.setCatalogData(cataData);
			funcData.setScreenData(cataData);
			
			 // Get function details from cache based on function ID.
			String funcId = cataData.get("C_FUNC_ID");
			Func function = CacheAPParameterHelper.getObjFunc(this.strBKGP, this.strCNTY, funcId);
			
			// Get main component from function details.
			String mainComp = function.getFunction().getMainComp();
			
			// Create and set session context and system information.
			SessionContext ctx = new SessionContext();
			SysInfo sysInfo = new SysInfo();
			
			// Set sysInfo fields...
			sysInfo.setOriginalFuncID(productMap.get("C_FUNC_ID"));
			sysInfo.setFuncId(funcId);
			
			// Log session and user information.
			setSTPUserInfo(body.getCorporateId(), body.getUserId(), ctx);
			this.logger.info("Company Group information is :" + ctx.getCmpGroup().toString());
			this.logger.info("User information is :" + ctx.getUsrInfo().toString());
			Company company = setCmpId(body.getCorporateId());
			this.logger.info("company information is :" + company.toString());
			
			// Set system information in session context.
			sysInfo.setCompInfo(company);
			sysInfo.setAuthLevel(ctx.getUsrInfo().getAuthLevel());
			ctx.setSysInfo(sysInfo);
			this.logger.info("Company Group information is :" + ctx.getCmpGroup().toString());
			this.logger.info("User information is :" + ctx.getUsrInfo().toString());
			
			 // Create and configure request object.
			Request request = new Request(3);
			request.setComponent(mainComp);
			request.setRequestInfo(funcData);
			request.setContext(ctx);
			
			 // Set session context for the request.
			SessionContext cxt = request.getContext();
			SessionUtil.setSessionContext(cxt);
			
			// Log request message.
			this.logger.info("---------------------------------");
			this.logger.info("Request Message is :" + request.toString());
			CELogSTDExec.info(AuthorizeApiService.class, "---------------------------------");
			CELogSTDExec.info(AuthorizeApiService.class, "Request Message is :" + request.toString());
			
			 // Execute data authorizer and return the result.
			Result aRes = new Result();
			return getDataAuthorizer(request, aRes);
		} catch (Exception e) {
			logger.error("Exception has occured processing function data"+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Processes the data for a given request and result, handling various operations and updates.
	 * 
	 * @param aReq The request object containing transaction information.
	 * @param aRes The result object to store transaction processing results.
	 * @return The result object after processing the transaction data.
	 * @throws Exception If an error occurs during data processing.
	 */
	public Result getData(Request aReq, Result aRes) throws Exception {
	    // Initialize transaction request and result objects
	    TrxRequest req = TrxRequestHelper.getTrxRequest(aReq);
	    TrxResult res = TrxResultHelper.getTrxResult(aRes);
	    
	    // Set release function flag in the request
	    req.setReleaseFunction(true);
	    
	    // Check if double release is needed
	    boolean needDoubleRelease = needDoubleRelease(req);
	    if (needDoubleRelease) {
	        // Update authorization and inbox tab
	        boolean completeRelease = updateAuthAndInboxTab(req);
	        if (!completeRelease) {
	            // Execute SQL update and handle partial release
	            SQLDao dao = req.getDao();
	            SQLDaoResult result = ASDBHelper.executeUpdate(dao);
	            if (!result.isSuccess())
	                throw result.getException(); 
	            res.setResult("PART_RELEASE");
	            return (Result)res;
	        } 
	    } 
	    
	    // Set transaction and inbox status based on request conditions
	    req.setTrxStatus("M");
	    req.setInboxStatus("M");
	    if (req.isRefused()) {
	        req.setTrxStatus("R");
	        req.setInboxStatus("R");
	    } 
	    if (req.isSave()) {
	        req.setTrxStatus("S");
	        req.setInboxStatus("Z");
	    } 
	    
	    // Initialize GapiInfo and SwiftInfo objects
	    GapiInfo gapiInfo = null;
	    SwiftInfo swiftInfo = null;
	    try {
	        // Get original function ID and function attributes
	        String oFuncId = ContextHelper.getSysInfo().getOriginalFuncID();
	        FuncAttr funcAttr = ContextHelper.getFuncAttr(oFuncId, "P");
	        
	        // Set final status based on request and result
	        setFinalStatus(req, (Result)res);
	        
	        // Iterate through components and release data
	        for (int i = 0, length = funcAttr.getComponentSize(); i < length; i++) {
	            Component comp = funcAttr.getComponent(i);
	            String compName = comp.getName();
	            Busiintf obj = (Busiintf)Class.forName("com.cs.eximap.busiintf." + compName).newInstance();
	            res = (TrxResult)obj.releaseData((Request)req, comp, (Result)res);
	        } 
	        
	        // Get GapiInfo and SwiftInfo objects and update inbox status
	        gapiInfo = res.getGapiReturn();
	        swiftInfo = res.getSwiftResult();
	        if (gapiInfo != null && gapiInfo.isHasGapi()) {
	            req.setInboxStatus("S");
	        } else if (swiftInfo != null && swiftInfo.hasSendSwift()) {
	            req.setInboxStatus("S");
	        } 
	        
	        // Process ledger data and image release
	        TrxLedgerAddRec ledger = new TrxLedgerAddRec();
	        Result ledgerRs = new Result();
	        ledgerRs = ledger.releaseData((Request)req, ledgerRs);
	        if (!ledgerRs.isSuccess())
	            return ledgerRs; 
	        
	        TrxManagerImage image = new TrxManagerImage();
	        image.releaseData((Request)req, (Result)res);
	        if (!res.isSuccess())
	            return (Result)res; 
	        
	        // Update inbox manager record, process notifications, and execute SQL update
	        CETrxInBoxManagerRecBean inboxRec = new CETrxInBoxManagerRecBean(req);
	        inboxRec.getReleaseUpdateData();
	        ReleaseNotification processRefuseMails = new ReleaseNotification(req, res, null);
	        if (req.isRefused()) {
	            processRefuseMails.processRefuseMails();
	        } else {
	            processRefuseMails.processReleaseMails(true);
	        } 
	        
	        SQLDao dao = req.getDao();
	        CELogSTDExec.info(AuthorizeApiService.class, "dao is :" + dao.getSqlStatements().toString());
	        SQLDaoResult result = ASDBHelper.executeUpdate(dao);
	        if (!result.isSuccess())
	            throw result.getException(); 
	        
	        return (Result)res;
	    } catch (Exception e) {
	        // Send notifications if available and rethrow the exception
	        if (gapiInfo != null)
	            GapiHelper.sendDom(gapiInfo, req); 
	        if (swiftInfo != null)
	            SwiftHelper.sendDom(swiftInfo, req); 
	        throw e;
	    } 
	}


	/**
	 * Sets the final status in the transaction result based on the provided transaction request.
	 * 
	 * @param trxRequest The transaction request object containing event and status information.
	 * @param aResult The result object to update with the final status.
	 */
	public void setFinalStatus(TrxRequest trxRequest, Result aResult) {
	    // Retrieve old and new event times, inbox status, and master status from the request
	    String oldEvent = trxRequest.getOldEventTime();
	    String newEvent = trxRequest.getNewEventTime();
	    String inboxStatus = trxRequest.getInboxStatus();
	    String masterStatus = trxRequest.getTrxStatus();
	    
	    // Create a new FinalStatus object and set its properties
	    FinalStatus finalStatus = new FinalStatus();
	    finalStatus.setOldEvent(oldEvent);
	    finalStatus.setNewEvent(newEvent);
	    finalStatus.setInboxStatus(inboxStatus);
	    finalStatus.setMasterStatus(masterStatus);
	    
	    // Update the final status in the result object if it's an instance of TrxResult
	    if (aResult instanceof TrxResult)
	        ((TrxResult)aResult).setFinalStatus(finalStatus); 
	}


	/**
	 * Determines if a double release is needed based on system parameters and group settings.
	 * 
	 * @param req The transaction request object containing context information.
	 * @return True if a double release is needed, false otherwise.
	 * @throws Exception If an error occurs during the determination process.
	 */
	public boolean needDoubleRelease(TrxRequest req) throws Exception {
	    try {
	        // Initialize the flag for double release
	        boolean needDoubleRelease = false;
	        
	        // Get the group setting for double release from the request context
	        String strGroupDouble = req.getContext().getCmpGroup().getValueByFld("IS_DOUBLE_REL");
	        
	        // Get the system parameter for double release from cache
	        String doubleRelease = CacheSYSHelper.getSysParam("DOUBLE_RELEASE");
	        
	        // Check if both conditions for double release are true
	        if ("T".equalsIgnoreCase(strGroupDouble) && "true".equalsIgnoreCase(doubleRelease))
	            needDoubleRelease = true; 

	        // Return the flag indicating whether double release is needed
	        return needDoubleRelease;
	    } catch (Exception e) {
	        // Re-throw the exception for handling at a higher level
	        throw e;
	    } 
	}


	/**
	 * Updates authorization and inbox tables based on transaction request information.
	 * 
	 * @param req The transaction request object containing update information.
	 * @return True if the update process is completed, false otherwise.
	 * @throws Exception If an error occurs during the update process.
	 */
	public boolean updateAuthAndInboxTab(TrxRequest req) throws Exception {
	    try {
	        // Initialize the flag for completion of update
	        boolean completeRe = false;
	        
	        // Generate SQL for authorization based on the request
	        SQLGenTool genSqlAuth = getAuthGenSql(req);
	        
	        // Check if the transaction is refused
	        if (req.isRefused()) {
	            // Set authorization status to 'S' (Rejected) and add SQL statement to request
	            genSqlAuth.addField("C_AUTH_STAT", "S", 1);
	            req.addSql(genSqlAuth.getSqlStatement());
	            return true; // Indicate completion of update
	        } 
	        
	        // Get the count of authorized users for the request
	        int iCount = getAuthorizedUserCount(req);
	        
	        // Process based on the count of authorized users
	        if (iCount == 0) {
	            // Append SQL statements for inbox, ledger, and master tables
	            appendInboxSql(req);
	            appendLedgerSql(req);
	            appendMasterSql(req);
	            // Set authorization status to 'T' (Authorized) and add SQL statement to request
	            genSqlAuth.addField("C_AUTH_STAT", "T", 12);
	            req.addSql(genSqlAuth.getSqlStatement());
	        } else if (iCount == 1) {
	            // Set authorization status to 'A' (Approved) and add SQL statement to request
	            genSqlAuth.addField("C_AUTH_STAT", "A", 12);
	            req.addSql(genSqlAuth.getSqlStatement());
	            completeRe = true; // Indicate completion of update
	        } 
	        
	        // Return flag indicating completion of update
	        return completeRe;
	    } catch (Exception e) {
	        // Re-throw the exception for handling at a higher level
	        throw e;
	    } 
	}

	/**
	 * Generates SQL for inserting authorization data into the specified table.
	 * 
	 * @param req The transaction request object containing data for authorization.
	 * @return The SQLGenTool object configured for inserting authorization data.
	 * @throws Exception If an error occurs during SQL generation.
	 */
	private SQLGenTool getAuthGenSql(TrxRequest req) throws Exception {
	    // Get the module name for the authorization table
	    String strAuthTableName = ContextHelper.getModuleName();
	    strAuthTableName = String.valueOf(strAuthTableName) + "_AUTH"; // Append "_AUTH" suffix
	    strAuthTableName = DSManager.getSchemaedTableName(this.TrxDS, strAuthTableName); // Get schemaed table name
	    
	    // Initialize SQLGenTool for insert operation
	    SQLGenTool genSql = SQLGenToolHelper.getInsertTool(this.TrxDBType);
	    genSql.setTable(strAuthTableName); // Set the table for SQL generation
	    
	    // Add fields for authorization data
	    genSql.addField("C_AUTH_USER", req.getUserID(), 12); // User ID for authorization
	    genSql.addField("C_MAIN_REF", req.getMainRef(), 12); // Main reference for authorization
	    genSql.addField("C_UNIT_CODE", req.getCompanyCode(), 12); // Company code for authorization
	    genSql.addField("D_AUTH_DATE", req.getSysDate("D_SYS_OP_DATE"), 91); // Authorization date
	    genSql.addField("I_EVENT_TIMES", req.getOldEventTime(), 4); // Old event time for authorization
	    genSql.addField("T_AUTH_TIME", req.getSysTime("T_SYS_OP_TIME"), 92); // Authorization time
	    
	    // Return the configured SQLGenTool object
	    return genSql;
	}


	/**
	 * Retrieves the count of authorized users for a specific transaction reference, company code, and event time.
	 * 
	 * @param req The transaction request object containing criteria for user authorization.
	 * @return The count of authorized users meeting the specified criteria.
	 * @throws Exception If an error occurs during the authorization user count retrieval process.
	 */
	private int getAuthorizedUserCount(TrxRequest req) throws Exception {
	    int intCount = 0; // Initialize the count of authorized users
	    
	    // Get the module name for the authorization table
	    String strAuthTableName = ContextHelper.getModuleName();
	    strAuthTableName = String.valueOf(strAuthTableName) + "_AUTH"; // Append "_AUTH" suffix
	    strAuthTableName = DSManager.getSchemaedTableName(this.TrxDS, strAuthTableName); // Get schemaed table name
	    
	    // Initialize SQLGenTool for query operation
	    SQLGenTool inqSql = SQLGenToolHelper.getQueryTool(this.TrxDBType);
	    inqSql.setTable(strAuthTableName); // Set the table for SQL query
	    inqSql.setFields("COUNT(*) AS RECOUNT"); // Set the fields to count authorized users
	    inqSql.appendClause("WHERE"); // Append WHERE clause
	    
	    // Specify clause fields for authorization criteria
	    String[] clauseFields = { "C_MAIN_REF", "C_UNIT_CODE", "I_EVENT_TIMES" };
	    TrxRequestHelper.setClause(inqSql, req, clauseFields); // Set clause for authorization criteria
	    
	    // Exclude the current user from the authorized user count
	    inqSql.appendClause("AND C_AUTH_USER<>'").appendClause(req.getUserID()).appendClause("'");
	    
	    // Get the SQL statement and execute the query
	    SQLStatement sql = inqSql.getSqlStatement();
	    SQLDaoResult result = ASDBHelper.executeQuery(sql, this.TrxDS);
	    
	    // Process the query result to retrieve the authorized user count
	    if (result.isSuccess()) {
	        String strCount = result.getFirstRecordSet().getFirstRecord().getValue("RECOUNT");
	        intCount = Integer.parseInt(strCount); // Parse the count value to integer
	    } 
	    
	    // Return the count of authorized users meeting the criteria
	    return intCount;
	}


	/**
	 * Appends SQL statements to update TRX_INBOX table with transaction status and releasing user information.
	 * 
	 * @param req The transaction request object containing data for updating TRX_INBOX table.
	 * @throws Exception If an error occurs during the SQL statement generation or execution.
	 */
	private void appendInboxSql(TrxRequest req) throws Exception {
	    // Initialize SQLGenTool for update operation
	    SQLGenTool inboxSql = SQLGenToolHelper.getUpdateTool(this.TrxDBType);
	    String strTable = DSManager.getSchemaedTableName(ContextHelper.getTrxDs(), "TRX_INBOX");
	    inboxSql.setTable(strTable); // Set the table for SQL update
	    inboxSql.addField("C_TRX_STATUS", "T", 12); // Add field to update transaction status
	    inboxSql.addField("C_RELE_BY", req.getUserID(), 12); // Add field to update releasing user
	    inboxSql.appendClause("WHERE"); // Append WHERE clause
	    
	    // Append clause conditions for updating TRX_INBOX table
	    inboxSql.appendClause("C_UNIT_CODE", 12, "=", req.getCompanyCode()).appendClause("AND");
	    inboxSql.appendClause("C_PRODUCT_ID", 12, "=", ContextHelper.getProductID()).appendClause("AND");
	    inboxSql.appendClause("C_MAIN_REF", 12, "=", req.getMainRef()).appendClause("AND");
	    inboxSql.appendClause("C_BK_GROUP_ID", 12, "=", req.getBankGroup());
	    
	    // Add the generated SQL statement to the transaction request
	    req.addSql(inboxSql.getSqlStatement());
	}

	/**
	 * Appends SQL statements to update the ledger table with transaction status.
	 * 
	 * @param req The transaction request object containing data for updating the ledger table.
	 * @throws Exception If an error occurs during the SQL statement generation or execution.
	 */
	private void appendLedgerSql(TrxRequest req) throws Exception {
	    // Initialize SQLGenTool for update operation
	    SQLGenTool ledgerSql = SQLGenToolHelper.getUpdateTool(this.TrxDBType);
	    ledgerSql.setTable(ContextHelper.getLedgerTable()); // Set the table for SQL update
	    ledgerSql.addField("C_TRX_STATUS", "T", 12); // Add field to update transaction status
	    ledgerSql.appendClause("WHERE"); // Append WHERE clause
	    
	    // Append clause conditions for updating the ledger table
	    ledgerSql.appendClause("C_UNIT_CODE", 12, "=", req.getCompanyCode()).appendClause("AND");
	    ledgerSql.appendClause("I_EVENT_TIMES", 4, "=", req.getOldEventTime()).appendClause("AND");
	    ledgerSql.appendClause("C_MAIN_REF", 12, "=", req.getMainRef());
	    
	    // Add the generated SQL statement to the transaction request
	    req.addSql(ledgerSql.getSqlStatement());
	}

	/**
	 * Appends SQL statements to update the master table with locked flag.
	 * 
	 * @param req The transaction request object containing data for updating the master table.
	 * @throws Exception If an error occurs during the SQL statement generation or execution.
	 */
	private void appendMasterSql(TrxRequest req) throws Exception {
	    // Initialize SQLGenTool for update operation
	    SQLGenTool masterSql = SQLGenToolHelper.getUpdateTool(this.TrxDBType);
	    masterSql.setTable(ContextHelper.getMasterTable()); // Set the table for SQL update
	    masterSql.addField("C_LOCKED_FLAG", "F", 12); // Add field to update locked flag
	    masterSql.appendClause("WHERE"); // Append WHERE clause
	    
	    // Append clause conditions for updating the master table
	    masterSql.appendClause("C_UNIT_CODE", 12, "=", req.getCompanyCode()).appendClause("AND");
	    masterSql.appendClause("I_EVENT_TIMES", 4, "=", req.getOldEventTime()).appendClause("AND");
	    masterSql.appendClause("C_MAIN_REF", 12, "=", req.getMainRef());
	    
	    // Add the generated SQL statement to the transaction request
	    req.addSql(masterSql.getSqlStatement());
	}


	/**
	 * Processes authorization-related data and updates transaction status and other information based on user actions.
	 * 
	 * @param aReq The request object containing authorization data.
	 * @param aRes The result object to store processing results.
	 * @return The processed result after authorization checks and updates.
	 * @throws Exception If an error occurs during authorization processing or database operations.
	 */
	public Result getDataAuthorizer(Request aReq, Result aRes) throws Exception {
	    TrxRequest req = TrxRequestHelper.getTrxRequest(aReq);
	    TrxResult res = TrxResultHelper.getTrxResult(aRes);
	    req.setAuthorizeFunction(true);
	    SimpleDO screen = req.getScreenData();
	    GapiInfo gapiInfo = null;
	    SwiftInfo swiftInfo = null;
	    try {
	        String trxType = screen.getFieldValue("TRX_TYPE");
	        this.logger.info("transaction type is :" + trxType);
	        // Process based on transaction type
	        if ("REASSIGNED".equals(trxType)) {
	            TrxReAssigned trxReAssigned = new TrxReAssigned();
	            aRes = trxReAssigned.reAssignData((Request) req, (Result) res);
	            SQLDao reAssSql = req.getDao();
	            SQLDaoResult sQLDaoResult = ASDBHelper.executeUpdate(reAssSql);
	            aRes.setResult(sQLDaoResult);
	            return aRes;
	        }
	        FuncAttr funcAttr = req.getPostFuncAttr();
	        RelAuthCheckProcess rcp = new RelAuthCheckProcess(req, res);
	        rcp.ceprocess(req);
	        res.setAuthResult(rcp.getAuthInfo());
	        setFinalStatus(req, (Result) res);
	        String sTrxStatus = req.getTrxStatus();
	        TrxRequest req4Img = TrxRequestHelper.getTrxRequest(aReq);
	        TrxManagerImage image = new TrxManagerImage();
	        image.getDataPending((Request) req4Img, (Result) res);
	        boolean isLastAuth = "A".equals(sTrxStatus);
	        this.logger.info("isLastAuthis :" + isLastAuth);
	        if (isLastAuth)
	            image.releaseData((Request) req4Img, (Result) res);
	        SQLDao imgDao = req4Img.getDao();
	        if (imgDao.getSqlSize() > 0) {
	            SQLDaoResult imgReslt = ASDBHelper.executeUpdate(imgDao);
	            if (!imgReslt.isSuccess()) {
	                this.logger.info("image result is :" + isLastAuth);
	                res.setSucess(false);
	                String errMsg = imgReslt.getErrorMsg();
	                res.setError(errMsg);
	                return (Result) res;
	            }
	            this.imgUpload2DB = true;
	            this.logger.info("image result is :" + this.imgUpload2DB);
	        }
	        // Process authorization for each functional component
	        for (int i = 0, length = funcAttr.getComponentSize(); i < length; i++) {
	            Component comp = funcAttr.getComponent(i);
	            String compName = comp.getName();
	            Busiintf obj = (Busiintf) Class.forName("com.cs.eximap.busiintf." + compName).newInstance();
	            obj.releaseData((Request) req, comp, (Result) res);
	            this.logger.info("F Atrr result is :" + res.toString());
	            if (!res.isSuccess())
	                return (Result) res;
	        }
	        // Determine inbox status based on transaction and external system responses
	        gapiInfo = res.getGapiReturn();
	        swiftInfo = res.getSwiftResult();
	        if (req.isRefused()) {
	            req.setInboxStatus("R");
	        } else {
	            if (gapiInfo != null && gapiInfo.isHasGapi())
	                req.setInboxStatus("S");
	            if (swiftInfo != null && swiftInfo.hasSendSwift())
	                req.setInboxStatus("S");
	        }
	        // Update ledger and execute SQL operations
	        TrxLedgerAddRec ledger = new TrxLedgerAddRec();
	        Result ledgerRs = new Result();
	        ledgerRs = ledger.releaseData((Request) req, ledgerRs);
	        if (!ledgerRs.isSuccess())
	            return ledgerRs;
	        if (!res.isSuccess())
	            return (Result) res;
	        String statusFromWeb = screen.getFieldValue("C_TRX_STATUS");
	        if (!StringUtil.isEmpty(statusFromWeb))
	            req.setTrxStatus(statusFromWeb);
	        CETrxInBoxManagerRecBean inboxRec = new CETrxInBoxManagerRecBean(req);
	        inboxRec.getReleaseUpdateData();
	        SQLDaoResult result = ASDBHelper.executeUpdate(req.getDao());
	        if (!result.isSuccess()) {
	            res.setSucess(false);
	            String errMsg = "";
	            WSException wsExp = new WSException("");
	            Exception exp = result.getException();
	            if (exp instanceof WSException) {
	                errMsg = result.getErrorMsg();
	            } else {
	                errMsg = exp.getMessage();
	            }
	            errMsg = ErrorHandling.msgConvert(errMsg);
	            wsExp.setMessage(errMsg);
	            res.setError((Exception) wsExp);
	            return (Result) res;
	        }
	        res.setResult(result);
	        return (Result) res;
	    } catch (Exception e) {
	        // Handle exceptions and send notifications if necessary
	        String strBankRef = screen.getFieldValue("BANK_REF_NO");
	        if (!StringUtil.isEmpty(strBankRef, true)) {
	            String strBankRefName = screen.getFieldValue("BANK_REF_NAME");
	            String strBankRefUnit = screen.getFieldValue("BANK_REF_UNIT");
	            SQLGenTool genUpdtSql = SQLGenToolHelper.getUpdateTool(this.TrxDBType);
	            String table = DSManager.getSchemaedTableName(this.TrxDS, "STD_TRX_REFUSING");
	            genUpdtSql.setTable(table);
	            genUpdtSql.addField("C_REF_STATUS", "R", 1);
	            genUpdtSql.appendClause("WHERE");
	            genUpdtSql.appendClause("C_UNIT_CODE", 1, "=", strBankRefUnit);
	            genUpdtSql.appendClause("AND");
	            genUpdtSql.appendClause("C_REF_NO", 1, "=", strBankRef);
	            genUpdtSql.appendClause("AND");
	            genUpdtSql.appendClause("C_REF_NAME", 1, "=", strBankRefName);
	            SQLStatement updtSql = genUpdtSql.getSqlStatement();
	            ASDBHelper.executeUpdate(updtSql, this.TrxDS);
	        }
	        if (gapiInfo != null)
	            GapiHelper.sendDom(gapiInfo, req);
	        if (swiftInfo != null)
	            SwiftHelper.sendDom(swiftInfo, req);
	        Result result = new Result();
	        result.setError(e);
	        result.setImgUpload2DB(this.imgUpload2DB);
	        return result;
	    }
	}

	/**
	 * Sets the user information in the session context based on the provided unit code and user ID.
	 * 
	 * @param sUnitCode The unit code of the user.
	 * @param sUserID   The user ID.
	 * @param cxt       The session context to set the user information.
	 */
	public void setSTPUserInfo(String sUnitCode, String sUserID, SessionContext cxt) {
	    // Check for null unit code or user ID
	    if (sUnitCode == null || sUserID == null) {
	        CELogSTPExec.info(this, "[ABU/abu_default.xml][stp.setSTPUserInfo()][error unit code|user id to pass]");
	        return;
	    }
	    String secuDS = DSManager.getSecuDS();
	    try {
	        // Retrieve company and user information from security database
	        String dbType = DSManager.getDBType(secuDS);
	        String unitTable = DSManager.getSchemaedTableName(secuDS, "SEC_BUSINESS_UNIT");
	        String userTable = DSManager.getSchemaedTableName(secuDS, "SEC_USER_INFO");
	        SQLGenTool sqlTool = new SQLGenTool(4, unitTable, dbType);
	        sqlTool.addField("*", null, 1);
	        sqlTool.appendClause("WHERE");
	        sqlTool.appendClause("C_UNIT_CODE", 1, "=", sUnitCode);
	        SQLStatement stmt = sqlTool.getSqlStatement();
	        SQLDao dao = new SQLDao();
	        dao.setDataSource(secuDS);
	        dao.addSqlStatement(stmt);
	        sqlTool = new SQLGenTool(4, userTable, dbType);
	        sqlTool.addField("*", null, 1);
	        sqlTool.appendClause("WHERE");
	        sqlTool.appendClause("C_UNIT_CODE", 1, "=", sUnitCode);
	        sqlTool.appendClause("AND");
	        sqlTool.appendClause("C_USER_ID", 1, "=", sUserID);
	        stmt = sqlTool.getSqlStatement();
	        dao.addSqlStatement(stmt);
	        SQLDaoResult result = ASDBHelper.executeQuery(dao);
	        if (result.isSuccess()) {
	            SQLRecordSet setUnit = result.getRecordSet(0);
	            if (setUnit.isSuccess()) {
	                SQLRecord recordUnit = setUnit.getFirstRecord();
	                if (recordUnit == null)
	                    CELogSTPExec.error(this, "CEStpJs:setSTPUserInfo:\nrecordUnit is null!");
	                CompanyGroup grp = dealUnitInfo(recordUnit);
	                cxt.setCmpGroup(grp);
	            } else {
	                Exception e = setUnit.getException();
	                CELogSTPExec.error(this, e);
	                throw e;
	            }
	            SQLRecordSet setUser = result.getRecordSet(1);
	            if (setUser.isSuccess()) {
	                SQLRecord recordUser = setUser.getFirstRecord();
	                if (recordUser == null)
	                    CELogSTPExec.error(this, "CEStpJs:setSTPUserInfo:\nrecordUser is null!");
	                UserInfo usr = dealUserInfo(recordUser);
	                cxt.setUsrInfo(usr);
	            } else {
	                Exception e = setUser.getException();
	                CELogSTPExec.error(this, e);
	                throw e;
	            }
	        } else {
	            Exception e = result.getException();
	            CELogSTPExec.error(this, e);
	            throw e;
	        }
	    } catch (Exception e) {
	        CELogSTPExec.error(this, e);
	        return;
	    }
	}

	/**
	 * Processes the unit information and converts it into a CompanyGroup object.
	 * 
	 * @param aRecordUnit The record containing unit information.
	 * @return The CompanyGroup object representing the unit information.
	 * @throws Exception If an error occurs during the conversion.
	 */
	private CompanyGroup dealUnitInfo(SQLRecord aRecordUnit) throws Exception {
	    CompanyGroup company = RecordConverter.getCompanyGroup((IReadMap) aRecordUnit);
	    return company;
	}

	/**
	 * Processes the user information and converts it into a UserInfo object.
	 * 
	 * @param aRecordUser The record containing user information.
	 * @return The UserInfo object representing the user information.
	 * @throws Exception If an error occurs during the conversion.
	 */
	private UserInfo dealUserInfo(SQLRecord aRecordUser) throws Exception {
	    UserInfo userInfo = RecordConverter.getUserInfo((IReadMap) aRecordUser);
	    return userInfo;
	}

	/**
	 * Retrieves and sets company information based on the provided company ID.
	 * 
	 * @param strId The company ID to retrieve information for.
	 * @return The Company object representing the company information.
	 * @throws Exception If an error occurs during database operations or data retrieval.
	 */
	public Company setCmpId(String strId) throws Exception {
	    Company company = null;
	    try {
	        String secuDS = DSManager.getSecuDS();
	        String dbType = DSManager.getDBType(secuDS);
	        String unitTable = DSManager.getSchemaedTableName(secuDS, "SEC_BUSINESS_UNIT");
	        SQLGenTool sqlTool = new SQLGenTool(4, unitTable, dbType);
	        sqlTool.addField("*", null, 1);
	        sqlTool.appendClause("WHERE");
	        sqlTool.appendClause("C_UNIT_CODE", 1, "=", strId);
	        SQLStatement stmt = sqlTool.getSqlStatement();
	        SQLDaoResult result = ASDBHelper.executeQuery(stmt, secuDS);
	        if (result.isSuccess()) {
	            SQLRecordSet recordSet = result.getFirstRecordSet();
	            if (recordSet.isSuccess()) {
	                SQLRecord record = recordSet.getFirstRecord();
	                if (record == null)
	                    CELogSTPExec.error(this, "CEStpJs:setCmpId:\nrecord is null!");
	                company = RecordConverter.getCompany((IReadMap) record);
	            } else {
	                Exception e = recordSet.getException();
	                CELogSTPExec.error(this, e);
	                throw e;
	            }
	        } else {
	            Exception e = result.getException();
	            CELogSTPExec.error(this, e);
	            throw e;
	        }
	    } catch (Exception e) {
	        CELogSTPExec.error(this, e);
	        throw e;
	    }
	    return company;
	}
}
