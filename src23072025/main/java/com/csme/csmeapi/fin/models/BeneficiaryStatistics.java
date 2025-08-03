package com.csme.csmeapi.fin.models;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Beneficiary Statistics Model
 * Contains statistical data for beneficiary transactions
 */
@ApiModel(description = "Beneficiary Statistics")
public class BeneficiaryStatistics {

    @JsonProperty("Applicant")
    @ApiModelProperty(value = "Applicant name", example = "ABC Corporation")
    private String applicant;

    @JsonProperty("Beneficiary")
    @ApiModelProperty(value = "Beneficiary name", example = "XYZ Trading Company")
    private String beneficiary;

    @JsonProperty("TransactionCount")
    @ApiModelProperty(value = "Number of transactions", example = "35")
    private Integer transactionCount = 0;

    @JsonProperty("TotalLcAmt")
    @ApiModelProperty(value = "Total LC amount", example = "2500000.00")
    private BigDecimal totalLcAmt = BigDecimal.ZERO;

    @JsonProperty("TotalCollAmt")
    @ApiModelProperty(value = "Total collection amount", example = "1200000.00")
    private BigDecimal totalCollAmt = BigDecimal.ZERO;

    @JsonProperty("TotalGteeAmt")
    @ApiModelProperty(value = "Total guarantee amount", example = "1800000.00")
    private BigDecimal totalGteeAmt = BigDecimal.ZERO;

    @JsonProperty("LastTransactionDate")
    @ApiModelProperty(value = "Date of last transaction")
    private Date lastTransactionDate;

    @JsonProperty("AverageTransactionValue")
    @ApiModelProperty(value = "Average transaction value", example = "157142.86")
    private BigDecimal averageTransactionValue = BigDecimal.ZERO;

    @JsonProperty("RelationshipScore")
    @ApiModelProperty(value = "Relationship strength score", example = "85.5")
    private Double relationshipScore;

    @JsonProperty("FrequencyRank")
    @ApiModelProperty(value = "Frequency ranking", example = "5")
    private Integer frequencyRank;

    @JsonProperty("VolumeRank")
    @ApiModelProperty(value = "Volume ranking", example = "3")
    private Integer volumeRank;

    // Constructors
    public BeneficiaryStatistics() {}

    public BeneficiaryStatistics(String applicant, String beneficiary) {
        this.applicant = applicant;
        this.beneficiary = beneficiary;
    }

    // Getters and Setters
    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Integer getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount != null ? transactionCount : 0;
        calculateAverageTransactionValue();
        calculateRelationshipScore();
    }

    public BigDecimal getTotalLcAmt() {
        return totalLcAmt;
    }

    public void setTotalLcAmt(BigDecimal totalLcAmt) {
        this.totalLcAmt = totalLcAmt != null ? totalLcAmt : BigDecimal.ZERO;
        calculateAverageTransactionValue();
        calculateRelationshipScore();
    }

    public BigDecimal getTotalCollAmt() {
        return totalCollAmt;
    }

    public void setTotalCollAmt(BigDecimal totalCollAmt) {
        this.totalCollAmt = totalCollAmt != null ? totalCollAmt : BigDecimal.ZERO;
        calculateAverageTransactionValue();
        calculateRelationshipScore();
    }

    public BigDecimal getTotalGteeAmt() {
        return totalGteeAmt;
    }

    public void setTotalGteeAmt(BigDecimal totalGteeAmt) {
        this.totalGteeAmt = totalGteeAmt != null ? totalGteeAmt : BigDecimal.ZERO;
        calculateAverageTransactionValue();
        calculateRelationshipScore();
    }

    public Date getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(Date lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
        calculateRelationshipScore();
    }

    public BigDecimal getAverageTransactionValue() {
        return averageTransactionValue;
    }

    public void setAverageTransactionValue(BigDecimal averageTransactionValue) {
        this.averageTransactionValue = averageTransactionValue;
    }

    public Double getRelationshipScore() {
        return relationshipScore;
    }

    public void setRelationshipScore(Double relationshipScore) {
        this.relationshipScore = relationshipScore;
    }

    public Integer getFrequencyRank() {
        return frequencyRank;
    }

    public void setFrequencyRank(Integer frequencyRank) {
        this.frequencyRank = frequencyRank;
    }

    public Integer getVolumeRank() {
        return volumeRank;
    }

    public void setVolumeRank(Integer volumeRank) {
        this.volumeRank = volumeRank;
    }

    // Utility Methods
    public BigDecimal getTotalAmount() {
        return totalLcAmt.add(totalCollAmt).add(totalGteeAmt);
    }

    private void calculateAverageTransactionValue() {
        if (transactionCount != null && transactionCount > 0) {
            BigDecimal total = getTotalAmount();
            this.averageTransactionValue = total.divide(new BigDecimal(transactionCount), 2, BigDecimal.ROUND_HALF_UP);
        }
    }

    private void calculateRelationshipScore() {
        // Calculate relationship score based on transaction frequency, volume, and recency
        double score = 0.0;
        
        // Frequency component (40% of score)
        if (transactionCount != null) {
            score += Math.min(transactionCount * 2.0, 40.0);
        }
        
        // Volume component (40% of score)
        BigDecimal total = getTotalAmount();
        if (total.compareTo(BigDecimal.ZERO) > 0) {
            // Normalize volume (this is a simplified calculation)
            double volumeScore = Math.min(total.doubleValue() / 100000.0, 40.0);
            score += volumeScore;
        }
        
        // Recency component (20% of score)
        if (lastTransactionDate != null) {
            long daysSinceLastTransaction = (System.currentTimeMillis() - lastTransactionDate.getTime()) / (1000 * 60 * 60 * 24);
            if (daysSinceLastTransaction <= 30) {
                score += 20.0;
            } else if (daysSinceLastTransaction <= 90) {
                score += 15.0;
            } else if (daysSinceLastTransaction <= 180) {
                score += 10.0;
            } else {
                score += 5.0;
            }
        }
        
        this.relationshipScore = Math.min(100.0, score);
    }

    public String getRelationshipKey() {
        return applicant + "|" + beneficiary;
    }

    @Override
    public String toString() {
        return "BeneficiaryStatistics{" +
                "applicant='" + applicant + '\'' +
                ", beneficiary='" + beneficiary + '\'' +
                ", transactionCount=" + transactionCount +
                ", totalLcAmt=" + totalLcAmt +
                ", totalCollAmt=" + totalCollAmt +
                ", totalGteeAmt=" + totalGteeAmt +
                ", lastTransactionDate=" + lastTransactionDate +
                ", averageTransactionValue=" + averageTransactionValue +
                ", relationshipScore=" + relationshipScore +
                ", frequencyRank=" + frequencyRank +
                ", volumeRank=" + volumeRank +
                '}';
    }
}