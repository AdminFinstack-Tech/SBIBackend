# Portfolio Value Implementation Without Account Data

## Current Situation
- No account-related tables available in the database
- Portfolio value must be calculated from trade finance transactions
- Table available: `CETRX.TRX_INBOX` with trade finance data

## Recommended Approach

### 1. Calculate Portfolio Value from Outstanding Trade Finance Obligations

Since we don't have account balances, the portfolio value represents the total outstanding trade finance obligations:

```sql
-- Total Portfolio Value Query
SELECT 
    COALESCE(SUM(
        CASE 
            WHEN C_MODULE IN ('LC', 'IMLC', 'EXLC') THEN NVL(LC_AMT, 0)
            WHEN C_MODULE IN ('BG', 'OWGT', 'IWGT') THEN NVL(GTEE_AMT, 0)
            WHEN C_MODULE IN ('DC', 'EXCO', 'IMCO') THEN NVL(COLL_AMT, 0)
            ELSE 0
        END
    ), 0) AS TOTAL_PORTFOLIO_VALUE
FROM CETRX.TRX_INBOX
WHERE C_UNIT_CODE = '${corporateId}'
    AND C_MODULE IN (${modules})
    AND C_TRX_STATUS IN ('A', 'C', 'P')  -- Active, Current, Pending
    AND (EXPIRY_DT IS NULL OR EXPIRY_DT >= SYSDATE);
```

### 2. Update PortfolioValueApiService.java

```java
private Map<String, BigDecimal> getTradeFinanceTotals(String corporateId, String accessibleModules) {
    Map<String, BigDecimal> totals = new HashMap<>();
    
    try {
        // Format modules for SQL
        String moduleList = formatModulesForSQL(accessibleModules);
        
        // Single query to get all totals
        String sqlQuery = "SELECT " +
            "COALESCE(SUM(CASE WHEN C_MODULE IN ('LC', 'IMLC', 'EXLC') THEN NVL(LC_AMT, 0) ELSE 0 END), 0) AS TOTAL_LC, " +
            "COALESCE(SUM(CASE WHEN C_MODULE IN ('BG', 'OWGT', 'IWGT') THEN NVL(GTEE_AMT, 0) ELSE 0 END), 0) AS TOTAL_BG, " +
            "COALESCE(SUM(CASE WHEN C_MODULE IN ('DC', 'EXCO', 'IMCO') THEN NVL(COLL_AMT, 0) ELSE 0 END), 0) AS TOTAL_DC, " +
            "COALESCE(SUM(CASE " +
            "    WHEN C_MODULE IN ('LC', 'IMLC', 'EXLC') THEN NVL(LC_AMT, 0) " +
            "    WHEN C_MODULE IN ('BG', 'OWGT', 'IWGT') THEN NVL(GTEE_AMT, 0) " +
            "    WHEN C_MODULE IN ('DC', 'EXCO', 'IMCO') THEN NVL(COLL_AMT, 0) " +
            "    ELSE 0 END), 0) AS TOTAL_VALUE " +
            "FROM CETRX.TRX_INBOX " +
            "WHERE C_UNIT_CODE = ? " +
            "AND C_MODULE IN (" + moduleList + ") " +
            "AND C_TRX_STATUS IN ('A', 'C', 'P') " +
            "AND (EXPIRY_DT IS NULL OR EXPIRY_DT >= SYSDATE)";
        
        // Use parameterized query
        PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
        pstmt.setString(1, corporateId);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            BigDecimal totalLC = rs.getBigDecimal("TOTAL_LC");
            BigDecimal totalBG = rs.getBigDecimal("TOTAL_BG");
            BigDecimal totalDC = rs.getBigDecimal("TOTAL_DC");
            BigDecimal totalValue = rs.getBigDecimal("TOTAL_VALUE");
            
            if (totalLC.compareTo(BigDecimal.ZERO) > 0) {
                totals.put("LC", totalLC);
            }
            if (totalBG.compareTo(BigDecimal.ZERO) > 0) {
                totals.put("BG", totalBG);
            }
            if (totalDC.compareTo(BigDecimal.ZERO) > 0) {
                totals.put("DC", totalDC);
            }
            totals.put("TOTAL", totalValue);
        }
        
    } catch (Exception e) {
        log.error("Error calculating portfolio value: " + e.getMessage());
    }
    
    return totals;
}
```

