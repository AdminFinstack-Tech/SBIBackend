package com.csme.csmeapi.fin.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

public class XTransactionAnalyticsRequest {
    
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
    
    @JsonProperty("TransactionType")
    private String transactionType;
    
    @JsonProperty("Currency")
    private String currency;

    // Constructors
    public XTransactionAnalyticsRequest() {}

    public XTransactionAnalyticsRequest(String corporateId, String userId) {
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

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "XTransactionAnalyticsRequest{" +
                "corporateId='" + corporateId + '\'' +
                ", userId='" + userId + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", module='" + module + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}