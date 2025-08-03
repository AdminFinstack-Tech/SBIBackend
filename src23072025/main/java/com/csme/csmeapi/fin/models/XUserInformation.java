package com.csme.csmeapi.fin.models;

import java.util.Objects;
import com.csme.csmeapi.fin.models.UserAuthLevel;
import com.csme.csmeapi.fin.models.XUserInformationProfilepic;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * XUserInformation
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XUserInformation   {
  @JsonProperty("profilepic")
  private XUserInformationProfilepic profilepic = null;

  @JsonProperty("userId")
  private String userId = null;

  @JsonProperty("userDescription")
  private String userDescription = null;

  @JsonProperty("companyId")
  private String companyId = "CSOFFICE";

  @JsonProperty("defaultCompanyId")
  private String defaultCompanyId = "CSOFFICE";

  @JsonProperty("authorizatoinLevel")
  private BigDecimal authorizatoinLevel = null;

  /**
   * Multi Login
   */
  public enum MultiLoginEnum {
    T("T"),
    
    F("F");

    private String value;

    MultiLoginEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static MultiLoginEnum fromValue(String text) {
      for (MultiLoginEnum b : MultiLoginEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("multiLogin")
  private MultiLoginEnum multiLogin = MultiLoginEnum.T;

  @JsonProperty("language")
  private String language = "EN";

  @JsonProperty("Availablelanguage")
  private String availablelanguage = null;

  @JsonProperty("MaxCriteriaOption")
  private String maxCriteriaOption = "5";

  /**
   * Active Status
   */
  public enum ActiveStatusEnum {
    T("T"),
    
    F("F");

    private String value;

    ActiveStatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ActiveStatusEnum fromValue(String text) {
      for (ActiveStatusEnum b : ActiveStatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("activeStatus")
  private ActiveStatusEnum activeStatus = ActiveStatusEnum.T;

  @JsonProperty("effectiveDate")
  private String effectiveDate = "System Date";

  @JsonProperty("expiryDate")
  private String expiryDate = "System Date + 100 Years";

  @JsonProperty("emailId")
  private String emailId = null;

  @JsonProperty("mobileNumber")
  private String mobileNumber = null;

  @JsonProperty("tokenId")
  private String tokenId = null;

  @JsonProperty("authorizationPref")
  private String authorizationPref = null;

  @JsonProperty("productAuthLevel")
  @Valid
  private List<UserAuthLevel> productAuthLevel = null;

  @JsonProperty("operatorRoles")
  @Valid
  private List<String> operatorRoles = null;

  public XUserInformation profilepic(XUserInformationProfilepic profilepic) {
    this.profilepic = profilepic;
    return this;
  }

  /**
   * Get profilepic
   * @return profilepic
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public XUserInformationProfilepic getProfilepic() {
    return profilepic;
  }

  public void setProfilepic(XUserInformationProfilepic profilepic) {
    this.profilepic = profilepic;
  }

  public XUserInformation userId(String userId) {
    this.userId = userId;
    return this;
  }

  /**
   * User Id*
   * @return userId
  **/
  @ApiModelProperty(value = "User Id*")
  
    public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public XUserInformation userDescription(String userDescription) {
    this.userDescription = userDescription;
    return this;
  }

  /**
   * User Description*
   * @return userDescription
  **/
  @ApiModelProperty(value = "User Description*")
  
    public String getUserDescription() {
    return userDescription;
  }

  public void setUserDescription(String userDescription) {
    this.userDescription = userDescription;
  }

  public XUserInformation companyId(String companyId) {
    this.companyId = companyId;
    return this;
  }

  /**
   * Company Id*
   * @return companyId
  **/
  @ApiModelProperty(value = "Company Id*")
  
    public String getCompanyId() {
    return companyId;
  }

  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }

  public XUserInformation defaultCompanyId(String defaultCompanyId) {
    this.defaultCompanyId = defaultCompanyId;
    return this;
  }

  /**
   * Default Company Id
   * @return defaultCompanyId
  **/
  @ApiModelProperty(value = "Default Company Id")
  
    public String getDefaultCompanyId() {
    return defaultCompanyId;
  }

  public void setDefaultCompanyId(String defaultCompanyId) {
    this.defaultCompanyId = defaultCompanyId;
  }

  public XUserInformation authorizatoinLevel(BigDecimal authorizatoinLevel) {
    this.authorizatoinLevel = authorizatoinLevel;
    return this;
  }

  /**
   * Authorization Level
   * minimum: 1
   * maximum: 8
   * @return authorizatoinLevel
  **/
  @ApiModelProperty(value = "Authorization Level")
  
    @Valid
  @DecimalMin("1") @DecimalMax("8")   public BigDecimal getAuthorizatoinLevel() {
    return authorizatoinLevel;
  }

  public void setAuthorizatoinLevel(BigDecimal authorizatoinLevel) {
    this.authorizatoinLevel = authorizatoinLevel;
  }

  public XUserInformation multiLogin(MultiLoginEnum multiLogin) {
    this.multiLogin = multiLogin;
    return this;
  }

  /**
   * Multi Login
   * @return multiLogin
  **/
  @ApiModelProperty(value = "Multi Login")
  
    public MultiLoginEnum getMultiLogin() {
    return multiLogin;
  }

  public void setMultiLogin(MultiLoginEnum multiLogin) {
    this.multiLogin = multiLogin;
  }

  public XUserInformation language(String language) {
    this.language = language;
    return this;
  }

  /**
   * Language
   * @return language
  **/
  @ApiModelProperty(value = "Language")
  
    public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public XUserInformation availablelanguage(String availablelanguage) {
    this.availablelanguage = availablelanguage;
    return this;
  }

  /**
   * Language
   * @return availablelanguage
  **/
  @ApiModelProperty(example = "EN;FR;AR", value = "Language")
  
    public String getAvailablelanguage() {
    return availablelanguage;
  }

  public void setAvailablelanguage(String availablelanguage) {
    this.availablelanguage = availablelanguage;
  }

  public XUserInformation maxCriteriaOption(String maxCriteriaOption) {
    this.maxCriteriaOption = maxCriteriaOption;
    return this;
  }

  /**
   * Maximum Criteria Saved Option Count
   * @return maxCriteriaOption
  **/
  @ApiModelProperty(value = "Maximum Criteria Saved Option Count")
  
    public String getMaxCriteriaOption() {
    return maxCriteriaOption;
  }

  public void setMaxCriteriaOption(String maxCriteriaOption) {
    this.maxCriteriaOption = maxCriteriaOption;
  }

  public XUserInformation activeStatus(ActiveStatusEnum activeStatus) {
    this.activeStatus = activeStatus;
    return this;
  }

  /**
   * Active Status
   * @return activeStatus
  **/
  @ApiModelProperty(value = "Active Status")
  
    public ActiveStatusEnum getActiveStatus() {
    return activeStatus;
  }

  public void setActiveStatus(ActiveStatusEnum activeStatus) {
    this.activeStatus = activeStatus;
  }

  public XUserInformation effectiveDate(String effectiveDate) {
    this.effectiveDate = effectiveDate;
    return this;
  }

  /**
   * Effective Date
   * @return effectiveDate
  **/
  @ApiModelProperty(example = "2020-12-21", value = "Effective Date")
  
    public String getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(String effectiveDate) {
    this.effectiveDate = effectiveDate;
  }

  public XUserInformation expiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
    return this;
  }

  /**
   * Expiry Date
   * @return expiryDate
  **/
  @ApiModelProperty(example = "2022-12-21", value = "Expiry Date")
  
    public String getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
  }

  public XUserInformation emailId(String emailId) {
    this.emailId = emailId;
    return this;
  }

  /**
   * Company Email Id
   * @return emailId
  **/
  @ApiModelProperty(value = "Company Email Id")
  
    public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public XUserInformation mobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
    return this;
  }

  /**
   * Company Mobile Number
   * @return mobileNumber
  **/
  @ApiModelProperty(value = "Company Mobile Number")
  
    public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public XUserInformation tokenId(String tokenId) {
    this.tokenId = tokenId;
    return this;
  }

  /**
   * Token Id Value
   * @return tokenId
  **/
  @ApiModelProperty(value = "Token Id Value")
  
    public String getTokenId() {
    return tokenId;
  }

  public void setTokenId(String tokenId) {
    this.tokenId = tokenId;
  }

  public XUserInformation authorizationPref(String authorizationPref) {
    this.authorizationPref = authorizationPref;
    return this;
  }

  /**
   * Authorization Preference(OTP/Hard Token/Soft Token)*
   * @return authorizationPref
  **/
  @ApiModelProperty(value = "Authorization Preference(OTP/Hard Token/Soft Token)*")
  
    public String getAuthorizationPref() {
    return authorizationPref;
  }

  public void setAuthorizationPref(String authorizationPref) {
    this.authorizationPref = authorizationPref;
  }

  public XUserInformation productAuthLevel(List<UserAuthLevel> productAuthLevel) {
    this.productAuthLevel = productAuthLevel;
    return this;
  }

  public XUserInformation addProductAuthLevelItem(UserAuthLevel productAuthLevelItem) {
    if (this.productAuthLevel == null) {
      this.productAuthLevel = new ArrayList<>();
    }
    this.productAuthLevel.add(productAuthLevelItem);
    return this;
  }

  /**
   * Get productAuthLevel
   * @return productAuthLevel
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<UserAuthLevel> getProductAuthLevel() {
    return productAuthLevel;
  }

  public void setProductAuthLevel(List<UserAuthLevel> productAuthLevel) {
    this.productAuthLevel = productAuthLevel;
  }

  public XUserInformation operatorRoles(List<String> operatorRoles) {
    this.operatorRoles = operatorRoles;
    return this;
  }

  public XUserInformation addOperatorRolesItem(String operatorRolesItem) {
    if (this.operatorRoles == null) {
      this.operatorRoles = new ArrayList<>();
    }
    this.operatorRoles.add(operatorRolesItem);
    return this;
  }

  /**
   * Get operatorRoles
   * @return operatorRoles
  **/
  @ApiModelProperty(value = "")
  
    public List<String> getOperatorRoles() {
    return operatorRoles;
  }

  public void setOperatorRoles(List<String> operatorRoles) {
    this.operatorRoles = operatorRoles;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XUserInformation xuserInformation = (XUserInformation) o;
    return Objects.equals(this.profilepic, xuserInformation.profilepic) &&
        Objects.equals(this.userId, xuserInformation.userId) &&
        Objects.equals(this.userDescription, xuserInformation.userDescription) &&
        Objects.equals(this.companyId, xuserInformation.companyId) &&
        Objects.equals(this.defaultCompanyId, xuserInformation.defaultCompanyId) &&
        Objects.equals(this.authorizatoinLevel, xuserInformation.authorizatoinLevel) &&
        Objects.equals(this.multiLogin, xuserInformation.multiLogin) &&
        Objects.equals(this.language, xuserInformation.language) &&
        Objects.equals(this.availablelanguage, xuserInformation.availablelanguage) &&
        Objects.equals(this.maxCriteriaOption, xuserInformation.maxCriteriaOption) &&
        Objects.equals(this.activeStatus, xuserInformation.activeStatus) &&
        Objects.equals(this.effectiveDate, xuserInformation.effectiveDate) &&
        Objects.equals(this.expiryDate, xuserInformation.expiryDate) &&
        Objects.equals(this.emailId, xuserInformation.emailId) &&
        Objects.equals(this.mobileNumber, xuserInformation.mobileNumber) &&
        Objects.equals(this.tokenId, xuserInformation.tokenId) &&
        Objects.equals(this.authorizationPref, xuserInformation.authorizationPref) &&
        Objects.equals(this.productAuthLevel, xuserInformation.productAuthLevel) &&
        Objects.equals(this.operatorRoles, xuserInformation.operatorRoles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(profilepic, userId, userDescription, companyId, defaultCompanyId, authorizatoinLevel, multiLogin, language, availablelanguage, maxCriteriaOption, activeStatus, effectiveDate, expiryDate, emailId, mobileNumber, tokenId, authorizationPref, productAuthLevel, operatorRoles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XUserInformation {\n");
    
    sb.append("    profilepic: ").append(toIndentedString(profilepic)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    userDescription: ").append(toIndentedString(userDescription)).append("\n");
    sb.append("    companyId: ").append(toIndentedString(companyId)).append("\n");
    sb.append("    defaultCompanyId: ").append(toIndentedString(defaultCompanyId)).append("\n");
    sb.append("    authorizatoinLevel: ").append(toIndentedString(authorizatoinLevel)).append("\n");
    sb.append("    multiLogin: ").append(toIndentedString(multiLogin)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    availablelanguage: ").append(toIndentedString(availablelanguage)).append("\n");
    sb.append("    maxCriteriaOption: ").append(toIndentedString(maxCriteriaOption)).append("\n");
    sb.append("    activeStatus: ").append(toIndentedString(activeStatus)).append("\n");
    sb.append("    effectiveDate: ").append(toIndentedString(effectiveDate)).append("\n");
    sb.append("    expiryDate: ").append(toIndentedString(expiryDate)).append("\n");
    sb.append("    emailId: ").append(toIndentedString(emailId)).append("\n");
    sb.append("    mobileNumber: ").append(toIndentedString(mobileNumber)).append("\n");
    sb.append("    tokenId: ").append(toIndentedString(tokenId)).append("\n");
    sb.append("    authorizationPref: ").append(toIndentedString(authorizationPref)).append("\n");
    sb.append("    productAuthLevel: ").append(toIndentedString(productAuthLevel)).append("\n");
    sb.append("    operatorRoles: ").append(toIndentedString(operatorRoles)).append("\n");
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
