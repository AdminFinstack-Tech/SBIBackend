# BCO Flutter SDK Documentation

## Table of Contents
1. [Introduction](#introduction)
2. [Installation](#installation)
3. [Quick Start](#quick-start)
4. [SDK Architecture](#sdk-architecture)
5. [Core Components](#core-components)
6. [API Reference](#api-reference)
7. [UI Components](#ui-components)
8. [Integration Guide](#integration-guide)
9. [Code Examples](#code-examples)
10. [Advanced Usage](#advanced-usage)
11. [Customization](#customization)
12. [Migration Guide](#migration-guide)

## Introduction

The BCO Flutter SDK provides a comprehensive set of tools and components for integrating Bank Credit Operations functionality into Flutter applications. It includes pre-built UI components, API services, and utilities for managing DSO referrals, CAA transactions, and operational risk assessments.

### Features
- 🎨 Pre-built Material Design UI components
- 🔌 Easy API integration with backend services
- 📱 Responsive design for mobile and tablet
- 🌍 Multi-language support
- 🔒 Built-in security features
- 📊 Real-time data synchronization
- 💾 Offline support with local caching

### Requirements
- Flutter SDK: 3.0.0 or higher
- Dart SDK: 2.19.0 or higher
- Android: API 21+ (Android 5.0+)
- iOS: 11.0+

## Installation

### 1. Add Dependency
Add the BCO SDK to your `pubspec.yaml`:

```yaml
dependencies:
  bco_flutter_sdk: ^1.0.0
  
  # Required dependencies
  dio: ^5.0.0
  provider: ^6.0.0
  flutter_secure_storage: ^9.0.0
  fluttertoast: ^8.2.0
  cached_network_image: ^3.2.0
```

### 2. Install Packages
```bash
flutter pub get
```

### 3. Platform-specific Setup

#### Android
Add required permissions to `android/app/src/main/AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

#### iOS
Add to `ios/Runner/Info.plist`:
```xml
<key>NSAppTransportSecurity</key>
<dict>
    <key>NSAllowsArbitraryLoads</key>
    <false/>
</dict>
```

## Quick Start

### 1. Initialize SDK
```dart
import 'package:bco_flutter_sdk/bco_flutter_sdk.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  
  // Initialize BCO SDK
  await BCOSdk.initialize(
    config: BCOConfig(
      baseUrl: 'https://api.sbi.com/bco/v1',
      apiKey: 'your-api-key',
      environment: BCOEnvironment.production,
    ),
  );
  
  runApp(MyApp());
}
```

### 2. Add BCO Provider
```dart
class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => BCOProvider()),
      ],
      child: MaterialApp(
        title: 'BCO Integration App',
        theme: BCOTheme.lightTheme,
        home: HomeScreen(),
      ),
    );
  }
}
```

### 3. Navigate to BCO Module
```dart
// In your navigation
Navigator.push(
  context,
  MaterialPageRoute(
    builder: (context) => BCOMainScreen(
      referenceNumber: 'BCO2024001',
      transactionType: 'DSO_REFERAL',
      customerName: 'ABC Corporation',
    ),
  ),
);
```

## SDK Architecture

### Package Structure
```
bco_flutter_sdk/
├── lib/
│   ├── src/
│   │   ├── api/              # API services
│   │   │   ├── bco_api_client.dart
│   │   │   ├── bco_api_service.dart
│   │   │   └── interceptors/
│   │   ├── models/           # Data models
│   │   │   ├── bco_transaction.dart
│   │   │   ├── dso_referal.dart
│   │   │   └── caa_observation.dart
│   │   ├── providers/        # State management
│   │   │   ├── bco_provider.dart
│   │   │   └── auth_provider.dart
│   │   ├── ui/              # UI components
│   │   │   ├── screens/
│   │   │   ├── widgets/
│   │   │   └── theme/
│   │   ├── utils/           # Utilities
│   │   │   ├── validators.dart
│   │   │   ├── formatters.dart
│   │   │   └── constants.dart
│   │   └── core/            # Core functionality
│   │       ├── bco_sdk.dart
│   │       ├── bco_config.dart
│   │       └── exceptions.dart
│   └── bco_flutter_sdk.dart  # Main export file
```

## Core Components

### BCOSdk Class
Main entry point for SDK initialization and configuration.

```dart
class BCOSdk {
  static BCOSdk? _instance;
  late BCOConfig config;
  late BCOApiClient apiClient;
  
  // Initialize SDK
  static Future<void> initialize({
    required BCOConfig config,
  }) async {
    _instance = BCOSdk._internal(config);
    await _instance!._setup();
  }
  
  // Get SDK instance
  static BCOSdk get instance {
    if (_instance == null) {
      throw BCONotInitializedException();
    }
    return _instance!;
  }
}
```

### BCOConfig
Configuration object for SDK setup.

```dart
class BCOConfig {
  final String baseUrl;
  final String apiKey;
  final BCOEnvironment environment;
  final Duration connectTimeout;
  final Duration receiveTimeout;
  final bool enableLogging;
  final Map<String, String>? defaultHeaders;
  
  BCOConfig({
    required this.baseUrl,
    required this.apiKey,
    this.environment = BCOEnvironment.production,
    this.connectTimeout = const Duration(seconds: 30),
    this.receiveTimeout = const Duration(seconds: 30),
    this.enableLogging = false,
    this.defaultHeaders,
  });
}
```

### BCOProvider
State management provider for BCO data.

```dart
class BCOProvider extends ChangeNotifier {
  final BCOApiService _apiService = BCOApiService();
  
  // State
  List<BCOTransaction> _transactions = [];
  BCOTransaction? _currentTransaction;
  bool _isLoading = false;
  String? _error;
  
  // Getters
  List<BCOTransaction> get transactions => _transactions;
  BCOTransaction? get currentTransaction => _currentTransaction;
  bool get isLoading => _isLoading;
  String? get error => _error;
  
  // Load transactions
  Future<void> loadTransactions({
    String? transactionType,
    String? status,
    int page = 1,
    int pageSize = 20,
  }) async {
    _setLoading(true);
    try {
      final response = await _apiService.getTransactions(
        transactionType: transactionType,
        status: status,
        page: page,
        pageSize: pageSize,
      );
      _transactions = response.transactions;
      _error = null;
    } catch (e) {
      _error = e.toString();
    } finally {
      _setLoading(false);
    }
  }
}
```

## API Reference

### BCOApiService
Service class for all BCO API operations.

```dart
class BCOApiService {
  final BCOApiClient _client = BCOSdk.instance.apiClient;
  
  // Get transactions
  Future<BCOTransactionResponse> getTransactions({
    String? transactionType,
    String? status,
    DateTime? fromDate,
    DateTime? toDate,
    int page = 1,
    int pageSize = 20,
  }) async {
    final response = await _client.post(
      '/inquire',
      data: {
        'transactionType': transactionType,
        'status': status,
        'fromDate': fromDate?.toIso8601String(),
        'toDate': toDate?.toIso8601String(),
        'pageNumber': page,
        'pageSize': pageSize,
      },
    );
    return BCOTransactionResponse.fromJson(response.data);
  }
  
  // Submit DSO referral
  Future<BCOSubmitResponse> submitDSOReferal({
    required String referenceNo,
    required String reasonForReferal,
    required String bcoRemarks,
    required bool isApproved,
  }) async {
    final response = await _client.post(
      '/dso-referal/$referenceNo/submit',
      data: {
        'reasonForReferal': reasonForReferal,
        'bcoRemarks': bcoRemarks,
        'approvalStatus': isApproved ? 'APPROVED' : 'REJECTED',
        'approvedBy': await _getUserId(),
        'approvedDate': DateTime.now().toIso8601String(),
      },
    );
    return BCOSubmitResponse.fromJson(response.data);
  }
  
  // Get CAA observations
  Future<List<CAAObservation>> getCAAObservations(String referenceNo) async {
    final response = await _client.get('/caa/$referenceNo/observations');
    return (response.data['observations'] as List)
        .map((json) => CAAObservation.fromJson(json))
        .toList();
  }
  
  // Submit opinion report
  Future<BCOSubmitResponse> submitOpinionReport({
    required String referenceNo,
    required String acceptability,
    required String bcoRemarks,
    String? documentUrl,
  }) async {
    final response = await _client.post(
      '/opinion-report/$referenceNo/submit',
      data: {
        'opinionReportAcceptability': acceptability,
        'bcoRemarks': bcoRemarks,
        'documentUrl': documentUrl,
        'reviewedBy': await _getUserId(),
        'reviewDate': DateTime.now().toIso8601String(),
      },
    );
    return BCOSubmitResponse.fromJson(response.data);
  }
}
```

## UI Components

### Pre-built Screens

#### BCOMainScreen
Main container with tabbed navigation.

```dart
BCOMainScreen(
  referenceNumber: 'BCO2024001',
  transactionType: 'DSO_REFERAL',
  customerName: 'ABC Corporation',
  onTransactionUpdated: (transaction) {
    // Handle update
  },
)
```

#### BCODashboard
Dashboard with summary statistics.

```dart
BCODashboard(
  onTransactionSelected: (transaction) {
    // Navigate to detail
  },
  filter: BCOFilter(
    status: 'PENDING',
    dateRange: DateTimeRange(
      start: DateTime.now().subtract(Duration(days: 30)),
      end: DateTime.now(),
    ),
  ),
)
```

### Custom Widgets

#### BCOTransactionCard
Display individual transaction.

```dart
BCOTransactionCard(
  transaction: transaction,
  onTap: () {
    // Handle tap
  },
  showActions: true,
)
```

#### BCOApprovalButtons
Approval/rejection button group.

```dart
BCOApprovalButtons(
  onApproved: () {
    // Handle approval
  },
  onRejected: () {
    // Handle rejection
  },
  isLoading: false,
)
```

#### BCOSearchBar
Search with filters.

```dart
BCOSearchBar(
  onSearch: (query) {
    // Handle search
  },
  onFilterChanged: (filter) {
    // Handle filter
  },
  filters: ['Status', 'Type', 'Date'],
)
```

### Theme Customization

```dart
// Use default theme
MaterialApp(
  theme: BCOTheme.lightTheme,
  darkTheme: BCOTheme.darkTheme,
);

// Custom theme
final customTheme = BCOTheme.lightTheme.copyWith(
  primaryColor: Colors.blue,
  accentColor: Colors.blueAccent,
);
```

## Integration Guide

### Step 1: Setup Authentication
```dart
// Configure authentication
BCOSdk.instance.setAuthToken('your-jwt-token');

// Or use auth provider
BCOSdk.instance.setAuthProvider(
  CustomAuthProvider(
    getToken: () async => await SecureStorage.getToken(),
    refreshToken: () async => await AuthService.refreshToken(),
  ),
);
```

### Step 2: Add to Navigation
```dart
// In your app's navigation
ListTile(
  title: Text('BCO Operations'),
  leading: Icon(Icons.account_balance),
  onTap: () {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => BCODashboard(),
      ),
    );
  },
)
```

### Step 3: Handle Callbacks
```dart
// Setup callbacks
BCOSdk.instance.setCallbacks(
  onError: (error) {
    // Global error handling
    showErrorDialog(error);
  },
  onSessionExpired: () {
    // Handle session expiry
    navigateToLogin();
  },
  onTransactionUpdated: (transaction) {
    // Handle updates
    refreshDashboard();
  },
);
```

## Code Examples

### Complete Integration Example
```dart
import 'package:flutter/material.dart';
import 'package:bco_flutter_sdk/bco_flutter_sdk.dart';
import 'package:provider/provider.dart';

class BCOIntegrationExample extends StatefulWidget {
  @override
  _BCOIntegrationExampleState createState() => _BCOIntegrationExampleState();
}

class _BCOIntegrationExampleState extends State<BCOIntegrationExample> {
  @override
  void initState() {
    super.initState();
    _loadData();
  }
  
  Future<void> _loadData() async {
    final provider = context.read<BCOProvider>();
    await provider.loadTransactions(
      transactionType: 'DSO_REFERAL',
      status: 'PENDING',
    );
  }
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('BCO Transactions'),
        actions: [
          IconButton(
            icon: Icon(Icons.filter_list),
            onPressed: _showFilterDialog,
          ),
        ],
      ),
      body: Consumer<BCOProvider>(
        builder: (context, provider, child) {
          if (provider.isLoading) {
            return Center(child: CircularProgressIndicator());
          }
          
          if (provider.error != null) {
            return BCOErrorWidget(
              error: provider.error!,
              onRetry: _loadData,
            );
          }
          
          if (provider.transactions.isEmpty) {
            return BCOEmptyState(
              message: 'No transactions found',
              onRefresh: _loadData,
            );
          }
          
          return RefreshIndicator(
            onRefresh: _loadData,
            child: ListView.builder(
              itemCount: provider.transactions.length,
              itemBuilder: (context, index) {
                final transaction = provider.transactions[index];
                return BCOTransactionCard(
                  transaction: transaction,
                  onTap: () => _openTransaction(transaction),
                );
              },
            ),
          );
        },
      ),
    );
  }
  
  void _openTransaction(BCOTransaction transaction) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => BCOMainScreen(
          referenceNumber: transaction.referenceNo,
          transactionType: transaction.transactionType,
          customerName: transaction.customerName,
        ),
      ),
    );
  }
  
  void _showFilterDialog() {
    showDialog(
      context: context,
      builder: (context) => BCOFilterDialog(
        onApply: (filter) {
          Navigator.pop(context);
          _applyFilter(filter);
        },
      ),
    );
  }
  
  Future<void> _applyFilter(BCOFilter filter) async {
    final provider = context.read<BCOProvider>();
    await provider.loadTransactions(
      transactionType: filter.transactionType,
      status: filter.status,
    );
  }
}
```

### Custom DSO Referral Implementation
```dart
class CustomDSOReferal extends StatefulWidget {
  final String referenceNo;
  
