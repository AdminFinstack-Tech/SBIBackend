package com.csme.csmeapi.fin.models;

import java.util.Objects;
import com.csme.csmeapi.fin.models.XproductInfoList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * XCompanyInformation
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XCompanyInformation   {
  @JsonProperty("CorporateName")
  private String corporateName = null;

  @JsonProperty("CorporateId")
  private String corporateId = null;

  @JsonProperty("CorporateGroupId")
  private String corporateGroupId = null;

  @JsonProperty("CorporateGroupName")
  private String corporateGroupName = null;

  @JsonProperty("CorporateArabicName")
  private String corporateArabicName = null;

  @JsonProperty("selfAuthorize")
  private String selfAuthorize = "true";

  @JsonProperty("country")
  private String country = "SA";

  /**
   * Is bank
   */
  public enum IsBankEnum {
    T("T"),
    
    F("F");

    private String value;

    IsBankEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static IsBankEnum fromValue(String text) {
      for (IsBankEnum b : IsBankEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("isBank")
  private IsBankEnum isBank = IsBankEnum.F;

  @JsonProperty("baseCCY")
  private String baseCCY = "SAR";

  /**
   * Amount Format
   */
  public enum AmtFormatEnum {
    EU("EU"),
    
    US("US");

    private String value;

    AmtFormatEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AmtFormatEnum fromValue(String text) {
      for (AmtFormatEnum b : AmtFormatEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("amtFormat")
  private AmtFormatEnum amtFormat = AmtFormatEnum.US;

  @JsonProperty("productsAccessList")
  @Valid
  private List<XproductInfoList> productsAccessList = null;

  public XCompanyInformation corporateName(String corporateName) {
    this.corporateName = corporateName;
    return this;
  }

  /**
   * Corporate Name
   * @return corporateName
  **/
  @ApiModelProperty(value = "Corporate Name")
  
    public String getCorporateName() {
    return corporateName;
  }

  public void setCorporateName(String corporateName) {
    this.corporateName = corporateName;
  }

  public XCompanyInformation corporateId(String corporateId) {
    this.corporateId = corporateId;
    return this;
  }

  /**
   * Corporate Id
   * @return corporateId
  **/
  @ApiModelProperty(value = "Corporate Id")
  
    public String getCorporateId() {
    return corporateId;
  }

  public void setCorporateId(String corporateId) {
    this.corporateId = corporateId;
  }

  public XCompanyInformation corporateGroupId(String corporateGroupId) {
    this.corporateGroupId = corporateGroupId;
    return this;
  }

  /**
   * Company Group Id*(Corporate CIF)
   * @return corporateGroupId
  **/
  @ApiModelProperty(example = "ARABTECH", value = "Company Group Id*(Corporate CIF)")
  
    public String getCorporateGroupId() {
    return corporateGroupId;
  }

  public void setCorporateGroupId(String corporateGroupId) {
    this.corporateGroupId = corporateGroupId;
  }

  public XCompanyInformation corporateGroupName(String corporateGroupName) {
    this.corporateGroupName = corporateGroupName;
    return this;
  }

  /**
   * Company Group Name*
   * @return corporateGroupName
  **/
  @ApiModelProperty(example = "CSOFFICE", value = "Company Group Name*")
  
    public String getCorporateGroupName() {
    return corporateGroupName;
  }

  public void setCorporateGroupName(String corporateGroupName) {
    this.corporateGroupName = corporateGroupName;
  }

  public XCompanyInformation corporateArabicName(String corporateArabicName) {
    this.corporateArabicName = corporateArabicName;
    return this;
  }

  /**
   * Company Name in Arabic
   * @return corporateArabicName
  **/
  @ApiModelProperty(example = "CSOFFICE", value = "Company Name in Arabic")
  
    public String getCorporateArabicName() {
    return corporateArabicName;
  }

  public void setCorporateArabicName(String corporateArabicName) {
    this.corporateArabicName = corporateArabicName;
  }

  public XCompanyInformation selfAuthorize(String selfAuthorize) {
    this.selfAuthorize = selfAuthorize;
    return this;
  }

  /**
   * Transaction Self Authorization Flag
   * @return selfAuthorize
  **/
  @ApiModelProperty(value = "Transaction Self Authorization Flag")
  
    public String getSelfAuthorize() {
    return selfAuthorize;
  }

  public void setSelfAuthorize(String selfAuthorize) {
    this.selfAuthorize = selfAuthorize;
  }

  public XCompanyInformation country(String country) {
    this.country = country;
    return this;
  }

  /**
   * Country (ISO Country Values)
   * @return country
  **/
  @ApiModelProperty(value = "Country (ISO Country Values)")
  
    public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public XCompanyInformation isBank(IsBankEnum isBank) {
    this.isBank = isBank;
    return this;
  }

  /**
   * Is bank
   * @return isBank
  **/
  @ApiModelProperty(value = "Is bank")
  
    public IsBankEnum getIsBank() {
    return isBank;
  }

  public void setIsBank(IsBankEnum isBank) {
    this.isBank = isBank;
  }

  public XCompanyInformation baseCCY(String baseCCY) {
    this.baseCCY = baseCCY;
    return this;
  }

  /**
   * Base Currency (ISO Currency Values)
   * @return baseCCY
  **/
  @ApiModelProperty(value = "Base Currency (ISO Currency Values)")
  
    public String getBaseCCY() {
    return baseCCY;
  }

  public void setBaseCCY(String baseCCY) {
    this.baseCCY = baseCCY;
  }

  public XCompanyInformation amtFormat(AmtFormatEnum amtFormat) {
    this.amtFormat = amtFormat;
    return this;
  }

  /**
   * Amount Format
   * @return amtFormat
  **/
  @ApiModelProperty(value = "Amount Format")
  
    public AmtFormatEnum getAmtFormat() {
    return amtFormat;
  }

  public void setAmtFormat(AmtFormatEnum amtFormat) {
    this.amtFormat = amtFormat;
  }

  public XCompanyInformation productsAccessList(List<XproductInfoList> productsAccessList) {
    this.productsAccessList = productsAccessList;
    return this;
  }

  public XCompanyInformation addProductsAccessListItem(XproductInfoList productsAccessListItem) {
    if (this.productsAccessList == null) {
      this.productsAccessList = new ArrayList<>();
    }
    this.productsAccessList.add(productsAccessListItem);
    return this;
  }

  /**
   * Get productsAccessList
   * @return productsAccessList
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<XproductInfoList> getProductsAccessList() {
    return productsAccessList;
  }

  public void setProductsAccessList(List<XproductInfoList> productsAccessList) {
    this.productsAccessList = productsAccessList;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XCompanyInformation xcompanyInformation = (XCompanyInformation) o;
    return Objects.equals(this.corporateName, xcompanyInformation.corporateName) &&
        Objects.equals(this.corporateId, xcompanyInformation.corporateId) &&
        Objects.equals(this.corporateGroupId, xcompanyInformation.corporateGroupId) &&
        Objects.equals(this.corporateGroupName, xcompanyInformation.corporateGroupName) &&
        Objects.equals(this.corporateArabicName, xcompanyInformation.corporateArabicName) &&
        Objects.equals(this.selfAuthorize, xcompanyInformation.selfAuthorize) &&
        Objects.equals(this.country, xcompanyInformation.country) &&
        Objects.equals(this.isBank, xcompanyInformation.isBank) &&
        Objects.equals(this.baseCCY, xcompanyInformation.baseCCY) &&
        Objects.equals(this.amtFormat, xcompanyInformation.amtFormat) &&
        Objects.equals(this.productsAccessList, xcompanyInformation.productsAccessList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(corporateName, corporateId, corporateGroupId, corporateGroupName, corporateArabicName, selfAuthorize, country, isBank, baseCCY, amtFormat, productsAccessList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XCompanyInformation {\n");
    
    sb.append("    corporateName: ").append(toIndentedString(corporateName)).append("\n");
    sb.append("    corporateId: ").append(toIndentedString(corporateId)).append("\n");
    sb.append("    corporateGroupId: ").append(toIndentedString(corporateGroupId)).append("\n");
    sb.append("    corporateGroupName: ").append(toIndentedString(corporateGroupName)).append("\n");
    sb.append("    corporateArabicName: ").append(toIndentedString(corporateArabicName)).append("\n");
    sb.append("    selfAuthorize: ").append(toIndentedString(selfAuthorize)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    isBank: ").append(toIndentedString(isBank)).append("\n");
    sb.append("    baseCCY: ").append(toIndentedString(baseCCY)).append("\n");
    sb.append("    amtFormat: ").append(toIndentedString(amtFormat)).append("\n");
    sb.append("    productsAccessList: ").append(toIndentedString(productsAccessList)).append("\n");
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
