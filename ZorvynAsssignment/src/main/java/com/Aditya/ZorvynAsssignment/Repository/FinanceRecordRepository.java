package com.Aditya.ZorvynAsssignment.Repository;

import com.Aditya.ZorvynAsssignment.Entity.FinanceRecord;
import com.Aditya.ZorvynAsssignment.Entity.RecordType;
import com.Aditya.ZorvynAsssignment.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinanceRecordRepository extends JpaRepository<FinanceRecord, Long> {

    List<FinanceRecord> findByCreatedBy(User user);

    List<FinanceRecord> findByCreatedByAndDateBetween(User user, LocalDate start, LocalDate end);

    List<FinanceRecord> findByCreatedByAndCategory(User user, String category);

    List<FinanceRecord> findByCreatedByAndType(User user, RecordType
            type);



    // Took help from Spring Data Jpa documentation
    // and AI-assisted guidance to dessign the queries, and The business logic was designed and adapted by me.
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FinanceRecord f WHERE f.createdBy = :user AND f.type = 'INCOME'")
    BigDecimal sumIncomeByUser(@Param("user") User user);

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FinanceRecord f WHERE f.createdBy = :user AND f.type = 'EXPENSE'")
    BigDecimal sumExpenseByUser(@Param("user") User user);

    @Query("SELECT f.category, SUM(f.amount) FROM FinanceRecord f WHERE f.createdBy = :user GROUP BY f.category")
    List<Object[]> categoryWiseTotalsByUser(@Param("user") User user);

    @Query("SELECT FUNCTION('TO_CHAR', f.date, 'YYYY-MM'), f.type, SUM(f.amount) " +
           "FROM FinanceRecord f WHERE f.createdBy = :user " +
           "GROUP BY FUNCTION('TO_CHAR', f.date, 'YYYY-MM'), f.type " +
           "ORDER BY FUNCTION('TO_CHAR', f.date, 'YYYY-MM') DESC")
    List<Object[]> monthlySummaryByUser(@Param("user") User user);

    List<FinanceRecord> findTop10ByCreatedByOrderByCreatedAtDesc(User user);
}
