package com.csme.csmeapi.mobile.model;

import java.math.BigInteger;
import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

@Validated
public class AuthLevel {
  @JsonProperty("limit_amount")
  private BigInteger limitAmount = null;
  
  @JsonProperty("auth_level")
  private Integer authLevel = null;
  
  public AuthLevel limitAmount(BigInteger limitAmount) {
    this.limitAmount = limitAmount;
    return this;
  }
  
  @ApiModelProperty(example = "100000", value = "Limit amount for level")
  public BigInteger getLimitAmount() {
    return this.limitAmount;
  }
  
  public void setLimitAmount(BigInteger limitAmount) {
    this.limitAmount = limitAmount;
  }
  
  public AuthLevel authLevel(Integer authLevel) {
    this.authLevel = authLevel;
    return this;
  }
  
  @ApiModelProperty(example = "1", value = "Limit amount for level")
  public Integer getAuthLevel() {
    return this.authLevel;
  }
  
  public void setAuthLevel(Integer authLevel) {
    this.authLevel = authLevel;
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (o == null || getClass() != o.getClass())
      return false; 
    AuthLevel authLevel = (AuthLevel)o;
    return (Objects.equals(this.limitAmount, authLevel.limitAmount) && 
      Objects.equals(this.authLevel, authLevel.authLevel));
  }
  
  public int hashCode() {
    return Objects.hash(new Object[] { this.limitAmount, this.authLevel });
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthLevel {\n");
    sb.append("limitAmount: ").append(toIndentedString(this.limitAmount)).append("\n");
    sb.append("authLevel: ").append(toIndentedString(this.authLevel)).append("\n");
    sb.append("}");
    return sb.toString();
  }
  
  private String toIndentedString(Object o) {
    if (o == null)
      return "null"; 
    return o.toString().replace("\n", "\n    ");
  }
}
