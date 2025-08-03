package com.csme.csmeapi.mobile.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * UndersignedOption
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-01-29T10:46:11.400+04:00[Asia/Muscat]")
public class UndersignedOption  implements OneOfXApplicationDetailsTextGuaranteeIssued {
  /**
   * Gets or Sets option
   */
  public enum OptionEnum {
    UNDERSIGNED("Undersigned");

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

  public UndersignedOption option(OptionEnum option) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UndersignedOption undersignedOption = (UndersignedOption) o;
    return Objects.equals(this.option, undersignedOption.option);
  }

  @Override
  public int hashCode() {
    return Objects.hash(option);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UndersignedOption {\n");
    
    sb.append("    option: ").append(toIndentedString(option)).append("\n");
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
