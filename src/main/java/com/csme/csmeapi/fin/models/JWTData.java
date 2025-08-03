package com.csme.csmeapi.fin.models;

import java.util.Date;

public class JWTData {
  private String id;
  
  private String userId;
  
  private String businessUnit;
  
  private String subject;
  
  private String issuer;
  
  private Date expireDate;
  
  public String getId() {
    return this.id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getUserId() {
    return this.userId;
  }
  
  public void setUserId(String userId) {
    this.userId = userId;
  }
  
  public String getBusinessUnit() {
    return this.businessUnit;
  }
  
  public void setBusinessUnit(String businessGroup) {
    this.businessUnit = businessGroup;
  }
  
  public String getSubject() {
    return this.subject;
  }
  
  public void setSubject(String subject) {
    this.subject = subject;
  }
  
  public String getIssuer() {
    return this.issuer;
  }
  
  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }
  
  public Date getExpireDate() {
    return this.expireDate;
  }
  
  public void setExpireDate(Date expireData) {
    this.expireDate = expireData;
  }
  
  public String toString() {
    return "JWTData [id=" + this.id + ", userId=" + this.userId + ", businessGroup=" + this.businessUnit + ", subject=" + this.subject + ", issuer=" + this.issuer + ", expireDate=" + this.expireDate + "]";
  }
}
