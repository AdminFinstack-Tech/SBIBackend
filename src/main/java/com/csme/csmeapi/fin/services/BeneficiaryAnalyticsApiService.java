package com.csme.csmeapi.fin.services;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.csme.csmeapi.fin.models.BeneficiaryStatistics;
import com.csme.csmeapi.fin.models.XBeneficiaryAnalyticsRequest;
import com.csme.csmeapi.fin.models.XBeneficiaryAnalyticsResponse;

@Service
public class BeneficiaryAnalyticsApiService {

    private static final Logger logger = LogManager.getLogger(BeneficiaryAnalyticsApiService.class);

    public XBeneficiaryAnalyticsResponse getBeneficiaryAnalytics(XBeneficiaryAnalyticsRequest request, String requestId) {
        try {
            logger.info("Getting beneficiary analytics for corporate: " + request.getCorporateId() + 
                       ", months: " + request.getMonths());

            XBeneficiaryAnalyticsResponse response = new XBeneficiaryAnalyticsResponse();
            
            // Set response metadata
            response.setStatusCode("00");
            response.setStatusDescription("Beneficiary Analytics Retrieved Successfully");
            response.setGeneratedAt(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date()));

            // Set analysis parameters
            Integer months = request.getMonths() != null ? request.getMonths() : 12;
            Integer topCount = request.getTopCount() != null ? request.getTopCount() : 50;
            response.setAnalyzedPeriod(months);

            // Generate beneficiary statistics
            List<BeneficiaryStatistics> beneficiaryStats = generateBeneficiaryStatistics(request, topCount);
            response.setBeneficiaryStatistics(beneficiaryStats);

            // Calculate totals
            Integer totalBeneficiaries = beneficiaryStats.size();
            Integer totalTransactions = beneficiaryStats.stream()
                .mapToInt(BeneficiaryStatistics::getTransactionCount)
                .sum();

            response.setTotalBeneficiaries(totalBeneficiaries);
            response.setTotalTransactions(totalTransactions);

            logger.info("Beneficiary analytics generated successfully for " + totalBeneficiaries + " beneficiaries");
            return response;

        } catch (Exception e) {
            logger.error("Error generating beneficiary analytics: " + e.getMessage());
            
            XBeneficiaryAnalyticsResponse errorResponse = new XBeneficiaryAnalyticsResponse();
            errorResponse.setStatusCode("01");
            errorResponse.setStatusDescription("Error retrieving beneficiary analytics: " + e.getMessage());
            errorResponse.setGeneratedAt(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date()));
            
            return errorResponse;
        }
    }

    private List<BeneficiaryStatistics> generateBeneficiaryStatistics(XBeneficiaryAnalyticsRequest request, Integer topCount) {
        List<BeneficiaryStatistics> beneficiaries = new ArrayList<>();

        // Sample beneficiary data
        String[] beneficiaryNames = {
            "ABC Trading Company", "XYZ Import Export Ltd", "Global Logistics Corp", 
            "International Trade Partners", "Middle East Trading", "Gulf Commercial Bank",
            "Emirates Steel Industries", "Dubai Textile Mills", "Saudi Petrochemicals",
            "Kuwait Investment Group", "Oman Manufacturing Co", "Qatar Steel Works",
            "Bahrain Trading House", "Jordan Export Co", "Lebanon Import Ltd",
            "Syria Commercial Corp", "Iraq Oil Services", "Yemen Trading Co",
            "African Trade Partners", "European Importers Ltd"
        };

        String[] countries = {"UAE", "Saudi Arabia", "Kuwait", "Qatar", "Bahrain", "Oman", 
                            "Jordan", "Lebanon", "Egypt", "Turkey", "India", "Pakistan", "Bangladesh"};
        
        String[] currencies = {"SAR", "USD", "EUR", "AED", "KWD", "QAR", "BHD", "OMR"};
        
        String[] modules = {"IMLC", "EXLC", "OWGT", "IMCO", "EXCO", "TFIN"};

        Random random = new Random(42); // Fixed seed for consistent results

        // Generate top beneficiaries with higher transaction counts
        for (int i = 0; i < Math.min(topCount, beneficiaryNames.length); i++) {
            BeneficiaryStatistics beneficiary = new BeneficiaryStatistics();
            
            beneficiary.setBeneficiaryName(beneficiaryNames[i]);
            beneficiary.setBeneficiaryCode("BEN" + String.format("%03d", i + 1));
            beneficiary.setCountry(countries[random.nextInt(countries.length)]);
            beneficiary.setPrimaryCurrency(currencies[random.nextInt(currencies.length)]);
            beneficiary.setPrimaryModule(modules[random.nextInt(modules.length)]);
            
            // Generate transaction count (higher for top beneficiaries)
            int baseCount = Math.max(1, 100 - (i * 3)); // Decreasing count for ranking
            int variance = random.nextInt(20) - 10; // ±10 variance
            beneficiary.setTransactionCount(Math.max(1, baseCount + variance));
            
            // Generate total amount based on transaction count
            double baseAmount = beneficiary.getTransactionCount() * (50000 + random.nextInt(200000));
            beneficiary.setTotalAmount(Math.round(baseAmount * 100.0) / 100.0);
            
            // Calculate average amount
            beneficiary.setAverageAmount(Math.round((beneficiary.getTotalAmount() / beneficiary.getTransactionCount()) * 100.0) / 100.0);
            
            // Set last transaction date (within the analyzed period)
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -random.nextInt(request.getMonths() * 30));
            beneficiary.setLastTransactionDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
            
            // Calculate percentage of total (will be updated after all beneficiaries are generated)
            beneficiary.setPercentageOfTotal(0.0); // Placeholder
            
            beneficiaries.add(beneficiary);
        }

        // Sort by transaction count (descending) as requested
        if ("TRANSACTION_COUNT".equals(request.getSortBy())) {
            beneficiaries.sort((a, b) -> Integer.compare(b.getTransactionCount(), a.getTransactionCount()));
        } else if ("TOTAL_AMOUNT".equals(request.getSortBy())) {
            beneficiaries.sort((a, b) -> Double.compare(b.getTotalAmount(), a.getTotalAmount()));
        } else if ("BENEFICIARY_NAME".equals(request.getSortBy())) {
            beneficiaries.sort((a, b) -> a.getBeneficiaryName().compareTo(b.getBeneficiaryName()));
        }

        // Calculate percentage of total transactions
        int totalTransactions = beneficiaries.stream().mapToInt(BeneficiaryStatistics::getTransactionCount).sum();
        for (BeneficiaryStatistics beneficiary : beneficiaries) {
            double percentage = (beneficiary.getTransactionCount() * 100.0) / totalTransactions;
            beneficiary.setPercentageOfTotal(Math.round(percentage * 100.0) / 100.0);
        }

        // Filter by minimum transaction count if specified
        Integer minCount = request.getMinTransactionCount() != null ? request.getMinTransactionCount() : 1;
        return beneficiaries.stream()
            .filter(b -> b.getTransactionCount() >= minCount)
            .collect(ArrayList::new, (list, item) -> list.add(item), (list1, list2) -> list1.addAll(list2));
    }
}