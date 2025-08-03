/**
 * The InquireListApiController class serves as the controller for handling inquiries related to a list of items.
 * It implements the InquireListApi interface and provides methods for retrieving information based on inquiry lists.
 */
package com.csme.csmeapi.api.controller;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.csme.csmeapi.api.InquireListApi;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XInquiryListRequest;
import com.csme.csmeapi.fin.models.XInquiryListResponse;
import com.csme.csmeapi.fin.services.InquiryListApiService;
import com.csme.csmeapi.fin.services.MobileAppCommonDao;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The InquireListApiController class serves as the controller for handling inquiries related to a list of items.
 * It implements the InquireListApi interface and provides methods for retrieving information based on inquiry lists.
 */
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-01-30T20:25:55.040+04:00[Asia/Muscat]")
@Controller
public class InquireListApiController implements InquireListApi {
	
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
	 * Constructs a new InquireListApiController instance.
	 *
	 * @param objectMapper The object mapper for JSON serialization and deserialization.
	 * @param request      The HTTP servlet request.
	 */
	@org.springframework.beans.factory.annotation.Autowired
	public InquireListApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	/**
	 * Retrieves the object mapper used for JSON serialization and deserialization.
	 *
	 * @return An optional containing the object mapper if available, otherwise an empty optional.
	 */
	@Override
	public Optional<ObjectMapper> getObjectMapper() {
		return Optional.ofNullable(objectMapper);
	}

	/**
	 * Retrieves the HTTP servlet request.
	 *
	 * @return An optional containing the HTTP servlet request if available, otherwise an empty optional.
	 */
	@Override
	public Optional<HttpServletRequest> getRequest() {
		return Optional.ofNullable(request);
	}

	/**
	 * Handles the inquiry list API request and returns the inquiry results.
	 *
	 * @param body     The inquiry list request body containing the list of items to inquire about.
	 * @param requestId The unique identifier for the request.
	 * @param channel   The channel through which the request is made.
	 * @param timeStamp The timestamp of the request.
	 * @param version   The version of the API.
	 * @return A ResponseEntity containing the inquiry results as an XInquiryListResponse object.
	 */
	public ResponseEntity<XInquiryListResponse> inquireList(@Valid XInquiryListRequest body, UUID requestId,
			String channel, Integer timeStamp, String version) {

    	logger.info("Fintuil is :"+ finUtil);
    	
		// Initialize variables
		MobileAppCommonDao<InquireListApiController> commonDao = null;
		Long sequence = null;
		XInquiryListResponse xInquiryListResponse = new XInquiryListResponse();
		InquiryListApiService xInquiryApiService = new InquiryListApiService();

		try {
			// Initialize commonDao for database operations
			commonDao = new MobileAppCommonDao<InquireListApiController>();

			// Check if the request ID already exists
			if (commonDao.requestAlreadyExists(requestId.toString())) {
				this.logger.info("Request Id already exists");
				commonDao.updateAuditRecord(requestId.toString(), "Exception has occurred", "Exception has occurred", HttpStatus.BAD_REQUEST);
				return new ResponseEntity<XInquiryListResponse>(xInquiryListResponse, HttpStatus.BAD_REQUEST);
			}

			// Save audit record and get inquiry info
			sequence = commonDao.saveAuditRecord(body.toString(), requestId.toString(), "InquireListApi");
			this.logger.info("Saving record into Audit table and sequence no is: " + sequence);

			if (sequence != null && requestId != null)
				xInquiryListResponse = xInquiryApiService.getInquiryInfo(body, requestId, sequence);

			// Convert response to JSON string for logging
			String jsonStr = FinUtil.getJsonString(xInquiryListResponse);
			this.logger.info("Converting POJO class to JSON string: " + jsonStr);

			// Convert JSON string back to object
			if (jsonStr != null)
				xInquiryListResponse = (XInquiryListResponse) this.objectMapper.readValue(jsonStr, XInquiryListResponse.class);

			// Log final response message
			this.logger.info("Final response message is: " + xInquiryListResponse.toString());

			// Update audit record with final JSON response
			commonDao.updateAuditRecord(sequence, xInquiryListResponse.toString(), null, HttpStatus.OK + "");

			// Return response entity with OK status and inquiry list response
			return new ResponseEntity<XInquiryListResponse>(xInquiryListResponse, HttpStatus.OK);
		} catch (Exception e) {
			// Handle exceptions and log error messages
			this.logger.error("Error processing inquiry list request: " + e.getMessage());

			// Update audit record with error response and internal server error status
			commonDao.updateAuditRecord(sequence, xInquiryListResponse.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR + "");

			// Return response entity with internal server error status and error response
			return new ResponseEntity<XInquiryListResponse>(xInquiryListResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
