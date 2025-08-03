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

import com.csme.csmeapi.api.ProfileApi;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.ProfileRequest;
import com.csme.csmeapi.fin.models.ProfileResponse;
import com.csme.csmeapi.fin.services.MobileAppCommonDao;
import com.csme.csmeapi.fin.services.ProfileApiServices;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;

/**
 * Controller class for handling profile-related API requests.
 */
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-01-21T18:21:27.250+04:00[Asia/Muscat]")
@Controller
public class ProfileApiController implements ProfileApi {
	
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
    public ProfileApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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
     * Handles the profile API endpoint.
     * 
     * @param body    Profile request body.
     * @param requestId Request ID header.
     * @param channel Channel header.
     * @param timeStamp Time stamp header.
     * @param version Version header.
     * @return ResponseEntity<ProfileResponse>
     */
    public ResponseEntity<ProfileResponse> profile(
            @ApiParam(value = "Profile Request", required = true) @Valid @RequestBody ProfileRequest body,
            @ApiParam(value = "Request Id", required = true) @RequestHeader(value = "requestId", required = true) UUID requestId,
            @ApiParam(value = "Channel Name", required = true, defaultValue = "FinMobileBankingApp") @RequestHeader(value = "channel", required = true) String channel,
            @ApiParam(value = "Time date Unix Format", required = true) @RequestHeader(value = "timeStamp", required = true) Integer timeStamp,
            @ApiParam(value = "Version of API", defaultValue = "1.0.0") @RequestHeader(value = "version", required = false) String version) {

    	logger.info("Fintuil is :"+ finUtil);
    	    	
    	String accept = this.request.getHeader("Accept");
        MobileAppCommonDao<?> commonDao = null;
        Long sequence = null;
        ProfileResponse profileResponse = new ProfileResponse();
        ProfileApiServices profileApiServices = new ProfileApiServices();
        if (accept != null)
            try {
                commonDao = new MobileAppCommonDao<>();
                if (commonDao.requestAlreadyExists(requestId.toString())) {
                    this.logger.info("Request Id is already exist");
                    commonDao.updateAuditRecord(requestId.toString(), "Exception has occurred", "Exception has occurred",
                            HttpStatus.BAD_REQUEST);
                    return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.BAD_REQUEST);
                }
                sequence = commonDao.saveAuditRecord(body.toString(), requestId.toString(), "ProfileApi");
                this.logger.info("Saving record into Audit table and sequence no is :" + sequence);
                if (sequence != null && requestId != null)
                    profileResponse = profileApiServices.getProfileInfo(body, requestId, sequence);

                this.logger.error("Final Response message is :" + profileResponse);
                String jsonStr = FinUtil.getJsonString(profileResponse);
                if (jsonStr != null)
                    profileResponse = (ProfileResponse) this.objectMapper.readValue(jsonStr, ProfileResponse.class);

                commonDao.updateAuditRecord(sequence, jsonStr, null, HttpStatus.OK + "");
                return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.OK);
            } catch (Exception e) {
                this.logger.error("Couldn't serialize response for content type application/json" + e);
                commonDao.updateAuditRecord(sequence, profileResponse.toString(), null,
                        HttpStatus.INTERNAL_SERVER_ERROR + "");
                return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.NOT_IMPLEMENTED);
    }

}
