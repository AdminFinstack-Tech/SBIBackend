package com.csme.csmeapi.mobile.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.util.Calendar;

import com.cs.base.log.CSLogger;
import com.cs.base.utility.ASPathConst;
import com.cs.eximap.log.ASCELog;

public class Tabadul2LogUtil {
	
	LGInquiryConstanst tabadul2Constanst = new LGInquiryConstanst();
	public static String logConfFilePath = ASPathConst.getUserDirPath() + File.pathSeparator + "EEConfig"  + File.pathSeparator + "EE_Log_Config.xml";
	private CSLogger logger = ASCELog.getCELogger("csme-rest");
	public String timeNow;
	
	public Tabadul2LogUtil(){
		Calendar cal = Calendar.getInstance();
		timeNow= cal.get(Calendar.DATE) + "-" + String.valueOf(cal.get(Calendar.MONTH)+1)  + "-" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR) + ":" +
				cal.get(Calendar.MINUTE) +":"+ cal.get(Calendar.SECOND) +"  :			"  ;
	}
	public void debug(String msg){
		if(!tabadul2Constanst.IS_WEB_MODE) {
			logger.debug(timeNow+msg);
		}else{
			System.out.println(timeNow+msg);
		}
	}
	
	public void writeDebug(String msg){
		if(!tabadul2Constanst.IS_WEB_MODE) {
			logger.debug(timeNow+msg);
		}else{
			System.out.println(timeNow+msg);
		}
	}
	
	public void writeDebug(Exception msg){
		if(!tabadul2Constanst.IS_WEB_MODE) {
			logger.debug(timeNow+msg);
		}else{
			System.out.println(timeNow+msg);
		}
	}
	
	public void writeError(Exception msg){

		if(!tabadul2Constanst.IS_WEB_MODE) {
			logger.error(timeNow+msg);
		}else{
			System.out.println(timeNow+msg);
		}
	}
	
	public void writeError(String msg){

		if(!tabadul2Constanst.IS_WEB_MODE) {
			logger.error(timeNow+msg);
		}else{
			System.out.println(msg);
		}
	}
	
	public void error(String msg){

		if(!tabadul2Constanst.IS_WEB_MODE) {
			logger.error(timeNow+msg);
		}else{
			System.out.println(timeNow+msg);
		}
	}
	
	public void error(Exception msg){

		if(!tabadul2Constanst.IS_WEB_MODE) {
			logger.error(timeNow+msg);
		}else{
			System.out.println(timeNow+getErrorStackTrace(msg));
		}
	}
	
	public void info(String msg){
		if(!tabadul2Constanst.IS_WEB_MODE) {
			logger.info(timeNow+msg);
		}else{
			System.out.println(timeNow+msg);
		}
	}
	
	public void infoS(String msg){
		if(!tabadul2Constanst.IS_WEB_MODE) {
			logger.info(timeNow+msg);
		}else{
			System.out.println(timeNow+msg);
		}
	}
	
	public void info(Exception msg){
		if(!tabadul2Constanst.IS_WEB_MODE) {
			logger.info(timeNow+msg);
		}else{
			System.out.println(timeNow+msg);
		}
	}
	
	public void info(long msg){
		if(!tabadul2Constanst.IS_WEB_MODE) {
			logger.info(timeNow+msg);
		}else{
			System.out.println(timeNow+msg);
		}
	}
	
	public String getErrorStackTrace(Throwable e) {
		String err = "";
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintWriter pw = new PrintWriter(os);
			e.printStackTrace(pw);
			os.close();
			pw.close();
			err = os.toString();
			os = null;
			pw = null;
		} catch (Exception ee) {
			logger.error("Error While Deleting already Sent documents " + e);
		}
		return err;
	}
}
