package com.csme.csmeapi.mobile.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * XCustomerDetails
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-01-29T10:46:11.400+04:00[Asia/Muscat]")
public class XCustomerDetails   {
  @JsonProperty("CustomerName")
  private String customerName = null;

  @JsonProperty("AccountNumber")
  private String accountNumber = null;

  @JsonProperty("ContactName")
  private String contactName = null;

  @JsonProperty("ContactNumber")
  private String contactNumber = null;

  public XCustomerDetails customerName(String customerName) {
    this.customerName = customerName;
    return this;
  }

  /**
   * Customer's name
   * @return customerName
  **/
  @ApiModelProperty(required = true, value = "Customer's name")
      @NotNull

    public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public XCustomerDetails accountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
    return this;
  }

  /**
   * Customer's account number
   * @return accountNumber
  **/
  @ApiModelProperty(required = true, value = "Customer's account number")
      @NotNull

    public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public XCustomerDetails contactName(String contactName) {
    this.contactName = contactName;
    return this;
  }

  /**
   * Name of the contact person
   * @return contactName
  **/
  @ApiModelProperty(required = true, value = "Name of the contact person")
      @NotNull

    public String getContactName() {
    return contactName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public XCustomerDetails contactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
    return this;
  }

  /**
   * Contact person's phone number
   * @return contactNumber
  **/
  @ApiModelProperty(required = true, value = "Contact person's phone number")
      @NotNull

    public String getContactNumber() {
    return contactNumber;
  }

  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XCustomerDetails xcustomerDetails = (XCustomerDetails) o;
    return Objects.equals(this.customerName, xcustomerDetails.customerName) &&
        Objects.equals(this.accountNumber, xcustomerDetails.accountNumber) &&
        Objects.equals(this.contactName, xcustomerDetails.contactName) &&
        Objects.equals(this.contactNumber, xcustomerDetails.contactNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerName, accountNumber, contactName, contactNumber);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XCustomerDetails {\n");
    
    sb.append("    customerName: ").append(toIndentedString(customerName)).append("\n");
    sb.append("    accountNumber: ").append(toIndentedString(accountNumber)).append("\n");
    sb.append("    contactName: ").append(toIndentedString(contactName)).append("\n");
    sb.append("    contactNumber: ").append(toIndentedString(contactNumber)).append("\n");
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
