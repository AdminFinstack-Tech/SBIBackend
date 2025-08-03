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
 * AttachmentInfoListObj
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class AttachmentInfoListObj   {
  @JsonProperty("AttachmentInfoList")
  private Object attachmentInfoList = null;

  public AttachmentInfoListObj attachmentInfoList(Object attachmentInfoList) {
    this.attachmentInfoList = attachmentInfoList;
    return this;
  }

  /**
   * Get attachmentInfoList
   * @return attachmentInfoList
  **/
  @ApiModelProperty(value = "")
  
    public Object getAttachmentInfoList() {
    return attachmentInfoList;
  }

  public void setAttachmentInfoList(Object attachmentInfoList) {
    this.attachmentInfoList = attachmentInfoList;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttachmentInfoListObj attachmentInfoListObj = (AttachmentInfoListObj) o;
    return Objects.equals(this.attachmentInfoList, attachmentInfoListObj.attachmentInfoList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attachmentInfoList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttachmentInfoListObj {\n");
    
    sb.append("    attachmentInfoList: ").append(toIndentedString(attachmentInfoList)).append("\n");
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
