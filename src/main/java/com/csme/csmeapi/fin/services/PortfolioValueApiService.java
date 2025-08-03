package com.csme.csmeapi.fin.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.cs.base.xml.XMLManager;
import com.cs.ce.core.helper.WSDBHelper;
import com.cs.core.dao.exec.SQLRecordSet;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.XPortfolioValueRequest;
import com.csme.csmeapi.fin.models.XPortfolioValueResponse;
import com.csme.csmeapi.fin.models.XPortfolioValueResponse.AccountBreakdown;
import com.csme.csmeapi.fin.services.MobileAppCommonDao;
import com.csme.csmeapi.mobile.util.MobileConfig;

@Service
public class PortfolioValueApiService {
    
    private MobileAppCommonDao mobileAppCommonDao;
    private String secuDs = null;
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    
    public PortfolioValueApiService() {
        try {
            this.mobileAppCommonDao = new MobileAppCommonDao();
            this.secuDs = FinUtil.SECDS;
        } catch (Exception e) {
            System.err.println("Error initializing PortfolioValueApiService: " + e.getMessage());
        }
    }
    
    public XPortfolioValueResponse getPortfolioValue(XPortfolioValueRequest request, String requestId) 
            throws Exception {
        
        XPortfolioValueResponse response = new XPortfolioValueResponse();
        
        try {
            // Set basic response fields
            response.setMessageId(String.valueOf(System.currentTimeMillis()));
            response.setRequestId(requestId);
            response.setTimestamp(DATE_FORMAT.format(new Date()));
            
            // Get user's accessible modules from FAP
            String accessibleModules = FinUtil.getCompanyAssignedModules(request.getCorporateId(), request.getUserId());
            System.out.println("User accessible modules for portfolio: " + accessibleModules);
            
            if (accessibleModules == null || accessibleModules.trim().isEmpty()) {
                response.setStatusCode("01");
                response.setStatusDescription("User does not have access to any modules");
                return response;
            }
            
            // Get portfolio data
            String baseCurrency = getBaseCurrency(request.getCorporateId());
            response.setCurrency(baseCurrency);
            response.setAsOfDate(DATE_FORMAT.format(new Date()));
            
            // Get account breakdown filtered by accessible modules
            List<AccountBreakdown> accounts = getAccountBreakdown(request.getCorporateId(), baseCurrency, accessibleModules);
            response.setAccountBreakdown(accounts);
            
            // Calculate total portfolio value
            BigDecimal totalValue = calculateTotalValue(accounts);
            response.setTotalValue(totalValue.toPlainString());
            
            // Set success response
            response.setStatusCode("00");
            response.setStatusDescription("Portfolio value retrieved successfully");
            
        } catch (Exception e) {
            System.err.println("Error in PortfolioValueApiService: " + e.getMessage());
            response.setStatusCode("99");
            response.setStatusDescription("Error retrieving portfolio value: " + e.getMessage());
        }
        
        return response;
    }
    
    private boolean validateUserAccess(String corporateId, String userId) {
        try {
            // Validate if user has access to view portfolio
            // This would typically check database for user permissions
            // For now, we'll return true if both IDs are provided
            return corporateId != null && !corporateId.isEmpty() 
                && userId != null && !userId.isEmpty();
        } catch (Exception e) {
            System.err.println("Error validating user access: " + e.getMessage());
            return false;
        }
    }
    
    private String getBaseCurrency(String corporateId) {
        try {
            // Get base currency from MobileConfig
            String currency = MobileConfig.getCustomProperty("BASE_CURRENCY_" + corporateId);
            if (currency == null || currency.isEmpty()) {
                // Default to SAR if not configured
                currency = "SAR";
            }
            return currency;
        } catch (Exception e) {
            System.err.println("Error getting base currency: " + e.getMessage());
            return "SAR"; // Default currency
        }
    }
    
