package com.csme.csmeapi.fin.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.text.StringSubstitutor;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.cs.base.xml.XMLManager;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.cache.CacheSYSHelper;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.exec.SQLGenTool;
import com.cs.core.dao.exec.SQLGenToolHelper;
import com.cs.core.dao.exec.SQLRecordSet;
import com.cs.core.xml.object.mlg.LanguageList;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.ProfileRequest;
import com.csme.csmeapi.fin.models.ProfileResponse;
import com.csme.csmeapi.fin.models.UserAuthLevel;
import com.csme.csmeapi.fin.models.XCompanyInformation;
import com.csme.csmeapi.fin.models.XProfileBodyResponse;
import com.csme.csmeapi.fin.models.XUserInformation;
import com.csme.csmeapi.fin.models.XUserInformation.ActiveStatusEnum;
import com.csme.csmeapi.fin.models.XUserInformation.MultiLoginEnum;
import com.csme.csmeapi.fin.models.XUserInformationProfilepic;
import com.csme.csmeapi.fin.models.XproductInfoList;
import com.csme.csmeapi.fin.models.XproductInfoListNofTransaction;
import com.csme.csmeapi.fin.util.CSMEMobLogUtil;

public class ProfileApiServices {
	
	FinUtil finUtil = new FinUtil();
	/**
	 * The security data source used for database operations.
	 */
	private String secuDs = null;
	/**
	 * The database type used for database operations.
	 */
	private String dbType = null;
	/**
	 * Logger instance for logging messages.
	 */
	private final CSMEMobLogUtil logger = new CSMEMobLogUtil();
	 /**
     * Constructs a ProfileApiServices instance with default security datasource, database type, and current timestamp.
     */
	
    public ProfileApiServices() {
        try {
            secuDs = FinUtil.SECDS; // Default security datasource
            dbType = FinUtil.SECDBTYPE; // Default database type
        } catch (Exception e) {
            // Print stack trace if an exception occurs during initialization
            e.printStackTrace();
        } 
    }

