package com.csme.csmeapi.fin.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * XInfoListObj
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XInfoListObj   {
  @JsonProperty("MainRef")
  private String mainRef = null;

  @JsonProperty("CorporateId")
  private String corporateId = null;

  @JsonProperty("CustomerReference")
  private String customerReference = null;

  @JsonProperty("GuaranteeNumber")
  private String guaranteeNumber = null;

  @JsonProperty("ApplicantName")
  private String applicantName = null;

  @JsonProperty("BeneficiaryName")
  private String beneficiaryName = null;

  @JsonProperty("Currency")
  private String currency = null;

  @JsonProperty("Amount")
  private String amount = null;

  @JsonProperty("Balance")
  private String balance = null;

  @JsonProperty("ExpiryDate")
  private String expiryDate = null;

  @JsonProperty("SystemStatus")
  private String systemStatus = null;

  @JsonProperty("CurrentStatus")
  private String currentStatus = null;

  @JsonProperty("EventTimes")
  private String eventTimes = null;

  @JsonProperty("FunctionName")
  private String functionName = null;

  @JsonProperty("FunctionId")
  private String functionId = null;

  public XInfoListObj mainRef(String mainRef) {
    this.mainRef = mainRef;
    return this;
  }

  /**
   * MainReference number or Etrade Reference Number
   * @return mainRef
  **/
  @ApiModelProperty(value = "MainReference number or Etrade Reference Number")
  
    public String getMainRef() {
    return mainRef;
  }

  public void setMainRef(String mainRef) {
    this.mainRef = mainRef;
  }

  public XInfoListObj corporateId(String corporateId) {
    this.corporateId = corporateId;
    return this;
  }

  /**
   * Corporate Id
   * @return corporateId
  **/
  @ApiModelProperty(value = "Corporate Id")
  
    public String getCorporateId() {
    return corporateId;
  }

  public void setCorporateId(String corporateId) {
    this.corporateId = corporateId;
  }

  public XInfoListObj customerReference(String customerReference) {
    this.customerReference = customerReference;
    return this;
  }

  /**
   * Customer Reference
   * @return customerReference
  **/
  @ApiModelProperty(value = "Customer Reference")
  
    public String getCustomerReference() {
    return customerReference;
  }

  public void setCustomerReference(String customerReference) {
    this.customerReference = customerReference;
  }

  public XInfoListObj guaranteeNumber(String guaranteeNumber) {
    this.guaranteeNumber = guaranteeNumber;
    return this;
  }

  /**
   * Guarantee Number
   * @return guaranteeNumber
  **/
  @ApiModelProperty(value = "Guarantee Number")
  
    public String getGuaranteeNumber() {
    return guaranteeNumber;
  }

  public void setGuaranteeNumber(String guaranteeNumber) {
    this.guaranteeNumber = guaranteeNumber;
  }

  public XInfoListObj applicantName(String applicantName) {
    this.applicantName = applicantName;
    return this;
  }

  /**
   * Applicant Name
   * @return applicantName
  **/
  @ApiModelProperty(value = "Applicant Name")
  
    public String getApplicantName() {
    return applicantName;
  }

  public void setApplicantName(String applicantName) {
    this.applicantName = applicantName;
  }

  public XInfoListObj beneficiaryName(String beneficiaryName) {
    this.beneficiaryName = beneficiaryName;
    return this;
  }

  /**
   * Beneficiary Name
   * @return beneficiaryName
  **/
  @ApiModelProperty(value = "Beneficiary Name")
  
    public String getBeneficiaryName() {
    return beneficiaryName;
  }

  public void setBeneficiaryName(String beneficiaryName) {
    this.beneficiaryName = beneficiaryName;
  }

  public XInfoListObj currency(String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Currency
   * @return currency
  **/
  @ApiModelProperty(value = "Currency")
  
    public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public XInfoListObj amount(String amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Amount
   * @return amount
  **/
  @ApiModelProperty(value = "Amount")
  
    public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public XInfoListObj balance(String balance) {
    this.balance = balance;
    return this;
  }

  /**
   * Balance
   * @return balance
  **/
  @ApiModelProperty(value = "Balance")
  
    public String getBalance() {
    return balance;
  }

  public void setBalance(String balance) {
    this.balance = balance;
  }

  public XInfoListObj expiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
    return this;
  }

  /**
   * Expiry Date
   * @return expiryDate
  **/
  @ApiModelProperty(example = "2022-12-21", value = "Expiry Date")
  
    public String getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
  }

  public XInfoListObj systemStatus(String systemStatus) {
    this.systemStatus = systemStatus;
    return this;
  }

  /**
   * System Status
   * @return systemStatus
  **/
  @ApiModelProperty(value = "System Status")
  
    public String getSystemStatus() {
    return systemStatus;
  }

  public void setSystemStatus(String systemStatus) {
    this.systemStatus = systemStatus;
  }

  public XInfoListObj currentStatus(String currentStatus) {
    this.currentStatus = currentStatus;
    return this;
  }

  /**
   * Current Status
   * @return currentStatus
  **/
  @ApiModelProperty(value = "Current Status")
  
    public String getCurrentStatus() {
    return currentStatus;
  }

  public void setCurrentStatus(String currentStatus) {
    this.currentStatus = currentStatus;
  }

  public XInfoListObj eventTimes(String eventTimes) {
    this.eventTimes = eventTimes;
    return this;
  }

  /**
   * Event Times
   * @return eventTimes
  **/
  @ApiModelProperty(value = "Event Times")
  
    public String getEventTimes() {
    return eventTimes;
  }

  public void setEventTimes(String eventTimes) {
    this.eventTimes = eventTimes;
  }

  public XInfoListObj functionName(String functionName) {
    this.functionName = functionName;
    return this;
  }

  /**
   * Function Name
   * @return functionName
  **/
  @ApiModelProperty(value = "Function Name")
  
    public String getFunctionName() {
    return functionName;
  }

  public void setFunctionName(String functionName) {
    this.functionName = functionName;
  }

  public XInfoListObj functionId(String functionId) {
    this.functionId = functionId;
    return this;
  }

  /**
   * Function Id
   * @return functionId
  **/
  @ApiModelProperty(value = "Function Id")
  
    public String getFunctionId() {
    return functionId;
  }

  public void setFunctionId(String functionId) {
    this.functionId = functionId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XInfoListObj xinfoListObj = (XInfoListObj) o;
    return Objects.equals(this.mainRef, xinfoListObj.mainRef) &&
        Objects.equals(this.corporateId, xinfoListObj.corporateId) &&
        Objects.equals(this.customerReference, xinfoListObj.customerReference) &&
        Objects.equals(this.guaranteeNumber, xinfoListObj.guaranteeNumber) &&
        Objects.equals(this.applicantName, xinfoListObj.applicantName) &&
        Objects.equals(this.beneficiaryName, xinfoListObj.beneficiaryName) &&
        Objects.equals(this.currency, xinfoListObj.currency) &&
        Objects.equals(this.amount, xinfoListObj.amount) &&
        Objects.equals(this.balance, xinfoListObj.balance) &&
        Objects.equals(this.expiryDate, xinfoListObj.expiryDate) &&
        Objects.equals(this.systemStatus, xinfoListObj.systemStatus) &&
        Objects.equals(this.currentStatus, xinfoListObj.currentStatus) &&
        Objects.equals(this.eventTimes, xinfoListObj.eventTimes) &&
        Objects.equals(this.functionName, xinfoListObj.functionName) &&
        Objects.equals(this.functionId, xinfoListObj.functionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mainRef, corporateId, customerReference, guaranteeNumber, applicantName, beneficiaryName, currency, amount, balance, expiryDate, systemStatus, currentStatus, eventTimes, functionName, functionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XInfoListObj {\n");
    
    sb.append("    mainRef: ").append(toIndentedString(mainRef)).append("\n");
    sb.append("    corporateId: ").append(toIndentedString(corporateId)).append("\n");
    sb.append("    customerReference: ").append(toIndentedString(customerReference)).append("\n");
    sb.append("    guaranteeNumber: ").append(toIndentedString(guaranteeNumber)).append("\n");
    sb.append("    applicantName: ").append(toIndentedString(applicantName)).append("\n");
    sb.append("    beneficiaryName: ").append(toIndentedString(beneficiaryName)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    expiryDate: ").append(toIndentedString(expiryDate)).append("\n");
    sb.append("    systemStatus: ").append(toIndentedString(systemStatus)).append("\n");
    sb.append("    currentStatus: ").append(toIndentedString(currentStatus)).append("\n");
    sb.append("    eventTimes: ").append(toIndentedString(eventTimes)).append("\n");
    sb.append("    functionName: ").append(toIndentedString(functionName)).append("\n");
    sb.append("    functionId: ").append(toIndentedString(functionId)).append("\n");
    sb.append("}");
    return sb.toString();
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
