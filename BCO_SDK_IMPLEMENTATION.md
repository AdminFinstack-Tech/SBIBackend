# BCO SDK Implementation Guide

## SDK Package Structure

```
bco-sdk/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/sbi/bco/sdk/
│   │   │       ├── BCOClient.java
│   │   │       ├── BCOClientBuilder.java
│   │   │       ├── auth/
│   │   │       │   ├── AuthProvider.java
│   │   │       │   ├── ApiKeyAuthProvider.java
│   │   │       │   └── OAuthProvider.java
│   │   │       ├── http/
│   │   │       │   ├── HttpClient.java
│   │   │       │   ├── RetryHandler.java
│   │   │       │   └── RateLimiter.java
│   │   │       ├── models/
│   │   │       │   ├── BCOTransaction.java
│   │   │       │   ├── BCOInquireRequest.java
│   │   │       │   └── BCOInquireResponse.java
│   │   │       ├── exceptions/
│   │   │       │   ├── BCOException.java
│   │   │       │   ├── AuthenticationException.java
│   │   │       │   └── ValidationException.java
│   │   │       └── utils/
│   │   │           ├── JsonSerializer.java
│   │   │           └── SignatureVerifier.java
│   │   └── resources/
│   │       └── bco-sdk.properties
│   └── test/
│       └── java/
│           └── com/sbi/bco/sdk/
│               └── BCOClientTest.java
├── pom.xml
├── README.md
└── LICENSE
```

## Core SDK Implementation

