package com.csme.csmeapi.api.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.cs.base.log.CSLogger;
import com.cs.base.utility.ASPathConst;
import com.cs.core.result.FinalStatus;
import com.cs.core.result.TrxResult;
import com.cs.eximap.log.ASCELog;
import com.csme.csmeapi.api.RequestLGApi;
import com.csme.csmeapi.mobile.model.IssuanceOfBankGuaranteeRq;
import com.csme.csmeapi.mobile.model.IssuanceOfBankGuaranteeRs;
import com.csme.csmeapi.mobile.model.IssuanceOfBankGuaranteeRs.StatusCodeEnum;
import com.csme.csmeapi.mobile.model.IssuanceOfBankGuaranteeRs.StatusEnum;
import com.csme.csmeapi.mobile.service.CreateTransactionService;
import com.csme.csmeapi.mobile.service.MobileAppCommonDao;
import com.csme.csmeapi.mobile.util.CSMEJWTUtil;
import com.csme.csmeapi.mobile.util.JWTData;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-11-18T10:50:11.647+04:00[Asia/Muscat]")
@Controller
public class RequestLGApiController implements RequestLGApi {


	private CSLogger logger = ASCELog.getCELogger("csme-rest");

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@org.springframework.beans.factory.annotation.Autowired
	public RequestLGApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@Override
	public Optional<ObjectMapper> getObjectMapper() {
		return Optional.ofNullable(objectMapper);
	}

	@Override
	public Optional<HttpServletRequest> getRequest() {
		return Optional.ofNullable(request);
	}

