package com.Aditya.ZorvynAsssignment.DTOs;

import com.Aditya.ZorvynAsssignment.Entity.RecordType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinanceRecordResponse {

    private Long id;
    private BigDecimal amount;
    private RecordType type;
    private String category;
    private LocalDate date;
    private String description;
    private String createdByUsername;
    private LocalDateTime createdAt;
}
