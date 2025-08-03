package com.csme.csmeapi.fin.models;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * XAttachmentRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XAttachmentRequest   {
  @JsonProperty("MainRef")
  private String mainRef = null;

  @JsonProperty("AttachmentRef")
  private String attachmentRef = null;

  public XAttachmentRequest mainRef(String mainRef) {
    this.mainRef = mainRef;
    return this;
  }

  /**
   * Main Reference
   * @return mainRef
  **/
  @ApiModelProperty(required = true, value = "Main Reference")
      @NotNull

    public String getMainRef() {
    return mainRef;
  }

  public void setMainRef(String mainRef) {
    this.mainRef = mainRef;
  }

  public XAttachmentRequest attachmentRef(String attachmentRef) {
    this.attachmentRef = attachmentRef;
    return this;
  }

  /**
   * Attachment Ref No
   * @return attachmentRef
  **/
  @ApiModelProperty(required = true, value = "Attachment Ref No")
      @NotNull

    public String getAttachmentRef() {
    return attachmentRef;
  }

  public void setAttachmentRef(String attachmentRef) {
    this.attachmentRef = attachmentRef;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XAttachmentRequest xattachmentRequest = (XAttachmentRequest) o;
    return Objects.equals(this.mainRef, xattachmentRequest.mainRef) &&
        Objects.equals(this.attachmentRef, xattachmentRequest.attachmentRef);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mainRef, attachmentRef);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XAttachmentRequest {\n");
    
    sb.append("    mainRef: ").append(toIndentedString(mainRef)).append("\n");
    sb.append("    attachmentRef: ").append(toIndentedString(attachmentRef)).append("\n");
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
