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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.csme.csmeapi.api.NotificationApi;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XNotificationRequest;
import com.csme.csmeapi.fin.models.XNotificationResponse;
import com.csme.csmeapi.fin.services.MobileAppCommonDao;
import com.csme.csmeapi.fin.services.NotificationServiceApi;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-02-11T17:09:05.951+04:00[Asia/Muscat]")
/**
 * Controller class for handling notification API requests.
 * Implements the NotificationApi interface.
 */
@Controller
public class NotificationApiController implements NotificationApi {

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
	 * Constructor for NotificationApiController.
	 * @param objectMapper The object mapper for JSON serialization.
	 * @param request The HTTP servlet request.
	 */
	@org.springframework.beans.factory.annotation.Autowired
	public NotificationApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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
	 * Endpoint for sending notifications.
	 * @param body The notification request body.
	 * @param requestId The unique request ID.
	 * @param channel The channel name.
	 * @param timeStamp The time in Unix format.
	 * @param version The API version (optional).
	 * @return A ResponseEntity containing the notification response.
	 */
	public ResponseEntity<XNotificationResponse> sendNotification(@ApiParam(value = "Notification Request" ,required=true )  @Valid @RequestBody XNotificationRequest body
			,@ApiParam(value = "Request Id" ,required=true) @RequestHeader(value="requestId", required=true) UUID requestId
			,@ApiParam(value = "Channel Name" ,required=true, defaultValue="FinMobileBankingApp") @RequestHeader(value="channel", required=true) String channel
			,@ApiParam(value = "Time date Unix Format" ,required=true) @RequestHeader(value="timeStamp", required=true) Integer timeStamp
			,@ApiParam(value = "Version of API" , defaultValue="1.0.0") @RequestHeader(value="version", required=false) String version
			) {

    	logger.info("Fintuil is :"+ finUtil);
    	
		String accept = this.request.getHeader("Accept");
		MobileAppCommonDao<?> commonDao = null;
		Long sequence = null;
		XNotificationResponse notificationResponse = new XNotificationResponse();
		NotificationServiceApi notificationServiceApi = new NotificationServiceApi();
		if (accept != null)
			try {
				commonDao = new MobileAppCommonDao<>();
				if (commonDao.requestAlreadyExists(requestId.toString())) {
					this.logger.info("Request Id is aleady exist");
					commonDao.updateAuditRecord(requestId.toString(), "Exception has occured", "Exception has occured", HttpStatus.BAD_REQUEST);
					return new ResponseEntity<XNotificationResponse>(notificationResponse, HttpStatus.BAD_REQUEST);
				} 
				sequence = commonDao.saveAuditRecord(body.toString(), requestId.toString(), "NotificationApi");
				this.logger.info("Saving record into Audit table and sequence no is :" + sequence);
				if (sequence != null && requestId != null)
					notificationResponse = notificationServiceApi.getNotification(body, requestId, sequence);
				this.logger.error("Final Response messsage is :" + notificationResponse);
				String jsonStr = FinUtil.getJsonString(notificationResponse);
				if (jsonStr != null)
					notificationResponse = (XNotificationResponse)this.objectMapper.readValue(jsonStr, XNotificationResponse.class); 

				commonDao.updateAuditRecord(sequence, jsonStr, null, HttpStatus.OK + "");
				return new ResponseEntity<XNotificationResponse>(notificationResponse, HttpStatus.OK);
			} catch (Exception e) {
				this.logger.error("Couldn't serialize response for content type application/json" + e);
				commonDao.updateAuditRecord(sequence, notificationResponse.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR + "");
				return new ResponseEntity<XNotificationResponse>(notificationResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}  
		return new ResponseEntity<XNotificationResponse>(notificationResponse, HttpStatus.NOT_IMPLEMENTED);
	}

}