  CustomDSOReferal({required this.referenceNo});
  
  @override
  _CustomDSOReferalState createState() => _CustomDSOReferalState();
}

class _CustomDSOReferalState extends State<CustomDSOReferal> {
  final _formKey = GlobalKey<FormState>();
  final _reasonController = TextEditingController();
  final _remarksController = TextEditingController();
  bool _isApproved = false;
  bool _isLoading = false;
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('DSO Referral')),
      body: Form(
        key: _formKey,
        child: ListView(
          padding: EdgeInsets.all(16),
          children: [
            // Transaction info
            BCOTransactionInfoCard(
              referenceNo: widget.referenceNo,
            ),
            SizedBox(height: 20),
            
            // Reason for referral
            TextFormField(
              controller: _reasonController,
              decoration: InputDecoration(
                labelText: 'Reason for Referral',
                border: OutlineInputBorder(),
              ),
              maxLines: 3,
              validator: BCOValidators.required,
            ),
            SizedBox(height: 16),
            
            // BCO Remarks
            TextFormField(
              controller: _remarksController,
              decoration: InputDecoration(
                labelText: 'BCO Remarks',
                border: OutlineInputBorder(),
              ),
              maxLines: 3,
              validator: BCOValidators.required,
            ),
            SizedBox(height: 24),
            
            // Approval buttons
            BCOApprovalButtons(
              onApproved: () => setState(() => _isApproved = true),
              onRejected: () => setState(() => _isApproved = false),
              isApproved: _isApproved,
            ),
            SizedBox(height: 32),
            
            // Submit button
            ElevatedButton(
              onPressed: _isLoading ? null : _submitForm,
              child: _isLoading
                  ? CircularProgressIndicator()
                  : Text('Submit'),
              style: ElevatedButton.styleFrom(
                minimumSize: Size(double.infinity, 48),
              ),
            ),
          ],
        ),
      ),
    );
  }
  
  Future<void> _submitForm() async {
    if (!_formKey.currentState!.validate()) return;
    
    setState(() => _isLoading = true);
    
    try {
      final apiService = BCOApiService();
      final response = await apiService.submitDSOReferal(
        referenceNo: widget.referenceNo,
        reasonForReferal: _reasonController.text,
        bcoRemarks: _remarksController.text,
        isApproved: _isApproved,
      );
      
      if (response.success) {
        BCOToast.success('DSO Referral submitted successfully');
        Navigator.pop(context, true);
      } else {
        BCOToast.error(response.message ?? 'Submission failed');
      }
    } catch (e) {
      BCOToast.error('Error: ${e.toString()}');
    } finally {
      setState(() => _isLoading = false);
    }
  }
}
```

### Offline Support Implementation
```dart
class OfflineBCOProvider extends BCOProvider {
  final BCOLocalStorage _localStorage = BCOLocalStorage();
  
