package com.csme.csmeapi.fin.models;

import java.util.Objects;
import com.csme.csmeapi.fin.models.XAuthUserListInfoObj;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * XAuthUserListResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XAuthUserListResponse   {
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

  @JsonProperty("InquiryAuthUserInfo")
  @Valid
  private List<XAuthUserListInfoObj> inquiryAuthUserInfo = null;

  public XAuthUserListResponse messageId(String messageId) {
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

  public XAuthUserListResponse requestId(String requestId) {
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

  public XAuthUserListResponse statusCode(String statusCode) {
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

  public XAuthUserListResponse statusDescription(String statusDescription) {
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

  public XAuthUserListResponse timestamp(String timestamp) {
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

  public XAuthUserListResponse inquiryAuthUserInfo(List<XAuthUserListInfoObj> inquiryAuthUserInfo) {
    this.inquiryAuthUserInfo = inquiryAuthUserInfo;
    return this;
  }

  public XAuthUserListResponse addInquiryAuthUserInfoItem(XAuthUserListInfoObj inquiryAuthUserInfoItem) {
    if (this.inquiryAuthUserInfo == null) {
      this.inquiryAuthUserInfo = new ArrayList<>();
    }
    this.inquiryAuthUserInfo.add(inquiryAuthUserInfoItem);
    return this;
  }

  /**
   * Get inquiryAuthUserInfo
   * @return inquiryAuthUserInfo
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<XAuthUserListInfoObj> getInquiryAuthUserInfo() {
    return inquiryAuthUserInfo;
  }

  public void setInquiryAuthUserInfo(List<XAuthUserListInfoObj> inquiryAuthUserInfo) {
    this.inquiryAuthUserInfo = inquiryAuthUserInfo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XAuthUserListResponse xauthUserListResponse = (XAuthUserListResponse) o;
    return Objects.equals(this.messageId, xauthUserListResponse.messageId) &&
        Objects.equals(this.requestId, xauthUserListResponse.requestId) &&
        Objects.equals(this.statusCode, xauthUserListResponse.statusCode) &&
        Objects.equals(this.statusDescription, xauthUserListResponse.statusDescription) &&
        Objects.equals(this.timestamp, xauthUserListResponse.timestamp) &&
        Objects.equals(this.inquiryAuthUserInfo, xauthUserListResponse.inquiryAuthUserInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, requestId, statusCode, statusDescription, timestamp, inquiryAuthUserInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XAuthUserListResponse {\n");
    
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    statusDescription: ").append(toIndentedString(statusDescription)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    inquiryAuthUserInfo: ").append(toIndentedString(inquiryAuthUserInfo)).append("\n");
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
