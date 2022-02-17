package com.dtone.lending.service.impl;

import com.dtone.lending.config.ConfigProperties;
import com.dtone.lending.constants.PaymentStatus;
import com.dtone.lending.domain.Loans;
import com.dtone.lending.domain.Payment;
import com.dtone.lending.domain.enumeration.LoanStatus;
import com.dtone.lending.repository.LoansRepository;
import com.dtone.lending.repository.PaymentRepository;
import com.dtone.lending.service.AsyncService;
import com.dtone.lending.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/**
 * Service Implementation for managing {@link Payment}.
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final LoansRepository loansRepository;


    private ConfigProperties configProperties;

    private final Pageable pageable;

    private AsyncService asyncService;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            LoansRepository loansRepository,
            ConfigProperties configProperties,
            AsyncService asyncService) {
        this.paymentRepository = paymentRepository;
        this.loansRepository = loansRepository;
        this.configProperties = configProperties;
        this.pageable = PageRequest.of(0, 10,
                Sort.by(Sort.Direction.ASC, "id"));
        this.asyncService = asyncService;
    }

    @Override
    public Payment save(Payment payment) {
        log.debug("Request to save Payment : {}", payment);
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> saveAll(Set<Payment> payment) {
        log.debug("Request to save Payment : {}", payment);
        return paymentRepository.saveAll(payment);
    }

    @Override
    public Optional<Payment> partialUpdate(Payment payment) {
        log.debug("Request to partially update Payment : {}", payment);

        return paymentRepository
            .findById(payment.getId())
            .map(
                existingPayment -> {
                    if (payment.getTransactionId() != null) {
                        existingPayment.setTransactionId(payment.getTransactionId());
                    }
                    if (payment.getTransactionAmount() != null) {
                        existingPayment.setTransactionAmount(payment.getTransactionAmount());
                    }
                    if (payment.getTransactionStatus() != null) {
                        existingPayment.setTransactionStatus(payment.getTransactionStatus());
                    }
                    if (payment.getCreatedDate() != null) {
                        existingPayment.setCreatedDate(payment.getCreatedDate());
                    }

                    return existingPayment;
                }
            )
            .map(paymentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findAll() {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll();
    }

    /**
     *  Get all the payments where Loans is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Payment> findAllWhereLoansIsNull() {
        log.debug("Request to get all payments where Loans is null");
        return StreamSupport
            .stream(paymentRepository.findAll().spliterator(), false)
            .filter(payment -> payment.getLoans() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id);
    }

    @Override
    public List<Payment> findByLoanId(Long id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findByLoansIdOrderByDueDateAsc(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }

    @Override
    public Payment postSingleLoanClaimsEvents(Payment payment) throws ExecutionException, InterruptedException {
        CompletableFuture<Payment> paymentCompletableFuture = asyncService.confirmPayment(payment);
        return paymentCompletableFuture.get();
    }

    @Override
    public void postLoanClaimsEvents() {
        final Collection<Payment> events = getDueLoanClaimEvents();
        log.info("Fetched Event Size :: " + events.size());
        if (!events.isEmpty()) {
            events.stream().map(asyncService::confirmPayment).collect(toList());
        }
    }

    public Collection<Payment> getDueLoanClaimEvents() {
        Slice<Payment> eventsPaged = paymentRepository.findByDueDateAndTransactionStatus(
                LocalDate.now(),
                PaymentStatus.PENDING.toString(),
                pageable);
        return eventsPaged.get().collect(Collectors.toList());
    }

}
