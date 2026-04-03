package com.Aditya.ZorvynAsssignment.DTOs;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummary {

    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance;
    private List<FinanceRecordResponse> recentActivity;

    // Category name → total amount
    private Map<String, BigDecimal> categoryTotals;

    // Monthly summaries embedded as simple maps
    private List<Map<String, Object>> monthlySummary;
}
