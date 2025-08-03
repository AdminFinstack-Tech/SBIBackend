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
 * XPreferenceResponseObj
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XPreferenceResponseObj   {
  @JsonProperty("PreferenceListInfo")
  private Object preferenceListInfo = null;

  public XPreferenceResponseObj preferenceListInfo(Object preferenceListInfo) {
    this.preferenceListInfo = preferenceListInfo;
    return this;
  }

  /**
   * Preference List Information
   * @return preferenceListInfo
  **/
  @ApiModelProperty(value = "Preference List Information")
  
    public Object getPreferenceListInfo() {
    return preferenceListInfo;
  }

  public void setPreferenceListInfo(Object preferenceListInfo) {
    this.preferenceListInfo = preferenceListInfo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XPreferenceResponseObj xpreferenceResponseObj = (XPreferenceResponseObj) o;
    return Objects.equals(this.preferenceListInfo, xpreferenceResponseObj.preferenceListInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(preferenceListInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XPreferenceResponseObj {\n");
    
    sb.append("    preferenceListInfo: ").append(toIndentedString(preferenceListInfo)).append("\n");
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
