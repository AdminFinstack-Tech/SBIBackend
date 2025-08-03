package com.csme.csmeapi.mobile.util;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.base.utility.ASPathConst;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class CSMEJWTUtil {
	private static final int TOKEN_EXP_DURATION=20000;
	private RSAPrivateKey privKey;
	private RSAPublicKey pubKey;
	private SignatureAlgorithm signatureAlgorithm=SignatureAlgorithm.RS256;
	private String PRIVATE_KEY_FILE_NAME;
	private String PUBLIC_KEY_FILE_NAME;
	private static final Logger logger = LoggerFactory.getLogger(CSMEJWTUtil.class);


	public CSMEJWTUtil() throws Exception{
		
	}

	/**
	 * Generate JWT for the Claims
	 * @param userId
	 * @param corpID
	 * @return
	 * @throws Exception 
	 */
	public String generateSampleJWT(String userId, String corpID) throws Exception{
		try {
			PRIVATE_KEY_FILE_NAME = ASPathConst.USER_DIR_PATH+File.separator + "INT"+ File.separator+"SSO_KEYS"+File.separator + File.separator+"csme-sso_Privatekey.der";
			//PRIVATE_KEY_FILE_NAME=System.getProperty("catalina.base") +File.separator + "conf" + File.separator + "csme-sso_Privatekey.der";

			File file = new File(PRIVATE_KEY_FILE_NAME);
			FileInputStream fis = new FileInputStream(file);
			DataInputStream dis = new DataInputStream(fis);
			byte keyBytes[] = new byte[(int)file.length()];
			dis.readFully(keyBytes);
			dis.close();
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			privKey = (RSAPrivateKey)kf.generatePrivate(spec);
			
			TreeMap<String, Object> claims = new TreeMap<String,Object>();
			claims.putAll(getOtherClaims());
			claims.putAll(getCERequiredClaims(userId, corpID));
			JwtBuilder builder = Jwts.builder().setId("1001").setClaims(claims).signWith(signatureAlgorithm, privKey);
			long expMillis = new Date().getTime() + TOKEN_EXP_DURATION;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
			String JWTtoken = builder.compact();
			return JWTtoken;
		} catch (Exception e) {
			throw new Exception("Error While generating JWT token  ",e);
		}
	}

	/**
	 * Parse and get Claims for JWT
	 * @param JWTtoken
	 * @return
	 * @throws Exception 
	 */
	public JWTData parseJWT(String JWTtoken) throws Exception{
		try {
			PUBLIC_KEY_FILE_NAME = ASPathConst.USER_DIR_PATH+File.separator + "INT" + File.separator+"SSO_KEYS"+File.separator+"csme-sso_PublicKey.der";
			logger.info("Public Key Path "+PUBLIC_KEY_FILE_NAME);
			File file1 = new File(PUBLIC_KEY_FILE_NAME);
			FileInputStream fis1 = new FileInputStream(file1);
			DataInputStream dis1 = new DataInputStream(fis1);
			byte keyBytes1[] = new byte[(int)file1.length()];
			dis1.readFully(keyBytes1);
			dis1.close();
			X509EncodedKeySpec spec1 = new X509EncodedKeySpec(keyBytes1);
			KeyFactory kf1 = KeyFactory.getInstance("RSA");
			pubKey = (RSAPublicKey)kf1.generatePublic(spec1);
			Claims claims = (Claims)Jwts.parser().setSigningKey(pubKey).parseClaimsJws(JWTtoken).getBody();
			System.out.println(claims.toString());
			JWTData jwtData = new JWTData();
			jwtData.setId(claims.getId());
			jwtData.setUserId((String)claims.get("userId"));
			jwtData.setBusinessUnit((String)claims.get("businessUnit"));
			jwtData.setSubject(claims.getSubject());
			jwtData.setIssuer(claims.getIssuer());
			jwtData.setExpireDate(claims.getExpiration());
			System.out.println(jwtData);
			return jwtData;
		} catch (Exception e) {
			logger.info("Error while parsing token "+e);
			logger.error(e.getMessage());
			throw new Exception("Error While Parsing the token ",e);
		}
	}

	/**
	 * Method to put the CE required (Mandatory) claims
	 * @param userId
	 * @param corpID
	 * @return
	 * @throws Exception 
	 */
	private TreeMap<String, Object> getCERequiredClaims(String userId, String corpID) throws Exception{
		try {
			TreeMap<String, Object> ceClaims = new TreeMap<String, Object>();
			ceClaims.put("userId", userId);
			//ceClaims.put("businessUnit", corpID);
			return ceClaims;
		} catch (Exception e) {
			throw new Exception("Error While setting the CE required Claims ",e);
		}
	}

	/**
	 * Method to put Other Claims as per the JWT standards and others..
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("serial")
	private TreeMap<String, Object> getOtherClaims() throws Exception {
		try {
			return new TreeMap<String, Object>(){{
				put("jti", "1001");
				put("sub", "CESSOTesting");
				put("iss", "CE");
			}};
		} catch (Exception e) {
			throw new Exception("Error While setting the Other required Claims ",e);
		}
	}
	public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static String getSystemDate() {
		String reportDate = df.format(new Date());
		String reportDate1= reportDate.substring(0,10);
		return reportDate1;

	}
	
	
	public static String getExpiryDate(String effectivDateStr) throws ParseException {
		String str = effectivDateStr +" 00:00:00" ;
		Calendar cal = Calendar.getInstance();
		cal.setTime(df.parse(str));
		cal.add(Calendar.YEAR, 100);
		String temp = df.format(cal.getTime());
		String expirtyDate=temp.substring(0,10);
		return expirtyDate;
	}
	
	public static String getStackTraceAsString(Throwable exception) {
        if (exception == null) {
            return "No exception to extract stack trace.";
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        return stringWriter.toString();
    }

	public static void main(String args[])	throws Exception{
		CSMEJWTUtil csmeJWTUtil = new CSMEJWTUtil();
		String tokenValue=csmeJWTUtil.generateSampleJWT("KP","CSME");//"12121218", "CSBANK");
		csmeJWTUtil.parseJWT(tokenValue);
	}
	
	public static String getStringByReplacePlcHolderNew(String s, Map<String, String> valuesMap,int count) {
		String AUTH_STRL_PLC_HLDR = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root seq=\"Y\">" + "<"
				+ valuesMap.get("FLD_NAME") + " ccy=\"" + valuesMap.get("CCY") + ""
				+ "\" count=\""+count+"\" nouse=\"N\" rulename=\"" + valuesMap.get("RULE_NAME") + "" + "\">"
				+ valuesMap.get("SELECT_STRING") + "" + "</" + valuesMap.get("FLD_NAME") + "></root>";
		return AUTH_STRL_PLC_HLDR;
	}

}