  @override
  Future<void> loadTransactions({
    String? transactionType,
    String? status,
    int page = 1,
    int pageSize = 20,
  }) async {
    // Try to load from cache first
    final cachedData = await _localStorage.getTransactions(
      transactionType: transactionType,
      status: status,
    );
    
    if (cachedData.isNotEmpty) {
      _transactions = cachedData;
      notifyListeners();
    }
    
    // Then try to fetch from API
    try {
      await super.loadTransactions(
        transactionType: transactionType,
        status: status,
        page: page,
        pageSize: pageSize,
      );
      
      // Cache the data
      await _localStorage.saveTransactions(_transactions);
    } catch (e) {
      // If offline, use cached data
      if (cachedData.isEmpty) {
        _error = 'No offline data available';
      }
    }
  }
  
  // Queue offline submissions
  Future<void> queueDSOSubmission(DSOReferal referal) async {
    await _localStorage.queueSubmission(referal);
    
    // Try to sync if online
    if (await isOnline()) {
      await syncOfflineData();
    }
  }
  
  Future<void> syncOfflineData() async {
    final pendingSubmissions = await _localStorage.getPendingSubmissions();
    
    for (final submission in pendingSubmissions) {
      try {
        await _apiService.submitDSOReferal(
          referenceNo: submission.referenceNo,
          reasonForReferal: submission.reasonForReferal,
          bcoRemarks: submission.bcoRemarks,
          isApproved: submission.isApproved,
        );
        
        await _localStorage.removeSubmission(submission.id);
      } catch (e) {
        // Keep in queue for next sync
      }
    }
  }
}
```

## Advanced Usage

### Custom Interceptors
```dart
class CustomBCOInterceptor extends Interceptor {
  @override
  void onRequest(RequestOptions options, RequestInterceptorHandler handler) {
    // Add custom headers
    options.headers['X-Custom-Header'] = 'value';
    
    // Log requests
    debugPrint('BCO Request: ${options.path}');
    
    handler.next(options);
  }
  
