package com.csme.csmeapi.fin.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.SSLContext;
import javax.validation.Valid;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.cs.ce.core.helper.CustomFormatHelper;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.exec.SQLDao;
import com.cs.core.dao.exec.SQLGenTool;
import com.cs.core.dao.exec.SQLGenToolHelper;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XForgotPasswordRequest;
import com.csme.csmeapi.fin.models.XForgotPasswordResponse;

@SuppressWarnings("deprecation")
public class ForgotpasswordApiService {
	FinUtil finUtil = new FinUtil();
	private Logger logger = LogManager.getLogger("CSMEMobile");
	public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static String secuDs;
	private static String dbType;
	public static Properties prop;
	private String currLoginTime;

	public ForgotpasswordApiService() throws Exception {
		secuDs = DSManager.getSecuDS();
		dbType = DSManager.getDBType(secuDs);
	}
	private boolean loginSuccessExist;

	public XForgotPasswordResponse generateOTP(@Valid XForgotPasswordRequest body, UUID requestId, Long sequence) throws Exception {
		XForgotPasswordResponse xforgotPasswordResponse = new XForgotPasswordResponse();
		try {
			if (checkRealLogin(body.getCorporateId(), body.getUserId())) {
				this.loginSuccessExist = true;
				SQLDao dao = getSuccessSQLsForStmt(body.getCorporateId(), body.getUserId());
				WSDBHelper.executeUpdate(dao);
				String otp = generateOTP(6);
				Long seq = storeOTPInDatabase(body.getCorporateId(), body.getUserId(), body.getMobileNo(), body.getEmail(), otp, "WAITING");
				// Send OTP via WhatsApp
				sendWhatsAppMessage(body.getMobileNo(), otp);
				xforgotPasswordResponse.setOTP(otp);
				xforgotPasswordResponse.setStatusCode("00");
				xforgotPasswordResponse.setStatusDescription("OTP Generate Succesfully");
				setResponse(requestId.toString(), sequence.toString(), otp, "00", "OTP Generate Succesfully", xforgotPasswordResponse);
				updateOTPStatus(seq, "SENT");
			} else {
				setResponse(requestId.toString(), sequence.toString(), null, "10", "Invalid credentials provided", xforgotPasswordResponse);
			}
		} catch (Exception e) {
			logger.error("Error generating OTP: " + e.getMessage());
			setResponse(null, null, null, null, null, xforgotPasswordResponse);
		}
		return xforgotPasswordResponse;
	}
	
