package com.csme.csmeapi.fin.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.cs.base.xml.XMLManager;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XDashboardAnalyticsRequest;
import com.csme.csmeapi.fin.models.XDashboardAnalyticsResponse;
import com.csme.csmeapi.fin.models.XBeneficiaryAnalyticsRequest;
import com.csme.csmeapi.fin.models.XBeneficiaryAnalyticsResponse;
import com.csme.csmeapi.fin.models.ModuleStatistics;
import com.csme.csmeapi.fin.models.BeneficiaryStatistics;
import com.csme.csmeapi.fin.models.PerformanceMetrics;

/**
 * Dashboard Analytics Service
 * Provides comprehensive analytics and performance metrics for banking operations
 * Uses XML configuration files from MOBILE folder for SQL queries following existing patterns
 */
@Service
public class DashboardAnalyticsApiService {

    private static final Logger log = Logger.getLogger("CSMEMobile");
    private String secuDs = null;

    public DashboardAnalyticsApiService() {
        try {
            secuDs = FinUtil.SECDS;
        } catch (Exception e) {
            log.error("Error initializing DashboardAnalyticsApiService: " + e.getMessage());
        }
    }

    /**
     * Get comprehensive dashboard analytics with yearly and monthly statistics
     * 
     * @param request Dashboard analytics request
     * @return Dashboard analytics response with statistics for all modules
     */
    public XDashboardAnalyticsResponse getDashboardAnalytics(XDashboardAnalyticsRequest request) {
        log.info("DashboardAnalyticsApiService.getDashboardAnalytics - Start");
        
        XDashboardAnalyticsResponse response = new XDashboardAnalyticsResponse();

        try {
            // Get user's accessible modules from profile
            String accessibleModules = FinUtil.getCompanyAssignedModules(request.getCorporateId(), request.getUserId());
            log.info("User accessible modules: " + accessibleModules);
            
            if (accessibleModules == null || accessibleModules.trim().isEmpty()) {
                response.setStatusCode("10");
                response.setStatusDescription("No accessible modules found for user");
                return response;
            }

            // Load XML configuration file
            String filePath = FinUtil.intPath + "DashboardAnalytics.xml";
            Document configDoc = XMLManager.xmlFileToDom(filePath);
            log.info("Loaded DashboardAnalytics config: " + XMLManager.convertToString(configDoc));

            // Get SQL query from XML configuration
            String sqlQuery = getQueryFromConfig(configDoc, "DashboardAnalyticsQuery");
            
            // Format modules for SQL IN clause
            String formattedModules = formatModulesForSQL(accessibleModules);
            
            // Prepare values for substitution with dynamic module filtering
            Map<String, String> valuesMap = new HashMap<>();
            valuesMap.put("unitcode", request.getCorporateId());
            valuesMap.put("modules", formattedModules);
            
            // Replace placeholders in SQL query
            StringSubstitutor substitutor = new StringSubstitutor(valuesMap);
            sqlQuery = substitutor.replace(sqlQuery);

            log.info("Executing dashboard analytics query: " + sqlQuery);
            
            // Execute the query
            SQLRecordSet recordSet = WSDBHelper.executeQuery(sqlQuery, secuDs);

            List<ModuleStatistics> moduleStatisticsList = new ArrayList<>();
            Map<String, PerformanceMetrics> performanceMetricsMap = new HashMap<>();

            if (recordSet.isSuccess() && recordSet.getRecordCount() > 0) {
                for (int i = 0; i < recordSet.getRecordCount(); i++) {
                    ModuleStatistics stats = new ModuleStatistics();
                    stats.setModule(recordSet.getRecord(i).getValue("C_MODULE"));
                    stats.setYear(recordSet.getRecord(i).getValue("Year"));
                    stats.setMonth(recordSet.getRecord(i).getValue("Month"));
                    stats.setCustomer(recordSet.getRecord(i).getValue("C_UNIT_CODE"));
                    
                    // Parse amounts safely
                    String lcAmtStr = recordSet.getRecord(i).getValue("Total_LcAmt");
                    stats.setTotalLcAmt(lcAmtStr != null && !lcAmtStr.isEmpty() ? new BigDecimal(lcAmtStr) : BigDecimal.ZERO);
                    
                    String collAmtStr = recordSet.getRecord(i).getValue("Total_CollAmt");
                    stats.setTotalCollAmt(collAmtStr != null && !collAmtStr.isEmpty() ? new BigDecimal(collAmtStr) : BigDecimal.ZERO);
                    
                    String gteeAmtStr = recordSet.getRecord(i).getValue("Total_GteeAmt");
                    stats.setTotalGteeAmt(gteeAmtStr != null && !gteeAmtStr.isEmpty() ? new BigDecimal(gteeAmtStr) : BigDecimal.ZERO);
                    
                    // Handle date parsing
                    String expiryStr = recordSet.getRecord(i).getValue("Latest_Expiry");
                    if (expiryStr != null && !expiryStr.isEmpty()) {
                        try {
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                            stats.setLatestExpiry(sdf.parse(expiryStr));
                        } catch (Exception e) {
                            log.warn("Error parsing expiry date: " + expiryStr);
                        }
                    }
                    
                    String transCountStr = recordSet.getRecord(i).getValue("Transaction_Count");
                    stats.setTransactionCount(transCountStr != null && !transCountStr.isEmpty() ? Integer.parseInt(transCountStr) : 0);

                    moduleStatisticsList.add(stats);

                    // Calculate performance metrics by module
                    String module = stats.getModule();
                    PerformanceMetrics metrics = performanceMetricsMap.getOrDefault(module, new PerformanceMetrics());
                    metrics.setModule(module);
                    metrics.setTotalTransactions(metrics.getTotalTransactions() + stats.getTransactionCount());
                    metrics.setTotalLcAmount(metrics.getTotalLcAmount().add(stats.getTotalLcAmt()));
                    metrics.setTotalCollAmount(metrics.getTotalCollAmount().add(stats.getTotalCollAmt()));
                    metrics.setTotalGteeAmount(metrics.getTotalGteeAmount().add(stats.getTotalGteeAmt()));
                    
                    performanceMetricsMap.put(module, metrics);
                }
            }

            // Get additional summary metrics
            Map<String, Object> summaryMetrics = getSummaryMetrics(request.getCorporateId(), accessibleModules, configDoc);

            response.setStatusCode("00");
            response.setStatusDescription("Dashboard analytics retrieved successfully");
            response.setModuleStatistics(moduleStatisticsList);
            response.setPerformanceMetrics(new ArrayList<>(performanceMetricsMap.values()));
            response.setSummaryMetrics(summaryMetrics);
            response.setTotalRecords(moduleStatisticsList.size());

            log.info("Dashboard analytics retrieved successfully. Total records: " + moduleStatisticsList.size());

        } catch (Exception e) {
            log.error("Error retrieving dashboard analytics: " + e.getMessage(), e);
            response.setStatusCode("10");
            response.setStatusDescription("Error retrieving dashboard analytics: " + e.getMessage());
        }

        return response;
    }


