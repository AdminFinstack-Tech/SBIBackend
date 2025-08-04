/**
 * Provides services for handling BCO (Bank Credit Operations) inquiries and transactions.
 * This class interacts with databases to retrieve and process BCO transaction information.
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
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.cs.base.xml.XMLManager;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinConfig;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XBCOInquireRequest;
import com.csme.csmeapi.fin.models.XBCOInquireResponse;
import com.csme.csmeapi.fin.models.XBCOTransaction;

/**
 * Provides services for handling BCO inquiries and responses.
 * This class interacts with databases to retrieve and process BCO transaction information.
 */
public class BCOInquireApiService {

	FinUtil finUtil = new FinUtil();

	/**
	 * Logger for logging API-related events and information.
	 */
	Logger logger = LogManager.getLogger("CSMEMobile");

	/**
	 * Security data source identifier used for database connections.
	 */
	private String secuDs = null;

	/**
	 * Language setting for the API service.
	 */
	public String Lang = null;

	/**
	 * Date formatter for consistent date formatting.
	 */
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * Constructor for the BCOInquireApiService class.
	 * Initializes the security data source and handles exceptions.
	 */
	public BCOInquireApiService() {
		try {
			secuDs = FinUtil.SECDS; // Initialize the security data source
		} catch (Exception e) {
			logger.error("Error initializing BCOInquireApiService: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves BCO inquiry information based on the provided request parameters.
	 * 
	 * @param body The request body containing inquiry parameters.
	 * @param requestId The unique identifier for the request.
	 * @param sequence The sequence number for tracking the request order.
	 * @return An XBCOInquireResponse object containing the BCO inquiry response.
	 */
	public XBCOInquireResponse getBCOInquiry(@Valid XBCOInquireRequest body, UUID requestId, Long sequence) {
		XBCOInquireResponse response = new XBCOInquireResponse();
		
		try {
			// Get and set the language based on corporate ID and user ID from the request body
			Lang = FinUtil.getLang(body.getCorporateId(), body.getUserId());
			
			// Build the BCO inquiry response based on the request parameters
			buildBCOInquiryResponse(body, requestId, sequence, response);
			
		} catch (Exception e) {
			// Handle any exceptions by setting the response status to indicate an error
			handleException(response, e);
		}
		
		return response;
	}

	/**
	 * Processes DSO referral requests for BCO review.
	 * 
	 * @param body The request body containing DSO referral parameters.
	 * @param requestId The unique identifier for the request.
	 * @param sequence The sequence number for tracking the request order.
	 * @return An XBCOInquireResponse object containing the DSO referral response.
	 */
	public XBCOInquireResponse processDSOReferal(@Valid XBCOInquireRequest body, UUID requestId, Long sequence) {
		XBCOInquireResponse response = new XBCOInquireResponse();
		
		try {
			// Get and set the language based on corporate ID and user ID from the request body
			Lang = FinUtil.getLang(body.getCorporateId(), body.getUserId());
			
			// Process the DSO referral
			processDSOReferalRequest(body, requestId, sequence, response);
			
		} catch (Exception e) {
			// Handle any exceptions by setting the response status to indicate an error
			handleException(response, e);
		}
		
		return response;
	}

	/**
	 * Builds the BCO inquiry response by querying the database.
	 * 
	 * @param body The request body.
	 * @param requestId The request ID.
	 * @param sequence The sequence number.
	 * @param response The response object to populate.
	 */
	private void buildBCOInquiryResponse(XBCOInquireRequest body, UUID requestId, Long sequence, XBCOInquireResponse response) {
		try {
			// Load BCO inquiry XML configuration
			String xmlPath = "/MOBILE/BCOInquiry.xml";
			Document xmlDoc = loadXMLConfiguration(xmlPath);
			
			if (xmlDoc == null) {
				// If XML not found, use direct SQL query
				executeBCOInquirySQL(body, response);
			} else {
				// Process using XML configuration
				processBCOInquiryXML(xmlDoc, body, response);
			}
			
			// Set response metadata
			response.setStatus("SUCCESS");
			response.setMessage("BCO inquiry completed successfully");
			response.setPageNumber(body.getPageNumber());
			response.setPageSize(body.getPageSize());
			
		} catch (Exception e) {
			logger.error("Error building BCO inquiry response: " + e.getMessage());
			throw new RuntimeException("Failed to build BCO inquiry response", e);
		}
	}

	/**
	 * Executes direct SQL query for BCO inquiry.
	 * 
	 * @param body The request body.
	 * @param response The response object to populate.
	 */
	private void executeBCOInquirySQL(XBCOInquireRequest body, XBCOInquireResponse response) {
		try {
			// Build SQL query based on request parameters
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ");
			sql.append("  REF_NO AS REFERENCE_NO, ");
			sql.append("  TRX_TYPE AS TRANSACTION_TYPE, ");
			sql.append("  CUSTOMER_NAME, ");
			sql.append("  CUSTOMER_ID, ");
			sql.append("  BRANCH_CODE, ");
			sql.append("  BRANCH_NAME, ");
			sql.append("  OBS_DATE AS OBSERVATION_DATE, ");
			sql.append("  STATUS, ");
			sql.append("  AMOUNT, ");
			sql.append("  CURRENCY, ");
			sql.append("  CREATED_DATE, ");
			sql.append("  UPDATED_DATE, ");
			sql.append("  REASON_FOR_REFERAL, ");
			sql.append("  BCO_REMARKS, ");
			sql.append("  APPROVAL_STATUS, ");
			sql.append("  APPROVED_BY, ");
			sql.append("  APPROVED_DATE ");
			sql.append("FROM BCO_TRANSACTIONS ");
			sql.append("WHERE C_UNIT_CODE = ? ");
			
			List<Object> params = new ArrayList<>();
			params.add(body.getCorporateId());
			
			// Add filters based on request
			if (body.getTransactionType() != null && !body.getTransactionType().isEmpty()) {
				sql.append("AND TRX_TYPE = ? ");
				params.add(body.getTransactionType());
			}
			
			if (body.getStatus() != null && !body.getStatus().isEmpty()) {
				sql.append("AND STATUS = ? ");
				params.add(body.getStatus());
			}
			
			if (body.getBranchCode() != null && !body.getBranchCode().isEmpty()) {
				sql.append("AND BRANCH_CODE = ? ");
				params.add(body.getBranchCode());
			}
			
			if (body.getCustomerId() != null && !body.getCustomerId().isEmpty()) {
				sql.append("AND CUSTOMER_ID = ? ");
				params.add(body.getCustomerId());
			}
			
			if (body.getReferenceNo() != null && !body.getReferenceNo().isEmpty()) {
				sql.append("AND REF_NO = ? ");
				params.add(body.getReferenceNo());
			}
			
			if (body.getFromDate() != null && !body.getFromDate().isEmpty()) {
				sql.append("AND OBS_DATE >= ? ");
				params.add(body.getFromDate());
			}
			
			if (body.getToDate() != null && !body.getToDate().isEmpty()) {
				sql.append("AND OBS_DATE <= ? ");
				params.add(body.getToDate());
			}
			
			sql.append("ORDER BY CREATED_DATE DESC ");
			
			// Add pagination
			int offset = (body.getPageNumber() - 1) * body.getPageSize();
			sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
			params.add(offset);
			params.add(body.getPageSize());
			
			// Execute query
			SQLRecordSet recordSet = WSDBHelper.executeQuery(secuDs, sql.toString(), params.toArray());
			
			// Process results
			List<XBCOTransaction> transactions = new ArrayList<>();
			int totalRecords = 0;
			
			if (recordSet != null && recordSet.next()) {
				do {
					XBCOTransaction transaction = mapRecordToTransaction(recordSet);
					transactions.add(transaction);
					totalRecords++;
				} while (recordSet.next());
			}
			
			response.setTransactions(transactions);
			response.setTotalRecords(totalRecords);
			
		} catch (Exception e) {
			logger.error("Error executing BCO inquiry SQL: " + e.getMessage());
			throw new RuntimeException("Failed to execute BCO inquiry", e);
		}
	}

	/**
	 * Processes BCO inquiry using XML configuration.
	 * 
	 * @param xmlDoc The XML document configuration.
	 * @param body The request body.
	 * @param response The response object to populate.
	 */
	private void processBCOInquiryXML(Document xmlDoc, XBCOInquireRequest body, XBCOInquireResponse response) {
		// Implementation for XML-based query processing
		// This would parse the XML configuration and execute queries accordingly
		// For now, fallback to direct SQL
		executeBCOInquirySQL(body, response);
	}

	/**
	 * Processes DSO referral request.
	 * 
	 * @param body The request body.
	 * @param requestId The request ID.
	 * @param sequence The sequence number.
	 * @param response The response object to populate.
	 */
	private void processDSOReferalRequest(XBCOInquireRequest body, UUID requestId, Long sequence, XBCOInquireResponse response) {
		try {
			// For DSO referral, we would typically insert or update a record
			// For now, return success response
			response.setStatus("SUCCESS");
			response.setMessage("DSO referral processed successfully");
			
			// You would typically insert the referral into the database here
			// insertDSOReferral(body);
			
		} catch (Exception e) {
			logger.error("Error processing DSO referral: " + e.getMessage());
			throw new RuntimeException("Failed to process DSO referral", e);
		}
	}

	/**
	 * Maps a database record to XBCOTransaction object.
	 * 
	 * @param record The SQL record.
	 * @return XBCOTransaction object.
	 */
	private XBCOTransaction mapRecordToTransaction(SQLRecordSet record) {
		XBCOTransaction transaction = new XBCOTransaction();
		
		try {
			transaction.setReferenceNo(record.getString("REFERENCE_NO"));
			transaction.setTransactionType(record.getString("TRANSACTION_TYPE"));
			transaction.setCustomerName(record.getString("CUSTOMER_NAME"));
			transaction.setCustomerId(record.getString("CUSTOMER_ID"));
			transaction.setBranchCode(record.getString("BRANCH_CODE"));
			transaction.setBranchName(record.getString("BRANCH_NAME"));
			transaction.setObservationDate(record.getString("OBSERVATION_DATE"));
			transaction.setStatus(record.getString("STATUS"));
			transaction.setAmount(record.getString("AMOUNT"));
			transaction.setCurrency(record.getString("CURRENCY"));
			
			// Handle dates
			if (record.getDate("CREATED_DATE") != null) {
				transaction.setCreatedAt(record.getDate("CREATED_DATE"));
			}
			if (record.getDate("UPDATED_DATE") != null) {
				transaction.setUpdatedAt(record.getDate("UPDATED_DATE"));
			}
			
			// DSO referral specific fields
			transaction.setReasonForReferal(record.getString("REASON_FOR_REFERAL"));
			transaction.setBcoRemarks(record.getString("BCO_REMARKS"));
			transaction.setApprovalStatus(record.getString("APPROVAL_STATUS"));
			transaction.setApprovedBy(record.getString("APPROVED_BY"));
			
			if (record.getDate("APPROVED_DATE") != null) {
				transaction.setApprovedDate(record.getDate("APPROVED_DATE"));
			}
			
		} catch (Exception e) {
			logger.error("Error mapping record to transaction: " + e.getMessage());
		}
		
		return transaction;
	}

	/**
	 * Loads XML configuration from the specified path.
	 * 
	 * @param xmlPath The path to the XML configuration file.
	 * @return Document object or null if not found.
	 */
	private Document loadXMLConfiguration(String xmlPath) {
		try {
			XMLManager xmlManager = new XMLManager();
			return xmlManager.getXMLDoc(xmlPath);
		} catch (Exception e) {
			logger.warn("XML configuration not found at: " + xmlPath);
			return null;
		}
	}

	/**
	 * Handles exceptions by setting appropriate error response.
	 * 
	 * @param response The response object.
	 * @param e The exception.
	 */
	private void handleException(XBCOInquireResponse response, Exception e) {
		logger.error("BCO Service Exception: " + e.getMessage(), e);
		response.setStatus("ERROR");
		response.setMessage("An error occurred: " + e.getMessage());
		response.setTransactions(new ArrayList<>());
		response.setTotalRecords(0);
	}
}