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

import com.csme.csmeapi.api.TransactionAnalyticsApi;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XTransactionAnalyticsRequest;
import com.csme.csmeapi.fin.models.XTransactionAnalyticsResponse;
import com.csme.csmeapi.fin.services.TransactionAnalyticsApiService;
import com.csme.csmeapi.fin.services.MobileAppCommonDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-07-23T10:00:00.000000+04:00[Asia/Dubai]")
@Controller
public class TransactionAnalyticsApiController implements TransactionAnalyticsApi {

    private static final Logger log = LogManager.getLogger(TransactionAnalyticsApiController.class);

    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    private final TransactionAnalyticsApiService transactionAnalyticsApiService;
    private final MobileAppCommonDao mobileAppCommonDao;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionAnalyticsApiController(ObjectMapper objectMapper, 
                                           HttpServletRequest request,
                                           TransactionAnalyticsApiService transactionAnalyticsApiService,
                                           MobileAppCommonDao mobileAppCommonDao) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.transactionAnalyticsApiService = transactionAnalyticsApiService;
        this.mobileAppCommonDao = mobileAppCommonDao;
    }

    @Override
    public ResponseEntity<Object> getTransactionAnalytics(
            @ApiParam(value = "Request Id (UUID format)", required = true) @RequestHeader(value = "requestId", required = true) String requestId,
            @ApiParam(value = "Secret Key") @RequestHeader(value = "SecertKey", required = false) String secertKey,
            @ApiParam(value = "Channel Name", required = true, defaultValue = "FinMobileBankingApp") @RequestHeader(value = "channel", required = true) String channel,
            @ApiParam(value = "Time date Unix Format", required = true) @RequestHeader(value = "timeStamp", required = true) String timeStamp,
            @ApiParam(value = "Entity", required = true) @RequestHeader(value = "entity", required = true) String entity,
            @ApiParam(value = "") @Valid @RequestBody XTransactionAnalyticsRequest transactionAnalytics,
            HttpServletRequest request) {

        log.info("TransactionAnalytics API called with requestId: " + requestId);

        try {
            // Validate request
            if (transactionAnalytics == null || 
                transactionAnalytics.getCorporateId() == null || 
                transactionAnalytics.getUserId() == null) {
                
                log.error("Invalid request: Missing required fields");
                return new ResponseEntity<>("Missing required fields: CorporateId and UserId", HttpStatus.BAD_REQUEST);
            }

            // Log request details
            log.info("Getting transaction analytics for Corporate: " + transactionAnalytics.getCorporateId() + 
                    ", User: " + transactionAnalytics.getUserId() + 
                    ", Module: " + transactionAnalytics.getModule());

            // Validate headers
            if (!FinUtil.validateHeaders(requestId, secertKey, channel, timeStamp, entity)) {
                log.error("Header validation failed");
                return new ResponseEntity<>("Invalid headers", HttpStatus.BAD_REQUEST);
            }

            // Call service
            XTransactionAnalyticsResponse response = transactionAnalyticsApiService.getTransactionAnalytics(transactionAnalytics, requestId);

            if (response != null && "00".equals(response.getStatusCode())) {
                log.info("Transaction analytics retrieved successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                log.error("Failed to retrieve transaction analytics: " + 
                         (response != null ? response.getStatusDescription() : "Unknown error"));
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            log.error("Exception in getTransactionAnalytics: " + e.getMessage(), e);
            
            XTransactionAnalyticsResponse errorResponse = new XTransactionAnalyticsResponse();
            errorResponse.setRequestId(requestId);
            errorResponse.setStatusCode("01");
            errorResponse.setStatusDescription("Internal server error: " + e.getMessage());
            
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}