package com.csme.csmeapi.fin.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * XResetPasswordRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XResetPasswordRequest   {
  @JsonProperty("CorporateId")
  private String corporateId = null;

  @JsonProperty("UserId")
  private String userId = null;

  @JsonProperty("Email")
  private String email = null;

  @JsonProperty("MobileNo")
  private String mobileNo = null;

  @JsonProperty("Password")
  private String password = null;

  @JsonProperty("OTP")
  private String OTP = null;

  public XResetPasswordRequest corporateId(String corporateId) {
    this.corporateId = corporateId;
    return this;
  }

  /**
   * Corporate ID
   * @return corporateId
  **/
  @ApiModelProperty(required = true, value = "Corporate ID")
      @NotNull

    public String getCorporateId() {
    return corporateId;
  }

  public void setCorporateId(String corporateId) {
    this.corporateId = corporateId;
  }

  public XResetPasswordRequest userId(String userId) {
    this.userId = userId;
    return this;
  }

  /**
   * User Id
   * @return userId
  **/
  @ApiModelProperty(required = true, value = "User Id")
      @NotNull

    public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public XResetPasswordRequest email(String email) {
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

  public XResetPasswordRequest mobileNo(String mobileNo) {
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

  public XResetPasswordRequest password(String password) {
    this.password = password;
    return this;
  }

  /**
   * User's password
   * @return password
  **/
  @ApiModelProperty(value = "User's password")
  
    public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public XResetPasswordRequest OTP(String OTP) {
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
    XResetPasswordRequest xresetPasswordRequest = (XResetPasswordRequest) o;
    return Objects.equals(this.corporateId, xresetPasswordRequest.corporateId) &&
        Objects.equals(this.userId, xresetPasswordRequest.userId) &&
        Objects.equals(this.email, xresetPasswordRequest.email) &&
        Objects.equals(this.mobileNo, xresetPasswordRequest.mobileNo) &&
        Objects.equals(this.password, xresetPasswordRequest.password) &&
        Objects.equals(this.OTP, xresetPasswordRequest.OTP);
  }

  @Override
  public int hashCode() {
    return Objects.hash(corporateId, userId, email, mobileNo, password, OTP);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XResetPasswordRequest {\n");
    
    sb.append("    corporateId: ").append(toIndentedString(corporateId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    mobileNo: ").append(toIndentedString(mobileNo)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
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
