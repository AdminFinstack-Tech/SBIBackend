# Backend Changes Required to Remove Static Values

## Summary
The SAR 250,000.00 value appearing in the dashboard is coming from example/mock data in the backend. Here are the changes made and additional changes needed to ensure the backend returns real data from the database.

## Changes Made

### 1. TransactionAnalyticsApiService.java
**File:** `/backend/src/main/java/com/csme/csmeapi/fin/services/TransactionAnalyticsApiService.java`

**Changes:**
- Removed hardcoded values in `generateTotalValue()` method (was returning 25000000.0)
- Removed hardcoded values in `generatePeriodAnalysis()` method
- Removed hardcoded module statistics values

**Before:**
```java
return moduleValues.getOrDefault(module, 25000000.0);
Double[] values = {750000.0, 3200000.0, 8900000.0, 25000000.0};
Double[] values = {12500000.0, 8750000.0, 6200000.0, 4100000.0, 3500000.0, 9800000.0};
```

**After:**
```java
return 0.0; // Should query actual data from database
Double[] values = {0.0, 0.0, 0.0, 0.0};
```

### 2. DashboardAnalytics.xml
**File:** `/backend/MOBILE/DashboardAnalytics.xml`

**Changes:**
- Fixed hardcoded unit code in SQL queries
- Added module filtering support
- Added transaction count to queries

**Before:**
```sql
AND C_UNIT_CODE = '2587457'
```

**After:**
```sql
AND C_UNIT_CODE = '${unitcode}'
AND C_MODULE IN (${modules})
COUNT(*) AS Transaction_Count
```

## Additional Changes Needed

### 1. Verify Database Connection
- Ensure the database connection is working properly
- Check if `CETRX.TRX_INBOX` table has data for the corporate IDs being tested
- Verify user has access to view the data

### 2. Update PortfolioValueApiService
The PortfolioValueApiService is correctly implemented but needs:
- Better error handling when no data is found
- Logging to debug why queries might return empty results
- Ensure it never falls back to example values

### 3. Remove Example Values from API Specifications
**File:** `/backend/src/main/resources/FinMobileAPI.yaml`

The YAML file contains example values that might be returned:
```yaml
TotalValue:
  type: string
  description: Total portfolio value
  example: "250000.00"  # This should be removed or changed to "0.00"
```

### 4. Add Configuration for Test Mode
Create a configuration property to control mock data:
```properties
# application.properties
app.use.mock.data=false
app.enable.test.mode=false
```

### 5. Implement Proper Data Loading
Update all services to:
1. Query real data from database
2. Return empty/zero values if no data exists
3. Never return hardcoded example values
4. Add proper logging for debugging

## Testing Steps

1. **Test with Postman:**
   - Use the provided Postman collection
   - Check if APIs return real data or zeros (not 250000)
   - Verify error messages when no data exists

2. **Database Verification:**
   ```sql
   -- Check if data exists for the corporate ID
   SELECT COUNT(*) FROM CETRX.TRX_INBOX WHERE C_UNIT_CODE = '0001517524';
   
   -- Check portfolio values
   SELECT 
     C_MODULE,
     SUM(LC_AMT) AS LC_TOTAL,
     SUM(GTEE_AMT) AS GTEE_TOTAL,
     SUM(COLL_AMT) AS COLL_TOTAL
   FROM CETRX.TRX_INBOX 
   WHERE C_UNIT_CODE = '0001517524'
   GROUP BY C_MODULE;
   ```

3. **Enable Debug Logging:**
   Add to `log4j.properties`:
   ```properties
   log4j.logger.com.csme.csmeapi.fin.services=DEBUG
   ```

## Expected Behavior After Changes

1. **When data exists:** APIs return actual values from database
2. **When no data exists:** APIs return 0 or empty values, not 250000
3. **On error:** APIs return proper error messages, not example values
4. **Portfolio Value:** Should sum up actual trade finance amounts from database

## Important Notes

- The frontend (Flutter app) is correctly implemented and will display whatever the backend returns
- The issue is entirely in the backend returning example/mock values
- All SQL queries must use parameterized values, never hardcoded corporate IDs
- Always filter by user's accessible modules for security