    private List<AccountBreakdown> getAccountBreakdown(String corporateId, String baseCurrency, String accessibleModules) {
        List<AccountBreakdown> accounts = new ArrayList<>();
        
        try {
            // Since account balances are not available, we'll calculate portfolio value
            // from trade finance transactions (LC, BG, DC) using the dashboard analytics query
            // filtered by accessible modules
            
            // Get trade finance totals filtered by accessible modules
            Map<String, BigDecimal> tradeFinanceTotals = getTradeFinanceTotals(corporateId, accessibleModules);
            
            // Letter of Credit portfolio - only if user has access to LC modules
            if (tradeFinanceTotals.containsKey("LC") && tradeFinanceTotals.get("LC").compareTo(BigDecimal.ZERO) > 0) {
                AccountBreakdown lcAccount = new AccountBreakdown();
                lcAccount.setAccountNumber("TF-LC-" + corporateId);
                lcAccount.setAccountType("Letters of Credit");
                lcAccount.setBalance(tradeFinanceTotals.get("LC").toPlainString());
                lcAccount.setCurrency(baseCurrency);
                accounts.add(lcAccount);
            }
            
            // Bank Guarantees portfolio
            if (tradeFinanceTotals.containsKey("BG") && tradeFinanceTotals.get("BG").compareTo(BigDecimal.ZERO) > 0) {
                AccountBreakdown bgAccount = new AccountBreakdown();
                bgAccount.setAccountNumber("TF-BG-" + corporateId);
                bgAccount.setAccountType("Bank Guarantees");
                bgAccount.setBalance(tradeFinanceTotals.get("BG").toPlainString());
                bgAccount.setCurrency(baseCurrency);
                accounts.add(bgAccount);
            }
            
            // Documentary Collections portfolio
            if (tradeFinanceTotals.containsKey("DC") && tradeFinanceTotals.get("DC").compareTo(BigDecimal.ZERO) > 0) {
                AccountBreakdown dcAccount = new AccountBreakdown();
                dcAccount.setAccountNumber("TF-DC-" + corporateId);
                dcAccount.setAccountType("Documentary Collections");
                dcAccount.setBalance(tradeFinanceTotals.get("DC").toPlainString());
                dcAccount.setCurrency(baseCurrency);
                accounts.add(dcAccount);
            }
            
            // Trade Loans portfolio
            if (tradeFinanceTotals.containsKey("TL") && tradeFinanceTotals.get("TL").compareTo(BigDecimal.ZERO) > 0) {
                AccountBreakdown tlAccount = new AccountBreakdown();
                tlAccount.setAccountNumber("TF-TL-" + corporateId);
                tlAccount.setAccountType("Trade Loans");
                tlAccount.setBalance(tradeFinanceTotals.get("TL").toPlainString());
                tlAccount.setCurrency(baseCurrency);
                accounts.add(tlAccount);
            }
            
            // If no trade finance data, return empty portfolio with clear message
            if (accounts.isEmpty()) {
                AccountBreakdown defaultAccount = new AccountBreakdown();
                defaultAccount.setAccountNumber("TF-" + corporateId);
                defaultAccount.setAccountType("No Active Trade Finance Transactions");
                defaultAccount.setBalance("0.00");
                defaultAccount.setCurrency(baseCurrency);
                accounts.add(defaultAccount);
                
                System.out.println("No active trade finance transactions found for corporateId: " + corporateId);
            }
            
        } catch (Exception e) {
            System.err.println("Error getting account breakdown: " + e.getMessage());
        }
        
        return accounts;
    }
    
    private BigDecimal calculateTotalValue(List<AccountBreakdown> accounts) {
        BigDecimal total = BigDecimal.ZERO;
        
        try {
            for (AccountBreakdown account : accounts) {
                if (account.getBalance() != null && !account.getBalance().isEmpty()) {
                    BigDecimal balance = new BigDecimal(account.getBalance());
                    total = total.add(balance);
                }
            }
        } catch (Exception e) {
            System.err.println("Error calculating total value: " + e.getMessage());
        }
        
        return total;
    }
    
