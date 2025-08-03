package com.csme.csmeapi.mobile.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * XApplicationDetailsLocal
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-01-29T10:46:11.400+04:00[Asia/Muscat]")
public class XApplicationDetailsLocal   {
  @JsonProperty("BeneficiaryName")
  private String beneficiaryName = null;

  @JsonProperty("Address")
  private String address = null;

  @JsonProperty("ZipCode")
  private String zipCode = null;

  @JsonProperty("POBox")
  private String poBox = null;

  @JsonProperty("City")
  private String city = null;

  @JsonProperty("MobileNumber")
  private String mobileNumber = null;

  public XApplicationDetailsLocal beneficiaryName(String beneficiaryName) {
    this.beneficiaryName = beneficiaryName;
    return this;
  }

  /**
   * Beneficiary's Name
   * @return beneficiaryName
  **/
  @ApiModelProperty(required = true, value = "Beneficiary's Name")
      @NotNull

    public String getBeneficiaryName() {
    return beneficiaryName;
  }

  public void setBeneficiaryName(String beneficiaryName) {
    this.beneficiaryName = beneficiaryName;
  }

  public XApplicationDetailsLocal address(String address) {
    this.address = address;
    return this;
  }

  /**
   * Address
   * @return address
  **/
  @ApiModelProperty(required = true, value = "Address")
      @NotNull

    public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public XApplicationDetailsLocal zipCode(String zipCode) {
    this.zipCode = zipCode;
    return this;
  }

  /**
   * Zip Code
   * @return zipCode
  **/
  @ApiModelProperty(required = true, value = "Zip Code")
      @NotNull

    public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public XApplicationDetailsLocal poBox(String poBox) {
    this.poBox = poBox;
    return this;
  }

  /**
   * PO Box
   * @return poBox
  **/
  @ApiModelProperty(required = true, value = "PO Box")
      @NotNull

    public String getPoBox() {
    return poBox;
  }

  public void setPoBox(String poBox) {
    this.poBox = poBox;
  }

  public XApplicationDetailsLocal city(String city) {
    this.city = city;
    return this;
  }

  /**
   * City
   * @return city
  **/
  @ApiModelProperty(required = true, value = "City")
      @NotNull

    public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public XApplicationDetailsLocal mobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
    return this;
  }

  /**
   * Mobile Number
   * @return mobileNumber
  **/
  @ApiModelProperty(required = true, value = "Mobile Number")
      @NotNull

    public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XApplicationDetailsLocal xapplicationDetailsLocal = (XApplicationDetailsLocal) o;
    return Objects.equals(this.beneficiaryName, xapplicationDetailsLocal.beneficiaryName) &&
        Objects.equals(this.address, xapplicationDetailsLocal.address) &&
        Objects.equals(this.zipCode, xapplicationDetailsLocal.zipCode) &&
        Objects.equals(this.poBox, xapplicationDetailsLocal.poBox) &&
        Objects.equals(this.city, xapplicationDetailsLocal.city) &&
        Objects.equals(this.mobileNumber, xapplicationDetailsLocal.mobileNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(beneficiaryName, address, zipCode, poBox, city, mobileNumber);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XApplicationDetailsLocal {\n");
    
    sb.append("    beneficiaryName: ").append(toIndentedString(beneficiaryName)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    zipCode: ").append(toIndentedString(zipCode)).append("\n");
    sb.append("    poBox: ").append(toIndentedString(poBox)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    mobileNumber: ").append(toIndentedString(mobileNumber)).append("\n");
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
