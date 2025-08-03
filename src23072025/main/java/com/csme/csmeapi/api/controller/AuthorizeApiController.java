package com.csme.csmeapi.api.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.cs.core.result.Result;
import com.csme.csmeapi.api.AuthorizeApi;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XAuthorizeRequest;
import com.csme.csmeapi.fin.models.XAuthorizeResponse;
import com.csme.csmeapi.fin.models.XAuthorizeResponse.StatusEnum;
import com.csme.csmeapi.fin.services.AuthorizeApiService;
import com.csme.csmeapi.fin.services.MobileAppCommonDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;

/**
 * Controller class for handling authorization API requests.
 */
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-01-30T20:25:55.040+04:00[Asia/Muscat]")
@Controller
public class AuthorizeApiController implements AuthorizeApi {

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
	 * Constructor for AuthorizeApiController.
	 * 
	 * @param objectMapper Object mapper for JSON processing.
	 * @param request      HTTP servlet request.
	 */
	@org.springframework.beans.factory.annotation.Autowired
	public AuthorizeApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@Override
	public Optional<ObjectMapper> getObjectMapper() {
		return Optional.ofNullable(objectMapper);
	}

	@Override
	public Optional<HttpServletRequest> getRequest() {
		return Optional.ofNullable(request);
	}

	/**
	 * Handles authorization transaction requests.
	 * 
	 * @param requestId Request Id header.
	 * @param channel   Channel Name header.
	 * @param timeStamp Time date Unix Format header.
	 * @param body      Authorization request body.
	 * @param version   Version of API header.
	 * @return ResponseEntity containing the authorization response.
	 */
	public ResponseEntity<XAuthorizeResponse> authorizeTransaction(
			@ApiParam(value = "Request Id", required = true) @RequestHeader(value = "requestId", required = true) UUID requestId,
			@ApiParam(value = "Channel Name", required = true, defaultValue = "FinMobileBankingApp") @RequestHeader(value = "channel", required = true) String channel,
			@ApiParam(value = "Time date Unix Format", required = true) @RequestHeader(value = "timeStamp", required = true) Integer timeStamp,
			@ApiParam(value = "") @Valid @RequestBody XAuthorizeRequest body,
			@ApiParam(value = "Version of API", defaultValue = "1.0.0") @RequestHeader(value = "version", required = false) String version) {
		
		logger.info("Fintuil is :"+ finUtil);
		String accept = this.request.getHeader("Accept");
		MobileAppCommonDao<XAuthorizeResponse> commonDao = null;
		Long sequence = null;
		XAuthorizeResponse xAuthorizeResponse = new XAuthorizeResponse();
		AuthorizeApiService authorizationService = new AuthorizeApiService();
		// Check if the Accept header is present
		if (accept != null) {
			try {
				commonDao = new MobileAppCommonDao<XAuthorizeResponse>();

				// Check if the request ID already exists
				if (commonDao.requestAlreadyExists(requestId.toString())) {
					this.logger.info("Request Id already exists");
					commonDao.updateAuditRecord(requestId.toString(), "Exception has occurred", "Exception has occurred", HttpStatus.BAD_REQUEST);
					return new ResponseEntity<XAuthorizeResponse>(xAuthorizeResponse, HttpStatus.BAD_REQUEST);
				}
				// Save audit record and set response attributes
				sequence = commonDao.saveAuditRecord(body.toString(), requestId.toString(), "AuthorizeApi");

				this.logger.info("Saving record into Audit table and sequence no is :" + sequence);
				Result res = authorizationService.getfunction(body);

				// Handle response based on success or failure
				if (res.isSuccess()) {
					xAuthorizeResponse = buildAuthorizeResponse(requestId.toString(),  body.getMainRef(),  sequence,  XAuthorizeResponse.StatusEnum.SUCCESS, "Success", "00");
					return new ResponseEntity<XAuthorizeResponse>(xAuthorizeResponse, HttpStatus.OK);
				} else {
					xAuthorizeResponse = buildAuthorizeResponse(requestId.toString(),  body.getMainRef(),  sequence,  XAuthorizeResponse.StatusEnum.FAILURE, "Failure", "10");
				}

				String jsonStr = FinUtil.getJsonString(xAuthorizeResponse);
				this.logger.info("Converting POJO class to JSON string is :" + jsonStr);
				if (jsonStr != null) {
					xAuthorizeResponse = (XAuthorizeResponse) this.objectMapper.readValue(jsonStr, XAuthorizeResponse.class);
				}
				// update sucess or failure message 
				commonDao.updateAuditRecord(sequence, jsonStr, xAuthorizeResponse.getStatusDescription(), xAuthorizeResponse.getStatusCode());
				this.logger.info("Final response sent message is :" + xAuthorizeResponse.toString());


				return new ResponseEntity<XAuthorizeResponse>(xAuthorizeResponse, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				this.logger.error("Couldn't serialize response for content type application/json" + e);
				commonDao.updateAuditRecord(sequence, "Exception has occurred", "Exception has occurred", HttpStatus.BAD_REQUEST);
				return new ResponseEntity<XAuthorizeResponse>(xAuthorizeResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<XAuthorizeResponse>(HttpStatus.NOT_IMPLEMENTED);
	}

	/**
	 * Builds an authorization response object.
	 *
	 * @param requestId   The request ID associated with the authorization request.
	 * @param EtradRef    The Etrade reference number.
	 * @param sequence    The sequence number of the authorization request.
	 * @param code        The status code indicating the result of the authorization.
	 * @param desc        The status description providing additional details about the authorization result.
	 * @param statusCode  The HTTP status code associated with the authorization response.
	 * @return An instance of XAuthorizeResponse representing the authorization response.
	 */
	private XAuthorizeResponse buildAuthorizeResponse(String requestId, String EtradRef, Long sequence, StatusEnum code, String desc, String statusCode) {
		XAuthorizeResponse xAuthorizeResponse = new XAuthorizeResponse();
		xAuthorizeResponse.setRequestId(requestId);
		xAuthorizeResponse.setEtradeReferenceNumber(EtradRef);
		xAuthorizeResponse.setMessageId(sequence.toString());
		xAuthorizeResponse.setTimestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date()));
		xAuthorizeResponse.setStatus(code);
		xAuthorizeResponse.setStatusCode(statusCode);
		xAuthorizeResponse.setStatusDescription(desc);
		return xAuthorizeResponse;
	}
	/**
	 * Main method for testing.
	 * 
	 * @param args Command-line arguments.
	 */
	public static void main(String[] args) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date currentDate = new Date();
		String reportDate = df.format(currentDate);
		System.out.println("Formatted Date: " + reportDate);
	}
}
