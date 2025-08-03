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
 * ThirdPartyOption
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-01-29T10:46:11.400+04:00[Asia/Muscat]")
public class ThirdPartyOption  implements OneOfXApplicationDetailsTextGuaranteeIssued {
  @JsonProperty("ThirdPartyName")
  private String thirdPartyName = null;

  /**
   * Gets or Sets option
   */
  public enum OptionEnum {
    THIRDPARTY("ThirdParty");

    private String value;

    OptionEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static OptionEnum fromValue(String text) {
      for (OptionEnum b : OptionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("option")
  private OptionEnum option = null;

  @JsonProperty("attachments")
  @Valid
  private List<AttachmentsObj> attachments = new ArrayList<>();

  public ThirdPartyOption thirdPartyName(String thirdPartyName) {
    this.thirdPartyName = thirdPartyName;
    return this;
  }

  /**
   * Name of Third Party
   * @return thirdPartyName
  **/
  @ApiModelProperty(value = "Name of Third Party")
  
    public String getThirdPartyName() {
    return thirdPartyName;
  }

  public void setThirdPartyName(String thirdPartyName) {
    this.thirdPartyName = thirdPartyName;
  }

  public ThirdPartyOption option(OptionEnum option) {
    this.option = option;
    return this;
  }

  /**
   * Get option
   * @return option
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public OptionEnum getOption() {
    return option;
  }

  public void setOption(OptionEnum option) {
    this.option = option;
  }

  public ThirdPartyOption attachments(List<AttachmentsObj> attachments) {
    this.attachments = attachments;
    return this;
  }

  public ThirdPartyOption addAttachmentsItem(AttachmentsObj attachmentsItem) {
    this.attachments.add(attachmentsItem);
    return this;
  }

  /**
   * Get attachments
   * @return attachments
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull
    @Valid
    public List<AttachmentsObj> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<AttachmentsObj> attachments) {
    this.attachments = attachments;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ThirdPartyOption thirdPartyOption = (ThirdPartyOption) o;
    return Objects.equals(this.thirdPartyName, thirdPartyOption.thirdPartyName) &&
        Objects.equals(this.option, thirdPartyOption.option) &&
        Objects.equals(this.attachments, thirdPartyOption.attachments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(thirdPartyName, option, attachments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ThirdPartyOption {\n");
    
    sb.append("    thirdPartyName: ").append(toIndentedString(thirdPartyName)).append("\n");
    sb.append("    option: ").append(toIndentedString(option)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
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
