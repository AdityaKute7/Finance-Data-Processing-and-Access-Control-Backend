package com.Aditya.ZorvynAsssignment.Services;

import com.Aditya.ZorvynAsssignment.DTOs.DashboardSummary;
import com.Aditya.ZorvynAsssignment.DTOs.FinanceRecordResponse;
import com.Aditya.ZorvynAsssignment.Entity.RecordType;
import com.Aditya.ZorvynAsssignment.Entity.User;
import com.Aditya.ZorvynAsssignment.Exception.ResourceNotFoundException;
import com.Aditya.ZorvynAsssignment.Repository.FinanceRecordRepository;
import com.Aditya.ZorvynAsssignment.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FinanceRecordRepository recordRepository;
    private final UserRepository userRepository;

    public DashboardSummary getFullSummary() {
        User user = getCurrentUser();

        BigDecimal totalIncome = recordRepository.sumIncomeByUser(user);
        BigDecimal totalExpenses = recordRepository.sumExpenseByUser(user);

        List<FinanceRecordResponse> recentActivity = recordRepository
                .findTop10ByCreatedByOrderByCreatedAtDesc(user).stream()
                .map(r -> FinanceRecordResponse.builder()
                        .id(r.getId())
                        .amount(r.getAmount())
                        .type(r.getType())
                        .category(r.getCategory())
                        .date(r.getDate())
                        .description(r.getDescription())
                        .createdByUsername(r.getCreatedBy().getUsername())
                        .createdAt(r.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        // Category-wise totals as a simple map
        Map<String, BigDecimal> categoryTotals = new LinkedHashMap<>();
        for (Object[] row : recordRepository.categoryWiseTotalsByUser(user)) {
            categoryTotals.put((String) row[0], (BigDecimal) row[1]);
        }

        // Monthly summary as a list of maps
        List<Object[]> monthlyRows = recordRepository.monthlySummaryByUser(user);
        Map<String, Map<String, Object>> monthMap = new LinkedHashMap<>();
        for (Object[] row : monthlyRows) {
            String month = (String) row[0];
            RecordType type = (RecordType) row[1];
            BigDecimal amount = (BigDecimal) row[2];

            Map<String, Object> entry = monthMap.computeIfAbsent(month, m -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("month", m);
                map.put("income", BigDecimal.ZERO);
                map.put("expense", BigDecimal.ZERO);
                return map;
            });

            if (type == RecordType.INCOME) {
                entry.put("income", amount);
            } else {
                entry.put("expense", amount);
            }
            BigDecimal inc = (BigDecimal) entry.get("income");
            BigDecimal exp = (BigDecimal) entry.get("expense");
            entry.put("net", inc.subtract(exp));
        }

        return DashboardSummary.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .netBalance(totalIncome.subtract(totalExpenses))
                .recentActivity(recentActivity)
                .categoryTotals(categoryTotals)
                .monthlySummary(new ArrayList<>(monthMap.values()))
                .build();
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }
}
