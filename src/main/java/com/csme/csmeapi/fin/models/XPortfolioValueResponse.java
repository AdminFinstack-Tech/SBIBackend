package com.csme.csmeapi.fin.models;

import java.util.List;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class XPortfolioValueResponse {
    
    @NotNull
    @JsonProperty("MessageId")
    private String messageId;
    
    @NotNull
    @JsonProperty("RequestId")
    private String requestId;
    
    @NotNull
    @JsonProperty("StatusCode")
    private String statusCode;
    
    @NotNull
    @JsonProperty("StatusDescription")
    private String statusDescription;
    
    @NotNull
    @JsonProperty("Timestamp")
    private String timestamp;
    
    @JsonProperty("Currency")
    private String currency;
    
    @JsonProperty("TotalValue")
    private String totalValue;
    
    @JsonProperty("AsOfDate")
    private String asOfDate;
    
    @JsonProperty("AccountBreakdown")
    private List<AccountBreakdown> accountBreakdown;
    
    // Inner class for AccountBreakdown
    public static class AccountBreakdown {
        @JsonProperty("accountNumber")
        private String accountNumber;
        
        @JsonProperty("accountType")
        private String accountType;
        
        @JsonProperty("balance")
        private String balance;
        
        @JsonProperty("currency")
        private String currency;
        
        // Constructors
        public AccountBreakdown() {
        }
        
        public AccountBreakdown(String accountNumber, String accountType, String balance, String currency) {
            this.accountNumber = accountNumber;
            this.accountType = accountType;
            this.balance = balance;
            this.currency = currency;
        }
        
        // Getters and Setters
        public String getAccountNumber() {
            return accountNumber;
        }
        
        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }
        
        public String getAccountType() {
            return accountType;
        }
        
        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }
        
        public String getBalance() {
            return balance;
        }
        
        public void setBalance(String balance) {
            this.balance = balance;
        }
        
        public String getCurrency() {
            return currency;
        }
        
        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }
    
    // Constructors
    public XPortfolioValueResponse() {
    }
    
    // Getters and Setters
    public String getMessageId() {
        return messageId;
    }
    
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
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
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getTotalValue() {
        return totalValue;
    }
    
    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }
    
    public String getAsOfDate() {
        return asOfDate;
    }
    
    public void setAsOfDate(String asOfDate) {
        this.asOfDate = asOfDate;
    }
    
    public List<AccountBreakdown> getAccountBreakdown() {
        return accountBreakdown;
    }
    
    public void setAccountBreakdown(List<AccountBreakdown> accountBreakdown) {
        this.accountBreakdown = accountBreakdown;
    }
    
    @Override
    public String toString() {
        return "XPortfolioValueResponse{" +
                "messageId='" + messageId + '\'' +
                ", requestId='" + requestId + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", statusDescription='" + statusDescription + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", currency='" + currency + '\'' +
                ", totalValue='" + totalValue + '\'' +
                ", asOfDate='" + asOfDate + '\'' +
                ", accountBreakdown=" + accountBreakdown +
                '}';
    }
}