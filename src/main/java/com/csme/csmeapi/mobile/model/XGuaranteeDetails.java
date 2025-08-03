package com.csme.csmeapi.mobile.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModelProperty;

/**
 * XGuaranteeDetails
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-01-29T10:46:11.400+04:00[Asia/Muscat]")
public class XGuaranteeDetails   {
  @JsonProperty("Typeofguarantee")
  private String typeofguarantee = null;

  @JsonProperty("OtherType")
  private String otherType = null;

  @JsonProperty("PurposeofGuarantee")
  private String purposeofGuarantee = null;
  
@JsonProperty("GuaranateeWording")
  private String guaranateeWording = null;

  @JsonProperty("TextType")
  private String textType = "Standardized Text";

  @JsonProperty("ToBeIssued")
  private OneOfXGuaranteeDetailsToBeIssued toBeIssued = null;

  /**
   * Gets or Sets cashMargin
   */
  public enum CashMarginEnum {
    FULLCOVER("FullCover"),
    
    LIMITS("Limits");

    private String value;

    CashMarginEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static CashMarginEnum fromValue(String text) {
      for (CashMarginEnum b : CashMarginEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("CashMargin")
  private CashMarginEnum cashMargin = null;

  @JsonProperty("LimitPercentage")
  private Integer limitPercentage = null;

  @JsonProperty("Amount")
  private String amount = null;

  @JsonProperty("Currency")
  private String currency = null;

  @JsonProperty("AmountInWords")
  private String amountInWords = null;

  @JsonProperty("ContractValue")
  private String contractValue = null;

  @JsonProperty("ExpiryDate")
  private String expiryDate = null;

  public XGuaranteeDetails typeofguarantee(String typeofguarantee) {
    this.typeofguarantee = typeofguarantee;
    return this;
  }

  /**
   * Type of Guarantee
   * @return typeofguarantee
  **/
  @ApiModelProperty(required = true, value = "Type of Guarantee")
      @NotNull

    public String getTypeofguarantee() {
    return typeofguarantee;
  }

  public void setTypeofguarantee(String typeofguarantee) {
    this.typeofguarantee = typeofguarantee;
  }

  public XGuaranteeDetails otherType(String otherType) {
    this.otherType = otherType;
    return this;
  }

  /**
   * Other Type of Guarantee
   * @return otherType
  **/
  @ApiModelProperty(value = "Other Type of Guarantee")
  
    public String getOtherType() {
    return otherType;
  }

  public void setOtherType(String otherType) {
    this.otherType = otherType;
  }

  public XGuaranteeDetails purposeofGuarantee(String purposeofGuarantee) {
    this.purposeofGuarantee = purposeofGuarantee;
    return this;
  }

  /**
   * Purpose of Guarantee
   * @return purposeofGuarantee
  **/
  @ApiModelProperty(required = true, value = "Purpose of Guarantee")
      @NotNull

    public String getPurposeofGuarantee() {
    return purposeofGuarantee;
  }

  public void setPurposeofGuarantee(String purposeofGuarantee) {
    this.purposeofGuarantee = purposeofGuarantee;
  }
  
  
  

  @ApiModelProperty(required = true, value = "Guarantee Wording")
  @NotNull
  public String getGuaranateeWording() {
	return guaranateeWording;
}

public void setGuaranateeWording(String guaranateeWording) {
	this.guaranateeWording = guaranateeWording;
}


  public XGuaranteeDetails textType(String textType) {
    this.textType = textType;
    return this;
  }

  /**
   * Guarantee Text Type
   * @return textType
  **/
  @ApiModelProperty(value = "Guarantee Text Type")
  
    public String getTextType() {
    return textType;
  }

  public void setTextType(String textType) {
    this.textType = textType;
  }

  public XGuaranteeDetails toBeIssued(OneOfXGuaranteeDetailsToBeIssued toBeIssued) {
    this.toBeIssued = toBeIssued;
    return this;
  }

  /**
   * Polymorphic field for Approval or UnApproval Wording
   * @return toBeIssued
  **/
  @ApiModelProperty(value = "Polymorphic field for Approval or UnApproval Wording")
  
    public OneOfXGuaranteeDetailsToBeIssued getToBeIssued() {
    return toBeIssued;
  }

  public void setToBeIssued(OneOfXGuaranteeDetailsToBeIssued toBeIssued) {
    this.toBeIssued = toBeIssued;
  }

  public XGuaranteeDetails cashMargin(CashMarginEnum cashMargin) {
    this.cashMargin = cashMargin;
    return this;
  }

  /**
   * Get cashMargin
   * @return cashMargin
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public CashMarginEnum getCashMargin() {
    return cashMargin;
  }

  public void setCashMargin(CashMarginEnum cashMargin) {
    this.cashMargin = cashMargin;
  }

  public XGuaranteeDetails limitPercentage(Integer limitPercentage) {
    this.limitPercentage = limitPercentage;
    return this;
  }

  /**
   * Limit Percentage; required if CashMargin = Limits
   * @return limitPercentage
  **/
  @ApiModelProperty(value = "Limit Percentage; required if CashMargin = Limits")
  
    public Integer getLimitPercentage() {
    return limitPercentage;
  }

  public void setLimitPercentage(Integer limitPercentage) {
    this.limitPercentage = limitPercentage;
  }

  public XGuaranteeDetails amount(String amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public XGuaranteeDetails currency(String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public XGuaranteeDetails amountInWords(String amountInWords) {
    this.amountInWords = amountInWords;
    return this;
  }

  /**
   * Amount spelled out in words
   * @return amountInWords
  **/
  @ApiModelProperty(value = "Amount spelled out in words")
  
    public String getAmountInWords() {
    return amountInWords;
  }

  public void setAmountInWords(String amountInWords) {
    this.amountInWords = amountInWords;
  }

  public XGuaranteeDetails contractValue(String contractValue) {
    this.contractValue = contractValue;
    return this;
  }

  /**
   * Contract Value
   * @return contractValue
  **/
  @ApiModelProperty(required = true, value = "Contract Value")
      @NotNull

    public String getContractValue() {
    return contractValue;
  }

  public void setContractValue(String contractValue) {
    this.contractValue = contractValue;
  }

  public XGuaranteeDetails expiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
    return this;
  }

  /**
   * Expiration date of the guarantee
   * @return expiryDate
  **/
  @ApiModelProperty(required = true, value = "Expiration date of the guarantee")
      @NotNull

    public String getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XGuaranteeDetails xguaranteeDetails = (XGuaranteeDetails) o;
    return Objects.equals(this.typeofguarantee, xguaranteeDetails.typeofguarantee) &&
        Objects.equals(this.otherType, xguaranteeDetails.otherType) &&
        Objects.equals(this.purposeofGuarantee, xguaranteeDetails.purposeofGuarantee) &&
        Objects.equals(this.guaranateeWording, xguaranteeDetails.guaranateeWording) &&
        Objects.equals(this.textType, xguaranteeDetails.textType) &&
        Objects.equals(this.toBeIssued, xguaranteeDetails.toBeIssued) &&
        Objects.equals(this.cashMargin, xguaranteeDetails.cashMargin) &&
        Objects.equals(this.limitPercentage, xguaranteeDetails.limitPercentage) &&
        Objects.equals(this.amount, xguaranteeDetails.amount) &&
        Objects.equals(this.currency, xguaranteeDetails.currency) &&
        Objects.equals(this.amountInWords, xguaranteeDetails.amountInWords) &&
        Objects.equals(this.contractValue, xguaranteeDetails.contractValue) &&
        Objects.equals(this.expiryDate, xguaranteeDetails.expiryDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(typeofguarantee, otherType, purposeofGuarantee,guaranateeWording, textType, toBeIssued, cashMargin, limitPercentage, amount, currency, amountInWords, contractValue, expiryDate);
  }

  @Override
public String toString() {
	return "XGuaranteeDetails [typeofguarantee=" + typeofguarantee + ", otherType=" + otherType
			+ ", purposeofGuarantee=" + purposeofGuarantee + ", guaranateeWording=" + guaranateeWording + ", textType="
			+ textType + ", toBeIssued=" + toBeIssued + ", cashMargin=" + cashMargin + ", limitPercentage="
			+ limitPercentage + ", amount=" + amount + ", currency=" + currency + ", amountInWords=" + amountInWords
			+ ", contractValue=" + contractValue + ", expiryDate=" + expiryDate + "]";
}

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
