/**
 * BCO Transaction Model
 */
package com.csme.csmeapi.fin.models;

import java.util.Objects;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * BCO Transaction
 */
@ApiModel(description = "BCO Transaction")
@Validated
public class XBCOTransaction {
  @JsonProperty("referenceNo")
  private String referenceNo = null;

  @JsonProperty("transactionType")
  private String transactionType = null;

  @JsonProperty("customerName")
  private String customerName = null;

  @JsonProperty("customerId")
  private String customerId = null;

  @JsonProperty("branchCode")
  private String branchCode = null;

  @JsonProperty("branchName")
  private String branchName = null;

  @JsonProperty("observationDate")
  private String observationDate = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("amount")
  private String amount = null;

  @JsonProperty("currency")
  private String currency = null;

  @JsonProperty("createdAt")
  private Date createdAt = null;

  @JsonProperty("updatedAt")
  private Date updatedAt = null;

  @JsonProperty("reasonForReferal")
  private String reasonForReferal = null;

  @JsonProperty("bcoRemarks")
  private String bcoRemarks = null;

  @JsonProperty("approvalStatus")
  private String approvalStatus = null;

  @JsonProperty("approvedBy")
  private String approvedBy = null;

  @JsonProperty("approvedDate")
  private Date approvedDate = null;

  public XBCOTransaction referenceNo(String referenceNo) {
    this.referenceNo = referenceNo;
    return this;
  }

  /**
   * Reference number
   * @return referenceNo
  **/
  @ApiModelProperty(required = true, value = "Reference number")
  @NotNull
  public String getReferenceNo() {
    return referenceNo;
  }

  public void setReferenceNo(String referenceNo) {
    this.referenceNo = referenceNo;
  }

  public XBCOTransaction transactionType(String transactionType) {
    this.transactionType = transactionType;
    return this;
  }

