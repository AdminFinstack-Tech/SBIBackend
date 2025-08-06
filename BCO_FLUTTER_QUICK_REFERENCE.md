# BCO Flutter Quick Reference Guide

## 🚀 Quick Setup

```dart
// 1. Initialize SDK
await BCOSdk.initialize(
  config: BCOConfig(
    baseUrl: 'https://api.sbi.com/bco/v1',
    apiKey: 'your-api-key',
  ),
);

// 2. Add Provider
MultiProvider(
  providers: [
    ChangeNotifierProvider(create: (_) => BCOProvider()),
  ],
  child: MyApp(),
);

// 3. Navigate to BCO
Navigator.push(
  context,
  MaterialPageRoute(builder: (_) => BCOMainScreen()),
);
```

## 📱 Common UI Components

### Transaction List
```dart
Consumer<BCOProvider>(
  builder: (context, provider, _) {
    return ListView.builder(
      itemCount: provider.transactions.length,
      itemBuilder: (context, index) {
        return BCOTransactionCard(
          transaction: provider.transactions[index],
          onTap: () => _openDetails(provider.transactions[index]),
        );
      },
    );
  },
);
```

### DSO Referral Form
```dart
BCOFormField(
  label: 'Reason for Referral',
  controller: _reasonController,
  validator: BCOValidators.required,
  maxLines: 3,
);

BCOApprovalButtons(
  onApproved: () => setState(() => _isApproved = true),
  onRejected: () => setState(() => _isApproved = false),
);
```

### Search and Filter
```dart
BCOSearchBar(
  onSearch: (query) => provider.searchTransactions(query),
  onFilterChanged: (filter) => provider.applyFilter(filter),
);
```

## 🔌 API Calls

### Load Transactions
```dart
final provider = context.read<BCOProvider>();
await provider.loadTransactions(
  transactionType: 'DSO_REFERAL',
  status: 'PENDING',
  page: 1,
  pageSize: 20,
);
```

### Submit DSO Referral
```dart
final apiService = BCOApiService();
final response = await apiService.submitDSOReferal(
  referenceNo: 'BCO2024001',
  reasonForReferal: 'Credit limit exceeded',
  bcoRemarks: 'Reviewed and approved',
  isApproved: true,
);
```

### Get Transaction Details
```dart
final transaction = await apiService.getTransactionDetails('BCO2024001');
```

## 📊 Data Models

### BCOTransaction
```dart
class BCOTransaction {
  final String referenceNo;
  final String transactionType; // DSO_REFERAL, CAA, OR
  final String status;          // PENDING, APPROVED, REJECTED
  final String customerName;
  final String branchCode;
  final DateTime observationDate;
}
```

### DSOReferal
```dart
class DSOReferal {
  final String referenceNo;
  final String reasonForReferal;
  final String bcoRemarks;
  final String approvalStatus;
  final GeneralInfo generalInfo;
}
```

## 🎨 Theming

```dart
// Use default theme
MaterialApp(
  theme: BCOTheme.lightTheme,
  darkTheme: BCOTheme.darkTheme,
);

// Custom colors
final customTheme = ThemeData(
  primaryColor: Color(0xFF673391),
  accentColor: Color(0xFFB22382),
);
```

## 🌍 Localization

```dart
// Get localized text
AppLocalizations.of(context)?.transactionDetails ?? 'Transaction Details'

// Supported languages
- English (en)
- Arabic (ar) 
- French (fr)
```

## ⚡ State Management

```dart
// Read state
final transactions = context.read<BCOProvider>().transactions;

// Watch state changes
context.watch<BCOProvider>().isLoading;

// Select specific state
context.select<BCOProvider, bool>((p) => p.isLoading);
```

## 🔒 Authentication

```dart
// Set auth token
BCOSdk.instance.setAuthToken('jwt-token');

// Handle session expiry
BCOSdk.instance.setCallbacks(
  onSessionExpired: () => navigateToLogin(),
);
```

## 📥 Offline Support

```dart
// Enable offline mode
BCOSdk.instance.enableOfflineMode();

// Queue offline submission
await provider.queueOfflineSubmission(dsoReferal);

// Sync when online
await provider.syncOfflineData();
```

## 🐛 Error Handling

```dart
try {
  await provider.loadTransactions();
} on BCOException catch (e) {
  if (e.code == 'NETWORK_ERROR') {
    showOfflineMessage();
  } else {
    showError(e.message);
  }
}
```

## 📱 Common Widgets

### Empty State
```dart
BCOEmptyState(
  message: 'No transactions found',
  icon: Icons.inbox_outlined,
  onRefresh: () => provider.loadTransactions(),
);
```

### Loading Indicator
```dart
BCOLoadingIndicator(
  message: 'Loading transactions...',
);
```

### Error Widget
```dart
BCOErrorWidget(
  error: 'Failed to load data',
  onRetry: () => provider.loadTransactions(),
);
```

## 🎯 Best Practices

### 1. Always handle loading states
```dart
if (provider.isLoading) {
  return CircularProgressIndicator();
}
```

### 2. Dispose controllers
```dart
@override
void dispose() {
  _reasonController.dispose();
  _remarksController.dispose();
  super.dispose();
}
```

### 3. Use form validation
```dart
if (!_formKey.currentState!.validate()) {
  return;
}
```

### 4. Handle navigation result
```dart
final result = await Navigator.push(...);
if (result == true) {
  _refreshData();
}
```

### 5. Show user feedback
```dart
BCOToast.success('Transaction approved');
BCOToast.error('Failed to submit');
```

## 🔧 Debugging

```dart
// Enable debug mode
BCOSdk.enableDebugMode();

// Log API calls
BCOSdk.instance.apiClient.enableLogging();

// Print state changes
provider.addListener(() {
  print('State updated: ${provider.transactions.length} transactions');
});
```

## 📦 Required Packages

```yaml
dependencies:
  bco_flutter_sdk: ^1.0.0
  provider: ^6.0.0
  dio: ^5.0.0
  flutter_secure_storage: ^9.0.0
  fluttertoast: ^8.2.0
```

## 🆘 Common Issues

| Issue | Solution |
|-------|----------|
| SDK not initialized | Call `BCOSdk.initialize()` in main() |
| State not updating | Ensure Provider is above widget tree |
| API timeout | Increase timeout in BCOConfig |
| Token expired | Implement token refresh logic |
| Offline data not syncing | Check network connectivity |

## 📞 Support

- 📧 Email: bco-support@sbi.com
- 📚 Docs: https://docs.sbi.com/bco-flutter
- 💬 Forum: https://forum.sbi.com/bco