package com.csme.csmeapi.mobile.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * XApplicationDetails
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-01-29T10:46:11.400+04:00[Asia/Muscat]")
public class XApplicationDetails   {
  @JsonProperty("TextGuaranteeIssued")
  private OneOfXApplicationDetailsTextGuaranteeIssued textGuaranteeIssued = null;

  /**
   * Specifies whether Local or Foreign
   */
  public enum OutwardGuaranteeEnum {
    LOCAL("Local"),
    
    FOREIGN("Foreign");

    private String value;

    OutwardGuaranteeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static OutwardGuaranteeEnum fromValue(String text) {
      for (OutwardGuaranteeEnum b : OutwardGuaranteeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("OutwardGuarantee")
  private OutwardGuaranteeEnum outwardGuarantee = null;

  @JsonProperty("LocalDetails")
  private Object localDetails = null;

  @JsonProperty("ForeignDetails")
  private Object foreignDetails = null;

  public XApplicationDetails textGuaranteeIssued(OneOfXApplicationDetailsTextGuaranteeIssued textGuaranteeIssued) {
    this.textGuaranteeIssued = textGuaranteeIssued;
    return this;
  }

  /**
   * Get textGuaranteeIssued
   * @return textGuaranteeIssued
  **/
  @ApiModelProperty(value = "")
  
    public OneOfXApplicationDetailsTextGuaranteeIssued getTextGuaranteeIssued() {
    return textGuaranteeIssued;
  }

  public void setTextGuaranteeIssued(OneOfXApplicationDetailsTextGuaranteeIssued textGuaranteeIssued) {
    this.textGuaranteeIssued = textGuaranteeIssued;
  }

  public XApplicationDetails outwardGuarantee(OutwardGuaranteeEnum outwardGuarantee) {
    this.outwardGuarantee = outwardGuarantee;
    return this;
  }

  /**
   * Specifies whether Local or Foreign
   * @return outwardGuarantee
  **/
  @ApiModelProperty(required = true, value = "Specifies whether Local or Foreign")
      @NotNull

    public OutwardGuaranteeEnum getOutwardGuarantee() {
    return outwardGuarantee;
  }

  public void setOutwardGuarantee(OutwardGuaranteeEnum outwardGuarantee) {
    this.outwardGuarantee = outwardGuarantee;
  }

  public XApplicationDetails localDetails(Object localDetails) {
    this.localDetails = localDetails;
    return this;
  }

  /**
   * Get localDetails
   * @return localDetails
  **/
  @ApiModelProperty(value = "")
  
    public Object getLocalDetails() {
    return localDetails;
  }

  public void setLocalDetails(Object localDetails) {
    this.localDetails = localDetails;
  }

  public XApplicationDetails foreignDetails(Object foreignDetails) {
    this.foreignDetails = foreignDetails;
    return this;
  }

  /**
   * Get foreignDetails
   * @return foreignDetails
  **/
  @ApiModelProperty(value = "")
  
    public Object getForeignDetails() {
    return foreignDetails;
  }

  public void setForeignDetails(Object foreignDetails) {
    this.foreignDetails = foreignDetails;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XApplicationDetails xapplicationDetails = (XApplicationDetails) o;
    return Objects.equals(this.textGuaranteeIssued, xapplicationDetails.textGuaranteeIssued) &&
        Objects.equals(this.outwardGuarantee, xapplicationDetails.outwardGuarantee) &&
        Objects.equals(this.localDetails, xapplicationDetails.localDetails) &&
        Objects.equals(this.foreignDetails, xapplicationDetails.foreignDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(textGuaranteeIssued, outwardGuarantee, localDetails, foreignDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XApplicationDetails {\n");
    
    sb.append("    textGuaranteeIssued: ").append(toIndentedString(textGuaranteeIssued)).append("\n");
    sb.append("    outwardGuarantee: ").append(toIndentedString(outwardGuarantee)).append("\n");
    sb.append("    localDetails: ").append(toIndentedString(localDetails)).append("\n");
    sb.append("    foreignDetails: ").append(toIndentedString(foreignDetails)).append("\n");
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
