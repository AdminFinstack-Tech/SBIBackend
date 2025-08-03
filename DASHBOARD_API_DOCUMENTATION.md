# Banking Dashboard API Documentation

## Overview
The Banking Dashboard APIs provide analytics and metrics for the mobile banking application. These APIs connect to the backend database to fetch real-time transaction data, portfolio values, and performance metrics.

## Base URL
```
http://213.42.43.227/cs-ce-sec-api-0.0.1
```

## Authentication Headers
All API requests require the following headers:

| Header | Required | Description |
|--------|----------|-------------|
| `Content-Type` | Yes | `application/json` |
| `requestId` | Yes | Unique request ID (UUID format) |
| `SecertKey` | No | Authentication key: `FinMobileCS` |
| `channel` | Yes | Channel identifier: `FinMobileBankingApp` |
| `timeStamp` | Yes | Unix timestamp |
| `version` | No | API version: `1.0.0` |

## API Endpoints

### 1. Dashboard Analytics
**Endpoint:** `/dashboardAnalytics`  
**Method:** `POST`  
**Description:** Retrieves comprehensive dashboard analytics including module statistics, performance metrics, and summary data.

**Request Body:**
```json
{
    "CorporateId": "0001517524",
    "UserId": "1517524adm3", 
    "Period": "current"
}
```

**Response Structure:**
```json
{
    "StatusCode": "00",
    "StatusDescription": "Success",
    "ModuleStatistics": [
        {
            "Module": "LC",
            "Year": "2024",
            "Month": "Jan",
            "TotalLcAmt": 1500000,
            "TotalCollAmt": 0,
            "TotalGteeAmt": 0,
            "TransactionCount": 25
        }
    ],
    "PerformanceMetrics": [
        {
            "MetricName": "Transaction Volume",
            "CurrentValue": 150,
            "PreviousValue": 120,
            "GrowthPercentage": 25.0
        }
    ],
    "SummaryMetrics": {
        "TotalTransactions": 450,
        "TotalLcAmount": 5000000,
        "TotalCollAmount": 2000000,
        "TotalGteeAmount": 3000000
    }
}
```

### 2. Module Analytics
**Endpoint:** `/moduleAnalytics`  
**Method:** `POST`  
**Description:** Retrieves analytics for specific trade finance modules (LC, BG, DC, TL).

**Request Body:**
```json
{
    "CorporateId": "0001517524",
    "UserId": "1517524adm3",
    "Module": "LC",
    "Period": "12m"
}
```

**Available Modules:**
- `LC` - Letters of Credit
- `BG` - Bank Guarantees  
- `DC` - Documentary Collections
- `TL` - Trade Loans
- `IMLC` - Import LC
- `EXLC` - Export LC
- `OWGT` - Outward Guarantee

### 3. Portfolio Value
**Endpoint:** `/portfolioValue`  
**Method:** `POST`  
**Description:** Retrieves total portfolio value with account balances and distribution.

**Request Body:**
```json
{
    "CorporateId": "0001517524",
    "UserId": "1517524adm3"
}
```

**Response Structure:**
```json
{
    "StatusCode": "00",
    "TotalValue": 15000000,
    "Currency": "SAR",
    "GrowthPercentage": 12.5,
    "AccountBalances": [
        {
            "AccountNumber": "1234567890",
            "AccountType": "Current Account",
            "AccountName": "Main Operating Account",
            "Currency": "SAR",
            "Balance": 5000000,
            "Status": "Active"
        }
    ],
    "AccountTypeDistribution": [
        {
            "AccountType": "Current Account",
            "AccountCount": 3,
            "TotalBalance": 8000000
        }
    ]
}
```

### 4. Beneficiary Analytics
**Endpoint:** `/beneficiaryAnalytics`  
**Method:** `POST`  
**Description:** Retrieves top beneficiaries by transaction count and amount.

**Request Body:**
```json
{
    "CorporateId": "0001517524",
    "UserId": "1517524adm3",
    "Months": 6
}
```

### 5. Performance Summary
**Endpoint:** `/performanceSummary`  
**Method:** `POST`  
**Description:** Retrieves performance summary with key metrics and trends.

**Request Body:**
```json
{
    "CorporateId": "0001517524",
    "UserId": "1517524adm3",
    "Period": "current"
}
```

## Database Queries

The APIs execute SQL queries against the following tables:
- `CETRX.TRX_INBOX` - Transaction data
- `CEACC.ACC_MASTER` - Account master data
- `CETRX.EXCHANGE_RATES` - Currency exchange rates

## Error Responses

**Duplicate Request:**
```json
{
    "StatusCode": "400",
    "StatusDescription": "Duplicate request ID"
}
```

**Internal Server Error:**
```json
{
    "StatusCode": "500",
    "StatusDescription": "Internal server error: [error details]"
}
```

## Testing with Postman

1. Import the `Banking_Dashboard_APIs.postman_collection.json` file into Postman
2. The collection uses Postman variables for dynamic values:
   - `{{$guid}}` - Generates unique request ID
   - `{{$timestamp}}` - Current Unix timestamp
3. Update the request body with valid Corporate ID and User ID
4. Execute requests in sequence to test the dashboard flow

## Notes

- All amounts are returned in the base currency (SAR by default)
- The APIs implement audit logging for all requests
- Request IDs must be unique to prevent duplicate processing
- The backend uses Oracle database with specific schemas (CETRX, CEACC)