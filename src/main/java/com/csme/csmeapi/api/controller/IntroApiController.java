package com.csme.csmeapi.api.controller;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;

import com.csme.csmeapi.api.IntroApi;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.IntroResponse;
import com.csme.csmeapi.fin.services.IntroApiService;
import com.csme.csmeapi.fin.services.MobileAppCommonDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-01-30T20:25:55.040+04:00[Asia/Muscat]")
/**
 * Controller class for handling introductory API requests.
 * Implements the IntroApi interface.
 */
@Controller
public class IntroApiController implements IntroApi {

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
     * Constructor for IntroApiController.
     * @param objectMapper The object mapper for JSON serialization.
     * @param request The HTTP servlet request.
     */
    @org.springframework.beans.factory.annotation.Autowired
    public IntroApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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

    public ResponseEntity<IntroResponse> introduction(@ApiParam(value = "Request Id" ,required=true) @RequestHeader(value="requestId", required=true) UUID requestId
,@ApiParam(value = "Channel Name" ,required=true, defaultValue="FinMobileBankingApp") @RequestHeader(value="channel", required=true) String channel
,@ApiParam(value = "Time date Unix Format" ,required=true) @RequestHeader(value="timeStamp", required=true) Integer timeStamp
,@ApiParam(value = "Secert Key" ) @RequestHeader(value="SecertKey", required=false) String secertKey
,@ApiParam(value = "Version of API" , defaultValue="1.0.0") @RequestHeader(value="version", required=false) String version
) {

    	logger.info("Fintuil is :"+ finUtil);
    	
    	String accept = this.request.getHeader("Accept");
		MobileAppCommonDao<?> commonDao = null;
		Long sequence = null;
		IntroResponse introResponse = new IntroResponse();
		IntroApiService IntroApiService = new IntroApiService();
		if (accept != null)
			try {
				commonDao = new MobileAppCommonDao<>();
				if (commonDao.requestAlreadyExists(requestId.toString())) {
					this.logger.info("Request Id is aleady exist");
					commonDao.updateAuditRecord(requestId.toString(), "Exception has occured", "Exception has occured", HttpStatus.BAD_REQUEST);
					return new ResponseEntity<IntroResponse>(introResponse, HttpStatus.BAD_REQUEST);
				} 
				sequence = commonDao.saveAuditRecord(requestId.toString(), requestId.toString(), "IntroApi");
				this.logger.info("Saving record into Audit table and sequence no is :" + sequence);
				introResponse = IntroApiService.getIntroduction(requestId, sequence);
				String jsonStr = FinUtil.getJsonString(introResponse);
				if (jsonStr != null)
					introResponse = (IntroResponse)this.objectMapper.readValue(jsonStr, IntroResponse.class); 
				commonDao.updateAuditRecord(sequence, jsonStr, null, HttpStatus.OK + "");
				return new ResponseEntity<IntroResponse>(introResponse, HttpStatus.OK);
			} catch (Exception e) {
				this.logger.error("Couldn't serialize response for content type application/json" + e);
				commonDao.updateAuditRecord(sequence, introResponse.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR + "");
				return new ResponseEntity<IntroResponse>(introResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}  
		return new ResponseEntity<IntroResponse>(introResponse, HttpStatus.NOT_IMPLEMENTED);
    }

}
