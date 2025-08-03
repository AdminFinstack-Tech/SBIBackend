package com.csme.csmeapi.mobile.util;

import java.io.File;
import java.util.Map;

import com.cs.base.utility.ASPathConst;

public class LGInquiryConstanst {
	
	public String TAB2_CONSTANT_PATH=ASPathConst.USER_DIR_PATH+File.separator+"INT"+File.separator+"L"+File.separator+"LGInquiryConstant.xml";
	public String dbmappingxml ;
	public Map<String, String> rbConstantMap;
	public boolean IS_WEB_MODE = false;
	
	public LGInquiryConstanst()
	{
//		CSMEDomParser esbparser;
//		try {
//			rbConstantMap = XMLManager.convertToHashMap(XMLManager.xmlFileToDom(TAB2_CONSTANT_PATH));
//			esbparser = new CSMEDomParser(new File(TAB2_CONSTANT_PATH));
//			dbmappingxml = esbparser.getNodeValue("/root/lginquiry/dbmappingxml");
//			
//		} catch (Exception e) {
//			try {
//				throw new Exception(e);
//			} catch (Exception e1) {
//			}
//		}
	}

}