### BCOClient.java
```java
package com.sbi.bco.sdk;

import com.sbi.bco.sdk.auth.AuthProvider;
import com.sbi.bco.sdk.http.HttpClient;
import com.sbi.bco.sdk.models.*;
import com.sbi.bco.sdk.exceptions.*;
import com.sbi.bco.sdk.utils.JsonSerializer;

import java.util.concurrent.CompletableFuture;
import java.util.Map;
import java.util.HashMap;

/**
 * Main client for BCO SDK operations
 */
public class BCOClient {
    private final String baseUrl;
    private final AuthProvider authProvider;
    private final HttpClient httpClient;
    private final JsonSerializer serializer;
    private final BCOClientConfig config;
    
    protected BCOClient(BCOClientBuilder builder) {
        this.baseUrl = builder.baseUrl;
        this.authProvider = builder.authProvider;
        this.config = builder.config;
        this.httpClient = new HttpClient(config);
        this.serializer = new JsonSerializer();
    }
    
    /**
     * Create a new BCOClient builder
     */
    public static BCOClientBuilder builder() {
        return new BCOClientBuilder();
    }
    
    /**
     * Inquire BCO transactions
     * @param request The inquiry request
     * @return BCOInquireResponse containing transaction details
     * @throws BCOException if the request fails
     */
    public BCOInquireResponse inquire(BCOInquireRequest request) throws BCOException {
        validateRequest(request);
        
        Map<String, String> headers = createHeaders();
        String jsonBody = serializer.serialize(request);
        
        HttpResponse response = httpClient.post(
            baseUrl + "/inquire",
            jsonBody,
            headers
        );
        
        if (!response.isSuccessful()) {
            throw handleErrorResponse(response);
        }
        
        return serializer.deserialize(response.getBody(), BCOInquireResponse.class);
    }
    
    /**
     * Inquire BCO transactions asynchronously
     */
    public CompletableFuture<BCOInquireResponse> inquireAsync(BCOInquireRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return inquire(request);
            } catch (BCOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * Submit DSO referral
     * @param referral The DSO referral details
     * @return BCOInquireResponse with submission status
     * @throws BCOException if the submission fails
     */
    public BCOInquireResponse submitDSOReferal(DSOReferal referral) throws BCOException {
        validateReferral(referral);
        
        Map<String, String> headers = createHeaders();
        String jsonBody = serializer.serialize(referral);
        
        HttpResponse response = httpClient.post(
            baseUrl + "/dso-referal",
            jsonBody,
            headers
        );
        
        if (!response.isSuccessful()) {
            throw handleErrorResponse(response);
        }
        
        return serializer.deserialize(response.getBody(), BCOInquireResponse.class);
    }
    
    /**
     * Get transaction details by reference number
     * @param referenceNo The transaction reference number
     * @return BCOTransaction details
     * @throws BCOException if the transaction is not found
     */
    public BCOTransaction getTransaction(String referenceNo) throws BCOException {
        if (referenceNo == null || referenceNo.isEmpty()) {
            throw new ValidationException("Reference number is required");
        }
        
        Map<String, String> headers = createHeaders();
        
        HttpResponse response = httpClient.get(
            baseUrl + "/transaction/" + referenceNo,
            headers
        );
        
        if (!response.isSuccessful()) {
            throw handleErrorResponse(response);
        }
        
        return serializer.deserialize(response.getBody(), BCOTransaction.class);
    }
    
    /**
     * Update transaction status
     * @param request The status update request
     * @return Updated BCOTransaction
     * @throws BCOException if the update fails
     */
    public BCOTransaction updateTransactionStatus(UpdateStatusRequest request) throws BCOException {
        validateStatusUpdate(request);
        
        Map<String, String> headers = createHeaders();
        String jsonBody = serializer.serialize(request);
        
        HttpResponse response = httpClient.put(
            baseUrl + "/transaction/" + request.getReferenceNo() + "/status",
            jsonBody,
            headers
        );
        
        if (!response.isSuccessful()) {
            throw handleErrorResponse(response);
        }
        
        return serializer.deserialize(response.getBody(), BCOTransaction.class);
    }
    
    /**
     * Submit batch of transactions
     * @param transactions List of transactions to submit
     * @return BatchResponse with results for each transaction
     * @throws BCOException if batch submission fails
     */
    public BatchResponse submitBatch(List<DSOReferal> transactions) throws BCOException {
        if (transactions == null || transactions.isEmpty()) {
            throw new ValidationException("Transactions list cannot be empty");
        }
        
        if (transactions.size() > config.getMaxBatchSize()) {
            throw new ValidationException("Batch size exceeds maximum limit of " + config.getMaxBatchSize());
        }
        
        Map<String, String> headers = createHeaders();
        String jsonBody = serializer.serialize(transactions);
        
        HttpResponse response = httpClient.post(
            baseUrl + "/batch",
            jsonBody,
            headers
        );
        
        if (!response.isSuccessful()) {
            throw handleErrorResponse(response);
        }
        
        return serializer.deserialize(response.getBody(), BatchResponse.class);
    }
    
    /**
     * Configure webhook for notifications
     * @param config Webhook configuration
     * @throws BCOException if configuration fails
     */
    public void configureWebhook(WebhookConfig config) throws BCOException {
        validateWebhookConfig(config);
        
        Map<String, String> headers = createHeaders();
        String jsonBody = serializer.serialize(config);
        
        HttpResponse response = httpClient.post(
            baseUrl + "/webhook/configure",
            jsonBody,
            headers
        );
        
        if (!response.isSuccessful()) {
            throw handleErrorResponse(response);
        }
    }
    
    /**
     * Get next page of results
     * @param currentResponse Current response with pagination info
     * @return Next page of results
     * @throws BCOException if there are no more pages
     */
    public BCOInquireResponse getNextPage(BCOInquireResponse currentResponse) throws BCOException {
        if (!currentResponse.hasNextPage()) {
            throw new BCOException("No more pages available");
        }
        
        BCOInquireRequest nextRequest = BCOInquireRequest.builder()
            .corporateId(currentResponse.getCorporateId())
            .userId(currentResponse.getUserId())
            .pageNumber(currentResponse.getPageNumber() + 1)
            .pageSize(currentResponse.getPageSize())
            .build();
            
        return inquire(nextRequest);
    }
    
    /**
     * Health check for API availability
     * @return true if API is available
     */
    public boolean healthCheck() {
        try {
            HttpResponse response = httpClient.get(baseUrl + "/health", new HashMap<>());
            return response.isSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
    
    // Private helper methods
    
    private Map<String, String> createHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("Request-Id", UUID.randomUUID().toString());
        headers.put("Sequence", String.valueOf(System.currentTimeMillis()));
        
        // Add authentication headers
        authProvider.addAuthHeaders(headers);
        
        return headers;
    }
    
    private void validateRequest(BCOInquireRequest request) throws ValidationException {
        if (request == null) {
            throw new ValidationException("Request cannot be null");
        }
        if (request.getCorporateId() == null || request.getCorporateId().isEmpty()) {
            throw new ValidationException("Corporate ID is required");
        }
        if (request.getUserId() == null || request.getUserId().isEmpty()) {
            throw new ValidationException("User ID is required");
        }
    }
    
    private void validateReferral(DSOReferal referral) throws ValidationException {
        if (referral == null) {
            throw new ValidationException("Referral cannot be null");
        }
        if (referral.getCustomerId() == null || referral.getCustomerId().isEmpty()) {
            throw new ValidationException("Customer ID is required");
        }
        if (referral.getReasonForReferal() == null || referral.getReasonForReferal().isEmpty()) {
            throw new ValidationException("Reason for referral is required");
        }
    }
    
    private void validateStatusUpdate(UpdateStatusRequest request) throws ValidationException {
        if (request == null) {
            throw new ValidationException("Update request cannot be null");
        }
        if (request.getReferenceNo() == null || request.getReferenceNo().isEmpty()) {
            throw new ValidationException("Reference number is required");
        }
        if (request.getStatus() == null) {
            throw new ValidationException("Status is required");
        }
    }
    
    private void validateWebhookConfig(WebhookConfig config) throws ValidationException {
        if (config == null) {
            throw new ValidationException("Webhook config cannot be null");
        }
        if (config.getUrl() == null || !isValidUrl(config.getUrl())) {
            throw new ValidationException("Valid webhook URL is required");
        }
        if (config.getEvents() == null || config.getEvents().isEmpty()) {
            throw new ValidationException("At least one event must be specified");
        }
    }
    
    private boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
    
    private BCOException handleErrorResponse(HttpResponse response) {
        try {
            ErrorResponse error = serializer.deserialize(response.getBody(), ErrorResponse.class);
            
            switch (error.getCode()) {
                case "BCO001":
                    return new ValidationException(error.getMessage());
                case "BCO002":
                    return new AuthenticationException(error.getMessage());
                case "BCO004":
                    return new NotFoundException(error.getMessage());
                case "BCO006":
                    return new RateLimitException(error.getMessage());
                default:
                    return new BCOException(error.getMessage(), error.getCode());
            }
        } catch (Exception e) {
            return new BCOException("Failed to parse error response: " + response.getBody());
        }
    }
}
```

