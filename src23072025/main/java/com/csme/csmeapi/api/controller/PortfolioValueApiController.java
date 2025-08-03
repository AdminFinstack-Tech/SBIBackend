package com.csme.csmeapi.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.csme.csmeapi.api.PortfolioValueApi;
import com.csme.csmeapi.fin.models.XPortfolioValueRequest;
import com.csme.csmeapi.fin.models.XPortfolioValueResponse;
import com.csme.csmeapi.fin.services.PortfolioValueApiService;

@Controller
public class PortfolioValueApiController implements PortfolioValueApi {

    @Autowired
    private PortfolioValueApiService portfolioValueApiService;

    @Override
    public ResponseEntity<XPortfolioValueResponse> getPortfolioValue(
            @RequestHeader(value = "requestId", required = true) String requestId,
            @RequestHeader(value = "channel", required = true) String channel,
            @RequestHeader(value = "timeStamp", required = true) Integer timeStamp,
            @RequestHeader(value = "version", required = false) String version,
            @Valid @RequestBody XPortfolioValueRequest portfolioValueRequest) {
        
        try {
            // Log the request
            System.out.println("PortfolioValue Request received - RequestId: " + requestId + 
                    ", CorporateId: " + portfolioValueRequest.getCorporateId() + 
                    ", UserId: " + portfolioValueRequest.getUserId());
            
            // Call the service to get portfolio value
            XPortfolioValueResponse response = portfolioValueApiService.getPortfolioValue(
                    portfolioValueRequest, requestId);
            
            // Return the response
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            // Log the error
            System.err.println("Error in PortfolioValueApiController: " + e.getMessage());
            e.printStackTrace();
            
            // Create error response
            XPortfolioValueResponse errorResponse = new XPortfolioValueResponse();
            errorResponse.setMessageId("ERROR");
            errorResponse.setRequestId(requestId);
            errorResponse.setStatusCode("99");
            errorResponse.setStatusDescription("Error retrieving portfolio value: " + e.getMessage());
            errorResponse.setTimestamp(String.valueOf(System.currentTimeMillis()));
            
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}