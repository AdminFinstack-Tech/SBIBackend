package com.csme.csmeapi.fin.models;

import java.util.Objects;
import com.csme.csmeapi.fin.models.XSaveFBTokenResponseObj;
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
 * SaveFBTokenRequestPost
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class SaveFBTokenRequestPost   {
  @JsonProperty("CorporateId")
  private String corporateId = null;

  @JsonProperty("UserId")
  private String userId = null;

  @JsonProperty("SaveFBTokenInfo")
  @Valid
  private List<XSaveFBTokenResponseObj> saveFBTokenInfo = null;

  public SaveFBTokenRequestPost corporateId(String corporateId) {
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

  public SaveFBTokenRequestPost userId(String userId) {
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

  public SaveFBTokenRequestPost saveFBTokenInfo(List<XSaveFBTokenResponseObj> saveFBTokenInfo) {
    this.saveFBTokenInfo = saveFBTokenInfo;
    return this;
  }

  public SaveFBTokenRequestPost addSaveFBTokenInfoItem(XSaveFBTokenResponseObj saveFBTokenInfoItem) {
    if (this.saveFBTokenInfo == null) {
      this.saveFBTokenInfo = new ArrayList<>();
    }
    this.saveFBTokenInfo.add(saveFBTokenInfoItem);
    return this;
  }

  /**
   * Get saveFBTokenInfo
   * @return saveFBTokenInfo
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<XSaveFBTokenResponseObj> getSaveFBTokenInfo() {
    return saveFBTokenInfo;
  }

  public void setSaveFBTokenInfo(List<XSaveFBTokenResponseObj> saveFBTokenInfo) {
    this.saveFBTokenInfo = saveFBTokenInfo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SaveFBTokenRequestPost saveFBTokenRequestPost = (SaveFBTokenRequestPost) o;
    return Objects.equals(this.corporateId, saveFBTokenRequestPost.corporateId) &&
        Objects.equals(this.userId, saveFBTokenRequestPost.userId) &&
        Objects.equals(this.saveFBTokenInfo, saveFBTokenRequestPost.saveFBTokenInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(corporateId, userId, saveFBTokenInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SaveFBTokenRequestPost {\n");
    
    sb.append("    corporateId: ").append(toIndentedString(corporateId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    saveFBTokenInfo: ").append(toIndentedString(saveFBTokenInfo)).append("\n");
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
