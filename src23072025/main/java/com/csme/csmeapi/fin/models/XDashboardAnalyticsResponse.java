package com.csme.csmeapi.fin.models;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Dashboard Analytics Response Model
 * Response payload containing comprehensive dashboard analytics
 */
@ApiModel(description = "Dashboard Analytics Response")
public class XDashboardAnalyticsResponse {

    @JsonProperty("StatusCode")
    @ApiModelProperty(value = "Response status code", example = "00")
    private String statusCode;

    @JsonProperty("StatusDescription")
    @ApiModelProperty(value = "Response status description", example = "Success")
    private String statusDescription;

    @JsonProperty("ModuleStatistics")
    @ApiModelProperty(value = "List of module statistics")
    private List<ModuleStatistics> moduleStatistics;

    @JsonProperty("PerformanceMetrics")
    @ApiModelProperty(value = "Performance metrics by module")
    private List<PerformanceMetrics> performanceMetrics;

    @JsonProperty("SummaryMetrics")
    @ApiModelProperty(value = "Overall summary metrics")
    private Map<String, Object> summaryMetrics;

    @JsonProperty("TotalRecords")
    @ApiModelProperty(value = "Total number of records returned", example = "150")
    private Integer totalRecords;

    @JsonProperty("GeneratedAt")
    @ApiModelProperty(value = "Timestamp when analytics were generated")
    private String generatedAt;

    @JsonProperty("AnalyticsPeriod")
    @ApiModelProperty(value = "Period covered by analytics")
    private String analyticsPeriod;

    // Constructors
    public XDashboardAnalyticsResponse() {
        this.generatedAt = java.time.LocalDateTime.now().toString();
    }

    // Getters and Setters
    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public List<ModuleStatistics> getModuleStatistics() {
        return moduleStatistics;
    }

    public void setModuleStatistics(List<ModuleStatistics> moduleStatistics) {
        this.moduleStatistics = moduleStatistics;
    }

    public List<PerformanceMetrics> getPerformanceMetrics() {
        return performanceMetrics;
    }

    public void setPerformanceMetrics(List<PerformanceMetrics> performanceMetrics) {
        this.performanceMetrics = performanceMetrics;
    }

    public Map<String, Object> getSummaryMetrics() {
        return summaryMetrics;
    }

    public void setSummaryMetrics(Map<String, Object> summaryMetrics) {
        this.summaryMetrics = summaryMetrics;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public String getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(String generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getAnalyticsPeriod() {
        return analyticsPeriod;
    }

    public void setAnalyticsPeriod(String analyticsPeriod) {
        this.analyticsPeriod = analyticsPeriod;
    }

    @Override
    public String toString() {
        return "XDashboardAnalyticsResponse{" +
                "statusCode='" + statusCode + '\'' +
                ", statusDescription='" + statusDescription + '\'' +
                ", totalRecords=" + totalRecords +
                ", generatedAt='" + generatedAt + '\'' +
                ", analyticsPeriod='" + analyticsPeriod + '\'' +
                '}';
    }
}