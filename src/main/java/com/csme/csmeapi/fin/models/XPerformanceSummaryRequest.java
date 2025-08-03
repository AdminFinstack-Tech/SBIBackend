package com.csme.csmeapi.fin.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

public class XPerformanceSummaryRequest {
    
    @NotNull
    @JsonProperty("CorporateId")
    private String corporateId;
    
    @NotNull
    @JsonProperty("UserId")
    private String userId;
    
    @JsonProperty("StartDate")
    private String startDate;
    
    @JsonProperty("EndDate")
    private String endDate;
    
    @JsonProperty("Module")
    private String module;
    
    @JsonProperty("Period")
    private String period;
    
    @JsonProperty("IncludeComparison")
    private Boolean includeComparison;
    
    @JsonProperty("MetricTypes")
    private String metricTypes;

    // Constructors
    public XPerformanceSummaryRequest() {}

    public XPerformanceSummaryRequest(String corporateId, String userId) {
        this.corporateId = corporateId;
        this.userId = userId;
    }

    // Getters and Setters
    public String getCorporateId() {
        return corporateId;
    }

    public void setCorporateId(String corporateId) {
        this.corporateId = corporateId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Boolean getIncludeComparison() {
        return includeComparison;
    }

    public void setIncludeComparison(Boolean includeComparison) {
        this.includeComparison = includeComparison;
    }

    public String getMetricTypes() {
        return metricTypes;
    }

    public void setMetricTypes(String metricTypes) {
        this.metricTypes = metricTypes;
    }

    @Override
    public String toString() {
        return "XPerformanceSummaryRequest{" +
                "corporateId='" + corporateId + '\'' +
                ", userId='" + userId + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", module='" + module + '\'' +
                ", period='" + period + '\'' +
                ", includeComparison=" + includeComparison +
                ", metricTypes='" + metricTypes + '\'' +
                '}';
    }
}