    /**
     * Get beneficiary analytics showing top beneficiaries by transaction count
     * 
     * @param request Beneficiary analytics request
     * @return Beneficiary analytics response with top beneficiaries
     */
    public XBeneficiaryAnalyticsResponse getBeneficiaryAnalytics(XBeneficiaryAnalyticsRequest request) {
        log.info("DashboardAnalyticsApiService.getBeneficiaryAnalytics - Start");
        
        XBeneficiaryAnalyticsResponse response = new XBeneficiaryAnalyticsResponse();

        try {
            // Load XML configuration file
            String filePath = FinUtil.intPath + "DashboardAnalytics.xml";
            Document configDoc = XMLManager.xmlFileToDom(filePath);

            // Get SQL query from XML configuration
            String sqlQuery = getQueryFromConfig(configDoc, "BeneficiaryAnalyticsQuery");
            
            // Prepare values for substitution
            Map<String, String> valuesMap = new HashMap<>();
            valuesMap.put("unitcode", request.getCorporateId());
            valuesMap.put("months", String.valueOf(-6)); // Default 6 months back
            
            // Replace placeholders in SQL query
            StringSubstitutor substitutor = new StringSubstitutor(valuesMap);
            sqlQuery = substitutor.replace(sqlQuery);

            log.info("Executing beneficiary analytics query: " + sqlQuery);
            
            // Execute the query
            SQLRecordSet recordSet = WSDBHelper.executeQuery(sqlQuery, secuDs);

            List<BeneficiaryStatistics> beneficiaryStatisticsList = new ArrayList<>();
            int totalTransactions = 0;

            if (recordSet.isSuccess() && recordSet.getRecordCount() > 0) {
                for (int i = 0; i < recordSet.getRecordCount(); i++) {
                    BeneficiaryStatistics stats = new BeneficiaryStatistics();
                    stats.setApplicant(recordSet.getRecord(i).getValue("APPL_NM"));
                    stats.setBeneficiary(recordSet.getRecord(i).getValue("BENE_NM"));
                    
                    String transCountStr = recordSet.getRecord(i).getValue("Transaction_Count");
                    int transCount = transCountStr != null && !transCountStr.isEmpty() ? Integer.parseInt(transCountStr) : 0;
                    stats.setTransactionCount(transCount);
                    
                    // Parse amounts safely
                    String lcAmtStr = recordSet.getRecord(i).getValue("Total_LcAmt");
                    stats.setTotalLcAmt(lcAmtStr != null && !lcAmtStr.isEmpty() ? new BigDecimal(lcAmtStr) : BigDecimal.ZERO);
                    
                    String collAmtStr = recordSet.getRecord(i).getValue("Total_CollAmt");
                    stats.setTotalCollAmt(collAmtStr != null && !collAmtStr.isEmpty() ? new BigDecimal(collAmtStr) : BigDecimal.ZERO);
                    
                    String gteeAmtStr = recordSet.getRecord(i).getValue("Total_GteeAmt");
                    stats.setTotalGteeAmt(gteeAmtStr != null && !gteeAmtStr.isEmpty() ? new BigDecimal(gteeAmtStr) : BigDecimal.ZERO);
                    
                    // Handle date parsing
                    String lastTrxDateStr = recordSet.getRecord(i).getValue("Last_Transaction_Date");
                    if (lastTrxDateStr != null && !lastTrxDateStr.isEmpty()) {
                        try {
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                            stats.setLastTransactionDate(sdf.parse(lastTrxDateStr));
                        } catch (Exception e) {
                            log.warn("Error parsing last transaction date: " + lastTrxDateStr);
                        }
                    }

                    beneficiaryStatisticsList.add(stats);
                    totalTransactions += transCount;
                }
            }

            response.setStatusCode("00");
            response.setStatusDescription("Beneficiary analytics retrieved successfully");
            response.setBeneficiaryStatistics(beneficiaryStatisticsList);
            response.setTotalBeneficiaries(beneficiaryStatisticsList.size());
            response.setTotalTransactions(totalTransactions);
            response.setAnalyzedPeriod(6);

            log.info("Beneficiary analytics retrieved successfully. Total beneficiaries: " + beneficiaryStatisticsList.size());

        } catch (Exception e) {
            log.error("Error retrieving beneficiary analytics: " + e.getMessage(), e);
            response.setStatusCode("10");
            response.setStatusDescription("Error retrieving beneficiary analytics: " + e.getMessage());
        }

        return response;
    }