### BCOClientBuilder.java
```java
package com.sbi.bco.sdk;

import com.sbi.bco.sdk.auth.*;

/**
 * Builder for creating BCOClient instances
 */
public class BCOClientBuilder {
    protected String baseUrl = "https://api.sbi.com/bco/v1";
    protected AuthProvider authProvider;
    protected BCOClientConfig config = new BCOClientConfig();
    
    public BCOClientBuilder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }
    
    public BCOClientBuilder apiKey(String apiKey) {
        this.authProvider = new ApiKeyAuthProvider(apiKey);
        return this;
    }
    
    public BCOClientBuilder apiKey(String apiKey, String apiSecret) {
        this.authProvider = new ApiKeyAuthProvider(apiKey, apiSecret);
        return this;
    }
    
    public BCOClientBuilder oauth(String clientId, String clientSecret) {
        this.authProvider = new OAuthProvider(clientId, clientSecret);
        return this;
    }
    
    public BCOClientBuilder connectionTimeout(int timeoutMs) {
        config.setConnectionTimeout(timeoutMs);
        return this;
    }
    
    public BCOClientBuilder readTimeout(int timeoutMs) {
        config.setReadTimeout(timeoutMs);
        return this;
    }
    
    public BCOClientBuilder maxRetries(int maxRetries) {
        config.setMaxRetries(maxRetries);
        return this;
    }
    
    public BCOClientBuilder enableLogging(boolean enable) {
        config.setLoggingEnabled(enable);
        return this;
    }
    
    public BCOClientBuilder enableDebugMode(boolean enable) {
        config.setDebugMode(enable);
        return this;
    }
    
    public BCOClientBuilder maxConnections(int maxConnections) {
        config.setMaxConnections(maxConnections);
        return this;
    }
    
    public BCOClientBuilder maxConnectionsPerRoute(int maxPerRoute) {
        config.setMaxConnectionsPerRoute(maxPerRoute);
        return this;
    }
    
    public BCOClient build() {
        if (authProvider == null) {
            throw new IllegalStateException("Authentication provider must be set");
        }
        return new BCOClient(this);
    }
}
```

