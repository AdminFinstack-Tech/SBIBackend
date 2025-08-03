package com.csme.csmeapi.fin.models;

import java.util.Objects;
import com.csme.csmeapi.fin.models.XproductInfoListNofTransaction;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * XproductInfoList
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XproductInfoList   {
  @JsonProperty("Module")
  private String module = null;

  @JsonProperty("Description")
  private String description = null;

  @JsonProperty("icon")
  private String icon = null;

  @JsonProperty("style")
  private Object style = null;

  @JsonProperty("NofTransaction")
  private XproductInfoListNofTransaction nofTransaction = null;

  public XproductInfoList module(String module) {
    this.module = module;
    return this;
  }

  /**
   * Module
   * @return module
  **/
  @ApiModelProperty(value = "Module")
  
    public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public XproductInfoList description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
  **/
  @ApiModelProperty(value = "")
  
    public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public XproductInfoList icon(String icon) {
    this.icon = icon;
    return this;
  }

  /**
   * Get icon
   * @return icon
  **/
  @ApiModelProperty(value = "")
  
    public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public XproductInfoList style(Object style) {
    this.style = style;
    return this;
  }

  /**
   * Get style
   * @return style
  **/
  @ApiModelProperty(value = "")
  
    public Object getStyle() {
    return style;
  }

  public void setStyle(Object style) {
    this.style = style;
  }

  public XproductInfoList nofTransaction(XproductInfoListNofTransaction nofTransaction) {
    this.nofTransaction = nofTransaction;
    return this;
  }

  /**
   * Get nofTransaction
   * @return nofTransaction
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public XproductInfoListNofTransaction getNofTransaction() {
    return nofTransaction;
  }

  public void setNofTransaction(XproductInfoListNofTransaction nofTransaction) {
    this.nofTransaction = nofTransaction;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XproductInfoList xproductInfoList = (XproductInfoList) o;
    return Objects.equals(this.module, xproductInfoList.module) &&
        Objects.equals(this.description, xproductInfoList.description) &&
        Objects.equals(this.icon, xproductInfoList.icon) &&
        Objects.equals(this.style, xproductInfoList.style) &&
        Objects.equals(this.nofTransaction, xproductInfoList.nofTransaction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(module, description, icon, style, nofTransaction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XproductInfoList {\n");
    
    sb.append("    module: ").append(toIndentedString(module)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    icon: ").append(toIndentedString(icon)).append("\n");
    sb.append("    style: ").append(toIndentedString(style)).append("\n");
    sb.append("    nofTransaction: ").append(toIndentedString(nofTransaction)).append("\n");
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
