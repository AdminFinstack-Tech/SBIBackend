package com.csme.csmeapi.fin.models;

import java.util.Objects;

import javax.validation.constraints.Pattern;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * XResetCredentialRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XResetCredentialRequest   {
  @JsonProperty("Email")
  private String email = null;

  @JsonProperty("MobileNo")
  private String mobileNo = null;

  @JsonProperty("OTP")
  private String OTP = null;

  public XResetCredentialRequest email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
  **/
  @ApiModelProperty(value = "")
  
    public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public XResetCredentialRequest mobileNo(String mobileNo) {
    this.mobileNo = mobileNo;
    return this;
  }

  /**
   * Get mobileNo
   * @return mobileNo
  **/
  @ApiModelProperty(value = "")
  
  @Pattern(regexp="^[1-9]\\d{1,14}$")   public String getMobileNo() {
    return mobileNo;
  }

  public void setMobileNo(String mobileNo) {
    this.mobileNo = mobileNo;
  }

  public XResetCredentialRequest OTP(String OTP) {
    this.OTP = OTP;
    return this;
  }

  /**
   * One Time Password
   * @return OTP
  **/
  @ApiModelProperty(value = "One Time Password")
  
    public String getOTP() {
    return OTP;
  }

  public void setOTP(String OTP) {
    this.OTP = OTP;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XResetCredentialRequest xresetCredentialRequest = (XResetCredentialRequest) o;
    return Objects.equals(this.email, xresetCredentialRequest.email) &&
        Objects.equals(this.mobileNo, xresetCredentialRequest.mobileNo) &&
        Objects.equals(this.OTP, xresetCredentialRequest.OTP);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, mobileNo, OTP);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XResetCredentialRequest {\n");
    
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    mobileNo: ").append(toIndentedString(mobileNo)).append("\n");
    sb.append("    OTP: ").append(toIndentedString(OTP)).append("\n");
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
