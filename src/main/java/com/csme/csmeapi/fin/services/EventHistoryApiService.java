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
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cs.base.xml.XMLManager;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XInquireEventRequest;
import com.csme.csmeapi.fin.models.XInquireEventRequestListObj;
import com.csme.csmeapi.fin.models.XInquireEventResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EventHistoryApiService {
	FinUtil finUtil = new FinUtil();
	
	Logger logger = LogManager.getLogger("CSMEMobile");
	ObjectMapper objectMapper = new ObjectMapper();
	String secuDs = null;

	public EventHistoryApiService() {
	    try {
	        this.secuDs = FinUtil.SECDS;
	    } catch (Exception e) {
	        logger.error("Error initializing security data source.", e);
	    }
	}

	public XInquireEventResponse getEventInquiryList(@Valid XInquireEventRequest body, UUID requestId, Long sequence) {
		XInquireEventResponse xInquireEventResponse = new XInquireEventResponse();
		Long pageNo = body.getPage();
		Long pageSize = body.getPerPage();
		try {
			String filePath = FinUtil.intPath+body.getModule()+"Event.xml";
			Document inquiryXml = XMLManager.xmlFileToDom(filePath);
			logger.info("Convert Inquiry Dom into String is : " + XMLManager.convertToString(inquiryXml));
			StringBuilder iboxQueryBuilder = getEventQuery(body, inquiryXml);
			String sqlQuery = iboxQueryBuilder.toString();
			this.logger.info("Excecute SqlQuery is " + sqlQuery);
			SQLRecordSet rs = WSDBHelper.executeQuery(sqlQuery, this.secuDs);
			List<XInquireEventRequestListObj> info = new ArrayList<XInquireEventRequestListObj>();
			if (rs.getRecordCount() > 0) {
				this.logger.info("Retervie value from database count is :" + rs.getRecordCount());
				for (int i = 0, len = rs.getRecordCount(); i < len; i++) {
					XInquireEventRequestListObj xInquireEventRequestListObj = new XInquireEventRequestListObj();
					SQLRecord record = rs.getSQLRecord(i);
					JSONObject commonJson = buildCommonInfoList(record, inquiryXml);
					JsonNode commonJsonNode = objectMapper.readTree(commonJson.toString());
					xInquireEventRequestListObj.setCommonInfoList(commonJsonNode);
					info.add(xInquireEventRequestListObj);
				}
				xInquireEventResponse = setResponseAttributes(requestId.toString(), sequence.toString(), 
					    Long.valueOf(pageNo.longValue() + 1L), Long.valueOf(pageNo.longValue() - 1L), 
					    Long.valueOf(pageNo.longValue()), Long.valueOf(pageSize.longValue()), 
					    Long.valueOf(rs.getTotalDBRecords()), info, "00", "Success");
			} else {
				xInquireEventResponse = setResponseAttributes(requestId.toString(), sequence.toString(), 
					    Long.valueOf(pageNo.longValue() + 1L), Long.valueOf(pageNo.longValue() - 1L), 
					    Long.valueOf(pageNo.longValue()), Long.valueOf(pageSize.longValue()), 
					    Long.valueOf(0), info, "09", "No Record exist Inquiry Event Transaction");
				
			} 
		} catch (Exception e) {
			xInquireEventResponse = setResponseAttributes(requestId.toString(), sequence.toString(), 
				    Long.valueOf(pageNo.longValue() + 1L), Long.valueOf(pageNo.longValue() - 1L), 
				    Long.valueOf(pageNo.longValue()), Long.valueOf(pageSize.longValue()), 
				    Long.valueOf(0), null, "99", "General Exception has occurred EventInquiryList");
			e.printStackTrace();
		} 
		return xInquireEventResponse;
	}

	public XInquireEventResponse setResponseAttributes(String requestId, String messageId, Long hasNext, Long hasPrev,
			Long page, Long perPage, Long total, List<XInquireEventRequestListObj> info,
			String statusCode, String statusDescription) {
		XInquireEventResponse xInquireEventResponse = new XInquireEventResponse();
		xInquireEventResponse.setRequestId(requestId.toString());
		xInquireEventResponse.setMessageId(messageId);
		String setTimeStamp = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).format(new Date());
		xInquireEventResponse.setTimestamp(setTimeStamp);
		xInquireEventResponse.setHasNext(hasNext);
		xInquireEventResponse.setHasPrev(hasPrev);
		xInquireEventResponse.setMessageId(requestId.toString());
		xInquireEventResponse.setPage(page);
		xInquireEventResponse.setPerPage(perPage);
		xInquireEventResponse.setTotal(total);
		xInquireEventResponse.setInfo(info);
		xInquireEventResponse.setStatusCode(statusCode);
		xInquireEventResponse.setStatusDescription(statusDescription);
		return xInquireEventResponse;
	}

	private void processNode(JSONObject json, Node childNode, SQLRecord sqlRecord) throws JSONException {
		String fieldName = childNode.getNodeName();
		NamedNodeMap childCmNodeAttr = childNode.getAttributes();
		JSONObject valueInJson = new JSONObject();
		JSONObject styleInJson = new JSONObject();

		for (int k = 0; k < childCmNodeAttr.getLength(); k++) {
			Node attribute = childCmNodeAttr.item(k);
			String attributeName = attribute.getNodeName();
			String attributeValue = attribute.getNodeValue();

			if (attributeName.equalsIgnoreCase("value")) {
				attributeValue = sqlRecord.getValue(attributeValue);
				valueInJson.put(attributeName, attributeValue);
			} else if (attributeName.equalsIgnoreCase("style")) {
				processStyleAttribute(styleInJson, attribute);
			} else {
				valueInJson.put(attributeName, attributeValue);
			}
		}
		json.put(fieldName, valueInJson);
	}

	private void processStyleAttribute(JSONObject styleInJson, Node attribute) throws JSONException {
		String[] styleValue = attribute.getNodeValue().split(";");
		for (String value : styleValue) {
			String[] newStyleValue = value.split(":");
			styleInJson.put(newStyleValue[0], newStyleValue[1]);
		}
	}

	private JSONObject buildCommonInfoList(SQLRecord sqlRecord, Document inquiryXml) throws JSONException {
		JSONObject commonJson = new JSONObject();
		NodeList commonNdList = inquiryXml.getElementsByTagName("CommonInquiry");
		Node chCmNd = commonNdList.item(0);
		Node[] chCmNdList = getChildNodes(chCmNd);

		if (chCmNdList.length > 0) {
			for (Node childNodeCm : chCmNdList) {
				if (childNodeCm.getNodeType() == 1) {
					processNode(commonJson, childNodeCm, sqlRecord);
				}
			}
		}
		return commonJson;
	}

	private Node[] getChildNodes(Node node) {
		NodeList nodeList = node.getChildNodes();
		List<Node> childNodes = new ArrayList<>();

		for (int j = 0; j < nodeList.getLength(); j++) {
			Node childNode = nodeList.item(j);
			if (childNode.getNodeType() == 1) {
				childNodes.add(childNode);
			}
		}
		return childNodes.toArray(new Node[0]);
	}



	private StringBuilder getEventQuery(@Valid XInquireEventRequest body, Document inquiryXml) throws Exception {
		StringBuilder iboxQueryBuilder = new StringBuilder();
		iboxQueryBuilder.append("SELECT * FROM ( SELECT A.*, rownum r FROM ( "); 
		Long pageNo = body.getPage();
		Long pageSize = body.getPerPage();
		Element rootElement = inquiryXml.getDocumentElement();
		String inquirySqlQuery = getValueByTagName(rootElement, "EventSqlQuery");

		Map<String, String> valuesMap = getValuesMap(body);
		StringSubstitutor substitutor = new StringSubstitutor(valuesMap);
		String result = substitutor.replace(inquirySqlQuery);
		logger.info("result info is :" + result);
		iboxQueryBuilder.append(result);
		iboxQueryBuilder.append(
				"  ) A WHERE rownum < ((" + pageNo + " * " + pageSize + ") + 1 ) ) WHERE r >= ((("
						+ pageNo + "-1) * " + pageSize + ") + 1)");
		return iboxQueryBuilder;
	}

	private Map<String, String> getValuesMap(@Valid XInquireEventRequest body) {
		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put("mainref", body.getMainRef());
		valuesMap.put("unitcode", body.getCorporateId());
		valuesMap.put("modulename", body.getModule());
		return valuesMap;
	}

	private String getValueByTagName(Element element, String tagName) {
		NodeList nodeList = element.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			Node node = nodeList.item(0);
			return node.getTextContent();
		}
		return null;
	}
}
