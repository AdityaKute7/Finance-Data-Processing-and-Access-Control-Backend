package com.Aditya.ZorvynAsssignment.Services;

import com.Aditya.ZorvynAsssignment.DTOs.FinanceRecordRequest;
import com.Aditya.ZorvynAsssignment.DTOs.FinanceRecordResponse;
import com.Aditya.ZorvynAsssignment.Entity.FinanceRecord;
import com.Aditya.ZorvynAsssignment.Entity.RecordType;
import com.Aditya.ZorvynAsssignment.Entity.User;
import com.Aditya.ZorvynAsssignment.Exception.ResourceNotFoundException;
import com.Aditya.ZorvynAsssignment.Repository.FinanceRecordRepository;
import com.Aditya.ZorvynAsssignment.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinanceRecordService {

    private final FinanceRecordRepository recordRepository;
    private final UserRepository userRepository;

    public FinanceRecordResponse createRecord(FinanceRecordRequest request) {
        User currentUser = getCurrentUser();

        FinanceRecord record = FinanceRecord.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .date(request.getDate())
                .description(request.getDescription())
                .createdBy(currentUser)
                .build();

        record = recordRepository.save(record);
        return toResponse(record);
    }

    public List<FinanceRecordResponse> getRecords() {
        User currentUser = getCurrentUser();
        return recordRepository.findByCreatedBy(currentUser).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public FinanceRecordResponse updateRecord(Long id, FinanceRecordRequest request) {
        User currentUser = getCurrentUser();
        FinanceRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));

        if (!record.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Record not found with id: " + id);
        }

        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDate(request.getDate());
        record.setDescription(request.getDescription());

        record = recordRepository.save(record);
        return toResponse(record);
    }

    public void deleteRecord(Long id) {
        User currentUser = getCurrentUser();
        FinanceRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));

        if (!record.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Record not found with id: " + id);
        }

        recordRepository.delete(record);
    }

    public List<FinanceRecordResponse> filterByDate(LocalDate startDate, LocalDate endDate) {
        User
                currentUser = getCurrentUser();
        return recordRepository.findByCreatedByAndDateBetween(currentUser, startDate, endDate).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<FinanceRecordResponse> filterByCategory(String category) {
        User currentUser = getCurrentUser();
        return recordRepository.findByCreatedByAndCategory(currentUser, category).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<FinanceRecordResponse> filterByType(RecordType type) {
        User currentUser = getCurrentUser();
        return recordRepository.findByCreatedByAndType(currentUser, type).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }

    private FinanceRecordResponse toResponse(FinanceRecord record) {
        return FinanceRecordResponse.builder()
                .id(record.getId())
                .amount(record.getAmount())
                .type(record.getType())
                .category(record.getCategory())
                .date(record.getDate())
                .description(record.getDescription())
                .createdByUsername(record.getCreatedBy().getUsername())
                .createdAt(record.getCreatedAt())
                .build();
    }
}
