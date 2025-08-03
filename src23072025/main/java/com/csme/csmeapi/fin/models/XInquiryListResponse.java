package com.csme.csmeapi.fin.models;

import java.util.Objects;
import com.csme.csmeapi.fin.models.XInquireListObj;
import com.csme.csmeapi.fin.models.XproductInfoList;
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
 * XInquiryListResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XInquiryListResponse   {
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

  @JsonProperty("productsAccessList")
  @Valid
  private List<XproductInfoList> productsAccessList = null;

  @JsonProperty("InquireListInfo")
  @Valid
  private List<XInquireListObj> inquireListInfo = null;

  public XInquiryListResponse messageId(String messageId) {
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

  public XInquiryListResponse requestId(String requestId) {
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

  public XInquiryListResponse statusCode(String statusCode) {
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

  public XInquiryListResponse statusDescription(String statusDescription) {
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

  public XInquiryListResponse timestamp(String timestamp) {
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

  public XInquiryListResponse total(Long total) {
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

  public XInquiryListResponse page(Long page) {
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

  public XInquiryListResponse perPage(Long perPage) {
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

  public XInquiryListResponse hasNext(Long hasNext) {
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

  public XInquiryListResponse hasPrev(Long hasPrev) {
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

  public XInquiryListResponse productsAccessList(List<XproductInfoList> productsAccessList) {
    this.productsAccessList = productsAccessList;
    return this;
  }

  public XInquiryListResponse addProductsAccessListItem(XproductInfoList productsAccessListItem) {
    if (this.productsAccessList == null) {
      this.productsAccessList = new ArrayList<>();
    }
    this.productsAccessList.add(productsAccessListItem);
    return this;
  }

  /**
   * Get productsAccessList
   * @return productsAccessList
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<XproductInfoList> getProductsAccessList() {
    return productsAccessList;
  }

  public void setProductsAccessList(List<XproductInfoList> productsAccessList) {
    this.productsAccessList = productsAccessList;
  }

  public XInquiryListResponse inquireListInfo(List<XInquireListObj> inquireListInfo) {
    this.inquireListInfo = inquireListInfo;
    return this;
  }

  public XInquiryListResponse addInquireListInfoItem(XInquireListObj inquireListInfoItem) {
    if (this.inquireListInfo == null) {
      this.inquireListInfo = new ArrayList<>();
    }
    this.inquireListInfo.add(inquireListInfoItem);
    return this;
  }

  /**
   * Get inquireListInfo
   * @return inquireListInfo
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<XInquireListObj> getInquireListInfo() {
    return inquireListInfo;
  }

  public void setInquireListInfo(List<XInquireListObj> inquireListInfo) {
    this.inquireListInfo = inquireListInfo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XInquiryListResponse xinquiryListResponse = (XInquiryListResponse) o;
    return Objects.equals(this.messageId, xinquiryListResponse.messageId) &&
        Objects.equals(this.requestId, xinquiryListResponse.requestId) &&
        Objects.equals(this.statusCode, xinquiryListResponse.statusCode) &&
        Objects.equals(this.statusDescription, xinquiryListResponse.statusDescription) &&
        Objects.equals(this.timestamp, xinquiryListResponse.timestamp) &&
        Objects.equals(this.total, xinquiryListResponse.total) &&
        Objects.equals(this.page, xinquiryListResponse.page) &&
        Objects.equals(this.perPage, xinquiryListResponse.perPage) &&
        Objects.equals(this.hasNext, xinquiryListResponse.hasNext) &&
        Objects.equals(this.hasPrev, xinquiryListResponse.hasPrev) &&
        Objects.equals(this.productsAccessList, xinquiryListResponse.productsAccessList) &&
        Objects.equals(this.inquireListInfo, xinquiryListResponse.inquireListInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, requestId, statusCode, statusDescription, timestamp, total, page, perPage, hasNext, hasPrev, productsAccessList, inquireListInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XInquiryListResponse {\n");
    
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    statusDescription: ").append(toIndentedString(statusDescription)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    perPage: ").append(toIndentedString(perPage)).append("\n");
    sb.append("    hasNext: ").append(toIndentedString(hasNext)).append("\n");
    sb.append("    hasPrev: ").append(toIndentedString(hasPrev)).append("\n");
    sb.append("    productsAccessList: ").append(toIndentedString(productsAccessList)).append("\n");
    sb.append("    inquireListInfo: ").append(toIndentedString(inquireListInfo)).append("\n");
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
