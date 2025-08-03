package com.csme.csmeapi.mobile.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * XBranchDetails
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-01-29T10:46:11.400+04:00[Asia/Muscat]")
public class XBranchDetails   {
  @JsonProperty("City")
  private String city = null;

  @JsonProperty("BranchName")
  private String branchName = null;

  @JsonProperty("ContactPerson")
  private String contactPerson = null;

  @JsonProperty("BranchAddress")
  private String branchAddress = null;

  @JsonProperty("BranchLocationLink")
  private String branchLocationLink = null;

  public XBranchDetails city(String city) {
    this.city = city;
    return this;
  }

  /**
   * City
   * @return city
  **/
  @ApiModelProperty(value = "City")
  
    public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public XBranchDetails branchName(String branchName) {
    this.branchName = branchName;
    return this;
  }

  /**
   * Branch Name
   * @return branchName
  **/
  @ApiModelProperty(value = "Branch Name")
  
    public String getBranchName() {
    return branchName;
  }

  public void setBranchName(String branchName) {
    this.branchName = branchName;
  }

  public XBranchDetails contactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
    return this;
  }

  /**
   * Contact Person
   * @return contactPerson
  **/
  @ApiModelProperty(value = "Contact Person")
  
    public String getContactPerson() {
    return contactPerson;
  }

  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
  }

  public XBranchDetails branchAddress(String branchAddress) {
    this.branchAddress = branchAddress;
    return this;
  }

  /**
   * Branch Address
   * @return branchAddress
  **/
  @ApiModelProperty(value = "Branch Address")
  
    public String getBranchAddress() {
    return branchAddress;
  }

  public void setBranchAddress(String branchAddress) {
    this.branchAddress = branchAddress;
  }

  public XBranchDetails branchLocationLink(String branchLocationLink) {
    this.branchLocationLink = branchLocationLink;
    return this;
  }

  /**
   * Branch Location Link
   * @return branchLocationLink
  **/
  @ApiModelProperty(value = "Branch Location Link")
  
    public String getBranchLocationLink() {
    return branchLocationLink;
  }

  public void setBranchLocationLink(String branchLocationLink) {
    this.branchLocationLink = branchLocationLink;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XBranchDetails xbranchDetails = (XBranchDetails) o;
    return Objects.equals(this.city, xbranchDetails.city) &&
        Objects.equals(this.branchName, xbranchDetails.branchName) &&
        Objects.equals(this.contactPerson, xbranchDetails.contactPerson) &&
        Objects.equals(this.branchAddress, xbranchDetails.branchAddress) &&
        Objects.equals(this.branchLocationLink, xbranchDetails.branchLocationLink);
  }

  @Override
  public int hashCode() {
    return Objects.hash(city, branchName, contactPerson, branchAddress, branchLocationLink);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XBranchDetails {\n");
    
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    branchName: ").append(toIndentedString(branchName)).append("\n");
    sb.append("    contactPerson: ").append(toIndentedString(contactPerson)).append("\n");
    sb.append("    branchAddress: ").append(toIndentedString(branchAddress)).append("\n");
    sb.append("    branchLocationLink: ").append(toIndentedString(branchLocationLink)).append("\n");
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
