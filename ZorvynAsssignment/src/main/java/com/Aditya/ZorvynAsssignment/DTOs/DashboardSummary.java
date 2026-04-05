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

    private Map<String, BigDecimal> categoryTotals;

    private List<Map<String, Object>> monthlySummary;
}
