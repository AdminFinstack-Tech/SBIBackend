/**
 * The BCOInquireApiController class serves as the controller for handling BCO (Bank Credit Operations) inquiries.
 * It implements the BCOInquireApi interface and provides methods for retrieving BCO transaction information.
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
import org.springframework.web.bind.annotation.CrossOrigin;

import com.csme.csmeapi.api.BCOInquireApi;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XBCOInquireRequest;
import com.csme.csmeapi.fin.models.XBCOInquireResponse;
import com.csme.csmeapi.fin.services.BCOInquireApiService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The BCOInquireApiController class serves as the controller for handling BCO inquiries.
 * It implements the BCOInquireApi interface and provides methods for retrieving BCO transaction information.
 */
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class BCOInquireApiController implements BCOInquireApi {
	
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
	 * Constructs a new BCOInquireApiController instance.
	 *
	 * @param objectMapper The object mapper for JSON serialization and deserialization.
	 * @param request      The HTTP servlet request.
	 */
	@org.springframework.beans.factory.annotation.Autowired
	public BCOInquireApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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
	 * Handles BCO inquiry requests.
	 *
	 * @param body      The BCO inquiry request body.
	 * @param requestId The unique request identifier.
	 * @param sequence  The request sequence number.
	 * @return ResponseEntity containing the BCO inquiry response.
	 */
	@Override
	public ResponseEntity<XBCOInquireResponse> bcoInquire(@Valid XBCOInquireRequest body, UUID requestId, Long sequence) {
		logger.info("BCO Inquiry Request received - Corporate ID: " + body.getCorporateId() + ", User ID: " + body.getUserId());
		
		try {
			// Log request details
			logger.debug("BCO Inquiry Request - Transaction Type: " + body.getTransactionType() + 
					   ", Status: " + body.getStatus() + 
					   ", Reference No: " + body.getReferenceNo());
			
			// Create service instance and process request
			BCOInquireApiService bcoService = new BCOInquireApiService();
			XBCOInquireResponse response = bcoService.getBCOInquiry(body, requestId, sequence);
			
			// Log response status
			logger.info("BCO Inquiry Response - Status: " + response.getStatus() + 
					   ", Total Records: " + response.getTotalRecords());
			
			return new ResponseEntity<XBCOInquireResponse>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			logger.error("Error processing BCO inquiry request: " + e.getMessage(), e);
			
			// Create error response
			XBCOInquireResponse errorResponse = new XBCOInquireResponse();
			errorResponse.setStatus("ERROR");
			errorResponse.setMessage("Failed to process BCO inquiry: " + e.getMessage());
			
			return new ResponseEntity<XBCOInquireResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Handles BCO DSO referral requests.
	 *
	 * @param body      The DSO referral request body.
	 * @param requestId The unique request identifier.
	 * @param sequence  The request sequence number.
	 * @return ResponseEntity containing the DSO referral response.
	 */
	@Override
	public ResponseEntity<XBCOInquireResponse> bcoDSOReferal(@Valid XBCOInquireRequest body, UUID requestId, Long sequence) {
		logger.info("BCO DSO Referral Request received - Corporate ID: " + body.getCorporateId() + ", User ID: " + body.getUserId());
		
		try {
			// Log request details
			logger.debug("DSO Referral Request - Customer ID: " + body.getCustomerId() + 
					   ", Branch Code: " + body.getBranchCode() + 
					   ", Reference No: " + body.getReferenceNo());
			
			// Set transaction type to DSO_REFERAL
			body.setTransactionType("DSO_REFERAL");
			
			// Create service instance and process request
			BCOInquireApiService bcoService = new BCOInquireApiService();
			XBCOInquireResponse response = bcoService.processDSOReferal(body, requestId, sequence);
			
			// Log response status
			logger.info("DSO Referral Response - Status: " + response.getStatus() + 
					   ", Message: " + response.getMessage());
			
			return new ResponseEntity<XBCOInquireResponse>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			logger.error("Error processing DSO referral request: " + e.getMessage(), e);
			
			// Create error response
			XBCOInquireResponse errorResponse = new XBCOInquireResponse();
			errorResponse.setStatus("ERROR");
			errorResponse.setMessage("Failed to process DSO referral: " + e.getMessage());
			
			return new ResponseEntity<XBCOInquireResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}