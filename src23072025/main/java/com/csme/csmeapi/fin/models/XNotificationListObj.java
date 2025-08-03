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
 * XNotificationListObj
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XNotificationListObj   {
  @JsonProperty("NotificationListInfo")
  private Object notificationListInfo = null;

  public XNotificationListObj notificationListInfo(Object notificationListInfo) {
    this.notificationListInfo = notificationListInfo;
    return this;
  }

  /**
   * Notification List Info
   * @return notificationListInfo
  **/
  @ApiModelProperty(value = "Notification List Info")
  
    public Object getNotificationListInfo() {
    return notificationListInfo;
  }

  public void setNotificationListInfo(Object notificationListInfo) {
    this.notificationListInfo = notificationListInfo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XNotificationListObj xnotificationListObj = (XNotificationListObj) o;
    return Objects.equals(this.notificationListInfo, xnotificationListObj.notificationListInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(notificationListInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XNotificationListObj {\n");
    
    sb.append("    notificationListInfo: ").append(toIndentedString(notificationListInfo)).append("\n");
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
