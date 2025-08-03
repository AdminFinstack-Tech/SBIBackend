package com.csme.csmeapi.fin.models;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * XInquireEventRequestListObj
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XInquireEventRequestListObj   {
  @JsonProperty("CommonInfoList")
  private Object commonInfoList = null;

  public XInquireEventRequestListObj commonInfoList(Object commonInfoList) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XInquireEventRequestListObj xinquireEventRequestListObj = (XInquireEventRequestListObj) o;
    return Objects.equals(this.commonInfoList, xinquireEventRequestListObj.commonInfoList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commonInfoList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XInquireEventRequestListObj {\n");
    
    sb.append("    commonInfoList: ").append(toIndentedString(commonInfoList)).append("\n");
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
