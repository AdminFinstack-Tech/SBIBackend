package com.csme.csmeapi.fin.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cs.base.log.CSLogger;
import com.cs.base.xml.XMLManager;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.exec.SQLRecord;
import com.cs.core.dao.exec.SQLRecordSet;
import com.cs.eximap.log.ASCELog;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XModuleAnalyticsRequest;
import com.csme.csmeapi.fin.models.XModuleAnalyticsResponse;
import com.csme.csmeapi.fin.models.XModuleAnalyticsResponse.TransactionTrend;
import com.csme.csmeapi.fin.models.XModuleAnalyticsResponse.TopBeneficiary;
import com.csme.csmeapi.mobile.util.MobileConfig;

@Service
public class ModuleAnalyticsApiService {
    
    private static final CSLogger logger = ASCELog.getCELogger("csme-rest");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    
    public XModuleAnalyticsResponse getModuleAnalytics(XModuleAnalyticsRequest request, String requestId) 
            throws Exception {
        
        XModuleAnalyticsResponse response = new XModuleAnalyticsResponse();
        String secuDs = DSManager.getSecuDS();
        
        try {
            // Set basic response fields
            response.setMessageId(String.valueOf(System.currentTimeMillis()));
            response.setRequestId(requestId);
            response.setTimestamp(DATE_FORMAT.format(new Date()));
            response.setModule(request.getModule());
            
            // Get base currency
            String baseCurrency = getBaseCurrency(request.getCorporateId());
            response.setCurrency(baseCurrency);
            
            // Convert period to months for query
            int months = getPeriodInMonths(request.getPeriod());
            
            // Load XML configuration
            String xmlFilePath = FinUtil.intPath + "ModuleAnalytics.xml";
            logger.info("Loading ModuleAnalytics XML from: " + xmlFilePath);
            Document xmlDoc = XMLManager.xmlFileToDom(xmlFilePath);
            
            // Get module summary analytics
            String summaryQuery = getQueryFromXML(xmlDoc, "getModuleSummaryAnalytics");
            if (summaryQuery != null) {
                summaryQuery = buildParameterizedQuery(summaryQuery, request.getCorporateId(), 
                                                     request.getModule(), months);
                
                logger.info("Executing summary query: " + summaryQuery);
                SQLRecordSet summaryResult = WSDBHelper.executeQuery(summaryQuery, secuDs);
                
                if (summaryResult.isSuccess() && summaryResult.getRecordCount() > 0) {
                    SQLRecord record = summaryResult.getFirstRecord();
                    response.setTotalTransactions(getIntValue(record.getValue("TOTALTRANSACTIONS")));
                    response.setPendingCount(getIntValue(record.getValue("PENDINGCOUNT")));
                    response.setTotalValue(getStringValue(record.getValue("TOTALVALUE")));
                } else {
                    // No data found
                    logger.info("No transaction data found for module: " + request.getModule() + 
                               ", corporate: " + request.getCorporateId());
                    response.setTotalTransactions(0);
                    response.setPendingCount(0);
                    response.setTotalValue("0.00");
                }
            }
            
            // Get transaction trends
            String trendsQuery = getQueryFromXML(xmlDoc, "getModuleTransactionTrends");
            if (trendsQuery != null) {
                trendsQuery = buildParameterizedQuery(trendsQuery, request.getCorporateId(), 
                                                    request.getModule(), months);
                
                logger.info("Executing trends query: " + trendsQuery);
                SQLRecordSet trendsResult = WSDBHelper.executeQuery(trendsQuery, secuDs);
                List<TransactionTrend> trends = new ArrayList<>();
                
                if (trendsResult.isSuccess() && trendsResult.getRecordCount() > 0) {
                    for (int i = 0; i < trendsResult.getRecordCount(); i++) {
                        SQLRecord record = trendsResult.getSQLRecord(i);
                        TransactionTrend trend = new TransactionTrend();
                        trend.setMonth(getStringValue(record.getValue("MONTH")));
                        trend.setCount(getIntValue(record.getValue("TRANSACTIONCOUNT")));
                        trend.setValue(getStringValue(record.getValue("TRANSACTIONVALUE")));
                        trends.add(trend);
                    }
                } else {
                    logger.info("No transaction trend data found");
                }
                response.setTransactionTrend(trends);
            }
            
            // Get top beneficiaries
            String beneficiariesQuery = getQueryFromXML(xmlDoc, "getModuleTopBeneficiaries");
            if (beneficiariesQuery != null) {
                beneficiariesQuery = buildParameterizedQuery(beneficiariesQuery, 
                                                           request.getCorporateId(), 
                                                           request.getModule(), months);
                
                logger.info("Executing beneficiaries query: " + beneficiariesQuery);
                SQLRecordSet beneficiariesResult = WSDBHelper.executeQuery(beneficiariesQuery, secuDs);
                List<TopBeneficiary> beneficiaries = new ArrayList<>();
                
                if (beneficiariesResult.isSuccess() && beneficiariesResult.getRecordCount() > 0) {
                    for (int i = 0; i < beneficiariesResult.getRecordCount(); i++) {
                        SQLRecord record = beneficiariesResult.getSQLRecord(i);
                        TopBeneficiary beneficiary = new TopBeneficiary();
                        beneficiary.setName(getStringValue(record.getValue("BENEFICIARYNAME")));
                        beneficiary.setCount(getIntValue(record.getValue("TRANSACTIONCOUNT")));
                        beneficiary.setValue(getStringValue(record.getValue("TOTALVALUE")));
                        beneficiaries.add(beneficiary);
                    }
                } else {
                    logger.info("No beneficiary data found");
                }
                response.setTopBeneficiaries(beneficiaries);
            }
            
            // Set success response
            response.setStatusCode("00");
            response.setStatusDescription("Module analytics retrieved successfully");
            
        } catch (Exception e) {
            logger.error("Error in ModuleAnalyticsApiService: " + e.getMessage());
            response.setStatusCode("99");
            response.setStatusDescription("Error retrieving module analytics: " + e.getMessage());
        }
        
        return response;
    }
    
