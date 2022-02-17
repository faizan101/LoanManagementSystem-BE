package com.dtone.lending.repository;

import com.dtone.lending.domain.Loans;
import com.dtone.lending.domain.enumeration.LoanStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data SQL repository for the Loans entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoansRepository extends JpaRepository<Loans, Long>, JpaSpecificationExecutor<Loans> {

    List<Loans> findAllByUserProfileId(Long userId);

    List<Loans> findAllByStatus(LoanStatus status);

    Slice<Loans> findByDueDateAndStatusNotIn(LocalDate dueDate, List<LoanStatus> status, Pageable page);
}
