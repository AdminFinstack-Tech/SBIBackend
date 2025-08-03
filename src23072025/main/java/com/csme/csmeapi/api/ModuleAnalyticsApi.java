package com.csme.csmeapi.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.csme.csmeapi.fin.models.XModuleAnalyticsRequest;
import com.csme.csmeapi.fin.models.XModuleAnalyticsResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "ModuleAnalytics", description = "Module Analytics API")
public interface ModuleAnalyticsApi {

    @ApiOperation(value = "Get Module Analytics", nickname = "GetModuleAnalytics", 
            notes = "Get analytics data for a specific module", 
            response = XModuleAnalyticsResponse.class, tags = { "ModuleAnalytics", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = XModuleAnalyticsResponse.class),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/ModuleAnalytics", produces = { "application/json" }, 
            consumes = { "application/json" }, method = RequestMethod.POST)
    public ResponseEntity<XModuleAnalyticsResponse> getModuleAnalytics(
            @ApiParam(value = "Request Id", required = true) 
            @RequestHeader(value = "requestId", required = true) String requestId,
            
            @ApiParam(value = "Channel Name", required = true, defaultValue = "FinMobileBankingApp") 
            @RequestHeader(value = "channel", required = true) String channel,
            
            @ApiParam(value = "Time date Unix Format", required = true) 
            @RequestHeader(value = "timeStamp", required = true) Integer timeStamp,
            
            @ApiParam(value = "Version of API", defaultValue = "1.0.0") 
            @RequestHeader(value = "version", required = false) String version,
            
            @ApiParam(value = "Module Analytics Request", required = true) 
            @Valid @RequestBody XModuleAnalyticsRequest moduleAnalyticsRequest);
}