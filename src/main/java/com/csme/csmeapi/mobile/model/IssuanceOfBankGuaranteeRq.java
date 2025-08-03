package com.csme.csmeapi.mobile.model;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * IssuanceOfBankGuaranteeRq
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-01-29T10:46:11.400+04:00[Asia/Muscat]")
public class IssuanceOfBankGuaranteeRq   {

	@JsonProperty("Status")
	private String status = null;

	@JsonProperty("EtradeReferenceNumber")
	private String etradeReferenceNumber = null;

	@JsonProperty("GuaranteeNumber")
	private String guaranteeNumber = null;

	@JsonProperty("Date")
	private String date = null;

	@JsonProperty("Branch")
	private String branch = null;

	@JsonProperty("BranchDetails")
	private XBranchDetails branchDetails = null;

	@JsonProperty("ApplicationDetails")
	private XApplicationDetails applicationDetails = null;

	@JsonProperty("GuaranteeDetails")
	private XGuaranteeDetails guaranteeDetails = null;

	@JsonProperty("CustomerDetails")
	private XCustomerDetails customerDetails = null;

	public IssuanceOfBankGuaranteeRq etradeReferenceNumber(String etradeReferenceNumber) {
		this.etradeReferenceNumber = etradeReferenceNumber;
		return this;
	}

	/**
	 * E Trade Reference number
	 * @return etradeReferenceNumber
	 **/
	@ApiModelProperty(value = "Status")
	public String getEtradeReferenceNumber() {
		return etradeReferenceNumber;
	}

	public void setEtradeReferenceNumber(String etradeReferenceNumber) {
		this.etradeReferenceNumber = etradeReferenceNumber;
	}

	/**
	 * Status Reference number
	 * @return etradeReferenceNumber
	 **/
	@ApiModelProperty(value = "E Trade Reference number")
	public String getStatus() {
		return status;
	}

	public IssuanceOfBankGuaranteeRq guaranteeNumber(String guaranteeNumber) {
		this.guaranteeNumber = guaranteeNumber;
		return this;
	}

	/**
	 * Guarantee Number
	 * @return guaranteeNumber
	 **/
	@ApiModelProperty(example = "24OGTE48800001", value = "Guarantee Number")

	public String getGuaranteeNumber() {
		return guaranteeNumber;
	}

	public void setGuaranteeNumber(String guaranteeNumber) {
		this.guaranteeNumber = guaranteeNumber;
	}

	public void setStatus(String Status) {
		this.status = Status;
	}

	public IssuanceOfBankGuaranteeRq date(String date) {
		this.date = date;
		return this;
	}

	/**
	 * Date
	 * @return date
	 **/
	@ApiModelProperty(required = true, value = "Date")
	@NotNull

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public IssuanceOfBankGuaranteeRq branch(String branch) {
		this.branch = branch;
		return this;
	}

	/**
	 * Branch
	 * @return branch
	 **/
	@ApiModelProperty(required = true, value = "Branch")
	@NotNull

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public IssuanceOfBankGuaranteeRq branchDetails(XBranchDetails branchDetails) {
		this.branchDetails = branchDetails;
		return this;
	}

	/**
	 * Get branchDetails
	 * @return branchDetails
	 **/
	@ApiModelProperty(required = true, value = "")
	@NotNull

	@Valid
	public XBranchDetails getBranchDetails() {
		return branchDetails;
	}

	public void setBranchDetails(XBranchDetails branchDetails) {
		this.branchDetails = branchDetails;
	}

	public IssuanceOfBankGuaranteeRq applicationDetails(XApplicationDetails applicationDetails) {
		this.applicationDetails = applicationDetails;
		return this;
	}

	/**
	 * Get applicationDetails
	 * @return applicationDetails
	 **/
	@ApiModelProperty(required = true, value = "")
	@NotNull

	@Valid
	public XApplicationDetails getApplicationDetails() {
		return applicationDetails;
	}

	public void setApplicationDetails(XApplicationDetails applicationDetails) {
		this.applicationDetails = applicationDetails;
	}

	public IssuanceOfBankGuaranteeRq guaranteeDetails(XGuaranteeDetails guaranteeDetails) {
		this.guaranteeDetails = guaranteeDetails;
		return this;
	}

	/**
	 * Get guaranteeDetails
	 * @return guaranteeDetails
	 **/
	@ApiModelProperty(required = true, value = "")
	@NotNull

	@Valid
	public XGuaranteeDetails getGuaranteeDetails() {
		return guaranteeDetails;
	}

	public void setGuaranteeDetails(XGuaranteeDetails guaranteeDetails) {
		this.guaranteeDetails = guaranteeDetails;
	}

	public IssuanceOfBankGuaranteeRq customerDetails(XCustomerDetails customerDetails) {
		this.customerDetails = customerDetails;
		return this;
	}

	/**
	 * Get customerDetails
	 * @return customerDetails
	 **/
	@ApiModelProperty(required = true, value = "")
	@NotNull

	@Valid
	public XCustomerDetails getCustomerDetails() {
		return customerDetails;
	}

	public void setCustomerDetails(XCustomerDetails customerDetails) {
		this.customerDetails = customerDetails;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		IssuanceOfBankGuaranteeRq issuanceOfBankGuaranteeRq = (IssuanceOfBankGuaranteeRq) o;
		return Objects.equals(this.etradeReferenceNumber, issuanceOfBankGuaranteeRq.etradeReferenceNumber) &&
				Objects.equals(this.date, issuanceOfBankGuaranteeRq.date) &&
				Objects.equals(this.branch, issuanceOfBankGuaranteeRq.branch) &&
				Objects.equals(this.branchDetails, issuanceOfBankGuaranteeRq.branchDetails) &&
				Objects.equals(this.applicationDetails, issuanceOfBankGuaranteeRq.applicationDetails) &&
				Objects.equals(this.guaranteeDetails, issuanceOfBankGuaranteeRq.guaranteeDetails) &&
				Objects.equals(this.customerDetails, issuanceOfBankGuaranteeRq.customerDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(etradeReferenceNumber, date, branch, branchDetails, applicationDetails, guaranteeDetails, customerDetails);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class IssuanceOfBankGuaranteeRq {\n");

		sb.append("    etradeReferenceNumber: ").append(toIndentedString(etradeReferenceNumber)).append("\n");
		sb.append("    date: ").append(toIndentedString(date)).append("\n");
		sb.append("    branch: ").append(toIndentedString(branch)).append("\n");
		sb.append("    branchDetails: ").append(toIndentedString(branchDetails)).append("\n");
		sb.append("    applicationDetails: ").append(toIndentedString(applicationDetails)).append("\n");
		sb.append("    guaranteeDetails: ").append(toIndentedString(guaranteeDetails)).append("\n");
		sb.append("    customerDetails: ").append(toIndentedString(customerDetails)).append("\n");
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