## Language-Specific SDK Implementations

### JavaScript/TypeScript SDK
```typescript
// bco-sdk.ts
import axios, { AxiosInstance } from 'axios';

export interface BCOClientConfig {
  baseUrl: string;
  apiKey: string;
  apiSecret?: string;
  timeout?: number;
  retries?: number;
  onError?: (error: BCOError) => void;
}

export class BCOClient {
  private client: AxiosInstance;
  private config: BCOClientConfig;
  
  constructor(config: BCOClientConfig) {
    this.config = config;
    this.client = axios.create({
      baseURL: config.baseUrl,
      timeout: config.timeout || 30000,
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
    });
    
    // Add request interceptor for authentication
    this.client.interceptors.request.use((config) => {
      config.headers['Authorization'] = `Bearer ${this.generateToken()}`;
      config.headers['Request-Id'] = this.generateRequestId();
      config.headers['Sequence'] = Date.now().toString();
      return config;
    });
    
    // Add response interceptor for error handling
    this.client.interceptors.response.use(
      (response) => response,
      (error) => this.handleError(error)
    );
  }
  
  async inquire(request: BCOInquireRequest): Promise<BCOInquireResponse> {
    const response = await this.client.post('/inquire', request);
    return response.data;
  }
  
  async submitDSOReferal(referral: DSOReferal): Promise<BCOInquireResponse> {
    const response = await this.client.post('/dso-referal', referral);
    return response.data;
  }
  
  async getTransaction(referenceNo: string): Promise<BCOTransaction> {
    const response = await this.client.get(`/transaction/${referenceNo}`);
    return response.data;
  }
  
  async updateTransactionStatus(
    referenceNo: string,
    status: TransactionStatus,
    remarks?: string
  ): Promise<BCOTransaction> {
    const response = await this.client.put(`/transaction/${referenceNo}/status`, {
      status,
      remarks
    });
    return response.data;
  }
  
  private generateToken(): string {
    // Implement JWT generation logic
    const payload = {
      apiKey: this.config.apiKey,
      timestamp: Date.now(),
      exp: Date.now() + 3600000 // 1 hour
    };
    
    // Sign with apiSecret if provided
    if (this.config.apiSecret) {
      // Implementation of JWT signing
    }
    
    return Buffer.from(JSON.stringify(payload)).toString('base64');
  }
  
  private generateRequestId(): string {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
      const r = Math.random() * 16 | 0;
      const v = c === 'x' ? r : (r & 0x3 | 0x8);
      return v.toString(16);
    });
  }
  
  private handleError(error: any): Promise<never> {
    const bcoError: BCOError = {
      code: error.response?.data?.code || 'UNKNOWN',
      message: error.response?.data?.message || error.message,
      details: error.response?.data?.details,
      traceId: error.response?.headers?.['x-trace-id']
    };
    
    if (this.config.onError) {
      this.config.onError(bcoError);
    }
    
    return Promise.reject(bcoError);
  }
}

// Types
export interface BCOInquireRequest {
  corporateId: string;
  userId: string;
  transactionType?: TransactionType;
  status?: TransactionStatus;
  fromDate?: string;
  toDate?: string;
  branchCode?: string;
  customerId?: string;
  referenceNo?: string;
  pageNumber?: number;
  pageSize?: number;
}

export interface BCOInquireResponse {
  status: string;
  message: string;
  totalRecords: number;
  pageNumber: number;
  pageSize: number;
  transactions: BCOTransaction[];
}

export interface BCOTransaction {
  referenceNo: string;
  transactionType: TransactionType;
  customerName: string;
  customerId: string;
  branchCode: string;
  branchName: string;
  observationDate: string;
  status: TransactionStatus;
  amount?: string;
  currency?: string;
  createdAt?: string;
  updatedAt?: string;
  reasonForReferal?: string;
  bcoRemarks?: string;
  approvalStatus?: string;
  approvedBy?: string;
  approvedDate?: string;
}

export enum TransactionType {
  DSO_REFERAL = 'DSO_REFERAL',
  CAA = 'CAA',
  OR = 'OR'
}

export enum TransactionStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  UNDER_REVIEW = 'UNDER_REVIEW',
  ESCALATED = 'ESCALATED'
}

export interface BCOError {
  code: string;
  message: string;
  details?: string;
  traceId?: string;
}
```

