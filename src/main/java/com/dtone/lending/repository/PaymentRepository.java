package com.dtone.lending.repository;

import com.dtone.lending.domain.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data SQL repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Slice<Payment> findByDueDateAndTransactionStatus (LocalDate dueDate, String status, Pageable pageable);

    List<Payment> findByLoansIdOrderByDueDateAsc (Long id);
}
