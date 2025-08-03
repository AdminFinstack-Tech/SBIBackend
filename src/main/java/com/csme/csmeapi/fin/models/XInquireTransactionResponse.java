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
 * XInquireTransactionResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XInquireTransactionResponse   {
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

  @JsonProperty("InquiryTrxInfo")
  @Valid
  private List<InquiryTrxInfoListObj> inquiryTrxInfo = null;

  @JsonProperty("AttachmentInfo")
  @Valid
  private List<AttachmentInfoListObj> attachmentInfo = null;

  public XInquireTransactionResponse messageId(String messageId) {
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

  public XInquireTransactionResponse requestId(String requestId) {
    this.requestId = requestId;
    return this;
  }

  /**
   * Request Id
   * @return requestId
  **/
  @ApiModelProperty(value = "Request Id")
  
    public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public XInquireTransactionResponse statusCode(String statusCode) {
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

  public XInquireTransactionResponse statusDescription(String statusDescription) {
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

  public XInquireTransactionResponse timestamp(String timestamp) {
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

  public XInquireTransactionResponse inquiryTrxInfo(List<InquiryTrxInfoListObj> inquiryTrxInfo) {
    this.inquiryTrxInfo = inquiryTrxInfo;
    return this;
  }

  public XInquireTransactionResponse addInquiryTrxInfoItem(InquiryTrxInfoListObj inquiryTrxInfoItem) {
    if (this.inquiryTrxInfo == null) {
      this.inquiryTrxInfo = new ArrayList<>();
    }
    this.inquiryTrxInfo.add(inquiryTrxInfoItem);
    return this;
  }

  /**
   * Get inquiryTrxInfo
   * @return inquiryTrxInfo
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<InquiryTrxInfoListObj> getInquiryTrxInfo() {
    return inquiryTrxInfo;
  }

  public void setInquiryTrxInfo(List<InquiryTrxInfoListObj> inquiryTrxInfo) {
    this.inquiryTrxInfo = inquiryTrxInfo;
  }

  public XInquireTransactionResponse attachmentInfo(List<AttachmentInfoListObj> attachmentInfo) {
    this.attachmentInfo = attachmentInfo;
    return this;
  }

  public XInquireTransactionResponse addAttachmentInfoItem(AttachmentInfoListObj attachmentInfoItem) {
    if (this.attachmentInfo == null) {
      this.attachmentInfo = new ArrayList<>();
    }
    this.attachmentInfo.add(attachmentInfoItem);
    return this;
  }

  /**
   * Get attachmentInfo
   * @return attachmentInfo
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<AttachmentInfoListObj> getAttachmentInfo() {
    return attachmentInfo;
  }

  public void setAttachmentInfo(List<AttachmentInfoListObj> attachmentInfo) {
    this.attachmentInfo = attachmentInfo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XInquireTransactionResponse xinquireTransactionResponse = (XInquireTransactionResponse) o;
    return Objects.equals(this.messageId, xinquireTransactionResponse.messageId) &&
        Objects.equals(this.requestId, xinquireTransactionResponse.requestId) &&
        Objects.equals(this.statusCode, xinquireTransactionResponse.statusCode) &&
        Objects.equals(this.statusDescription, xinquireTransactionResponse.statusDescription) &&
        Objects.equals(this.timestamp, xinquireTransactionResponse.timestamp) &&
        Objects.equals(this.inquiryTrxInfo, xinquireTransactionResponse.inquiryTrxInfo) &&
        Objects.equals(this.attachmentInfo, xinquireTransactionResponse.attachmentInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, requestId, statusCode, statusDescription, timestamp, inquiryTrxInfo, attachmentInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XInquireTransactionResponse {\n");
    
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    statusDescription: ").append(toIndentedString(statusDescription)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    inquiryTrxInfo: ").append(toIndentedString(inquiryTrxInfo)).append("\n");
    sb.append("    attachmentInfo: ").append(toIndentedString(attachmentInfo)).append("\n");
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
