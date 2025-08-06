# BCO SDK Technical Documentation

## Table of Contents
1. [Overview](#overview)
2. [Architecture](#architecture)
3. [SDK Integration](#sdk-integration)
4. [API Reference](#api-reference)
5. [Authentication](#authentication)
6. [Data Models](#data-models)
7. [Error Handling](#error-handling)
8. [Code Examples](#code-examples)
9. [Security](#security)
10. [Performance](#performance)

## Overview

The BCO (Bank Credit Operations) SDK provides a comprehensive interface for integrating bank credit operations functionality into third-party applications. It supports DSO referrals, CAA transactions, and operational risk assessments.

### Key Features
- RESTful API architecture
- JWT-based authentication
- Real-time transaction processing
- Pagination and filtering support
- Multi-language support
- Webhook notifications
- Audit trail logging

### Version Information
- **Current Version**: 1.0.0
- **API Version**: v1
- **Last Updated**: January 2025

## Architecture

### System Architecture
```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│                 │     │                 │     │                 │
│  Client App     │────▶│   BCO SDK       │────▶│  BCO Backend    │
│                 │     │                 │     │                 │
└─────────────────┘     └─────────────────┘     └─────────────────┘
         │                       │                       │
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  UI Components  │     │  HTTP Client    │     │   Database      │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

### Component Overview
1. **Client Application**: Your application integrating BCO functionality
2. **BCO SDK**: Provides APIs and utilities for BCO operations
3. **BCO Backend**: Core processing engine
4. **Database**: Persistent storage for transactions

## SDK Integration

### Installation

#### Maven
```xml
<dependency>
    <groupId>com.sbi.bco</groupId>
    <artifactId>bco-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### Gradle
```gradle
implementation 'com.sbi.bco:bco-sdk:1.0.0'
```

#### NPM (JavaScript/TypeScript)
```bash
npm install @sbi/bco-sdk
```

#### Flutter/Dart
```yaml
dependencies:
  bco_sdk: ^1.0.0
```

### Quick Start

#### Java Example
```java
import com.sbi.bco.sdk.BCOClient;
import com.sbi.bco.sdk.models.*;

public class BCOIntegration {
    public static void main(String[] args) {
        // Initialize BCO Client
        BCOClient client = BCOClient.builder()
            .baseUrl("https://api.sbi.com/bco/v1")
            .apiKey("your-api-key")
            .apiSecret("your-api-secret")
            .build();
        
        // Create inquiry request
        BCOInquireRequest request = BCOInquireRequest.builder()
            .corporateId("0001517524")
            .userId("USER001")
            .transactionType("DSO_REFERAL")
            .pageNumber(1)
            .pageSize(10)
            .build();
        
        // Execute inquiry
        BCOInquireResponse response = client.inquire(request);
        
        // Process results
        response.getTransactions().forEach(transaction -> {
            System.out.println("Reference: " + transaction.getReferenceNo());
            System.out.println("Status: " + transaction.getStatus());
        });
    }
}
```

#### JavaScript/TypeScript Example
```typescript
import { BCOClient, TransactionType } from '@sbi/bco-sdk';

async function integrateBC0() {
    // Initialize client
    const client = new BCOClient({
        baseUrl: 'https://api.sbi.com/bco/v1',
        apiKey: 'your-api-key',
        apiSecret: 'your-api-secret'
    });
    
    // Create inquiry request
    const request = {
        corporateId: '0001517524',
        userId: 'USER001',
        transactionType: TransactionType.DSO_REFERAL,
        pageNumber: 1,
        pageSize: 10
    };
    
    try {
        // Execute inquiry
        const response = await client.inquire(request);
        
        // Process results
        response.transactions.forEach(transaction => {
            console.log(`Reference: ${transaction.referenceNo}`);
            console.log(`Status: ${transaction.status}`);
        });
    } catch (error) {
        console.error('BCO Error:', error);
    }
}
```

#### Flutter/Dart Example
```dart
import 'package:bco_sdk/bco_sdk.dart';

class BCOIntegration {
  final BCOClient client = BCOClient(
    baseUrl: 'https://api.sbi.com/bco/v1',
    apiKey: 'your-api-key',
    apiSecret: 'your-api-secret',
  );
  
  Future<void> fetchBCOTransactions() async {
    try {
      // Create inquiry request
      final request = BCOInquireRequest(
        corporateId: '0001517524',
        userId: 'USER001',
        transactionType: TransactionType.dsoReferal,
        pageNumber: 1,
        pageSize: 10,
      );
      
      // Execute inquiry
      final response = await client.inquire(request);
      
      // Process results
      for (final transaction in response.transactions) {
        print('Reference: ${transaction.referenceNo}');
        print('Status: ${transaction.status}');
      }
    } catch (e) {
      print('BCO Error: $e');
    }
  }
}
```

## API Reference

### Base URL
```
Production: https://api.sbi.com/bco/v1
Staging: https://api-staging.sbi.com/bco/v1
Development: https://api-dev.sbi.com/bco/v1
```

### Endpoints

#### 1. BCO Inquiry
```http
POST /inquire
Content-Type: application/json
Authorization: Bearer {token}
Request-Id: {uuid}
Sequence: {number}

{
  "corporateId": "string",
  "userId": "string",
  "transactionType": "string",
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

**Response**
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
      "createdAt": "2024-01-15T10:30:00Z",
      "updatedAt": "2024-01-15T10:30:00Z",
      "reasonForReferal": "Credit limit exceeded",
      "bcoRemarks": "Under review",
      "approvalStatus": "PENDING",
      "approvedBy": null,
      "approvedDate": null
    }
  ]
}
```

#### 2. DSO Referral Submission
```http
POST /dso-referal
Content-Type: application/json
Authorization: Bearer {token}
Request-Id: {uuid}
Sequence: {number}

{
  "corporateId": "string",
  "userId": "string",
  "customerId": "string",
  "customerName": "string",
  "branchCode": "string",
  "branchName": "string",
  "amount": "string",
  "currency": "string",
  "reasonForReferal": "string",
  "observationDate": "yyyy-MM-dd",
  "attachments": [
    {
      "fileName": "string",
      "fileType": "string",
      "fileData": "base64-encoded-string"
    }
  ]
}
```

#### 3. Update Transaction Status
```http
PUT /transaction/{referenceNo}/status
Content-Type: application/json
Authorization: Bearer {token}

{
  "status": "APPROVED | REJECTED | PENDING",
  "remarks": "string",
  "approvedBy": "string"
}
```

#### 4. Get Transaction Details
```http
GET /transaction/{referenceNo}
Authorization: Bearer {token}
```

#### 5. Webhook Configuration
```http
POST /webhook/configure
Content-Type: application/json
Authorization: Bearer {token}

{
  "url": "https://your-app.com/webhook/bco",
  "events": ["TRANSACTION_CREATED", "STATUS_CHANGED", "REFERRAL_APPROVED"],
  "secret": "your-webhook-secret"
}
```

## Authentication

### API Key Authentication
```java
BCOClient client = BCOClient.builder()
    .apiKey("your-api-key")
    .apiSecret("your-api-secret")
    .build();
```

### OAuth 2.0 Flow
```java
// Step 1: Get authorization URL
String authUrl = client.getAuthorizationUrl(
    "client-id",
    "redirect-uri",
    "state"
);

// Step 2: Exchange code for token
TokenResponse token = client.exchangeCodeForToken(
    "authorization-code",
    "client-id",
    "client-secret"
);

// Step 3: Use access token
client.setAccessToken(token.getAccessToken());
```

### JWT Token Structure
```json
{
  "sub": "user-id",
  "iss": "https://api.sbi.com",
  "aud": "bco-api",
  "exp": 1234567890,
  "iat": 1234567890,
  "corporateId": "0001517524",
  "permissions": ["bco:read", "bco:write", "bco:approve"]
}
```

## Data Models

### Transaction Types
```java
public enum TransactionType {
    DSO_REFERAL("DSO_REFERAL"),
    CAA("CAA"),
    OR("OR");
}
```

### Status Types
```java
public enum TransactionStatus {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    UNDER_REVIEW("UNDER_REVIEW"),
    ESCALATED("ESCALATED");
}
```

### BCOTransaction Model
```java
public class BCOTransaction {
    private String referenceNo;
    private String transactionType;
    private String customerName;
    private String customerId;
    private String branchCode;
    private String branchName;
    private String observationDate;
    private String status;
    private BigDecimal amount;
    private String currency;
    private Date createdAt;
    private Date updatedAt;
    private String reasonForReferal;
    private String bcoRemarks;
    private String approvalStatus;
    private String approvedBy;
    private Date approvedDate;
    private List<Attachment> attachments;
    private Map<String, Object> metadata;
}
```

### Error Response Model
```java
public class ErrorResponse {
    private String code;
    private String message;
    private String details;
    private String traceId;
    private Date timestamp;
}
```

## Error Handling

### Error Codes
| Code | Description | HTTP Status |
|------|-------------|-------------|
| BCO001 | Invalid request parameters | 400 |
| BCO002 | Authentication failed | 401 |
| BCO003 | Insufficient permissions | 403 |
| BCO004 | Transaction not found | 404 |
| BCO005 | Duplicate transaction | 409 |
| BCO006 | Rate limit exceeded | 429 |
| BCO007 | Internal server error | 500 |
| BCO008 | Service unavailable | 503 |

### Error Handling Example
```java
try {
    BCOInquireResponse response = client.inquire(request);
} catch (BCOException e) {
    switch (e.getErrorCode()) {
        case "BCO001":
            // Handle validation error
            break;
        case "BCO002":
            // Refresh authentication
            break;
        case "BCO006":
            // Implement retry with backoff
            break;
        default:
            // Generic error handling
    }
}
```

## Code Examples

### Complete Integration Example
```java
import com.sbi.bco.sdk.*;
import com.sbi.bco.sdk.models.*;
import com.sbi.bco.sdk.listeners.*;

public class BCOIntegrationExample {
    private BCOClient client;
    
    public void initialize() {
        // Configure client
        client = BCOClient.builder()
            .baseUrl("https://api.sbi.com/bco/v1")
            .apiKey("your-api-key")
            .apiSecret("your-api-secret")
            .connectionTimeout(30000)
            .readTimeout(30000)
            .maxRetries(3)
            .enableLogging(true)
            .build();
        
        // Set up webhook listener
        client.setWebhookListener(new BCOWebhookListener() {
            @Override
            public void onTransactionCreated(BCOTransaction transaction) {
                System.out.println("New transaction: " + transaction.getReferenceNo());
            }
            
            @Override
            public void onStatusChanged(String referenceNo, String oldStatus, String newStatus) {
                System.out.println("Status changed for " + referenceNo + 
                    ": " + oldStatus + " -> " + newStatus);
            }
        });
    }
    
    public void submitDSOReferal() {
        try {
            // Create DSO referral
            DSOReferal referal = DSOReferal.builder()
                .corporateId("0001517524")
                .userId("USER001")
                .customerId("CUST001")
                .customerName("ABC Corporation")
                .branchCode("BR001")
                .branchName("Main Branch")
                .amount(new BigDecimal("50000.00"))
                .currency("INR")
                .reasonForReferal("Credit limit exceeded")
                .observationDate(LocalDate.now())
                .build();
            
            // Add attachments
            referal.addAttachment(new Attachment(
                "credit_report.pdf",
                "application/pdf",
                Files.readAllBytes(Paths.get("credit_report.pdf"))
            ));
            
            // Submit referral
            BCOInquireResponse response = client.submitDSOReferal(referal);
            
            if (response.getStatus().equals("SUCCESS")) {
                System.out.println("Referral submitted: " + response.getMessage());
            }
            
        } catch (BCOException e) {
            handleError(e);
        }
    }
    
    public void queryTransactions() {
        try {
            // Build complex query
            BCOInquireRequest request = BCOInquireRequest.builder()
                .corporateId("0001517524")
                .userId("USER001")
                .transactionType(TransactionType.DSO_REFERAL)
                .status(TransactionStatus.PENDING)
                .fromDate(LocalDate.now().minusDays(30))
                .toDate(LocalDate.now())
                .branchCode("BR001")
                .pageNumber(1)
                .pageSize(50)
                .sortBy("createdAt")
                .sortOrder(SortOrder.DESC)
                .build();
            
            // Execute query with pagination
            BCOInquireResponse response = client.inquire(request);
            
            // Process all pages
            while (response.hasNextPage()) {
                processTransactions(response.getTransactions());
                response = client.getNextPage(response);
            }
            
        } catch (BCOException e) {
            handleError(e);
        }
    }
    
    public void approveTransaction(String referenceNo) {
        try {
            UpdateStatusRequest request = UpdateStatusRequest.builder()
                .referenceNo(referenceNo)
                .status(TransactionStatus.APPROVED)
                .remarks("Approved after verification")
                .approvedBy("MANAGER001")
                .build();
            
            BCOTransaction updated = client.updateTransactionStatus(request);
            System.out.println("Transaction approved: " + updated.getReferenceNo());
            
        } catch (BCOException e) {
            handleError(e);
        }
    }
    
    private void processTransactions(List<BCOTransaction> transactions) {
        transactions.forEach(transaction -> {
            System.out.println("Processing: " + transaction.getReferenceNo());
            // Business logic here
        });
    }
    
    private void handleError(BCOException e) {
        System.err.println("BCO Error: " + e.getMessage());
        System.err.println("Error Code: " + e.getErrorCode());
        System.err.println("Trace ID: " + e.getTraceId());
    }
}
```

### Webhook Handler Example
```java
@RestController
@RequestMapping("/webhook")
public class BCOWebhookController {
    
    @Autowired
    private BCOWebhookVerifier verifier;
    
    @PostMapping("/bco")
    public ResponseEntity<String> handleBCOWebhook(
            @RequestBody String payload,
            @RequestHeader("X-BCO-Signature") String signature) {
        
        try {
            // Verify webhook signature
            if (!verifier.verify(payload, signature)) {
                return ResponseEntity.status(401).body("Invalid signature");
            }
            
            // Parse webhook event
            BCOWebhookEvent event = BCOWebhookEvent.parse(payload);
            
            switch (event.getType()) {
                case TRANSACTION_CREATED:
                    handleTransactionCreated(event.getTransaction());
                    break;
                case STATUS_CHANGED:
                    handleStatusChanged(event.getReferenceNo(), 
                        event.getOldStatus(), event.getNewStatus());
                    break;
                case REFERRAL_APPROVED:
                    handleReferralApproved(event.getReferenceNo());
                    break;
            }
            
            return ResponseEntity.ok("OK");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing webhook");
        }
    }
}
```

## Security

### API Security Best Practices

1. **Always use HTTPS**: Never send credentials over unencrypted connections
2. **Rotate API keys regularly**: Implement key rotation every 90 days
3. **Implement rate limiting**: Respect API rate limits to avoid throttling
4. **Validate webhooks**: Always verify webhook signatures
5. **Use least privilege**: Request only necessary permissions
6. **Secure storage**: Never store credentials in code or version control

### Example: Secure Configuration
```java
// Use environment variables
BCOClient client = BCOClient.builder()
    .apiKey(System.getenv("BCO_API_KEY"))
    .apiSecret(System.getenv("BCO_API_SECRET"))
    .build();

// Or use secure configuration service
@Configuration
public class BCOConfig {
    @Value("${bco.api.key}")
    private String apiKey;
    
    @Value("${bco.api.secret}")
    private String apiSecret;
    
    @Bean
    public BCOClient bcoClient() {
        return BCOClient.builder()
            .apiKey(apiKey)
            .apiSecret(apiSecret)
            .build();
    }
}
```

## Performance

### Optimization Guidelines

1. **Use pagination**: Always paginate large result sets
2. **Implement caching**: Cache frequently accessed data
3. **Batch operations**: Group multiple operations when possible
4. **Async processing**: Use async APIs for non-blocking operations
5. **Connection pooling**: Reuse HTTP connections

### Performance Example
```java
// Async API usage
CompletableFuture<BCOInquireResponse> future = client.inquireAsync(request);
future.thenAccept(response -> {
    // Process response asynchronously
    processTransactions(response.getTransactions());
});

// Batch operations
List<DSOReferal> referals = Arrays.asList(referal1, referal2, referal3);
BatchResponse batchResponse = client.submitBatch(referals);

// Connection pooling configuration
BCOClient client = BCOClient.builder()
    .maxConnections(100)
    .maxConnectionsPerRoute(20)
    .connectionPoolTimeout(5000)
    .build();
```

### Rate Limits
| Endpoint | Rate Limit | Window |
|----------|------------|---------|
| /inquire | 100 requests | 1 minute |
| /dso-referal | 50 requests | 1 minute |
| /transaction/* | 200 requests | 1 minute |
| /webhook/* | 10 requests | 1 minute |

## SDK Configuration

### Java Configuration
```properties
# application.properties
bco.api.base-url=https://api.sbi.com/bco/v1
bco.api.key=your-api-key
bco.api.secret=your-api-secret
bco.api.timeout.connection=30000
bco.api.timeout.read=30000
bco.api.retry.max-attempts=3
bco.api.retry.backoff-multiplier=2
bco.api.logging.enabled=true
bco.api.logging.level=INFO
```

### JavaScript/TypeScript Configuration
```javascript
// config.js
export const bcoConfig = {
    baseUrl: process.env.BCO_API_URL || 'https://api.sbi.com/bco/v1',
    apiKey: process.env.BCO_API_KEY,
    apiSecret: process.env.BCO_API_SECRET,
    timeout: 30000,
    retries: 3,
    retryDelay: 1000,
    logging: {
        enabled: true,
        level: 'info'
    }
};
```

### Flutter Configuration
```dart
// config.dart
class BCOConfig {
  static const String baseUrl = String.fromEnvironment(
    'BCO_API_URL',
    defaultValue: 'https://api.sbi.com/bco/v1',
  );
  static const String apiKey = String.fromEnvironment('BCO_API_KEY');
  static const String apiSecret = String.fromEnvironment('BCO_API_SECRET');
  static const Duration timeout = Duration(seconds: 30);
  static const int maxRetries = 3;
}
```

## Troubleshooting

### Common Issues

1. **Authentication Errors**
   - Verify API credentials are correct
   - Check token expiration
   - Ensure proper headers are sent

2. **Connection Timeouts**
   - Increase timeout values
   - Check network connectivity
   - Verify firewall rules

3. **Rate Limiting**
   - Implement exponential backoff
   - Use batch operations
   - Cache responses when possible

4. **Data Validation Errors**
   - Check required fields
   - Verify date formats
   - Ensure proper encoding

### Debug Mode
```java
// Enable debug logging
BCOClient client = BCOClient.builder()
    .enableDebugMode(true)
    .logLevel(LogLevel.DEBUG)
    .build();

// Custom logger
client.setLogger(new BCOLogger() {
    @Override
    public void log(LogLevel level, String message, Object... args) {
        // Custom logging implementation
    }
});
```

## Migration Guide

### Migrating from Legacy API

1. **Update endpoint URLs**
   ```
   Old: https://api.sbi.com/trade-finance/bco
   New: https://api.sbi.com/bco/v1
   ```

2. **Update authentication method**
   ```java
   // Old
   client.setCredentials(username, password);
   
   // New
   client.setApiKey(apiKey, apiSecret);
   ```

3. **Update response handling**
   ```java
   // Old
   BCOResponse response = client.execute(request);
   if (response.isSuccess()) {
       List<Transaction> transactions = response.getTransactions();
   }
   
   // New
   BCOInquireResponse response = client.inquire(request);
   if ("SUCCESS".equals(response.getStatus())) {
       List<BCOTransaction> transactions = response.getTransactions();
   }
   ```

## Support

### Resources
- **Documentation**: https://developer.sbi.com/bco
- **API Status**: https://status.sbi.com
- **Support Portal**: https://support.sbi.com
- **Community Forum**: https://forum.sbi.com/bco

### Contact
- **Email**: bco-support@sbi.com
- **Phone**: +91-1800-XXX-XXXX
- **Developer Relations**: dev-relations@sbi.com

### Version History
| Version | Release Date | Changes |
|---------|--------------|---------|
| 1.0.0 | Jan 2025 | Initial release |
| 0.9.0 | Dec 2024 | Beta release |
| 0.8.0 | Nov 2024 | Alpha release |