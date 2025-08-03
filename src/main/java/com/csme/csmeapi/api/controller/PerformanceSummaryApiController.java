package com.csme.csmeapi.api.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.csme.csmeapi.api.PerformanceSummaryApi;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XPerformanceSummaryRequest;
import com.csme.csmeapi.fin.models.XPerformanceSummaryResponse;
import com.csme.csmeapi.fin.services.PerformanceSummaryApiService;
import com.csme.csmeapi.fin.services.MobileAppCommonDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-07-23T10:00:00.000000+04:00[Asia/Dubai]")
@Controller
public class PerformanceSummaryApiController implements PerformanceSummaryApi {

    private static final Logger log = LogManager.getLogger(PerformanceSummaryApiController.class);

    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    private final PerformanceSummaryApiService performanceSummaryApiService;
    private final MobileAppCommonDao mobileAppCommonDao;

    @org.springframework.beans.factory.annotation.Autowired
    public PerformanceSummaryApiController(ObjectMapper objectMapper, 
                                         HttpServletRequest request,
                                         PerformanceSummaryApiService performanceSummaryApiService,
                                         MobileAppCommonDao mobileAppCommonDao) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.performanceSummaryApiService = performanceSummaryApiService;
        this.mobileAppCommonDao = mobileAppCommonDao;
    }

    @Override
    public ResponseEntity<Object> getPerformanceSummary(
            @ApiParam(value = "Request Id (UUID format)", required = true) @RequestHeader(value = "requestId", required = true) String requestId,
            @ApiParam(value = "Secret Key") @RequestHeader(value = "SecertKey", required = false) String secertKey,
            @ApiParam(value = "Channel Name", required = true, defaultValue = "FinMobileBankingApp") @RequestHeader(value = "channel", required = true) String channel,
            @ApiParam(value = "Time date Unix Format", required = true) @RequestHeader(value = "timeStamp", required = true) String timeStamp,
            @ApiParam(value = "Entity", required = true) @RequestHeader(value = "entity", required = true) String entity,
            @ApiParam(value = "") @Valid @RequestBody XPerformanceSummaryRequest performanceSummary,
            HttpServletRequest request) {

        log.info("PerformanceSummary API called with requestId: " + requestId);

        try {
            // Validate request
            if (performanceSummary == null || 
                performanceSummary.getCorporateId() == null || 
                performanceSummary.getUserId() == null) {
                
                log.error("Invalid request: Missing required fields");
                return new ResponseEntity<>("Missing required fields: CorporateId and UserId", HttpStatus.BAD_REQUEST);
            }

            // Log request details
            log.info("Getting performance summary for Corporate: " + performanceSummary.getCorporateId() + 
                    ", User: " + performanceSummary.getUserId() + 
                    ", Period: " + performanceSummary.getPeriod());

            // Validate headers
            if (!FinUtil.validateHeaders(requestId, secertKey, channel, timeStamp, entity)) {
                log.error("Header validation failed");
                return new ResponseEntity<>("Invalid headers", HttpStatus.BAD_REQUEST);
            }

            // Call service
            XPerformanceSummaryResponse response = performanceSummaryApiService.getPerformanceSummary(performanceSummary, requestId);

            if (response != null && "00".equals(response.getStatusCode())) {
                log.info("Performance summary retrieved successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                log.error("Failed to retrieve performance summary: " + 
                         (response != null ? response.getStatusDescription() : "Unknown error"));
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            log.error("Exception in getPerformanceSummary: " + e.getMessage(), e);
            
            XPerformanceSummaryResponse errorResponse = new XPerformanceSummaryResponse();
            errorResponse.setRequestId(requestId);
            errorResponse.setStatusCode("01");
            errorResponse.setStatusDescription("Internal server error: " + e.getMessage());
            
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}