### Flutter/Dart SDK
```dart
// bco_sdk.dart
import 'dart:convert';
import 'package:http/http.dart' as http;

class BCOClient {
  final String baseUrl;
  final String apiKey;
  final String? apiSecret;
  final Duration timeout;
  
  BCOClient({
    required this.baseUrl,
    required this.apiKey,
    this.apiSecret,
    this.timeout = const Duration(seconds: 30),
  });
  
  Future<BCOInquireResponse> inquire(BCOInquireRequest request) async {
    final response = await _post('/inquire', request.toJson());
    return BCOInquireResponse.fromJson(response);
  }
  
  Future<BCOInquireResponse> submitDSOReferal(DSOReferal referral) async {
    final response = await _post('/dso-referal', referral.toJson());
    return BCOInquireResponse.fromJson(response);
  }
  
  Future<BCOTransaction> getTransaction(String referenceNo) async {
    final response = await _get('/transaction/$referenceNo');
    return BCOTransaction.fromJson(response);
  }
  
  Future<BCOTransaction> updateTransactionStatus(
    String referenceNo,
    TransactionStatus status, {
    String? remarks,
  }) async {
    final response = await _put(
      '/transaction/$referenceNo/status',
      {
        'status': status.toString().split('.').last,
        if (remarks != null) 'remarks': remarks,
      },
    );
    return BCOTransaction.fromJson(response);
  }
  
  Future<Map<String, dynamic>> _get(String path) async {
    final uri = Uri.parse('$baseUrl$path');
    final response = await http.get(
      uri,
      headers: _buildHeaders(),
    ).timeout(timeout);
    
    return _handleResponse(response);
  }
  
  Future<Map<String, dynamic>> _post(
    String path,
    Map<String, dynamic> body,
  ) async {
    final uri = Uri.parse('$baseUrl$path');
    final response = await http.post(
      uri,
      headers: _buildHeaders(),
      body: jsonEncode(body),
    ).timeout(timeout);
    
    return _handleResponse(response);
  }
  
  Future<Map<String, dynamic>> _put(
    String path,
    Map<String, dynamic> body,
  ) async {
    final uri = Uri.parse('$baseUrl$path');
    final response = await http.put(
      uri,
      headers: _buildHeaders(),
      body: jsonEncode(body),
    ).timeout(timeout);
    
    return _handleResponse(response);
  }
  
  Map<String, String> _buildHeaders() {
    return {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Authorization': 'Bearer ${_generateToken()}',
      'Request-Id': _generateRequestId(),
      'Sequence': DateTime.now().millisecondsSinceEpoch.toString(),
    };
  }
  
  String _generateToken() {
    final payload = {
      'apiKey': apiKey,
      'timestamp': DateTime.now().millisecondsSinceEpoch,
      'exp': DateTime.now().add(Duration(hours: 1)).millisecondsSinceEpoch,
    };
    
    return base64Encode(utf8.encode(jsonEncode(payload)));
  }
  
  String _generateRequestId() {
    final random = Random();
    final values = List<int>.generate(16, (i) => random.nextInt(256));
    return base64Encode(values);
  }
  
  Map<String, dynamic> _handleResponse(http.Response response) {
    final body = jsonDecode(response.body);
    
    if (response.statusCode >= 200 && response.statusCode < 300) {
      return body;
    }
    
    throw BCOException(
      code: body['code'] ?? 'UNKNOWN',
      message: body['message'] ?? 'Unknown error',
      details: body['details'],
      traceId: response.headers['x-trace-id'],
    );
  }
}

// Models
class BCOInquireRequest {
  final String corporateId;
  final String userId;
  final TransactionType? transactionType;
  final TransactionStatus? status;
  final DateTime? fromDate;
  final DateTime? toDate;
  final String? branchCode;
  final String? customerId;
  final String? referenceNo;
  final int pageNumber;
  final int pageSize;
  
  BCOInquireRequest({
    required this.corporateId,
    required this.userId,
    this.transactionType,
    this.status,
    this.fromDate,
    this.toDate,
    this.branchCode,
    this.customerId,
    this.referenceNo,
    this.pageNumber = 1,
    this.pageSize = 10,
  });
  
  Map<String, dynamic> toJson() => {
    'corporateId': corporateId,
    'userId': userId,
    if (transactionType != null)
      'transactionType': transactionType.toString().split('.').last,
    if (status != null) 'status': status.toString().split('.').last,
    if (fromDate != null)
      'fromDate': DateFormat('yyyy-MM-dd').format(fromDate!),
    if (toDate != null)
      'toDate': DateFormat('yyyy-MM-dd').format(toDate!),
    if (branchCode != null) 'branchCode': branchCode,
    if (customerId != null) 'customerId': customerId,
    if (referenceNo != null) 'referenceNo': referenceNo,
    'pageNumber': pageNumber,
    'pageSize': pageSize,
  };
}

// Enums
enum TransactionType { dsoReferal, caa, or }

enum TransactionStatus { pending, approved, rejected, underReview, escalated }

// Exception
class BCOException implements Exception {
  final String code;
  final String message;
  final String? details;
  final String? traceId;
  
  BCOException({
    required this.code,
    required this.message,
    this.details,
    this.traceId,
  });
  
  @override
  String toString() => 'BCOException: [$code] $message';
}
```