    private Map<String, BigDecimal> getTradeFinanceTotals(String corporateId, String accessibleModules) {
        Map<String, BigDecimal> totals = new HashMap<>();
        
        try {
            // Format accessible modules for SQL IN clause
            String[] moduleArray = accessibleModules.split(",");
            StringBuilder moduleList = new StringBuilder();
            for (int i = 0; i < moduleArray.length; i++) {
                if (i > 0) moduleList.append(",");
                moduleList.append("'").append(moduleArray[i].trim()).append("'");
            }
            
            // Query to get current outstanding amounts by module - filtered by accessible modules
            // Using the same columns as DashboardAnalytics which is working
            String sqlQuery = "SELECT " +
                "C_MODULE, " +
                "SUM(NVL(LC_AMT, 0)) AS Total_LcAmt, " +
                "SUM(NVL(COLL_AMT, 0)) AS Total_CollAmt, " +
                "SUM(NVL(GTEE_AMT, 0)) AS Total_GteeAmt, " +
                "SUM(NVL(TL_AMT, 0)) AS Total_TlAmt, " +
                "COUNT(*) AS Transaction_Count " +
                "FROM CETRX.TRX_INBOX " +
                "WHERE C_UNIT_CODE = '" + corporateId + "' " +
                "AND C_MODULE IN (" + moduleList.toString() + ") " + // Filter by accessible modules
                "AND C_TRX_STATUS IN ('A', 'C') " + // Active or Current transactions
                "AND (EXPIRY_DT IS NULL OR EXPIRY_DT >= SYSDATE) " + // Not expired
                "GROUP BY C_MODULE";
            
            System.out.println("Portfolio Query: " + sqlQuery);
            SQLRecordSet recordSet = WSDBHelper.executeQuery(sqlQuery, secuDs);
            
            System.out.println("Portfolio Query Result - Success: " + recordSet.isSuccess() + 
                    ", Records: " + recordSet.getRecordCount());
            
            if (recordSet.isSuccess() && recordSet.getRecordCount() > 0) {
                for (int i = 0; i < recordSet.getRecordCount(); i++) {
                    String module = recordSet.getRecord(i).getValue("C_MODULE");
                    
                    System.out.println("Processing module: " + module);
                    System.out.println("  LC_AMT: " + recordSet.getRecord(i).getValue("Total_LcAmt"));
                    System.out.println("  GTEE_AMT: " + recordSet.getRecord(i).getValue("Total_GteeAmt"));
                    System.out.println("  COLL_AMT: " + recordSet.getRecord(i).getValue("Total_CollAmt"));
                    System.out.println("  TL_AMT: " + recordSet.getRecord(i).getValue("Total_TlAmt"));
                    
                    // Get the appropriate amount based on module type
                    BigDecimal amount = BigDecimal.ZERO;
                    
                    if ("LC".equals(module) || "IMLC".equals(module) || "EXLC".equals(module)) {
                        String lcAmtStr = recordSet.getRecord(i).getValue("Total_LcAmt");
                        amount = lcAmtStr != null && !lcAmtStr.isEmpty() ? new BigDecimal(lcAmtStr) : BigDecimal.ZERO;
                        
                        // Aggregate all LC types under "LC"
                        BigDecimal existingLC = totals.getOrDefault("LC", BigDecimal.ZERO);
                        totals.put("LC", existingLC.add(amount));
                    } else if ("BG".equals(module) || "OWGT".equals(module)) {
                        String gteeAmtStr = recordSet.getRecord(i).getValue("Total_GteeAmt");
                        amount = gteeAmtStr != null && !gteeAmtStr.isEmpty() ? new BigDecimal(gteeAmtStr) : BigDecimal.ZERO;
                        
                        // Aggregate all guarantee types under "BG"
                        BigDecimal existingBG = totals.getOrDefault("BG", BigDecimal.ZERO);
                        totals.put("BG", existingBG.add(amount));
                    } else if ("DC".equals(module) || "EXCO".equals(module)) {
                        String collAmtStr = recordSet.getRecord(i).getValue("Total_CollAmt");
                        amount = collAmtStr != null && !collAmtStr.isEmpty() ? new BigDecimal(collAmtStr) : BigDecimal.ZERO;
                        
                        // Aggregate all collection types under "DC"
                        BigDecimal existingDC = totals.getOrDefault("DC", BigDecimal.ZERO);
                        totals.put("DC", existingDC.add(amount));
                    } else if ("TL".equals(module)) {
                        String tlAmtStr = recordSet.getRecord(i).getValue("Total_TlAmt");
                        amount = tlAmtStr != null && !tlAmtStr.isEmpty() ? new BigDecimal(tlAmtStr) : BigDecimal.ZERO;
                        totals.put("TL", amount);
                    }
                }
            }
            
            // If no data found, try to get from dashboard analytics summary
            if (totals.isEmpty()) {
                totals = getTradeFinanceTotalsFromSummary(corporateId, accessibleModules);
            }
            
        } catch (Exception e) {
            System.err.println("Error getting trade finance totals: " + e.getMessage());
            // Return empty totals on error
        }
        
        return totals;
    }
    
