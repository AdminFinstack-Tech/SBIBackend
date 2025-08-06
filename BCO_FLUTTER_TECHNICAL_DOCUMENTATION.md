# BCO Flutter Frontend Technical Documentation

## Table of Contents
1. [Overview](#overview)
2. [Architecture](#architecture)
3. [UI Components](#ui-components)
4. [API Integration](#api-integration)
5. [Data Models](#data-models)
6. [State Management](#state-management)
7. [Navigation Flow](#navigation-flow)
8. [Error Handling](#error-handling)
9. [Localization](#localization)
10. [Security](#security)
11. [Performance Optimization](#performance-optimization)
12. [Testing](#testing)

## Overview

The BCO (Bank Credit Operations) Flutter frontend provides a comprehensive mobile interface for managing bank credit operations including DSO referrals, CAA transactions, and operational risk assessments. This documentation covers the technical implementation details for developers.

### Key Features
- Tabbed interface for DSO, CAA, and OR modules
- Real-time transaction processing
- Offline capability with data synchronization
- Multi-language support
- Secure authentication
- Document management
- Push notifications

### Technology Stack
- **Framework**: Flutter 3.x
- **Language**: Dart 2.19+
- **State Management**: Provider/Riverpod
- **HTTP Client**: Dio with interceptors
- **Local Storage**: Hive/SharedPreferences
- **Authentication**: JWT tokens
- **UI Components**: Material Design 3

## Architecture

### Project Structure
```
lib/
├── models/                   # Data models
│   ├── bco_models.dart      # BCO specific models
│   └── app_response.dart    # API response wrapper
├── networking/              # Network layer
│   ├── BCOApiUtils.dart    # BCO API service
│   ├── HttpUtil.dart       # HTTP client wrapper
│   └── AppConstants.dart   # API constants
├── ui/                     # UI layer
│   └── bco/               # BCO screens
│       ├── bco_main_screen.dart
│       ├── bco_dashboard.dart
│       ├── dso_referral_screen.dart
│       ├── caa_screen.dart
│       └── or_screen.dart
├── utils/                  # Utilities
│   ├── colors.dart        # Color constants
│   ├── textStyles.dart    # Text style definitions
│   └── LogUtils.dart      # Logging utilities
└── l10n/                  # Localization
    └── app_localizations.dart
```

### Architecture Pattern
The app follows a clean architecture pattern with clear separation of concerns:

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   Presentation  │────▶│    Business     │────▶│      Data       │
│     Layer       │     │     Logic       │     │     Layer       │
└─────────────────┘     └─────────────────┘     └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   UI Widgets    │     │   Use Cases     │     │  Repositories   │
│   Controllers   │     │   Services      │     │  Data Sources   │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

## UI Components

### BCO Main Screen
The main container screen that manages the tabbed navigation between different BCO modules.

```dart
// Key implementation details
class BCOMainScreen extends StatefulWidget {
  final String? referenceNumber;
  final String? transactionType;
  final String? customerName;
  
  // Tab controller manages three tabs:
  // 1. DSO Referral
  // 2. CAA (Credit Approval Authority)
  // 3. OR (Operational Risk)
}
```

#### Component Hierarchy
```
BCOMainScreen
├── CustomTopNavigation
│   ├── Gradient Status Bar
│   ├── Logo Section
│   └── Action Icons
├── Transaction Details Card
│   ├── Reference Number
│   ├── Transaction Type
│   └── Customer Name
├── Tab Bar
│   ├── DSO Referral Tab
│   ├── CAA Tab
│   └── OR Tab
└── Tab Views
    ├── DSORefferalScreen
    ├── CAAScreen
    └── ORScreen
```

### DSO Referral Screen
Handles DSO (Designated Sanctioning Officer) referral submissions and approvals.

#### Features
- General information display (expandable)
- Reason for referral input
- BCO remarks with character limit
- Approval/rejection workflow
- Search and sort functionality

#### Key Components
```dart
// Form validation
final _formKey = GlobalKey<FormState>();

// Text controllers
final _reasonController = TextEditingController();
final _bcoRemarksController = TextEditingController();

// State management
bool _isApproved = false;
bool _isNotApproved = false;
```

### CAA Screen
Manages Credit Approval Authority observations and compliance tracking.

#### Features
- Multiple observation entries
- Reason for non-compliance
- BCO remarks per observation
- Batch submission capability

### OR Screen (Opinion Report)
Handles operational risk assessments and opinion reports.

#### Features
- Opinion report acceptability
- Document upload/viewing
- Risk matrix visualization
- Summary generation

## API Integration

### BCOApiUtils Service
Central service for all BCO-related API calls.

```dart
class BCOApiUtils {
  // Singleton pattern
  static final BCOApiUtils _instance = BCOApiUtils._internal();
  
  // API Methods
  Future<ApiResponse?> getDashboard({...});
  Future<ApiResponse?> getTransactionDetails(String referenceNo);
  Future<ApiResponse?> submitDSOReferal(String referenceNo, Map data);
  Future<ApiResponse?> getCAAObservations(String referenceNo);
  Future<ApiResponse?> submitOpinionReport(String referenceNo, Map data);
}
```

### API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/bco/dashboard` | Get dashboard data |
| GET | `/api/bco/transaction/{ref}` | Get transaction details |
| GET | `/api/bco/dso-referral/{ref}` | Get DSO referral details |
| POST | `/api/bco/dso-referral/{ref}/submit` | Submit DSO referral |
| GET | `/api/bco/caa/{ref}/observations` | Get CAA observations |
| POST | `/api/bco/caa/{ref}/submit` | Submit CAA observations |
| GET | `/api/bco/opinion-report/{ref}` | Get opinion report |
| POST | `/api/bco/opinion-report/{ref}/submit` | Submit opinion report |

### HTTP Client Configuration
```dart
// HttpUtil configuration
class HttpUtil {
  late Dio _dio;
  
  HttpUtil() {
    _dio = Dio(BaseOptions(
      baseUrl: AppConstants.BASE_URL,
      connectTimeout: 30000,
      receiveTimeout: 30000,
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      },
    ));
    
    // Add interceptors
    _dio.interceptors.add(AuthInterceptor());
    _dio.interceptors.add(LoggingInterceptor());
    _dio.interceptors.add(ErrorInterceptor());
  }
}
```

## Data Models

### Core Models

#### BCOTransaction
Primary model for BCO transactions:
```dart
class BCOTransaction {
  final String referenceNo;
  final String transactionType;
  final String customerName;
  final String customerId;
  final String branchCode;
  final String branchName;
  final String observationDate;
  final String status;
  final String? amount;
  final String? currency;
  
  // JSON serialization
  factory BCOTransaction.fromJson(Map<String, dynamic> json);
  Map<String, dynamic> toJson();
}
```

#### DSOReferal
Model for DSO referral data:
```dart
class DSOReferal {
  final String referenceNo;
  final String reasonForReferal;
  final String bcoRemarks;
  final String approvalStatus;
  final GeneralInfo generalInfo;
}
```

#### BCOApiResponse
Generic API response wrapper:
```dart
class BCOApiResponse<T> {
  final bool success;
  final String? message;
  final T? data;
  final String? error;
  
  factory BCOApiResponse.fromJson(
    Map<String, dynamic> json,
    T Function(dynamic) fromJsonT,
  );
}
```

## State Management

### Provider Pattern
```dart
// BCO State Provider
class BCOProvider extends ChangeNotifier {
  List<BCOTransaction> _transactions = [];
  bool _isLoading = false;
  String? _error;
  
  // Getters
  List<BCOTransaction> get transactions => _transactions;
  bool get isLoading => _isLoading;
  String? get error => _error;
  
  // Methods
  Future<void> loadTransactions() async {
    _isLoading = true;
    notifyListeners();
    
    try {
      final response = await BCOApiUtils().getDashboard();
      if (response?.success == true) {
        _transactions = (response!.data as List)
            .map((json) => BCOTransaction.fromJson(json))
            .toList();
      }
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }
}
```

### Usage in Widgets
```dart
// Consumer widget
Consumer<BCOProvider>(
  builder: (context, provider, child) {
    if (provider.isLoading) {
      return CircularProgressIndicator();
    }
    
    return ListView.builder(
      itemCount: provider.transactions.length,
      itemBuilder: (context, index) {
        final transaction = provider.transactions[index];
        return TransactionCard(transaction: transaction);
      },
    );
  },
)
```

## Navigation Flow

### Navigation Structure
```
BottomNavigation
└── BCO (Menu Item)
    └── BCOMainScreen
        ├── DSO Referral Tab
        │   └── DSORefferalScreen
        │       └── Submit Confirmation Dialog
        ├── CAA Tab
        │   └── CAAScreen
        │       └── Observation List
        └── OR Tab
            └── ORScreen
                └── Document Viewer
```

### Navigation Implementation
```dart
// Navigate to BCO from menu
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

// Tab navigation handled by DefaultTabController
DefaultTabController(
  length: 3,
  child: Scaffold(
    body: TabBarView(
      children: [
        DSORefferalScreen(),
        CAAScreen(),
        ORScreen(),
      ],
    ),
  ),
);
```

## Error Handling

### API Error Handling
```dart
class ErrorInterceptor extends Interceptor {
  @override
  void onError(DioError err, ErrorInterceptorHandler handler) {
    switch (err.type) {
      case DioErrorType.connectTimeout:
        showError('Connection timeout');
        break;
      case DioErrorType.response:
        handleResponseError(err.response);
        break;
      default:
        showError('Network error occurred');
    }
    handler.next(err);
  }
  
  void handleResponseError(Response? response) {
    if (response?.statusCode == 401) {
      // Handle unauthorized - logout user
      navigateToLogin();
    } else if (response?.statusCode == 400) {
      // Handle validation errors
      showValidationErrors(response?.data);
    }
  }
}
```

### UI Error Display
```dart
// Toast messages for quick feedback
Fluttertoast.showToast(
  msg: "Error: ${error.message}",
  toastLength: Toast.LENGTH_SHORT,
  gravity: ToastGravity.BOTTOM,
  backgroundColor: Colors.red,
);

// Dialog for critical errors
showDialog(
  context: context,
  builder: (context) => AlertDialog(
    title: Text('Error'),
    content: Text(error.details),
    actions: [
      TextButton(
        onPressed: () => Navigator.pop(context),
        child: Text('OK'),
      ),
    ],
  ),
);
```

## Localization

### Multi-language Support
```dart
// AppLocalizations usage
AppLocalizations.of(context)?.transactionDetails ?? 'Transaction Details'

// Supported languages
- English (en)
- Arabic (ar)
- French (fr)

// Implementation
MaterialApp(
  localizationsDelegates: [
    AppLocalizations.delegate,
    GlobalMaterialLocalizations.delegate,
    GlobalWidgetsLocalizations.delegate,
  ],
  supportedLocales: [
    Locale('en'),
    Locale('ar'),
    Locale('fr'),
  ],
);
```

## Security

### Authentication
```dart
class AuthInterceptor extends Interceptor {
  @override
  void onRequest(RequestOptions options, RequestInterceptorHandler handler) {
    // Add JWT token to headers
    final token = SecureStorage.getToken();
    if (token != null) {
      options.headers['Authorization'] = 'Bearer $token';
    }
    
    // Add request ID for tracking
    options.headers['Request-Id'] = generateRequestId();
    options.headers['Sequence'] = DateTime.now().millisecondsSinceEpoch.toString();
    
    handler.next(options);
  }
}
```

### Data Encryption
```dart
// Sensitive data storage
class SecureStorage {
  static const _storage = FlutterSecureStorage();
  
  static Future<void> saveToken(String token) async {
    await _storage.write(key: 'jwt_token', value: token);
  }
  
  static Future<String?> getToken() async {
    return await _storage.read(key: 'jwt_token');
  }
}
```

## Performance Optimization

### List Optimization
```dart
// Use ListView.builder for large lists
ListView.builder(
  itemCount: transactions.length,
  itemBuilder: (context, index) {
    return TransactionCard(
      key: ValueKey(transactions[index].referenceNo),
      transaction: transactions[index],
    );
  },
);
```

### Image Caching
```dart
// Cache network images
CachedNetworkImage(
  imageUrl: documentUrl,
  placeholder: (context, url) => CircularProgressIndicator(),
  errorWidget: (context, url, error) => Icon(Icons.error),
);
```

### State Optimization
```dart
// Use const constructors where possible
const SizedBox(height: 16);
const Icon(Icons.check, color: Colors.green);

// Avoid rebuilding static widgets
class _StaticHeader extends StatelessWidget {
  const _StaticHeader({Key? key}) : super(key: key);
  
  @override
  Widget build(BuildContext context) {
    return const Text('BCO Dashboard');
  }
}
```

## Testing

### Unit Tests
```dart
// Model testing
void main() {
  group('BCOTransaction Tests', () {
    test('fromJson creates valid object', () {
      final json = {
        'referenceNo': 'BCO2024001',
        'transactionType': 'DSO_REFERAL',
        'customerName': 'Test Customer',
      };
      
      final transaction = BCOTransaction.fromJson(json);
      
      expect(transaction.referenceNo, 'BCO2024001');
      expect(transaction.transactionType, 'DSO_REFERAL');
    });
  });
}
```

### Widget Tests
```dart
// Screen testing
void main() {
  testWidgets('BCOMainScreen displays tabs', (WidgetTester tester) async {
    await tester.pumpWidget(
      MaterialApp(
        home: BCOMainScreen(),
      ),
    );
    
    expect(find.text('DSO Referral'), findsOneWidget);
    expect(find.text('CAA'), findsOneWidget);
    expect(find.text('OR'), findsOneWidget);
  });
}
```

### Integration Tests
```dart
// API integration testing
void main() {
  test('BCO API returns dashboard data', () async {
    final apiUtils = BCOApiUtils();
    final response = await apiUtils.getDashboard();
    
    expect(response?.success, true);
    expect(response?.data, isNotNull);
  });
}
```

## Best Practices

### Code Organization
1. **Single Responsibility**: Each widget/class should have one clear purpose
2. **DRY Principle**: Extract common widgets and functions
3. **Consistent Naming**: Follow Dart naming conventions
4. **Documentation**: Document complex logic and public APIs

### Performance Guidelines
1. **Lazy Loading**: Load data only when needed
2. **Pagination**: Implement for large data sets
3. **Caching**: Cache API responses when appropriate
4. **Debouncing**: Debounce search inputs

### UI/UX Guidelines
1. **Loading States**: Always show loading indicators
2. **Error Feedback**: Provide clear error messages
3. **Empty States**: Handle empty data gracefully
4. **Accessibility**: Support screen readers and large text

### Security Guidelines
1. **Input Validation**: Validate all user inputs
2. **Secure Storage**: Use encrypted storage for sensitive data
3. **API Security**: Always use HTTPS
4. **Token Management**: Implement token refresh logic

## Troubleshooting

### Common Issues

1. **API Connection Failed**
   ```dart
   // Check network connectivity
   final connectivityResult = await Connectivity().checkConnectivity();
   if (connectivityResult == ConnectivityResult.none) {
     showError('No internet connection');
   }
   ```

2. **Token Expired**
   ```dart
   // Implement token refresh
   if (response.statusCode == 401) {
     await refreshToken();
     // Retry request
   }
   ```

3. **State Not Updating**
   ```dart
   // Ensure notifyListeners() is called
   setState(() {
     _isLoading = false;
   });
   ```

## Migration Guide

### Upgrading from Previous Version
1. Update dependencies in `pubspec.yaml`
2. Run `flutter pub get`
3. Update API endpoints if changed
4. Test all BCO flows

## Support

For technical support or questions:
- GitHub Issues: [Project Repository]
- Email: bco-mobile@sbi.com
- Documentation: [Online Docs]