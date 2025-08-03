package com.csme.csmeapi.fin.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;

import com.cs.base.xml.XMLManager;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XBankLimitInquireRequest;
import com.csme.csmeapi.fin.models.XBankLimitInquireResponse;
import com.csme.csmeapi.fin.models.XBankLimitResponseInfoObj;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BankLimitInquireResponseServiceApi {
	FinUtil finUtil = new FinUtil();
	
	private static Logger logger = LogManager.getLogger("CSMEMobile");
	ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) {
		XBankLimitInquireRequest request = new XBankLimitInquireRequest(/* fill in with necessary parameters */);
		UUID requestId = UUID.randomUUID();
		Long sequence = 123456L;
		BankLimitInquireResponseServiceApi service = new BankLimitInquireResponseServiceApi();
		try {
			XBankLimitInquireResponse response = service.getLimitInfo(request, requestId, sequence);
			logger.info("Response:");
			logger.info("Message ID: " + response.getMessageId());
			logger.info("Request ID: " + response.getRequestId());
			logger.info("Status Code: " + response.getStatusCode());
			logger.info("Status Description: " + response.getStatusDescription());
			logger.info("Timestamp: " + response.getTimestamp());
			logger.info("Bank Limit Response Info:"+response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public XBankLimitInquireResponse getLimitInfo(@Valid XBankLimitInquireRequest body, UUID requestId, Long sequence) throws Exception {
		try {
			XBankLimitInquireResponse XBankLimitInquireResponse= new XBankLimitInquireResponse();
			XBankLimitInquireResponse.setMessageId(sequence.toString());
			XBankLimitInquireResponse.setRequestId(requestId.toString());
			XBankLimitInquireResponse.setStatusCode("00");
			XBankLimitInquireResponse.setStatusDescription("Success");
			String setTimeStamp = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).format(new Date());
			XBankLimitInquireResponse.setTimestamp(setTimeStamp);
			List<XBankLimitResponseInfoObj> bankLimitResponseInfo = new ArrayList<XBankLimitResponseInfoObj>();
			XBankLimitResponseInfoObj XBankLimitResponseInfoObj = new XBankLimitResponseInfoObj();
			Document responseDom = XMLManager.xmlFileToDom(FinUtil.intPath+"LimitInq.xml");
			String jsonString = convertXmlToJson(XMLManager.convertToString(responseDom));
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONObject eeObject = jsonObject.getJSONObject("EE");
			JSONObject limitsObject = eeObject.getJSONObject("LIMITS");
			JSONArray limitArray = limitsObject.getJSONArray("LIMIT");
			JsonNode limitJsonNode = mapper.readTree(limitArray.toString());
			XBankLimitResponseInfoObj.setLimitListInfo(limitJsonNode);
			bankLimitResponseInfo.add(XBankLimitResponseInfoObj);
			XBankLimitInquireResponse.setBankLimitResponseInfo(bankLimitResponseInfo);
			logger.info("getLimitInfo method Bank limit inquiry processed successfully.");
			return XBankLimitInquireResponse;
		} catch (JsonMappingException e) {
			logger.info("getLimitInfo method Exception has occured in JsonMappingException is :"+e.getMessage());
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			logger.info("getLimitInfo method Exception has occured in JsonProcessingException is :"+e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			logger.info("getLimitInfo method Exception has occured in JSONException is :"+e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.info("getLimitInfo method , Exception has occured in Exception is :"+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public static String convertXmlToJson(String xmlString) throws JSONException {
		JSONObject jsonObject = XML.toJSONObject(xmlString);
		return jsonObject.toString(4);
	}

}
