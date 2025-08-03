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
 * XAttachementInfoObj
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XAttachementInfoObj   {
  @JsonProperty("XAttachementInfoListObj")
  private Object xattachementInfoListObj = null;

  public XAttachementInfoObj xattachementInfoListObj(Object xattachementInfoListObj) {
    this.xattachementInfoListObj = xattachementInfoListObj;
    return this;
  }

  /**
   * Get xattachementInfoListObj
   * @return xattachementInfoListObj
  **/
  @ApiModelProperty(value = "")
  
    public Object getXattachementInfoListObj() {
    return xattachementInfoListObj;
  }

  public void setXattachementInfoListObj(Object xattachementInfoListObj) {
    this.xattachementInfoListObj = xattachementInfoListObj;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XAttachementInfoObj xattachementInfoObj = (XAttachementInfoObj) o;
    return Objects.equals(this.xattachementInfoListObj, xattachementInfoObj.xattachementInfoListObj);
  }

  @Override
  public int hashCode() {
    return Objects.hash(xattachementInfoListObj);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XAttachementInfoObj {\n");
    
    sb.append("    xattachementInfoListObj: ").append(toIndentedString(xattachementInfoListObj)).append("\n");
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
