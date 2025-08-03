package com.csme.csmeapi.fin.models;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * XBankLimitResponseInfoObj
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XBankLimitResponseInfoObj   {
  @JsonProperty("LimitListInfo")
  private Object limitListInfo = null;

  public XBankLimitResponseInfoObj limitListInfo(Object limitListInfo) {
    this.limitListInfo = limitListInfo;
    return this;
  }

  /**
   * Limit List Info
   * @return limitListInfo
  **/
  @ApiModelProperty(value = "Limit List Info")
  
    public Object getLimitListInfo() {
    return limitListInfo;
  }

  public void setLimitListInfo(Object limitListInfo) {
    this.limitListInfo = limitListInfo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XBankLimitResponseInfoObj xbankLimitResponseInfoObj = (XBankLimitResponseInfoObj) o;
    return Objects.equals(this.limitListInfo, xbankLimitResponseInfoObj.limitListInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(limitListInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XBankLimitResponseInfoObj {\n");
    
    sb.append("    limitListInfo: ").append(toIndentedString(limitListInfo)).append("\n");
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
