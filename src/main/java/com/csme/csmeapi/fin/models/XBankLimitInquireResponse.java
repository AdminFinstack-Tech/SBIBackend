package com.csme.csmeapi.fin.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * XBankLimitInquireResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XBankLimitInquireResponse   {
  @JsonProperty("MessageId")
  private String messageId = null;

  @JsonProperty("RequestId")
  private String requestId = null;

  @JsonProperty("StatusCode")
  private String statusCode = null;

  @JsonProperty("StatusDescription")
  private String statusDescription = null;

  @JsonProperty("Timestamp")
  private String timestamp = null;

  @JsonProperty("BankLimitResponseInfo")
  @Valid
  private List<XBankLimitResponseInfoObj> bankLimitResponseInfo = null;

  public XBankLimitInquireResponse messageId(String messageId) {
    this.messageId = messageId;
    return this;
  }

  /**
   * Status Code
   * @return messageId
  **/
  @ApiModelProperty(required = true, value = "Status Code")
      @NotNull

    public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public XBankLimitInquireResponse requestId(String requestId) {
    this.requestId = requestId;
    return this;
  }

  /**
   * Request Id
   * @return requestId
  **/
  @ApiModelProperty(required = true, value = "Request Id")
      @NotNull

    public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public XBankLimitInquireResponse statusCode(String statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  /**
   * Status Code
   * @return statusCode
  **/
  @ApiModelProperty(required = true, value = "Status Code")
      @NotNull

    public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  public XBankLimitInquireResponse statusDescription(String statusDescription) {
    this.statusDescription = statusDescription;
    return this;
  }

  /**
   * Status Description
   * @return statusDescription
  **/
  @ApiModelProperty(required = true, value = "Status Description")
      @NotNull

    public String getStatusDescription() {
    return statusDescription;
  }

  public void setStatusDescription(String statusDescription) {
    this.statusDescription = statusDescription;
  }

  public XBankLimitInquireResponse timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Timestamp
   * @return timestamp
  **/
  @ApiModelProperty(required = true, value = "Timestamp")
      @NotNull

    public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public XBankLimitInquireResponse bankLimitResponseInfo(List<XBankLimitResponseInfoObj> bankLimitResponseInfo) {
    this.bankLimitResponseInfo = bankLimitResponseInfo;
    return this;
  }

  public XBankLimitInquireResponse addBankLimitResponseInfoItem(XBankLimitResponseInfoObj bankLimitResponseInfoItem) {
    if (this.bankLimitResponseInfo == null) {
      this.bankLimitResponseInfo = new ArrayList<>();
    }
    this.bankLimitResponseInfo.add(bankLimitResponseInfoItem);
    return this;
  }

  /**
   * Get bankLimitResponseInfo
   * @return bankLimitResponseInfo
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<XBankLimitResponseInfoObj> getBankLimitResponseInfo() {
    return bankLimitResponseInfo;
  }

  public void setBankLimitResponseInfo(List<XBankLimitResponseInfoObj> bankLimitResponseInfo) {
    this.bankLimitResponseInfo = bankLimitResponseInfo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XBankLimitInquireResponse xbankLimitInquireResponse = (XBankLimitInquireResponse) o;
    return Objects.equals(this.messageId, xbankLimitInquireResponse.messageId) &&
        Objects.equals(this.requestId, xbankLimitInquireResponse.requestId) &&
        Objects.equals(this.statusCode, xbankLimitInquireResponse.statusCode) &&
        Objects.equals(this.statusDescription, xbankLimitInquireResponse.statusDescription) &&
        Objects.equals(this.timestamp, xbankLimitInquireResponse.timestamp) &&
        Objects.equals(this.bankLimitResponseInfo, xbankLimitInquireResponse.bankLimitResponseInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, requestId, statusCode, statusDescription, timestamp, bankLimitResponseInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XBankLimitInquireResponse {\n");
    
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    statusDescription: ").append(toIndentedString(statusDescription)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    bankLimitResponseInfo: ").append(toIndentedString(bankLimitResponseInfo)).append("\n");
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