	private void setResponse(String requestId, String sequence , String otp, String code , String desc, XForgotPasswordResponse response) {
        response.setMessageId(sequence.toString());
        response.setRequestId(requestId.toString());
		String setTimeStamp = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).format(new Date());
		response.setOTP(otp);
		response.setStatusCode(code);
		response.setStatusDescription(desc);
		response.setTimestamp(setTimeStamp);
    }
	
	public static String readAuthorizationTokenFromXML(String xmlFilePath, String string) {
        try {
            // Load and parse the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFilePath);

            // Get the value of the token element
            NodeList tokenNodes = doc.getElementsByTagName(string);
            if (tokenNodes.getLength() > 0) {
                Element tokenElement = (Element) tokenNodes.item(0);
                return tokenElement.getTextContent().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	public void sendWhatsAppMessage(String recipient, String message) throws Exception {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			String url = readAuthorizationTokenFromXML(FinUtil.intPath+"WAToken.xml","url");
			HttpPost httpPost = new HttpPost(url);

			String token = readAuthorizationTokenFromXML(FinUtil.intPath+"WAToken.xml","token");
			
			httpPost.setHeader("Authorization", "Bearer " + token);
			httpPost.setHeader("Content-Type", "application/json");

			// Set TLS version to v1.2
		    SSLContext sslcontext = SSLContexts.custom()
		            .setProtocol("TLSv1.2")
		            .build();
		    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
		            sslcontext,
		            new String[] { "TLSv1.2" },
		            null,
		            SSLConnectionSocketFactory.getDefaultHostnameVerifier());

		    // Set the custom SSL socket factory
		    CloseableHttpClient customHttpClient = HttpClients.custom()
		            .setSSLSocketFactory(sslsf)
		            .build();

			
			// Request body
			String requestBody = "{\r\n" +
					"  \"messaging_product\": \"whatsapp\",\r\n" +
					"  \"recipient_type\": \"individual\",\r\n" +
					"  \"to\": \"" + recipient + "\",\r\n" +
					"  \"type\": \"template\",\r\n" +
					"  \"template\": {\r\n" +
					"    \"name\": \"otp\",\r\n" +
					"    \"language\": {\r\n" +
					"      \"code\": \"en_US\"\r\n" +
					"    },\r\n" +
					"    \"components\": [\r\n" +
					"      {\r\n" +
					"        \"type\": \"body\",\r\n" +
					"        \"parameters\": [\r\n" +
					"          {\r\n" +
					"            \"type\": \"text\",\r\n" +
					"            \"text\": \"" + message + "\"\r\n" +
					"          }\r\n" +
					"        ]\r\n" +
					"      },\r\n" +
					"      {\r\n" +
					"        \"type\": \"button\",\r\n" +
					"        \"sub_type\": \"url\",\r\n" +
					"        \"index\": \"0\",\r\n" +
					"        \"parameters\": [\r\n" +
					"          {\r\n" +
					"            \"type\": \"text\",\r\n" +
					"            \"text\": \"" + message + "\"\r\n" +
					"          }\r\n" +
					"        ]\r\n" +
					"      }\r\n" +
					"    ]\r\n" +
					"  }\r\n" +
					"}";
			StringEntity requestEntity = new StringEntity(requestBody);
			httpPost.setEntity(requestEntity);

			try (CloseableHttpResponse response = customHttpClient.execute(httpPost)) {
				HttpEntity responseEntity = response.getEntity();
				if (responseEntity != null) {
					String responseString = EntityUtils.toString(responseEntity);
					logger.info("Response message is : " + responseString);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private SQLDao getSuccessSQLsForStmt(String userId, String unitCode) throws Exception {
		SQLDao dao = new SQLDao();
		dao.setDataSource(DSManager.getSecuDS());
		String sUserTable = DSManager.getSchemaedTableName(DSManager.getSecuDS(), "SEC_USER_INFO");
		String sTempTable = DSManager.getSchemaedTableName(DSManager.getSecuDS(), "SEC_USER_INFO_TEMP");
		String dbType = DSManager.getDBType(DSManager.getSecuDS());
		SQLGenTool genSql = SQLGenToolHelper.getUpdateTool(dbType);
		genSql.setTable(sUserTable);
		genSql.addField("I_LOGON_RETRIES", "0", 4);
		genSql.appendClause("WHERE");
		genSql.appendClause("C_USER_ID", 1, userId).appendClause("AND");
		genSql.appendClause("C_UNIT_CODE", 1, unitCode);
		dao.addSqlStatement(genSql.getSqlStatement());
		SQLGenTool genSqlForTemp = genSql.cloneTool();
		genSqlForTemp.setTable(sTempTable);
		dao.addSqlStatement(genSqlForTemp.getSqlStatement());
		String loginSuccTable = DSManager.getSchemaedTableName(DSManager.getSecuDS(), "SEC_LOGIN_SUCCEED_INFO");
		String currDT = CustomFormatHelper.getSystemGMTDate("T_CURR_LOGIN_TIME", "yyyy-mm-dd hh:mm:ss.fff");
		if (this.loginSuccessExist) {
			SQLGenTool loGenSQLTool = SQLGenToolHelper.getUpdateTool(dbType);
			loGenSQLTool.setTable(loginSuccTable);
			String loginTime = CustomFormatHelper.getGMTDate("T_LAST_LOGIN_TIME", this.currLoginTime, "yyyy-mm-dd hh:mm:ss.fff");
			loGenSQLTool.addField("T_LAST_LOGIN_TIME", loginTime, 93);
			loGenSQLTool.addField("T_CURR_LOGIN_TIME", currDT, 93);
			loGenSQLTool.appendClause("WHERE");
			loGenSQLTool.appendClause("C_USER_ID", 1, userId).appendClause("AND");
			loGenSQLTool.appendClause("C_UNIT_CODE", 1, unitCode);
			dao.addSqlStatement(loGenSQLTool.getSqlStatement());
		} else {
			SQLGenTool loGenSQLTool = SQLGenToolHelper.getInsertTool(dbType);
			loGenSQLTool.setTable(loginSuccTable);
			loGenSQLTool.addField("C_UNIT_CODE", userId, 12);
			loGenSQLTool.addField("C_USER_ID", unitCode, 12);
			String strLastLoginTime = CustomFormatHelper.getSystemGMTDate("T_LAST_LOGIN_TIME", "yyyy-mm-dd hh:mm:ss.fff");
			loGenSQLTool.addField("T_LAST_LOGIN_TIME", strLastLoginTime, 93);
			loGenSQLTool.addField("T_CURR_LOGIN_TIME", currDT, 93);
			dao.addSqlStatement(loGenSQLTool.getSqlStatement());
		} 
		return dao;
	}

	private boolean checkRealLogin(String oriBu, String oriUserId) {
		boolean isValid = false;
		try {
			String secuDs = DSManager.getSecuDS();
			String dbType = DSManager.getDBType(secuDs);
			String userInfoTable = DSManager.getSchemaedTableName(secuDs, "SEC_USER_INFO");
			SQLGenTool loginUserSQLToo = new SQLGenTool(4, userInfoTable, dbType);
			loginUserSQLToo.setFields("C_USER_ID,C_UNIT_CODE");
			loginUserSQLToo.appendClause("WHERE");
			loginUserSQLToo.appendClause("C_USER_ID", 12, "=", oriUserId);
			loginUserSQLToo.appendClause("AND");
			loginUserSQLToo.appendClause("C_UNIT_CODE", 12, "=", oriBu);
			SQLRecordSet rs = WSDBHelper.executeQuery(loginUserSQLToo.getSqlStatement(), secuDs);
			if (rs.isSuccess()) {
				isValid = true;
			} else {
				isValid = false;
			} 
		} catch (Exception e) {
			logger.error("Exception occured in checkRealLogin function " + e);
		} 
		return isValid;
	}


	public Long storeOTPInDatabase(String corporateId, String userId, String mobileNumber, String emailId, String otp, String status) {
        try {
            SQLDao sqlDao = new SQLDao();
            sqlDao.setDataSource(secuDs);
            Long seq = getSequence();
            if (seq == null)
                throw new Exception("Not able to generate sequence ");
            String reportDate = df.format(new Date());
            SQLGenTool assignSQLTool1 = SQLGenToolHelper.getInsertTool(dbType);
            String table = DSManager.getSchemaedTableName(secuDs, "OTP_TABLE");
            assignSQLTool1.setTable(table);
            assignSQLTool1.addField("OTP_ID", seq.toString(), -5);
            assignSQLTool1.addField("CorporateId", corporateId, 12);
            assignSQLTool1.addField("UserId", userId, 12);
            assignSQLTool1.addField("MobileNumber", mobileNumber, 12);
            assignSQLTool1.addField("EmailId", emailId, 12);
            assignSQLTool1.addField("OTP", otp, 12); // Assuming OTP is the column for storing OTP
            assignSQLTool1.addField("Status", status, 12);
            assignSQLTool1.addField("CreationTime", reportDate, 93);
            sqlDao.addSqlStatement(assignSQLTool1.getSqlStatement());
            int update = WSDBHelper.executeUpdate(sqlDao);
            logger.info("update  in the database: " + update);
            return seq;
        } catch (Exception e) {
            logger.error("Error while storing OTP in the database: " + e.getMessage());
            return null;
        }
    }

	private void updateOTPStatus(Long otpId, String status) {
		try {
			SQLDao dao = new SQLDao();
			dao.setDataSource(secuDs);
			SQLGenTool updateTool = SQLGenToolHelper.getUpdateTool(dbType);
			String table = DSManager.getSchemaedTableName(secuDs, "OTP_TABLE");
			updateTool.setTable(table);
			updateTool.addField("Status", status, 12);
			updateTool.appendClause("WHERE");
			updateTool.appendClause("OTP_ID", 1, otpId.toString());
			dao.addSqlStatement(updateTool.getSqlStatement());
			WSDBHelper.executeUpdate(dao);
			logger.info("OTP status updated to '" + status + "' for OTP_ID: " + otpId);
		} catch (Exception e) {
			logger.error("Error updating OTP status: " + e.getMessage());
		}
	}

	// Method to generate a sequence number
	private Long getSequence() {
		// Implement logic to get a sequence number from the database
		// For example:
		// return sequenceGenerator.getNextSequence();
		return new Random().nextLong(); // For demonstration purposes
	}
	
	 public static void main(String[] args) {
	        try {
	            // Instantiate ForgotpasswordApiService
	            String recipient = "ilyas";
	            String subject = "Your OTP for password reset";
	            String body = "Your OTP is: ";
	            sendEmail(recipient, subject, body);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	public static String generateOTP(int length) {
		String numbers = "0123456789";
		Random random = new Random();
		StringBuilder otp = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			otp.append(numbers.charAt(random.nextInt(numbers.length())));
		}
		return otp.toString();
	}
	
	public static void sendEmail(String recipient, String subject, String body) throws Exception {
	    // Assuming you have SMTP server details
	    String host = "mail.chinaystems.com";
	    String username = "CSME/ilyas";
	    String password = "Ashraf143";

	    // Get system properties
	    Properties properties = System.getProperties();

	    // Setup mail server
	    properties.setProperty("mail.smtp.host", host);
	    properties.setProperty("mail.smtp.auth", "true");
	    properties.setProperty("mail.smtp.starttls.enable", "true");
	    properties.setProperty("mail.smtp.port", "587");

	    // Get the default Session object
	    Session session = Session.getInstance(properties, new Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(username, password);
	        }
	    });

	    try {
	        // Create a default MimeMessage object
	        MimeMessage message = new MimeMessage(session);

	        // Set From: header field
	        message.setFrom(new InternetAddress(username));

	        // Set To: header field
	        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

	        // Set Subject: header field
	        message.setSubject(subject);

	        // Now set the actual message
	        message.setText(body);

	        // Send message
	        Transport.send(message);
	        System.out.println("Email sent successfully to " + recipient);
	    } catch (MessagingException mex) {
	        mex.printStackTrace();
	    }
	}

}
