package com.csme.csmeapi.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.csme.csmeapi.api.ModuleAnalyticsApi;
import com.csme.csmeapi.fin.models.XModuleAnalyticsRequest;
import com.csme.csmeapi.fin.models.XModuleAnalyticsResponse;
import com.csme.csmeapi.fin.services.ModuleAnalyticsApiService;

@Controller
public class ModuleAnalyticsApiController implements ModuleAnalyticsApi {

    @Autowired
    private ModuleAnalyticsApiService moduleAnalyticsApiService;

    @Override
    public ResponseEntity<XModuleAnalyticsResponse> getModuleAnalytics(
            @RequestHeader(value = "requestId", required = true) String requestId,
            @RequestHeader(value = "channel", required = true) String channel,
            @RequestHeader(value = "timeStamp", required = true) Integer timeStamp,
            @RequestHeader(value = "version", required = false) String version,
            @Valid @RequestBody XModuleAnalyticsRequest moduleAnalyticsRequest) {
        
        try {
            // Log the request
            System.out.println("ModuleAnalytics Request received - RequestId: " + requestId + 
                    ", CorporateId: " + moduleAnalyticsRequest.getCorporateId() + 
                    ", UserId: " + moduleAnalyticsRequest.getUserId() +
                    ", Module: " + moduleAnalyticsRequest.getModule());
            
            // Call the service to get module analytics
            XModuleAnalyticsResponse response = moduleAnalyticsApiService.getModuleAnalytics(
                    moduleAnalyticsRequest, requestId);
            
            // Return the response
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            // Log the error
            System.err.println("Error in ModuleAnalyticsApiController: " + e.getMessage());
            e.printStackTrace();
            
            // Create error response
            XModuleAnalyticsResponse errorResponse = new XModuleAnalyticsResponse();
            errorResponse.setMessageId("ERROR");
            errorResponse.setRequestId(requestId);
            errorResponse.setStatusCode("99");
            errorResponse.setStatusDescription("Error retrieving module analytics: " + e.getMessage());
            errorResponse.setTimestamp(String.valueOf(System.currentTimeMillis()));
            
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}