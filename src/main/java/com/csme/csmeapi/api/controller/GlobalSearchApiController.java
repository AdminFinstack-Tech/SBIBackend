/**
 * The GlobalSearchApiController class serves as the controller for handling global search API requests.
 * It implements the GlobalsearchApi interface and provides methods for conducting global search inquiries.
 */
package com.csme.csmeapi.api.controller;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.csme.csmeapi.api.GlobalsearchApi;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XGlobalSearchRequest;
import com.csme.csmeapi.fin.models.XInquiryListResponse;
import com.csme.csmeapi.fin.services.GlobalSearchApiService;
import com.csme.csmeapi.fin.services.MobileAppCommonDao;
import com.fasterxml.jackson.databind.ObjectMapper;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-01-30T20:25:55.040+04:00[Asia/Muscat]")
@Controller
public class GlobalSearchApiController implements GlobalsearchApi {

	FinUtil finUtil = new FinUtil();
	
	/**
	 * Logger instance for logging messages related to the "CSMEMobile" module.
	 */
	private Logger logger = LogManager.getLogger("CSMEMobile");

	/**
	 * Object mapper for JSON serialization and deserialization.
	 */
	private final ObjectMapper objectMapper;

	/**
	 * HTTP servlet request instance for handling HTTP requests.
	 */
	private final HttpServletRequest request;

	/**
	 * Constructs a new GlobalSearchApiController instance.
	 *
	 * @param objectMapper The object mapper for JSON serialization and deserialization.
	 * @param request      The HTTP servlet request.
	 */
	@org.springframework.beans.factory.annotation.Autowired
	public GlobalSearchApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}
	/**
	 * Retrieves the object mapper used for JSON serialization and deserialization.
	 *
	 * @return An optional containing the object mapper if available, otherwise an empty optional.
	 */
	@Override
	public Optional < ObjectMapper > getObjectMapper() {
		return Optional.ofNullable(objectMapper);
	}

	/**
	 * Retrieves the HTTP servlet request.
	 *
	 * @return An optional containing the HTTP servlet request if available, otherwise an empty optional.
	 */
	@Override
	public Optional < HttpServletRequest > getRequest() {
		return Optional.ofNullable(request);
	}

	/**
	 * Handles the global search API request and returns the search results.
	 *
	 * @param body     The search request body containing the search criteria.
	 * @param requestId The unique identifier for the request.
	 * @param channel   The channel through which the request is made.
	 * @param timeStamp The timestamp of the request.
	 * @param version   The version of the API.
	 * @return A ResponseEntity containing the search results as an XInquiryListResponse object.
	 */
	public ResponseEntity < XInquiryListResponse > globalsearch(@Valid XGlobalSearchRequest body, UUID requestId,
			String channel, Integer timeStamp, String version) {

    	logger.info("Fintuil is :"+ finUtil);
    	
		// Initialize necessary variables
		MobileAppCommonDao <GlobalSearchApiController> commonDao = null;
		Long sequence = null;
		XInquiryListResponse xInquiryListResponse = new XInquiryListResponse();
		GlobalSearchApiService globalSearchApiService = new GlobalSearchApiService();
		try {
			// Initialize commonDao to interact with the database
			commonDao = new MobileAppCommonDao <GlobalSearchApiController> ();

			// Check if the request already exists
			if (commonDao.requestAlreadyExists(requestId.toString())) {
				// Log and return a bad request response if the request exists
				logger.info("Request Id already exists");
				commonDao.updateAuditRecord(requestId.toString(), "Request Id already exists", "Request Id already exists", HttpStatus.BAD_REQUEST);
				return ResponseEntity.badRequest().body(xInquiryListResponse);
			}

			// Save the audit record for the new request
			sequence = commonDao.saveAuditRecord(body.toString(), requestId.toString(), "GlobalSearchApi");
			logger.info("Saving record into Audit table and sequence no is: " + sequence);

			// Process the search request
			if (sequence != null && requestId != null) {
				xInquiryListResponse = globalSearchApiService.getInquiryInfo(body, requestId, sequence);
			}

			// Convert the response to JSON string for audit logging
			String jsonStr = FinUtil.getJsonString(xInquiryListResponse);
			logger.info("Converting POJO class to JSON string: " + jsonStr);

			// Convert the JSON string back to an object
			if (jsonStr != null) {
				xInquiryListResponse = objectMapper.readValue(jsonStr, XInquiryListResponse.class);
			}

			// Log the final response message
			logger.info("Final response message: " + xInquiryListResponse.toString());

			// Update the audit record with the final JSON response
			commonDao.updateAuditRecord(sequence, xInquiryListResponse.toString(), null, HttpStatus.OK.toString());

			// Return a response entity with the search results and OK status
			return ResponseEntity.ok(xInquiryListResponse);
		} catch (Exception e) {
			// Handle exceptions and log error messages
			logger.error("Error processing global search request: " + e.getMessage());
			// Update the audit record with the error response and internal server error status
			commonDao.updateAuditRecord(sequence, xInquiryListResponse.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR.toString());

			// Return a response entity with the error message and internal server error status
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(xInquiryListResponse);
		}
	}

}