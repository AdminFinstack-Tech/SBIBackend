package com.csme.csmeapi.fin.models;

import java.util.Objects;
import com.csme.csmeapi.fin.models.XCompanyInformation;
import com.csme.csmeapi.fin.models.XUserInformation;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * XProfileBodyResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XProfileBodyResponse   {
  @JsonProperty("CompanyInformation")
  @Valid
  private List<XCompanyInformation> companyInformation = new ArrayList<>();

  @JsonProperty("UserInformation")
  @Valid
  private List<XUserInformation> userInformation = new ArrayList<>();

  public XProfileBodyResponse companyInformation(List<XCompanyInformation> companyInformation) {
    this.companyInformation = companyInformation;
    return this;
  }

  public XProfileBodyResponse addCompanyInformationItem(XCompanyInformation companyInformationItem) {
    this.companyInformation.add(companyInformationItem);
    return this;
  }

  /**
   * Get companyInformation
   * @return companyInformation
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull
    @Valid
    public List<XCompanyInformation> getCompanyInformation() {
    return companyInformation;
  }

  public void setCompanyInformation(List<XCompanyInformation> companyInformation) {
    this.companyInformation = companyInformation;
  }

  public XProfileBodyResponse userInformation(List<XUserInformation> userInformation) {
    this.userInformation = userInformation;
    return this;
  }

  public XProfileBodyResponse addUserInformationItem(XUserInformation userInformationItem) {
    this.userInformation.add(userInformationItem);
    return this;
  }

  /**
   * Get userInformation
   * @return userInformation
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull
    @Valid
    public List<XUserInformation> getUserInformation() {
    return userInformation;
  }

  public void setUserInformation(List<XUserInformation> userInformation) {
    this.userInformation = userInformation;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XProfileBodyResponse xprofileBodyResponse = (XProfileBodyResponse) o;
    return Objects.equals(this.companyInformation, xprofileBodyResponse.companyInformation) &&
        Objects.equals(this.userInformation, xprofileBodyResponse.userInformation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(companyInformation, userInformation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XProfileBodyResponse {\n");
    
    sb.append("    companyInformation: ").append(toIndentedString(companyInformation)).append("\n");
    sb.append("    userInformation: ").append(toIndentedString(userInformation)).append("\n");
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
