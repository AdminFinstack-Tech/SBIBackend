package com.csme.csmeapi.mobile.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * XApplicationDetailsForeign
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-01-29T10:46:11.400+04:00[Asia/Muscat]")
public class XApplicationDetailsForeign   {
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

  @JsonProperty("SwiftCode")
  private String swiftCode = null;

  @JsonProperty("Country")
  private String country = null;

  public XApplicationDetailsForeign beneficiaryName(String beneficiaryName) {
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

  public XApplicationDetailsForeign address(String address) {
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

  public XApplicationDetailsForeign zipCode(String zipCode) {
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

  public XApplicationDetailsForeign poBox(String poBox) {
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

  public XApplicationDetailsForeign city(String city) {
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

  public XApplicationDetailsForeign mobileNumber(String mobileNumber) {
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

  public XApplicationDetailsForeign swiftCode(String swiftCode) {
    this.swiftCode = swiftCode;
    return this;
  }

  /**
   * Swift Code
   * @return swiftCode
  **/
  @ApiModelProperty(required = true, value = "Swift Code")
      @NotNull

    public String getSwiftCode() {
    return swiftCode;
  }

  public void setSwiftCode(String swiftCode) {
    this.swiftCode = swiftCode;
  }

  public XApplicationDetailsForeign country(String country) {
    this.country = country;
    return this;
  }

  /**
   * Country
   * @return country
  **/
  @ApiModelProperty(required = true, value = "Country")
      @NotNull

    public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XApplicationDetailsForeign xapplicationDetailsForeign = (XApplicationDetailsForeign) o;
    return Objects.equals(this.beneficiaryName, xapplicationDetailsForeign.beneficiaryName) &&
        Objects.equals(this.address, xapplicationDetailsForeign.address) &&
        Objects.equals(this.zipCode, xapplicationDetailsForeign.zipCode) &&
        Objects.equals(this.poBox, xapplicationDetailsForeign.poBox) &&
        Objects.equals(this.city, xapplicationDetailsForeign.city) &&
        Objects.equals(this.mobileNumber, xapplicationDetailsForeign.mobileNumber) &&
        Objects.equals(this.swiftCode, xapplicationDetailsForeign.swiftCode) &&
        Objects.equals(this.country, xapplicationDetailsForeign.country);
  }

  @Override
  public int hashCode() {
    return Objects.hash(beneficiaryName, address, zipCode, poBox, city, mobileNumber, swiftCode, country);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XApplicationDetailsForeign {\n");
    
    sb.append("    beneficiaryName: ").append(toIndentedString(beneficiaryName)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    zipCode: ").append(toIndentedString(zipCode)).append("\n");
    sb.append("    poBox: ").append(toIndentedString(poBox)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    mobileNumber: ").append(toIndentedString(mobileNumber)).append("\n");
    sb.append("    swiftCode: ").append(toIndentedString(swiftCode)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
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
