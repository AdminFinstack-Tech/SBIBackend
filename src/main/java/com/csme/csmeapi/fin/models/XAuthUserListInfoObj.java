package com.csme.csmeapi.fin.models;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * XAuthUserListInfoObj
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XAuthUserListInfoObj   {
  @JsonProperty("PendingUserList")
  private Object pendingUserList = null;

  @JsonProperty("AuthorizedUserList")
  private Object authorizedUserList = null;

  public XAuthUserListInfoObj pendingUserList(Object pendingUserList) {
    this.pendingUserList = pendingUserList;
    return this;
  }

  /**
   * Get pendingUserList
   * @return pendingUserList
  **/
  @ApiModelProperty(value = "")
  
    public Object getPendingUserList() {
    return pendingUserList;
  }

  public void setPendingUserList(Object pendingUserList) {
    this.pendingUserList = pendingUserList;
  }

  public XAuthUserListInfoObj authorizedUserList(Object authorizedUserList) {
    this.authorizedUserList = authorizedUserList;
    return this;
  }

  /**
   * Get authorizedUserList
   * @return authorizedUserList
  **/
  @ApiModelProperty(value = "")
  
    public Object getAuthorizedUserList() {
    return authorizedUserList;
  }

  public void setAuthorizedUserList(Object authorizedUserList) {
    this.authorizedUserList = authorizedUserList;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XAuthUserListInfoObj xauthUserListInfoObj = (XAuthUserListInfoObj) o;
    return Objects.equals(this.pendingUserList, xauthUserListInfoObj.pendingUserList) &&
        Objects.equals(this.authorizedUserList, xauthUserListInfoObj.authorizedUserList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pendingUserList, authorizedUserList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XAuthUserListInfoObj {\n");
    
    sb.append("    pendingUserList: ").append(toIndentedString(pendingUserList)).append("\n");
    sb.append("    authorizedUserList: ").append(toIndentedString(authorizedUserList)).append("\n");
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