    /**
     * Retrieves profile information based on the provided request.
     *
     * @param body The profile request body.
     * @param requestId The unique request ID.
     * @param sequence The sequence number for the message.
     * @return The profile response containing the requested information.
     */
    public ProfileResponse getProfileInfo(@Valid ProfileRequest body, UUID requestId, Long sequence) {
        ProfileResponse resp = new ProfileResponse();
        resp.setMessageId(sequence.toString());
        resp.setRequestId(requestId.toString());
        String setTimeStamp = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).format(new Date());
        resp.setTimestamp(setTimeStamp);
        try {
            // Retrieve company information
            XCompanyInformation companyInformationItem = getCompanyById(body.getCorporateId(), body.getUserId());
            // Populate profile response
            List<XProfileBodyResponse> profileBodyResponse = new ArrayList<>();
            XProfileBodyResponse xProfileBodyResponse = new XProfileBodyResponse();
            xProfileBodyResponse.addCompanyInformationItem(companyInformationItem);

            // Retrieve user information
            XUserInformation userInformationItem = getUserById(body.getUserId(), body.getCorporateId());
            xProfileBodyResponse.addUserInformationItem(userInformationItem);
            profileBodyResponse.add(xProfileBodyResponse);
            resp.setProfileBodyResponse(profileBodyResponse);

            // Set success status
            resp.setStatusCode("00");
            resp.setStatusDescription("Success");
        } catch (Exception e) {
            // Handle exceptions and set error status
            resp.setStatusCode("99");
            resp.setStatusDescription("General Exception");
            this.logger.error("Exception occurred in get Profile info function: " + e);
        }
        return resp;
    }


    /**
     * Retrieves company information based on the provided company ID and user ID.
     *
     * @param companyId The ID of the company to retrieve information for.
     * @param userId The ID of the user requesting the information.
     * @return An XCompanyInformation object containing the company details.
     * @throws Exception if an error occurs during the retrieval process.
     */
    @SuppressWarnings("unchecked")
	public XCompanyInformation getCompanyById(String companyId, String userId) throws Exception {
        // Create a new XCompanyInformation object
        XCompanyInformation xCompanyInformation = new XCompanyInformation();
        try {
            // Get the secure data source and database type
            String secuDs = DSManager.getSecuDS();
            String dbType = DSManager.getDBType(secuDs);
            
            // Log information about fetching company details
            this.logger.info("Getting the Company Details: " + companyId);
            
            // Get the schemaed table name and create SQL query tool
            String tableWithSchema = DSManager.getSchemaedTableName(secuDs, "SEC_BUSINESS_UNIT ");
            SQLGenTool queryTool = SQLGenToolHelper.getQueryTool(dbType);
            queryTool.setTable(tableWithSchema);
            
            // Add fields to the query and set conditions
            queryTool.addQueryField("C_UNIT_CODE");
            queryTool.addQueryField("C_UNIT_NAME");
            queryTool.addQueryField("C_CNTY_CODE");
            queryTool.addQueryField("C_IS_BANK");
            queryTool.addQueryField("BASE_CCY");
            queryTool.addQueryField("AMT_FORMAT");
            queryTool.addQueryField("C_UNIT_NAME_AB");
            queryTool.appendClause("WHERE");
            queryTool.appendClause("C_UNIT_CODE", 12, companyId);
            
            // Log the SQL query statement
            this.logger.info("Query to get the Company Details: " + queryTool.getSqlStatement());
            
            // Execute the query and get the result set
            SQLRecordSet rs = WSDBHelper.executeQuery(queryTool.getSqlStatement(), secuDs);
            
            // Process the result set if successful
            if (rs.isSuccess() && rs.getRecordCount() > 0) {
                // Set company information from the result set
                xCompanyInformation.setCorporateId(rs.getFirstRecord().getValue("C_UNIT_CODE"));
                xCompanyInformation.setCountry(rs.getFirstRecord().getValue("C_CNTY_CODE"));
                xCompanyInformation.setCorporateName(rs.getFirstRecord().getValue("C_UNIT_NAME"));
                xCompanyInformation.setIsBank(XCompanyInformation.IsBankEnum.fromValue(rs.getFirstRecord().getValue("C_IS_BANK")));
                xCompanyInformation.setBaseCCY(rs.getFirstRecord().getValue("BASE_CCY"));
                xCompanyInformation.setAmtFormat(XCompanyInformation.AmtFormatEnum.fromValue(rs.getFirstRecord().getValue("AMT_FORMAT")));
                xCompanyInformation.setCorporateArabicName(rs.getFirstRecord().getValue("C_UNIT_NAME_AB"));
            } 
            
            // Get company assigned modules and process them
            String myString = FinUtil.getCompanyAssignedModules(companyId, userId);
            String[] myArray = myString.split(",");
            List<XproductInfoList> productsAccessList = new ArrayList<XproductInfoList>();
            Document iconDescDoc = FinUtil.getIconDescriptionDoc();
            for (String module : myArray) {
                // Create XproductInfoList objects and set module details
                XproductInfoList xProductInfoList = new XproductInfoList();
                xProductInfoList.setModule(module);
                String icon = FinUtil.getIconLink(iconDescDoc, module);
                xProductInfoList.setIcon(icon);
                String desc = FinUtil.getModuleDescription(iconDescDoc, module);
                xProductInfoList.setDescription(desc);
                JSONObject style = FinUtil.getModuleStyle(iconDescDoc, module);
                Map<String, Object> styleMap = new HashMap<>();
                // Iterate over keys and put them into the map
                if (style != null && style.length() > 0) {
                    Iterator<String> keys = style.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        Object value = style.get(key);
                        styleMap.put(key, value);
                    }
                    xProductInfoList.setStyle(styleMap);
                }
                // Set transaction counts for the module
                XproductInfoListNofTransaction nofTransaction = new XproductInfoListNofTransaction();
                nofTransaction.setPendingCount(Integer.toString(getPendingCountTrx(companyId, userId, module, "P")));
                nofTransaction.setTotalCount(Integer.toString(getCountTransaction(companyId, module, "M")));
                xProductInfoList.setNofTransaction(nofTransaction);
                productsAccessList.add(xProductInfoList);
            }
            xCompanyInformation.setProductsAccessList(productsAccessList);
            return xCompanyInformation;
        } catch (Exception e) {
            // Log error and throw an exception
            this.logger.error("Error while Getting the Company Details");
            FinUtil.getErrorStackTrace(e);
            throw new Exception(e);
        } 
    }

    /**
     * Checks if a company exists in the database.
     *
     * @param companyId The ID of the company to check for existence.
     * @return true if the company exists, false otherwise.
     * @throws Exception if an error occurs during database interaction.
     */
    public boolean companyExists(String companyId) throws Exception {
        boolean exists = false;
        try {
            // Get the secure data source and database type
            String secuDs = DSManager.getSecuDS();
            String dbType = DSManager.getDBType(secuDs);

            // Construct the SQL query
            String tableWithSchema = DSManager.getSchemaedTableName(secuDs, "SEC_BUSINESS_UNIT ");
            SQLGenTool queryTool = SQLGenToolHelper.getQueryTool(dbType);
            queryTool.setTable(tableWithSchema);
            queryTool.setFields("C_UNIT_CODE");
            queryTool.appendClause("WHERE");
            queryTool.appendClause("C_UNIT_CODE", 12, companyId);

            // Execute the SQL query
            SQLRecordSet rs = WSDBHelper.executeQuery(queryTool.getSqlStatement(), secuDs);
            if (rs.isSuccess() && rs.getRecordCount() > 0) {
                exists = true;
            }
            return exists;
        } catch (Exception e) {
            // Log and rethrow the exception
            this.logger.error("Error while checking company existence.");
            FinUtil.getErrorStackTrace(e);
            throw new Exception(e);
        }
    }
    
    /**
     * Retrieves user information by user ID and unit code.
     *
     * @param userId   The ID of the user to retrieve information for.
     * @param unitCode The unit code associated with the user.
     * @return The user's information.
     * @throws Exception if an error occurs during database interaction.
     */
	public XUserInformation getUserById(String userId, String unitCode) throws Exception {
		XUserInformation XUserInformation = new XUserInformation();
		try {
			 // Log user details retrieval
			this.logger.info("Getting the User Details " + userId);
			// Construct SQL query for user information
			String tableWithSchema = DSManager.getSchemaedTableName(this.secuDs, "SEC_USER_INFO ");
			SQLGenTool queryTool = SQLGenToolHelper.getQueryTool(this.dbType);
			queryTool.setTable(tableWithSchema);
			queryTool.addQueryField("C_USER_ID");
			queryTool.addQueryField("C_USER_DESC");
			queryTool.addQueryField("C_UNIT_CODE");
			queryTool.addQueryField("C_DFLT_UNIT_CODE");
			queryTool.addQueryField("I_AUTH_LEVEL");
			queryTool.addQueryField("EFFECTIVE_DATE");
			queryTool.addQueryField("EXPIRY_DATE");
			queryTool.addQueryField("C_INACTIVE");
			queryTool.addQueryField("I_MULTI_LOGIN");
			queryTool.addQueryField("C_LANG_NAME");
			queryTool.addQueryField("EMAIL_ID");
			queryTool.addQueryField("MOBILE");
			queryTool.addQueryField("TOKEN_ID");
			queryTool.addQueryField("PROFILE_PIC");
			queryTool.addQueryField("MAX_CRITERIA");
			queryTool.appendClause("WHERE");
			queryTool.appendClause("C_USER_ID", 12, userId);
			queryTool.appendClause("AND");
			queryTool.appendClause("C_UNIT_CODE", 12, unitCode);
		    this.logger.info("Query to get the User Details " + queryTool.getSqlStatement());
	        // Execute the SQL query
			SQLRecordSet rs2 = WSDBHelper.executeQuery(queryTool.getSqlStatement(), this.secuDs);
			if (rs2.isSuccess() && rs2.getRecordCount() > 0) {
				// Populate XUserInformation object with retrieved data
				XUserInformation.setUserId(rs2.getFirstRecord().getValue("C_USER_ID"));
				XUserInformation.setCompanyId(rs2.getFirstRecord().getValue("C_UNIT_CODE"));
				XUserInformation.setDefaultCompanyId(rs2.getFirstRecord().getValue("C_DFLT_UNIT_CODE"));
				XUserInformation.setAuthorizatoinLevel(new BigDecimal(rs2.getFirstRecord().getValue("I_AUTH_LEVEL")));
				XUserInformation.setEffectiveDate(rs2.getFirstRecord().getValue("EFFECTIVE_DATE"));
				XUserInformation.setExpiryDate(rs2.getFirstRecord().getValue("EXPIRY_DATE"));
				if (rs2.getFirstRecord().getValue("C_INACTIVE").equalsIgnoreCase("F")) {
					XUserInformation.setActiveStatus(ActiveStatusEnum.fromValue("T"));
				} else {
					XUserInformation.setActiveStatus(ActiveStatusEnum.fromValue("F"));
				} 
				if (rs2.getFirstRecord().getValue("I_MULTI_LOGIN") != null && rs2.getFirstRecord().getValue("I_MULTI_LOGIN").equalsIgnoreCase("1")) {
					XUserInformation.setMultiLogin(MultiLoginEnum.fromValue("T"));
				} else {
					XUserInformation.setMultiLogin(MultiLoginEnum.fromValue("F"));
				} 
				XUserInformation.setLanguage(rs2.getFirstRecord().getValue("C_LANG_NAME"));
				LanguageList langList = CacheSYSHelper.getSystemLanguages();
				String availablelanguage = getAllLanguagesSeparatedBySemicolon(langList);
				logger.info("availablelanguage is :"+availablelanguage);
				XUserInformation.setAvailablelanguage(availablelanguage);
				XUserInformation.setEmailId(rs2.getFirstRecord().getValue("EMAIL_ID"));
				XUserInformation.setMobileNumber(rs2.getFirstRecord().getValue("MOBILE"));
				XUserInformation.setUserDescription(rs2.getFirstRecord().getValue("C_USER_DESC"));
				XUserInformation.setTokenId(rs2.getFirstRecord().getValue("TOKEN_ID"));
				XUserInformationProfilepic profilepic = new XUserInformationProfilepic();
				profilepic.setImage(rs2.getFirstRecord().getValue("PROFILE_PIC"));
				XUserInformation.setMaxCriteriaOption(rs2.getFirstRecord().getValue("MAX_CRITERIA"));
				XUserInformation.setProfilepic(profilepic);
			} 
			String tableWithSchema1 = DSManager.getSchemaedTableName(this.secuDs, "SEC_USER_AUTH_LEVEL ");
			SQLGenTool queryTool1 = SQLGenToolHelper.getQueryTool(this.dbType);
			queryTool1.setTable(tableWithSchema1);
			queryTool1.addQueryField("C_PRODUCT_ID");
			queryTool1.addQueryField("I_AUTH_LEVEL");
			queryTool1.appendClause("WHERE");
			queryTool1.appendClause("C_USER_ID", 12, userId);
			queryTool1.appendClause("AND");
			queryTool1.appendClause("C_UNIT_CODE", 12, unitCode);
			this.logger.info("Query to get the User Auth Level Details " + queryTool1.getSqlStatement());
			SQLRecordSet authLevelRecordsSet = WSDBHelper.executeQuery(queryTool1.getSqlStatement(), this.secuDs);
			List<UserAuthLevel> authLevelList = new ArrayList<>();
			HashMap<String, String> productModuleMap = FinUtil.getProductModuleMap(
					FinUtil.getConstant("DEFAULT_C_BK_GROUP_ID"), FinUtil.getConstant("DEFAULT_C_CNTY_CODE"));
			if (authLevelRecordsSet.isSuccess() && authLevelRecordsSet.getRecordCount() > 0)
				for (int i = 0; i < authLevelRecordsSet.getRecordCount(); i++) {
					UserAuthLevel userAuthLevel = new UserAuthLevel();
					String productId = authLevelRecordsSet.getRecord(i).getValue("C_PRODUCT_ID");
					userAuthLevel.setProductName(productModuleMap.get(productId));
					userAuthLevel.setAuthLevel(authLevelRecordsSet.getRecord(i).getValue("I_AUTH_LEVEL"));
					authLevelList.add(userAuthLevel);
				}  
			XUserInformation.setProductAuthLevel(authLevelList);
			String tableWithSchema2 = DSManager.getSchemaedTableName(this.secuDs, "SEC_USER_ROLE ");
			SQLGenTool queryTool2 = SQLGenToolHelper.getQueryTool(this.dbType);
			queryTool2.setTable(tableWithSchema2);
			queryTool2.addQueryField("C_ROLE_ID");
			queryTool2.appendClause("WHERE");
			queryTool2.appendClause("C_USER_ID", 12, userId);
			queryTool2.appendClause("AND");
			queryTool2.appendClause("C_GRP_CODE", 12, unitCode);
			this.logger.info("Query to get the User Role Details " + queryTool2.getSqlStatement());
			SQLRecordSet rolesResultSet = WSDBHelper.executeQuery(queryTool2.getSqlStatement(), this.secuDs);
			List<String> rolesList = new ArrayList<>();
			if (rolesResultSet.isSuccess() && rolesResultSet.getRecordCount() > 0)
				for (int i = 0; i < rolesResultSet.getRecordCount(); i++)
					rolesList.add(rolesResultSet.getRecord(i).getValue("C_ROLE_ID"));  
			XUserInformation.setOperatorRoles(rolesList);
			return XUserInformation;
		} catch (Exception e) {
			 // Log and rethrow exception
	        this.logger.error("Error while Getting the User Details");
	        FinUtil.getErrorStackTrace(e);
	        throw new Exception(e);
		} 
	}
	
	/**
	 * Generates a string containing all languages separated by semicolons from the given LanguageList.
	 *
	 * @param languageList The LanguageList containing the languages to concatenate.
	 * @return A string containing all languages separated by semicolons.
	 */
	public static String getAllLanguagesSeparatedBySemicolon(LanguageList languageList) {
	    StringBuilder stringBuilder = new StringBuilder();
	    for (int i = 0; i < languageList.getLanguageCount(); i++) {
	        // Append a semicolon if not the first language
	        if (i > 0) {
	            stringBuilder.append("; ");
	        }
	        // Append the language name
	        stringBuilder.append(languageList.getLanguage(i).getName());
	    }
	    return stringBuilder.toString();
	}
	/**
	 * Retrieves the count of transactions based on the unit code, module, and status.
	 * 
	 * @param unitCode The unit code for which transactions are counted.
	 * @param module The module name to filter transactions.
	 * @param status The status of transactions to consider.
	 * @return The count of transactions matching the criteria.
	 */
	private int getCountTransaction(String unitCode, String module, String status) {
	    int count = 0;
	    try {
	        StringBuilder iboxQueryBuilder = new StringBuilder();
	        
	        // Build the file path for the XML configuration file
	        String filePath = FinUtil.intPath + "CommonINQ.xml";
	        // Load the XML document
	        Document commonXmlDoc = XMLManager.xmlFileToDom(filePath);
	        // Log the XML document as a string for debugging
	        logger.info("Convert Inquiry Dom into String is: " + XMLManager.convertToString(commonXmlDoc));
	        
	        // Get the SQL query for the specified module from the XML document
	        String moduleQuery = getModuleQuery(module, commonXmlDoc);
	        
	        // Substitute values into the query template
	        Map<String, String> valuesMap = new HashMap<>();
	        valuesMap.put("unitcode", unitCode);
	        valuesMap.put("modulename", module);
	        StringSubstitutor substitutor = new StringSubstitutor(valuesMap);
	        String result = substitutor.replace(moduleQuery);
	        iboxQueryBuilder.append(result);
	        
	        // Log the constructed SQL query
	        logger.info("Execute SqlQuery: " + iboxQueryBuilder);
	        
	        // Execute the SQL query and get the record set
	        SQLRecordSet rs = WSDBHelper.executeQuery(iboxQueryBuilder.toString(), secuDs);
	        // Get the count of records in the result set
	        count = rs.getRecordCount();
	        
	        // Log the count of transactions
	        logger.info("Count transaction is " + count);
	    } catch (Exception e) {
	        // Log any exceptions that occur during the process
	        logger.error("Exception occurred in getCountTransaction function: " + e);
	    }
	    return count;
	}
	
	/**
	 * Retrieves the count of pending transactions based on the unit code, module, and status.
	 * 
	 * @param unitCode The unit code for which pending transactions are counted.
	 * @param module The module name to filter transactions.
	 * @param status The status of transactions to consider (P for pending, T for processed, S for saved).
	 * @return The count of pending transactions matching the criteria.
	 */
	private int getPendingCountTrx(String unitCode, String userID, String module, String status) {
	    int count = 0;
	    try {
	        // Construct the SQL query based on input parameters
	        StringBuilder sqlQuery = new StringBuilder();
	        sqlQuery.append("SELECT C_TRX_STATUS FROM CETRX.TRX_INBOX WHERE C_UNIT_CODE = '")
	                .append(unitCode).append("' AND C_MODULE = '").append(module).append("'");
	        // Add condition based on the status parameter if it's provided and valid
	        if (status != null && (status.equalsIgnoreCase("P") || status.equalsIgnoreCase("T") || status.equalsIgnoreCase("S"))) {
	            sqlQuery.append(" AND C_TRX_STATUS IN ('P', 'T') AND "
	            		+ "C_TRX_ID NOT IN (SELECT C_TRX_ID FROM CETRX.TRX_AUTH_LIST WHERE  "
	            		+ "C_GRP_CODE = '"+unitCode+"' "
	            		+ " AND  C_USER_ID = '"+userID+"'  "
	            		+ " AND  C_UNIT_CODE = '"+unitCode+"' ) "
	            		+ "AND C_TRX_ID IN(SELECT C_TRX_ID FROM CETRX.TRX_MATRIX_LIST "
	            		+ "WHERE  C_UNIT_CODE = '"+unitCode+"'  "
	            		+ " ) ORDER BY CUST_NO ");
	        }
	        
	        // Log the constructed SQL query
	        logger.info("Execute SqlQuery: " + sqlQuery);
	        
	        // Execute the SQL query and get the record set
	        SQLRecordSet rs = WSDBHelper.executeQuery(sqlQuery.toString(), secuDs);
	        // Get the count of records in the result set
	        count = rs.getRecordCount();
	        
	        // Log the count of transactions
	        logger.info("Count transaction is " + count);
	    } catch (Exception e) {
	        // Log any exceptions that occur during the process
	        this.logger.error("Exception occurred in getPendingCountTrx function: " + e);
	    }
	    return count;
	}

	
	/**
	 * Retrieves the SQL query for a specified module from an XML document.
	 * 
	 * @param moduleName The name of the module for which the SQL query is required.
	 * @param inquiryXml The XML document containing module-specific queries.
	 * @return The SQL query for the specified module, or null if not found.
	 */
	private static String getModuleQuery(String moduleName, Document inquiryXml) {
	    // Get the root element of the XML document
	    Element rootElement = inquiryXml.getDocumentElement();
	    
	    // Get the nodes corresponding to the module name
	    NodeList moduleNodes = rootElement.getElementsByTagName(moduleName);
	    
	    // Check if nodes for the module are found
	    if (moduleNodes.getLength() > 0) {
	        // Extract the first module element
	        Element moduleElement = (Element) moduleNodes.item(0);
	        
	        // Build the SQL query from the module element's text content
	        StringBuilder queryBuilder = new StringBuilder();
	        queryBuilder.append(moduleElement.getTextContent().trim());
	        return queryBuilder.toString();
	    }
	    
	    // Return null if module not found in the XML document
	    return null;
	}
}
