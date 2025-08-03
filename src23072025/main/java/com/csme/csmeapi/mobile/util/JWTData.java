package com.csme.csmeapi.mobile.util;

import java.util.Date;

public class JWTData {

	private String id;
	private String userId;
	private String businessUnit;
	private String subject;
	private String issuer;
	private Date expireDate;

	public JWTData() {

	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessGroup) {
		this.businessUnit = businessGroup;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireData) {
		this.expireDate = expireData;
	}

	@Override
	public String toString() {
		return "JWTData [id=" + id + ", userId=" + userId + ", businessGroup=" + businessUnit + ", subject=" + subject
				+ ", issuer=" + issuer + ", expireDate=" + expireDate + "]";
	}
}
