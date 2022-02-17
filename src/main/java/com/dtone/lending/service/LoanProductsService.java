package com.dtone.lending.service;

import com.dtone.lending.domain.LoanProducts;
import com.dtone.lending.domain.SubCategory;
import com.dtone.lending.dto.web.EligibilityResponseDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link LoanProducts}.
 */
public interface LoanProductsService {
    /**
     * Save a loanProducts.
     *
     * @param loanProducts the entity to save.
     * @return the persisted entity.
     */
    LoanProducts save(LoanProducts loanProducts);

    /**
     * Partially updates a loanProducts.
     *
     * @param loanProducts the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LoanProducts> partialUpdate(LoanProducts loanProducts);

    /**
     * Get all the loanProducts.
     *
     * @return the list of entities.
     */
    List<LoanProducts> findAll();

    /**
     * Get all the Eligible loanProducts.
     *
     * @return the list of entities.
     */
    List<EligibilityResponseDTO> findAllEligible(Long userId);

    /**
     * Get the "id" loanProducts.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LoanProducts> findOne(Long id);

    /**
     * Delete the "id" loanProducts.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
