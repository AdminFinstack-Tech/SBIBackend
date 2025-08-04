/**
 * BCO Inquire Request Model
 */
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
 * BCO Inquire Request
 */
@ApiModel(description = "BCO Inquire Request")
@Validated
public class XBCOInquireRequest {
  @JsonProperty("corporateId")
  private String corporateId = null;

  @JsonProperty("userId")
  private String userId = null;

  @JsonProperty("transactionType")
  private String transactionType = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("fromDate")
  private String fromDate = null;

  @JsonProperty("toDate")
  private String toDate = null;

  @JsonProperty("branchCode")
  private String branchCode = null;

  @JsonProperty("customerId")
  private String customerId = null;

  @JsonProperty("referenceNo")
  private String referenceNo = null;

  @JsonProperty("pageNumber")
  private Integer pageNumber = 1;

  @JsonProperty("pageSize")
  private Integer pageSize = 10;

  public XBCOInquireRequest corporateId(String corporateId) {
    this.corporateId = corporateId;
    return this;
  }

  /**
   * Corporate ID
   * @return corporateId
  **/
  @ApiModelProperty(required = true, value = "Corporate ID")
  @NotNull
  public String getCorporateId() {
    return corporateId;
  }

  public void setCorporateId(String corporateId) {
    this.corporateId = corporateId;
  }

  public XBCOInquireRequest userId(String userId) {
    this.userId = userId;
    return this;
  }

  /**
   * User ID
   * @return userId
  **/
  @ApiModelProperty(required = true, value = "User ID")
  @NotNull
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public XBCOInquireRequest transactionType(String transactionType) {
    this.transactionType = transactionType;
    return this;
  }

  /**
   * Transaction Type (DSO_REFERAL, CAA, OR)
   * @return transactionType
  **/
  @ApiModelProperty(value = "Transaction Type")
  public String getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
  }

  public XBCOInquireRequest status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Status filter
   * @return status
  **/
  @ApiModelProperty(value = "Status filter")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public XBCOInquireRequest fromDate(String fromDate) {
    this.fromDate = fromDate;
    return this;
  }

  /**
   * From date (yyyy-MM-dd)
   * @return fromDate
  **/
  @ApiModelProperty(value = "From date (yyyy-MM-dd)")
  public String getFromDate() {
    return fromDate;
  }

  public void setFromDate(String fromDate) {
    this.fromDate = fromDate;
  }

  public XBCOInquireRequest toDate(String toDate) {
    this.toDate = toDate;
    return this;
  }

  /**
   * To date (yyyy-MM-dd)
   * @return toDate
  **/
  @ApiModelProperty(value = "To date (yyyy-MM-dd)")
  public String getToDate() {
    return toDate;
  }

  public void setToDate(String toDate) {
    this.toDate = toDate;
  }

  public XBCOInquireRequest branchCode(String branchCode) {
    this.branchCode = branchCode;
    return this;
  }

  /**
   * Branch code filter
   * @return branchCode
  **/
  @ApiModelProperty(value = "Branch code filter")
  public String getBranchCode() {
    return branchCode;
  }

  public void setBranchCode(String branchCode) {
    this.branchCode = branchCode;
  }

  public XBCOInquireRequest customerId(String customerId) {
    this.customerId = customerId;
    return this;
  }

  /**
   * Customer ID filter
   * @return customerId
  **/
  @ApiModelProperty(value = "Customer ID filter")
  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public XBCOInquireRequest referenceNo(String referenceNo) {
    this.referenceNo = referenceNo;
    return this;
  }

  /**
   * Reference number filter
   * @return referenceNo
  **/
  @ApiModelProperty(value = "Reference number filter")
  public String getReferenceNo() {
    return referenceNo;
  }

  public void setReferenceNo(String referenceNo) {
    this.referenceNo = referenceNo;
  }

  public XBCOInquireRequest pageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
    return this;
  }

  /**
   * Page number for pagination
   * @return pageNumber
  **/
  @ApiModelProperty(value = "Page number for pagination")
  public Integer getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
  }

  public XBCOInquireRequest pageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  /**
   * Page size for pagination
   * @return pageSize
  **/
  @ApiModelProperty(value = "Page size for pagination")
  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XBCOInquireRequest xBCOInquireRequest = (XBCOInquireRequest) o;
    return Objects.equals(this.corporateId, xBCOInquireRequest.corporateId) &&
        Objects.equals(this.userId, xBCOInquireRequest.userId) &&
        Objects.equals(this.transactionType, xBCOInquireRequest.transactionType) &&
        Objects.equals(this.status, xBCOInquireRequest.status) &&
        Objects.equals(this.fromDate, xBCOInquireRequest.fromDate) &&
        Objects.equals(this.toDate, xBCOInquireRequest.toDate) &&
        Objects.equals(this.branchCode, xBCOInquireRequest.branchCode) &&
        Objects.equals(this.customerId, xBCOInquireRequest.customerId) &&
        Objects.equals(this.referenceNo, xBCOInquireRequest.referenceNo) &&
        Objects.equals(this.pageNumber, xBCOInquireRequest.pageNumber) &&
        Objects.equals(this.pageSize, xBCOInquireRequest.pageSize);
  }

  @Override
  public int hashCode() {
    return Objects.hash(corporateId, userId, transactionType, status, fromDate, toDate, branchCode, customerId, referenceNo, pageNumber, pageSize);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XBCOInquireRequest {\n");
    
    sb.append("    corporateId: ").append(toIndentedString(corporateId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    transactionType: ").append(toIndentedString(transactionType)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    fromDate: ").append(toIndentedString(fromDate)).append("\n");
    sb.append("    toDate: ").append(toIndentedString(toDate)).append("\n");
    sb.append("    branchCode: ").append(toIndentedString(branchCode)).append("\n");
    sb.append("    customerId: ").append(toIndentedString(customerId)).append("\n");
    sb.append("    referenceNo: ").append(toIndentedString(referenceNo)).append("\n");
    sb.append("    pageNumber: ").append(toIndentedString(pageNumber)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
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