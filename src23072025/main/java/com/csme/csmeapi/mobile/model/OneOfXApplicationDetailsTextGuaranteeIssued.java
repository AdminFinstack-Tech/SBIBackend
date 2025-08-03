package com.csme.csmeapi.mobile.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
* OneOfXApplicationDetailsTextGuaranteeIssued
*/

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "option")
@JsonSubTypes({@Type(value = ThirdPartyOption.class, name = "ThirdPartyOption"), @Type(value = UndersignedOption.class, name = "UndersignedOption")})
public interface OneOfXApplicationDetailsTextGuaranteeIssued {

}