## Integration Examples

### Spring Boot Integration
```java
@Configuration
@EnableConfigurationProperties(BCOProperties.class)
public class BCOConfiguration {
    
    @Bean
    public BCOClient bcoClient(BCOProperties properties) {
        return BCOClient.builder()
            .baseUrl(properties.getBaseUrl())
            .apiKey(properties.getApiKey(), properties.getApiSecret())
            .connectionTimeout(properties.getConnectionTimeout())
            .readTimeout(properties.getReadTimeout())
            .maxRetries(properties.getMaxRetries())
            .enableLogging(properties.isLoggingEnabled())
            .build();
    }
}

@Service
public class BCOService {
    private final BCOClient bcoClient;
    
    @Autowired
    public BCOService(BCOClient bcoClient) {
        this.bcoClient = bcoClient;
    }
    
    public List<BCOTransaction> getPendingTransactions(String corporateId) {
        try {
            BCOInquireRequest request = BCOInquireRequest.builder()
                .corporateId(corporateId)
                .userId(SecurityContextHolder.getContext().getAuthentication().getName())
                .status(TransactionStatus.PENDING)
                .pageSize(100)
                .build();
                
            BCOInquireResponse response = bcoClient.inquire(request);
            return response.getTransactions();
            
        } catch (BCOException e) {
            log.error("Failed to fetch pending transactions", e);
            throw new ServiceException("Unable to retrieve transactions", e);
        }
    }
}
```

