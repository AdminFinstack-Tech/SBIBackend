package com.csme.csmeapi.fin.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Dashboard Analytics Request Model
 * Request payload for comprehensive dashboard analytics
 */
@ApiModel(description = "Dashboard Analytics Request")
public class XDashboardAnalyticsRequest {

    @JsonProperty("CorporateId")
    @ApiModelProperty(value = "Corporate ID", required = true, example = "CORP001")
    private String corporateId;

    @JsonProperty("UserId")
    @ApiModelProperty(value = "User ID", required = true, example = "USER001")
    private String userId;

    @JsonProperty("DateFrom")
    @ApiModelProperty(value = "Start date for analytics (YYYY-MM-DD)", example = "2024-01-01")
    private String dateFrom;

    @JsonProperty("DateTo")
    @ApiModelProperty(value = "End date for analytics (YYYY-MM-DD)", example = "2024-12-31")
    private String dateTo;

    @JsonProperty("IncludeYearlyTrends")
    @ApiModelProperty(value = "Include yearly trend analysis", example = "true")
    private Boolean includeYearlyTrends = true;

    @JsonProperty("IncludeMonthlyBreakdown")
    @ApiModelProperty(value = "Include monthly breakdown", example = "true")
    private Boolean includeMonthlyBreakdown = true;

    @JsonProperty("GroupByCustomer")
    @ApiModelProperty(value = "Group results by customer", example = "false")
    private Boolean groupByCustomer = false;

    // Constructors
    public XDashboardAnalyticsRequest() {}

    public XDashboardAnalyticsRequest(String corporateId, String userId) {
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

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public Boolean getIncludeYearlyTrends() {
        return includeYearlyTrends;
    }

    public void setIncludeYearlyTrends(Boolean includeYearlyTrends) {
        this.includeYearlyTrends = includeYearlyTrends;
    }

    public Boolean getIncludeMonthlyBreakdown() {
        return includeMonthlyBreakdown;
    }

    public void setIncludeMonthlyBreakdown(Boolean includeMonthlyBreakdown) {
        this.includeMonthlyBreakdown = includeMonthlyBreakdown;
    }

    public Boolean getGroupByCustomer() {
        return groupByCustomer;
    }

    public void setGroupByCustomer(Boolean groupByCustomer) {
        this.groupByCustomer = groupByCustomer;
    }

    @Override
    public String toString() {
        return "XDashboardAnalyticsRequest{" +
                "corporateId='" + corporateId + '\'' +
                ", userId='" + userId + '\'' +
                ", dateFrom='" + dateFrom + '\'' +
                ", dateTo='" + dateTo + '\'' +
                ", includeYearlyTrends=" + includeYearlyTrends +
                ", includeMonthlyBreakdown=" + includeMonthlyBreakdown +
                ", groupByCustomer=" + groupByCustomer +
                '}';
    }
}