package com.csme.csmeapi.fin.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class XTransactionAnalyticsResponse {
    
    @JsonProperty("MessageId")
    private String messageId;
    
    @JsonProperty("RequestId")
    private String requestId;
    
    @JsonProperty("StatusCode")
    private String statusCode;
    
    @JsonProperty("StatusDescription")
    private String statusDescription;
    
    @JsonProperty("Timestamp")
    private String timestamp;
    
    @JsonProperty("TransactionAnalytics")
    private TransactionAnalyticsData transactionAnalytics;

    // Nested class for transaction analytics data
    public static class TransactionAnalyticsData {
        
        @JsonProperty("TotalTransactions")
        private Integer totalTransactions;
        
        @JsonProperty("TotalValue")
        private Double totalValue;
        
        @JsonProperty("Currency")
        private String currency;
        
        @JsonProperty("PeriodAnalysis")
        private List<PeriodData> periodAnalysis;
        
        @JsonProperty("ModuleBreakdown")
        private List<ModuleData> moduleBreakdown;
        
        @JsonProperty("StatusDistribution")
        private List<StatusData> statusDistribution;
        
        @JsonProperty("MonthlyTrends")
        private List<MonthlyData> monthlyTrends;

        // Getters and Setters
        public Integer getTotalTransactions() { return totalTransactions; }
        public void setTotalTransactions(Integer totalTransactions) { this.totalTransactions = totalTransactions; }
        
        public Double getTotalValue() { return totalValue; }
        public void setTotalValue(Double totalValue) { this.totalValue = totalValue; }
        
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        
        public List<PeriodData> getPeriodAnalysis() { return periodAnalysis; }
        public void setPeriodAnalysis(List<PeriodData> periodAnalysis) { this.periodAnalysis = periodAnalysis; }
        
        public List<ModuleData> getModuleBreakdown() { return moduleBreakdown; }
        public void setModuleBreakdown(List<ModuleData> moduleBreakdown) { this.moduleBreakdown = moduleBreakdown; }
        
        public List<StatusData> getStatusDistribution() { return statusDistribution; }
        public void setStatusDistribution(List<StatusData> statusDistribution) { this.statusDistribution = statusDistribution; }
        
        public List<MonthlyData> getMonthlyTrends() { return monthlyTrends; }
        public void setMonthlyTrends(List<MonthlyData> monthlyTrends) { this.monthlyTrends = monthlyTrends; }
    }

    public static class PeriodData {
        @JsonProperty("Period")
        private String period;
        @JsonProperty("Count")
        private Integer count;
        @JsonProperty("Value")
        private Double value;

        // Getters and Setters
        public String getPeriod() { return period; }
        public void setPeriod(String period) { this.period = period; }
        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }
        public Double getValue() { return value; }
        public void setValue(Double value) { this.value = value; }
    }

    public static class ModuleData {
        @JsonProperty("Module")
        private String module;
        @JsonProperty("Count")
        private Integer count;
        @JsonProperty("Value")
        private Double value;
        @JsonProperty("Percentage")
        private Double percentage;

        // Getters and Setters
        public String getModule() { return module; }
        public void setModule(String module) { this.module = module; }
        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }
        public Double getValue() { return value; }
        public void setValue(Double value) { this.value = value; }
        public Double getPercentage() { return percentage; }
        public void setPercentage(Double percentage) { this.percentage = percentage; }
    }

    public static class StatusData {
        @JsonProperty("Status")
        private String status;
        @JsonProperty("Count")
        private Integer count;
        @JsonProperty("Percentage")
        private Double percentage;

        // Getters and Setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }
        public Double getPercentage() { return percentage; }
        public void setPercentage(Double percentage) { this.percentage = percentage; }
    }

    public static class MonthlyData {
        @JsonProperty("Month")
        private String month;
        @JsonProperty("Count")
        private Integer count;
        @JsonProperty("Value")
        private Double value;

        // Getters and Setters
        public String getMonth() { return month; }
        public void setMonth(String month) { this.month = month; }
        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }
        public Double getValue() { return value; }
        public void setValue(Double value) { this.value = value; }
    }

    // Main class Getters and Setters
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    
    public String getStatusCode() { return statusCode; }
    public void setStatusCode(String statusCode) { this.statusCode = statusCode; }
    
    public String getStatusDescription() { return statusDescription; }
    public void setStatusDescription(String statusDescription) { this.statusDescription = statusDescription; }
    
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    
    public TransactionAnalyticsData getTransactionAnalytics() { return transactionAnalytics; }
    public void setTransactionAnalytics(TransactionAnalyticsData transactionAnalytics) { this.transactionAnalytics = transactionAnalytics; }
}