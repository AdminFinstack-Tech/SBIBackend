package com.csme.csmeapi.mobile.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
* OneOfXGuaranteeDetailsToBeIssued
*/
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "option")
@JsonSubTypes({@Type(value = UnApprovalWording.class, name = "UnApprovalWording"), @Type(value = ApprovalWording.class, name = "ApprovalWording")})
public interface OneOfXGuaranteeDetailsToBeIssued {

}
