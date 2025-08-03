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

import com.csme.csmeapi.api.ForgotpasswordApi;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XForgotPasswordRequest;
import com.csme.csmeapi.fin.models.XForgotPasswordResponse;
import com.csme.csmeapi.fin.services.ForgotpasswordApiService;
import com.csme.csmeapi.fin.services.MobileAppCommonDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-02-09T19:33:29.157+04:00[Asia/Muscat]")
/**
 * Controller class for handling forgot password API requests.
 */
@Controller
public class ForgotpasswordApiController implements ForgotpasswordApi {

	FinUtil finUtil = new FinUtil();
	
    // Logger for logging messages
    private Logger logger = LogManager.getLogger("CSMEMobile");

    // ObjectMapper for JSON serialization/deserialization
    private final ObjectMapper objectMapper;

    // HttpServletRequest for handling HTTP request details
    private final HttpServletRequest request;

    /**
     * Constructor for ForgotpasswordApiController.
     *
     * @param objectMapper ObjectMapper for JSON serialization/deserialization.
     * @param request      HttpServletRequest for handling HTTP request details.
     */
    @org.springframework.beans.factory.annotation.Autowired
    public ForgotpasswordApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    /**
     * Get the ObjectMapper instance.
     *
     * @return Optional containing ObjectMapper instance.
     */
    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.ofNullable(objectMapper);
    }

    /**
     * Get the HttpServletRequest instance.
     *
     * @return Optional containing HttpServletRequest instance.
     */
    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    /**
     * Handles forgot password API requests.
     *
     * @param body     The request body containing forgot password information.
     * @param requestId The unique identifier for the request.
     * @param channel  The channel through which the request is made.
     * @param timeStamp The timestamp of the request.
     * @param version  The version of the API.
     * @return ResponseEntity containing the forgot password response.
     */
    public ResponseEntity<XForgotPasswordResponse> forgotpassword(
            @ApiParam(value = "Forgot Password", required = true) @Valid @RequestBody XForgotPasswordRequest body,
            @ApiParam(value = "Request Id", required = true) @RequestHeader(value = "requestId", required = true) UUID requestId,
            @ApiParam(value = "Channel Name", required = true, defaultValue = "FinMobileBankingApp") @RequestHeader(value = "channel", required = true) String channel,
            @ApiParam(value = "Time date Unix Format", required = true) @RequestHeader(value = "timeStamp", required = true) Integer timeStamp,
            @ApiParam(value = "Version of API", defaultValue = "1.0.0") @RequestHeader(value = "version", required = false) String version) {
        
    	logger.info("Fintuil is :"+ finUtil);
    	
        // Get the 'Accept' header from the request
        String accept = this.request.getHeader("Accept");
        MobileAppCommonDao<?> commonDao = null;
        Long sequence = null;
        XForgotPasswordResponse forgotPasswordResponse = new XForgotPasswordResponse();
        
        // Check if 'Accept' header is present
        if (accept != null) {
            try {
                ForgotpasswordApiService ForgotpasswordApiService = new ForgotpasswordApiService();
                commonDao = new MobileAppCommonDao<>();
                
                // Check if the request ID already exists
                if (commonDao.requestAlreadyExists(requestId.toString())) {
                    this.logger.info("Request Id is already exist");
                    commonDao.updateAuditRecord(requestId.toString(), "Exception has occurred", "Exception has occurred", HttpStatus.BAD_REQUEST);
                    return new ResponseEntity<XForgotPasswordResponse>(forgotPasswordResponse, HttpStatus.BAD_REQUEST);
                } 
                
                // Save audit record and generate OTP for forgot password
                sequence = commonDao.saveAuditRecord(body.toString(), requestId.toString(), "ForgotPasswordApi");
                this.logger.info("Saving record into Audit table and sequence no is :" + sequence);
                
                if (sequence != null && requestId != null)
                    forgotPasswordResponse = ForgotpasswordApiService.generateOTP(body, requestId, sequence);

                this.logger.error("Final Response message is :" + forgotPasswordResponse);
                String jsonStr = FinUtil.getJsonString(forgotPasswordResponse);
                
                if (jsonStr != null)
                    forgotPasswordResponse = (XForgotPasswordResponse) this.objectMapper.readValue(jsonStr, XForgotPasswordResponse.class); 

                commonDao.updateAuditRecord(sequence, jsonStr, null, HttpStatus.OK + "");
                return new ResponseEntity<XForgotPasswordResponse>(forgotPasswordResponse, HttpStatus.OK);
            } catch (Exception e) {
                this.logger.error("Couldn't serialize response for content type application/json" + e);
                commonDao.updateAuditRecord(sequence, forgotPasswordResponse.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR + "");
                return new ResponseEntity<XForgotPasswordResponse>(forgotPasswordResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }  
        }
        return new ResponseEntity<XForgotPasswordResponse>(forgotPasswordResponse, HttpStatus.NOT_IMPLEMENTED);
    }
}

