package com.csme.csmeapi.mobile.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * ProductAuthLevel
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-09T23:56:19.075+05:30[Asia/Calcutta]")
public class ProductAuthLevel   {
  @JsonProperty("productName")
  private String productName = null;

  @JsonProperty("curency")
  private String curency = null;

  @JsonProperty("authLevels")
  @Valid
  private List<AuthLevel> authLevels = null;

  public ProductAuthLevel productName(String productName) {
    this.productName = productName;
    return this;
  }

  /**
   * Product Name
   * @return productName
  **/
  @ApiModelProperty(value = "Product Name")
  
    public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public ProductAuthLevel curency(String curency) {
    this.curency = curency;
    return this;
  }

  /**
   * currency for auth matrix
   * @return curency
  **/
  @ApiModelProperty(example = "AED", value = "currency for auth matrix")
  
    public String getCurency() {
    return curency;
  }

  public void setCurency(String curency) {
    this.curency = curency;
  }

  public ProductAuthLevel authLevels(List<AuthLevel> authLevels) {
    this.authLevels = authLevels;
    return this;
  }

  public ProductAuthLevel addAuthLevelsItem(AuthLevel authLevelsItem) {
    if (this.authLevels == null) {
      this.authLevels = new ArrayList<AuthLevel>();
    }
    this.authLevels.add(authLevelsItem);
    return this;
  }

  /**
   * Get authLevels
   * @return authLevels
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<AuthLevel> getAuthLevels() {
    return authLevels;
  }

  public void setAuthLevels(List<AuthLevel> authLevels) {
    this.authLevels = authLevels;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductAuthLevel productAuthLevel = (ProductAuthLevel) o;
    return Objects.equals(this.productName, productAuthLevel.productName) &&
        Objects.equals(this.curency, productAuthLevel.curency) &&
        Objects.equals(this.authLevels, productAuthLevel.authLevels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productName, curency, authLevels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductAuthLevel {\n");
    
    sb.append("    productName: ").append(toIndentedString(productName)).append("\n");
    sb.append("    curency: ").append(toIndentedString(curency)).append("\n");
    sb.append("    authLevels: ").append(toIndentedString(authLevels)).append("\n");
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
