package com.csme.csmeapi.fin.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * XInquireEventRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XInquireEventRequest   {
  @JsonProperty("CorporateId")
  private String corporateId = null;

  @JsonProperty("UserId")
  private String userId = null;

  @JsonProperty("Module")
  private String module = null;

  @JsonProperty("MainRef")
  private String mainRef = null;

  @JsonProperty("total")
  private Long total = null;

  @JsonProperty("page")
  private Long page = null;

  @JsonProperty("per_page")
  private Long perPage = null;

  @JsonProperty("has_next")
  private Long hasNext = null;

  @JsonProperty("has_prev")
  private Long hasPrev = null;

  public XInquireEventRequest corporateId(String corporateId) {
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

  public XInquireEventRequest userId(String userId) {
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

  public XInquireEventRequest module(String module) {
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

  public XInquireEventRequest mainRef(String mainRef) {
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

  public XInquireEventRequest total(Long total) {
    this.total = total;
    return this;
  }

  /**
   * Get total
   * @return total
  **/
  @ApiModelProperty(value = "")
  
    public Long getTotal() {
    return total;
  }

  public void setTotal(Long total) {
    this.total = total;
  }

  public XInquireEventRequest page(Long page) {
    this.page = page;
    return this;
  }

  /**
   * Get page
   * @return page
  **/
  @ApiModelProperty(value = "")
  
    public Long getPage() {
    return page;
  }

  public void setPage(Long page) {
    this.page = page;
  }

  public XInquireEventRequest perPage(Long perPage) {
    this.perPage = perPage;
    return this;
  }

  /**
   * Get perPage
   * @return perPage
  **/
  @ApiModelProperty(value = "")
  
    public Long getPerPage() {
    return perPage;
  }

  public void setPerPage(Long perPage) {
    this.perPage = perPage;
  }

  public XInquireEventRequest hasNext(Long hasNext) {
    this.hasNext = hasNext;
    return this;
  }

  /**
   * Get hasNext
   * @return hasNext
  **/
  @ApiModelProperty(value = "")
  
    public Long getHasNext() {
    return hasNext;
  }

  public void setHasNext(Long hasNext) {
    this.hasNext = hasNext;
  }

  public XInquireEventRequest hasPrev(Long hasPrev) {
    this.hasPrev = hasPrev;
    return this;
  }

  /**
   * Get hasPrev
   * @return hasPrev
  **/
  @ApiModelProperty(value = "")
  
    public Long getHasPrev() {
    return hasPrev;
  }

  public void setHasPrev(Long hasPrev) {
    this.hasPrev = hasPrev;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XInquireEventRequest xinquireEventRequest = (XInquireEventRequest) o;
    return Objects.equals(this.corporateId, xinquireEventRequest.corporateId) &&
        Objects.equals(this.userId, xinquireEventRequest.userId) &&
        Objects.equals(this.module, xinquireEventRequest.module) &&
        Objects.equals(this.mainRef, xinquireEventRequest.mainRef) &&
        Objects.equals(this.total, xinquireEventRequest.total) &&
        Objects.equals(this.page, xinquireEventRequest.page) &&
        Objects.equals(this.perPage, xinquireEventRequest.perPage) &&
        Objects.equals(this.hasNext, xinquireEventRequest.hasNext) &&
        Objects.equals(this.hasPrev, xinquireEventRequest.hasPrev);
  }

  @Override
  public int hashCode() {
    return Objects.hash(corporateId, userId, module, mainRef, total, page, perPage, hasNext, hasPrev);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XInquireEventRequest {\n");
    
    sb.append("    corporateId: ").append(toIndentedString(corporateId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    module: ").append(toIndentedString(module)).append("\n");
    sb.append("    mainRef: ").append(toIndentedString(mainRef)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    perPage: ").append(toIndentedString(perPage)).append("\n");
    sb.append("    hasNext: ").append(toIndentedString(hasNext)).append("\n");
    sb.append("    hasPrev: ").append(toIndentedString(hasPrev)).append("\n");
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
