package com.csme.csmeapi.fin.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Beneficiary Analytics Request Model
 * Request payload for beneficiary transaction analytics
 */
@ApiModel(description = "Beneficiary Analytics Request")
public class XBeneficiaryAnalyticsRequest {

    @JsonProperty("CorporateId")
    @ApiModelProperty(value = "Corporate ID", required = true, example = "CORP001")
    private String corporateId;

    @JsonProperty("UserId")
    @ApiModelProperty(value = "User ID", required = true, example = "USER001")
    private String userId;

    @JsonProperty("Months")
    @ApiModelProperty(value = "Number of months to analyze (looking back)", required = true, example = "12")
    private Integer months;

    @JsonProperty("TopCount")
    @ApiModelProperty(value = "Number of top beneficiaries to return", example = "50")
    private Integer topCount = 50;

    @JsonProperty("MinTransactionCount")
    @ApiModelProperty(value = "Minimum transaction count filter", example = "5")
    private Integer minTransactionCount = 1;

    @JsonProperty("IncludeAmountAnalysis")
    @ApiModelProperty(value = "Include amount-based analysis", example = "true")
    private Boolean includeAmountAnalysis = true;

    @JsonProperty("SortBy")
    @ApiModelProperty(value = "Sort criteria", example = "TRANSACTION_COUNT", 
                     allowableValues = "TRANSACTION_COUNT,TOTAL_AMOUNT,BENEFICIARY_NAME")
    private String sortBy = "TRANSACTION_COUNT";

    // Constructors
    public XBeneficiaryAnalyticsRequest() {}

    public XBeneficiaryAnalyticsRequest(String corporateId, String userId, Integer months) {
        this.corporateId = corporateId;
        this.userId = userId;
        this.months = months;
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

    public Integer getMonths() {
        return months;
    }

    public void setMonths(Integer months) {
        this.months = months;
    }

    public Integer getTopCount() {
        return topCount;
    }

    public void setTopCount(Integer topCount) {
        this.topCount = topCount;
    }

    public Integer getMinTransactionCount() {
        return minTransactionCount;
    }

    public void setMinTransactionCount(Integer minTransactionCount) {
        this.minTransactionCount = minTransactionCount;
    }

    public Boolean getIncludeAmountAnalysis() {
        return includeAmountAnalysis;
    }

    public void setIncludeAmountAnalysis(Boolean includeAmountAnalysis) {
        this.includeAmountAnalysis = includeAmountAnalysis;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public String toString() {
        return "XBeneficiaryAnalyticsRequest{" +
                "corporateId='" + corporateId + '\'' +
                ", userId='" + userId + '\'' +
                ", months=" + months +
                ", topCount=" + topCount +
                ", minTransactionCount=" + minTransactionCount +
                ", includeAmountAnalysis=" + includeAmountAnalysis +
                ", sortBy='" + sortBy + '\'' +
                '}';
    }
}