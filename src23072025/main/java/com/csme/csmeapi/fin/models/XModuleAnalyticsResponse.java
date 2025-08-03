package com.csme.csmeapi.fin.models;

import java.util.List;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class XModuleAnalyticsResponse {
    
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
    
    @JsonProperty("Module")
    private String module;
    
    @JsonProperty("TotalTransactions")
    private Integer totalTransactions;
    
    @JsonProperty("PendingCount")
    private Integer pendingCount;
    
    @JsonProperty("TotalValue")
    private String totalValue;
    
    @JsonProperty("Currency")
    private String currency;
    
    @JsonProperty("TransactionTrend")
    private List<TransactionTrend> transactionTrend;
    
    @JsonProperty("TopBeneficiaries")
    private List<TopBeneficiary> topBeneficiaries;
    
    // Inner class for TransactionTrend
    public static class TransactionTrend {
        @JsonProperty("month")
        private String month;
        
        @JsonProperty("count")
        private Integer count;
        
        @JsonProperty("value")
        private String value;
        
        // Constructors
        public TransactionTrend() {
        }
        
        public TransactionTrend(String month, Integer count, String value) {
            this.month = month;
            this.count = count;
            this.value = value;
        }
        
        // Getters and Setters
        public String getMonth() {
            return month;
        }
        
        public void setMonth(String month) {
            this.month = month;
        }
        
        public Integer getCount() {
            return count;
        }
        
        public void setCount(Integer count) {
            this.count = count;
        }
        
        public String getValue() {
            return value;
        }
        
        public void setValue(String value) {
            this.value = value;
        }
    }
    
    // Inner class for TopBeneficiary
    public static class TopBeneficiary {
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("count")
        private Integer count;
        
        @JsonProperty("value")
        private String value;
        
        // Constructors
        public TopBeneficiary() {
        }
        
        public TopBeneficiary(String name, Integer count, String value) {
            this.name = name;
            this.count = count;
            this.value = value;
        }
        
        // Getters and Setters
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public Integer getCount() {
            return count;
        }
        
        public void setCount(Integer count) {
            this.count = count;
        }
        
        public String getValue() {
            return value;
        }
        
        public void setValue(String value) {
            this.value = value;
        }
    }
    
    // Constructors
    public XModuleAnalyticsResponse() {
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
    
    public String getModule() {
        return module;
    }
    
    public void setModule(String module) {
        this.module = module;
    }
    
    public Integer getTotalTransactions() {
        return totalTransactions;
    }
    
    public void setTotalTransactions(Integer totalTransactions) {
        this.totalTransactions = totalTransactions;
    }
    
    public Integer getPendingCount() {
        return pendingCount;
    }
    
    public void setPendingCount(Integer pendingCount) {
        this.pendingCount = pendingCount;
    }
    
    public String getTotalValue() {
        return totalValue;
    }
    
    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public List<TransactionTrend> getTransactionTrend() {
        return transactionTrend;
    }
    
    public void setTransactionTrend(List<TransactionTrend> transactionTrend) {
        this.transactionTrend = transactionTrend;
    }
    
    public List<TopBeneficiary> getTopBeneficiaries() {
        return topBeneficiaries;
    }
    
    public void setTopBeneficiaries(List<TopBeneficiary> topBeneficiaries) {
        this.topBeneficiaries = topBeneficiaries;
    }
    
    @Override
    public String toString() {
        return "XModuleAnalyticsResponse{" +
                "messageId='" + messageId + '\'' +
                ", requestId='" + requestId + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", statusDescription='" + statusDescription + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", module='" + module + '\'' +
                ", totalTransactions=" + totalTransactions +
                ", pendingCount=" + pendingCount +
                ", totalValue='" + totalValue + '\'' +
                ", currency='" + currency + '\'' +
                ", transactionTrend=" + transactionTrend +
                ", topBeneficiaries=" + topBeneficiaries +
                '}';
    }
}