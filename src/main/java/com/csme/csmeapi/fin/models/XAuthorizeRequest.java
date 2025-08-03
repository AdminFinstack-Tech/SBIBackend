package com.csme.csmeapi.fin.models;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModelProperty;

/**
 * XAuthorizeRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XAuthorizeRequest   {
  @JsonProperty("CorporateId")
  private String corporateId = null;

  @JsonProperty("UserId")
  private String userId = null;

  @JsonProperty("MainRef")
  private String mainRef = null;

  @JsonProperty("AuthorizerDate")
  private String authorizerDate = null;

  @JsonProperty("AuthorizerTime")
  private String authorizerTime = null;

  @JsonProperty("AuthorizerBy")
  private String authorizerBy = null;

  /**
   * Action
   */
  public enum ActionEnum {
    ACCEPTED("Accepted"),
    
    REJECTED("Rejected");

    private String value;

    ActionEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ActionEnum fromValue(String text) {
      for (ActionEnum b : ActionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("Action")
  private ActionEnum action = null;

  @JsonProperty("RejectedReason")
  private String rejectedReason = null;

  public XAuthorizeRequest corporateId(String corporateId) {
    this.corporateId = corporateId;
    return this;
  }

  /**
   * Corporate Id
   * @return corporateId
  **/
  @ApiModelProperty(value = "Corporate Id")
  
    public String getCorporateId() {
    return corporateId;
  }

  public void setCorporateId(String corporateId) {
    this.corporateId = corporateId;
  }

  public XAuthorizeRequest userId(String userId) {
    this.userId = userId;
    return this;
  }

  /**
   * User Id
   * @return userId
  **/
  @ApiModelProperty(value = "User Id")
  
    public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public XAuthorizeRequest mainRef(String mainRef) {
    this.mainRef = mainRef;
    return this;
  }

  /**
   * Main Reference Number
   * @return mainRef
  **/
  @ApiModelProperty(value = "Main Reference Number")
  
    public String getMainRef() {
    return mainRef;
  }

  public void setMainRef(String mainRef) {
    this.mainRef = mainRef;
  }

  public XAuthorizeRequest authorizerDate(String authorizerDate) {
    this.authorizerDate = authorizerDate;
    return this;
  }

  /**
   * AuthorizerDate
   * @return authorizerDate
  **/
  @ApiModelProperty(example = "2022-12-21", value = "AuthorizerDate")
  
    public String getAuthorizerDate() {
    return authorizerDate;
  }

  public void setAuthorizerDate(String authorizerDate) {
    this.authorizerDate = authorizerDate;
  }

  public XAuthorizeRequest authorizerTime(String authorizerTime) {
    this.authorizerTime = authorizerTime;
    return this;
  }

  /**
   * AuthorizerTime
   * @return authorizerTime
  **/
  @ApiModelProperty(value = "AuthorizerTime")
  
    public String getAuthorizerTime() {
    return authorizerTime;
  }

  public void setAuthorizerTime(String authorizerTime) {
    this.authorizerTime = authorizerTime;
  }

  public XAuthorizeRequest authorizerBy(String authorizerBy) {
    this.authorizerBy = authorizerBy;
    return this;
  }

  /**
   * AuthorizerBy
   * @return authorizerBy
  **/
  @ApiModelProperty(value = "AuthorizerBy")
  
    public String getAuthorizerBy() {
    return authorizerBy;
  }

  public void setAuthorizerBy(String authorizerBy) {
    this.authorizerBy = authorizerBy;
  }

  public XAuthorizeRequest action(ActionEnum action) {
    this.action = action;
    return this;
  }

  /**
   * Action
   * @return action
  **/
  @ApiModelProperty(value = "Action")
  
    public ActionEnum getAction() {
    return action;
  }

  public void setAction(ActionEnum action) {
    this.action = action;
  }

  public XAuthorizeRequest rejectedReason(String rejectedReason) {
    this.rejectedReason = rejectedReason;
    return this;
  }

  /**
   * Rejected Reason
   * @return rejectedReason
  **/
  @ApiModelProperty(value = "Rejected Reason")
  
    public String getRejectedReason() {
    return rejectedReason;
  }

  public void setRejectedReason(String rejectedReason) {
    this.rejectedReason = rejectedReason;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XAuthorizeRequest xauthorizeRequest = (XAuthorizeRequest) o;
    return Objects.equals(this.corporateId, xauthorizeRequest.corporateId) &&
        Objects.equals(this.userId, xauthorizeRequest.userId) &&
        Objects.equals(this.mainRef, xauthorizeRequest.mainRef) &&
        Objects.equals(this.authorizerDate, xauthorizeRequest.authorizerDate) &&
        Objects.equals(this.authorizerTime, xauthorizeRequest.authorizerTime) &&
        Objects.equals(this.authorizerBy, xauthorizeRequest.authorizerBy) &&
        Objects.equals(this.action, xauthorizeRequest.action) &&
        Objects.equals(this.rejectedReason, xauthorizeRequest.rejectedReason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(corporateId, userId, mainRef, authorizerDate, authorizerTime, authorizerBy, action, rejectedReason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XAuthorizeRequest {\n");
    
    sb.append("    corporateId: ").append(toIndentedString(corporateId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    mainRef: ").append(toIndentedString(mainRef)).append("\n");
    sb.append("    authorizerDate: ").append(toIndentedString(authorizerDate)).append("\n");
    sb.append("    authorizerTime: ").append(toIndentedString(authorizerTime)).append("\n");
    sb.append("    authorizerBy: ").append(toIndentedString(authorizerBy)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    rejectedReason: ").append(toIndentedString(rejectedReason)).append("\n");
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
