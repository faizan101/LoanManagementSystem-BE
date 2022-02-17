package com.dtone.lending.service;

import com.dtone.lending.domain.LoanProducts;
import com.dtone.lending.domain.SubCategory;
import com.dtone.lending.dto.web.EligibilityResponseDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.dtone.lending.domain.SubCategory}.
 */
public interface SubCategoryService {
    /**
     * Get the "id" SubCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubCategory> findOne(Long id);

}
