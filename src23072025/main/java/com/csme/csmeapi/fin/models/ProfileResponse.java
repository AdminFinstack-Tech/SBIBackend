package com.csme.csmeapi.fin.models;

import java.util.Objects;
import com.csme.csmeapi.fin.models.XProfileBodyResponse;
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
 * ProfileResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class ProfileResponse   {
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

  @JsonProperty("ProfileBodyResponse")
  @Valid
  private List<XProfileBodyResponse> profileBodyResponse = null;

  public ProfileResponse messageId(String messageId) {
    this.messageId = messageId;
    return this;
  }

  /**
   * Message Id
   * @return messageId
  **/
  @ApiModelProperty(required = true, value = "Message Id")
      @NotNull

    public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public ProfileResponse requestId(String requestId) {
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

  public ProfileResponse statusCode(String statusCode) {
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

  public ProfileResponse statusDescription(String statusDescription) {
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

  public ProfileResponse timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Time stamp
   * @return timestamp
  **/
  @ApiModelProperty(required = true, value = "Time stamp")
      @NotNull

    public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public ProfileResponse profileBodyResponse(List<XProfileBodyResponse> profileBodyResponse) {
    this.profileBodyResponse = profileBodyResponse;
    return this;
  }

  public ProfileResponse addProfileBodyResponseItem(XProfileBodyResponse profileBodyResponseItem) {
    if (this.profileBodyResponse == null) {
      this.profileBodyResponse = new ArrayList<>();
    }
    this.profileBodyResponse.add(profileBodyResponseItem);
    return this;
  }

  /**
   * Get profileBodyResponse
   * @return profileBodyResponse
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<XProfileBodyResponse> getProfileBodyResponse() {
    return profileBodyResponse;
  }

  public void setProfileBodyResponse(List<XProfileBodyResponse> profileBodyResponse) {
    this.profileBodyResponse = profileBodyResponse;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProfileResponse profileResponse = (ProfileResponse) o;
    return Objects.equals(this.messageId, profileResponse.messageId) &&
        Objects.equals(this.requestId, profileResponse.requestId) &&
        Objects.equals(this.statusCode, profileResponse.statusCode) &&
        Objects.equals(this.statusDescription, profileResponse.statusDescription) &&
        Objects.equals(this.timestamp, profileResponse.timestamp) &&
        Objects.equals(this.profileBodyResponse, profileResponse.profileBodyResponse);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, requestId, statusCode, statusDescription, timestamp, profileBodyResponse);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProfileResponse {\n");
    
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    statusDescription: ").append(toIndentedString(statusDescription)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    profileBodyResponse: ").append(toIndentedString(profileBodyResponse)).append("\n");
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
