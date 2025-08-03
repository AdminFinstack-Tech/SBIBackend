package com.csme.csmeapi.mobile.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cs.base.log.CSLogger;
import com.cs.base.utility.ASPathConst;
import com.cs.eximap.log.ASCELog;

public class Tabadul2LogUtil {
	
	private static final LGInquiryConstants tabadul2Constants = new LGInquiryConstants();
	public static final String logConfFilePath = ASPathConst.getUserDirPath() + File.separator + "EEConfig"  + File.separator + "EE_Log_Config.xml";
	private static final CSLogger logger = ASCELog.getCELogger("csme-rest");
	private static final ThreadLocal<SimpleDateFormat> dateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
	
	public Tabadul2LogUtil(){
		// Constructor - no initialization needed
	}
	
	private String getTimeNow() {
		return dateFormat.get().format(new Date()) + "  :			";
	}
	public void debug(String msg){
		if(!tabadul2Constants.isWebMode()) {
			logger.debug(getTimeNow()+msg);
		}else{
			System.out.println(getTimeNow()+msg);
		}
	}
	
	public void writeDebug(String msg){
		if(!tabadul2Constants.isWebMode()) {
			logger.debug(getTimeNow()+msg);
		}else{
			System.out.println(getTimeNow()+msg);
		}
	}
	
	public void writeDebug(Exception msg){
		if(!tabadul2Constants.isWebMode()) {
			logger.debug(getTimeNow()+msg.getMessage());
		}else{
			System.out.println(getTimeNow()+msg.getMessage());
		}
	}
	
	public void writeError(Exception msg){

		if(!tabadul2Constants.isWebMode()) {
			logger.error(getTimeNow()+msg.getMessage());
		}else{
			System.out.println(getTimeNow()+msg.getMessage());
		}
	}
	
	public void writeError(String msg){

		if(!tabadul2Constants.isWebMode()) {
			logger.error(getTimeNow()+msg);
		}else{
			System.out.println(getTimeNow()+msg);
		}
	}
	
	public void error(String msg){

		if(!tabadul2Constants.isWebMode()) {
			logger.error(getTimeNow()+msg);
		}else{
			System.out.println(getTimeNow()+msg);
		}
	}
	
	public void error(Exception msg){

		if(!tabadul2Constants.isWebMode()) {
			logger.error(getTimeNow()+msg.getMessage());
		}else{
			System.out.println(getTimeNow()+getErrorStackTrace(msg));
		}
	}
	
	public void info(String msg){
		if(!tabadul2Constants.isWebMode()) {
			logger.info(getTimeNow()+msg);
		}else{
			System.out.println(getTimeNow()+msg);
		}
	}
	
	public void infoS(String msg){
		if(!tabadul2Constants.isWebMode()) {
			logger.info(getTimeNow()+msg);
		}else{
			System.out.println(getTimeNow()+msg);
		}
	}
	
	public void info(Exception msg){
		if(!tabadul2Constants.isWebMode()) {
			logger.info(getTimeNow()+msg.getMessage());
		}else{
			System.out.println(getTimeNow()+msg.getMessage());
		}
	}
	
	public void info(long msg){
		if(!tabadul2Constants.isWebMode()) {
			logger.info(getTimeNow()+msg);
		}else{
			System.out.println(getTimeNow()+msg);
		}
	}
	
	public String getErrorStackTrace(Throwable e) {
		if (e == null) {
			return "";
		}
		
		try (ByteArrayOutputStream os = new ByteArrayOutputStream();
		     PrintWriter pw = new PrintWriter(os)) {
			e.printStackTrace(pw);
			pw.flush();
			return os.toString();
		} catch (Exception ee) {
			logger.error("Error getting stack trace: " + ee.getMessage());
			return e.getMessage() != null ? e.getMessage() : "Unknown error";
		}
	}
}
