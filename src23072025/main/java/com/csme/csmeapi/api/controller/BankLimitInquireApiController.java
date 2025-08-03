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

import com.csme.csmeapi.api.BankLimitInquireApi;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XBankLimitInquireRequest;
import com.csme.csmeapi.fin.models.XBankLimitInquireResponse;
import com.csme.csmeapi.fin.services.BankLimitInquireResponseServiceApi;
import com.csme.csmeapi.fin.services.MobileAppCommonDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-02-06T23:52:00.541+04:00[Asia/Muscat]")
/**
 * Controller class for handling bank limit inquiry API requests.
 */
@Controller
public class BankLimitInquireApiController implements BankLimitInquireApi {

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
     * Constructor for BankLimitInquireApiController.
     *
     * @param objectMapper ObjectMapper instance for JSON processing.
     * @param request      HttpServletRequest instance for handling HTTP requests.
     */
    @org.springframework.beans.factory.annotation.Autowired
    public BankLimitInquireApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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
     * Handles bank limit inquiry API requests.
     *
     * @param body     The request body containing limit inquiry information.
     * @param requestId The unique identifier for the request.
     * @param channel  The channel through which the request is made.
     * @param timeStamp The timestamp of the request.
     * @param version  The version of the API.
     * @return ResponseEntity containing the bank limit inquiry response.
     */
    public ResponseEntity<XBankLimitInquireResponse> bankLimitInquire(
            @ApiParam(value = "Inquire Event History of Transaction", required = true) @Valid @RequestBody XBankLimitInquireRequest body,
            @ApiParam(value = "Request Id", required = true) @RequestHeader(value = "requestId", required = true) UUID requestId,
            @ApiParam(value = "Channel Name", required = true, defaultValue = "FinMobileBankingApp") @RequestHeader(value = "channel", required = true) String channel,
            @ApiParam(value = "Time date Unix Format", required = true) @RequestHeader(value = "timeStamp", required = true) Integer timeStamp,
            @ApiParam(value = "Version of API", defaultValue = "1.0.0") @RequestHeader(value = "version", required = false) String version) {
    	
    	logger.info("Fintuil is :"+ finUtil);
        String accept = this.request.getHeader("Accept");
        MobileAppCommonDao<?> commonDao = null;
        Long sequence = null;
        XBankLimitInquireResponse bankLimitInquireResponse = new XBankLimitInquireResponse();
        BankLimitInquireResponseServiceApi bankLimitResponse = new BankLimitInquireResponseServiceApi();
        
        if (accept != null) {
            try {
                commonDao = new MobileAppCommonDao<>();
                
                // Check if the request ID already exists
                if (commonDao.requestAlreadyExists(requestId.toString())) {
                    this.logger.info("Request Id is already exist");
                    commonDao.updateAuditRecord(requestId.toString(), "Exception has occurred", "Exception has occurred", HttpStatus.BAD_REQUEST);
                    return new ResponseEntity<XBankLimitInquireResponse>(bankLimitInquireResponse, HttpStatus.BAD_REQUEST);
                } 
                
                // Save audit record and retrieve limit inquiry response
                sequence = commonDao.saveAuditRecord(body.toString(), requestId.toString(), "BankLimitInquireApi");
                this.logger.info("Saving record into Audit table and sequence no is :" + sequence);
                
                if (sequence != null && requestId != null)
                    bankLimitInquireResponse = bankLimitResponse.getLimitInfo(body, requestId, sequence);

                this.logger.error("Final Response message is :" + bankLimitInquireResponse);
                String jsonStr = FinUtil.getJsonString(bankLimitInquireResponse);
                
                if (jsonStr != null)
                    bankLimitInquireResponse = (XBankLimitInquireResponse) this.objectMapper.readValue(jsonStr, XBankLimitInquireResponse.class); 

                commonDao.updateAuditRecord(sequence, jsonStr, null, HttpStatus.OK + "");
                return new ResponseEntity<XBankLimitInquireResponse>(bankLimitInquireResponse, HttpStatus.OK);
            } catch (Exception e) {
                this.logger.error("Couldn't serialize response for content type application/json" + e);
                commonDao.updateAuditRecord(sequence, bankLimitInquireResponse.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR + "");
                return new ResponseEntity<XBankLimitInquireResponse>(bankLimitInquireResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }  
        }

        return new ResponseEntity<XBankLimitInquireResponse>(bankLimitInquireResponse, HttpStatus.NOT_IMPLEMENTED);
    }
}


