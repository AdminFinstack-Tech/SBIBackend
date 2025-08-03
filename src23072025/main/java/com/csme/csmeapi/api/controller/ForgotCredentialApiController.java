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

import com.csme.csmeapi.api.ForgotcredentialApi;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XForgotCredentialRequest;
import com.csme.csmeapi.fin.models.XForgotCredentialResponse;
import com.csme.csmeapi.fin.services.ForgotCredentialApiService;
import com.csme.csmeapi.fin.services.MobileAppCommonDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-02-10T21:42:00.326+04:00[Asia/Muscat]")
/**
 * Controller class for handling forgot credential API requests.
 */
@Controller
public class ForgotCredentialApiController implements ForgotcredentialApi {
	
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
     * Constructor for ForgotCredentialApiController.
     *
     * @param objectMapper ObjectMapper instance for JSON processing.
     * @param request      HttpServletRequest instance for handling HTTP requests.
     */
    @org.springframework.beans.factory.annotation.Autowired
    public ForgotCredentialApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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
     * Handles forgot credential API requests.
     *
     * @param body     The request body containing forgot credential information.
     * @param requestId The unique identifier for the request.
     * @param channel  The channel through which the request is made.
     * @param timeStamp The timestamp of the request.
     * @param version  The version of the API.
     * @return ResponseEntity containing the forgot credential response.
     */
    public ResponseEntity<XForgotCredentialResponse> forgotcredential(
            @ApiParam(value = "Forgot Password", required = true) @Valid @RequestBody XForgotCredentialRequest body,
            @ApiParam(value = "Request Id", required = true) @RequestHeader(value = "requestId", required = true) UUID requestId,
            @ApiParam(value = "Channel Name", required = true, defaultValue = "FinMobileBankingApp") @RequestHeader(value = "channel", required = true) String channel,
            @ApiParam(value = "Time date Unix Format", required = true) @RequestHeader(value = "timeStamp", required = true) Integer timeStamp,
            @ApiParam(value = "Version of API", defaultValue = "1.0.0") @RequestHeader(value = "version", required = false) String version) {
        
    	logger.info("Fintuil is :"+ finUtil);
        // Get the 'Accept' header from the request
        String accept = this.request.getHeader("Accept");
        MobileAppCommonDao<?> commonDao = null;
        Long sequence = null;
        XForgotCredentialResponse forgotCredentialResponse = new XForgotCredentialResponse();
        
        // Check if 'Accept' header is present
        if (accept != null) {
            try {
                ForgotCredentialApiService forgotCredentialApiService = new ForgotCredentialApiService();
                commonDao = new MobileAppCommonDao<>();
                
                // Check if the request ID already exists
                if (commonDao.requestAlreadyExists(requestId.toString())) {
                    this.logger.info("Request Id is already exist");
                    commonDao.updateAuditRecord(requestId.toString(), "Exception has occurred", "Exception has occurred", HttpStatus.BAD_REQUEST);
                    return new ResponseEntity<XForgotCredentialResponse>(forgotCredentialResponse, HttpStatus.BAD_REQUEST);
                } 
                
                // Save audit record and generate OTP for forgot credential
                sequence = commonDao.saveAuditRecord(body.toString(), requestId.toString(), "ForgotCredentialApi");
                this.logger.info("Saving record into Audit table and sequence no is :" + sequence);
                
                if (sequence != null && requestId != null)
                    forgotCredentialResponse = forgotCredentialApiService.generateOTP(body, requestId, sequence);

                this.logger.error("Final Response message is :" + forgotCredentialResponse);
                String jsonStr = FinUtil.getJsonString(forgotCredentialResponse);
                
                if (jsonStr != null)
                    forgotCredentialResponse = (XForgotCredentialResponse) this.objectMapper.readValue(jsonStr, XForgotCredentialResponse.class); 

                commonDao.updateAuditRecord(sequence, jsonStr, null, HttpStatus.OK + "");
                return new ResponseEntity<XForgotCredentialResponse>(forgotCredentialResponse, HttpStatus.OK);
            } catch (Exception e) {
                this.logger.error("Couldn't serialize response for content type application/json" + e);
                commonDao.updateAuditRecord(sequence, forgotCredentialResponse.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR + "");
                return new ResponseEntity<XForgotCredentialResponse>(forgotCredentialResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }  
        }

        return new ResponseEntity<XForgotCredentialResponse>(forgotCredentialResponse, HttpStatus.NOT_IMPLEMENTED);
    }

}
