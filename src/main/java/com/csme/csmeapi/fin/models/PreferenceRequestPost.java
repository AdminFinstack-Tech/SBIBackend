package com.csme.csmeapi.fin.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * PreferenceRequestPost
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class PreferenceRequestPost   {
  @JsonProperty("CorporateId")
  private String corporateId = null;

  @JsonProperty("UserId")
  private String userId = null;

  @JsonProperty("SavePreferenceRespInfo")
  @Valid
  private List<XPreferenceResponseObj> savePreferenceRespInfo = null;

  public PreferenceRequestPost corporateId(String corporateId) {
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

  public PreferenceRequestPost userId(String userId) {
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

  public PreferenceRequestPost savePreferenceRespInfo(List<XPreferenceResponseObj> savePreferenceRespInfo) {
    this.savePreferenceRespInfo = savePreferenceRespInfo;
    return this;
  }

  public PreferenceRequestPost addSavePreferenceRespInfoItem(XPreferenceResponseObj savePreferenceRespInfoItem) {
    if (this.savePreferenceRespInfo == null) {
      this.savePreferenceRespInfo = new ArrayList<>();
    }
    this.savePreferenceRespInfo.add(savePreferenceRespInfoItem);
    return this;
  }

  /**
   * Get savePreferenceRespInfo
   * @return savePreferenceRespInfo
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<XPreferenceResponseObj> getSavePreferenceRespInfo() {
    return savePreferenceRespInfo;
  }

  public void setSavePreferenceRespInfo(List<XPreferenceResponseObj> savePreferenceRespInfo) {
    this.savePreferenceRespInfo = savePreferenceRespInfo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PreferenceRequestPost preferenceRequestPost = (PreferenceRequestPost) o;
    return Objects.equals(this.corporateId, preferenceRequestPost.corporateId) &&
        Objects.equals(this.userId, preferenceRequestPost.userId) &&
        Objects.equals(this.savePreferenceRespInfo, preferenceRequestPost.savePreferenceRespInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(corporateId, userId, savePreferenceRespInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PreferenceRequestPost {\n");
    
    sb.append("    corporateId: ").append(toIndentedString(corporateId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    savePreferenceRespInfo: ").append(toIndentedString(savePreferenceRespInfo)).append("\n");
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
