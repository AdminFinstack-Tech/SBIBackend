package com.csme.csmeapi.fin.models;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Module Statistics Model
 * Contains statistical data for a specific module in a given time period
 */
@ApiModel(description = "Module Statistics")
public class ModuleStatistics {

    @JsonProperty("Module")
    @ApiModelProperty(value = "Module name", example = "IMLC")
    private String module;

    @JsonProperty("Year")
    @ApiModelProperty(value = "Year", example = "2024")
    private String year;

    @JsonProperty("Month")
    @ApiModelProperty(value = "Month", example = "Jan")
    private String month;

    @JsonProperty("Customer")
    @ApiModelProperty(value = "Customer code", example = "CUST001")
    private String customer;

    @JsonProperty("TotalLcAmt")
    @ApiModelProperty(value = "Total LC amount", example = "1000000.00")
    private BigDecimal totalLcAmt = BigDecimal.ZERO;

    @JsonProperty("TotalCollAmt")
    @ApiModelProperty(value = "Total collection amount", example = "500000.00")
    private BigDecimal totalCollAmt = BigDecimal.ZERO;

    @JsonProperty("TotalGteeAmt")
    @ApiModelProperty(value = "Total guarantee amount", example = "750000.00")
    private BigDecimal totalGteeAmt = BigDecimal.ZERO;

    @JsonProperty("LatestExpiry")
    @ApiModelProperty(value = "Latest expiry date")
    private Date latestExpiry;

    @JsonProperty("TransactionCount")
    @ApiModelProperty(value = "Number of transactions", example = "25")
    private Integer transactionCount = 0;

    @JsonProperty("AverageAmount")
    @ApiModelProperty(value = "Average transaction amount", example = "40000.00")
    private BigDecimal averageAmount = BigDecimal.ZERO;

    @JsonProperty("GrowthRate")
    @ApiModelProperty(value = "Growth rate compared to previous period", example = "15.5")
    private Double growthRate;

    // Constructors
    public ModuleStatistics() {}

    public ModuleStatistics(String module, String year, String month, String customer) {
        this.module = module;
        this.year = year;
        this.month = month;
        this.customer = customer;
    }

    // Getters and Setters
    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public BigDecimal getTotalLcAmt() {
        return totalLcAmt;
    }

    public void setTotalLcAmt(BigDecimal totalLcAmt) {
        this.totalLcAmt = totalLcAmt != null ? totalLcAmt : BigDecimal.ZERO;
        calculateAverageAmount();
    }

    public BigDecimal getTotalCollAmt() {
        return totalCollAmt;
    }

    public void setTotalCollAmt(BigDecimal totalCollAmt) {
        this.totalCollAmt = totalCollAmt != null ? totalCollAmt : BigDecimal.ZERO;
    }

    public BigDecimal getTotalGteeAmt() {
        return totalGteeAmt;
    }

    public void setTotalGteeAmt(BigDecimal totalGteeAmt) {
        this.totalGteeAmt = totalGteeAmt != null ? totalGteeAmt : BigDecimal.ZERO;
    }

    public Date getLatestExpiry() {
        return latestExpiry;
    }

    public void setLatestExpiry(Date latestExpiry) {
        this.latestExpiry = latestExpiry;
    }

    public Integer getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount != null ? transactionCount : 0;
        calculateAverageAmount();
    }

    public BigDecimal getAverageAmount() {
        return averageAmount;
    }

    public void setAverageAmount(BigDecimal averageAmount) {
        this.averageAmount = averageAmount;
    }

    public Double getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(Double growthRate) {
        this.growthRate = growthRate;
    }

    // Utility Methods
    public BigDecimal getTotalAmount() {
        return totalLcAmt.add(totalCollAmt).add(totalGteeAmt);
    }

    private void calculateAverageAmount() {
        if (transactionCount != null && transactionCount > 0) {
            BigDecimal total = getTotalAmount();
            this.averageAmount = total.divide(new BigDecimal(transactionCount), 2, BigDecimal.ROUND_HALF_UP);
        }
    }

    public String getPeriodKey() {
        return year + "-" + month;
    }

    @Override
    public String toString() {
        return "ModuleStatistics{" +
                "module='" + module + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", customer='" + customer + '\'' +
                ", totalLcAmt=" + totalLcAmt +
                ", totalCollAmt=" + totalCollAmt +
                ", totalGteeAmt=" + totalGteeAmt +
                ", transactionCount=" + transactionCount +
                ", averageAmount=" + averageAmount +
                ", growthRate=" + growthRate +
                '}';
    }
}