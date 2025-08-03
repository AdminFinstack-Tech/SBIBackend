package com.csme.csmeapi.mobile.model;

import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModelProperty;

/**
 * InquiryLGReq
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-01-29T10:46:11.400+04:00[Asia/Muscat]")
public class InquiryLGReq   {
  @JsonProperty("EtradeReferenceNumber")
  private String etradeReferenceNumber = null;

  @JsonProperty("CIC")
  private String CIC = null;

  @JsonProperty("UserId")
  private String userId = null;

  @JsonProperty("Module")
  private String module = null;

  @JsonProperty("FuncType")
  private String funcType = null;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    _00("00"),
    
    _01("01"),
    
    _02("02"),
    
    _03("03");

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

  @JsonProperty("Page")
  private Integer page = 1;

  @JsonProperty("PerPage")
  private Integer perPage = 5;

  public InquiryLGReq etradeReferenceNumber(String etradeReferenceNumber) {
    this.etradeReferenceNumber = etradeReferenceNumber;
    return this;
  }

  /**
   * E Trade Reference number
   * @return etradeReferenceNumber
  **/
  @ApiModelProperty(value = "E Trade Reference number")
  
    public String getEtradeReferenceNumber() {
    return etradeReferenceNumber;
  }

  public void setEtradeReferenceNumber(String etradeReferenceNumber) {
    this.etradeReferenceNumber = etradeReferenceNumber;
  }

  public InquiryLGReq CIC(String CIC) {
    this.CIC = CIC;
    return this;
  }

  /**
   * Company Id
   * @return CIC
  **/
  @ApiModelProperty(required = true, value = "Company Id")
      @NotNull

    public String getCIC() {
    return CIC;
  }

  public void setCIC(String CIC) {
    this.CIC = CIC;
  }

  public InquiryLGReq userId(String userId) {
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

  public InquiryLGReq module(String module) {
    this.module = module;
    return this;
  }

  /**
   * Module Code
   * @return module
  **/
  @ApiModelProperty(value = "Module Code")
  
    public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public InquiryLGReq funcType(String funcType) {
    this.funcType = funcType;
    return this;
  }

  /**
   * Date
   * @return funcType
  **/
  @ApiModelProperty(value = "Date")
  
    public String getFuncType() {
    return funcType;
  }

  public void setFuncType(String funcType) {
    this.funcType = funcType;
  }

  public InquiryLGReq status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(example = "00", value = "")
  
    public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public InquiryLGReq page(Integer page) {
    this.page = page;
    return this;
  }

  /**
   * Page number
   * minimum: 1
   * @return page
  **/
  @ApiModelProperty(required = true, value = "Page number")
      @NotNull

  @Min(1)  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public InquiryLGReq perPage(Integer perPage) {
    this.perPage = perPage;
    return this;
  }

  /**
   * Number of items per page
   * minimum: 1
   * maximum: 10
   * @return perPage
  **/
  @ApiModelProperty(required = true, value = "Number of items per page")
      @NotNull

  @Min(1) @Max(10)   public Integer getPerPage() {
    return perPage;
  }

  public void setPerPage(Integer perPage) {
    this.perPage = perPage;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InquiryLGReq inquiryLGReq = (InquiryLGReq) o;
    return Objects.equals(this.etradeReferenceNumber, inquiryLGReq.etradeReferenceNumber) &&
        Objects.equals(this.CIC, inquiryLGReq.CIC) &&
        Objects.equals(this.userId, inquiryLGReq.userId) &&
        Objects.equals(this.module, inquiryLGReq.module) &&
        Objects.equals(this.funcType, inquiryLGReq.funcType) &&
        Objects.equals(this.status, inquiryLGReq.status) &&
        Objects.equals(this.page, inquiryLGReq.page) &&
        Objects.equals(this.perPage, inquiryLGReq.perPage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(etradeReferenceNumber, CIC, userId, module, funcType, status, page, perPage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InquiryLGReq {\n");
    
    sb.append("    etradeReferenceNumber: ").append(toIndentedString(etradeReferenceNumber)).append("\n");
    sb.append("    CIC: ").append(toIndentedString(CIC)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    module: ").append(toIndentedString(module)).append("\n");
    sb.append("    funcType: ").append(toIndentedString(funcType)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    perPage: ").append(toIndentedString(perPage)).append("\n");
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
