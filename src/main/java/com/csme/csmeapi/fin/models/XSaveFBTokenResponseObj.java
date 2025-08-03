package com.csme.csmeapi.fin.models;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * XSaveFBTokenResponseObj
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XSaveFBTokenResponseObj   {
  @JsonProperty("TokenListInfo")
  private Object tokenListInfo = null;

  public XSaveFBTokenResponseObj tokenListInfo(Object tokenListInfo) {
    this.tokenListInfo = tokenListInfo;
    return this;
  }

  /**
   * Token List Information
   * @return tokenListInfo
  **/
  @ApiModelProperty(value = "Token List Information")
  
    public Object getTokenListInfo() {
    return tokenListInfo;
  }

  public void setTokenListInfo(Object tokenListInfo) {
    this.tokenListInfo = tokenListInfo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XSaveFBTokenResponseObj xsaveFBTokenResponseObj = (XSaveFBTokenResponseObj) o;
    return Objects.equals(this.tokenListInfo, xsaveFBTokenResponseObj.tokenListInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tokenListInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XSaveFBTokenResponseObj {\n");
    
    sb.append("    tokenListInfo: ").append(toIndentedString(tokenListInfo)).append("\n");
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