	public ResponseEntity<IssuanceOfBankGuaranteeRs> requestBG(@ApiParam(value = "Request Id" ,required=true) @RequestHeader(value="requestId", required=true) UUID requestId
			,@ApiParam(value = "Channel Name" ,required=true, defaultValue="MobileBanking") @RequestHeader(value="channel", required=true) String channel
			,@ApiParam(value = "Time date Unix Format" ,required=true) @RequestHeader(value="timeStamp", required=true) Integer timeStamp
			,@ApiParam(value = "Company Id" ,required=true) @RequestHeader(value="CIC", required=true) String CIC
			,@ApiParam(value = "User Id" ,required=true) @RequestHeader(value="UserId", required=true) String userId
			,@NotNull @ApiParam(value = "Module Code", required = true, allowableValues = "OWGT") @Valid @RequestParam(value = "Module", required = true) String module
			,@ApiParam(value = ""  )  @Valid @RequestBody IssuanceOfBankGuaranteeRq body
			,@ApiParam(value = "Authorization Token" ) @RequestHeader(value="authToken", required=false) String authToken
			,@ApiParam(value = "Version of API" , defaultValue="1.0.0") @RequestHeader(value="version", required=false) String version
			,@ApiParam(value = "FuncType") @Valid @RequestParam(value = "FuncType", required = false) String funcType
			) {
		MobileAppCommonDao commonDao = null;
		Long sequence = null;
		IssuanceOfBankGuaranteeRs resp = new IssuanceOfBankGuaranteeRs();
		String accept = request.getHeader("Accept");
		String method = request.getMethod();
		if (accept != null && accept.contains("application/json")) {
			try {
				CreateTransactionService createTransactionService = new CreateTransactionService();
				try {
					commonDao = new MobileAppCommonDao();
					if (commonDao.requestAlreadyExists(requestId.toString())) {
						logger.info("Request Id is aleady exist");
						commonDao.updateAuditRecord(requestId.toString(), "Exception has occured", "Exception has occured", 
								HttpStatus.BAD_REQUEST);
						return new ResponseEntity<IssuanceOfBankGuaranteeRs>(HttpStatus.BAD_REQUEST);
					}

					sequence = commonDao.saveAuditRecord("CIC : "+CIC+ " UserId : "+userId+ " Body : "+body.toString(), requestId.toString(), "RequestLGApi");
					logger.info("Saving record into Audit table and sequence no is :"+sequence);
					
					String reference = commonDao.getReference(CIC, "P08071500002"); 
					reference = reference.replace("488OGT", "DIGOGT");
					logger.info("Reference number is :"+reference);
					body.setEtradeReferenceNumber(reference);
					Map<String, Object> mapBody =  convertToMap(body);
					mapBody.put("cic", CIC);
					mapBody.put("module", module);
					mapBody.put("userid", userId);
					
					TrxResult res = (TrxResult) createTransactionService.getFunction(mapBody,method); 
					FinalStatus finalStatus = res.getFinalStatus();
					if(res.isSuccess()) {
						resp.setEtradeReferenceNumber(body.getEtradeReferenceNumber());
						resp.setMessageId(sequence.toString());
						resp.setStatusCode(StatusCodeEnum._00);
						resp.setStatus(StatusEnum.SUCCESS);
						resp.setStatusDescription("Success");
						String setTimeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new java.util.Date());
						resp.setTimestamp(setTimeStamp);
						String nextStatus = finalStatus.getInboxStatus();
						if(nextStatus!=null && ("A".equalsIgnoreCase(nextStatus) || "M".equalsIgnoreCase(nextStatus))) {
							resp.setNextStatus("Fully Authorized");
						} else if(nextStatus!=null && ("A".equalsIgnoreCase(nextStatus) || "M".equalsIgnoreCase(nextStatus))){
							resp.setNextStatus("Partially Authorised");
						}
						resp.setTransactionStatus(getTrxStatus(finalStatus.getMasterStatus()));
						commonDao.updateAuditRecord(sequence, "Success", "", HttpStatus.OK);
						return new ResponseEntity<IssuanceOfBankGuaranteeRs>(resp,HttpStatus.OK);
					} else {
						resp.setEtradeReferenceNumber(body.getEtradeReferenceNumber());
						resp.setMessageId(sequence.toString());
						resp.setStatusCode(StatusCodeEnum._01);
						resp.setStatus(StatusEnum.FAILURE);
						resp.setStatusDescription("Failure");
						String setTimeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new java.util.Date());
						resp.setTimestamp(setTimeStamp);
						commonDao.updateAuditRecord(sequence, "Exception has occured", "Exception has occured",HttpStatus.OK);
						return new ResponseEntity<IssuanceOfBankGuaranteeRs>(resp,HttpStatus.OK);
					}
				} catch (Exception e) {
					resp.setMessageId(sequence.toString());
					resp.setStatusCode(StatusCodeEnum._99);
					resp.setStatus(StatusEnum.FAILURE);
					resp.setStatusDescription("Due to Techinal Error, "+CSMEJWTUtil.getStackTraceAsString(e));
					String setTimeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new java.util.Date());
					resp.setTimestamp(setTimeStamp);
					commonDao.updateAuditRecord(sequence, "Exception has occured", "Exception has occured",HttpStatus.OK);
					return new ResponseEntity<IssuanceOfBankGuaranteeRs>(resp,HttpStatus.NOT_ACCEPTABLE);
				}
			} catch (Exception e) {
				resp.setMessageId(sequence.toString());
				resp.setStatusCode(StatusCodeEnum._99);
				resp.setStatus(StatusEnum.FAILURE);
				resp.setStatusCode(null);
				resp.setStatusDescription("Failure"+CSMEJWTUtil.getStackTraceAsString(e));
				String setTimeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new java.util.Date());
				resp.setTimestamp(setTimeStamp);
				commonDao.updateAuditRecord(sequence, "Exception has occured", "Exception has occured",HttpStatus.OK);
				return new ResponseEntity<IssuanceOfBankGuaranteeRs>(resp,HttpStatus.NOT_IMPLEMENTED);
			}
		}
		return new ResponseEntity<IssuanceOfBankGuaranteeRs>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public String getTrxStatus(String trxStatus){
		String finalStatus =  null;
		if("R".equalsIgnoreCase(trxStatus)) {
			finalStatus = "Rejected by Supervisor";
		} else if("M".equalsIgnoreCase(trxStatus)) {
			finalStatus = "Authorised";
		} else if("A".equalsIgnoreCase(trxStatus)) {
			finalStatus = "Authorised";
		} else if("T".equalsIgnoreCase(trxStatus)) {
			finalStatus = "Authorised";
		} else if("Z".equalsIgnoreCase(trxStatus)) {
			finalStatus = "Saved";
		} else if("S".equalsIgnoreCase(trxStatus)) {
			finalStatus = "Released";
		} else if("P".equalsIgnoreCase(trxStatus)) {
			finalStatus = "Awaiting Corporate Approval";
		}
		return finalStatus;
	}

	public ResponseEntity<IssuanceOfBankGuaranteeRs> securityCheck(UUID requestId, String authToken) throws Exception {
		Properties restProperties = fetchProperties();
		if (restProperties.get("securityRequired").toString().equalsIgnoreCase("yes")) {
			if (isEmpty(authToken)) {
				return new ResponseEntity<IssuanceOfBankGuaranteeRs>(HttpStatus.UNAUTHORIZED);
			} else {
				String validTokenResponse = isValidToken(authToken);
				if (!validTokenResponse.equalsIgnoreCase("SUCCESS")) {
					return new ResponseEntity<IssuanceOfBankGuaranteeRs>(HttpStatus.UNAUTHORIZED);
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	public static String isValidToken(String jwtToken) throws Exception {
		try {
			CSMEJWTUtil csmeJwtMapper = new CSMEJWTUtil();
			JWTData jwtData = csmeJwtMapper.parseJWT(jwtToken);
			return "Success";
		} catch (Exception e) {
			return e.getMessage();
		}

	}

	public static Properties fetchProperties() throws IOException {
		Properties properties = new Properties();
		try {
			File file = new File(ASPathConst.USER_DIR_PATH + File.separator + "INT" + File.separator + "securityApi.properties");
			InputStream in = new FileInputStream(file);
			properties.load(in);
		} catch (IOException e) {
			throw e;
		}
		return properties;
	}

	public static boolean isEmpty(String val) {
		if (val == null) {
			return true;
		}else {
			val=val.trim();
			if(val.length() < 1) {
				return true;
			}
		}
		return false;
	}

	public static Map<String, Object> convertToMap(Object obj) {
		return convertToMap(obj, new HashSet<>());
	}

	private static Map<String, Object> convertToMap(Object obj, Set<Object> visited) {
		if (obj == null) {
			return null;
		}

		// Avoid circular references
		if (visited.contains(obj)) {
			return Collections.singletonMap("circularReference", "Detected");
		}

		visited.add(obj);

		Map<String, Object> map = new HashMap<>();
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Object value = field.get(obj);

				if (value != null) {
					if (isPrimitiveOrWrapper(value.getClass()) || value instanceof String) {
						map.put(field.getName(), value);
					} else if (value instanceof List) {
						List<?> list = (List<?>) value;
						map.put(field.getName(), list.stream()
								.map(item -> convertToMap(item, visited))
								.collect(Collectors.toList()));
					} else if (value instanceof Map) {
						map.put(field.getName(), value);
					} else {
						map.put(field.getName(), convertToMap(value, visited));
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while converting object to map", e);
		}
		visited.remove(obj); // Remove the object from visited set once processed
		return map;
	}

	private static boolean isPrimitiveOrWrapper(Class<?> type) {
		return type.isPrimitive() ||
				type.equals(String.class) ||
				type.equals(Integer.class) ||
				type.equals(Double.class) ||
				type.equals(Float.class) ||
				type.equals(Long.class) ||
				type.equals(Short.class) ||
				type.equals(Byte.class) ||
				type.equals(Boolean.class) ||
				type.equals(Character.class);
	}

	public static void main(String[] args) throws Exception {
		String jsonString = "{\n"
				+ "  \"EtradeReferenceNumber\": \"string\",\n"
				+ "  \"Date\": \"string\",\n"
				+ "  \"Branch\": \"string\",\n"
				+ "  \"BranchDetails\": {\n"
				+ "    \"City\": \"string\",\n"
				+ "    \"BranchName\": \"string\",\n"
				+ "    \"ContactPerson\": \"string\",\n"
				+ "    \"BranchAddress\": \"string\",\n"
				+ "    \"BranchLocationLink\": \"string\"\n"
				+ "  },\n"
				+ "  \"ApplicationDetails\": {\n"
				+ "    \"OutwardGuarantee\": \"Local\",\n"
				+ "    \"LocalDetails\": {\n"
				+ "      \"TextGuaranteeIssued\": {\n"
				+ "        \"ThirdPartyName\": \"string\",\n"
				+ "        \"option\": \"ThirdParty\",\n"
				+ "        \"attachments\": [\n"
				+ "          {\n"
				+ "            \"AttachmentContent\": \"Base64\",\n"
				+ "            \"AttachmentName\": \"string\",\n"
				+ "            \"AttachmentSize\": \"string\",\n"
				+ "            \"AttachmentType\": \"string\"\n"
				+ "          }\n"
				+ "        ]\n"
				+ "      },\n"
				+ "      \"BeneficiaryName\": \"string\",\n"
				+ "      \"Address\": \"string\",\n"
				+ "      \"ZipCode\": \"string\",\n"
				+ "      \"POBox\": \"string\",\n"
				+ "      \"City\": \"string\",\n"
				+ "      \"MobileNumber\": \"string\"\n"
				+ "    },\n"
				+ "    \"ForeignDetails\": {\n"
				+ "      \"TextGuaranteeIssued\": {\n"
				+ "        \"ThirdPartyName\": \"string\",\n"
				+ "        \"option\": \"ThirdParty\",\n"
				+ "        \"attachments\": [\n"
				+ "          {\n"
				+ "            \"AttachmentContent\": \"Base64\",\n"
				+ "            \"AttachmentName\": \"string\",\n"
				+ "            \"AttachmentSize\": \"string\",\n"
				+ "            \"AttachmentType\": \"string\"\n"
				+ "          }\n"
				+ "        ]\n"
				+ "      },\n"
				+ "      \"BeneficiaryName\": \"string\",\n"
				+ "      \"Address\": \"string\",\n"
				+ "      \"ZipCode\": \"string\",\n"
				+ "      \"POBox\": \"string\",\n"
				+ "      \"City\": \"string\",\n"
				+ "      \"MobileNumber\": \"string\",\n"
				+ "      \"SwiftCode\": \"string\",\n"
				+ "      \"Country\": \"string\"\n"
				+ "    }\n"
				+ "  },\n"
				+ "  \"GuranteeDetails\": {\n"
				+ "    \"Typeofguarantee\": \"string\",\n"
				+ "    \"OtherType\": \"string\",\n"
				+ "    \"PurposeofGuarantee\": \"string\",\n"
				+ "    \"ToBeIssued\": {\n"
				+ "       \"type\": \"UnApprovalWording\",\n"
				+ "      \"ToBeIssued\": \"ApprovalWording\",\n"
				+ "      \"ApprovalDetails\": \"string\"\n"
				+ "    },\n"
				+ "    \"CashMargin\": \"FullCover\",\n"
				+ "    \"LimitPercentage\": 0,\n"
				+ "    \"Amount\": \"10000\",\n"
				+ "    \"Currency\": \"SAR\",\n"
				+ "    \"AmountInWords\": \"string\",\n"
				+ "    \"ContractValue\": \"string\",\n"
				+ "    \"ExpiryDate\": \"2024-11-24\"\n"
				+ "  },\n"
				+ "  \"CustomerDetails\": {\n"
				+ "    \"CustomerName\": \"string\",\n"
				+ "    \"AccountNumber\": \"string\",\n"
				+ "    \"ContactName\": \"string\",\n"
				+ "    \"ContactNumber\": \"string\"\n"
				+ "  }\n"
				+ "}";

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule()); // Register the JavaTimeModule
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Optional

			IssuanceOfBankGuaranteeRq issuanceRq = objectMapper.readValue(jsonString, IssuanceOfBankGuaranteeRq.class);

			System.out.println("Deserialization successful!");
			System.out.println("Customer Name: " + issuanceRq.getCustomerDetails().getCustomerName());
			Map<String, Object> mapBody =  convertToMap(issuanceRq);
			System.out.println("mapBody is : "+mapBody);
			String templateString = "{\n"
					+ "  \"CNTC_DETL\": \"{guranteeDetails.currency}\",\n"
					+ "  \"GTEE_AMT\": \"{guranteeDetails.amount}\",\n"
					+ "  \"INT_OLDLMTREF\": \"\",\n"
					+ "  \"ITEM_NAME\": \"\",\n"
					+ "  \"EXPIRY_DT\": \"2024-11-20\",\n"
					+ "  \"INT_LMTEARREF\": \"\",\n"
					+ "  \"ISSBK_ID_C\": \"\",\n"
					+ "  \"DELIV_OF_ORIG_CODE_C\": \"\",\n"
					+ "  \"FUNC_ID\": \"\",\n"
					+ "  \"GTEE_FRMT\": \"STANDARD FORMAT\",\n"
					+ "  \"INT_LMTREF\": \"\",\n"
					+ "  \"ADV_THRU_BK_ADD3\": \"\",\n"
					+ "  \"ISSBK_ADD\": \"\",\n"
					+ "  \"CUST_INSTR\": \"\",\n"
					+ "  \"ADV_THRU_BK_ADD2\": \"\",\n"
					+ "  \"AVBL_ID_ADD2_C\": \"\",\n"
					+ "  \"ADV_THRU_SWIFT_ADD\": \"\",\n"
					+ "  \"AVBL_BKID_NAME\": \"\",\n"
					+ "  \"TRX_DT_HIJRI\": \"1446-5-17\",\n"
					+ "  \"TEMP_BENE_ADD\": \"TSR\",\n"
					+ "  \"ISSBK_SWIFT_ADD\": \"\",\n"
					+ "  \"menucheck\": \"on\",\n"
					+ "  \"AVBL_BKID_FV_BT_C\": \"FAVOURITES\",\n"
					+ "  \"BENE_BK_SW_ADD\": \"\",\n"
					+ "  \"CATA_TYPE\": \"\",\n"
					+ "  \"AVBL_SW_ADD_C\": \"\",\n"
					+ "  \"TRAN_TYPE\": \"\",\n"
					+ "  \"T_SVR_GMT\": \"15:11:01\",\n"
					+ "  \"SEND_TO_ADD1\": \"\",\n"
					+ "  \"ADD_AMT_INFO_C\": \"\",\n"
					+ "  \"SEND_TO_ADD3\": \"\",\n"
					+ "  \"PARTY_NM\": \"\",\n"
					+ "  \"SEND_TO_ADD2\": \"\",\n"
					+ "  \"INT_LMTCOLLTMP\": \"\",\n"
					+ "  \"CSRFTOKEN\": [\n"
					+ "    \"ei8AsdGK82shr8J3Q809q3W395aL2F5C2GD80V1938g4AD41mEZF4i2C6q200060\",\n"
					+ "    \"ei8AsdGK82shr8J3Q809q3W395aL2F5C2GD80V1938g4AD41mEZF4i2C6q200060\"\n"
					+ "  ],\n"
					+ "  \"AVBL_BKID_FV_BT\": \"FAVOURITES\",\n"
					+ "  \"BENE_BK_ID\": \"\",\n"
					+ "  \"CONTCT_DETL\": \"YTTY\",\n"
					+ "  \"UNIT_CODE\": \"\",\n"
					+ "  \"AUTO_EXTEND_NOTIF_PERIOD_LOC\": \"\",\n"
					+ "  \"APPLICABLE_RULES_C\": \"\",\n"
					+ "  \"AVBL_ID_ADD3_C\": \"\",\n"
					+ "  \"INT_LMTEXPIRY\": \"\",\n"
					+ "  \"FILE_23X_NARR\": \"\",\n"
					+ "  \"AVBL_ID_C\": \"\",\n"
					+ "  \"GTEE_TYPE_JO\": \"Bid Bonds (Tender Guarantees)\",\n"
					+ "  \"CONF_BKID_NAME\": \"\",\n"
					+ "  \"AVBL_BKID_SWIFT_ADD\": \"\",\n"
					+ "  \"INT_RETURNCODE\": \"\",\n"
					+ "  \"RM_DECISION\": \"No\",\n"
					+ "  \"APPLICABLE_RULES\": \"NONE\",\n"
					+ "  \"SESSION_ID\": \"xszxjPP04W1Jteed6fTy0jO\",\n"
					+ "  \"EXCELTYPE\": \"\",\n"
					+ "  \"AUTO_EXTEN_PERIOD_DAYS_C\": \"\",\n"
					+ "  \"CURR_OPER\": \"\",\n"
					+ "  \"ISSBK_SWIFT_ADD_C\": \"\",\n"
					+ "  \"OBLIGOR_INS_ADD_C\": \"\",\n"
					+ "  \"TRANS_CONDITION\": \"\",\n"
					+ "  \"CHG_AC\": \"1212\",\n"
					+ "  \"GOVERN_LAW_CNTY_CODE\": \"SA\",\n"
					+ "  \"FORM_OF_UNDERTAKING_C\": \"\",\n"
					+ "  \"ADV_ID\": \"\",\n"
					+ "  \"TEMP_SEND_NAME\": \"\",\n"
					+ "  \"CHG_AC_CCY\": \"SAR\",\n"
					+ "  \"TRAN_NAME\": \"\",\n"
					+ "  \"GTEE_AMT_C\": \"0\",\n"
					+ "  \"UNDERTAKING_TYPE_C\": \"\",\n"
					+ "  \"CUST_TYPE\": \"\",\n"
					+ "  \"EXPIRY_COND_C\": \"\",\n"
					+ "  \"DOC_PRES_INSTR\": \"\",\n"
					+ "  \"TRANS_CONDITION_C\": \"\",\n"
					+ "  \"GOVERN_LAW_CNTY_CODE_C\": \"SA\",\n"
					+ "  \"EXPIRY_TYPE\": \"FIXD\",\n"
					+ "  \"CONF_BK_SWIFT_ADD\": \"\",\n"
					+ "  \"ENG_ADD\": [\"TSR\"],\n"
					+ "  \"PARTY_ID\": \"\",\n"
					+ "  \"_EVENT_TITLE\": \"\",\n"
					+ "  \"AUTO_EXTEN_PERIOD_DAYS\": \"\",\n"
					+ "  \"PARENT_MAIN_REF\": \"488000005172011\",\n"
					+ "  \"GOVERN_LAW\": \"\",\n"
					+ "  \"TITLE_NAME\": \"\",\n"
					+ "  \"AUTO_EXTEN_NOTIF_C\": \"\",\n"
					+ "  \"DELIV_OF_ORIG_CODE\": \"\",\n"
					+ "  \"CONF_BKID_ID\": \"\",\n"
					+ "  \"DoType\": \"\",\n"
					+ "  \"AjaxPost\": \"T\",\n"
					+ "  \"_ACT_TYPE\": \"\",\n"
					+ "  \"OBLIGOR_INS_NAME\": \"\",\n"
					+ "  \"OBLIGOR_INS_NAME_C\": \"\",\n"
					+ "  \"GRP_NAME\": \"\",\n"
					+ "  \"FORM_OF_UNDERTAKING\": \"DGAR\",\n"
					+ "  \"INI_ITEM_ID\": \"\",\n"
					+ "  \"SAVED\": \"\",\n"
					+ "  \"EXPIRY_COND\": \"\",\n"
					+ "  \"EXPIRY_TYPE_C\": \"\",\n"
					+ "  \"TMP_LMTEARREF\": \"\",\n"
					+ "  \"DELIVERY_TO_C\": \"\",\n"
					+ "  \"AVBL_ID\": \"\",\n"
					+ "  \"AVBL_BKID_ADD3\": \"\",\n"
					+ "  \"INT_LMTEDATE\": \"\",\n"
					+ "  \"AVBL_BKID_ADD2\": \"\",\n"
					+ "  \"AUTO_EXTEN_PERIOD_C\": \"\",\n"
					+ "  \"CONF_BKID_ADD\": \"\",\n"
					+ "  \"APPL_ID\": \"1517524\",\n"
					+ "  \"AUTO_EXTEN_PERIOD\": \"\",\n"
					+ "  \"PURP_OF_MESS\": \"ACNF\",\n"
					+ "  \"BENE_BT\": \"\",\n"
					+ "  \"INT_RETURNMESSAGE\": \"\",\n"
					+ "  \"CE_SELECT_CATAFIELD\": \"\",\n"
					+ "  \"INT_LMTCOLL\": \"\",\n"
					+ "  \"TEMP_ADV_THR_BKADD\": \"\",\n"
					+ "  \"STAN_WORD_REQD_LANG_C\": \"\",\n"
					+ "  \"AVBL_BKID_ADD\": \"\",\n"
					+ "  \"ADD_AMT_INFO\": \"\",\n"
					+ "  \"CURRNT_STATUS\": [\"Awaiting Verification\", \"Awaiting Verification\"],\n"
					+ "  \"ISSUE_DATE_C\": \"\",\n"
					+ "  \"ADV_THRU_BK_FV_BT\": \"FAVOURITES\",\n"
					+ "  \"FUN_DESC\": \"Apply for Guarantee تقديم طلب ضمان\",\n"
					+ "  \"CATA_NUM\": \"\",\n"
					+ "  \"ExcelType\": \"\",\n"
					+ "  \"C_LAN_VAL\": null,\n"
					+ "  \"SEND_TO_NAME\": \"\",\n"
					+ "  \"FUN_NAME\": \"\",\n"
					+ "  \"ID\": \"\",\n"
					+ "  \"BENE_TEMP_ID\": \"\",\n"
					+ "  \"RFU_REF_NO\": [\"488000005172011\"],\n"
					+ "  \"INT_LMTCASHMRGN\": \"\",\n"
					+ "  \"CONF_INSR\": \"\",\n"
					+ "  \"CHG_DESC_C\": \"\",\n"
					+ "  \"CHG_DESC_B\": \"\",\n"
					+ "  \"INT_LMTSDATE\": \"\",\n"
					+ "  \"CURR_DATE\": \"\",\n"
					+ "  \"ADV_THRU_BK_ADD\": \"\",\n"
					+ "  \"ITEM_ID\": \"\",\n"
					+ "  \"CUST_NO\": \"488000005172011\",\n"
					+ "  \"TRANS_COMM\": \"\",\n"
					+ "  \"BENE_NM_C\": \"\",\n"
					+ "  \"BENE_BK_FV_BT\": \"FAVOURITES\",\n"
					+ "  \"DELETE_EARMARK\": \"No\",\n"
					+ "  \"GOVERN_LAW_C\": \"\",\n"
					+ "  \"SEL_TYPE\": \"\",\n"
					+ "  \"CATA_CURR\": \"\",\n"
					+ "  \"UNDERTAKING_TYPE_NARR_C\": \"\",\n"
					+ "  \"AUTO_EXTEND_NOTIF_PERIOD_LOC_C\": \"\",\n"
					+ "  \"FL_FLAG\": \"Local\",\n"
					+ "  \"ISSBK_ID\": \"\",\n"
					+ "  \"UNDEL_TRANS_DET\": \"\",\n"
					+ "  \"TRX_DT\": \"2024-11-19\",\n"
					+ "  \"INT_LMTPCODE\": \"\",\n"
					+ "  \"UNDER_GTEE_AMT\": \"\",\n"
					+ "  \"DEMAND_INDICATOR\": \"\",\n"
					+ "  \"BENE_NM\": \"212\",\n"
					+ "  \"ISSUE_BK_FV_BT\": \"FAVOURITES\",\n"
					+ "  \"DELIV_OF_ORIG_UNDER_C\": \"\",\n"
					+ "  \"ISSBK_BK_ADD2\": \"\",\n"
					+ "  \"ISSBK_BK_ADD3\": \"\",\n"
					+ "  \"DELIVERY_TO\": \"\",\n"
					+ "  \"GTEE_CCY_B\": \"\",\n"
					+ "  \"AUTO_EXTEN_EXPIRY_DT_C\": \"\",\n"
					+ "  \"GTEE_CCY_C\": \"\",\n"
					+ "  \"ISSBK_ADD_C\": \"\",\n"
					+ "  \"ISSBK_NAME_C\": \"\",\n"
					+ "  \"BENE_BK_ADD2\": \"\",\n"
					+ "  \"BENE_BK_ADD3\": \"\",\n"
					+ "  \"BENE_BK_ADD1\": \"\",\n"
					+ "  \"TRANS_INDICATOR\": \"\",\n"
					+ "  \"ISSUE_BK_FV_BT_C\": \"FAVOURITES\",\n"
					+ "  \"D_SVR_GMT\": \"2024-11-19\",\n"
					+ "  \"EXPIRY_DT_C\": \"\",\n"
					+ "  \"APPL_NM\": \"SULAIMAN ABDUL AZIZ ALRAJHI EST.\",\n"
					+ "  \"BENE_REF_ID\": \"\",\n"
					+ "  \"C_IP_ADDRESS\": \"10.3.1.90\",\n"
					+ "  \"NXT_STATUS\": [\"Bank Decision on Guarantee\", \"Bank Decision on Guarantee\"],\n"
					+ "  \"DOC_PRES_INSTR_C\": \"\",\n"
					+ "  \"INT_LMTDET\": \"\",\n"
					+ "  \"INT_LMTCCY\": \"\",\n"
					+ "  \"ISSBK_BK_ADD2_C\": \"\",\n"
					+ "  \"C_UNIT_NAME\": \"\",\n"
					+ "  \"AUTO_EXTEN_EXPIRY_DT\": \"\",\n"
					+ "  \"DEMAND_INDICATOR_C\": \"\",\n"
					+ "  \"OBLIGOR_INS_ADD\": \"\",\n"
					+ "  \"AVBL_ID_ADD_C\": \"\",\n"
					+ "  \"AUTO_EXTEN_NOTIF\": \"\",\n"
					+ "  \"REFRESH_TOKEN\": \"1732014656614\",\n"
					+ "  \"TRXL_TYPE\": \"\",\n"
					+ "  \"SELECTEDFIELDS\": \"\",\n"
					+ "  \"PRODUCT_NAME\": \"\",\n"
					+ "  \"RFU_REF_NO_GLOBAL\": [\"FALSE\"],\n"
					+ "  \"GTEE_WORD\": \"\",\n"
					+ "  \"GTEE_CCY\": \"{guranteeDetails.currency}\",\n"
					+ "  \"INT_SYSFNTYPE\": \"\",\n"
					+ "  \"TEMP_FOR_AC_OF_ADD\": \" Guarantee Purpose: YTTY\",\n"
					+ "  \"FILE_23X_CODE\": \" \",\n"
					+ "  \"CONF_BKID_ADD3\": \"\",\n"
					+ "  \"CONF_BKID_ADD2\": \"\",\n"
					+ "  \"LOGIN_NAME\": \"\",\n"
					+ "  \"C_MAIN_REF\": \"488000005172011\",\n"
					+ "  \"REASON_FOR_REJ\": \"\",\n"
					+ "  \"IMG_INQ_TYPE\": \"\",\n"
					+ "  \"CATA_PAGE_SIZE\": \"\",\n"
					+ "  \"_TRX_STATUS\": \"\",\n"
					+ "  \"STAN_WORD_REQD_C\": \"\",\n"
					+ "  \"_SYS_EVENT_TIME\": \"1\",\n"
					+ "  \"PREV_TRAN_NAME\": \"\",\n"
					+ "  \"EXPIRY_DT_HIJRI_BT\": \"1446-5-18\",\n"
					+ "  \"APPL_NM_C\": \"\",\n"
					+ "  \"SUB_CATA_ID\": \"\",\n"
					+ "  \"BENE_BK_NM\": \"\",\n"
					+ "  \"GTEE_TYPE\": \"Bid Bonds (Tender Guarantees)\",\n"
					+ "  \"ISSBK_NAME\": \"\",\n"
					+ "  \"ISSBK_BK_ADD3_C\": \"\",\n"
					+ "  \"needOutPutInfo\": \"true\",\n"
					+ "  \"CONTCT_NO\": \"\",\n"
					+ "  \"DELIV_OF_ORIG_UNDER\": \"\",\n"
					+ "  \"INT_LMTAVAILAMT\": \"\",\n"
					+ "  \"COMM_BANK\": \"\",\n"
					+ "  \"PRODUCT_ID\": \"\",\n"
					+ "  \"TrxSessionId\": \"8A8283930193441E4C41005F\",\n"
					+ "  \"AVBL_ID_NAME_C\": \"\",\n"
					+ "  \"CONF_BKID_FV_BT\": \"FAVOURITES\",\n"
					+ "  \"ADV_THRU_BK_NAME\": \"\",\n"
					+ "  \"PPC_AVAIL_REF\": \"\"\n"
					+ "}\n"
					+ "";

			String resultString = replacePlaceholders(templateString, mapBody);

			System.out.println("result is :"+resultString);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> flattenMap(Map<String, Object> map, String parentKey) {
		Map<String, Object> flatMap = new HashMap<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = (parentKey != null ? parentKey + "." : "") + entry.getKey();
			Object value = entry.getValue();
			if (value instanceof Map) {
				flatMap.putAll(flattenMap((Map<String, Object>) value, key));
			} else {
				flatMap.put(key, value);
			}
		}
		return flatMap;
	}

	public static String replacePlaceholders(String templateString, Map<String, Object> mapBody) {
		// Flatten nested map
		Map<String, Object> flatMap = flattenMap(mapBody, null);

		// Replace placeholders
		for (Map.Entry<String, Object> entry : flatMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			templateString = templateString.replace("{" + key + "}", value != null ? value.toString() : "");
		}
		return templateString;
	}

	public static Object getKeyFromValue(Map<String, String> hm, Object value) {
		Object o =  null;
		for (Entry<String, String> entry : hm.entrySet()) {
			if (entry.getValue().equals(value)) {
				o = entry.getKey();
			}
		}
		return o;
	}

}