    private String getQueryFromXML(Document xmlDoc, String queryId) {
        try {
            Element rootElement = xmlDoc.getDocumentElement();
            NodeList queryNodes = rootElement.getElementsByTagName("query");
            
            for (int i = 0; i < queryNodes.getLength(); i++) {
                Element queryElement = (Element) queryNodes.item(i);
                String id = queryElement.getAttribute("id");
                
                if (queryId.equals(id)) {
                    return queryElement.getTextContent().trim();
                }
            }
        } catch (Exception e) {
            logger.error("Error reading query from XML: " + e.getMessage());
        }
        return null;
    }
    
    private String buildParameterizedQuery(String query, String corporateId, String module, int months) {
        // Replace template variables with actual values
        String result = query;
        result = result.replace("${unitcode}", corporateId);
        result = result.replace("${module}", module);
        result = result.replace("${months}", String.valueOf(months));
        return result;
    }
    
    private String getBaseCurrency(String corporateId) {
        try {
            String currency = MobileConfig.getCustomProperty("BASE_CURRENCY");
            if (currency == null || currency.isEmpty()) {
                // Try to get from database - SEC_USER_INFO table
                String query = "SELECT C_BASE_CCY FROM CEUSER.SEC_USER_INFO WHERE C_UNIT_CODE = '" + corporateId + "'";
                String secuDs = DSManager.getSecuDS();
                SQLRecordSet rs = WSDBHelper.executeQuery(query, secuDs);
                
                if (rs.isSuccess() && rs.getRecordCount() > 0) {
                    currency = rs.getFirstRecord().getValue("C_BASE_CCY");
                }
            }
            return currency != null && !currency.isEmpty() ? currency : "SAR";
        } catch (Exception e) {
            logger.error("Error getting base currency: " + e.getMessage());
            return "SAR";
        }
    }
    
    private int getPeriodInMonths(String period) {
        if (period == null) return 3; // Default 3 months
        
        switch (period.toLowerCase()) {
            case "7d":
                return 1; // Last month
            case "30d":
                return 1; // Last month
            case "90d":
                return 3; // Last 3 months
            case "1y":
                return 12; // Last year
            default:
                return 3; // Default 3 months
        }
    }
    
    private Integer getIntValue(String value) {
        if (value == null || value.trim().isEmpty()) return 0;
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }
    
    private String getStringValue(String value) {
        return value != null ? value : "0";
    }
}