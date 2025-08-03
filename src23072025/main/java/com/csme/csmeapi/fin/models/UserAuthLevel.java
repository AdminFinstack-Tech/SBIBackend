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
 * UserAuthLevel
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class UserAuthLevel   {
  @JsonProperty("productName")
  private String productName = "IWGT";

  @JsonProperty("authLevel")
  private String authLevel = null;

  public UserAuthLevel productName(String productName) {
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

  public UserAuthLevel authLevel(String authLevel) {
    this.authLevel = authLevel;
    return this;
  }

  /**
   * currency for auth matrix
   * @return authLevel
  **/
  @ApiModelProperty(example = "1", value = "currency for auth matrix")
  
    public String getAuthLevel() {
    return authLevel;
  }

  public void setAuthLevel(String authLevel) {
    this.authLevel = authLevel;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserAuthLevel userAuthLevel = (UserAuthLevel) o;
    return Objects.equals(this.productName, userAuthLevel.productName) &&
        Objects.equals(this.authLevel, userAuthLevel.authLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productName, authLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserAuthLevel {\n");
    
    sb.append("    productName: ").append(toIndentedString(productName)).append("\n");
    sb.append("    authLevel: ").append(toIndentedString(authLevel)).append("\n");
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
