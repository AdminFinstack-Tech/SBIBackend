package com.csme.csmeapi.fin.models;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Performance Metrics Model
 * Contains aggregated performance metrics for modules
 */
@ApiModel(description = "Performance Metrics")
public class PerformanceMetrics {

    @JsonProperty("Module")
    @ApiModelProperty(value = "Module name", example = "IMLC")
    private String module;

    @JsonProperty("TotalTransactions")
    @ApiModelProperty(value = "Total number of transactions", example = "150")
    private Integer totalTransactions = 0;

    @JsonProperty("TotalLcAmount")
    @ApiModelProperty(value = "Total LC amount", example = "15000000.00")
    private BigDecimal totalLcAmount = BigDecimal.ZERO;

    @JsonProperty("TotalCollAmount")
    @ApiModelProperty(value = "Total collection amount", example = "8000000.00")
    private BigDecimal totalCollAmount = BigDecimal.ZERO;

    @JsonProperty("TotalGteeAmount")
    @ApiModelProperty(value = "Total guarantee amount", example = "12000000.00")
    private BigDecimal totalGteeAmount = BigDecimal.ZERO;

    @JsonProperty("AverageTransactionValue")
    @ApiModelProperty(value = "Average transaction value", example = "233333.33")
    private BigDecimal averageTransactionValue = BigDecimal.ZERO;

    @JsonProperty("MonthlyGrowthRate")
    @ApiModelProperty(value = "Monthly growth rate percentage", example = "8.5")
    private Double monthlyGrowthRate;

    @JsonProperty("YearlyGrowthRate")
    @ApiModelProperty(value = "Yearly growth rate percentage", example = "25.3")
    private Double yearlyGrowthRate;

    @JsonProperty("MarketShare")
    @ApiModelProperty(value = "Market share percentage", example = "35.2")
    private Double marketShare;

    @JsonProperty("PerformanceScore")
    @ApiModelProperty(value = "Overall performance score", example = "85.5")
    private Double performanceScore;

    @JsonProperty("Trend")
    @ApiModelProperty(value = "Performance trend", example = "INCREASING", allowableValues = "INCREASING,DECREASING,STABLE")
    private String trend;

    // Constructors
    public PerformanceMetrics() {}

    public PerformanceMetrics(String module) {
        this.module = module;
    }

    // Getters and Setters
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
        this.totalTransactions = totalTransactions != null ? totalTransactions : 0;
        calculateAverageTransactionValue();
    }

    public BigDecimal getTotalLcAmount() {
        return totalLcAmount;
    }

    public void setTotalLcAmount(BigDecimal totalLcAmount) {
        this.totalLcAmount = totalLcAmount != null ? totalLcAmount : BigDecimal.ZERO;
        calculateAverageTransactionValue();
    }

    public BigDecimal getTotalCollAmount() {
        return totalCollAmount;
    }

    public void setTotalCollAmount(BigDecimal totalCollAmount) {
        this.totalCollAmount = totalCollAmount != null ? totalCollAmount : BigDecimal.ZERO;
        calculateAverageTransactionValue();
    }

    public BigDecimal getTotalGteeAmount() {
        return totalGteeAmount;
    }

    public void setTotalGteeAmount(BigDecimal totalGteeAmount) {
        this.totalGteeAmount = totalGteeAmount != null ? totalGteeAmount : BigDecimal.ZERO;
        calculateAverageTransactionValue();
    }

    public BigDecimal getAverageTransactionValue() {
        return averageTransactionValue;
    }

    public void setAverageTransactionValue(BigDecimal averageTransactionValue) {
        this.averageTransactionValue = averageTransactionValue;
    }

    public Double getMonthlyGrowthRate() {
        return monthlyGrowthRate;
    }

    public void setMonthlyGrowthRate(Double monthlyGrowthRate) {
        this.monthlyGrowthRate = monthlyGrowthRate;
        updateTrend();
    }

    public Double getYearlyGrowthRate() {
        return yearlyGrowthRate;
    }

    public void setYearlyGrowthRate(Double yearlyGrowthRate) {
        this.yearlyGrowthRate = yearlyGrowthRate;
        updateTrend();
    }

    public Double getMarketShare() {
        return marketShare;
    }

    public void setMarketShare(Double marketShare) {
        this.marketShare = marketShare;
    }

    public Double getPerformanceScore() {
        return performanceScore;
    }

    public void setPerformanceScore(Double performanceScore) {
        this.performanceScore = performanceScore;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    // Utility Methods
    public BigDecimal getTotalAmount() {
        return totalLcAmount.add(totalCollAmount).add(totalGteeAmount);
    }

    private void calculateAverageTransactionValue() {
        if (totalTransactions != null && totalTransactions > 0) {
            BigDecimal total = getTotalAmount();
            this.averageTransactionValue = total.divide(new BigDecimal(totalTransactions), 2, BigDecimal.ROUND_HALF_UP);
        }
    }

    private void updateTrend() {
        if (monthlyGrowthRate != null) {
            if (monthlyGrowthRate > 5.0) {
                this.trend = "INCREASING";
            } else if (monthlyGrowthRate < -5.0) {
                this.trend = "DECREASING";
            } else {
                this.trend = "STABLE";
            }
        }
    }

    public void calculatePerformanceScore() {
        // Simple performance score calculation based on multiple factors
        double score = 50.0; // Base score
        
        if (monthlyGrowthRate != null) {
            score += Math.min(monthlyGrowthRate * 2, 30); // Max 30 points for growth
        }
        
        if (totalTransactions != null && totalTransactions > 0) {
            score += Math.min(totalTransactions / 10.0, 20); // Max 20 points for volume
        }
        
        // Ensure score is between 0 and 100
        this.performanceScore = Math.max(0, Math.min(100, score));
    }

    @Override
    public String toString() {
        return "PerformanceMetrics{" +
                "module='" + module + '\'' +
                ", totalTransactions=" + totalTransactions +
                ", totalLcAmount=" + totalLcAmount +
                ", totalCollAmount=" + totalCollAmount +
                ", totalGteeAmount=" + totalGteeAmount +
                ", averageTransactionValue=" + averageTransactionValue +
                ", monthlyGrowthRate=" + monthlyGrowthRate +
                ", yearlyGrowthRate=" + yearlyGrowthRate +
                ", performanceScore=" + performanceScore +
                ", trend='" + trend + '\'' +
                '}';
    }
}