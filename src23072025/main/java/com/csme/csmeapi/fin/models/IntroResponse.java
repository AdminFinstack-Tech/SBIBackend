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
 * IntroResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class IntroResponse   {
  @JsonProperty("PageInfo")
  private Object pageInfo = null;

  @JsonProperty("LoginPageInfo")
  private Object loginPageInfo = null;

  @JsonProperty("LandingPage")
  private Object landingPage = null;

  @JsonProperty("VersionInfo")
  private Object versionInfo = null;

  @JsonProperty("Disclaimer")
  private Object disclaimer = null;

  public IntroResponse pageInfo(Object pageInfo) {
    this.pageInfo = pageInfo;
    return this;
  }

  /**
   * Get pageInfo
   * @return pageInfo
  **/
  @ApiModelProperty(example = "{   \"pageInfo1\": {     \"Text\": \"Sample Text\"   },   \"PageInfo2\": {     \"Image\": \"http://dev-arb:9081/cs-ce-sec-api-0.0.1/landing.jpg\"   } }", value = "")
  
    public Object getPageInfo() {
    return pageInfo;
  }

  public void setPageInfo(Object pageInfo) {
    this.pageInfo = pageInfo;
  }

  public IntroResponse loginPageInfo(Object loginPageInfo) {
    this.loginPageInfo = loginPageInfo;
    return this;
  }

  /**
   * Get loginPageInfo
   * @return loginPageInfo
  **/
  @ApiModelProperty(example = "{   \"BackgroundImage\": \"http://dev-arb:9081/cs-ce-sec-api-0.0.1/landing.jpg\" }", value = "")
  
    public Object getLoginPageInfo() {
    return loginPageInfo;
  }

  public void setLoginPageInfo(Object loginPageInfo) {
    this.loginPageInfo = loginPageInfo;
  }

  public IntroResponse landingPage(Object landingPage) {
    this.landingPage = landingPage;
    return this;
  }

  /**
   * Get landingPage
   * @return landingPage
  **/
  @ApiModelProperty(example = "{   \"BackgroundImage\": \"http://dev-arb:9081/cs-ce-sec-api-0.0.1/landing.jpg\" }", value = "")
  
    public Object getLandingPage() {
    return landingPage;
  }

  public void setLandingPage(Object landingPage) {
    this.landingPage = landingPage;
  }

  public IntroResponse versionInfo(Object versionInfo) {
    this.versionInfo = versionInfo;
    return this;
  }

  /**
   * Get versionInfo
   * @return versionInfo
  **/
  @ApiModelProperty(example = "1.0.0", value = "")
  
    public Object getVersionInfo() {
    return versionInfo;
  }

  public void setVersionInfo(Object versionInfo) {
    this.versionInfo = versionInfo;
  }

  public IntroResponse disclaimer(Object disclaimer) {
    this.disclaimer = disclaimer;
    return this;
  }

  /**
   * Get disclaimer
   * @return disclaimer
  **/
  @ApiModelProperty(value = "")
  
    public Object getDisclaimer() {
    return disclaimer;
  }

  public void setDisclaimer(Object disclaimer) {
    this.disclaimer = disclaimer;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IntroResponse introResponse = (IntroResponse) o;
    return Objects.equals(this.pageInfo, introResponse.pageInfo) &&
        Objects.equals(this.loginPageInfo, introResponse.loginPageInfo) &&
        Objects.equals(this.landingPage, introResponse.landingPage) &&
        Objects.equals(this.versionInfo, introResponse.versionInfo) &&
        Objects.equals(this.disclaimer, introResponse.disclaimer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pageInfo, loginPageInfo, landingPage, versionInfo, disclaimer);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IntroResponse {\n");
    
    sb.append("    pageInfo: ").append(toIndentedString(pageInfo)).append("\n");
    sb.append("    loginPageInfo: ").append(toIndentedString(loginPageInfo)).append("\n");
    sb.append("    landingPage: ").append(toIndentedString(landingPage)).append("\n");
    sb.append("    versionInfo: ").append(toIndentedString(versionInfo)).append("\n");
    sb.append("    disclaimer: ").append(toIndentedString(disclaimer)).append("\n");
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
