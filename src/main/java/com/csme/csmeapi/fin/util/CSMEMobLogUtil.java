package com.csme.csmeapi.fin.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;

import com.cs.base.log.CSLogger;
import com.cs.eximap.log.ASCELog;
import com.cs.eximap.log.CELogUtility;
import com.csme.csmeapi.fin.config.FinConfig;
import com.csme.csmeapi.fin.config.FinUtil;

/**
 * Utility class for logging messages in the CSME Mobile application.
 */
public class CSMEMobLogUtil {
	
	/**
	 * Indicates whether the application is running in web mode.
	 * If set to true, logs will be output to the console.
	 * Default is false, which uses the CSLogger for logging.
	 */
	private static final boolean IS_WEB_MODE = false;

	/**
	 * The file path for the log configuration.
	 * This path is retrieved from the FinUtil class.
	 */
	public static String logConfFilePath = FinUtil.logConfFilePath;

	/**
	 * The logger instance used for logging messages.
	 * Initialized with the CSMEMobile logger from ASCELog.
	 */
	public static final CSLogger logger = ASCELog.getCELogger(CELogUtility.getLoggerWithClassName(FinConfig.class, "CSMEMobile"));

	/**
	 * The current timestamp for log messages.
	 * Used to prefix log messages with the timestamp of the log entry.
	 */
	public String timeNow;

	
	/**
	 * Constructor for CSMEMobLogUtil class.
	 * Initializes the timeNow variable with the current timestamp.
	 */
	public CSMEMobLogUtil(){
	    // Get the current timestamp using Calendar.getInstance()
	    Calendar cal = Calendar.getInstance();
	    // Format the timestamp as "DD-MM-YYYY HH:MM:SS"
	    timeNow = cal.get(Calendar.DATE) + "-" + String.valueOf(cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR) + " " +
	            cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND) + "  :            ";
	}

	public void debug(String msg){
		if(!IS_WEB_MODE) {
			logger.debug(timeNow+msg);
		}else{
			System.out.println(timeNow+msg);
		}
	}
	
	public void writeDebug(String msg){
		if(!IS_WEB_MODE) {
			logger.debug(timeNow+msg);
		}else{
			System.out.println(timeNow+msg);
		}
	}
	
	public void writeDebug(Exception msg){
		if(!IS_WEB_MODE) {
			logger.debug(timeNow+msg);
		}else{
			System.out.println(timeNow+msg);
		}
	}
	
	public void writeError(Exception msg){

		if(!IS_WEB_MODE) {
			logger.error(timeNow+msg);
		}else{
			System.out.println(timeNow+msg);
		}
	}
	
	public void writeError(String msg){

		if(!IS_WEB_MODE) {
			logger.error(timeNow+msg);
		}else{
			System.out.println(msg);
		}
	}
	
	public void error(String msg){

		if(!IS_WEB_MODE) {
			logger.error(timeNow+msg);
		}else{
			System.out.println(timeNow+msg);
		}
	}
	
	public void error(Exception msg){

		if(!IS_WEB_MODE) {
			logger.error(timeNow+msg);
		}else{
			System.out.println(timeNow+getErrorStackTrace(msg));
		}
	}
	
	public void info(String msg){
		if(!IS_WEB_MODE) {
			logger.info(timeNow+msg);
		}else{
			System.out.println(timeNow+msg);
		}
	}
	
	public void infoS(String msg){
		if(!IS_WEB_MODE) {
			logger.info(timeNow+msg);
		}else{
			System.out.println(timeNow+msg);
		}
	}
	
	public void info(Exception msg){
		if(!IS_WEB_MODE) {
			logger.info(timeNow+msg);
		}else{
			System.out.println(timeNow+msg);
		}
	}
	
	public void info(long msg){
		if(!IS_WEB_MODE) {
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
