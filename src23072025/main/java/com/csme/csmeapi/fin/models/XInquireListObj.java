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
 * XInquireListObj
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XInquireListObj   {
  @JsonProperty("CommonInfoList")
  private Object commonInfoList = null;

  @JsonProperty("DynamicInfoList")
  private Object dynamicInfoList = null;

  public XInquireListObj commonInfoList(Object commonInfoList) {
    this.commonInfoList = commonInfoList;
    return this;
  }

  /**
   * Common Field Information List
   * @return commonInfoList
  **/
  @ApiModelProperty(value = "Common Field Information List")
  
    public Object getCommonInfoList() {
    return commonInfoList;
  }

  public void setCommonInfoList(Object commonInfoList) {
    this.commonInfoList = commonInfoList;
  }

  public XInquireListObj dynamicInfoList(Object dynamicInfoList) {
    this.dynamicInfoList = dynamicInfoList;
    return this;
  }

  /**
   * Dynamic Field Information List
   * @return dynamicInfoList
  **/
  @ApiModelProperty(value = "Dynamic Field Information List")
  
    public Object getDynamicInfoList() {
    return dynamicInfoList;
  }

  public void setDynamicInfoList(Object dynamicInfoList) {
    this.dynamicInfoList = dynamicInfoList;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XInquireListObj xinquireListObj = (XInquireListObj) o;
    return Objects.equals(this.commonInfoList, xinquireListObj.commonInfoList) &&
        Objects.equals(this.dynamicInfoList, xinquireListObj.dynamicInfoList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commonInfoList, dynamicInfoList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XInquireListObj {\n");
    
    sb.append("    commonInfoList: ").append(toIndentedString(commonInfoList)).append("\n");
    sb.append("    dynamicInfoList: ").append(toIndentedString(dynamicInfoList)).append("\n");
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
