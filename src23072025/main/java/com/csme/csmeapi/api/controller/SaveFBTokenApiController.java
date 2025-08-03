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

import com.csme.csmeapi.api.SaveFBTokenApi;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.SaveFBTokenRequestPost;
import com.csme.csmeapi.fin.models.SaveFBTokenResponsePost;
import com.csme.csmeapi.fin.services.MobileAppCommonDao;
import com.csme.csmeapi.fin.services.SaveFBTokenApiService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;

@Controller
public class SaveFBTokenApiController implements SaveFBTokenApi {

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
	 * Constructor to initialize the controller with necessary dependencies.
	 * 
	 * @param objectMapper Object mapper for JSON operations.
	 * @param request       HTTP servlet request instance.
	 */
	@org.springframework.beans.factory.annotation.Autowired
	public SaveFBTokenApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	/**
	 * Retrieves an optional object mapper instance.
	 * 
	 * @return Optional<ObjectMapper>
	 */
	@Override
	public Optional<ObjectMapper> getObjectMapper() {
		return Optional.ofNullable(objectMapper);
	}

	/**
	 * Retrieves an optional HTTP servlet request instance.
	 * 
	 * @return Optional<HttpServletRequest>
	 */
	@Override
	public Optional<HttpServletRequest> getRequest() {
		return Optional.ofNullable(request);
	}

	/**
	 * Handles the save FB token API endpoint.
	 * 
	 * @param body    Save FB token request body.
	 * @param requestId Request ID header.
	 * @param channel Channel header.
	 * @param timeStamp Time stamp header.
	 * @param version Version header.
	 * @return ResponseEntity<SaveFBTokenResponsePost>
	 */
	public ResponseEntity<SaveFBTokenResponsePost> saveFBToken(
			@ApiParam(value = "FB Token Request", required = true) @Valid @RequestBody SaveFBTokenRequestPost body,
			@ApiParam(value = "Request Id", required = true) @RequestHeader(value = "requestId", required = true) UUID requestId,
			@ApiParam(value = "Channel Name", required = true, defaultValue = "FinMobileBankingApp") @RequestHeader(value = "channel", required = true) String channel,
			@ApiParam(value = "Time date Unix Format", required = true) @RequestHeader(value = "timeStamp", required = true) Integer timeStamp,
			@ApiParam(value = "Version of API", defaultValue = "1.0.0") @RequestHeader(value = "version", required = false) String version) {

    	logger.info("Fintuil is :"+ finUtil);
    	
		String accept = this.request.getHeader("Accept");
		MobileAppCommonDao<?> commonDao = null;
		Long sequence = null;
		SaveFBTokenResponsePost saveFBTokenResponsePost = new SaveFBTokenResponsePost();
		SaveFBTokenApiService saveFBTokenApiService = new SaveFBTokenApiService();
		try {
			if (accept != null) {
				commonDao = new MobileAppCommonDao<>();
				if (commonDao.requestAlreadyExists(requestId.toString())) {
					this.logger.info("Request Id already exists");
					commonDao.updateAuditRecord(requestId.toString(), "Exception has occurred", "Exception has occurred", HttpStatus.BAD_REQUEST);
					return new ResponseEntity<>(saveFBTokenResponsePost, HttpStatus.BAD_REQUEST);
				}
				sequence = commonDao.saveAuditRecord(body.toString(), requestId.toString(), "SaveFBTokenApi");
				this.logger.info("Saving record into Audit table and sequence no is :" + sequence);
				if (sequence != null && requestId != null)
					saveFBTokenResponsePost = saveFBTokenApiService.saveFireBaseToken(body, requestId, sequence);

				this.logger.info("Final Response message is :" + saveFBTokenResponsePost);
				String jsonStr = FinUtil.getJsonString(saveFBTokenResponsePost);
				if (jsonStr != null)
					saveFBTokenResponsePost = (SaveFBTokenResponsePost) this.objectMapper.readValue(jsonStr, SaveFBTokenResponsePost.class);

				commonDao.updateAuditRecord(sequence, jsonStr, null, HttpStatus.OK.toString());
				return new ResponseEntity<>(saveFBTokenResponsePost, HttpStatus.OK);

			}
		} catch (Exception e) {
			this.logger.error("Couldn't serialize response for content type application/json: " + e.getMessage());
			commonDao.updateAuditRecord(sequence, saveFBTokenResponsePost.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			return new ResponseEntity<>(saveFBTokenResponsePost, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(saveFBTokenResponsePost, HttpStatus.NOT_IMPLEMENTED);
	}

}
