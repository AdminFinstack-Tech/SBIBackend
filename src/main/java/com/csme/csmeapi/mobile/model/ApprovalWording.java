package com.csme.csmeapi.mobile.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModelProperty;

/**
 * ApprovalWording
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-01-29T10:46:11.400+04:00[Asia/Muscat]")
public class ApprovalWording  implements OneOfXGuaranteeDetailsToBeIssued {
  /**
   * Gets or Sets toBeIssued
   */
  public enum ToBeIssuedEnum {
    APPROVALWORDING("ApprovalWording");

    private String value;

    ToBeIssuedEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ToBeIssuedEnum fromValue(String text) {
      for (ToBeIssuedEnum b : ToBeIssuedEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("ToBeIssued")
  private ToBeIssuedEnum toBeIssued = null;

  @JsonProperty("ApprovalDetails")
  private String approvalDetails = null;

  public ApprovalWording toBeIssued(ToBeIssuedEnum toBeIssued) {
    this.toBeIssued = toBeIssued;
    return this;
  }

  /**
   * Get toBeIssued
   * @return toBeIssued
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public ToBeIssuedEnum getToBeIssued() {
    return toBeIssued;
  }

  public void setToBeIssued(ToBeIssuedEnum toBeIssued) {
    this.toBeIssued = toBeIssued;
  }

  public ApprovalWording approvalDetails(String approvalDetails) {
    this.approvalDetails = approvalDetails;
    return this;
  }

  /**
   * Details for Approval wording
   * @return approvalDetails
  **/
  @ApiModelProperty(required = true, value = "Details for Approval wording")
      @NotNull

    public String getApprovalDetails() {
    return approvalDetails;
  }

  public void setApprovalDetails(String approvalDetails) {
    this.approvalDetails = approvalDetails;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApprovalWording approvalWording = (ApprovalWording) o;
    return Objects.equals(this.toBeIssued, approvalWording.toBeIssued) &&
        Objects.equals(this.approvalDetails, approvalWording.approvalDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(toBeIssued, approvalDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApprovalWording {\n");
    
    sb.append("    toBeIssued: ").append(toIndentedString(toBeIssued)).append("\n");
    sb.append("    approvalDetails: ").append(toIndentedString(approvalDetails)).append("\n");
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
