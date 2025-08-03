package com.csme.csmeapi.fin.models;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModelProperty;

/**
 * XAuthorizeResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XAuthorizeResponse   {
  @JsonProperty("EtradeReferenceNumber")
  private String etradeReferenceNumber = null;

  @JsonProperty("MessageId")
  private String messageId = null;

  @JsonProperty("RequestId")
  private String requestId = null;

  @JsonProperty("StatusCode")
  private String statusCode = null;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    SUCCESS("Success"),
    
    FAILURE("Failure");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("Status")
  private StatusEnum status = null;

  @JsonProperty("StatusDescription")
  private String statusDescription = null;

  @JsonProperty("Timestamp")
  private String timestamp = null;

  public XAuthorizeResponse etradeReferenceNumber(String etradeReferenceNumber) {
    this.etradeReferenceNumber = etradeReferenceNumber;
    return this;
  }

  /**
   * E-Trade Reference Number
   * @return etradeReferenceNumber
  **/
  @ApiModelProperty(example = "48800OGTE88132", value = "E-Trade Reference Number")
  
    public String getEtradeReferenceNumber() {
    return etradeReferenceNumber;
  }

  public void setEtradeReferenceNumber(String etradeReferenceNumber) {
    this.etradeReferenceNumber = etradeReferenceNumber;
  }

  public XAuthorizeResponse messageId(String messageId) {
    this.messageId = messageId;
    return this;
  }

  /**
   * Get messageId
   * @return messageId
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public XAuthorizeResponse requestId(String requestId) {
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

  public XAuthorizeResponse statusCode(String statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  /**
   * Get statusCode
   * @return statusCode
  **/
  @ApiModelProperty(example = "success or failure", required = true, value = "")
      @NotNull

    public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  public XAuthorizeResponse status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(value = "")
  
    public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public XAuthorizeResponse statusDescription(String statusDescription) {
    this.statusDescription = statusDescription;
    return this;
  }

  /**
   * Get statusDescription
   * @return statusDescription
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public String getStatusDescription() {
    return statusDescription;
  }

  public void setStatusDescription(String statusDescription) {
    this.statusDescription = statusDescription;
  }

  public XAuthorizeResponse timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XAuthorizeResponse xauthorizeResponse = (XAuthorizeResponse) o;
    return Objects.equals(this.etradeReferenceNumber, xauthorizeResponse.etradeReferenceNumber) &&
        Objects.equals(this.messageId, xauthorizeResponse.messageId) &&
        Objects.equals(this.requestId, xauthorizeResponse.requestId) &&
        Objects.equals(this.statusCode, xauthorizeResponse.statusCode) &&
        Objects.equals(this.status, xauthorizeResponse.status) &&
        Objects.equals(this.statusDescription, xauthorizeResponse.statusDescription) &&
        Objects.equals(this.timestamp, xauthorizeResponse.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(etradeReferenceNumber, messageId, requestId, statusCode, status, statusDescription, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XAuthorizeResponse {\n");
    
    sb.append("    etradeReferenceNumber: ").append(toIndentedString(etradeReferenceNumber)).append("\n");
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    statusDescription: ").append(toIndentedString(statusDescription)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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