### React Integration
```jsx
import React, { useState, useEffect } from 'react';
import { BCOClient } from '@sbi/bco-sdk';

const bcoClient = new BCOClient({
  baseUrl: process.env.REACT_APP_BCO_API_URL,
  apiKey: process.env.REACT_APP_BCO_API_KEY,
});

function BCODashboard() {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  useEffect(() => {
    fetchTransactions();
  }, []);
  
  const fetchTransactions = async () => {
    try {
      setLoading(true);
      const response = await bcoClient.inquire({
        corporateId: getUserCorporateId(),
        userId: getUserId(),
        pageNumber: 1,
        pageSize: 20,
      });
      
      setTransactions(response.transactions);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };
  
  const approveTransaction = async (referenceNo) => {
    try {
      await bcoClient.updateTransactionStatus(
        referenceNo,
        'APPROVED',
        'Approved by user'
      );
      
      // Refresh transactions
      fetchTransactions();
    } catch (err) {
      setError(err.message);
    }
  };
  
  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;
  
  return (
    <div className="bco-dashboard">
      <h1>BCO Transactions</h1>
      <table>
        <thead>
          <tr>
            <th>Reference No</th>
            <th>Customer</th>
            <th>Amount</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map(tx => (
            <tr key={tx.referenceNo}>
              <td>{tx.referenceNo}</td>
              <td>{tx.customerName}</td>
              <td>{tx.amount} {tx.currency}</td>
              <td>{tx.status}</td>
              <td>
                {tx.status === 'PENDING' && (
                  <button onClick={() => approveTransaction(tx.referenceNo)}>
                    Approve
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
```

## Publishing the SDK

### Maven Central
```xml
<project>
  <groupId>com.sbi.bco</groupId>
  <artifactId>bco-sdk</artifactId>
  <version>1.0.0</version>
  
  <name>BCO SDK</name>
  <description>SDK for SBI Bank Credit Operations</description>
  <url>https://github.com/sbi/bco-sdk-java</url>
  
  <licenses>
    <license>
      <name>Apache License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
    </license>
  </licenses>
  
  <distributionManagement>
    <repository>
      <id>ossrh</id>
      <url>https://oss.sonatype.org/service/local/staging/deploy/maven2/</url>
    </repository>
  </distributionManagement>
</project>
```

### NPM Registry
```json
{
  "name": "@sbi/bco-sdk",
  "version": "1.0.0",
  "description": "SDK for SBI Bank Credit Operations",
  "main": "dist/index.js",
  "types": "dist/index.d.ts",
  "repository": {
    "type": "git",
    "url": "https://github.com/sbi/bco-sdk-js.git"
  },
  "publishConfig": {
    "registry": "https://registry.npmjs.org/",
    "access": "public"
  }
}
```

### pub.dev (Dart/Flutter)
```yaml
name: bco_sdk
description: SDK for SBI Bank Credit Operations
version: 1.0.0
homepage: https://github.com/sbi/bco-sdk-dart

environment:
  sdk: '>=2.12.0 <3.0.0'

dependencies:
  http: ^0.13.0
  intl: ^0.17.0

dev_dependencies:
  test: ^1.16.0
  mockito: ^5.0.0
```

This comprehensive documentation provides everything needed to integrate BCO as an SDK into any application, with examples in multiple programming languages and frameworks.