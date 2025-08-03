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
 
import com.csme.csmeapi.api.DashboardAnalyticsApi;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XDashboardAnalyticsRequest;
import com.csme.csmeapi.fin.models.XDashboardAnalyticsResponse;
import com.csme.csmeapi.fin.models.XModuleAnalyticsRequest;
import com.csme.csmeapi.fin.models.XModuleAnalyticsResponse;
import com.csme.csmeapi.fin.models.XBeneficiaryAnalyticsRequest;
import com.csme.csmeapi.fin.models.XBeneficiaryAnalyticsResponse;
import com.csme.csmeapi.fin.services.DashboardAnalyticsApiService;
import com.csme.csmeapi.fin.services.ModuleAnalyticsApiService;
import com.csme.csmeapi.fin.services.MobileAppCommonDao;
import com.fasterxml.jackson.databind.ObjectMapper;
 
import io.swagger.annotations.ApiParam;
 
/**
* Dashboard Analytics API Controller
* Provides comprehensive analytics and performance metrics for the mobile banking application
*/
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-01-21T18:21:27.250+04:00[Asia/Muscat]")
@Controller
public class DashboardAnalyticsApiController implements DashboardAnalyticsApi {
 
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
    public DashboardAnalyticsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }
 
    /**
     * Retrieves an optional object mapper instance.
     *
     * @return Optional<ObjectMapper>
     */
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.ofNullable(objectMapper);
    }
 
    /**
     * Retrieves an optional HTTP servlet request instance.
     *
     * @return Optional<HttpServletRequest>
     */
    public Optional<HttpServletRequest> getRequest() {
        return Optional.ofNullable(request);
    }
 
    /**
     * Get overall dashboard analytics with yearly and monthly statistics for all modules
     */
    @Override
    public ResponseEntity<Object> getDashboardAnalytics(
            @ApiParam(value = "Request Id (UUID format)", required = true) @RequestHeader(value = "requestId", required = true) String requestId,
            @ApiParam(value = "Secret Key") @RequestHeader(value = "SecertKey", required = false) String secertKey,
            @ApiParam(value = "Channel Name", required = true, defaultValue = "FinMobileBankingApp") @RequestHeader(value = "channel", required = true) String channel,
            @ApiParam(value = "Time date Unix Format", required = true) @RequestHeader(value = "timeStamp", required = true) Integer timeStamp,
            @ApiParam(value = "Version of API", defaultValue = "1.0.0") @RequestHeader(value = "version", required = false) String version,
            @ApiParam(value = "Dashboard analytics request", required = true) @Valid @RequestBody XDashboardAnalyticsRequest body) {
 
        logger.info("DashboardAnalyticsApiController.getDashboardAnalytics - Start");
        logger.info("Request ID: " + requestId + ", Corporate ID: " + body.getCorporateId() +
                ", User ID: " + body.getUserId());
 
        XDashboardAnalyticsResponse response = new XDashboardAnalyticsResponse();
        String sequenceNumber = "";
        Long auditId = null;
        
        try {
            // Initialize services
            MobileAppCommonDao mobileAppCommonDao = new MobileAppCommonDao();
            DashboardAnalyticsApiService dashboardAnalyticsService = new DashboardAnalyticsApiService();
            
            // Validate request ID to prevent duplicate requests
            if (mobileAppCommonDao.requestAlreadyExists(requestId)) {
                logger.warn("Duplicate request ID detected: " + requestId);
                response.setStatusCode("400");
                response.setStatusDescription("Duplicate request ID");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
 
            // Log audit trail
            auditId = mobileAppCommonDao.saveAuditRecord(new ObjectMapper().writeValueAsString(body),
                    requestId, "DashboardAnalytics");
            sequenceNumber = auditId != null ? auditId.toString() : "";
 
            // Process dashboard analytics request
            response = dashboardAnalyticsService.getDashboardAnalytics(body);
            
            if (response.getStatusCode().equals("00")) {
                logger.info("Dashboard analytics retrieved successfully for Corporate ID: " + body.getCorporateId());
                
                // Update audit trail with success
                if (auditId != null) {
                    mobileAppCommonDao.updateAuditRecord(auditId,
                            new ObjectMapper().writeValueAsString(response), null, HttpStatus.OK);
                }
                        
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                logger.error("Failed to retrieve dashboard analytics: " + response.getStatusDescription());
                
                // Update audit trail with failure
                if (auditId != null) {
                    mobileAppCommonDao.updateAuditRecord(auditId,
                            new ObjectMapper().writeValueAsString(response), response.getStatusDescription(), HttpStatus.BAD_REQUEST);
                }
                        
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
 
        } catch (Exception e) {
            logger.error("Exception in getDashboardAnalytics: " + e.getMessage(), e);
            
            response.setStatusCode("500");
            response.setStatusDescription("Internal server error: " + e.getMessage());
            
            // Update audit trail with error
            if (auditId != null) {
                try {
                    MobileAppCommonDao errorDao = new MobileAppCommonDao();
                    errorDao.updateAuditRecord(auditId,
                            new ObjectMapper().writeValueAsString(response), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                } catch (Exception ex) {
                    logger.error("Error updating audit trail: " + ex.getMessage());
                }
            }
            
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
 
    /**
     * Get module-specific analytics with filtering by module and number of months
     */
    @Override
    public ResponseEntity<Object> getModuleAnalytics(
            @ApiParam(value = "Request Id (UUID format)", required = true) @RequestHeader(value = "requestId", required = true) String requestId,
            @ApiParam(value = "Secret Key") @RequestHeader(value = "SecertKey", required = false) String secertKey,
            @ApiParam(value = "Channel Name", required = true, defaultValue = "FinMobileBankingApp") @RequestHeader(value = "channel", required = true) String channel,
            @ApiParam(value = "Time date Unix Format", required = true) @RequestHeader(value = "timeStamp", required = true) Integer timeStamp,
            @ApiParam(value = "Version of API", defaultValue = "1.0.0") @RequestHeader(value = "version", required = false) String version,
            @ApiParam(value = "Module analytics request", required = true) @Valid @RequestBody XModuleAnalyticsRequest body) {
 
        logger.info("DashboardAnalyticsApiController.getModuleAnalytics - Start");
        logger.info("Request ID: " + requestId + ", Corporate ID: " + body.getCorporateId() +
                ", Module: " + body.getModule() + ", Period: " + body.getPeriod());
 
        XModuleAnalyticsResponse response = new XModuleAnalyticsResponse();
        String sequenceNumber = "";
        Long auditId = null;
        
        try {
            // Initialize services
            MobileAppCommonDao mobileAppCommonDao = new MobileAppCommonDao();
            ModuleAnalyticsApiService moduleAnalyticsService = new ModuleAnalyticsApiService();
            
            // Validate request ID
            if (mobileAppCommonDao.requestAlreadyExists(requestId)) {
                logger.warn("Duplicate request ID detected: " + requestId);
                response.setStatusCode("400");
                response.setStatusDescription("Duplicate request ID");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
 
            // Log audit trail
            auditId = mobileAppCommonDao.saveAuditRecord(new ObjectMapper().writeValueAsString(body),
                    requestId, "ModuleAnalytics");
            sequenceNumber = auditId != null ? auditId.toString() : "";
 
            // Process module analytics request
            response = moduleAnalyticsService.getModuleAnalytics(body, requestId);
            
            if (response.getStatusCode().equals("00")) {
                logger.info("Module analytics retrieved successfully for Module: " + body.getModule());
                
                if (auditId != null) {
                    mobileAppCommonDao.updateAuditRecord(auditId,
                            new ObjectMapper().writeValueAsString(response), null, HttpStatus.OK);
                }
                        
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                logger.error("Failed to retrieve module analytics: " + response.getStatusDescription());
                
                if (auditId != null) {
                    mobileAppCommonDao.updateAuditRecord(auditId,
                            new ObjectMapper().writeValueAsString(response), response.getStatusDescription(), HttpStatus.BAD_REQUEST);
                }
                        
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
 
        } catch (Exception e) {
            logger.error("Exception in getModuleAnalytics: " + e.getMessage(), e);
            
            response.setStatusCode("500");
            response.setStatusDescription("Internal server error: " + e.getMessage());
            
            if (auditId != null) {
                try {
                    MobileAppCommonDao errorDao = new MobileAppCommonDao();
                    errorDao.updateAuditRecord(auditId,
                            new ObjectMapper().writeValueAsString(response), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                } catch (Exception ex) {
                    logger.error("Error updating audit trail: " + ex.getMessage());
                }
            }
            
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
 
    /**
     * Get beneficiary analytics showing top beneficiaries by transaction count
     */
    @Override
    public ResponseEntity<Object> getBeneficiaryAnalytics(
            @ApiParam(value = "Request Id (UUID format)", required = true) @RequestHeader(value = "requestId", required = true) String requestId,
            @ApiParam(value = "Secret Key") @RequestHeader(value = "SecertKey", required = false) String secertKey,
            @ApiParam(value = "Channel Name", required = true, defaultValue = "FinMobileBankingApp") @RequestHeader(value = "channel", required = true) String channel,
            @ApiParam(value = "Time date Unix Format", required = true) @RequestHeader(value = "timeStamp", required = true) Integer timeStamp,
            @ApiParam(value = "Version of API", defaultValue = "1.0.0") @RequestHeader(value = "version", required = false) String version,
            @ApiParam(value = "Beneficiary analytics request", required = true) @Valid @RequestBody XBeneficiaryAnalyticsRequest body) {
 
        logger.info("DashboardAnalyticsApiController.getBeneficiaryAnalytics - Start");
        logger.info("Request ID: " + requestId + ", Corporate ID: " + body.getCorporateId() +
                ", Months: " + body.getMonths());
 
        XBeneficiaryAnalyticsResponse response = new XBeneficiaryAnalyticsResponse();
        String sequenceNumber = "";
        Long auditId = null;
        
        try {
            // Initialize services
            MobileAppCommonDao mobileAppCommonDao = new MobileAppCommonDao();
            DashboardAnalyticsApiService dashboardAnalyticsService = new DashboardAnalyticsApiService();
            
            // Validate request ID
            if (mobileAppCommonDao.requestAlreadyExists(requestId)) {
                logger.warn("Duplicate request ID detected: " + requestId);
                response.setStatusCode("400");
                response.setStatusDescription("Duplicate request ID");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
 
            // Log audit trail
            auditId = mobileAppCommonDao.saveAuditRecord(new ObjectMapper().writeValueAsString(body),
                    requestId, "BeneficiaryAnalytics");
            sequenceNumber = auditId != null ? auditId.toString() : "";
 
            // Process beneficiary analytics request
            response = dashboardAnalyticsService.getBeneficiaryAnalytics(body);
            
            if (response.getStatusCode().equals("00")) {
                logger.info("Beneficiary analytics retrieved successfully");
                
                if (auditId != null) {
                    mobileAppCommonDao.updateAuditRecord(auditId,
                            new ObjectMapper().writeValueAsString(response), null, HttpStatus.OK);
                }
                        
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                logger.error("Failed to retrieve beneficiary analytics: " + response.getStatusDescription());
                
                if (auditId != null) {
                    mobileAppCommonDao.updateAuditRecord(auditId,
                            new ObjectMapper().writeValueAsString(response), response.getStatusDescription(), HttpStatus.BAD_REQUEST);
                }
                        
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
 
        } catch (Exception e) {
            logger.error("Exception in getBeneficiaryAnalytics: " + e.getMessage(), e);
            
            response.setStatusCode("500");
            response.setStatusDescription("Internal server error: " + e.getMessage());
            
            if (auditId != null) {
                try {
                    MobileAppCommonDao errorDao = new MobileAppCommonDao();
                    errorDao.updateAuditRecord(auditId,
                            new ObjectMapper().writeValueAsString(response), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                } catch (Exception ex) {
                    logger.error("Error updating audit trail: " + ex.getMessage());
                }
            }
            
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
 
    /**
     * Get performance summary including key performance metrics and trends
     */
    @Override
    public ResponseEntity<Object> getPerformanceSummary(
            @ApiParam(value = "Request Id (UUID format)", required = true) @RequestHeader(value = "requestId", required = true) String requestId,
            @ApiParam(value = "Secret Key") @RequestHeader(value = "SecertKey", required = false) String secertKey,
            @ApiParam(value = "Channel Name", required = true, defaultValue = "FinMobileBankingApp") @RequestHeader(value = "channel", required = true) String channel,
            @ApiParam(value = "Time date Unix Format", required = true) @RequestHeader(value = "timeStamp", required = true) Integer timeStamp,
            @ApiParam(value = "Version of API", defaultValue = "1.0.0") @RequestHeader(value = "version", required = false) String version,
            @ApiParam(value = "Performance summary request", required = true) @Valid @RequestBody XDashboardAnalyticsRequest body) {
 
        logger.info("DashboardAnalyticsApiController.getPerformanceSummary - Start");
        
        try {
            DashboardAnalyticsApiService dashboardAnalyticsService = new DashboardAnalyticsApiService();
            Object performanceSummary = dashboardAnalyticsService.getPerformanceSummary(body);
            return new ResponseEntity<>(performanceSummary, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception in getPerformanceSummary: " + e.getMessage(), e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
 