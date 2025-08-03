package com.csme.csmeapi.fin.models;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * XInquireTransactionRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XInquireTransactionRequest   {
  @JsonProperty("CorporateId")
  private String corporateId = null;

  @JsonProperty("UserId")
  private String userId = null;

  @JsonProperty("Module")
  private String module = null;

  @JsonProperty("MainRef")
  private String mainRef = null;

  @JsonProperty("functionId")
  private String functionId = null;

  @JsonProperty("EventTimes")
  private String eventTimes = null;

  public XInquireTransactionRequest corporateId(String corporateId) {
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

  public XInquireTransactionRequest userId(String userId) {
    this.userId = userId;
    return this;
  }

  /**
   * User Id
   * @return userId
  **/
  @ApiModelProperty(required = true, value = "User Id")
      @NotNull

    public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public XInquireTransactionRequest module(String module) {
    this.module = module;
    return this;
  }

  /**
   * Module
   * @return module
  **/
  @ApiModelProperty(required = true, value = "Module")
      @NotNull

    public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public XInquireTransactionRequest mainRef(String mainRef) {
    this.mainRef = mainRef;
    return this;
  }

  /**
   * MainRef
   * @return mainRef
  **/
  @ApiModelProperty(required = true, value = "MainRef")
      @NotNull

    public String getMainRef() {
    return mainRef;
  }

  public void setMainRef(String mainRef) {
    this.mainRef = mainRef;
  }

  public XInquireTransactionRequest functionId(String functionId) {
    this.functionId = functionId;
    return this;
  }

  /**
   * function Id
   * @return functionId
  **/
  @ApiModelProperty(required = true, value = "function Id")
      @NotNull

    public String getFunctionId() {
    return functionId;
  }

  public void setFunctionId(String functionId) {
    this.functionId = functionId;
  }

  public XInquireTransactionRequest eventTimes(String eventTimes) {
    this.eventTimes = eventTimes;
    return this;
  }

  /**
   * Event Times
   * @return eventTimes
  **/
  @ApiModelProperty(required = true, value = "Event Times")
      @NotNull

    public String getEventTimes() {
    return eventTimes;
  }

  public void setEventTimes(String eventTimes) {
    this.eventTimes = eventTimes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XInquireTransactionRequest xinquireTransactionRequest = (XInquireTransactionRequest) o;
    return Objects.equals(this.corporateId, xinquireTransactionRequest.corporateId) &&
        Objects.equals(this.userId, xinquireTransactionRequest.userId) &&
        Objects.equals(this.module, xinquireTransactionRequest.module) &&
        Objects.equals(this.mainRef, xinquireTransactionRequest.mainRef) &&
        Objects.equals(this.functionId, xinquireTransactionRequest.functionId) &&
        Objects.equals(this.eventTimes, xinquireTransactionRequest.eventTimes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(corporateId, userId, module, mainRef, functionId, eventTimes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XInquireTransactionRequest {\n");
    
    sb.append("    corporateId: ").append(toIndentedString(corporateId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    module: ").append(toIndentedString(module)).append("\n");
    sb.append("    mainRef: ").append(toIndentedString(mainRef)).append("\n");
    sb.append("    functionId: ").append(toIndentedString(functionId)).append("\n");
    sb.append("    eventTimes: ").append(toIndentedString(eventTimes)).append("\n");
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
