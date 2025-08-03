package com.csme.csmeapi.fin.models;

import java.util.Objects;
import com.csme.csmeapi.fin.models.XAttachementInfoObj;
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
 * XAttachmentResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XAttachmentResponse   {
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

  @JsonProperty("AttachementInfo")
  @Valid
  private List<XAttachementInfoObj> attachementInfo = null;

  public XAttachmentResponse messageId(String messageId) {
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

  public XAttachmentResponse requestId(String requestId) {
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

  public XAttachmentResponse statusCode(String statusCode) {
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

  public XAttachmentResponse statusDescription(String statusDescription) {
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

  public XAttachmentResponse timestamp(String timestamp) {
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

  public XAttachmentResponse attachementInfo(List<XAttachementInfoObj> attachementInfo) {
    this.attachementInfo = attachementInfo;
    return this;
  }

  public XAttachmentResponse addAttachementInfoItem(XAttachementInfoObj attachementInfoItem) {
    if (this.attachementInfo == null) {
      this.attachementInfo = new ArrayList<>();
    }
    this.attachementInfo.add(attachementInfoItem);
    return this;
  }

  /**
   * Get attachementInfo
   * @return attachementInfo
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<XAttachementInfoObj> getAttachementInfo() {
    return attachementInfo;
  }

  public void setAttachementInfo(List<XAttachementInfoObj> attachementInfo) {
    this.attachementInfo = attachementInfo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XAttachmentResponse xattachmentResponse = (XAttachmentResponse) o;
    return Objects.equals(this.messageId, xattachmentResponse.messageId) &&
        Objects.equals(this.requestId, xattachmentResponse.requestId) &&
        Objects.equals(this.statusCode, xattachmentResponse.statusCode) &&
        Objects.equals(this.statusDescription, xattachmentResponse.statusDescription) &&
        Objects.equals(this.timestamp, xattachmentResponse.timestamp) &&
        Objects.equals(this.attachementInfo, xattachmentResponse.attachementInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, requestId, statusCode, statusDescription, timestamp, attachementInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XAttachmentResponse {\n");
    
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    statusDescription: ").append(toIndentedString(statusDescription)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    attachementInfo: ").append(toIndentedString(attachementInfo)).append("\n");
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
