package com.dtone.lending.service;

import com.dtone.lending.domain.Loans;
import com.dtone.lending.domain.Payment;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Service Interface for managing {@link Payment}.
 */
public interface PaymentService {
    /**
     * Save a payment.
     *
     * @param payment the entity to save.
     * @return the persisted entity.
     */
    Payment save(Payment payment);


    /**
     * Save all payment.
     *
     * @param payment the entity to save.
     * @return the persisted entity.
     */
    List<Payment> saveAll(Set<Payment> payment);

    /**
     * Partially updates a payment.
     *
     * @param payment the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Payment> partialUpdate(Payment payment);

    /**
     * Get all the payments.
     *
     * @return the list of entities.
     */
    List<Payment> findAll();
    /**
     * Get all the Payment where Loans is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Payment> findAllWhereLoansIsNull();

    /**
     * Get the "id" payment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Payment> findOne(Long id);


    /**
     * Get the "Loan id" payments.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    List<Payment> findByLoanId(Long id);

    /**
     * Delete the "id" payment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Payment postSingleLoanClaimsEvents(Payment payment) throws ExecutionException, InterruptedException;

    void postLoanClaimsEvents();
}
