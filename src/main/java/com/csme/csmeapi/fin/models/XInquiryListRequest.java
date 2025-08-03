package com.csme.csmeapi.fin.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModelProperty;

/**
 * XInquiryListRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XInquiryListRequest   {
  @JsonProperty("CorporateId")
  private String corporateId = null;

  @JsonProperty("UserId")
  private String userId = null;

  @JsonProperty("Module")
  private String module = null;

  @JsonProperty("page")
  private Integer page = 1;

  @JsonProperty("per_page")
  private Integer perPage = 10;

  /**
   * Based on Type reterive List record
   */
  public enum TypeEnum {
    INQ("INQ"),
    
    AUTH("AUTH");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("Type")
  private TypeEnum type = null;

  @JsonProperty("filterby")
  @Valid
  private List<XInfoListObj> filterby = null;

  public XInquiryListRequest corporateId(String corporateId) {
    this.corporateId = corporateId;
    return this;
  }

  /**
   * Corporate Id
   * @return corporateId
  **/
  @ApiModelProperty(required = true, value = "Corporate Id")
      @NotNull

    public String getCorporateId() {
    return corporateId;
  }

  public void setCorporateId(String corporateId) {
    this.corporateId = corporateId;
  }

  public XInquiryListRequest userId(String userId) {
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

  public XInquiryListRequest module(String module) {
    this.module = module;
    return this;
  }

  /**
   * Module Name
   * @return module
  **/
  @ApiModelProperty(example = "IWGT;OWGT;IMLC", required = true, value = "Module Name")
      @NotNull

    public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public XInquiryListRequest page(Integer page) {
    this.page = page;
    return this;
  }

  /**
   * page
   * minimum: 1
   * @return page
  **/
  @ApiModelProperty(required = true, value = "page")
      @NotNull

  @Min(1)  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public XInquiryListRequest perPage(Integer perPage) {
    this.perPage = perPage;
    return this;
  }

  /**
   * per_page
   * minimum: 1
   * maximum: 20
   * @return perPage
  **/
  @ApiModelProperty(required = true, value = "per_page")
      @NotNull

  @Min(1) @Max(20)   public Integer getPerPage() {
    return perPage;
  }

  public void setPerPage(Integer perPage) {
    this.perPage = perPage;
  }

  public XInquiryListRequest type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Based on Type reterive List record
   * @return type
  **/
  @ApiModelProperty(required = true, value = "Based on Type reterive List record")
      @NotNull

    public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public XInquiryListRequest filterby(List<XInfoListObj> filterby) {
    this.filterby = filterby;
    return this;
  }

  public XInquiryListRequest addFilterbyItem(XInfoListObj filterbyItem) {
    if (this.filterby == null) {
      this.filterby = new ArrayList<>();
    }
    this.filterby.add(filterbyItem);
    return this;
  }

  /**
   * Get filterby
   * @return filterby
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<XInfoListObj> getFilterby() {
    return filterby;
  }

  public void setFilterby(List<XInfoListObj> filterby) {
    this.filterby = filterby;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XInquiryListRequest xinquiryListRequest = (XInquiryListRequest) o;
    return Objects.equals(this.corporateId, xinquiryListRequest.corporateId) &&
        Objects.equals(this.userId, xinquiryListRequest.userId) &&
        Objects.equals(this.module, xinquiryListRequest.module) &&
        Objects.equals(this.page, xinquiryListRequest.page) &&
        Objects.equals(this.perPage, xinquiryListRequest.perPage) &&
        Objects.equals(this.type, xinquiryListRequest.type) &&
        Objects.equals(this.filterby, xinquiryListRequest.filterby);
  }

  @Override
  public int hashCode() {
    return Objects.hash(corporateId, userId, module, page, perPage, type, filterby);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XInquiryListRequest {\n");
    
    sb.append("    corporateId: ").append(toIndentedString(corporateId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    module: ").append(toIndentedString(module)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    perPage: ").append(toIndentedString(perPage)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    filterby: ").append(toIndentedString(filterby)).append("\n");
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