### 3. Alternative Approach - Show Transaction Summary

If no outstanding amounts exist, show a transaction summary instead:

```java
public XPortfolioValueResponse getPortfolioValue(XPortfolioValueRequest request, String requestId) {
    XPortfolioValueResponse response = new XPortfolioValueResponse();
    
    try {
        // ... existing setup code ...
        
        // Get trade finance totals
        Map<String, BigDecimal> totals = getTradeFinanceTotals(
            request.getCorporateId(), 
            accessibleModules
        );
        
        // If no outstanding amounts, get historical summary
        if (totals.isEmpty() || totals.get("TOTAL").compareTo(BigDecimal.ZERO) == 0) {
            totals = getHistoricalTransactionSummary(
                request.getCorporateId(), 
                accessibleModules
            );
            response.setAsOfDate("Historical Transaction Summary");
        }
        
        // Set total value
        BigDecimal totalValue = totals.getOrDefault("TOTAL", BigDecimal.ZERO);
        response.setTotalValue(totalValue.toPlainString());
        
        // Build account breakdown
        List<AccountBreakdown> breakdown = new ArrayList<>();
        if (totals.containsKey("LC")) {
            AccountBreakdown lcBreakdown = new AccountBreakdown();
            lcBreakdown.setAccountType("Letters of Credit");
            lcBreakdown.setBalance(totals.get("LC").toPlainString());
            lcBreakdown.setCurrency(baseCurrency);
            breakdown.add(lcBreakdown);
        }
        // ... similar for BG, DC ...
        
        response.setAccountBreakdown(breakdown);
        response.setStatusCode("00");
        response.setStatusDescription("Success");
        
    } catch (Exception e) {
        response.setStatusCode("99");
        response.setStatusDescription("Error: " + e.getMessage());
    }
    
    return response;
}
```

### 4. Historical Summary Query

For cases where there are no outstanding amounts:

```sql
-- Historical transaction summary (last 12 months)
SELECT 
    'Historical Summary' AS ACCOUNT_TYPE,
    COUNT(*) AS TRANSACTION_COUNT,
    COALESCE(SUM(
        CASE 
            WHEN C_MODULE IN ('LC', 'IMLC', 'EXLC') THEN NVL(LC_AMT, 0)
            WHEN C_MODULE IN ('BG', 'OWGT', 'IWGT') THEN NVL(GTEE_AMT, 0)
            WHEN C_MODULE IN ('DC', 'EXCO', 'IMCO') THEN NVL(COLL_AMT, 0)
            ELSE 0
        END
    ), 0) AS TOTAL_AMOUNT
FROM CETRX.TRX_INBOX
WHERE C_UNIT_CODE = '${corporateId}'
    AND C_MODULE IN (${modules})
    AND TRX_DT >= ADD_MONTHS(SYSDATE, -12);
```

## Key Points

1. **No Mock Data**: Always return actual values from database or 0
2. **Clear Status**: Indicate whether showing outstanding or historical data
3. **Module Filtering**: Only show data for modules user has access to
4. **Error Handling**: Return empty portfolio with 0 value on errors
5. **Logging**: Add detailed logging to debug issues

## Testing SQL

To verify data exists:

```sql
-- Check what data exists
SELECT 
    C_MODULE,
    C_TRX_STATUS,
    COUNT(*) as COUNT,
    SUM(NVL(LC_AMT, 0)) as LC_TOTAL,
    SUM(NVL(GTEE_AMT, 0)) as GTEE_TOTAL,
    SUM(NVL(COLL_AMT, 0)) as COLL_TOTAL
FROM CETRX.TRX_INBOX
WHERE C_UNIT_CODE = '0001517524'
GROUP BY C_MODULE, C_TRX_STATUS
ORDER BY C_MODULE, C_TRX_STATUS;
```

This approach ensures the portfolio value API returns meaningful data based on trade finance transactions when account data is not available.