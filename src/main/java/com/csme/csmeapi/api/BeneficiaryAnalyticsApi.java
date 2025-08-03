package com.csme.csmeapi.api;

import com.csme.csmeapi.fin.models.XBeneficiaryAnalyticsRequest;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-07-23T10:00:00.000000+04:00[Asia/Dubai]")
@Validated
@Api(value = "BeneficiaryAnalytics", description = "the BeneficiaryAnalytics API")
public interface BeneficiaryAnalyticsApi {

    @ApiOperation(value = "Get beneficiary analytics", nickname = "getBeneficiaryAnalytics", notes = "Retrieve comprehensive beneficiary analytics including transaction patterns, top beneficiaries, and distribution analysis", response = Object.class, tags = {"Beneficiary Analytics"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Beneficiary analytics retrieved successfully", response = Object.class),
            @ApiResponse(code = 400, message = "Bad request - invalid input parameters"),
            @ApiResponse(code = 401, message = "Unauthorized - invalid credentials"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/BeneficiaryAnalytics",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<Object> getBeneficiaryAnalytics(
            @ApiParam(value = "Request Id (UUID format)", required = true) @RequestHeader(value = "requestId", required = true) String requestId,
            @ApiParam(value = "Secret Key") @RequestHeader(value = "SecertKey", required = false) String secertKey,
            @ApiParam(value = "Channel Name", required = true, defaultValue = "FinMobileBankingApp") @RequestHeader(value = "channel", required = true) String channel,
            @ApiParam(value = "Time date Unix Format", required = true) @RequestHeader(value = "timeStamp", required = true) String timeStamp,
            @ApiParam(value = "Entity", required = true) @RequestHeader(value = "entity", required = true) String entity,
            @ApiParam(value = "") @Valid @RequestBody XBeneficiaryAnalyticsRequest beneficiaryAnalytics,
            HttpServletRequest request);
}