  @override
  void onResponse(Response response, ResponseInterceptorHandler handler) {
    // Process response
    if (response.data['encrypted'] == true) {
      response.data = decryptData(response.data);
    }
    
    handler.next(response);
  }
}

// Add to SDK
BCOSdk.instance.apiClient.addInterceptor(CustomBCOInterceptor());
```

### Custom Error Handling
```dart
class BCOErrorHandler {
  static void handleError(dynamic error, BuildContext context) {
    if (error is BCOException) {
      switch (error.code) {
        case 'AUTH_FAILED':
          _showReAuthDialog(context);
          break;
        case 'NETWORK_ERROR':
          _showOfflineDialog(context);
          break;
        case 'VALIDATION_ERROR':
          _showValidationErrors(context, error.validationErrors);
          break;
        default:
          _showGenericError(context, error.message);
      }
    } else {
      _showGenericError(context, error.toString());
    }
  }
  
  static void _showReAuthDialog(BuildContext context) {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) => AlertDialog(
        title: Text('Session Expired'),
        content: Text('Please login again to continue'),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.of(context).pushNamedAndRemoveUntil(
                '/login',
                (route) => false,
              );
            },
            child: Text('Login'),
          ),
        ],
      ),
    );
  }
}
```

### Performance Monitoring
```dart
class BCOPerformanceMonitor {
  static final _performanceData = <String, List<int>>{};
  
