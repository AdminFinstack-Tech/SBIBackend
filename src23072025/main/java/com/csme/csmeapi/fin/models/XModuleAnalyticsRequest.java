package com.csme.csmeapi.fin.models;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class XModuleAnalyticsRequest {
    
    @NotNull
    @JsonProperty("CorporateId")
    private String corporateId;
    
    @NotNull
    @JsonProperty("UserId")
    private String userId;
    
    @NotNull
    @JsonProperty("Module")
    private String module;
    
    @JsonProperty("Period")
    private String period = "30d";
    
    // Constructors
    public XModuleAnalyticsRequest() {
    }
    
    public XModuleAnalyticsRequest(String corporateId, String userId, String module) {
        this.corporateId = corporateId;
        this.userId = userId;
        this.module = module;
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
    
    @Override
    public String toString() {
        return "XModuleAnalyticsRequest{" +
                "corporateId='" + corporateId + '\'' +
                ", userId='" + userId + '\'' +
                ", module='" + module + '\'' +
                ", period='" + period + '\'' +
                '}';
    }
}