package com.csme.csmeapi.mobile.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * AttachmentsObj
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-01-29T10:46:11.400+04:00[Asia/Muscat]")
public class AttachmentsObj   {
  @JsonProperty("FilenetReference")
  private String filenetReference = null;

  @JsonProperty("AttachmentName")
  private String attachmentName = null;

  public AttachmentsObj filenetReference(String filenetReference) {
    this.filenetReference = filenetReference;
    return this;
  }

  /**
   * Filenet Reference Number
   * @return filenetReference
  **/
  @ApiModelProperty(value = "Filenet Reference Number")
  
    public String getFilenetReference() {
    return filenetReference;
  }

  public void setFilenetReference(String filenetReference) {
    this.filenetReference = filenetReference;
  }

  public AttachmentsObj attachmentName(String attachmentName) {
    this.attachmentName = attachmentName;
    return this;
  }

  /**
   * Name of the attachment
   * @return attachmentName
  **/
  @ApiModelProperty(value = "Name of the attachment")
  
    public String getAttachmentName() {
    return attachmentName;
  }

  public void setAttachmentName(String attachmentName) {
    this.attachmentName = attachmentName;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttachmentsObj attachmentsObj = (AttachmentsObj) o;
    return Objects.equals(this.filenetReference, attachmentsObj.filenetReference) &&
        Objects.equals(this.attachmentName, attachmentsObj.attachmentName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(filenetReference, attachmentName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttachmentsObj {\n");
    
    sb.append("    filenetReference: ").append(toIndentedString(filenetReference)).append("\n");
    sb.append("    attachmentName: ").append(toIndentedString(attachmentName)).append("\n");
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