  static void startTimer(String operation) {
    _performanceData[operation] = [DateTime.now().millisecondsSinceEpoch];
  }
  
  static void endTimer(String operation) {
    if (_performanceData.containsKey(operation)) {
      final startTime = _performanceData[operation]![0];
      final duration = DateTime.now().millisecondsSinceEpoch - startTime;
      
      // Log performance
      debugPrint('BCO Performance - $operation: ${duration}ms');
      
      // Send to analytics
      Analytics.track('bco_performance', {
        'operation': operation,
        'duration': duration,
      });
    }
  }
}

// Usage
BCOPerformanceMonitor.startTimer('load_transactions');
await provider.loadTransactions();
BCOPerformanceMonitor.endTimer('load_transactions');
```

## Customization

### Theme Customization
```dart
class CustomBCOTheme {
  static ThemeData get theme {
    return ThemeData(
      primaryColor: Color(0xFF673391),
      accentColor: Color(0xFFB22382),
      fontFamily: 'Roboto',
      
      // Custom color scheme
      colorScheme: ColorScheme.light(
        primary: Color(0xFF673391),
        secondary: Color(0xFFB22382),
        surface: Colors.white,
        background: Color(0xFFF8F9FA),
        error: Colors.red,
      ),
      
      // Custom text theme
      textTheme: TextTheme(
        headline1: TextStyle(
          fontSize: 24,
          fontWeight: FontWeight.bold,
          color: Color(0xFF3F3F3F),
        ),
        bodyText1: TextStyle(
          fontSize: 14,
          color: Color(0xFF3F3F3F),
        ),
      ),
      
      // Custom component themes
      elevatedButtonTheme: ElevatedButtonThemeData(
        style: ElevatedButton.styleFrom(
          backgroundColor: Color(0xFF673391),
          foregroundColor: Colors.white,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
          ),
        ),
      ),
      
      inputDecorationTheme: InputDecorationTheme(
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
          borderSide: BorderSide(
            color: Color(0xFF673391),
            width: 2,
          ),
        ),
      ),
    );
  }
}
```

### Localization
```dart
// Add BCO localization delegate
MaterialApp(
  localizationsDelegates: [
    BCOLocalizations.delegate,
    GlobalMaterialLocalizations.delegate,
    GlobalWidgetsLocalizations.delegate,
  ],
  supportedLocales: BCOLocalizations.supportedLocales,
);

