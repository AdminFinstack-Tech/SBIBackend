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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.csme.csmeapi.api.InquireAttachmentApi;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XAttachmentRequest;
import com.csme.csmeapi.fin.models.XAttachmentResponse;
import com.csme.csmeapi.fin.services.InquireAttachmentService;
import com.csme.csmeapi.fin.services.MobileAppCommonDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T10:01:59.297+04:00[Asia/Muscat]")
@Controller
public class InquireAttachmentApiController implements InquireAttachmentApi {

	@Autowired
	private FinUtil finUtil;

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
	 * Constructor for InquireTransactionApiController.
	 * @param objectMapper The object mapper for JSON serialization.
	 * @param request The HTTP servlet request.
	 */
	@org.springframework.beans.factory.annotation.Autowired
	public InquireAttachmentApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	/**
	 * Get the object mapper used for JSON serialization.
	 * @return An optional containing the object mapper.
	 */
	@Override
	public Optional<ObjectMapper> getObjectMapper() {
		return Optional.ofNullable(objectMapper);
	}

	/**
	 * Get the HTTP servlet request.
	 * @return An optional containing the HTTP servlet request.
	 */
	@Override
	public Optional<HttpServletRequest> getRequest() {
		return Optional.ofNullable(request);
	}

	/**
	 * Endpoint for inquiring about a transaction.
	 * @param body The request body containing transaction details.
	 * @param requestId The unique request ID.
	 * @param channel The channel name.
	 * @param timeStamp The time in Unix format.
	 * @param version The API version (optional).
	 * @return A ResponseEntity containing the inquiry response.
	 */
	public ResponseEntity<XAttachmentResponse> inquireAttachment(@ApiParam(value = "Inquire Attachment Request" ,required=true )  @Valid @RequestBody XAttachmentRequest body
			,@ApiParam(value = "Request Id" ,required=true) @RequestHeader(value="requestId", required=true) UUID requestId
			,@ApiParam(value = "Channel Name" ,required=true, defaultValue="FinMobileBankingApp") @RequestHeader(value="channel", required=true) String channel
			,@ApiParam(value = "Time date Unix Format" ,required=true) @RequestHeader(value="timeStamp", required=true) Integer timeStamp
			,@ApiParam(value = "Version of API" , defaultValue="1.0.0") @RequestHeader(value="version", required=false) String version
			) {
		logger.info("Fintuil is :"+ finUtil);

		// Get the Accept header from the request
		String accept = request.getHeader("Accept");

		// Initialize variables
		MobileAppCommonDao<XAttachmentResponse> commonDao = null;
		Long sequence = null;
		XAttachmentResponse xAttachmentResponse = new XAttachmentResponse();
		InquireAttachmentService inquireAttachmentService = new InquireAttachmentService();

		// Check if the Accept header is not null
		if (accept != null) {
			try {
				// Initialize commonDao for database operations
				commonDao = new MobileAppCommonDao<>();

				// Check if the request ID already exists
				if (commonDao.requestAlreadyExists(requestId.toString())) {
					this.logger.info("Request Id already exists");
					commonDao.updateAuditRecord(requestId.toString(), "Exception has occurred", "Exception has occurred", HttpStatus.BAD_REQUEST);
					return new ResponseEntity<XAttachmentResponse>(xAttachmentResponse, HttpStatus.BAD_REQUEST);
				} 

				// Save audit record and get  inquiry Attachement
				sequence = commonDao.saveAuditRecord(body.toString(), requestId.toString(), "InquireAttachmentApi");
				this.logger.info("Saving record into Audit table and sequence no is: " + sequence);

				if (sequence != null && requestId != null)
					xAttachmentResponse = inquireAttachmentService.getInquiryAttachment(body, requestId, sequence);

				// Convert response to JSON string for logging
				String jsonStr = FinUtil.getJsonString(xAttachmentResponse);
				this.logger.info("Converting POJO class to JSON string: " + jsonStr);

				// Log final response message
				this.logger.info("Final response message is: " + xAttachmentResponse.toString());

				// Update audit record with final JSON response
				commonDao.updateAuditRecord(sequence, jsonStr, null, HttpStatus.OK + "");

				// Return response entity with OK status and transaction inquiry response
				return new ResponseEntity<XAttachmentResponse>(xAttachmentResponse, HttpStatus.OK);
			} catch (Exception e) {
				// Handle exceptions and log error messages
				this.logger.error("Error processing transaction inquiry request: " + e.getMessage());

				// Update audit record with error response and internal server error status
				commonDao.updateAuditRecord(sequence, xAttachmentResponse.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR + "");

				// Return response entity with internal server error status and error response
				return new ResponseEntity<XAttachmentResponse>(xAttachmentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}  
		}

		// Return response entity with not implemented status if Accept header is null
		return new ResponseEntity<XAttachmentResponse>(xAttachmentResponse, HttpStatus.NOT_IMPLEMENTED);
	}

}
