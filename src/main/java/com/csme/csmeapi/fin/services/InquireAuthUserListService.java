package com.csme.csmeapi.fin.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.text.StringSubstitutor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cs.base.xml.XMLManager;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XAuthUserListInfoObj;
import com.csme.csmeapi.fin.models.XAuthUserListRequest;
import com.csme.csmeapi.fin.models.XAuthUserListResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class InquireAuthUserListService {
	
	FinUtil finUtil = new FinUtil();

	/**
	 * Logger for logging information related to inquiry transactions.
	 */
	Logger logger = LogManager.getLogger("CSMEMobile");
	/**
	 * The secure data source for database operations.
	 * Initialized as null and expected to be set during class initialization.
	 */
	String secuDs = null;
	/**
	 * Object mapper for converting objects to JSON format and vice versa.
	 */
	ObjectMapper objectMapper = new ObjectMapper();
	/**
	 * Language code used for inquiry transactions.
	 * Initialized as null and expected to be set based on the request context.
	 */
	public String Lang = null;

	/**
	 * Initializes the InquireTransactionApiService.
	 * Retrieves the secure data source for database operations.
	 */
	public InquireAuthUserListService() {
		try {
			this.secuDs = FinUtil.SECDS;
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Constructs a map containing key-value pairs based on the specified inquiry transaction request.
	 * The keys represent attributes such as main reference, corporate ID, event times, module name, and user ID.
	 * 
	 * @param body The inquiry transaction request object from which to extract values.
	 * @return A map containing key-value pairs representing the request attributes.
	 */
	private Map<String, String> getValuesMap(@Valid XAuthUserListRequest body) {
		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put("mainref", body.getMainRef());
		valuesMap.put("unitcode", body.getCorporateId());
		valuesMap.put("eventtimes", body.getEventTimes());
		valuesMap.put("modulename", body.getModule());
		valuesMap.put("userid", body.getUserId());
		valuesMap.put("module", body.getModule());
		return valuesMap;
	}

	public XAuthUserListResponse getInquireAuthUserList(@Valid XAuthUserListRequest body, UUID requestId,
			Long sequence) {
		XAuthUserListResponse xAuthUserListResponse = new XAuthUserListResponse();
		try {
			Lang = FinUtil.getLang(body.getCorporateId(), body.getUserId());
			logger.info("Inquiry Transaction Language is :"+Lang);
			String module = body.getModule();
			String filePath = FinUtil.intPath+module+"Trx.xml";
			logger.info("Inquiry Transaction File path is :"+filePath);
			Document inquiryXml = XMLManager.xmlFileToDom(filePath);
			
			
			Element rootInqEle = inquiryXml.getDocumentElement();
			String fetchNextAuthListQuery = FinUtil.getValueByTagName(rootInqEle, "FetchNextAuthList");
			StringSubstitutor attachmentsubstitutor = new StringSubstitutor(getValuesMap(body));
			String finalQuery = attachmentsubstitutor.replace(fetchNextAuthListQuery);
			logger.info("Inquiry Transaction Final Query is :"+finalQuery);
			SQLRecordSet nextAuthListRec = WSDBHelper.executeQuery(finalQuery, this.secuDs);
			
			List<XAuthUserListInfoObj> xAuthUserListInfoObjList = new ArrayList<XAuthUserListInfoObj>();
			XAuthUserListInfoObj XAuthUserListInfoObj = new XAuthUserListInfoObj();
			
			//Fetch Next Auth List
			ArrayNode nextUserAuthListArr = objectMapper.createArrayNode();
			if (nextAuthListRec != null && nextAuthListRec.getRecordCount() > 0) {
				for (int a = 0; a < nextAuthListRec.getRecordCount() ; a++) {
					SQLRecord nextAuthListRecSql = nextAuthListRec.getSQLRecord(a);
					JSONObject childValueInfoJson = new JSONObject();
					String userId = nextAuthListRecSql.getValue("C_USER_ID");
					String authLevel = nextAuthListRecSql.getValue("I_AUTH_LEVEL");
					String authStatus = nextAuthListRecSql.getValue("C_TRX_STATUS"); 
					childValueInfoJson.put("Userid", userId);
					childValueInfoJson.put("AuthLevel", authLevel);
					childValueInfoJson.put("AuthStatus", authStatus);
					JsonNode tabInfoNode = objectMapper.readTree(childValueInfoJson.toString());
					nextUserAuthListArr.add(tabInfoNode);
				}
				XAuthUserListInfoObj.setPendingUserList(nextUserAuthListArr);
			}
			
			//Fetch Already Auth List
			
			ArrayNode alreadyUserAuthListArr = objectMapper.createArrayNode();

			rootInqEle = inquiryXml.getDocumentElement();
			String fetchAlreadyAuthListQuery = FinUtil.getValueByTagName(rootInqEle, "FetchAlreadyAuthList");
			attachmentsubstitutor = new StringSubstitutor(getValuesMap(body));
			finalQuery = attachmentsubstitutor.replace(fetchAlreadyAuthListQuery);
			logger.info("Inquiry Transaction Final Query is :"+finalQuery);
			SQLRecordSet alreadyAuthListRec = WSDBHelper.executeQuery(finalQuery, this.secuDs);
			
			if (alreadyAuthListRec != null && alreadyAuthListRec.getRecordCount() > 0) {
				for (int a = 0; a < alreadyAuthListRec.getRecordCount() ; a++) {
					SQLRecord alreadyAuthListRecSql = alreadyAuthListRec.getSQLRecord(a);
					JSONObject childValueInfoJson = new JSONObject();
					String userId = alreadyAuthListRecSql.getValue("C_USER_ID");
					String authLevel = alreadyAuthListRecSql.getValue("I_AUTH_LEVEL");
					String authStatus = alreadyAuthListRecSql.getValue("C_TRX_STATUS"); 
					childValueInfoJson.put("Userid", userId);
					childValueInfoJson.put("AuthLevel", authLevel);
					childValueInfoJson.put("AuthStatus", authStatus);
					JsonNode tabInfoNode = objectMapper.readTree(childValueInfoJson.toString());
					alreadyUserAuthListArr.add(tabInfoNode);
				}
				XAuthUserListInfoObj.setAuthorizedUserList(alreadyUserAuthListArr);
			} 
			xAuthUserListInfoObjList.add(XAuthUserListInfoObj);
			if(xAuthUserListInfoObjList.size() >0) {
				responseSet(requestId, sequence, "00", "Sucess",  xAuthUserListInfoObjList, xAuthUserListResponse);
			} else {
				responseSet(requestId, sequence, "08", "No Record exist",  new ArrayList<XAuthUserListInfoObj>(), xAuthUserListResponse);
			}
			
		}catch (Exception e) {
			logger.error("Error processing inquiry transaction: " + e.getMessage(), e);
			responseSet(requestId, sequence, "99", "General Exception",  new ArrayList<XAuthUserListInfoObj>(), xAuthUserListResponse);
		}
		return xAuthUserListResponse;
	}
	
	private XAuthUserListResponse responseSet(UUID requestId, Long sequence,String code, String desc,
			List<XAuthUserListInfoObj> xAuthUserListInfoObjList,XAuthUserListResponse xAuthUserListResponse) {
		xAuthUserListResponse.setRequestId(requestId.toString());
		xAuthUserListResponse.setMessageId(sequence.toString());
		String setTimeStamp = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).format(new Date());
		xAuthUserListResponse.setTimestamp(setTimeStamp);
		xAuthUserListResponse.setInquiryAuthUserInfo(xAuthUserListInfoObjList);
		xAuthUserListResponse.setStatusCode(code);
		xAuthUserListResponse.setStatusDescription(desc);
		return xAuthUserListResponse;
	}
}
