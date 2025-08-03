package com.csme.csmeapi.fin.models;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class XPortfolioValueRequest {
    
    @NotNull
    @JsonProperty("CorporateId")
    private String corporateId;
    
    @NotNull
    @JsonProperty("UserId")
    private String userId;
    
    // Constructors
    public XPortfolioValueRequest() {
    }
    
    public XPortfolioValueRequest(String corporateId, String userId) {
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
    
    @Override
    public String toString() {
        return "XPortfolioValueRequest{" +
                "corporateId='" + corporateId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}