package com.csme.csmeapi.fin.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class XPerformanceSummaryResponse {
    
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
    
    @JsonProperty("PerformanceSummary")
    private PerformanceSummaryData performanceSummary;

    // Nested class for performance summary data
    public static class PerformanceSummaryData {
        
        @JsonProperty("OverallScore")
        private Double overallScore;
        
        @JsonProperty("ProcessingMetrics")
        private ProcessingMetrics processingMetrics;
        
        @JsonProperty("EfficiencyMetrics")
        private EfficiencyMetrics efficiencyMetrics;
        
        @JsonProperty("QualityMetrics")
        private QualityMetrics qualityMetrics;
        
        @JsonProperty("ComparisonData")
        private ComparisonData comparisonData;
        
        @JsonProperty("Recommendations")
        private List<String> recommendations;

        // Getters and Setters
        public Double getOverallScore() { return overallScore; }
        public void setOverallScore(Double overallScore) { this.overallScore = overallScore; }
        
        public ProcessingMetrics getProcessingMetrics() { return processingMetrics; }
        public void setProcessingMetrics(ProcessingMetrics processingMetrics) { this.processingMetrics = processingMetrics; }
        
        public EfficiencyMetrics getEfficiencyMetrics() { return efficiencyMetrics; }
        public void setEfficiencyMetrics(EfficiencyMetrics efficiencyMetrics) { this.efficiencyMetrics = efficiencyMetrics; }
        
        public QualityMetrics getQualityMetrics() { return qualityMetrics; }
        public void setQualityMetrics(QualityMetrics qualityMetrics) { this.qualityMetrics = qualityMetrics; }
        
        public ComparisonData getComparisonData() { return comparisonData; }
        public void setComparisonData(ComparisonData comparisonData) { this.comparisonData = comparisonData; }
        
        public List<String> getRecommendations() { return recommendations; }
        public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
    }

    public static class ProcessingMetrics {
        @JsonProperty("AverageProcessingTime")
        private Double averageProcessingTime;
        @JsonProperty("MedianProcessingTime")
        private Double medianProcessingTime;
        @JsonProperty("FastestProcessingTime")
        private Double fastestProcessingTime;
        @JsonProperty("SlowestProcessingTime")
        private Double slowestProcessingTime;
        @JsonProperty("TimeUnit")
        private String timeUnit;

        // Getters and Setters
        public Double getAverageProcessingTime() { return averageProcessingTime; }
        public void setAverageProcessingTime(Double averageProcessingTime) { this.averageProcessingTime = averageProcessingTime; }
        public Double getMedianProcessingTime() { return medianProcessingTime; }
        public void setMedianProcessingTime(Double medianProcessingTime) { this.medianProcessingTime = medianProcessingTime; }
        public Double getFastestProcessingTime() { return fastestProcessingTime; }
        public void setFastestProcessingTime(Double fastestProcessingTime) { this.fastestProcessingTime = fastestProcessingTime; }
        public Double getSlowestProcessingTime() { return slowestProcessingTime; }
        public void setSlowestProcessingTime(Double slowestProcessingTime) { this.slowestProcessingTime = slowestProcessingTime; }
        public String getTimeUnit() { return timeUnit; }
        public void setTimeUnit(String timeUnit) { this.timeUnit = timeUnit; }
    }

    public static class EfficiencyMetrics {
        @JsonProperty("ApprovalRate")
        private Double approvalRate;
        @JsonProperty("RejectionRate")
        private Double rejectionRate;
        @JsonProperty("StraightThroughProcessingRate")
        private Double straightThroughProcessingRate;
        @JsonProperty("ReturnRate")
        private Double returnRate;
        @JsonProperty("AutomationRate")
        private Double automationRate;

        // Getters and Setters
        public Double getApprovalRate() { return approvalRate; }
        public void setApprovalRate(Double approvalRate) { this.approvalRate = approvalRate; }
        public Double getRejectionRate() { return rejectionRate; }
        public void setRejectionRate(Double rejectionRate) { this.rejectionRate = rejectionRate; }
        public Double getStraightThroughProcessingRate() { return straightThroughProcessingRate; }
        public void setStraightThroughProcessingRate(Double straightThroughProcessingRate) { this.straightThroughProcessingRate = straightThroughProcessingRate; }
        public Double getReturnRate() { return returnRate; }
        public void setReturnRate(Double returnRate) { this.returnRate = returnRate; }
        public Double getAutomationRate() { return automationRate; }
        public void setAutomationRate(Double automationRate) { this.automationRate = automationRate; }
    }

    public static class QualityMetrics {
        @JsonProperty("AccuracyScore")
        private Double accuracyScore;
        @JsonProperty("ComplianceScore")
        private Double complianceScore;
        @JsonProperty("ErrorRate")
        private Double errorRate;
        @JsonProperty("ReworkRate")
        private Double reworkRate;
        @JsonProperty("CustomerSatisfactionScore")
        private Double customerSatisfactionScore;

        // Getters and Setters
        public Double getAccuracyScore() { return accuracyScore; }
        public void setAccuracyScore(Double accuracyScore) { this.accuracyScore = accuracyScore; }
        public Double getComplianceScore() { return complianceScore; }
        public void setComplianceScore(Double complianceScore) { this.complianceScore = complianceScore; }
        public Double getErrorRate() { return errorRate; }
        public void setErrorRate(Double errorRate) { this.errorRate = errorRate; }
        public Double getReworkRate() { return reworkRate; }
        public void setReworkRate(Double reworkRate) { this.reworkRate = reworkRate; }
        public Double getCustomerSatisfactionScore() { return customerSatisfactionScore; }
        public void setCustomerSatisfactionScore(Double customerSatisfactionScore) { this.customerSatisfactionScore = customerSatisfactionScore; }
    }

    public static class ComparisonData {
        @JsonProperty("PreviousPeriodScore")
        private Double previousPeriodScore;
        @JsonProperty("ScoreChange")
        private Double scoreChange;
        @JsonProperty("PercentageChange")
        private Double percentageChange;
        @JsonProperty("Trend")
        private String trend;
        @JsonProperty("BenchmarkScore")
        private Double benchmarkScore;

        // Getters and Setters
        public Double getPreviousPeriodScore() { return previousPeriodScore; }
        public void setPreviousPeriodScore(Double previousPeriodScore) { this.previousPeriodScore = previousPeriodScore; }
        public Double getScoreChange() { return scoreChange; }
        public void setScoreChange(Double scoreChange) { this.scoreChange = scoreChange; }
        public Double getPercentageChange() { return percentageChange; }
        public void setPercentageChange(Double percentageChange) { this.percentageChange = percentageChange; }
        public String getTrend() { return trend; }
        public void setTrend(String trend) { this.trend = trend; }
        public Double getBenchmarkScore() { return benchmarkScore; }
        public void setBenchmarkScore(Double benchmarkScore) { this.benchmarkScore = benchmarkScore; }
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
    
    public PerformanceSummaryData getPerformanceSummary() { return performanceSummary; }
    public void setPerformanceSummary(PerformanceSummaryData performanceSummary) { this.performanceSummary = performanceSummary; }
}