    private Map<String, BigDecimal> getTradeFinanceTotalsFromSummary(String corporateId, String accessibleModules) {
        Map<String, BigDecimal> totals = new HashMap<>();
        
        try {
            // Format accessible modules for SQL IN clause
            String[] moduleArray = accessibleModules.split(",");
            StringBuilder moduleList = new StringBuilder();
            for (int i = 0; i < moduleArray.length; i++) {
                if (i > 0) moduleList.append(",");
                moduleList.append("'").append(moduleArray[i].trim()).append("'");
            }
            
            // Alternative query using summary data for current year - filtered by accessible modules
            String sqlQuery = "SELECT " +
                "COALESCE(SUM(NVL(LC_AMT, 0)), 0) AS TOTAL_LC, " +
                "COALESCE(SUM(NVL(COLL_AMT, 0)), 0) AS TOTAL_DC, " +
                "COALESCE(SUM(NVL(GTEE_AMT, 0)), 0) AS TOTAL_BG, " +
                "COALESCE(SUM(CASE " +
                "    WHEN C_MODULE IN ('LC', 'IMLC', 'EXLC') THEN NVL(LC_AMT, 0) " +
                "    WHEN C_MODULE IN ('BG', 'OWGT', 'IWGT') THEN NVL(GTEE_AMT, 0) " +
                "    WHEN C_MODULE IN ('DC', 'EXCO', 'IMCO') THEN NVL(COLL_AMT, 0) " +
                "    ELSE 0 END), 0) AS TOTAL_ALL " +
                "FROM CETRX.TRX_INBOX " +
                "WHERE C_UNIT_CODE = '" + corporateId + "' " +
                "AND C_MODULE IN (" + moduleList.toString() + ") " + // Filter by accessible modules
                "AND EXTRACT(YEAR FROM TRX_DT) = EXTRACT(YEAR FROM SYSDATE)";
            
            System.out.println("Summary Portfolio Query: " + sqlQuery);
            
            SQLRecordSet recordSet = WSDBHelper.executeQuery(sqlQuery, secuDs);
            
            if (recordSet.isSuccess() && recordSet.getRecordCount() > 0) {
                String lcTotal = recordSet.getRecord(0).getValue("TOTAL_LC");
                String dcTotal = recordSet.getRecord(0).getValue("TOTAL_DC");
                String bgTotal = recordSet.getRecord(0).getValue("TOTAL_BG");
                String tlTotal = recordSet.getRecord(0).getValue("TOTAL_TL");
                
                if (lcTotal != null && !lcTotal.isEmpty() && !"0".equals(lcTotal)) {
                    totals.put("LC", new BigDecimal(lcTotal));
                }
                if (dcTotal != null && !dcTotal.isEmpty() && !"0".equals(dcTotal)) {
                    totals.put("DC", new BigDecimal(dcTotal));
                }
                if (bgTotal != null && !bgTotal.isEmpty() && !"0".equals(bgTotal)) {
                    totals.put("BG", new BigDecimal(bgTotal));
                }
                if (tlTotal != null && !tlTotal.isEmpty() && !"0".equals(tlTotal)) {
                    totals.put("TL", new BigDecimal(tlTotal));
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error getting trade finance summary totals: " + e.getMessage());
        }
        
        return totals;
    }
}