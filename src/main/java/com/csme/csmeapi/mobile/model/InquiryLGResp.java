package com.csme.csmeapi.mobile.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModelProperty;

/**
 * InquiryLGResp
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-01-29T10:46:11.400+04:00[Asia/Muscat]")
public class InquiryLGResp   {
  @JsonProperty("CurrentStatus")
  private String currentStatus = null;

  /**
   * E Trade Status
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

  @JsonProperty("EtradeReferenceNumber")
  private String etradeReferenceNumber = null;

  @JsonProperty("GuaranteeNumber")
  private String guaranteeNumber = null;

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

  @JsonProperty("Info")
  @Valid
  private List<IssuanceOfBankGuaranteeRq> info = new ArrayList<>();

  @JsonProperty("MessageId")
  private String messageId = null;

  @JsonProperty("NextStatus")
  private String nextStatus = null;

  /**
   * Status code indicating the result of the operation
   */
  public enum StatusCodeEnum {
    _00("00"),
    
    _01("01"),
    
    _02("02"),
    
    _03("03"),
    
    _04("04"),
    
    _06("06"),
    
    _07("07"),
    
    _08("08"),
    
    _09("09"),
    
    _10("10"),
    
    _11("11"),
    
    _14("14"),
    
    _15("15"),
    
    _99("99");

    private String value;

    StatusCodeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusCodeEnum fromValue(String text) {
      for (StatusCodeEnum b : StatusCodeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("StatusCode")
  private StatusCodeEnum statusCode = null;

  @JsonProperty("StatusDescription")
  private String statusDescription = null;

  @JsonProperty("Timestamp")
  private String timestamp = null;

  public InquiryLGResp currentStatus(String currentStatus) {
    this.currentStatus = currentStatus;
    return this;
  }

  /**
   * Current Status
   * @return currentStatus
  **/
  @ApiModelProperty(value = "Current Status")
  
    public String getCurrentStatus() {
    return currentStatus;
  }

  public void setCurrentStatus(String currentStatus) {
    this.currentStatus = currentStatus;
  }

  public InquiryLGResp status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * E Trade Status
   * @return status
  **/
  @ApiModelProperty(example = "00", value = "E Trade Status")
  
    public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public InquiryLGResp etradeReferenceNumber(String etradeReferenceNumber) {
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

  public InquiryLGResp guaranteeNumber(String guaranteeNumber) {
    this.guaranteeNumber = guaranteeNumber;
    return this;
  }

  /**
   * Guarantee Number
   * @return guaranteeNumber
  **/
  @ApiModelProperty(example = "24OGTE48800001", value = "Guarantee Number")
  
    public String getGuaranteeNumber() {
    return guaranteeNumber;
  }

  public void setGuaranteeNumber(String guaranteeNumber) {
    this.guaranteeNumber = guaranteeNumber;
  }

  public InquiryLGResp total(Long total) {
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

  public InquiryLGResp page(Long page) {
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

  public InquiryLGResp perPage(Long perPage) {
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

  public InquiryLGResp hasNext(Long hasNext) {
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

  public InquiryLGResp hasPrev(Long hasPrev) {
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

  public InquiryLGResp info(List<IssuanceOfBankGuaranteeRq> info) {
    this.info = info;
    return this;
  }

  public InquiryLGResp addInfoItem(IssuanceOfBankGuaranteeRq infoItem) {
    this.info.add(infoItem);
    return this;
  }

  /**
   * Get info
   * @return info
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull
    @Valid
    public List<IssuanceOfBankGuaranteeRq> getInfo() {
    return info;
  }

  public void setInfo(List<IssuanceOfBankGuaranteeRq> info) {
    this.info = info;
  }

  public InquiryLGResp messageId(String messageId) {
    this.messageId = messageId;
    return this;
  }

  /**
   * Message ID
   * @return messageId
  **/
  @ApiModelProperty(required = true, value = "Message ID")
      @NotNull

    public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public InquiryLGResp nextStatus(String nextStatus) {
    this.nextStatus = nextStatus;
    return this;
  }

  /**
   * Next Status
   * @return nextStatus
  **/
  @ApiModelProperty(value = "Next Status")
  
    public String getNextStatus() {
    return nextStatus;
  }

  public void setNextStatus(String nextStatus) {
    this.nextStatus = nextStatus;
  }

  public InquiryLGResp statusCode(StatusCodeEnum statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  /**
   * Status code indicating the result of the operation
   * @return statusCode
  **/
  @ApiModelProperty(example = "00", required = true, value = "Status code indicating the result of the operation")
      @NotNull

    public StatusCodeEnum getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(StatusCodeEnum statusCode) {
    this.statusCode = statusCode;
  }

  public InquiryLGResp statusDescription(String statusDescription) {
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

  public InquiryLGResp timestamp(String timestamp) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InquiryLGResp inquiryLGResp = (InquiryLGResp) o;
    return Objects.equals(this.currentStatus, inquiryLGResp.currentStatus) &&
        Objects.equals(this.status, inquiryLGResp.status) &&
        Objects.equals(this.etradeReferenceNumber, inquiryLGResp.etradeReferenceNumber) &&
        Objects.equals(this.guaranteeNumber, inquiryLGResp.guaranteeNumber) &&
        Objects.equals(this.total, inquiryLGResp.total) &&
        Objects.equals(this.page, inquiryLGResp.page) &&
        Objects.equals(this.perPage, inquiryLGResp.perPage) &&
        Objects.equals(this.hasNext, inquiryLGResp.hasNext) &&
        Objects.equals(this.hasPrev, inquiryLGResp.hasPrev) &&
        Objects.equals(this.info, inquiryLGResp.info) &&
        Objects.equals(this.messageId, inquiryLGResp.messageId) &&
        Objects.equals(this.nextStatus, inquiryLGResp.nextStatus) &&
        Objects.equals(this.statusCode, inquiryLGResp.statusCode) &&
        Objects.equals(this.statusDescription, inquiryLGResp.statusDescription) &&
        Objects.equals(this.timestamp, inquiryLGResp.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentStatus, status, etradeReferenceNumber, guaranteeNumber, total, page, perPage, hasNext, hasPrev, info, messageId, nextStatus, statusCode, statusDescription, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InquiryLGResp {\n");
    
    sb.append("    currentStatus: ").append(toIndentedString(currentStatus)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    etradeReferenceNumber: ").append(toIndentedString(etradeReferenceNumber)).append("\n");
    sb.append("    guaranteeNumber: ").append(toIndentedString(guaranteeNumber)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    perPage: ").append(toIndentedString(perPage)).append("\n");
    sb.append("    hasNext: ").append(toIndentedString(hasNext)).append("\n");
    sb.append("    hasPrev: ").append(toIndentedString(hasPrev)).append("\n");
    sb.append("    info: ").append(toIndentedString(info)).append("\n");
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    nextStatus: ").append(toIndentedString(nextStatus)).append("\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
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
