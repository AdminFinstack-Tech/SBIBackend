package com.csme.csmeapi.fin.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csme.csmeapi.fin.models.XPortfolioValueRequest;
import com.csme.csmeapi.fin.models.XPortfolioValueResponse;
import com.csme.csmeapi.fin.models.XPortfolioValueResponse.AccountBreakdown;
import com.csme.csmeapi.mobile.service.MobileAppCommonDao;
import com.csme.csmeapi.mobile.util.MobileConfig;
import com.csme.csmeapi.mobile.util.MobileUtil;

@Service
public class PortfolioValueApiService {
    
    @Autowired
    private MobileAppCommonDao mobileAppCommonDao;
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    
    public XPortfolioValueResponse getPortfolioValue(XPortfolioValueRequest request, String requestId) 
            throws Exception {
        
        XPortfolioValueResponse response = new XPortfolioValueResponse();
        
        try {
            // Set basic response fields
            response.setMessageId(String.valueOf(System.currentTimeMillis()));
            response.setRequestId(requestId);
            response.setTimestamp(DATE_FORMAT.format(new Date()));
            
            // Validate user access
            boolean hasAccess = validateUserAccess(request.getCorporateId(), request.getUserId());
            
            if (!hasAccess) {
                response.setStatusCode("01");
                response.setStatusDescription("User does not have access to portfolio data");
                return response;
            }
            
            // Get portfolio data
            String baseCurrency = getBaseCurrency(request.getCorporateId());
            response.setCurrency(baseCurrency);
            response.setAsOfDate(DATE_FORMAT.format(new Date()));
            
            // Get account breakdown and calculate total
            List<AccountBreakdown> accounts = getAccountBreakdown(request.getCorporateId(), baseCurrency);
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
            String currency = MobileConfig.getProperty("BASE_CURRENCY_" + corporateId);
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
    
    private List<AccountBreakdown> getAccountBreakdown(String corporateId, String baseCurrency) {
        List<AccountBreakdown> accounts = new ArrayList<>();
        
        try {
            // In a real implementation, this would query the database for actual account balances
            // For now, we'll return sample data
            
            // Sample account 1 - Current Account
            AccountBreakdown account1 = new AccountBreakdown();
            account1.setAccountNumber("SA" + corporateId + "001");
            account1.setAccountType("Current Account");
            account1.setBalance("125000.00");
            account1.setCurrency(baseCurrency);
            accounts.add(account1);
            
            // Sample account 2 - Savings Account
            AccountBreakdown account2 = new AccountBreakdown();
            account2.setAccountNumber("SA" + corporateId + "002");
            account2.setAccountType("Savings Account");
            account2.setBalance("75000.00");
            account2.setCurrency(baseCurrency);
            accounts.add(account2);
            
            // Sample account 3 - Investment Account
            AccountBreakdown account3 = new AccountBreakdown();
            account3.setAccountNumber("SA" + corporateId + "003");
            account3.setAccountType("Investment Account");
            account3.setBalance("50000.00");
            account3.setCurrency(baseCurrency);
            accounts.add(account3);
            
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
}