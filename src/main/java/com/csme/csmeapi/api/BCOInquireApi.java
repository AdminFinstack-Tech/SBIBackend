/**
 * BCO (Bank Credit Operations) API Interface
 * Handles all BCO-related inquiries and operations
 */
package com.csme.csmeapi.api;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.csme.csmeapi.fin.models.XBCOInquireRequest;
import com.csme.csmeapi.fin.models.XBCOInquireResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Api(value = "BCOInquire", description = "the BCO Inquire API")
public interface BCOInquireApi {

    Logger log = LoggerFactory.getLogger(BCOInquireApi.class);

    Optional<ObjectMapper> getObjectMapper();

    Optional<HttpServletRequest> getRequest();

    @ApiOperation(value = "BCO Transaction Inquiry", nickname = "bcoInquire", notes = "Retrieve BCO transactions and referrals", response = XBCOInquireResponse.class, authorizations = {
        @Authorization(value = "jwt")    }, tags={ "BCO Operations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = XBCOInquireResponse.class),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/bco/inquire",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<XBCOInquireResponse> bcoInquire(
        @ApiParam(value = "BCO inquiry request body", required=true) @Valid @RequestBody XBCOInquireRequest body,
        @ApiParam(value = "Request ID for tracking", required=true) @RequestHeader(value="Request-Id", required=true) UUID requestId,
        @ApiParam(value = "Sequence number for ordering", required=true) @RequestHeader(value="Sequence", required=true) Long sequence);

    @ApiOperation(value = "BCO DSO Referral", nickname = "bcoDSOReferal", notes = "Submit DSO referral for BCO review", response = XBCOInquireResponse.class, authorizations = {
        @Authorization(value = "jwt")    }, tags={ "BCO Operations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = XBCOInquireResponse.class),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/bco/dso-referal",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<XBCOInquireResponse> bcoDSOReferal(
        @ApiParam(value = "DSO referral request body", required=true) @Valid @RequestBody XBCOInquireRequest body,
        @ApiParam(value = "Request ID for tracking", required=true) @RequestHeader(value="Request-Id", required=true) UUID requestId,
        @ApiParam(value = "Sequence number for ordering", required=true) @RequestHeader(value="Sequence", required=true) Long sequence);
}