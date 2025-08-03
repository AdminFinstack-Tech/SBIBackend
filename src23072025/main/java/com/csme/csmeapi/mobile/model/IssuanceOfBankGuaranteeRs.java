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
 * IssuanceOfBankGuaranteeRs
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-01-29T10:46:11.400+04:00[Asia/Muscat]")
public class IssuanceOfBankGuaranteeRs   {
  @JsonProperty("EtradeReferenceNumber")
  private String etradeReferenceNumber = null;

  @JsonProperty("MessageId")
  private String messageId = null;

  @JsonProperty("NextStatus")
  private String nextStatus = null;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    SUCCESS("Success"),
    
    FAILURE("Failure");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("Status")
  private StatusEnum status = null;

  /**
   * Status code indicating the result of the operation
   */
  public enum StatusCodeEnum {
    _00("00"),
    
    _01("01"),
    
    _02("02"),
    
    _03("03"),
    
    _04("04"),
    
    _06("06"),
    
    _07("07"),
    
    _08("08"),
    
    _09("09"),
    
    _10("10"),
    
    _11("11"),
    
    _14("14"),
    
    _15("15"),
    
    _99("99");

    private String value;

    StatusCodeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusCodeEnum fromValue(String text) {
      for (StatusCodeEnum b : StatusCodeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("StatusCode")
  private StatusCodeEnum statusCode = null;

  @JsonProperty("StatusDescription")
  private String statusDescription = null;

  @JsonProperty("Timestamp")
  private String timestamp = null;

  @JsonProperty("TransactionStatus")
  private String transactionStatus = null;

  public IssuanceOfBankGuaranteeRs etradeReferenceNumber(String etradeReferenceNumber) {
    this.etradeReferenceNumber = etradeReferenceNumber;
    return this;
  }

  /**
   * E-Trade Reference Number
   * @return etradeReferenceNumber
  **/
  @ApiModelProperty(example = "48800OGTE88132", value = "E-Trade Reference Number")
  
    public String getEtradeReferenceNumber() {
    return etradeReferenceNumber;
  }

  public void setEtradeReferenceNumber(String etradeReferenceNumber) {
    this.etradeReferenceNumber = etradeReferenceNumber;
  }

  public IssuanceOfBankGuaranteeRs messageId(String messageId) {
    this.messageId = messageId;
    return this;
  }

  /**
   * Get messageId
   * @return messageId
  **/
  @ApiModelProperty(value = "")
  
    public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public IssuanceOfBankGuaranteeRs nextStatus(String nextStatus) {
    this.nextStatus = nextStatus;
    return this;
  }

  /**
   * Next Status
   * @return nextStatus
  **/
  @ApiModelProperty(value = "Next Status")
  
    public String getNextStatus() {
    return nextStatus;
  }

  public void setNextStatus(String nextStatus) {
    this.nextStatus = nextStatus;
  }

  public IssuanceOfBankGuaranteeRs status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(value = "")
  
    public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public IssuanceOfBankGuaranteeRs statusCode(StatusCodeEnum statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  /**
   * Status code indicating the result of the operation
   * @return statusCode
  **/
  @ApiModelProperty(example = "00", value = "Status code indicating the result of the operation")
  
    public StatusCodeEnum getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(StatusCodeEnum statusCode) {
    this.statusCode = statusCode;
  }

  public IssuanceOfBankGuaranteeRs statusDescription(String statusDescription) {
    this.statusDescription = statusDescription;
    return this;
  }

  /**
   * Detailed description of the status
   * @return statusDescription
  **/
  @ApiModelProperty(value = "Detailed description of the status")
  
    public String getStatusDescription() {
    return statusDescription;
  }

  public void setStatusDescription(String statusDescription) {
    this.statusDescription = statusDescription;
  }

  public IssuanceOfBankGuaranteeRs timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
  **/
  @ApiModelProperty(value = "")
  
    public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public IssuanceOfBankGuaranteeRs transactionStatus(String transactionStatus) {
    this.transactionStatus = transactionStatus;
    return this;
  }

  /**
   * Transaction Status
   * @return transactionStatus
  **/
  @ApiModelProperty(value = "Transaction Status")
  
    public String getTransactionStatus() {
    return transactionStatus;
  }

  public void setTransactionStatus(String transactionStatus) {
    this.transactionStatus = transactionStatus;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IssuanceOfBankGuaranteeRs issuanceOfBankGuaranteeRs = (IssuanceOfBankGuaranteeRs) o;
    return Objects.equals(this.etradeReferenceNumber, issuanceOfBankGuaranteeRs.etradeReferenceNumber) &&
        Objects.equals(this.messageId, issuanceOfBankGuaranteeRs.messageId) &&
        Objects.equals(this.nextStatus, issuanceOfBankGuaranteeRs.nextStatus) &&
        Objects.equals(this.status, issuanceOfBankGuaranteeRs.status) &&
        Objects.equals(this.statusCode, issuanceOfBankGuaranteeRs.statusCode) &&
        Objects.equals(this.statusDescription, issuanceOfBankGuaranteeRs.statusDescription) &&
        Objects.equals(this.timestamp, issuanceOfBankGuaranteeRs.timestamp) &&
        Objects.equals(this.transactionStatus, issuanceOfBankGuaranteeRs.transactionStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(etradeReferenceNumber, messageId, nextStatus, status, statusCode, statusDescription, timestamp, transactionStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IssuanceOfBankGuaranteeRs {\n");
    
    sb.append("    etradeReferenceNumber: ").append(toIndentedString(etradeReferenceNumber)).append("\n");
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    nextStatus: ").append(toIndentedString(nextStatus)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    statusDescription: ").append(toIndentedString(statusDescription)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    transactionStatus: ").append(toIndentedString(transactionStatus)).append("\n");
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
