/**
 * Provides services for inquiring transactions.
 * This class handles querying transaction data based on request parameters.
 */
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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cs.base.xml.XMLManager;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.AttachmentInfoListObj;
import com.csme.csmeapi.fin.models.InquiryTrxInfoListObj;
import com.csme.csmeapi.fin.models.XInquireTransactionRequest;
import com.csme.csmeapi.fin.models.XInquireTransactionResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * Provides services for inquiring transactions.
 * This class handles querying transaction data based on request parameters.
 */
public class InquireTransactionApiService {

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
	public InquireTransactionApiService() {
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
	private Map<String, String> getValuesMap(@Valid XInquireTransactionRequest body) {
		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put("mainref", body.getMainRef());
		valuesMap.put("unitcode", body.getCorporateId());
		valuesMap.put("eventtimes", body.getEventTimes());
		valuesMap.put("modulename", body.getModule());
		valuesMap.put("userid", body.getUserId());
		valuesMap.put("module", body.getModule());
		return valuesMap;
	}

	/**
	 * Processes an inquiry transaction request.
	 * Retrieves transaction information based on the request parameters.
	 * 
	 * @param body The inquiry transaction request object.
	 * @param requestId The unique identifier for the request.
	 * @param sequence The sequence number of the request.
	 * @return The inquiry transaction response object.
	 * @throws Exception If an error occurs during processing.
	 */
	public XInquireTransactionResponse getInquiryTransaction(@Valid XInquireTransactionRequest body, UUID requestId, Long sequence) {
		XInquireTransactionResponse xInquireTransactionResponse = new XInquireTransactionResponse();
		try {
			Lang = FinUtil.getLang(body.getCorporateId(), body.getUserId());
			logger.info("Inquiry Transaction Language is :"+Lang);
			String module = body.getModule();
			String filePath = FinUtil.intPath+module+"Trx.xml";
			logger.info("Inquiry Transaction File path is :"+filePath);
			Document inquiryXml = XMLManager.xmlFileToDom(filePath);
			Element eventTableInqEle = (Element)XMLManager.findNode(inquiryXml, body.getFunctionId());
			
			List<AttachmentInfoListObj> attachmentInfo = new ArrayList<AttachmentInfoListObj>();
			//Attachment
			ArrayNode attachmentJsonArr = objectMapper.createArrayNode();
			Element rootInqEle = inquiryXml.getDocumentElement();
			String attachmentQuery = FinUtil.getValueByTagName(rootInqEle, "ATTACHMENT_QUERY");
			StringSubstitutor attachmentsubstitutor = new StringSubstitutor(getValuesMap(body));
			String attachementTableQuery = attachmentsubstitutor.replace(attachmentQuery);
			logger.info("Inquiry Transaction Final Query is :"+attachementTableQuery);
			SQLRecordSet attachmentRdSet = WSDBHelper.executeQuery(attachementTableQuery, this.secuDs);
			if (attachmentRdSet != null && attachmentRdSet.getRecordCount() > 0) {
				for (int a = 0; a < attachmentRdSet.getRecordCount() ; a++) {
					AttachmentInfoListObj attachmentInfoListObj = new AttachmentInfoListObj();
					SQLRecord attachmentSqlRecord = attachmentRdSet.getSQLRecord(a);
					JSONObject childValueInfoJson = new JSONObject();
					String fileName = attachmentSqlRecord.getValue("C_IMG_DOC_DESC");
					String filetype = attachmentSqlRecord.getValue("C_IMG_FILE_TYPE");
					String fileSize = attachmentSqlRecord.getValue("I_IMG_FILE_SIZE"); 
					String attachmentRef = attachmentSqlRecord.getValue("C_IMG_INDX");
					childValueInfoJson.put("FileName", fileName);
					childValueInfoJson.put("FileType", filetype);
					childValueInfoJson.put("FileSize", fileSize); 
					childValueInfoJson.put("AttachmentRef", attachmentRef);
					JsonNode tabInfoNode = objectMapper.readTree(childValueInfoJson.toString());
					attachmentJsonArr.add(tabInfoNode);
					attachmentInfoListObj.setAttachmentInfoList(attachmentJsonArr);
					attachmentInfo.add(attachmentInfoListObj);
				}
			}
			//
			
			if(eventTableInqEle != null) {
				String SqlQuery = FinUtil.getValueByTagName(eventTableInqEle, "Query");
				logger.info("Inquiry Transaction Query is :"+SqlQuery);
				StringSubstitutor substitutor = new StringSubstitutor(getValuesMap(body));
				String eventTableQuery = substitutor.replace(SqlQuery);
				logger.info("Inquiry Transaction Final Query is :"+eventTableQuery);
				SQLRecordSet eventTableRdSet = WSDBHelper.executeQuery(eventTableQuery, this.secuDs);
				List<InquiryTrxInfoListObj> inquiryTrxInfoListObjs = new ArrayList<InquiryTrxInfoListObj>();
				logger.info("Inquiry Transaction Total Result count is  :"+eventTableRdSet.getRecordCount());
				if (eventTableRdSet != null && eventTableRdSet.getRecordCount() > 0) {
					for (int a = 0; a < eventTableRdSet.getRecordCount() ; a++) {
						SQLRecord eventTableSqlRecord = eventTableRdSet.getSQLRecord(a);
						NodeList eventNdList = eventTableInqEle.getChildNodes();
						for (int b = 0; b < eventNdList.getLength(); b++) {
							Node eventChNd = eventNdList.item(b);
							if(eventChNd.getNodeType() == 1 && eventChNd.getNodeName() != "Query") {
								NodeList eventChNdList = eventChNd.getChildNodes();
								for (int c = 0; c < eventChNdList.getLength(); c++) {
									Node eventChNdListNd = eventChNdList.item(c);
									NodeList eventChNdListNdList = eventChNdListNd.getChildNodes();
									if(eventChNdListNd.getNodeType() == 1) {
										NamedNodeMap eveTbNodeAttrList = eventChNdListNd.getAttributes();
										Node labelNode = eveTbNodeAttrList.getNamedItem("label");
										String tabLabelNm = labelNode.getNodeValue();
										InquiryTrxInfoListObj inquiryTrxInfoListObj = new InquiryTrxInfoListObj();
										ArrayNode jsonArray = objectMapper.createArrayNode();
										for (int d = 0; d < eventChNdListNdList.getLength(); d++) {
											Node eventChNdListNdNd = eventChNdListNdList.item(d);
											if(eventChNdListNdNd.getNodeType() == 1) {
												String fieldName = eventChNdListNdNd.getNodeName();
												NamedNodeMap trxCmNodeAttr = eventChNdListNdNd.getAttributes();
												JSONObject childValueInfoJson = new JSONObject();
												for (int t = 0; t < trxCmNodeAttr.getLength(); t++) {
													Node attribute = trxCmNodeAttr.item(t);
													String attributeName = attribute.getNodeName();
													String attributeValue = attribute.getNodeValue();
													JSONObject styleJson = new JSONObject();
													if(attributeName.equalsIgnoreCase("value")) {
														attributeValue = eventTableSqlRecord.getValue(attributeValue);
														childValueInfoJson.put(attributeName, attributeValue);
													} else if(attributeName.equalsIgnoreCase("style")) {
														String[] styleValue = attribute.getNodeValue().split(";");
														for (int l = 0; l < styleValue.length; l++) {
															String[] newStyleValue = styleValue[l].split(":");
															styleJson.put(newStyleValue[0], newStyleValue[1]);
														}
														childValueInfoJson.put(attributeName, styleJson);
													} else if(attributeName.equalsIgnoreCase("label")) {
														String fieldDesc = FinUtil.getDBFieldDesc(fieldName, this.Lang);
														if(fieldDesc != null && fieldDesc != "") {
															childValueInfoJson.put(attributeName, fieldDesc);
														} else {
															childValueInfoJson.put(attributeName, attributeValue);
														}
													} else {
														childValueInfoJson.put(attributeName, attributeValue);
													}
												}
												JsonNode tabInfoNode = objectMapper.readTree(childValueInfoJson.toString());
												jsonArray.add(tabInfoNode);
											}
										}

										inquiryTrxInfoListObj.setTitle(tabLabelNm);
										inquiryTrxInfoListObj.setInquiryTrxInfoList(jsonArray);
										inquiryTrxInfoListObjs.add(inquiryTrxInfoListObj);
									}
								}
							}
						}
						responseSet(requestId, sequence, "00", "Sucess",  inquiryTrxInfoListObjs,attachmentInfo, xInquireTransactionResponse);
					}
				} else {
					responseSet(requestId, sequence, "08", "No Record exist in Inbox table",  inquiryTrxInfoListObjs, attachmentInfo, xInquireTransactionResponse);
				}
			} else {
				responseSet(requestId, sequence, "09", "Function id is not exist in config file",  new ArrayList<InquiryTrxInfoListObj>(), new ArrayList<AttachmentInfoListObj>() ,xInquireTransactionResponse);
			} 

		}catch (Exception e) {
			logger.error("Error processing inquiry transaction: " + e.getMessage(), e);
			responseSet(requestId, sequence, "99", "General Exception",  new ArrayList<InquiryTrxInfoListObj>(), new ArrayList<AttachmentInfoListObj>(), xInquireTransactionResponse);
		}
		logger.info("Inquiry Transaction Final Response message  is  :"+xInquireTransactionResponse.toString());
		return xInquireTransactionResponse;
	}

	/**
	 * Sets the response attributes for an inquiry transaction.
	 * Constructs the response object with the specified attributes and returns it.
	 * 
	 * @param requestId The unique identifier for the request.
	 * @param sequence The sequence number of the request.
	 * @param code The status code indicating the result of the transaction.
	 * @param desc The status description providing additional information about the result.
	 * @param inquiryTrxInfoListObjs The list of inquiry transaction information objects.
	 * @param attachmentInfo 
	 * @param xInquireTransactionResponse The response object to be populated.
	 * @return The populated inquiry transaction response object.
	 */
	private XInquireTransactionResponse responseSet(UUID requestId, Long sequence,String code, String desc,
			List<InquiryTrxInfoListObj> inquiryTrxInfoListObjs,List<AttachmentInfoListObj> attachmentInfo, XInquireTransactionResponse xInquireTransactionResponse) {
		xInquireTransactionResponse.setRequestId(requestId.toString());
		xInquireTransactionResponse.setMessageId(sequence.toString());
		String setTimeStamp = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).format(new Date());
		xInquireTransactionResponse.setTimestamp(setTimeStamp);
		xInquireTransactionResponse.setInquiryTrxInfo(inquiryTrxInfoListObjs);
		xInquireTransactionResponse.setAttachmentInfo(attachmentInfo);
		xInquireTransactionResponse.setStatusCode(code);
		xInquireTransactionResponse.setStatusDescription(desc);
		return xInquireTransactionResponse;
	}

}


