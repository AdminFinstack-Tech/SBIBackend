/**
 * Provides services for inquiring transactions.
 * This class handles querying transaction data based on request parameters.
 */
package com.csme.csmeapi.fin.services;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import javax.validation.Valid;

import org.apache.commons.text.StringSubstitutor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cs.base.xml.XMLManager;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.ce.core.helper.ZipHelper;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XAttachementInfoObj;
import com.csme.csmeapi.fin.models.XAttachmentRequest;
import com.csme.csmeapi.fin.models.XAttachmentResponse;
import com.csme.csmeapi.fin.util.CSMEBase64;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * Provides services for inquiring transactions.
 * This class handles querying transaction data based on request parameters.
 */
public class InquireAttachmentService {

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
	public InquireAttachmentService() {
		try {
			this.secuDs = FinUtil.SECDS;
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	private Map<String, String> getValuesMap(@Valid XAttachmentRequest body) {
		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put("attachementRef", body.getAttachmentRef());
		return valuesMap;
	}

	public XAttachmentResponse getInquiryAttachment(@Valid XAttachmentRequest body, UUID requestId, Long sequence) {
		XAttachmentResponse xAttachmentResponse = new XAttachmentResponse();
		try {
			String filePath = FinUtil.intPath+"CommonINQ.xml";
			logger.info("Inquiry Transaction File path is :"+filePath);
			Document inquiryXml = XMLManager.xmlFileToDom(filePath);
			//Attachment
			ArrayNode attachmentJsonArr = objectMapper.createArrayNode();
			Element rootInqEle = inquiryXml.getDocumentElement();
			String attachmentQuery = FinUtil.getValueByTagName(rootInqEle, "ATTACHMENT_QUERY");
			StringSubstitutor attachmentsubstitutor = new StringSubstitutor(getValuesMap(body));
			String attachementTableQuery = attachmentsubstitutor.replace(attachmentQuery);
			logger.info("Inquiry Transaction Final Query is :"+attachementTableQuery);
			SQLRecordSet attachmentRdSet = WSDBHelper.executeQuery(attachementTableQuery, this.secuDs);
			List<XAttachementInfoObj> xAttachementInfoObjList = new ArrayList<XAttachementInfoObj>();
			if (attachmentRdSet != null && attachmentRdSet.getRecordCount() > 0) {
					for (int a = 0; a < attachmentRdSet.getRecordCount() ; a++) {
						XAttachementInfoObj xAttachementInfoObj = new XAttachementInfoObj();
						SQLRecord attachmentSqlRecord = attachmentRdSet.getSQLRecord(a);
						JSONObject childValueInfoJson = new JSONObject();
						String fileName = attachmentSqlRecord.getValue("C_IMG_DOC_DESC");
						String filetype = attachmentSqlRecord.getValue("C_IMG_FILE_TYPE");
						String fileSize = attachmentSqlRecord.getValue("I_IMG_FILE_SIZE"); 
						String attachmentRef = attachmentSqlRecord.getValue("C_IMG_INDX");
						String fileContent = attachmentSqlRecord.getValue("B_IMG_CONTENT");
						
						byte[] b = null;
						Object newAttachmentContent = ZipHelper.decompressByBase64(fileContent);
				        if (newAttachmentContent != null && newAttachmentContent.getClass().toString().indexOf("String") != -1) {
				        	fileContent = (String)newAttachmentContent;
				          b = fileContent.getBytes("ISO-8859-1");
				        } else {
				          b = (byte[])newAttachmentContent;
				        } 
						
						String encodedBytes = ZipHelper.encode(b);
						childValueInfoJson.put("FileName", fileName);
						childValueInfoJson.put("FileType", filetype);
						childValueInfoJson.put("FileSize", fileSize); 
						childValueInfoJson.put("FileContent", encodedBytes); 
						childValueInfoJson.put("AttachmentRef", attachmentRef);
						JsonNode tabInfoNode = objectMapper.readTree(childValueInfoJson.toString());
						attachmentJsonArr.add(tabInfoNode);
						xAttachementInfoObj.setXattachementInfoListObj(attachmentJsonArr);
						xAttachementInfoObjList.add(xAttachementInfoObj);
					}
					responseSet(requestId, sequence, "00", "Sucess",  xAttachementInfoObjList, xAttachmentResponse);
			} else {
				responseSet(requestId, sequence, "08", "No Record exist",  new ArrayList<XAttachementInfoObj>(), xAttachmentResponse);
			}
			//
			
		}catch (Exception e) {
			logger.error("Error processing inquiry transaction: " + e.getMessage(), e);
			responseSet(requestId, sequence, "99", "General Exception",  new ArrayList<XAttachementInfoObj>(), xAttachmentResponse);
		}
		return xAttachmentResponse;
	}

	private XAttachmentResponse responseSet(UUID requestId, Long sequence,String code, String desc,
			List<XAttachementInfoObj> attachementInfo,XAttachmentResponse xAttachmentResponse) {
		xAttachmentResponse.setRequestId(requestId.toString());
		xAttachmentResponse.setMessageId(sequence.toString());
		String setTimeStamp = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).format(new Date());
		xAttachmentResponse.setTimestamp(setTimeStamp);
		xAttachmentResponse.setAttachementInfo(attachementInfo);
		xAttachmentResponse.setStatusCode(code);
		xAttachmentResponse.setStatusDescription(desc);
		return xAttachmentResponse;
	}
	
	public static String decompressZIPBase64(String str)
			throws Exception
	{
		for (int i = 0; i < str.length(); i++) {
			if (str.substring(i, i + 1).equalsIgnoreCase(">")) {
				return str;
			}
		}
		byte[] tempByte = CSMEBase64.decode(str);
		ByteArrayInputStream bos = new ByteArrayInputStream(tempByte);
		ObjectInputStream objIn = new ObjectInputStream(new GZIPInputStream(bos));
		Object obj = objIn.readObject();
		return (String)obj;
	}

}