// Custom translations
class CustomBCOLocalizations extends BCOLocalizations {
  @override
  String get transactionDetails => 'Transaction Details';
  
  @override
  String get approveTransaction => 'Approve Transaction';
  
  @override
  String get rejectTransaction => 'Reject Transaction';
}

// Register custom localizations
BCOLocalizations.registerCustom(CustomBCOLocalizations());
```

### Custom Validators
```dart
class CustomBCOValidators {
  static String? referenceNumber(String? value) {
    if (value == null || value.isEmpty) {
      return 'Reference number is required';
    }
    
    final regex = RegExp(r'^BCO\d{7}$');
    if (!regex.hasMatch(value)) {
      return 'Invalid reference number format';
    }
    
    return null;
  }
  
  static String? amount(String? value) {
    if (value == null || value.isEmpty) {
      return 'Amount is required';
    }
    
    final amount = double.tryParse(value);
    if (amount == null || amount <= 0) {
      return 'Enter a valid amount';
    }
    
    if (amount > 10000000) {
      return 'Amount exceeds maximum limit';
    }
    
    return null;
  }
}
```

## Migration Guide

### From Version 0.x to 1.0

1. **Update Dependencies**
   ```yaml
   # Old
   bco_flutter_sdk: ^0.9.0
   
   # New
   bco_flutter_sdk: ^1.0.0
   ```

2. **API Changes**
   ```dart
   // Old
   BCOApi.initialize(apiKey: 'key');
   
   // New
   BCOSdk.initialize(
     config: BCOConfig(
       baseUrl: 'https://api.sbi.com/bco/v1',
       apiKey: 'key',
     ),
   );
   ```

3. **Model Changes**
   ```dart
   // Old
   Transaction.fromJson(json);
   
   // New
   BCOTransaction.fromJson(json);
   ```

4. **Provider Changes**
   ```dart
   // Old
   BCOState.of(context).transactions;
   
   // New
   context.read<BCOProvider>().transactions;
   ```

### Breaking Changes
- Renamed all model classes with BCO prefix
- Changed initialization method
- Updated provider pattern
- New theme structure

### Deprecation Notices
- `BCOApi` class deprecated, use `BCOSdk`
- `Transaction` model deprecated, use `BCOTransaction`
- `BCOState` deprecated, use `BCOProvider`

## Troubleshooting

### Common Issues

1. **SDK Not Initialized**
   ```dart
   // Error: BCONotInitializedException
   
   // Solution: Initialize SDK before use
   await BCOSdk.initialize(config: BCOConfig(...));
   ```

2. **Network Timeouts**
   ```dart
   // Configure longer timeouts
   BCOConfig(
     connectTimeout: Duration(seconds: 60),
     receiveTimeout: Duration(seconds: 60),
   );
   ```

3. **State Not Updating**
   ```dart
   // Ensure provider is properly set up
   ChangeNotifierProvider(
     create: (_) => BCOProvider(),
     child: MyApp(),
   );
   ```

### Debug Mode
```dart
// Enable debug logging
BCOSdk.enableDebugMode();

// Custom debug output
BCOSdk.setDebugLogger((message) {
  print('[BCO Debug] $message');
});
```

## Support

### Resources
- **Documentation**: https://docs.sbi.com/bco-flutter-sdk
- **API Reference**: https://api.sbi.com/bco/docs
- **Sample App**: https://github.com/sbi/bco-flutter-example
- **Issue Tracker**: https://github.com/sbi/bco-flutter-sdk/issues

### Contact
- **Email**: bco-sdk-support@sbi.com
- **Forum**: https://forum.sbi.com/bco-sdk
- **Stack Overflow**: Tag `bco-flutter-sdk`

### License
This SDK is licensed under the Apache License 2.0. See LICENSE file for details.