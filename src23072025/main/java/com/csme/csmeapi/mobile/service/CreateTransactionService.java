package com.csme.csmeapi.mobile.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.cs.base.log.CSLogger;
import com.cs.base.utility.ASPathConst;
import com.cs.base.xml.XMLManager;
import com.cs.ce.core.helper.RecordConverter;
import com.cs.ceap.ejb.business.CETrxInBoxManagerRecBean;
import com.cs.core.cache.CacheAPParameterHelper;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.exec.SQLDao;
import com.cs.core.dao.exec.SQLDaoResult;
import com.cs.core.dao.exec.SQLGenTool;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.cs.core.dao.exec.SQLStatement;
import com.cs.core.intf.IReadMap;
import com.cs.core.pojo.UserInfo;
import com.cs.core.pojo.bo.FuncData;
import com.cs.core.pojo.bo.SimpleDO;
import com.cs.core.pojo.buinfo.Company;
import com.cs.core.pojo.buinfo.CompanyGroup;
import com.cs.core.pojo.buinfo.SysInfo;
import com.cs.core.pojo.image.Image;
import com.cs.core.pojo.image.ImgHeader;
import com.cs.core.pojo.swift.SwiftInfo;
import com.cs.core.request.Request;
import com.cs.core.request.TrxRequest;
import com.cs.core.result.FinalStatus;
import com.cs.core.result.GapiInfo;
import com.cs.core.result.Result;
import com.cs.core.result.TrxResult;
import com.cs.core.utility.SessionContext;
import com.cs.core.utility.SessionUtil;
import com.cs.core.utility.StringUtil;
import com.cs.core.xml.object.func.Func;
import com.cs.core.xml.object.func.function.Function;
import com.cs.core.xml.object.funcattr.FuncAttr;
import com.cs.core.xml.object.funcattr.component.Component;
import com.cs.eximap.busiintf.Busiintf;
import com.cs.eximap.ctrlintf.Ctrlintf;
import com.cs.eximap.ctrlintf.TrxManagerMaster;
import com.cs.eximap.log.ASCELog;
import com.cs.eximap.log.CELogSTDExec;
import com.cs.eximap.log.CELogSTPExec;
import com.cs.eximap.miscintf.delegate.TrxLedgerAddRec;
import com.cs.eximap.miscintf.delegate.TrxManagerImage;
import com.cs.eximap.stp.util.STPInIMGUtil;
import com.cs.eximap.utility.ASDBHelper;
import com.cs.eximap.utility.TrxRequestHelper;
import com.cs.eximap.utility.TrxResultHelper;
import com.cs.eximap.utility.helper.GapiHelper;
import com.cs.eximap.utility.helper.SwiftHelper;
import com.csme.csmeapi.mobile.model.CommonApplicantDetails;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateTransactionService {
	public CreateTransactionService() throws Exception {
	}

	private CSLogger logger = ASCELog.getCELogger("csme-rest");
	private String strBKGP = "CSBANK" ;
	private String strCNTY = "SA";

	public static String replacePlaceholders(String templateString, Map<String, Object> mapBody) {
        Map<String, Object> flatMap = flattenMap(mapBody, null);

        for (Map.Entry<String, Object> entry : flatMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            templateString = templateString.replace("{" + key + "}", value != null ? value.toString() : "");
            templateString.replaceAll("\\{[^{}]*\\}", "null");
        }
        templateString = templateString.replaceAll("\\{[^{}]*\\}", "null");
        return templateString;
    }

    private static Map<String, Object> flattenMap(Map<String, Object> map, String prefix) {
        Map<String, Object> flatMap = new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = (prefix == null) ? entry.getKey() : prefix + "." + entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                // Recursive call for nested map
                flatMap.putAll(flattenMap((Map<String, Object>) value, key));
            } else if (value instanceof List) {
                // Handle lists (convert first element, join multiple values)
                List<?> list = (List<?>) value;
                if (!list.isEmpty()) {
                    if (list.get(0) instanceof Map) {
                        int index = 0;
                        for (Object obj : list) {
                            if (obj instanceof Map) {
                                flatMap.putAll(flattenMap((Map<String, Object>) obj, key + "[" + index + "]"));
                            }
                            index++;
                        }
                    } else {
                        // If list contains primitive types, convert to string
                        flatMap.put(key, list.size() == 1 ? list.get(0).toString() : list.toString());
                    }
                } else {
                    flatMap.put(key, "");
                }
            } else {
                // Add normal key-value pairs
                flatMap.put(key, value);
            }
        }
        return flatMap;
    }

	public static Object getKeyFromValue(Map<String, String> hm, Object value) {
		for (Map.Entry<String, String> entry : hm.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static String replacePlaceholders(String templateString, CommonApplicantDetails applicantDetails) {
		Map<String, Object> detailsMap = applicantDetails.toMap();

		// Replace placeholders dynamically
		for (Map.Entry<String, Object> entry : detailsMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			templateString = templateString.replace("{" + key + "}", value != null ? value.toString() : "");
		}
		return templateString;
	}

	@SuppressWarnings("unchecked")
	public Result getFunction(@Valid Map<String, Object> body, String method) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		CommonApplicantDetails commonApplicantDetails = new CommonApplicantDetails();

		try {
			// Validate input arguments
			if (body == null || body.isEmpty()) {
				logger.error("Input body is null or empty");
				throw new IllegalArgumentException("Input body cannot be null or empty");
			}

			if (method == null || method.trim().isEmpty()) {
				logger.error("Method is null or empty");
				throw new IllegalArgumentException("Method cannot be null or empty");
			}

			// Extract application details
			Map<String, Object> applicationDetailsMap = (Map<String, Object>) body.get("applicationDetails");
			if (applicationDetailsMap == null || applicationDetailsMap.isEmpty()) {
				logger.error("Application details are missing");
				throw new IllegalArgumentException("Application details cannot be null or empty");
			}

			// Extract outward guarantee
			Map<String, Object> outwardGuaranteeMap = (Map<String, Object>) applicationDetailsMap.get("outwardGuarantee");
			String outwardGuaranteeValue = (String) outwardGuaranteeMap.get("value");
			if (outwardGuaranteeValue == null) {
				logger.error("Outward guarantee value is missing");
				throw new IllegalArgumentException("Outward guarantee value is required");
			}

			// Extract details based on outward guarantee
			Map<String, Object> detailsMap = "Local".equalsIgnoreCase(outwardGuaranteeValue)
					? (Map<String, Object>) applicationDetailsMap.get("localDetails")
							: (Map<String, Object>) applicationDetailsMap.get("foreignDetails");

			if (detailsMap == null || detailsMap.isEmpty()) {
				logger.error("Details for the selected guarantee type are missing");
				throw new IllegalArgumentException("Details for the selected guarantee type cannot be null or empty");
			}

			// Populate applicant details
			populateApplicantDetails(commonApplicantDetails, detailsMap);

			// Process attachments
			Map<String, Object> textGuaranteeIssuedMap = (Map<String, Object>) applicationDetailsMap.get("textGuaranteeIssued");
			List<Map<String, Object>> attachmentsList = (List<Map<String, Object>>) textGuaranteeIssuedMap.get("attachments");

			Image image = null;
			if(attachmentsList != null && attachmentsList.size() > 0) {
				StringBuilder attDocStringBuilder = new StringBuilder();
				attDocStringBuilder.append("<root><send-item hasAttach=\"true\"><item name=\"image\">");
	
				for (Map<String, Object> attachment : attachmentsList) {
					String filenetReference = (String) attachment.get("filenetReference");
					String attachmentName = (String) attachment.get("attachmentName");
	
					attDocStringBuilder.append("<index desc=\"")
					.append(filenetReference).append("_").append(attachmentName)
					.append("\" format=\"pdf\" isCompressed=\"true\" size=\"0\" value=\"\">")
					.append("<content><![CDATA[").append("").append("]]></content></index>");
				}
				attDocStringBuilder.append("</item></send-item></root>");
	
				Document document = XMLManager.xmlStrToDom(attDocStringBuilder.toString());
				Element rootElement = document.getDocumentElement();
				Node sendItemNode = XMLManager.findChildNode(rootElement, "send-item");
				image = STPInIMGUtil.getImageInfo(sendItemNode, rootElement);
				ImgHeader hdr = image.getImgHeader(0);
				hdr.setType("Others");
				hdr.setEType("Others");
			}

			// Load and process the template JSON
			Map<String, Object> templateJson = loadTemplateJson(method);
			if (templateJson == null || templateJson.isEmpty()) {
				logger.error("Unable to retrieve template JSON");
				throw new IllegalStateException("Template JSON is missing or invalid");
			}

			String templateString = objectMapper.writeValueAsString(templateJson);
			Map<String, Object> combinedMap = new HashMap<>(body);
			combinedMap.putAll(commonApplicantDetails.toMap());
			String resultString = replacePlaceholders(templateString, combinedMap);
			logger.error("resultString is :"+resultString);
			JSONObject jsonObject = new JSONObject(resultString);
			SimpleDO productMap = jsonToMap(jsonObject);

			// Prepare function data
			FuncData funcData = new FuncData();
			funcData.setImageData(image);
			funcData.setCatalogData(productMap);
			funcData.setScreenData(productMap);
			funcData.setOriginalData(productMap);

			String funcId = productMap.get("C_FUNC_ID");
			Func function = CacheAPParameterHelper.getObjFunc(this.strBKGP, this.strCNTY, funcId);
			String mainComp = function.getFunction().getMainComp();

			// Prepare context and session
			SessionContext ctx = new SessionContext();
			setSTPUserInfo(productMap.get("C_UNIT_CODE"), productMap.get("C_USER_ID"), ctx);

			SysInfo sysInfo = new SysInfo();
			sysInfo.setOriginalFuncID(funcId);
			sysInfo.setFuncId(funcId);
			sysInfo.setCompInfo(setCmpId(productMap.get("C_UNIT_CODE")));
			ctx.setSysInfo(sysInfo);

			Request request = new Request(3);
			request.setComponent(mainComp);
			request.setRequestInfo(funcData);
			request.setContext(ctx);

			SessionUtil.setSessionContext(request.getContext());

			// Execute request and return result
			Result result = new Result();
			return getDataCreatePending(request, result);

		} catch (Exception e) {
			logger.error("An unexpected exception occurred while processing request: {}"+ body+ e);
			throw e; // Re-throw to ensure exception visibility
		}
	}


	private void populateApplicantDetails(CommonApplicantDetails details, Map<String, Object> source) {
		details.setBeneficiaryName((String) source.get("BeneficiaryName"));
		details.setAddress((String) source.get("Address"));
		details.setZipCode((String) source.get("ZipCode"));
		details.setPoBox((String) source.get("POBox"));
		details.setCity((String) source.get("City"));
		details.setMobileNumber((String) source.get("MobileNumber"));
		details.setSwiftCode((String) source.get("SwiftCode"));
		details.setCountry((String) source.get("Country"));
	}


	private Map<String, Object> loadTemplateJson(String method) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String fileName;

		// Determine the file based on the HTTP method
		if ("POST".equalsIgnoreCase(method)) {
			fileName = "CreateRequestTemplate.json";
		} else if ("PUT".equalsIgnoreCase(method)) {
			fileName = "UpdateRequestTemplate.json";
		} else {
			fileName = "template.json";
		}

		File file = new File(ASPathConst.USER_DIR_PATH + File.separator + "INT" + File.separator + fileName);

		if (!file.exists() || !file.isFile()) {
			logger.error("Template JSON file not found at path: {}"+ file.getAbsolutePath());
			throw new IOException("Template JSON file not found: " + fileName);
		}

		try (InputStream inputStream = new FileInputStream(file)) {
			return objectMapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {});
		}
	}

	public static SimpleDO jsonToMap(JSONObject json) throws JSONException {
		SimpleDO retMap = new SimpleDO();

		if(json != JSONObject.NULL) {
			retMap = toMap(json);
		}
		return retMap; 
	}

	public static SimpleDO toMap(JSONObject object) throws JSONException {
		SimpleDO map = new SimpleDO();

		Iterator<?> keysItr = object.keys();
		while(keysItr.hasNext()) {
			String key = (String) keysItr.next();
			Object value = object.get(key);

			if(value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if(value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	public static List<Object> toList(JSONArray array) throws JSONException {
		List<Object> list = new ArrayList<>();
		for(int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if(value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if(value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}

	public Result getDataCreatePending(Request aReq, Result aRes) throws Exception {
		TrxRequest trxRequest = TrxRequestHelper.getTrxRequest(aReq);
		TrxResult trxResult = TrxResultHelper.getTrxResult(aRes);
		SimpleDO screen = trxRequest.getScreenData();
		GapiInfo gapiInfo = null;
		SwiftInfo swiftInfo = null;
		boolean isSave = trxRequest.isSave();
		String trxStatus = isSave ? "S" : "M";
		trxRequest.setTrxStatus(trxStatus);
		String inboxStatus = isSave ? "Z" : "M";
		String screenStatus = screen.get("C_TRX_STATUS");
		if (!StringUtil.isEmpty(screenStatus, true))
			inboxStatus = screenStatus; 
		trxRequest.setInboxStatus(inboxStatus);
		setFinalStatus(trxRequest, (Result)trxResult);
		try {
			FuncAttr funcAttr = trxRequest.getPostFuncAttr();
			for (int i = 0, length = funcAttr.getComponentSize(); i < length; i++) {
				Component comp = funcAttr.getComponent(i);
				String name = comp.getName();
				Busiintf obj = (Busiintf)Class.forName("com.cs.eximap.busiintf." + name).newInstance();
				obj.getDataMaster((Request)trxRequest, comp, (Result)trxResult);
				if (!trxResult.isSuccess())
					return (Result)trxResult; 
			} 
			gapiInfo = trxResult.getGapiReturn();
			if (gapiInfo != null && gapiInfo.isHasGapi())
				trxRequest.setInboxStatus("S"); 
			swiftInfo = trxResult.getSwiftResult();
			if (swiftInfo != null && swiftInfo.hasSendSwift())
				trxRequest.setInboxStatus("S"); 
			TrxLedgerAddRec ledger = new TrxLedgerAddRec();
			trxResult = (TrxResult)ledger.getDataMaster((Request)trxRequest, (Result)trxResult);
			if (!trxResult.isSuccess())
				return (Result)trxResult; 
			TrxManagerImage managerImg = new TrxManagerImage();
			managerImg.getDataMaster((Request)trxRequest, (Result)trxResult);
			if (!trxResult.isSuccess())
				return (Result)trxResult; 
			CETrxInBoxManagerRecBean inboxRec = new CETrxInBoxManagerRecBean(trxRequest);
			inboxRec.getUpdateEventData();
			String isBatch = (String)aReq.getContext().getAPAttribute("isBatch");
			if (StringUtil.isTrue(isBatch)) {
				trxResult.setBatchSqlDao(trxRequest.getDao());
			} else {
				SQLDaoResult daoResult = ASDBHelper.executeUpdate(trxRequest.getDao());
				if (!daoResult.isSuccess()) {
					trxResult.setError(daoResult.getException());
					return (Result)trxResult;
				} 
				trxResult.setResult(daoResult);
			} 
			return (Result)trxResult;
		} catch (Exception e) {
			if (gapiInfo != null)
				GapiHelper.sendDom(gapiInfo, trxRequest); 
			if (swiftInfo != null)
				SwiftHelper.sendDom(swiftInfo, trxRequest); 
			throw e;
		} 
	}

	public void setFinalStatus(TrxRequest trxRequest, Result aResult) {
		String oldEvent = trxRequest.getOldEventTime();
		String newEvent = trxRequest.getNewEventTime();
		String inboxStatus = trxRequest.getInboxStatus();
		String masterStatus = trxRequest.getTrxStatus();
		FinalStatus finalStatus = new FinalStatus();
		finalStatus.setOldEvent(oldEvent);
		finalStatus.setNewEvent(newEvent);
		finalStatus.setInboxStatus(inboxStatus);
		finalStatus.setMasterStatus(masterStatus);
		if (aResult instanceof TrxResult)
			((TrxResult)aResult).setFinalStatus(finalStatus); 
	}

	public void setSTPUserInfo(String sUnitCode, String sUserID, SessionContext cxt) {
		if (sUnitCode == null || sUserID == null) {
			CELogSTPExec.info(this, "[ABU/abu_default.xml][stp.setSTPUserInfo()][error unit code|user id to pass]");
			return;
		} 
		String secuDS = DSManager.getSecuDS();
		try {
			String dbType = DSManager.getDBType(secuDS);
			String unitTable = DSManager.getSchemaedTableName(secuDS, "SEC_BUSINESS_UNIT");
			String userTable = DSManager.getSchemaedTableName(secuDS, "SEC_USER_INFO");
			SQLGenTool sqlTool = new SQLGenTool(4, unitTable, dbType);
			sqlTool.addField("*", null, 1);
			sqlTool.appendClause("WHERE");
			sqlTool.appendClause("C_UNIT_CODE", 1, "=", sUnitCode);
			SQLStatement stmt = sqlTool.getSqlStatement();
			SQLDao dao = new SQLDao();
			dao.setDataSource(secuDS);
			dao.addSqlStatement(stmt);
			sqlTool = new SQLGenTool(4, userTable, dbType);
			sqlTool.addField("*", null, 1);
			sqlTool.appendClause("WHERE");
			sqlTool.appendClause("C_UNIT_CODE", 1, "=", sUnitCode);
			sqlTool.appendClause("AND");
			sqlTool.appendClause("C_USER_ID", 1, "=", sUserID);
			stmt = sqlTool.getSqlStatement();
			dao.addSqlStatement(stmt);
			SQLDaoResult result = ASDBHelper.executeQuery(dao);
			if (result.isSuccess()) {
				SQLRecordSet setUnit = result.getRecordSet(0);
				if (setUnit.isSuccess()) {
					SQLRecord recordUnit = setUnit.getFirstRecord();
					if (recordUnit == null)
						CELogSTPExec.error(this, "CEStpJs:setSTPUserInfo:\nrecordUnit is null!"); 
					CompanyGroup grp = dealUnitInfo(recordUnit);
					//adding company group in session
					cxt.setCmpGroup(grp);
				} else {
					Exception e = setUnit.getException();
					CELogSTPExec.error(this, e);
					throw e;
				} 
				SQLRecordSet setUser = result.getRecordSet(1);
				if (setUser.isSuccess()) {
					SQLRecord recordUser = setUser.getFirstRecord();
					if (recordUser == null)
						CELogSTPExec.error(this, "CEStpJs:setSTPUserInfo:\nrecordUser is null!"); 
					//adding user in session
					UserInfo usr = dealUserInfo(recordUser);
					cxt.setUsrInfo(usr);
				} else {
					Exception e = setUser.getException();
					CELogSTPExec.error(this, e);
					throw e;
				} 
			} else {
				Exception e = result.getException();
				CELogSTPExec.error(this, e);
				throw e;
			} 
		} catch (Exception e) {
			CELogSTPExec.error(this, e);
			return;
		} 
	}

	private CompanyGroup dealUnitInfo(SQLRecord aRecordUnit) throws Exception {
		CompanyGroup company = RecordConverter.getCompanyGroup((IReadMap)aRecordUnit);
		return company;
	}

	private UserInfo dealUserInfo(SQLRecord aRecordUser) throws Exception {
		UserInfo userInfo = RecordConverter.getUserInfo((IReadMap)aRecordUser);
		return userInfo;
	}

	public Company setCmpId(String strId) throws Exception {
		Company company = null;
		try {
			String secuDS = DSManager.getSecuDS();
			String dbType = DSManager.getDBType(secuDS);
			String unitTable = DSManager.getSchemaedTableName(secuDS, "SEC_BUSINESS_UNIT");
			SQLGenTool sqlTool = new SQLGenTool(4, unitTable, dbType);
			sqlTool.addField("*", null, 1);
			sqlTool.appendClause("WHERE");
			sqlTool.appendClause("C_UNIT_CODE", 1, "=", strId);
			SQLStatement stmt = sqlTool.getSqlStatement();
			SQLDaoResult result = ASDBHelper.executeQuery(stmt, secuDS);
			if (result.isSuccess()) {
				SQLRecordSet recordSet = result.getFirstRecordSet();
				if (recordSet.isSuccess()) {
					SQLRecord record = recordSet.getFirstRecord();
					if (record == null)
						CELogSTPExec.error(this, "CEStpJs:setCmpId:\nrecord is null!"); 
					company = RecordConverter.getCompany(record);
				} else {
					Exception e = recordSet.getException();
					CELogSTPExec.error(this, e);
					throw e;
				} 
			} else {
				Exception e = result.getException();
				CELogSTPExec.error(this, e);
				throw e;
			} 
		} catch (Exception e) {
			CELogSTPExec.error(this, e);
			throw e;
		} 

		return company;
	} 


	public static void main(String[] args) {

		String format = String.format("%07d", 518);
		System.out.println(format);

		//		IssuanceOfBankGuaranteeRq rq = new IssuanceOfBankGuaranteeRq();
		//		// Populate fields of rq as needed
		//		rq.setEtradeReferenceNumber("E123456");
		//		rq.setDate("2024-11-25");
		//
		//		XBranchDetails branchDetails = new XBranchDetails();
		//		branchDetails.setCity("New York");
		//		branchDetails.setBranchName("Downtown Branch");
		//		rq.setBranchDetails(branchDetails);

		// Convert object to Map
		//        Map<String, Object> resultMap = convertToMap(rq);
		//        System.out.println(resultMap);



		//        String templateString = "{\"CNTC_DETL\":\"\",\"GTEE_AMT\":\"{etradeReferenceNumber}\",\"INT_OLDLMTREF\":\"\",\"ITEM_NAME\":\"\",\"EXPIRY_DT\":\"2024-11-20\",\"INT_LMTEARREF\":\"\",\"ISSBK_ID_C\":\"\",\"DELIV_OF_ORIG_CODE_C\":\"\",\"FUNC_ID\":\"\",\"GTEE_FRMT\":\"STANDARD FORMAT\",\"INT_LMTREF\":\"\",\"ADV_THRU_BK_ADD3\":\"\",\"ISSBK_ADD\":\"\",\"CUST_INSTR\":\"\",\"ADV_THRU_BK_ADD2\":\"\",\"AVBL_ID_ADD2_C\":\"\",\"ADV_THRU_SWIFT_ADD\":\"\",\"AVBL_BKID_NAME\":\"\",\"TRX_DT_HIJRI\":\"1446-5-17\",\"TEMP_BENE_ADD\":\"TSR\",\"ISSBK_SWIFT_ADD\":\"\",\"menucheck\":\"on\",\"AVBL_BKID_FV_BT_C\":\"FAVOURITES\",\"BENE_BK_SW_ADD\":\"\",\"CATA_TYPE\":\"\",\"AVBL_SW_ADD_C\":\"\",\"TRAN_TYPE\":\"\",\"T_SVR_GMT\":\"15:11:01\",\"SEND_TO_ADD1\":\"\",\"ADD_AMT_INFO_C\":\"\",\"SEND_TO_ADD3\":\"\",\"PARTY_NM\":\"\",\"SEND_TO_ADD2\":\"\",\"INT_LMTCOLLTMP\":\"\",\"CSRFTOKEN\":[\"ei8AsdGK82shr8J3Q809q3W395aL2F5C2GD80V1938g4AD41mEZF4i2C6q200060\",\"ei8AsdGK82shr8J3Q809q3W395aL2F5C2GD80V1938g4AD41mEZF4i2C6q200060\"],\"AVBL_BKID_FV_BT\":\"FAVOURITES\",\"BENE_BK_ID\":\"\",\"CONTCT_DETL\":\"YTTY\",\"UNIT_CODE\":\"\",\"AUTO_EXTEND_NOTIF_PERIOD_LOC\":\"\",\"APPLICABLE_RULES_C\":\"\",\"AVBL_ID_ADD3_C\":\"\",\"INT_LMTEXPIRY\":\"\",\"FILE_23X_NARR\":\"\",\"AVBL_ID_C\":\"\",\"GTEE_TYPE_JO\":\"Bid Bonds (Tender Guarantees)\",\"CONF_BKID_NAME\":\"\",\"AVBL_BKID_SWIFT_ADD\":\"\",\"INT_RETURNCODE\":\"\",\"RM_DECISION\":\"No\",\"APPLICABLE_RULES\":\"NONE\",\"SESSION_ID\":\"xszxjPP04W1Jteed6fTy0jO\",\"EXCELTYPE\":\"\",\"AUTO_EXTEN_PERIOD_DAYS_C\":\"\",\"CURR_OPER\":\"\",\"ISSBK_SWIFT_ADD_C\":\"\",\"OBLIGOR_INS_ADD_C\":\"\",\"TRANS_CONDITION\":\"\",\"CHG_AC\":\"1212\",\"GOVERN_LAW_CNTY_CODE\":\"SA\",\"FORM_OF_UNDERTAKING_C\":\"\",\"ADV_ID\":\"\",\"TEMP_SEND_NAME\":\"\",\"CHG_AC_CCY\":\"SAR\",\"TRAN_NAME\":\"\",\"GTEE_AMT_C\":\"0\",\"UNDERTAKING_TYPE_C\":\"\",\"CUST_TYPE\":\"\",\"EXPIRY_COND_C\":\"\",\"DOC_PRES_INSTR\":\"\",\"TRANS_CONDITION_C\":\"\",\"GOVERN_LAW_CNTY_CODE_C\":\"SA\",\"EXPIRY_TYPE\":\"FIXD\",\"CONF_BK_SWIFT_ADD\":\"\",\"ENG_ADD\":[\"TSR\"],\"PARTY_ID\":\"\",\"_EVENT_TITLE\":\"\",\"AUTO_EXTEN_PERIOD_DAYS\":\"\",\"PARENT_MAIN_REF\":\"488000005172011\",\"GOVERN_LAW\":\"\",\"TITLE_NAME\":\"\",\"AUTO_EXTEN_NOTIF_C\":\"\",\"DELIV_OF_ORIG_CODE\":\"\",\"CONF_BKID_ID\":\"\",\"DoType\":\"\",\"AjaxPost\":\"T\",\"_ACT_TYPE\":\"\",\"OBLIGOR_INS_NAME\":\"\",\"OBLIGOR_INS_NAME_C\":\"\",\"GRP_NAME\":\"\",\"FORM_OF_UNDERTAKING\":\"DGAR\",\"INI_ITEM_ID\":\"\",\"SAVED\":\"\",\"EXPIRY_COND\":\"\",\"EXPIRY_TYPE_C\":\"\",\"TMP_LMTEARREF\":\"\",\"DELIVERY_TO_C\":\"\",\"AVBL_ID\":\"\",\"AVBL_BKID_ADD3\":\"\",\"INT_LMTEDATE\":\"\",\"AVBL_BKID_ADD2\":\"\",\"AUTO_EXTEN_PERIOD_C\":\"\",\"CONF_BKID_ADD\":\"\",\"APPL_ID\":\"1517524\",\"AUTO_EXTEN_PERIOD\":\"\",\"PURP_OF_MESS\":\"ACNF\",\"BENE_BT\":\"\",\"INT_RETURNMESSAGE\":\"\",\"CE_SELECT_CATAFIELD\":\"\",\"INT_LMTCOLL\":\"\",\"TEMP_ADV_THR_BKADD\":\"\",\"STAN_WORD_REQD_LANG_C\":\"\",\"AVBL_BKID_ADD\":\"\",\"ADD_AMT_INFO\":\"\",\"CURRNT_STATUS\":[\"Awaiting Verification\",\"Awaiting Verification\"],\"ISSUE_DATE_C\":\"\",\"ADV_THRU_BK_FV_BT\":\"FAVOURITES\",\"FUN_DESC\":\"Apply for Guarantee تقديم طلب ضمان\",\"CATA_NUM\":\"\",\"ExcelType\":\"\",\"C_LAN_VAL\":null,\"SEND_TO_NAME\":\"\",\"FUN_NAME\":\"\",\"ID\":\"\",\"BENE_TEMP_ID\":\"\",\"RFU_REF_NO\":[\"488000005172011\"],\"INT_LMTCASHMRGN\":\"\",\"CONF_INSR\":\"\",\"CHG_DESC_C\":\"\",\"CHG_DESC_B\":\"\",\"INT_LMTSDATE\":\"\",\"CURR_DATE\":\"\",\"ADV_THRU_BK_ADD\":\"\",\"ITEM_ID\":\"\",\"CUST_NO\":\"488000005172011\",\"TRANS_COMM\":\"\",\"BENE_NM_C\":\"\",\"BENE_BK_FV_BT\":\"FAVOURITES\",\"DELETE_EARMARK\":\"No\",\"GOVERN_LAW_C\":\"\",\"SEL_TYPE\":\"\",\"CATA_CURR\":\"\",\"UNDERTAKING_TYPE_NARR_C\":\"\",\"AUTO_EXTEND_NOTIF_PERIOD_LOC_C\":\"\",\"FL_FLAG\":\"Local\",\"ISSBK_ID\":\"\",\"UNDEL_TRANS_DET\":\"\",\"TRX_DT\":\"2024-11-19\",\"INT_LMTPCODE\":\"\",\"UNDER_GTEE_AMT\":\"\",\"DEMAND_INDICATOR\":\"\",\"BENE_NM\":\"212\",\"ISSUE_BK_FV_BT\":\"FAVOURITES\",\"DELIV_OF_ORIG_UNDER_C\":\"\",\"ISSBK_BK_ADD2\":\"\",\"ISSBK_BK_ADD3\":\"\",\"DELIVERY_TO\":\"\",\"GTEE_CCY_B\":\"\",\"AUTO_EXTEN_EXPIRY_DT_C\":\"\",\"GTEE_CCY_C\":\"\",\"ISSBK_ADD_C\":\"\",\"ISSBK_NAME_C\":\"\",\"BENE_BK_ADD2\":\"\",\"BENE_BK_ADD3\":\"\",\"BENE_BK_ADD1\":\"\",\"TRANS_INDICATOR\":\"\",\"ISSUE_BK_FV_BT_C\":\"FAVOURITES\",\"D_SVR_GMT\":\"2024-11-19\",\"EXPIRY_DT_C\":\"\",\"APPL_NM\":\"SULAIMAN ABDUL AZIZ ALRAJHI EST.\",\"BENE_REF_ID\":\"\",\"C_IP_ADDRESS\":\"10.3.1.90\",\"NXT_STATUS\":[\"Bank Decision on Guarantee\",\"Bank Decision on Guarantee\"],\"DOC_PRES_INSTR_C\":\"\",\"INT_LMTDET\":\"\",\"INT_LMTCCY\":\"\",\"ISSBK_BK_ADD2_C\":\"\",\"C_UNIT_NAME\":\"\",\"AUTO_EXTEN_EXPIRY_DT\":\"\",\"DEMAND_INDICATOR_C\":\"\",\"OBLIGOR_INS_ADD\":\"\",\"AVBL_ID_ADD_C\":\"\",\"AUTO_EXTEN_NOTIF\":\"\",\"REFRESH_TOKEN\":\"1732014656614\",\"TRXL_TYPE\":\"\",\"SELECTEDFIELDS\":\"\",\"PRODUCT_NAME\":\"\",\"RFU_REF_NO_GLOBAL\":[\"FALSE\"],\"GTEE_WORD\":\"\",\"GTEE_CCY\":\"SAR\",\"INT_SYSFNTYPE\":\"\",\"TEMP_FOR_AC_OF_ADD\":\" Guarantee Purpose: YTTY\",\"FILE_23X_CODE\":\" \",\"CONF_BKID_ADD3\":\"\",\"CONF_BKID_ADD2\":\"\",\"LOGIN_NAME\":\"\",\"C_MAIN_REF\":\"488000005172011\",\"REASON_FOR_REJ\":\"\",\"IMG_INQ_TYPE\":\"\",\"CATA_PAGE_SIZE\":\"\",\"_TRX_STATUS\":\"\",\"STAN_WORD_REQD_C\":\"\",\"_SYS_EVENT_TIME\":\"1\",\"PREV_TRAN_NAME\":\"\",\"EXPIRY_DT_HIJRI_BT\":\"1446-5-18\",\"APPL_NM_C\":\"\",\"SUB_CATA_ID\":\"\",\"BENE_BK_NM\":\"\",\"GTEE_TYPE\":\"Bid Bonds (Tender Guarantees)\",\"ISSBK_NAME\":\"\",\"ISSBK_BK_ADD3_C\":\"\",\"needOutPutInfo\":\"true\",\"CONTCT_NO\":\"\",\"DELIV_OF_ORIG_UNDER\":\"\",\"INT_LMTAVAILAMT\":\"\",\"COMM_BANK\":\"\",\"PRODUCT_ID\":\"\",\"TrxSessionId\":\"8A8283930193441E4C41005F\",\"AVBL_ID_NAME_C\":\"\",\"CONF_BKID_FV_BT\":\"FAVOURITES\",\"ADV_THRU_BK_NAME\":\"\",\"PPC_AVAIL_REF\":\"\"}";
		//		String resultString = replacePlaceholders(templateString, resultMap);
		//		System.out.println(resultString);

		//		String body = "class IssuanceOfBankGuaranteeRq {\r\n"
		//				+ "    etradeReferenceNumber: string\r\n"
		//				+ "    date: string\r\n"
		//				+ "    branch: string\r\n"
		//				+ "    branchDetails: class XBranchDetails {\r\n"
		//				+ "        city: string\r\n"
		//				+ "        branchName: string\r\n"
		//				+ "        contactPerson: string\r\n"
		//				+ "        branchAddress: string\r\n"
		//				+ "        branchLocationLink: string\r\n"
		//				+ "    }\r\n"
		//				+ "    applicationDetails: class XApplicationDetails {\r\n"
		//				+ "        outwardGuarantee: Local\r\n"
		//				+ "        localDetails: {TextGuaranteeIssued={ThirdPartyName=string, option=ThirdParty, attachments=[{AttachmentContent=Base64, AttachmentName=string, AttachmentSize=string, AttachmentType=string}]}, BeneficiaryName=string, Address=string, ZipCode=string, POBox=string, City=string, MobileNumber=string}\r\n"
		//				+ "        foreignDetails: {TextGuaranteeIssued={ThirdPartyName=string, option=ThirdParty, attachments=[{AttachmentContent=Base64, AttachmentName=string, AttachmentSize=string, AttachmentType=string}]}, BeneficiaryName=string, Address=string, ZipCode=string, POBox=string, City=string, MobileNumber=string, SwiftCode=string, Country=string}\r\n"
		//				+ "    }\r\n"
		//				+ "    guranteeDetails: class XGuaranteeDetails {\r\n"
		//				+ "        typeofguarantee: string\r\n"
		//				+ "        otherType: string\r\n"
		//				+ "        purposeofGuarantee: string\r\n"
		//				+ "        toBeIssued: class UnApprovalWording {\r\n"
		//				+ "            toBeIssued: null\r\n"
		//				+ "            unApprovalDetails: null\r\n"
		//				+ "        }\r\n"
		//				+ "        cashMargin: FullCover\r\n"
		//				+ "        limitPercentage: 0\r\n"
		//				+ "        amount: string\r\n"
		//				+ "        currency: string\r\n"
		//				+ "        amountInWords: string\r\n"
		//				+ "        contractValue: string\r\n"
		//				+ "        expiryDate: 2024-11-24\r\n"
		//				+ "    }\r\n"
		//				+ "    customerDetails: class XCustomerDetails {\r\n"
		//				+ "        customerName: string\r\n"
		//				+ "        accountNumber: string\r\n"
		//				+ "        contactName: string\r\n"
		//				+ "        contactNumber: string\r\n"
		//				+ "    }\r\n"
		//				+ "}";
		//		Map<String, Object> s = convertToMap(body);
		//		System.out.println(s);
	}

}
