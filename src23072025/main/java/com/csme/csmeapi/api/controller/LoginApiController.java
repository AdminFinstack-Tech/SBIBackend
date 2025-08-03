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

import com.csme.csmeapi.api.LoginApi;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.config.JwtTokenUtil;
import com.csme.csmeapi.fin.models.LoginRequest;
import com.csme.csmeapi.fin.models.LoginResponse;
import com.csme.csmeapi.fin.services.LoginApiServices;
import com.csme.csmeapi.fin.services.MobileAppCommonDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-01-04T22:54:02.424+04:00[Asia/Muscat]")
@Controller
public class LoginApiController implements LoginApi {

	FinUtil finUtil = new FinUtil();
	
	/**
	 * JWT Token Utility instance for handling JSON Web Tokens (JWTs) in authentication.
	 */
	@Autowired
	private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

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

	@org.springframework.beans.factory.annotation.Autowired
	public LoginApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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
	 * Endpoint for user login.
	 * @param body The login request body.
	 * @param requestId The unique request ID.
	 * @param channel The channel name.
	 * @param timeStamp The time in Unix format.
	 * @param secertKey The secret key (optional).
	 * @param version The API version (optional).
	 * @return A ResponseEntity containing the login response.
	 */
	public ResponseEntity<LoginResponse> login(@ApiParam(value = "Login Request" ,required=true )  @Valid @RequestBody LoginRequest body
			,@ApiParam(value = "Request Id" ,required=true) @RequestHeader(value="requestId", required=true) UUID requestId
			,@ApiParam(value = "Channel Name" ,required=true, defaultValue="FinMobileBankingApp") @RequestHeader(value="channel", required=true) String channel
			,@ApiParam(value = "Time date Unix Format" ,required=true) @RequestHeader(value="timeStamp", required=true) Integer timeStamp
			,@ApiParam(value = "Secert Key" ) @RequestHeader(value="SecertKey", required=false) String secertKey
			,@ApiParam(value = "Version of API" , defaultValue="1.0.0") @RequestHeader(value="version", required=false) String version
			) {

    	logger.info("Fintuil is :"+ finUtil);
    	
		String accept = this.request.getHeader("Accept");
		MobileAppCommonDao<?> commonDao = null;
		Long sequence = null;
		LoginResponse loginResponse = new LoginResponse();
		LoginApiServices loginApiServices = new LoginApiServices();

		if (accept != null)
			try {
				commonDao = new MobileAppCommonDao<>();
				if (commonDao.requestAlreadyExists(requestId.toString())) {
					this.logger.info("Request Id is aleady exist");
					commonDao.updateAuditRecord(requestId.toString(), "Exception has occured", "Exception has occured", HttpStatus.BAD_REQUEST);
					return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.BAD_REQUEST);
				} 
				sequence = commonDao.saveAuditRecord(body.toString(), requestId.toString(), "LoginApiController");
				this.logger.info("Saving record into Audit table and sequence no is :" + sequence);
				if (sequence != null && requestId != null)
					loginResponse = loginApiServices.getLoginInfo(body, requestId, sequence);

				try {
					if(loginResponse.getStatusCode().equalsIgnoreCase("00")) {
						String token = jwtTokenUtil.generateToken(body,secertKey);
						if(token != null && token != "") {
							loginResponse.setToken(token);
						} else {
							loginResponse = unAuthErrorResponse(requestId, sequence, loginResponse);
							commonDao.updateAuditRecord(sequence, loginResponse.toString(), null, HttpStatus.UNAUTHORIZED + "");
							this.logger.error("Exception is occured in unauth token else");
							return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.UNAUTHORIZED);
						}
					} 
				} catch (Exception e) {
					e.printStackTrace();
					this.logger.error("Exception is occured in unauth token :" + e);
					loginResponse = unAuthErrorResponse(requestId, sequence, loginResponse);
					commonDao.updateAuditRecord(sequence, loginResponse.toString(), null, HttpStatus.UNAUTHORIZED + "");
					return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.UNAUTHORIZED);
				}
				this.logger.error("Final Response messsage is :" + loginResponse);
				String jsonStr = FinUtil.getJsonString(loginResponse);
				if (jsonStr != null)
					loginResponse = (LoginResponse)this.objectMapper.readValue(jsonStr, LoginResponse.class); 

				commonDao.updateAuditRecord(sequence, jsonStr, null, HttpStatus.OK + "");
				return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
			} catch (Exception e) {
				this.logger.error("Couldn't serialize response for content type application/json" + e);
				commonDao.updateAuditRecord(sequence, loginResponse.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR + "");
				return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}  
		return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.NOT_IMPLEMENTED);
	}

	private LoginResponse unAuthErrorResponse(UUID requestId, Long sequence, LoginResponse loginResponse) {
		loginResponse = new LoginResponse();
		loginResponse.setMessageId(sequence.toString());
		loginResponse.setRequestId(requestId.toString());
		String setTimeStamp = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).format(new Date());
		loginResponse.setTimestamp(setTimeStamp);
		loginResponse.setStatusCode("98");
		loginResponse.setStatusDescription("UNAUTHORIZED");
		return loginResponse;
	}

}
