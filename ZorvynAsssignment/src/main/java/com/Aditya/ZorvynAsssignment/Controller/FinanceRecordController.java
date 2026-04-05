package com.Aditya.ZorvynAsssignment.Controller;

import com.Aditya.ZorvynAsssignment.DTOs.FinanceRecordRequest;
import com.Aditya.ZorvynAsssignment.DTOs.FinanceRecordResponse;
import com.Aditya.ZorvynAsssignment.Entity.RecordType;
import com.Aditya.ZorvynAsssignment.Services.FinanceRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class FinanceRecordController {

    private final FinanceRecordService recordService;

    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public ResponseEntity<List<FinanceRecordResponse>> getRecords() {
        return ResponseEntity.ok(recordService.getRecords());
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FinanceRecordResponse> createRecord(
            @Valid @RequestBody FinanceRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recordService.createRecord(request));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FinanceRecordResponse> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody FinanceRecordRequest request) {
        return ResponseEntity.ok(recordService.updateRecord(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/filter/date")
    @PreAuthorize("hasAnyRole('VIEWER','ANALYST','ADMIN')")
    public ResponseEntity<List<FinanceRecordResponse>> filterByDate(
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<FinanceRecordResponse> records = recordService.filterByDate(startDate, endDate);
        return ResponseEntity.ok(records);
    }


    @GetMapping("/filter/category")
    @PreAuthorize("hasAnyRole('VIEWER','ANALYST','ADMIN')")
    public ResponseEntity<List<FinanceRecordResponse>> filterByCategory(
            @RequestParam("category") String category) {

        List<FinanceRecordResponse> records = recordService.filterByCategory(category);
        return ResponseEntity.ok(records);
    }


    @GetMapping("/filter/type")
    @PreAuthorize("hasAnyRole('VIEWER','ANALYST','ADMIN')")
    public ResponseEntity<List<FinanceRecordResponse>> filterByType(
            @RequestParam("type") RecordType type) {

        List<FinanceRecordResponse> records = recordService.filterByType(type);
        return ResponseEntity.ok(records);
    }
}
