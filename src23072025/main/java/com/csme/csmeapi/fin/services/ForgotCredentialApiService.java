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

import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.exec.SQLDao;
import com.cs.core.dao.exec.SQLGenTool;
import com.cs.core.dao.exec.SQLGenToolHelper;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XForgotCredentialRequest;
import com.csme.csmeapi.fin.models.XForgotCredentialResponse;

public class ForgotCredentialApiService {
	FinUtil finUtil = new FinUtil();
	private static Logger logger = LogManager.getLogger("CSMEMobile");
	public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static String secuDs;
	private static String dbType;
	public static Properties prop;

	static {
        try {
        	secuDs = FinUtil.SECDS;
        	dbType = FinUtil.SECDBTYPE;
        } catch (Exception e) {
            logger.error("Error initializing ForgotCredentialApiService: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

	public XForgotCredentialResponse generateOTP(@Valid XForgotCredentialRequest body, UUID requestId, Long sequence) throws Exception {
		XForgotCredentialResponse xForgotCredentialResponse = new XForgotCredentialResponse();
		try {
			SQLRecordSet rs = checkRealLogin(body.getEmail(), body.getMobileNo());
			if (rs != null && rs.getRecordCount() > 0) {
				String otp = generateOTP(6);
				Long seq = storeOTPInDatabase(rs,body.getMobileNo(), body.getEmail(), otp, "WAITING");
				// Send OTP via WhatsApp
				sendWhatsAppMessage(body.getMobileNo(), otp);
				updateOTPStatus(seq, "SENT");
				setResponse(requestId.toString(), sequence.toString(), otp, "00", "OTP Generate Succesfully", xForgotCredentialResponse);
			} else {
				setResponse(requestId.toString(), sequence.toString(), null, "10", "Invalid credentials provided", xForgotCredentialResponse);
			}
		} catch (Exception e) {
			logger.error("Error generating OTP: " + e.getMessage());
            setResponse(null, null, null, null, null, xForgotCredentialResponse);
		}
		return xForgotCredentialResponse;
	}
	
	private void setResponse(String requestId, String sequence , String otp, String code , String desc, XForgotCredentialResponse response) {
        response.setMessageId(sequence.toString());
        response.setRequestId(requestId.toString());
		String setTimeStamp = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).format(new Date());
		response.setOTP(otp);
		response.setStatusCode(code);
		response.setStatusDescription(desc);
		response.setTimestamp(setTimeStamp);
    }
	
	public static String readAuthorizationTokenFromXML(String xmlFilePath, String tagName) {
        try {
            // Load and parse the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFilePath);

            // Get the value of the token element
            NodeList tokenNodes = doc.getElementsByTagName(tagName);
            if (tokenNodes.getLength() > 0) {
                Element tokenElement = (Element) tokenNodes.item(0);
                return tokenElement.getTextContent();
            }
        } catch (Exception e) {
        	logger.error("Error reading authorization token from XML: " + e.getMessage());
        }
        return null;
    }

	public void sendWhatsAppMessage(String recipient, String message) throws Exception {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			String url = readAuthorizationTokenFromXML(FinUtil.intPath+"WAToken.xml","url");
			HttpPost httpPost = new HttpPost(url);
			String token = readAuthorizationTokenFromXML(FinUtil.intPath+"WAToken.xml","token");
			// Request headers
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
					logger.info("Response: " + responseString);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private SQLRecordSet checkRealLogin(String oriBu, String oriUserId) {
		SQLRecordSet rs = null;
		try {
			String secuDs = DSManager.getSecuDS();
			String dbType = DSManager.getDBType(secuDs);
			String userInfoTable = DSManager.getSchemaedTableName(secuDs, "SEC_USER_INFO");
			SQLGenTool loginUserSQLToo = new SQLGenTool(4, userInfoTable, dbType);
			loginUserSQLToo.setFields("C_USER_ID,C_UNIT_CODE");
			loginUserSQLToo.appendClause("WHERE");
			loginUserSQLToo.appendClause("EMAIL_ID", 12, "=", oriBu);
			loginUserSQLToo.appendClause("AND");
			loginUserSQLToo.appendClause("MOBILE", 12, "=", oriUserId);
			rs = WSDBHelper.executeQuery(loginUserSQLToo.getSqlStatement(), secuDs);
		} catch (Exception e) {
			 logger.error("Exception occurred in checkRealLogin function: " + e.getMessage());
		} 
		return rs;
	}

	public Long storeOTPInDatabase(SQLRecordSet rs, String mobileNumber, String emailId, String otp, String status) {
        try {
        	
        	SQLRecord loginRcd = rs.getFirstRecord();
			String corporateId = loginRcd.getValue("C_UNIT_CODE");
			String userId = loginRcd.getValue("C_USER_ID");
        	
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
            logger.error("updating record OTP in the database: " + update);
            return seq;
        } catch (Exception e) {
        	logger.error("Error storing OTP in the database: " + e.getMessage());
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

	private Long getSequence() {
		return new Random().nextLong(); 
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
	
	public void sendEmail(String recipientEmail, String subject, String body) throws MessagingException {
        // Email configuration
        String host = "smtp.example.com";
        String username = "your_email@example.com";
        String password = "your_email_password";

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Create and send email message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }

}
