package com.csme.csmeapi.fin.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Beneficiary Analytics Response Model
 * Response payload containing beneficiary transaction analytics
 */
@ApiModel(description = "Beneficiary Analytics Response")
public class XBeneficiaryAnalyticsResponse {

    @JsonProperty("StatusCode")
    @ApiModelProperty(value = "Response status code", example = "00")
    private String statusCode;

    @JsonProperty("StatusDescription")
    @ApiModelProperty(value = "Response status description", example = "Success")
    private String statusDescription;

    @JsonProperty("BeneficiaryStatistics")
    @ApiModelProperty(value = "List of beneficiary statistics")
    private List<BeneficiaryStatistics> beneficiaryStatistics;

    @JsonProperty("TotalBeneficiaries")
    @ApiModelProperty(value = "Total number of beneficiaries", example = "125")
    private Integer totalBeneficiaries;

    @JsonProperty("TotalTransactions")
    @ApiModelProperty(value = "Total number of transactions", example = "1500")
    private Integer totalTransactions;

    @JsonProperty("AnalyzedPeriod")
    @ApiModelProperty(value = "Period analyzed in months", example = "12")
    private Integer analyzedPeriod;

    @JsonProperty("GeneratedAt")
    @ApiModelProperty(value = "Timestamp when analytics were generated")
    private String generatedAt;

    // Constructors
    public XBeneficiaryAnalyticsResponse() {
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

    public List<BeneficiaryStatistics> getBeneficiaryStatistics() {
        return beneficiaryStatistics;
    }

    public void setBeneficiaryStatistics(List<BeneficiaryStatistics> beneficiaryStatistics) {
        this.beneficiaryStatistics = beneficiaryStatistics;
    }

    public Integer getTotalBeneficiaries() {
        return totalBeneficiaries;
    }

    public void setTotalBeneficiaries(Integer totalBeneficiaries) {
        this.totalBeneficiaries = totalBeneficiaries;
    }

    public Integer getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Integer totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public Integer getAnalyzedPeriod() {
        return analyzedPeriod;
    }

    public void setAnalyzedPeriod(Integer analyzedPeriod) {
        this.analyzedPeriod = analyzedPeriod;
    }

    public String getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(String generatedAt) {
        this.generatedAt = generatedAt;
    }

    @Override
    public String toString() {
        return "XBeneficiaryAnalyticsResponse{" +
                "statusCode='" + statusCode + '\'' +
                ", statusDescription='" + statusDescription + '\'' +
                ", totalBeneficiaries=" + totalBeneficiaries +
                ", totalTransactions=" + totalTransactions +
                ", analyzedPeriod=" + analyzedPeriod +
                ", generatedAt='" + generatedAt + '\'' +
                '}';
    }
}