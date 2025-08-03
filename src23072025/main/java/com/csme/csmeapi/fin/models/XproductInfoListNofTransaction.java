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
 * XproductInfoListNofTransaction
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-09-23T22:57:54.741+04:00[Asia/Muscat]")
public class XproductInfoListNofTransaction   {
  @JsonProperty("PendingCount")
  private String pendingCount = null;

  @JsonProperty("TotalCount")
  private String totalCount = null;

  public XproductInfoListNofTransaction pendingCount(String pendingCount) {
    this.pendingCount = pendingCount;
    return this;
  }

  /**
   * Get pendingCount
   * @return pendingCount
  **/
  @ApiModelProperty(value = "")
  
    public String getPendingCount() {
    return pendingCount;
  }

  public void setPendingCount(String pendingCount) {
    this.pendingCount = pendingCount;
  }

  public XproductInfoListNofTransaction totalCount(String totalCount) {
    this.totalCount = totalCount;
    return this;
  }

  /**
   * Get totalCount
   * @return totalCount
  **/
  @ApiModelProperty(value = "")
  
    public String getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(String totalCount) {
    this.totalCount = totalCount;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XproductInfoListNofTransaction xproductInfoListNofTransaction = (XproductInfoListNofTransaction) o;
    return Objects.equals(this.pendingCount, xproductInfoListNofTransaction.pendingCount) &&
        Objects.equals(this.totalCount, xproductInfoListNofTransaction.totalCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pendingCount, totalCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XproductInfoListNofTransaction {\n");
    
    sb.append("    pendingCount: ").append(toIndentedString(pendingCount)).append("\n");
    sb.append("    totalCount: ").append(toIndentedString(totalCount)).append("\n");
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
