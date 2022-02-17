package com.dtone.lending.service;

import com.dtone.lending.domain.Loans;
import com.dtone.lending.domain.enumeration.LoanStatus;
import com.dtone.lending.dto.web.loan.LoansResponseDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Loans}.
 */
public interface LoansService {
    /**
     * Save a loans.
     *
     * @param loans the entity to save.
     * @return the persisted entity.
     */
    Loans save(Loans loans);

    /**
     * Partially updates a loans.
     *
     * @param loans the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Loans> partialUpdate(Loans loans);

    /**
     * Get all the loans.
     *
     * @return the list of entities.
     */
    List<Loans> findAll();

    /**
     * Get all the loans.
     *
     * @return the list of entities.
     */
    List<Loans> findAllByUserID(Long userId);

    /**
     * Get all the loans by status.
     *
     * @return the list of entities.
     */
    List<Loans> findAllByStatus(LoanStatus status);

    /**
     * Get the "id" loans.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Loans> findOne(Long id);

    /**
     * Delete the "id" loans.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Entity to DTO Mapper Method "id" loans.
     *
     * @param id the id of the entity.
     */
    List<LoansResponseDTO> generateResponse(List<Loans> loans);

    LoansResponseDTO generateResponse(Loans loans);
}
