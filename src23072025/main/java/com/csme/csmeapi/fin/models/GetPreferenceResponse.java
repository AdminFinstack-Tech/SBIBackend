package com.csme.csmeapi.fin.models;

import java.util.Objects;
import com.csme.csmeapi.fin.models.XPreferenceResponseObj;
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
 * GetPreferenceResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class GetPreferenceResponse   {
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

  @JsonProperty("SavePreferenceRespInfo")
  @Valid
  private List<XPreferenceResponseObj> savePreferenceRespInfo = null;

  public GetPreferenceResponse messageId(String messageId) {
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

  public GetPreferenceResponse requestId(String requestId) {
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

  public GetPreferenceResponse statusCode(String statusCode) {
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

  public GetPreferenceResponse statusDescription(String statusDescription) {
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

  public GetPreferenceResponse timestamp(String timestamp) {
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

  public GetPreferenceResponse savePreferenceRespInfo(List<XPreferenceResponseObj> savePreferenceRespInfo) {
    this.savePreferenceRespInfo = savePreferenceRespInfo;
    return this;
  }

  public GetPreferenceResponse addSavePreferenceRespInfoItem(XPreferenceResponseObj savePreferenceRespInfoItem) {
    if (this.savePreferenceRespInfo == null) {
      this.savePreferenceRespInfo = new ArrayList<>();
    }
    this.savePreferenceRespInfo.add(savePreferenceRespInfoItem);
    return this;
  }

  /**
   * Get savePreferenceRespInfo
   * @return savePreferenceRespInfo
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<XPreferenceResponseObj> getSavePreferenceRespInfo() {
    return savePreferenceRespInfo;
  }

  public void setSavePreferenceRespInfo(List<XPreferenceResponseObj> savePreferenceRespInfo) {
    this.savePreferenceRespInfo = savePreferenceRespInfo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetPreferenceResponse getPreferenceResponse = (GetPreferenceResponse) o;
    return Objects.equals(this.messageId, getPreferenceResponse.messageId) &&
        Objects.equals(this.requestId, getPreferenceResponse.requestId) &&
        Objects.equals(this.statusCode, getPreferenceResponse.statusCode) &&
        Objects.equals(this.statusDescription, getPreferenceResponse.statusDescription) &&
        Objects.equals(this.timestamp, getPreferenceResponse.timestamp) &&
        Objects.equals(this.savePreferenceRespInfo, getPreferenceResponse.savePreferenceRespInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, requestId, statusCode, statusDescription, timestamp, savePreferenceRespInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetPreferenceResponse {\n");
    
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    statusDescription: ").append(toIndentedString(statusDescription)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    savePreferenceRespInfo: ").append(toIndentedString(savePreferenceRespInfo)).append("\n");
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
