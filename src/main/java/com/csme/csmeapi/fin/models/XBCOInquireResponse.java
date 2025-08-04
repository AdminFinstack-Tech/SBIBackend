/**
 * BCO Inquire Response Model
 */
package com.csme.csmeapi.fin.models;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * BCO Inquire Response
 */
@ApiModel(description = "BCO Inquire Response")
@Validated
public class XBCOInquireResponse {
  @JsonProperty("status")
  private String status = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("totalRecords")
  private Integer totalRecords = null;

  @JsonProperty("pageNumber")
  private Integer pageNumber = null;

  @JsonProperty("pageSize")
  private Integer pageSize = null;

  @JsonProperty("transactions")
  @Valid
  private List<XBCOTransaction> transactions = null;

  public XBCOInquireResponse status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Response status
   * @return status
  **/
  @ApiModelProperty(value = "Response status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public XBCOInquireResponse message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Response message
   * @return message
  **/
  @ApiModelProperty(value = "Response message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public XBCOInquireResponse totalRecords(Integer totalRecords) {
    this.totalRecords = totalRecords;
    return this;
  }

  /**
   * Total number of records
   * @return totalRecords
  **/
  @ApiModelProperty(value = "Total number of records")
  public Integer getTotalRecords() {
    return totalRecords;
  }

  public void setTotalRecords(Integer totalRecords) {
    this.totalRecords = totalRecords;
  }

  public XBCOInquireResponse pageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
    return this;
  }

  /**
   * Current page number
   * @return pageNumber
  **/
  @ApiModelProperty(value = "Current page number")
  public Integer getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
  }

  public XBCOInquireResponse pageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  /**
   * Page size
   * @return pageSize
  **/
  @ApiModelProperty(value = "Page size")
  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public XBCOInquireResponse transactions(List<XBCOTransaction> transactions) {
    this.transactions = transactions;
    return this;
  }

  public XBCOInquireResponse addTransactionsItem(XBCOTransaction transactionsItem) {
    if (this.transactions == null) {
      this.transactions = new ArrayList<XBCOTransaction>();
    }
    this.transactions.add(transactionsItem);
    return this;
  }

  /**
   * List of BCO transactions
   * @return transactions
  **/
  @ApiModelProperty(value = "List of BCO transactions")
  @Valid
  public List<XBCOTransaction> getTransactions() {
    return transactions;
  }

  public void setTransactions(List<XBCOTransaction> transactions) {
    this.transactions = transactions;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XBCOInquireResponse xBCOInquireResponse = (XBCOInquireResponse) o;
    return Objects.equals(this.status, xBCOInquireResponse.status) &&
        Objects.equals(this.message, xBCOInquireResponse.message) &&
        Objects.equals(this.totalRecords, xBCOInquireResponse.totalRecords) &&
        Objects.equals(this.pageNumber, xBCOInquireResponse.pageNumber) &&
        Objects.equals(this.pageSize, xBCOInquireResponse.pageSize) &&
        Objects.equals(this.transactions, xBCOInquireResponse.transactions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, message, totalRecords, pageNumber, pageSize, transactions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XBCOInquireResponse {\n");
    
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    totalRecords: ").append(toIndentedString(totalRecords)).append("\n");
    sb.append("    pageNumber: ").append(toIndentedString(pageNumber)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    transactions: ").append(toIndentedString(transactions)).append("\n");
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