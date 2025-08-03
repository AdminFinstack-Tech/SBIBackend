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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.csme.csmeapi.api.PreferenceApi;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.GetPreferenceResponse;
import com.csme.csmeapi.fin.models.PreferenceRequestPost;
import com.csme.csmeapi.fin.models.PreferenceResponsePost;
import com.csme.csmeapi.fin.services.MobileAppCommonDao;
import com.csme.csmeapi.fin.services.SavepreferenceApiService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-01-30T20:25:55.040+04:00[Asia/Muscat]")
/**
 * Controller class for managing preferences.
 * Handles retrieving and saving preferences.
 */
@Controller
public class PreferenceApiController implements PreferenceApi {

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
     * Constructor for PreferenceApiController.
     * Initializes the object mapper and HTTP servlet request.
     * 
     * @param objectMapper The object mapper for JSON processing.
     * @param request The HTTP servlet request.
     */
    @org.springframework.beans.factory.annotation.Autowired
    public PreferenceApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    /**
     * Retrieves the optional object mapper.
     * 
     * @return Optional containing the object mapper if present, else empty.
     */
    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.ofNullable(objectMapper);
    }

    /**
     * Retrieves the optional HTTP servlet request.
     * 
     * @return Optional containing the HTTP servlet request if present, else empty.
     */
    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.ofNullable(request);
    }
    /**
     * Endpoint for retrieving user preferences.
     * @param requestId The request ID.
     * @param channel The channel name.
     * @param timeStamp The timestamp.
     * @param corporateId The corporate ID.
     * @param userId The user ID.
     * @param version The API version.
     * @return ResponseEntity containing the GetPreferenceResponse.
     */
    public ResponseEntity<GetPreferenceResponse> getPreference(@ApiParam(value = "Request Id" ,required=true) @RequestHeader(value="requestId", required=true) UUID requestId
,@ApiParam(value = "Channel Name" ,required=true, defaultValue="FinMobileBankingApp") @RequestHeader(value="channel", required=true) String channel
,@ApiParam(value = "Time date Unix Format" ,required=true) @RequestHeader(value="timeStamp", required=true) Integer timeStamp
,@ApiParam(value = "corporateId",required=true) @PathVariable("corporateId") String corporateId
,@ApiParam(value = "userId",required=true) @PathVariable("userId") String userId
,@ApiParam(value = "Version of API" , defaultValue="1.0.0") @RequestHeader(value="version", required=false) String version
) {

    	logger.info("Fintuil is :"+ finUtil);
    	
    	String accept = this.request.getHeader("Accept");
		MobileAppCommonDao<?> commonDao = null;
		Long sequence = null;
		GetPreferenceResponse getPreferenceResponse = new GetPreferenceResponse();
		SavepreferenceApiService savepreferenceApiService = new SavepreferenceApiService();
		if (accept != null)
			try {
				commonDao = new MobileAppCommonDao<>();
				if (commonDao.requestAlreadyExists(requestId.toString())) {
					this.logger.info("Request Id is aleady exist");
					commonDao.updateAuditRecord(requestId.toString(), "Exception has occured", "Exception has occured", HttpStatus.BAD_REQUEST);
					return new ResponseEntity<GetPreferenceResponse>(getPreferenceResponse, HttpStatus.BAD_REQUEST);
				} 
				sequence = commonDao.saveAuditRecord(corporateId.toString()+userId.toString(), requestId.toString(), "PreferenceApi");
				this.logger.info("Saving record into Audit table and sequence no is :" + sequence);
				if (sequence != null && requestId != null)
					getPreferenceResponse = savepreferenceApiService.getSavePreference(corporateId,userId, requestId, sequence);

				this.logger.error("Final Response messsage is :" + getPreferenceResponse);
				String jsonStr = FinUtil.getJsonString(getPreferenceResponse);
				if (jsonStr != null)
					getPreferenceResponse = (GetPreferenceResponse)this.objectMapper.readValue(jsonStr, GetPreferenceResponse.class); 

				commonDao.updateAuditRecord(sequence, jsonStr, null, HttpStatus.OK + "");
				return new ResponseEntity<GetPreferenceResponse>(getPreferenceResponse, HttpStatus.OK);
			} catch (Exception e) {
				this.logger.error("Couldn't serialize response for content type application/json" + e);
				commonDao.updateAuditRecord(sequence, getPreferenceResponse.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR + "");
				return new ResponseEntity<GetPreferenceResponse>(getPreferenceResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}  
		return new ResponseEntity<GetPreferenceResponse>(getPreferenceResponse, HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<PreferenceResponsePost> preference(@ApiParam(value = "Login Request" ,required=true )  @Valid @RequestBody PreferenceRequestPost body
,@ApiParam(value = "Request Id" ,required=true) @RequestHeader(value="requestId", required=true) UUID requestId
,@ApiParam(value = "Channel Name" ,required=true, defaultValue="FinMobileBankingApp") @RequestHeader(value="channel", required=true) String channel
,@ApiParam(value = "Time date Unix Format" ,required=true) @RequestHeader(value="timeStamp", required=true) Integer timeStamp
,@ApiParam(value = "Version of API" , defaultValue="1.0.0") @RequestHeader(value="version", required=false) String version
) {
    	String accept = this.request.getHeader("Accept");
		MobileAppCommonDao<?> commonDao = null;
		Long sequence = null;
		PreferenceResponsePost preferenceResponsePost = new PreferenceResponsePost();
		SavepreferenceApiService savepreferenceApiService = new SavepreferenceApiService();
		if (accept != null)
			try {
				commonDao = new MobileAppCommonDao<>();
				if (commonDao.requestAlreadyExists(requestId.toString())) {
					this.logger.info("Request Id is aleady exist");
					commonDao.updateAuditRecord(requestId.toString(), "Exception has occured", "Exception has occured", HttpStatus.BAD_REQUEST);
					return new ResponseEntity<PreferenceResponsePost>(preferenceResponsePost, HttpStatus.BAD_REQUEST);
				} 
				sequence = commonDao.saveAuditRecord(body.toString(), requestId.toString(), "PreferenceApiPost");
				this.logger.info("Saving record into Audit table and sequence no is :" + sequence);
				if (sequence != null && requestId != null)
					preferenceResponsePost = savepreferenceApiService.saveSavePreference(body, requestId, sequence);

				this.logger.error("Final Response messsage is :" + preferenceResponsePost);
				String jsonStr = FinUtil.getJsonString(preferenceResponsePost);
				if (jsonStr != null)
					preferenceResponsePost = (PreferenceResponsePost)this.objectMapper.readValue(jsonStr, PreferenceResponsePost.class); 

				commonDao.updateAuditRecord(sequence, jsonStr, null, HttpStatus.OK + "");
				return new ResponseEntity<PreferenceResponsePost>(preferenceResponsePost, HttpStatus.OK);
			} catch (Exception e) {
				this.logger.error("Couldn't serialize response for content type application/json" + e);
				commonDao.updateAuditRecord(sequence, preferenceResponsePost.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR + "");
				return new ResponseEntity<PreferenceResponsePost>(preferenceResponsePost, HttpStatus.INTERNAL_SERVER_ERROR);
			}  
		return new ResponseEntity<PreferenceResponsePost>(preferenceResponsePost, HttpStatus.NOT_IMPLEMENTED);
    }

}
