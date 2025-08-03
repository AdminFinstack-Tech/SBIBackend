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
 * InquiryTrxInfoListObj
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class InquiryTrxInfoListObj   {
  @JsonProperty("title")
  private String title = null;

  @JsonProperty("InquiryTrxInfoList")
  private Object inquiryTrxInfoList = null;

  public InquiryTrxInfoListObj title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
  **/
  @ApiModelProperty(value = "")
  
    public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public InquiryTrxInfoListObj inquiryTrxInfoList(Object inquiryTrxInfoList) {
    this.inquiryTrxInfoList = inquiryTrxInfoList;
    return this;
  }

  /**
   * Get inquiryTrxInfoList
   * @return inquiryTrxInfoList
  **/
  @ApiModelProperty(value = "")
  
    public Object getInquiryTrxInfoList() {
    return inquiryTrxInfoList;
  }

  public void setInquiryTrxInfoList(Object inquiryTrxInfoList) {
    this.inquiryTrxInfoList = inquiryTrxInfoList;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InquiryTrxInfoListObj inquiryTrxInfoListObj = (InquiryTrxInfoListObj) o;
    return Objects.equals(this.title, inquiryTrxInfoListObj.title) &&
        Objects.equals(this.inquiryTrxInfoList, inquiryTrxInfoListObj.inquiryTrxInfoList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, inquiryTrxInfoList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InquiryTrxInfoListObj {\n");
    
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    inquiryTrxInfoList: ").append(toIndentedString(inquiryTrxInfoList)).append("\n");
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
