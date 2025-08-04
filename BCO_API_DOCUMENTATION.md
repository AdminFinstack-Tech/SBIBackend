# BCO (Bank Credit Operations) API Documentation

## Overview
The BCO API provides endpoints for managing Bank Credit Operations including DSO referrals, CAA (Credit Approval Authority) transactions, and OR (Operational Risk) assessments.

## API Endpoints

### 1. BCO Inquiry
**Endpoint:** `POST /api/bco/inquire`

**Description:** Retrieve BCO transactions based on various filters

**Request Headers:**
- `Request-Id`: UUID (required)
- `Sequence`: Long (required)
- `Authorization`: Bearer token (required)

**Request Body:**
```json
{
  "corporateId": "string",
  "userId": "string",
  "transactionType": "DSO_REFERAL | CAA | OR",
  "status": "string",
  "fromDate": "yyyy-MM-dd",
  "toDate": "yyyy-MM-dd",
  "branchCode": "string",
  "customerId": "string",
  "referenceNo": "string",
  "pageNumber": 1,
  "pageSize": 10
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "BCO inquiry completed successfully",
  "totalRecords": 100,
  "pageNumber": 1,
  "pageSize": 10,
  "transactions": [
    {
      "referenceNo": "BCO2024001",
      "transactionType": "DSO_REFERAL",
      "customerName": "ABC Corporation",
      "customerId": "CUST001",
      "branchCode": "BR001",
      "branchName": "Main Branch",
      "observationDate": "2024-01-15",
      "status": "PENDING",
      "amount": "50000.00",
      "currency": "INR",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00",
      "reasonForReferal": "Credit limit exceeded",
      "bcoRemarks": "Under review",
      "approvalStatus": "PENDING",
      "approvedBy": null,
      "approvedDate": null
    }
  ]
}
```

### 2. DSO Referral
**Endpoint:** `POST /api/bco/dso-referal`

**Description:** Submit a DSO (Designated Sanctioning Officer) referral for BCO review

**Request Headers:**
- `Request-Id`: UUID (required)
- `Sequence`: Long (required)
- `Authorization`: Bearer token (required)

**Request Body:**
```json
{
  "corporateId": "string",
  "userId": "string",
  "customerId": "string",
  "branchCode": "string",
  "referenceNo": "string"
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "DSO referral processed successfully",
  "totalRecords": 1,
  "transactions": []
}
```

## Database Schema

### BCO_TRANSACTIONS Table
```sql
CREATE TABLE BCO_TRANSACTIONS (
    REF_NO VARCHAR(50) PRIMARY KEY,
    TRX_TYPE VARCHAR(20),
    C_UNIT_CODE VARCHAR(20),
    CUSTOMER_NAME VARCHAR(100),
    CUSTOMER_ID VARCHAR(50),
    BRANCH_CODE VARCHAR(20),
    BRANCH_NAME VARCHAR(100),
    OBS_DATE DATE,
    STATUS VARCHAR(20),
    AMOUNT DECIMAL(18,2),
    CURRENCY VARCHAR(3),
    CREATED_DATE TIMESTAMP,
    UPDATED_DATE TIMESTAMP,
    REASON_FOR_REFERAL VARCHAR(500),
    BCO_REMARKS VARCHAR(500),
    APPROVAL_STATUS VARCHAR(20),
    APPROVED_BY VARCHAR(50),
    APPROVED_DATE TIMESTAMP
);
```

### BCO_DSO_REFERALS Table
```sql
CREATE TABLE BCO_DSO_REFERALS (
    REF_NO VARCHAR(50) PRIMARY KEY,
    TRX_TYPE VARCHAR(20),
    C_UNIT_CODE VARCHAR(20),
    CUSTOMER_NAME VARCHAR(100),
    CUSTOMER_ID VARCHAR(50),
    BRANCH_CODE VARCHAR(20),
    BRANCH_NAME VARCHAR(100),
    OBS_DATE DATE,
    STATUS VARCHAR(20),
    AMOUNT DECIMAL(18,2),
    CURRENCY VARCHAR(3),
    REASON_FOR_REFERAL VARCHAR(500),
    BCO_REMARKS VARCHAR(500),
    APPROVAL_STATUS VARCHAR(20),
    CREATED_BY VARCHAR(50),
    CREATED_DATE TIMESTAMP,
    APPROVED_BY VARCHAR(50),
    APPROVED_DATE TIMESTAMP,
    UPDATED_DATE TIMESTAMP
);
```

## File Structure

```
backend/
├── src/main/java/com/csme/csmeapi/
│   ├── api/
│   │   ├── BCOInquireApi.java                    # API Interface
│   │   └── controller/
│   │       └── BCOInquireApiController.java      # Controller Implementation
│   └── fin/
│       ├── models/
│       │   ├── XBCOInquireRequest.java          # Request Model
│       │   ├── XBCOInquireResponse.java         # Response Model
│       │   └── XBCOTransaction.java             # Transaction Model
│       └── services/
│           └── BCOInquireApiService.java        # Service Implementation
└── MOBILE/
    └── BCOInquiry.xml                            # SQL Configuration

```

## Integration with Flutter App

The Flutter app has corresponding BCO screens that consume these APIs:

1. **BCO Main Screen** (`lib/ui/bco/bco_main_screen.dart`)
   - Lists all BCO transactions
   - Uses `/api/bco/inquire` endpoint

2. **DSO Referral Screen** (`lib/ui/bco/dso_referral_screen.dart`)
   - Handles DSO referral submissions
   - Uses `/api/bco/dso-referal` endpoint

3. **CAA Screen** (`lib/ui/bco/caa_screen.dart`)
   - Manages Credit Approval Authority transactions
   - Filters by `transactionType: "CAA"`

4. **OR Screen** (`lib/ui/bco/or_screen.dart`)
   - Handles Operational Risk assessments
   - Filters by `transactionType: "OR"`

## Testing with Postman

Import the following collection to test the BCO APIs:

```json
{
  "info": {
    "name": "BCO API Collection",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "BCO Inquiry",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Request-Id",
            "value": "{{$guid}}"
          },
          {
            "key": "Sequence",
            "value": "1"
          },
          {
            "key": "Authorization",
            "value": "Bearer {{token}}"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"corporateId\": \"0001517524\",\n  \"userId\": \"USER001\",\n  \"pageNumber\": 1,\n  \"pageSize\": 10\n}",
          "options": {
            "raw": {
              "language": "json"
            }
          }
        },
        "url": {
          "raw": "{{baseUrl}}/api/bco/inquire",
          "host": ["{{baseUrl}}"],
          "path": ["api", "bco", "inquire"]
        }
      }
    }
  ]
}
```

## Security Considerations

1. All endpoints require JWT authentication
2. Corporate ID validation ensures data isolation
3. User permissions are checked at the service layer
4. SQL injection prevention through parameterized queries
5. XML configuration uses CDATA for SQL statements

## Future Enhancements

1. Add approval workflow for DSO referrals
2. Implement email notifications for status changes
3. Add audit trail for all BCO operations
4. Integrate with risk assessment systems
5. Add dashboard analytics for BCO metrics

## Support

For issues or questions regarding the BCO API, please contact the development team or raise an issue in the GitHub repository.