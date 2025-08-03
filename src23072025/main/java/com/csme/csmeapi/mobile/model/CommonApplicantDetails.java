package com.csme.csmeapi.mobile.model;

import java.util.HashMap;
import java.util.Map;

public class CommonApplicantDetails {
	
	private String beneficiaryName = null;

	private String address = null;

	private String zipCode = null;

	private String poBox = null;

	private String city = null;

	private String mobileNumber = null;

	private String swiftCode = null;

	private String country = null;
	
	private String textGuaranteeIssued = null;

	public String getTextGuaranteeIssued() {
		return textGuaranteeIssued;
	}

	public void setTextGuaranteeIssued(String textGuaranteeIssued) {
		this.textGuaranteeIssued = textGuaranteeIssued;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPoBox() {
		return poBox;
	}

	public void setPoBox(String poBox) {
		this.poBox = poBox;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "CommonApplicantDetails [beneficiaryName=" + beneficiaryName + ", address=" + address + ", zipCode="
				+ zipCode + ", poBox=" + poBox + ", city=" + city + ", mobileNumber=" + mobileNumber + ", swiftCode="
				+ swiftCode + ", country=" + country + ", textGuaranteeIssued=" + textGuaranteeIssued + "]";
	}
	
	public Map<String, Object> toMap() {
	    Map<String, Object> map = new HashMap<>();
	    map.put("beneficiaryName", this.beneficiaryName);
	    map.put("address", this.address);
	    map.put("zipCode", this.zipCode);
	    map.put("poBox", this.poBox);
	    map.put("city", this.city);
	    map.put("mobileNumber", this.mobileNumber);
	    map.put("swiftCode", this.swiftCode);
	    map.put("country", this.country);
	    map.put("textGuaranteeIssued", this.textGuaranteeIssued);
	    return map;
	}

	
	

}