    /**
     * Get performance summary with key metrics and trends
     */
    public Map<String, Object> getPerformanceSummary(XDashboardAnalyticsRequest request) {
        log.info("DashboardAnalyticsApiService.getPerformanceSummary - Start");
        
        Map<String, Object> summary = new HashMap<>();

        try {
            // Get user's accessible modules from profile
            String accessibleModules = FinUtil.getCompanyAssignedModules(request.getCorporateId(), request.getUserId());
            
            String filePath = FinUtil.intPath + "DashboardAnalytics.xml";
            Document configDoc = XMLManager.xmlFileToDom(filePath);
            summary.putAll(getSummaryMetrics(request.getCorporateId(), accessibleModules, configDoc));
        } catch (Exception e) {
            log.error("Error retrieving performance summary: " + e.getMessage(), e);
            summary.put("error", "Error retrieving performance summary: " + e.getMessage());
        }

        return summary;
    }

    /**
     * Get summary metrics including totals and trends
     */
    private Map<String, Object> getSummaryMetrics(String corporateId, String accessibleModules, Document configDoc) throws Exception {
        
        Map<String, Object> metrics = new HashMap<>();
        
        try {
            // Get summary metrics query
            String sqlQuery = getQueryFromConfig(configDoc, "SummaryMetricsQuery");
            
            // Format modules for SQL IN clause
            String formattedModules = formatModulesForSQL(accessibleModules);
            
            // Prepare values for substitution
            Map<String, String> valuesMap = new HashMap<>();
            valuesMap.put("unitcode", corporateId);
            valuesMap.put("modules", formattedModules);
            
            // Replace placeholders in SQL query
            StringSubstitutor substitutor = new StringSubstitutor(valuesMap);
            sqlQuery = substitutor.replace(sqlQuery);
            
            SQLRecordSet recordSet = WSDBHelper.executeQuery(sqlQuery, secuDs);
            
            if (recordSet.isSuccess() && recordSet.getRecordCount() > 0) {
                String transCountStr = recordSet.getFirstRecord().getValue("total_transactions");
                metrics.put("currentYearTransactions", transCountStr != null ? Integer.parseInt(transCountStr) : 0);
                
                String lcAmtStr = recordSet.getFirstRecord().getValue("total_lc_amount");
                metrics.put("currentYearLcAmount", lcAmtStr != null && !lcAmtStr.isEmpty() ? new BigDecimal(lcAmtStr) : BigDecimal.ZERO);
                
                String collAmtStr = recordSet.getFirstRecord().getValue("total_coll_amount");
                metrics.put("currentYearCollAmount", collAmtStr != null && !collAmtStr.isEmpty() ? new BigDecimal(collAmtStr) : BigDecimal.ZERO);
                
                String gteeAmtStr = recordSet.getFirstRecord().getValue("total_gtee_amount");
                metrics.put("currentYearGteeAmount", gteeAmtStr != null && !gteeAmtStr.isEmpty() ? new BigDecimal(gteeAmtStr) : BigDecimal.ZERO);
            }

            // Get active modules
            String moduleQuery = getQueryFromConfig(configDoc, "ActiveModulesQuery");
            moduleQuery = substitutor.replace(moduleQuery);
            
            SQLRecordSet moduleRecordSet = WSDBHelper.executeQuery(moduleQuery, secuDs);
            
            List<String> activeModules = new ArrayList<>();
            if (moduleRecordSet.isSuccess() && moduleRecordSet.getRecordCount() > 0) {
                for (int i = 0; i < moduleRecordSet.getRecordCount(); i++) {
                    String module = moduleRecordSet.getRecord(i).getValue("C_MODULE");
                    if (module != null && !module.isEmpty()) {
                        activeModules.add(module);
                    }
                }
            }
            metrics.put("activeModules", activeModules);
            metrics.put("activeModulesCount", activeModules.size());

        } catch (Exception e) {
            log.error("Error getting summary metrics: " + e.getMessage(), e);
            throw e;
        }

        return metrics;
    }

    /**
     * Gets the SQL query from XML configuration by tag name
     * Similar to existing getModuleQuery pattern
     */
    private String getQueryFromConfig(Document configDoc, String tagName) {
        Element rootElement = configDoc.getDocumentElement();
        NodeList queryNodes = rootElement.getElementsByTagName(tagName);

        if (queryNodes.getLength() > 0) {
            Element queryElement = (Element) queryNodes.item(0);
            return queryElement.getTextContent().trim();
        }

        return null;
    }

    /**
     * Format modules string for SQL IN clause
     * Converts "IMLC,EXLC,OWGT" to "'IMLC','EXLC','OWGT'"
     */
    private String formatModulesForSQL(String modules) {
        if (modules == null || modules.trim().isEmpty()) {
            return "''";
        }
        
        String[] moduleArray = modules.split(",");
        StringBuilder formattedModules = new StringBuilder();
        
        for (int i = 0; i < moduleArray.length; i++) {
            String module = moduleArray[i].trim();
            if (!module.isEmpty()) {
                if (formattedModules.length() > 0) {
                    formattedModules.append(",");
                }
                formattedModules.append("'").append(module).append("'");
            }
        }
        
        return formattedModules.toString();
    }
}