  /**
   * Transaction type (DSO_REFERAL, CAA, OR)
   * @return transactionType
  **/
  @ApiModelProperty(required = true, value = "Transaction type")
  @NotNull
  public String getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
  }

  public XBCOTransaction customerName(String customerName) {
    this.customerName = customerName;
    return this;
  }

  /**
   * Customer name
   * @return customerName
  **/
  @ApiModelProperty(required = true, value = "Customer name")
  @NotNull
  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public XBCOTransaction customerId(String customerId) {
    this.customerId = customerId;
    return this;
  }

  /**
   * Customer ID
   * @return customerId
  **/
  @ApiModelProperty(required = true, value = "Customer ID")
  @NotNull
  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public XBCOTransaction branchCode(String branchCode) {
    this.branchCode = branchCode;
    return this;
  }

  /**
   * Branch code
   * @return branchCode
  **/
  @ApiModelProperty(required = true, value = "Branch code")
  @NotNull
  public String getBranchCode() {
    return branchCode;
  }

  public void setBranchCode(String branchCode) {
    this.branchCode = branchCode;
  }

  public XBCOTransaction branchName(String branchName) {
    this.branchName = branchName;
    return this;
  }

  /**
   * Branch name
   * @return branchName
  **/
  @ApiModelProperty(required = true, value = "Branch name")
  @NotNull
  public String getBranchName() {
    return branchName;
  }

  public void setBranchName(String branchName) {
    this.branchName = branchName;
  }

  public XBCOTransaction observationDate(String observationDate) {
    this.observationDate = observationDate;
    return this;
  }

  /**
   * Observation date
   * @return observationDate
  **/
  @ApiModelProperty(required = true, value = "Observation date")
  @NotNull
  public String getObservationDate() {
    return observationDate;
  }

  public void setObservationDate(String observationDate) {
    this.observationDate = observationDate;
  }

  public XBCOTransaction status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Transaction status
   * @return status
  **/
  @ApiModelProperty(required = true, value = "Transaction status")
  @NotNull
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public XBCOTransaction amount(String amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Transaction amount
   * @return amount
  **/
  @ApiModelProperty(value = "Transaction amount")
  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public XBCOTransaction currency(String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Currency code
   * @return currency
  **/
  @ApiModelProperty(value = "Currency code")
  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public XBCOTransaction createdAt(Date createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Created timestamp
   * @return createdAt
  **/
  @ApiModelProperty(value = "Created timestamp")
  @Valid
  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public XBCOTransaction updatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Updated timestamp
   * @return updatedAt
  **/
  @ApiModelProperty(value = "Updated timestamp")
  @Valid
  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public XBCOTransaction reasonForReferal(String reasonForReferal) {
    this.reasonForReferal = reasonForReferal;
    return this;
  }

  /**
   * Reason for DSO referal
   * @return reasonForReferal
  **/
  @ApiModelProperty(value = "Reason for DSO referal")
  public String getReasonForReferal() {
    return reasonForReferal;
  }

  public void setReasonForReferal(String reasonForReferal) {
    this.reasonForReferal = reasonForReferal;
  }

  public XBCOTransaction bcoRemarks(String bcoRemarks) {
    this.bcoRemarks = bcoRemarks;
    return this;
  }

  /**
   * BCO remarks
   * @return bcoRemarks
  **/
  @ApiModelProperty(value = "BCO remarks")
  public String getBcoRemarks() {
    return bcoRemarks;
  }

  public void setBcoRemarks(String bcoRemarks) {
    this.bcoRemarks = bcoRemarks;
  }

  public XBCOTransaction approvalStatus(String approvalStatus) {
    this.approvalStatus = approvalStatus;
    return this;
  }

  /**
   * Approval status
   * @return approvalStatus
  **/
  @ApiModelProperty(value = "Approval status")
  public String getApprovalStatus() {
    return approvalStatus;
  }

  public void setApprovalStatus(String approvalStatus) {
    this.approvalStatus = approvalStatus;
  }

  public XBCOTransaction approvedBy(String approvedBy) {
    this.approvedBy = approvedBy;
    return this;
  }

  /**
   * Approved by user
   * @return approvedBy
  **/
  @ApiModelProperty(value = "Approved by user")
  public String getApprovedBy() {
    return approvedBy;
  }

  public void setApprovedBy(String approvedBy) {
    this.approvedBy = approvedBy;
  }

  public XBCOTransaction approvedDate(Date approvedDate) {
    this.approvedDate = approvedDate;
    return this;
  }

  /**
   * Approval date
   * @return approvedDate
  **/
  @ApiModelProperty(value = "Approval date")
  @Valid
  public Date getApprovedDate() {
    return approvedDate;
  }

  public void setApprovedDate(Date approvedDate) {
    this.approvedDate = approvedDate;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XBCOTransaction xBCOTransaction = (XBCOTransaction) o;
    return Objects.equals(this.referenceNo, xBCOTransaction.referenceNo) &&
        Objects.equals(this.transactionType, xBCOTransaction.transactionType) &&
        Objects.equals(this.customerName, xBCOTransaction.customerName) &&
        Objects.equals(this.customerId, xBCOTransaction.customerId) &&
        Objects.equals(this.branchCode, xBCOTransaction.branchCode) &&
        Objects.equals(this.branchName, xBCOTransaction.branchName) &&
        Objects.equals(this.observationDate, xBCOTransaction.observationDate) &&
        Objects.equals(this.status, xBCOTransaction.status) &&
        Objects.equals(this.amount, xBCOTransaction.amount) &&
        Objects.equals(this.currency, xBCOTransaction.currency) &&
        Objects.equals(this.createdAt, xBCOTransaction.createdAt) &&
        Objects.equals(this.updatedAt, xBCOTransaction.updatedAt) &&
        Objects.equals(this.reasonForReferal, xBCOTransaction.reasonForReferal) &&
        Objects.equals(this.bcoRemarks, xBCOTransaction.bcoRemarks) &&
        Objects.equals(this.approvalStatus, xBCOTransaction.approvalStatus) &&
        Objects.equals(this.approvedBy, xBCOTransaction.approvedBy) &&
        Objects.equals(this.approvedDate, xBCOTransaction.approvedDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(referenceNo, transactionType, customerName, customerId, branchCode, branchName, observationDate, status, amount, currency, createdAt, updatedAt, reasonForReferal, bcoRemarks, approvalStatus, approvedBy, approvedDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XBCOTransaction {\n");
    
    sb.append("    referenceNo: ").append(toIndentedString(referenceNo)).append("\n");
    sb.append("    transactionType: ").append(toIndentedString(transactionType)).append("\n");
    sb.append("    customerName: ").append(toIndentedString(customerName)).append("\n");
    sb.append("    customerId: ").append(toIndentedString(customerId)).append("\n");
    sb.append("    branchCode: ").append(toIndentedString(branchCode)).append("\n");
    sb.append("    branchName: ").append(toIndentedString(branchName)).append("\n");
    sb.append("    observationDate: ").append(toIndentedString(observationDate)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    reasonForReferal: ").append(toIndentedString(reasonForReferal)).append("\n");
    sb.append("    bcoRemarks: ").append(toIndentedString(bcoRemarks)).append("\n");
    sb.append("    approvalStatus: ").append(toIndentedString(approvalStatus)).append("\n");
    sb.append("    approvedBy: ").append(toIndentedString(approvedBy)).append("\n");
    sb.append("    approvedDate: ").append(toIndentedString(approvedDate)).append("\n");
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