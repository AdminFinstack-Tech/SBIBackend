# BCO Universal SDK Integration Guide

## Table of Contents
1. [Overview](#overview)
2. [SDK Architecture](#sdk-architecture)
3. [React Native Integration](#react-native-integration)
4. [React Web Integration](#react-web-integration)
5. [Angular Integration](#angular-integration)
6. [Vue.js Integration](#vue-js-integration)
7. [iOS Native Integration](#ios-native-integration)
8. [Android Native Integration](#android-native-integration)
9. [Ionic Integration](#ionic-integration)
10. [Xamarin Integration](#xamarin-integration)
11. [REST API Direct Integration](#rest-api-direct-integration)
12. [WebSocket Integration](#websocket-integration)
13. [GraphQL Integration](#graphql-integration)
14. [Common Integration Patterns](#common-integration-patterns)

## Overview

The BCO (Bank Credit Operations) SDK provides universal integration capabilities for various platforms and frameworks. This guide covers integration approaches for all major development platforms.

### Supported Platforms
- 📱 **Mobile**: React Native, Flutter, Ionic, Xamarin, Native iOS/Android
- 🌐 **Web**: React, Angular, Vue.js, Plain JavaScript
- 🖥️ **Desktop**: Electron, .NET MAUI
- 🔌 **API**: REST, GraphQL, WebSocket

### Core Features
- Multi-platform support
- Unified API interface
- Real-time synchronization
- Offline capabilities
- Type-safe implementations
- Authentication management

## SDK Architecture

### Universal SDK Structure
```
bco-universal-sdk/
├── packages/
│   ├── core/                 # Core logic (TypeScript)
│   ├── react-native/         # React Native package
│   ├── react/                # React web package
│   ├── angular/              # Angular package
│   ├── vue/                  # Vue.js package
│   ├── ios/                  # iOS native SDK
│   ├── android/              # Android native SDK
│   └── rest-client/          # REST API client
├── docs/                     # Documentation
├── examples/                 # Example applications
└── tools/                    # Development tools
```

## React Native Integration

### Installation

```bash
# Using npm
npm install @sbi/bco-react-native-sdk

# Using yarn
yarn add @sbi/bco-react-native-sdk

# iOS specific
cd ios && pod install
```

### Setup

#### 1. Initialize SDK
```typescript
// App.tsx
import { BCOSdk } from '@sbi/bco-react-native-sdk';

const App = () => {
  useEffect(() => {
    BCOSdk.initialize({
      baseUrl: 'https://api.sbi.com/bco/v1',
      apiKey: 'your-api-key',
      environment: 'production',
    });
  }, []);

  return <AppNavigator />;
};
```

#### 2. Create BCO Provider
```typescript
// providers/BCOProvider.tsx
import React, { createContext, useContext, useReducer } from 'react';
import { BCOState, BCOAction } from '@sbi/bco-react-native-sdk';

const BCOContext = createContext<{
  state: BCOState;
  dispatch: React.Dispatch<BCOAction>;
} | null>(null);

export const BCOProvider: React.FC = ({ children }) => {
  const [state, dispatch] = useReducer(bcoReducer, initialState);

  return (
    <BCOContext.Provider value={{ state, dispatch }}>
      {children}
    </BCOContext.Provider>
  );
};

export const useBCO = () => {
  const context = useContext(BCOContext);
  if (!context) {
    throw new Error('useBCO must be used within BCOProvider');
  }
  return context;
};
```

#### 3. BCO Screen Components
```typescript
// screens/BCODashboard.tsx
import React, { useEffect } from 'react';
import {
  View,
  Text,
  FlatList,
  StyleSheet,
  TouchableOpacity,
} from 'react-native';
import {
  BCOTransactionCard,
  BCOSearchBar,
  BCOFilter,
  useBCOTransactions,
} from '@sbi/bco-react-native-sdk';

export const BCODashboard = () => {
  const {
    transactions,
    loading,
    error,
    loadTransactions,
    refreshTransactions,
  } = useBCOTransactions();

  useEffect(() => {
    loadTransactions({ status: 'PENDING' });
  }, []);

  const renderTransaction = ({ item }) => (
    <BCOTransactionCard
      transaction={item}
      onPress={() => navigateToDetail(item)}
    />
  );

  if (loading) {
    return <BCOLoadingView />;
  }

  if (error) {
    return <BCOErrorView error={error} onRetry={loadTransactions} />;
  }

  return (
    <View style={styles.container}>
      <BCOSearchBar onSearch={handleSearch} />
      <BCOFilter onFilterChange={handleFilterChange} />
      
      <FlatList
        data={transactions}
        renderItem={renderTransaction}
        keyExtractor={(item) => item.referenceNo}
        refreshing={loading}
        onRefresh={refreshTransactions}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
});
```

#### 4. DSO Referral Implementation
```typescript
// screens/DSOReferal.tsx
import React, { useState } from 'react';
import {
  View,
  ScrollView,
  TextInput,
  Button,
  Alert,
} from 'react-native';
import { BCOApi, BCOValidators } from '@sbi/bco-react-native-sdk';

export const DSOReferal = ({ route, navigation }) => {
  const { referenceNo } = route.params;
  const [formData, setFormData] = useState({
    reasonForReferal: '',
    bcoRemarks: '',
    isApproved: false,
  });
  const [loading, setLoading] = useState(false);

  const handleSubmit = async () => {
    if (!BCOValidators.validateDSOForm(formData)) {
      Alert.alert('Error', 'Please fill all required fields');
      return;
    }

    setLoading(true);
    try {
      const response = await BCOApi.submitDSOReferal({
        referenceNo,
        ...formData,
      });

      if (response.success) {
        Alert.alert('Success', 'DSO Referral submitted successfully');
        navigation.goBack();
      }
    } catch (error) {
      Alert.alert('Error', error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <ScrollView style={styles.container}>
      <BCOTransactionInfo referenceNo={referenceNo} />
      
      <TextInput
        style={styles.input}
        placeholder="Reason for Referral"
        value={formData.reasonForReferal}
        onChangeText={(text) =>
          setFormData({ ...formData, reasonForReferal: text })
        }
        multiline
      />
      
      <TextInput
        style={styles.input}
        placeholder="BCO Remarks"
        value={formData.bcoRemarks}
        onChangeText={(text) =>
          setFormData({ ...formData, bcoRemarks: text })
        }
        multiline
      />
      
      <BCOApprovalButtons
        onApprove={() => setFormData({ ...formData, isApproved: true })}
        onReject={() => setFormData({ ...formData, isApproved: false })}
        isApproved={formData.isApproved}
      />
      
      <Button
        title="Submit"
        onPress={handleSubmit}
        disabled={loading}
      />
    </ScrollView>
  );
};
```

### Navigation Setup
```typescript
// navigation/BCONavigator.tsx
import { createStackNavigator } from '@react-navigation/stack';
import {
  BCODashboard,
  BCOMainScreen,
  DSOReferal,
  CAAScreen,
  ORScreen,
} from '../screens';

const Stack = createStackNavigator();

export const BCONavigator = () => (
  <Stack.Navigator>
    <Stack.Screen name="BCODashboard" component={BCODashboard} />
    <Stack.Screen name="BCOMain" component={BCOMainScreen} />
    <Stack.Screen name="DSOReferal" component={DSOReferal} />
    <Stack.Screen name="CAA" component={CAAScreen} />
    <Stack.Screen name="OR" component={ORScreen} />
  </Stack.Navigator>
);
```

## React Web Integration

### Installation
```bash
npm install @sbi/bco-react-sdk
```

### Setup

#### 1. Create BCO Context
```typescript
// contexts/BCOContext.tsx
import React, { createContext, useContext, useEffect, useState } from 'react';
import { BCOSdk, BCOTransaction } from '@sbi/bco-react-sdk';

interface BCOContextType {
  transactions: BCOTransaction[];
  loading: boolean;
  error: string | null;
  loadTransactions: (filters?: any) => Promise<void>;
  submitDSOReferal: (data: any) => Promise<void>;
}

const BCOContext = createContext<BCOContextType | null>(null);

export const BCOProvider: React.FC = ({ children }) => {
  const [transactions, setTransactions] = useState<BCOTransaction[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    BCOSdk.initialize({
      baseUrl: process.env.REACT_APP_BCO_API_URL,
      apiKey: process.env.REACT_APP_BCO_API_KEY,
    });
  }, []);

  const loadTransactions = async (filters?: any) => {
    setLoading(true);
    setError(null);
    try {
      const response = await BCOSdk.api.getTransactions(filters);
      setTransactions(response.data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const submitDSOReferal = async (data: any) => {
    setLoading(true);
    try {
      await BCOSdk.api.submitDSOReferal(data);
      await loadTransactions();
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return (
    <BCOContext.Provider
      value={{
        transactions,
        loading,
        error,
        loadTransactions,
        submitDSOReferal,
      }}
    >
      {children}
    </BCOContext.Provider>
  );
};

export const useBCO = () => {
  const context = useContext(BCOContext);
  if (!context) {
    throw new Error('useBCO must be used within BCOProvider');
  }
  return context;
};
```

#### 2. BCO Dashboard Component
```typescript
// components/BCODashboard.tsx
import React, { useEffect, useState } from 'react';
import {
  Grid,
  Card,
  CardContent,
  Typography,
  Button,
  TextField,
  Select,
  MenuItem,
} from '@mui/material';
import { useBCO } from '../contexts/BCOContext';
import { BCOTransactionList } from './BCOTransactionList';
import { BCOFilters } from './BCOFilters';

export const BCODashboard: React.FC = () => {
  const { transactions, loading, error, loadTransactions } = useBCO();
  const [filters, setFilters] = useState({
    status: 'ALL',
    transactionType: 'ALL',
    searchQuery: '',
  });

  useEffect(() => {
    loadTransactions(filters);
  }, [filters]);

  const handleFilterChange = (newFilters: any) => {
    setFilters({ ...filters, ...newFilters });
  };

  if (error) {
    return (
      <Alert severity="error">
        {error}
        <Button onClick={() => loadTransactions(filters)}>Retry</Button>
      </Alert>
    );
  }

  return (
    <Grid container spacing={3}>
      <Grid item xs={12}>
        <Typography variant="h4">BCO Dashboard</Typography>
      </Grid>
      
      <Grid item xs={12}>
        <BCOFilters
          filters={filters}
          onChange={handleFilterChange}
        />
      </Grid>
      
      <Grid item xs={12}>
        <BCOTransactionList
          transactions={transactions}
          loading={loading}
          onTransactionClick={(transaction) => {
            // Navigate to detail
          }}
        />
      </Grid>
    </Grid>
  );
};
```

#### 3. DSO Referral Form
```typescript
// components/DSOReferralForm.tsx
import React from 'react';
import { useForm, Controller } from 'react-hook-form';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  FormControl,
  RadioGroup,
  FormControlLabel,
  Radio,
} from '@mui/material';
import { useBCO } from '../contexts/BCOContext';

interface DSOFormData {
  reasonForReferal: string;
  bcoRemarks: string;
  isApproved: boolean;
}

export const DSOReferralForm: React.FC<{
  referenceNo: string;
  open: boolean;
  onClose: () => void;
}> = ({ referenceNo, open, onClose }) => {
  const { submitDSOReferal } = useBCO();
  const { control, handleSubmit, formState: { errors } } = useForm<DSOFormData>();

  const onSubmit = async (data: DSOFormData) => {
    try {
      await submitDSOReferal({
        referenceNo,
        ...data,
      });
      onClose();
    } catch (error) {
      console.error('Submission failed:', error);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
      <form onSubmit={handleSubmit(onSubmit)}>
        <DialogTitle>DSO Referral - {referenceNo}</DialogTitle>
        
        <DialogContent>
          <Controller
            name="reasonForReferal"
            control={control}
            rules={{ required: 'Reason is required' }}
            render={({ field }) => (
              <TextField
                {...field}
                label="Reason for Referral"
                multiline
                rows={3}
                fullWidth
                margin="normal"
                error={!!errors.reasonForReferal}
                helperText={errors.reasonForReferal?.message}
              />
            )}
          />
          
          <Controller
            name="bcoRemarks"
            control={control}
            rules={{ required: 'BCO remarks are required' }}
            render={({ field }) => (
              <TextField
                {...field}
                label="BCO Remarks"
                multiline
                rows={3}
                fullWidth
                margin="normal"
                error={!!errors.bcoRemarks}
                helperText={errors.bcoRemarks?.message}
              />
            )}
          />
          
          <Controller
            name="isApproved"
            control={control}
            defaultValue={false}
            render={({ field }) => (
              <FormControl margin="normal">
                <RadioGroup {...field} row>
                  <FormControlLabel
                    value={true}
                    control={<Radio />}
                    label="Approve"
                  />
                  <FormControlLabel
                    value={false}
                    control={<Radio />}
                    label="Reject"
                  />
                </RadioGroup>
              </FormControl>
            )}
          />
        </DialogContent>
        
        <DialogActions>
          <Button onClick={onClose}>Cancel</Button>
          <Button type="submit" variant="contained" color="primary">
            Submit
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
};
```

## Angular Integration

### Installation
```bash
npm install @sbi/bco-angular-sdk
```

### Setup

#### 1. BCO Module
```typescript
// bco.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BCOSdkModule } from '@sbi/bco-angular-sdk';
import { BCORoutingModule } from './bco-routing.module';
import { BCODashboardComponent } from './components/bco-dashboard/bco-dashboard.component';
import { DSORefferalComponent } from './components/dso-refferal/dso-refferal.component';

@NgModule({
  declarations: [
    BCODashboardComponent,
    DSORefferalComponent,
  ],
  imports: [
    CommonModule,
    BCORoutingModule,
    BCOSdkModule.forRoot({
      baseUrl: 'https://api.sbi.com/bco/v1',
      apiKey: 'your-api-key',
    }),
  ],
})
export class BCOModule {}
```

#### 2. BCO Service
```typescript
// services/bco.service.ts
import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { BCOSdk, BCOTransaction } from '@sbi/bco-angular-sdk';

@Injectable({
  providedIn: 'root',
})
export class BCOService {
  private transactionsSubject = new BehaviorSubject<BCOTransaction[]>([]);
  public transactions$ = this.transactionsSubject.asObservable();
  
  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$ = this.loadingSubject.asObservable();

  constructor(private bcoSdk: BCOSdk) {}

  loadTransactions(filters?: any): Observable<BCOTransaction[]> {
    this.loadingSubject.next(true);
    
    return new Observable(observer => {
      this.bcoSdk.api.getTransactions(filters)
        .then(response => {
          this.transactionsSubject.next(response.data);
          observer.next(response.data);
          observer.complete();
        })
        .catch(error => {
          observer.error(error);
        })
        .finally(() => {
          this.loadingSubject.next(false);
        });
    });
  }

  submitDSOReferal(data: any): Observable<any> {
    return new Observable(observer => {
      this.bcoSdk.api.submitDSOReferal(data)
        .then(response => {
          observer.next(response);
          observer.complete();
          // Reload transactions
          this.loadTransactions().subscribe();
        })
        .catch(error => {
          observer.error(error);
        });
    });
  }
}
```

#### 3. BCO Dashboard Component
```typescript
// components/bco-dashboard/bco-dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BCOService } from '../../services/bco.service';
import { BCOTransaction } from '@sbi/bco-angular-sdk';

@Component({
  selector: 'app-bco-dashboard',
  templateUrl: './bco-dashboard.component.html',
  styleUrls: ['./bco-dashboard.component.scss'],
})
export class BCODashboardComponent implements OnInit {
  transactions$ = this.bcoService.transactions$;
  loading$ = this.bcoService.loading$;
  
  filters = {
    status: 'ALL',
    transactionType: 'ALL',
    searchQuery: '',
  };

  constructor(
    private bcoService: BCOService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadTransactions();
  }

  loadTransactions(): void {
    this.bcoService.loadTransactions(this.filters).subscribe();
  }

  onFilterChange(filters: any): void {
    this.filters = { ...this.filters, ...filters };
    this.loadTransactions();
  }

  onTransactionClick(transaction: BCOTransaction): void {
    this.router.navigate(['/bco/transaction', transaction.referenceNo]);
  }
}
```

```html
<!-- bco-dashboard.component.html -->
<div class="bco-dashboard">
  <h2>BCO Dashboard</h2>
  
  <bco-filters
    [filters]="filters"
    (filterChange)="onFilterChange($event)"
  ></bco-filters>
  
  <div *ngIf="loading$ | async" class="loading">
    <mat-spinner></mat-spinner>
  </div>
  
  <div *ngIf="!(loading$ | async)" class="transactions-grid">
    <mat-card
      *ngFor="let transaction of transactions$ | async"
      (click)="onTransactionClick(transaction)"
      class="transaction-card"
    >
      <mat-card-header>
        <mat-card-title>{{ transaction.referenceNo }}</mat-card-title>
        <mat-card-subtitle>{{ transaction.transactionType }}</mat-card-subtitle>
      </mat-card-header>
      <mat-card-content>
        <p>{{ transaction.customerName }}</p>
        <p>Status: {{ transaction.status }}</p>
      </mat-card-content>
    </mat-card>
  </div>
</div>
```

## Vue.js Integration

### Installation
```bash
npm install @sbi/bco-vue-sdk
```

### Setup

#### 1. Vue Plugin
```javascript
// plugins/bco.js
import { BCOSdk } from '@sbi/bco-vue-sdk';

export default {
  install(app, options) {
    const bcoSdk = new BCOSdk({
      baseUrl: options.baseUrl || 'https://api.sbi.com/bco/v1',
      apiKey: options.apiKey,
    });

    app.config.globalProperties.$bco = bcoSdk;
    app.provide('bco', bcoSdk);
  },
};
```

#### 2. Main Setup
```javascript
// main.js
import { createApp } from 'vue';
import App from './App.vue';
import BCOPlugin from './plugins/bco';

const app = createApp(App);

app.use(BCOPlugin, {
  baseUrl: process.env.VUE_APP_BCO_API_URL,
  apiKey: process.env.VUE_APP_BCO_API_KEY,
});

app.mount('#app');
```

#### 3. BCO Composable
```javascript
// composables/useBCO.js
import { ref, computed, inject } from 'vue';

export function useBCO() {
  const bco = inject('bco');
  const transactions = ref([]);
  const loading = ref(false);
  const error = ref(null);

  const loadTransactions = async (filters = {}) => {
    loading.value = true;
    error.value = null;
    
    try {
      const response = await bco.api.getTransactions(filters);
      transactions.value = response.data;
    } catch (err) {
      error.value = err.message;
    } finally {
      loading.value = false;
    }
  };

  const submitDSOReferal = async (data) => {
    loading.value = true;
    
    try {
      const response = await bco.api.submitDSOReferal(data);
      await loadTransactions();
      return response;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  return {
    transactions: computed(() => transactions.value),
    loading: computed(() => loading.value),
    error: computed(() => error.value),
    loadTransactions,
    submitDSOReferal,
  };
}
```

#### 4. BCO Dashboard Component
```vue
<!-- components/BCODashboard.vue -->
<template>
  <div class="bco-dashboard">
    <h2>BCO Dashboard</h2>
    
    <BCOFilters
      :filters="filters"
      @update:filters="updateFilters"
    />
    
    <div v-if="loading" class="loading">
      <Spinner />
    </div>
    
    <div v-else-if="error" class="error">
      {{ error }}
      <button @click="retry">Retry</button>
    </div>
    
    <div v-else class="transactions-grid">
      <BCOTransactionCard
        v-for="transaction in transactions"
        :key="transaction.referenceNo"
        :transaction="transaction"
        @click="openTransaction(transaction)"
      />
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useBCO } from '@/composables/useBCO';
import BCOFilters from './BCOFilters.vue';
import BCOTransactionCard from './BCOTransactionCard.vue';

export default {
  name: 'BCODashboard',
  components: {
    BCOFilters,
    BCOTransactionCard,
  },
  setup() {
    const router = useRouter();
    const { transactions, loading, error, loadTransactions } = useBCO();
    
    const filters = ref({
      status: 'ALL',
      transactionType: 'ALL',
      searchQuery: '',
    });

    const updateFilters = (newFilters) => {
      filters.value = { ...filters.value, ...newFilters };
      loadTransactions(filters.value);
    };

    const openTransaction = (transaction) => {
      router.push({
        name: 'BCOTransaction',
        params: { referenceNo: transaction.referenceNo },
      });
    };

    const retry = () => {
      loadTransactions(filters.value);
    };

    onMounted(() => {
      loadTransactions(filters.value);
    });

    return {
      transactions,
      loading,
      error,
      filters,
      updateFilters,
      openTransaction,
      retry,
    };
  },
};
</script>
```

## iOS Native Integration

### Installation

#### CocoaPods
```ruby
# Podfile
pod 'BCOSDKiOS', '~> 1.0'
```

#### Swift Package Manager
```swift
dependencies: [
    .package(url: "https://github.com/sbi/bco-ios-sdk.git", from: "1.0.0")
]
```

### Implementation

#### 1. SDK Initialization
```swift
// AppDelegate.swift
import BCOSDKiOS

class AppDelegate: UIResponder, UIApplicationDelegate {
    func application(_ application: UIApplication,
                    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        // Initialize BCO SDK
        BCOSdk.shared.initialize(
            config: BCOConfig(
                baseURL: "https://api.sbi.com/bco/v1",
                apiKey: "your-api-key",
                environment: .production
            )
        )
        
        return true
    }
}
```

#### 2. BCO Service
```swift
// Services/BCOService.swift
import Foundation
import BCOSDKiOS
import Combine

class BCOService: ObservableObject {
    @Published var transactions: [BCOTransaction] = []
    @Published var isLoading = false
    @Published var error: Error?
    
    private var cancellables = Set<AnyCancellable>()
    private let api = BCOSdk.shared.api
    
    func loadTransactions(filters: BCOFilters? = nil) {
        isLoading = true
        error = nil
        
        api.getTransactions(filters: filters)
            .receive(on: DispatchQueue.main)
            .sink(
                receiveCompletion: { [weak self] completion in
                    self?.isLoading = false
                    if case .failure(let error) = completion {
                        self?.error = error
                    }
                },
                receiveValue: { [weak self] response in
                    self?.transactions = response.transactions
                }
            )
            .store(in: &cancellables)
    }
    
    func submitDSOReferal(_ referal: DSOReferal) -> AnyPublisher<BCOResponse, Error> {
        return api.submitDSOReferal(referal)
            .receive(on: DispatchQueue.main)
            .eraseToAnyPublisher()
    }
}
```

#### 3. SwiftUI Views
```swift
// Views/BCODashboardView.swift
import SwiftUI
import BCOSDKiOS

struct BCODashboardView: View {
    @StateObject private var bcoService = BCOService()
    @State private var selectedTransaction: BCOTransaction?
    @State private var showFilters = false
    
    var body: some View {
        NavigationView {
            ZStack {
                if bcoService.isLoading {
                    ProgressView("Loading...")
                } else if let error = bcoService.error {
                    ErrorView(error: error) {
                        bcoService.loadTransactions()
                    }
                } else {
                    transactionsList
                }
            }
            .navigationTitle("BCO Dashboard")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: { showFilters.toggle() }) {
                        Image(systemName: "line.horizontal.3.decrease.circle")
                    }
                }
            }
            .sheet(isPresented: $showFilters) {
                BCOFiltersView { filters in
                    bcoService.loadTransactions(filters: filters)
                    showFilters = false
                }
            }
            .sheet(item: $selectedTransaction) { transaction in
                BCOTransactionDetailView(transaction: transaction)
            }
        }
        .onAppear {
            bcoService.loadTransactions()
        }
    }
    
    private var transactionsList: some View {
        List(bcoService.transactions) { transaction in
            BCOTransactionRow(transaction: transaction)
                .onTapGesture {
                    selectedTransaction = transaction
                }
        }
        .refreshable {
            await withCheckedContinuation { continuation in
                bcoService.loadTransactions()
                continuation.resume()
            }
        }
    }
}
```

#### 4. UIKit Implementation
```swift
// ViewControllers/BCODashboardViewController.swift
import UIKit
import BCOSDKiOS

class BCODashboardViewController: UIViewController {
    @IBOutlet weak var tableView: UITableView!
    
    private let bcoService = BCOService()
    private var transactions: [BCOTransaction] = []
    private let refreshControl = UIRefreshControl()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        loadTransactions()
    }
    
    private func setupUI() {
        title = "BCO Dashboard"
        
        tableView.delegate = self
        tableView.dataSource = self
        tableView.refreshControl = refreshControl
        
        refreshControl.addTarget(self, action: #selector(refreshData), for: .valueChanged)
    }
    
    private func loadTransactions() {
        BCOSdk.shared.api.getTransactions { [weak self] result in
            DispatchQueue.main.async {
                self?.refreshControl.endRefreshing()
                
                switch result {
                case .success(let response):
                    self?.transactions = response.transactions
                    self?.tableView.reloadData()
                case .failure(let error):
                    self?.showError(error)
                }
            }
        }
    }
    
    @objc private func refreshData() {
        loadTransactions()
    }
    
    private func showError(_ error: Error) {
        let alert = UIAlertController(
            title: "Error",
            message: error.localizedDescription,
            preferredStyle: .alert
        )
        alert.addAction(UIAlertAction(title: "OK", style: .default))
        present(alert, animated: true)
    }
}

// MARK: - UITableViewDataSource
extension BCODashboardViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return transactions.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "TransactionCell", for: indexPath) as! BCOTransactionCell
        cell.configure(with: transactions[indexPath.row])
        return cell
    }
}

// MARK: - UITableViewDelegate
extension BCODashboardViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let transaction = transactions[indexPath.row]
        performSegue(withIdentifier: "showDetail", sender: transaction)
    }
}
```

## Android Native Integration

### Installation

#### Gradle
```gradle
// app/build.gradle
dependencies {
    implementation 'com.sbi.bco:bco-android-sdk:1.0.0'
}
```

### Implementation

#### 1. SDK Initialization
```kotlin
// Application.kt
import com.sbi.bco.sdk.BCOSdk
import com.sbi.bco.sdk.BCOConfig

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize BCO SDK
        BCOSdk.initialize(
            context = this,
            config = BCOConfig(
                baseUrl = "https://api.sbi.com/bco/v1",
                apiKey = "your-api-key",
                environment = BCOConfig.Environment.PRODUCTION
            )
        )
    }
}
```

#### 2. BCO Repository
```kotlin
// repository/BCORepository.kt
import com.sbi.bco.sdk.BCOSdk
import com.sbi.bco.sdk.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BCORepository @Inject constructor() {
    private val api = BCOSdk.getInstance().api
    
    fun getTransactions(filters: BCOFilters? = null): Flow<List<BCOTransaction>> = flow {
        try {
            val response = api.getTransactions(filters)
            emit(response.transactions)
        } catch (e: Exception) {
            throw BCOException("Failed to load transactions", e)
        }
    }
    
    suspend fun submitDSOReferal(referal: DSOReferal): BCOResponse {
        return api.submitDSOReferal(referal)
    }
    
    suspend fun getCAAObservations(referenceNo: String): List<CAAObservation> {
        return api.getCAAObservations(referenceNo)
    }
}
```

#### 3. ViewModel
```kotlin
// viewmodel/BCOViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbi.bco.sdk.models.BCOTransaction
import com.sbi.bco.sdk.models.BCOFilters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BCOViewModel @Inject constructor(
    private val repository: BCORepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(BCOUiState())
    val uiState: StateFlow<BCOUiState> = _uiState.asStateFlow()
    
    fun loadTransactions(filters: BCOFilters? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            repository.getTransactions(filters)
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
                .collect { transactions ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            transactions = transactions,
                            error = null
                        )
                    }
                }
        }
    }
    
    fun submitDSOReferal(referal: DSOReferal) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isSubmitting = true) }
                repository.submitDSOReferal(referal)
                loadTransactions() // Refresh
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        error = e.message
                    )
                }
            }
        }
    }
}

data class BCOUiState(
    val transactions: List<BCOTransaction> = emptyList(),
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null
)
```

#### 4. Compose UI
```kotlin
// ui/BCODashboardScreen.kt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BCODashboardScreen(
    viewModel: BCOViewModel = hiltViewModel(),
    onTransactionClick: (BCOTransaction) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadTransactions()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BCO Dashboard") },
                actions = {
                    IconButton(onClick = { /* Show filters */ }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    ErrorMessage(
                        error = uiState.error,
                        onRetry = { viewModel.loadTransactions() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    TransactionsList(
                        transactions = uiState.transactions,
                        onTransactionClick = onTransactionClick
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionsList(
    transactions: List<BCOTransaction>,
    onTransactionClick: (BCOTransaction) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(transactions) { transaction ->
            BCOTransactionCard(
                transaction = transaction,
                onClick = { onTransactionClick(transaction) }
            )
        }
    }
}

@Composable
private fun BCOTransactionCard(
    transaction: BCOTransaction,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = transaction.referenceNo,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = transaction.customerName,
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = transaction.transactionType,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = transaction.status,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
```

#### 5. XML Layout Implementation
```kotlin
// activities/BCODashboardActivity.kt
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sbi.bco.sdk.models.BCOTransaction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BCODashboardActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityBcoDashboardBinding
    private val viewModel: BCOViewModel by viewModels()
    private lateinit var adapter: BCOTransactionAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBcoDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        observeViewModel()
    }
    
    private fun setupUI() {
        adapter = BCOTransactionAdapter { transaction ->
            openTransactionDetail(transaction)
        }
        
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@BCODashboardActivity)
            adapter = this@BCODashboardActivity.adapter
        }
        
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadTransactions()
        }
    }
    
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.swipeRefreshLayout.isRefreshing = state.isLoading
                
                state.error?.let { error ->
                    showError(error)
                }
                
                adapter.submitList(state.transactions)
            }
        }
    }
    
    private fun openTransactionDetail(transaction: BCOTransaction) {
        startActivity(
            BCODetailActivity.newIntent(this, transaction.referenceNo)
        )
    }
    
    private fun showError(error: String) {
        Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG)
            .setAction("Retry") {
                viewModel.loadTransactions()
            }
            .show()
    }
}
```

## Ionic Integration

### Installation
```bash
npm install @sbi/bco-ionic-sdk
ionic cap sync
```

### Implementation

#### 1. BCO Service
```typescript
// services/bco.service.ts
import { Injectable } from '@angular/core';
import { BCOSdk, BCOTransaction } from '@sbi/bco-ionic-sdk';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BCOService {
  private sdk: BCOSdk;
  private transactionsSubject = new BehaviorSubject<BCOTransaction[]>([]);
  
  transactions$ = this.transactionsSubject.asObservable();

  constructor() {
    this.sdk = new BCOSdk({
      baseUrl: 'https://api.sbi.com/bco/v1',
      apiKey: 'your-api-key'
    });
  }

  async loadTransactions(filters?: any): Promise<void> {
    try {
      const response = await this.sdk.api.getTransactions(filters);
      this.transactionsSubject.next(response.transactions);
    } catch (error) {
      throw error;
    }
  }

  async submitDSOReferal(data: any): Promise<any> {
    return this.sdk.api.submitDSOReferal(data);
  }
}
```

#### 2. BCO Page Component
```typescript
// pages/bco-dashboard/bco-dashboard.page.ts
import { Component, OnInit } from '@angular/core';
import { ModalController } from '@ionic/angular';
import { BCOService } from '../../services/bco.service';
import { DSORefferalModal } from '../../modals/dso-refferal/dso-refferal.modal';

@Component({
  selector: 'app-bco-dashboard',
  templateUrl: './bco-dashboard.page.html',
  styleUrls: ['./bco-dashboard.page.scss'],
})
export class BCODashboardPage implements OnInit {
  transactions$ = this.bcoService.transactions$;
  isLoading = false;

  constructor(
    private bcoService: BCOService,
    private modalController: ModalController
  ) {}

  ngOnInit() {
    this.loadTransactions();
  }

  async loadTransactions(event?: any) {
    this.isLoading = true;
    try {
      await this.bcoService.loadTransactions();
    } catch (error) {
      console.error('Error loading transactions:', error);
    } finally {
      this.isLoading = false;
      if (event) {
        event.target.complete();
      }
    }
  }

  async openDSOReferal(transaction: any) {
    const modal = await this.modalController.create({
      component: DSORefferalModal,
      componentProps: {
        transaction
      }
    });

    await modal.present();
    
    const { data } = await modal.onWillDismiss();
    if (data?.submitted) {
      this.loadTransactions();
    }
  }
}
```

```html
<!-- bco-dashboard.page.html -->
<ion-header>
  <ion-toolbar>
    <ion-title>BCO Dashboard</ion-title>
    <ion-buttons slot="end">
      <ion-button (click)="showFilters()">
        <ion-icon name="filter-outline"></ion-icon>
      </ion-button>
    </ion-buttons>
  </ion-toolbar>
</ion-header>

<ion-content>
  <ion-refresher slot="fixed" (ionRefresh)="loadTransactions($event)">
    <ion-refresher-content></ion-refresher-content>
  </ion-refresher>

  <ion-progress-bar *ngIf="isLoading" type="indeterminate"></ion-progress-bar>

  <ion-list>
    <ion-item
      *ngFor="let transaction of transactions$ | async"
      (click)="openTransaction(transaction)"
      button
    >
      <ion-label>
        <h2>{{ transaction.referenceNo }}</h2>
        <h3>{{ transaction.customerName }}</h3>
        <p>{{ transaction.transactionType }} - {{ transaction.status }}</p>
      </ion-label>
      <ion-note slot="end">{{ transaction.amount | currency }}</ion-note>
    </ion-item>
  </ion-list>
</ion-content>
```

## Xamarin Integration

### Installation
```bash
# NuGet Package Manager
Install-Package SBI.BCO.Xamarin.SDK
```

### Implementation

#### 1. SDK Initialization
```csharp
// App.xaml.cs
using SBI.BCO.SDK;

public partial class App : Application
{
    public App()
    {
        InitializeComponent();
        
        // Initialize BCO SDK
        BCOSdk.Initialize(new BCOConfig
        {
            BaseUrl = "https://api.sbi.com/bco/v1",
            ApiKey = "your-api-key",
            Environment = BCOEnvironment.Production
        });
        
        MainPage = new NavigationPage(new MainPage());
    }
}
```

#### 2. BCO Service
```csharp
// Services/BCOService.cs
using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using SBI.BCO.SDK;
using SBI.BCO.SDK.Models;

public class BCOService
{
    private readonly BCOApi _api;
    
    public BCOService()
    {
        _api = BCOSdk.Instance.Api;
    }
    
    public async Task<List<BCOTransaction>> GetTransactionsAsync(BCOFilters filters = null)
    {
        try
        {
            var response = await _api.GetTransactionsAsync(filters);
            return response.Transactions;
        }
        catch (BCOException ex)
        {
            throw new Exception($"Failed to load transactions: {ex.Message}", ex);
        }
    }
    
    public async Task<BCOResponse> SubmitDSOReferalAsync(DSOReferal referal)
    {
        return await _api.SubmitDSOReferalAsync(referal);
    }
}
```

#### 3. ViewModel
```csharp
// ViewModels/BCODashboardViewModel.cs
using System;
using System.Collections.ObjectModel;
using System.Threading.Tasks;
using System.Windows.Input;
using Xamarin.Forms;
using SBI.BCO.SDK.Models;

public class BCODashboardViewModel : BaseViewModel
{
    private readonly BCOService _bcoService;
    private bool _isLoading;
    private string _error;
    
    public ObservableCollection<BCOTransaction> Transactions { get; }
    
    public bool IsLoading
    {
        get => _isLoading;
        set => SetProperty(ref _isLoading, value);
    }
    
    public string Error
    {
        get => _error;
        set => SetProperty(ref _error, value);
    }
    
    public ICommand LoadTransactionsCommand { get; }
    public ICommand TransactionSelectedCommand { get; }
    
    public BCODashboardViewModel()
    {
        _bcoService = new BCOService();
        Transactions = new ObservableCollection<BCOTransaction>();
        
        LoadTransactionsCommand = new Command(async () => await LoadTransactions());
        TransactionSelectedCommand = new Command<BCOTransaction>(async (t) => await OnTransactionSelected(t));
    }
    
    private async Task LoadTransactions()
    {
        IsLoading = true;
        Error = null;
        
        try
        {
            var transactions = await _bcoService.GetTransactionsAsync();
            
            Transactions.Clear();
            foreach (var transaction in transactions)
            {
                Transactions.Add(transaction);
            }
        }
        catch (Exception ex)
        {
            Error = ex.Message;
        }
        finally
        {
            IsLoading = false;
        }
    }
    
    private async Task OnTransactionSelected(BCOTransaction transaction)
    {
        await Shell.Current.GoToAsync($"//transaction?referenceNo={transaction.ReferenceNo}");
    }
}
```

#### 4. XAML View
```xml
<!-- Views/BCODashboardPage.xaml -->
<?xml version="1.0" encoding="utf-8"?>
<ContentPage xmlns="http://xamarin.com/schemas/2014/forms"
             xmlns:x="http://schemas.microsoft.com/winfx/2009/xaml"
             x:Class="BCOApp.Views.BCODashboardPage"
             Title="BCO Dashboard">
    
    <ContentPage.ToolbarItems>
        <ToolbarItem Text="Filter" IconImageSource="filter.png" Command="{Binding ShowFiltersCommand}" />
    </ContentPage.ToolbarItems>
    
    <RefreshView IsRefreshing="{Binding IsLoading}"
                 Command="{Binding LoadTransactionsCommand}">
        <CollectionView ItemsSource="{Binding Transactions}"
                        SelectionMode="Single"
                        SelectedItem="{Binding SelectedTransaction}">
            <CollectionView.ItemTemplate>
                <DataTemplate>
                    <Frame Margin="10,5" Padding="10" HasShadow="True">
                        <StackLayout>
                            <Label Text="{Binding ReferenceNo}"
                                   FontAttributes="Bold"
                                   FontSize="16" />
                            <Label Text="{Binding CustomerName}"
                                   FontSize="14" />
                            <Grid>
                                <Grid.ColumnDefinitions>
                                    <ColumnDefinition Width="*" />
                                    <ColumnDefinition Width="Auto" />
                                </Grid.ColumnDefinitions>
                                <Label Text="{Binding TransactionType}"
                                       Grid.Column="0"
                                       FontSize="12"
                                       TextColor="Gray" />
                                <Label Text="{Binding Status}"
                                       Grid.Column="1"
                                       FontSize="12"
                                       TextColor="{Binding StatusColor}" />
                            </Grid>
                        </StackLayout>
                        <Frame.GestureRecognizers>
                            <TapGestureRecognizer 
                                Command="{Binding Path=TransactionSelectedCommand, Source={RelativeSource AncestorType={x:Type ContentPage}}}"
                                CommandParameter="{Binding .}" />
                        </Frame.GestureRecognizers>
                    </Frame>
                </DataTemplate>
            </CollectionView.ItemTemplate>
        </CollectionView>
    </RefreshView>
</ContentPage>
```

## REST API Direct Integration

For platforms not covered by specific SDKs, you can integrate directly with the BCO REST API.

### Authentication
```bash
# Using API Key
curl -H "Authorization: Bearer YOUR_API_KEY" \
     -H "Request-Id: $(uuidgen)" \
     -H "Sequence: $(date +%s)" \
     https://api.sbi.com/bco/v1/inquire

# Using OAuth 2.0
curl -X POST https://api.sbi.com/oauth/token \
     -d "grant_type=client_credentials" \
     -d "client_id=YOUR_CLIENT_ID" \
     -d "client_secret=YOUR_CLIENT_SECRET"
```

### API Examples

#### Get Transactions
```javascript
// JavaScript/Node.js
const axios = require('axios');

async function getTransactions(filters = {}) {
  try {
    const response = await axios.post('https://api.sbi.com/bco/v1/inquire', {
      corporateId: 'CORP001',
      userId: 'USER001',
      ...filters
    }, {
      headers: {
        'Authorization': `Bearer ${API_KEY}`,
        'Request-Id': generateUUID(),
        'Sequence': Date.now()
      }
    });
    
    return response.data;
  } catch (error) {
    console.error('Error:', error.response?.data || error.message);
    throw error;
  }
}
```

#### Python Integration
```python
# Python
import requests
import uuid
import time

class BCOClient:
    def __init__(self, base_url, api_key):
        self.base_url = base_url
        self.api_key = api_key
        self.session = requests.Session()
        self.session.headers.update({
            'Authorization': f'Bearer {api_key}',
            'Content-Type': 'application/json'
        })
    
    def get_transactions(self, filters=None):
        headers = {
            'Request-Id': str(uuid.uuid4()),
            'Sequence': str(int(time.time() * 1000))
        }
        
        data = {
            'corporateId': 'CORP001',
            'userId': 'USER001'
        }
        if filters:
            data.update(filters)
        
        response = self.session.post(
            f'{self.base_url}/inquire',
            json=data,
            headers=headers
        )
        response.raise_for_status()
        return response.json()
    
    def submit_dso_referal(self, reference_no, data):
        headers = {
            'Request-Id': str(uuid.uuid4()),
            'Sequence': str(int(time.time() * 1000))
        }
        
        response = self.session.post(
            f'{self.base_url}/dso-referal/{reference_no}/submit',
            json=data,
            headers=headers
        )
        response.raise_for_status()
        return response.json()

# Usage
client = BCOClient('https://api.sbi.com/bco/v1', 'your-api-key')
transactions = client.get_transactions({'status': 'PENDING'})
```

#### PHP Integration
```php
<?php
// PHP
class BCOClient {
    private $baseUrl;
    private $apiKey;
    
    public function __construct($baseUrl, $apiKey) {
        $this->baseUrl = $baseUrl;
        $this->apiKey = $apiKey;
    }
    
    public function getTransactions($filters = []) {
        $data = array_merge([
            'corporateId' => 'CORP001',
            'userId' => 'USER001'
        ], $filters);
        
        $headers = [
            'Authorization: Bearer ' . $this->apiKey,
            'Content-Type: application/json',
            'Request-Id: ' . $this->generateUUID(),
            'Sequence: ' . round(microtime(true) * 1000)
        ];
        
        $ch = curl_init($this->baseUrl . '/inquire');
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        
        $response = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        curl_close($ch);
        
        if ($httpCode !== 200) {
            throw new Exception('API request failed');
        }
        
        return json_decode($response, true);
    }
    
    private function generateUUID() {
        return sprintf('%04x%04x-%04x-%04x-%04x-%04x%04x%04x',
            mt_rand(0, 0xffff), mt_rand(0, 0xffff),
            mt_rand(0, 0xffff),
            mt_rand(0, 0x0fff) | 0x4000,
            mt_rand(0, 0x3fff) | 0x8000,
            mt_rand(0, 0xffff), mt_rand(0, 0xffff), mt_rand(0, 0xffff)
        );
    }
}

// Usage
$client = new BCOClient('https://api.sbi.com/bco/v1', 'your-api-key');
$transactions = $client->getTransactions(['status' => 'PENDING']);
```

## WebSocket Integration

For real-time updates, use the BCO WebSocket API.

### JavaScript WebSocket Client
```javascript
class BCOWebSocket {
  constructor(url, apiKey) {
    this.url = url;
    this.apiKey = apiKey;
    this.ws = null;
    this.listeners = new Map();
  }
  
  connect() {
    this.ws = new WebSocket(`${this.url}?token=${this.apiKey}`);
    
    this.ws.onopen = () => {
      console.log('BCO WebSocket connected');
      this.subscribe(['transaction.created', 'transaction.updated']);
    };
    
    this.ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      this.emit(data.event, data.payload);
    };
    
    this.ws.onerror = (error) => {
      console.error('BCO WebSocket error:', error);
    };
    
    this.ws.onclose = () => {
      console.log('BCO WebSocket disconnected');
      // Implement reconnection logic
      setTimeout(() => this.connect(), 5000);
    };
  }
  
  subscribe(events) {
    this.send({
      type: 'subscribe',
      events: events
    });
  }
  
  on(event, callback) {
    if (!this.listeners.has(event)) {
      this.listeners.set(event, []);
    }
    this.listeners.get(event).push(callback);
  }
  
  emit(event, data) {
    const callbacks = this.listeners.get(event) || [];
    callbacks.forEach(callback => callback(data));
  }
  
  send(data) {
    if (this.ws?.readyState === WebSocket.OPEN) {
      this.ws.send(JSON.stringify(data));
    }
  }
}

// Usage
const bcoWs = new BCOWebSocket('wss://api.sbi.com/bco/v1/ws', 'your-api-key');
bcoWs.connect();

bcoWs.on('transaction.created', (transaction) => {
  console.log('New transaction:', transaction);
  updateUI(transaction);
});

bcoWs.on('transaction.updated', (transaction) => {
  console.log('Transaction updated:', transaction);
  updateTransaction(transaction);
});
```

## GraphQL Integration

For GraphQL APIs, use the BCO GraphQL endpoint.

### GraphQL Schema
```graphql
type Query {
  transactions(filters: TransactionFilter): TransactionResponse!
  transaction(referenceNo: String!): Transaction
  dashboardStats: DashboardStats!
}

type Mutation {
  submitDSOReferal(input: DSORefferalInput!): SubmitResponse!
  submitCAAObservations(input: CAAObservationsInput!): SubmitResponse!
  submitOpinionReport(input: OpinionReportInput!): SubmitResponse!
}

type Subscription {
  transactionUpdated(referenceNo: String): Transaction!
  newTransaction: Transaction!
}

input TransactionFilter {
  status: TransactionStatus
  transactionType: TransactionType
  fromDate: Date
  toDate: Date
  branchCode: String
  customerId: String
}

type Transaction {
  referenceNo: String!
  transactionType: TransactionType!
  customerName: String!
  customerId: String!
  branchCode: String!
  branchName: String!
  observationDate: Date!
  status: TransactionStatus!
  amount: Float
  currency: String
  createdAt: DateTime!
  updatedAt: DateTime!
}
```

### GraphQL Client Implementation
```javascript
// Apollo Client (React)
import { ApolloClient, InMemoryCache, gql } from '@apollo/client';

const client = new ApolloClient({
  uri: 'https://api.sbi.com/bco/v1/graphql',
  cache: new InMemoryCache(),
  headers: {
    authorization: `Bearer ${API_KEY}`,
  },
});

// Query transactions
const GET_TRANSACTIONS = gql`
  query GetTransactions($filters: TransactionFilter) {
    transactions(filters: $filters) {
      totalCount
      transactions {
        referenceNo
        transactionType
        customerName
        status
        amount
        currency
      }
    }
  }
`;

// Submit DSO Referral
const SUBMIT_DSO_REFERAL = gql`
  mutation SubmitDSOReferal($input: DSORefferalInput!) {
    submitDSOReferal(input: $input) {
      success
      message
      transaction {
        referenceNo
        status
      }
    }
  }
`;

// Usage in React component
function BCODashboard() {
  const { loading, error, data } = useQuery(GET_TRANSACTIONS, {
    variables: {
      filters: {
        status: 'PENDING',
        transactionType: 'DSO_REFERAL'
      }
    }
  });
  
  const [submitReferal] = useMutation(SUBMIT_DSO_REFERAL);
  
  const handleSubmit = async (referal) => {
    try {
      const result = await submitReferal({
        variables: { input: referal }
      });
      console.log('Submitted:', result);
    } catch (error) {
      console.error('Submission failed:', error);
    }
  };
  
  // Render UI...
}
```

## Common Integration Patterns

### 1. Authentication Handling
```typescript
// Centralized auth interceptor
class AuthInterceptor {
  constructor(private authService: AuthService) {}
  
  async intercept(request: Request): Promise<Request> {
    const token = await this.authService.getToken();
    
    if (!token) {
      throw new Error('No authentication token');
    }
    
    // Check token expiry
    if (this.authService.isTokenExpired(token)) {
      const newToken = await this.authService.refreshToken();
      request.headers.set('Authorization', `Bearer ${newToken}`);
    } else {
      request.headers.set('Authorization', `Bearer ${token}`);
    }
    
    // Add required headers
    request.headers.set('Request-Id', generateUUID());
    request.headers.set('Sequence', Date.now().toString());
    
    return request;
  }
}
```

### 2. Error Handling Pattern
```typescript
// Universal error handler
class BCOErrorHandler {
  static handle(error: any): void {
    if (error.code === 'AUTH_FAILED') {
      // Redirect to login
      Router.navigate('/login');
    } else if (error.code === 'NETWORK_ERROR') {
      // Show offline message
      Toast.show('No internet connection');
    } else if (error.code === 'VALIDATION_ERROR') {
      // Show validation errors
      error.validationErrors.forEach(err => {
        Toast.show(err.message);
      });
    } else {
      // Generic error
      Toast.show('An error occurred. Please try again.');
    }
  }
}
```

### 3. Offline Support Pattern
```typescript
// Offline queue manager
class OfflineQueueManager {
  private queue: OfflineRequest[] = [];
  
  async addToQueue(request: OfflineRequest): Promise<void> {
    this.queue.push(request);
    await this.saveQueue();
  }
  
  async syncQueue(): Promise<void> {
    if (!await this.isOnline()) {
      return;
    }
    
    const queue = [...this.queue];
    this.queue = [];
    
    for (const request of queue) {
      try {
        await this.executeRequest(request);
      } catch (error) {
        // Re-add to queue if failed
        this.queue.push(request);
      }
    }
    
    await this.saveQueue();
  }
  
  private async isOnline(): Promise<boolean> {
    try {
      await fetch('https://api.sbi.com/bco/v1/health');
      return true;
    } catch {
      return false;
    }
  }
}
```

### 4. Caching Strategy
```typescript
// Cache manager
class BCOCacheManager {
  private cache = new Map<string, CacheEntry>();
  private readonly TTL = 5 * 60 * 1000; // 5 minutes
  
  async get<T>(key: string, fetcher: () => Promise<T>): Promise<T> {
    const cached = this.cache.get(key);
    
    if (cached && !this.isExpired(cached)) {
      return cached.data as T;
    }
    
    const data = await fetcher();
    this.cache.set(key, {
      data,
      timestamp: Date.now()
    });
    
    return data;
  }
  
  private isExpired(entry: CacheEntry): boolean {
    return Date.now() - entry.timestamp > this.TTL;
  }
  
  invalidate(pattern?: string): void {
    if (pattern) {
      // Invalidate matching keys
      for (const key of this.cache.keys()) {
        if (key.includes(pattern)) {
          this.cache.delete(key);
        }
      }
    } else {
      // Clear all
      this.cache.clear();
    }
  }
}
```

## Best Practices

### 1. Security
- Always use HTTPS
- Store API keys securely
- Implement token refresh logic
- Validate all inputs
- Use certificate pinning for mobile apps

### 2. Performance
- Implement pagination for large datasets
- Use caching strategically
- Batch API requests when possible
- Implement lazy loading
- Use compression for data transfer

### 3. Error Handling
- Provide meaningful error messages
- Implement retry logic with exponential backoff
- Handle network connectivity issues
- Log errors for debugging
- Show user-friendly error states

### 4. User Experience
- Show loading indicators
- Implement pull-to-refresh
- Provide offline functionality
- Use optimistic updates
- Handle empty states gracefully

## Support

### Documentation
- API Reference: https://api.sbi.com/bco/docs
- SDK Guides: https://docs.sbi.com/bco-sdk
- Sample Apps: https://github.com/sbi/bco-examples

### Contact
- Email: bco-sdk@sbi.com
- Forum: https://forum.sbi.com/bco
- GitHub: https://github.com/sbi/bco-sdk

### Version Compatibility
| SDK Version | API Version | Min Platform Version |
|-------------|-------------|---------------------|
| 1.0.x | v1 | iOS 11, Android 21 |
| 2.0.x | v2 | iOS 13, Android 23 |