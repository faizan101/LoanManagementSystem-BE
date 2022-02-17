package com.dtone.lending.repository;

import com.dtone.lending.domain.LoanProducts;
import com.dtone.lending.dto.web.EligibilityResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the LoanProducts entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoanProductsRepository extends JpaRepository<LoanProducts, Long> {

    @Query(name = "findEligiblePlans", nativeQuery = true)
    List<EligibilityResponseDTO> findEligiblePlans(@Param("creditScore") Integer creditScore);
}
