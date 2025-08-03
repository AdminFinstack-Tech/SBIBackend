/**
 * The InquireEventApiController class serves as the controller for handling inquire event API requests.
 * It implements the InquireEventApi interface and provides methods for inquiring event history transactions.
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.csme.csmeapi.api.InquireEventApi;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XInquireEventRequest;
import com.csme.csmeapi.fin.models.XInquireEventResponse;
import com.csme.csmeapi.fin.services.EventHistoryApiService;
import com.csme.csmeapi.fin.services.MobileAppCommonDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;

/**
 * The InquireEventApiController class serves as the controller for handling inquire event API requests.
 * It implements the InquireEventApi interface and provides methods for querying event history.
 */
@Controller
public class InquireEventApiController implements InquireEventApi {

	FinUtil finUtil = new FinUtil();
	
	/**
	 * The logger instance for logging messages related to InquireEventApiController operations.
	 */
	private Logger logger = LogManager.getLogger("CSMEMobile");

	/**
	 * The object mapper for JSON serialization and deserialization.
	 */
	private final ObjectMapper objectMapper;

	/**
	 * The HTTP servlet request.
	 */
	private final HttpServletRequest request;

	/**
	 * Constructs a new InquireEventApiController instance.
	 *
	 * @param objectMapper The object mapper for JSON serialization and deserialization.
	 * @param request      The HTTP servlet request.
	 */
	@org.springframework.beans.factory.annotation.Autowired
	public InquireEventApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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
     * Handles the inquire event API request and returns the event history transactions.
     *
     * @param body       The request body containing inquire event information.
     * @param requestId  The unique identifier for the request.
     * @param channel    The channel through which the request is made.
     * @param timeStamp  The timestamp of the request.
     * @param version    The version of the API.
     * @return A ResponseEntity containing the inquire event response as an XInquireEventResponse object.
     */
	public ResponseEntity<XInquireEventResponse> inquireEvent(
	        @ApiParam(value = "Inquire Event History of Transaction", required = true) @Valid @RequestBody XInquireEventRequest body,
	        @ApiParam(value = "Request Id", required = true) @RequestHeader(value = "requestId", required = true) UUID requestId,
	        @ApiParam(value = "Channel Name", required = true, defaultValue = "FinMobileBankingApp") @RequestHeader(value = "channel", required = true) String channel,
	        @ApiParam(value = "Time date Unix Format", required = true) @RequestHeader(value = "timeStamp", required = true) Integer timeStamp,
	        @ApiParam(value = "Version of API", defaultValue = "1.0.0") @RequestHeader(value = "version", required = false) String version) {

    	logger.info("Fintuil is :"+ finUtil);
    	
	    // Get the 'Accept' header value from the request
	    String accept = this.request.getHeader("Accept");
	    MobileAppCommonDao<XInquireEventResponse> commonDao = null;
	    Long sequence = null;
	    XInquireEventResponse xInquireEventResponse = new XInquireEventResponse();
	    EventHistoryApiService eventHistoryApiService = new EventHistoryApiService();
	    
	    // Check if 'Accept' header is not null
	    if (accept != null) {
	        try {
	            // Initialize MobileAppCommonDao for database operations
	            commonDao = new MobileAppCommonDao<>();
	            
	            // Check if the request ID already exists
	            if (commonDao.requestAlreadyExists(requestId.toString())) {
	                this.logger.info("Request Id already exists");
	                commonDao.updateAuditRecord(requestId.toString(), "Exception has occurred", "Exception has occurred", HttpStatus.BAD_REQUEST);
	                return new ResponseEntity<XInquireEventResponse>(xInquireEventResponse, HttpStatus.BAD_REQUEST);
	            } 
	            
	            // Save audit record and get event inquiry list
	            sequence = commonDao.saveAuditRecord(body.toString(), requestId.toString(), "InquireEventApi");
	            this.logger.info("Saving record into Audit table and sequence no is: " + sequence);
	            
	            if (sequence != null && requestId != null)
	                xInquireEventResponse = eventHistoryApiService.getEventInquiryList(body, requestId, sequence);
	            
	            // Convert the response to JSON string for audit logging
	            String jsonStr = FinUtil.getJsonString(xInquireEventResponse);
	            this.logger.info("Converting POJO class to JSON string: " + jsonStr);
	            
	            // Convert the JSON string back to an object
	            if (jsonStr != null)
	                xInquireEventResponse = (XInquireEventResponse) this.objectMapper.readValue(jsonStr, XInquireEventResponse.class); 

	            // Log the final response message
	            this.logger.info("Final response message: " + xInquireEventResponse.toString());
	            
	            // Update the audit record with the final JSON response
	            commonDao.updateAuditRecord(sequence, jsonStr, null, HttpStatus.OK.toString());
	            
	            // Return a response entity with the search results and OK status
	            return new ResponseEntity<XInquireEventResponse>(xInquireEventResponse, HttpStatus.OK);
	        } catch (Exception e) {
	            // Handle exceptions and log error messages
	            this.logger.error("Error processing event inquiry request: " + e.getMessage());
	            
	            // Update the audit record with the error response and internal server error status
	            commonDao.updateAuditRecord(sequence, xInquireEventResponse.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR.toString());
	            
	            // Return a response entity with the error message and internal server error status
	            return new ResponseEntity<XInquireEventResponse>(xInquireEventResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	        }  
	    }

	    // Return a response entity with the 'Not Implemented' status if 'Accept' header is null
	    return new ResponseEntity<XInquireEventResponse>(xInquireEventResponse, HttpStatus.NOT_IMPLEMENTED);
	}

}
