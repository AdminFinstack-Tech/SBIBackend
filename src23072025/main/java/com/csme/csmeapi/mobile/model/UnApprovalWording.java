package com.csme.csmeapi.mobile.model;

import java.util.Objects;
import com.csme.csmeapi.mobile.model.AttachmentsObj;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * UnApprovalWording
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-01-29T10:46:11.400+04:00[Asia/Muscat]")
public class UnApprovalWording  implements OneOfXGuaranteeDetailsToBeIssued {
  /**
   * Gets or Sets toBeIssued
   */
  public enum ToBeIssuedEnum {
    UNAPPROVALWORDING("UnApprovalWording");

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

  @JsonProperty("UnApprovalDetails")
  @Valid
  private List<AttachmentsObj> unApprovalDetails = new ArrayList<>();

  public UnApprovalWording toBeIssued(ToBeIssuedEnum toBeIssued) {
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

  public UnApprovalWording unApprovalDetails(List<AttachmentsObj> unApprovalDetails) {
    this.unApprovalDetails = unApprovalDetails;
    return this;
  }

  public UnApprovalWording addUnApprovalDetailsItem(AttachmentsObj unApprovalDetailsItem) {
    this.unApprovalDetails.add(unApprovalDetailsItem);
    return this;
  }

  /**
   * Get unApprovalDetails
   * @return unApprovalDetails
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull
    @Valid
    public List<AttachmentsObj> getUnApprovalDetails() {
    return unApprovalDetails;
  }

  public void setUnApprovalDetails(List<AttachmentsObj> unApprovalDetails) {
    this.unApprovalDetails = unApprovalDetails;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UnApprovalWording unApprovalWording = (UnApprovalWording) o;
    return Objects.equals(this.toBeIssued, unApprovalWording.toBeIssued) &&
        Objects.equals(this.unApprovalDetails, unApprovalWording.unApprovalDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(toBeIssued, unApprovalDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UnApprovalWording {\n");
    
    sb.append("    toBeIssued: ").append(toIndentedString(toBeIssued)).append("\n");
    sb.append("    unApprovalDetails: ").append(toIndentedString(unApprovalDetails)).append("\n");
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
