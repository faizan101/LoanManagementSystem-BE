package com.dtone.lending.controller.web;

import com.dtone.lending.constants.PaymentStatus;
import com.dtone.lending.domain.Loans;
import com.dtone.lending.domain.Payment;
import com.dtone.lending.domain.enumeration.LoanStatus;
import com.dtone.lending.repository.PaymentRepository;
import com.dtone.lending.service.LoansService;
import com.dtone.lending.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/api")
public class PaymentResource {

    private final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    private final PaymentService paymentService;

    private final PaymentRepository paymentRepository;

    private final LoansService loansService;

    public PaymentResource(PaymentService paymentService, PaymentRepository paymentRepository, LoansService loansService) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
        this.loansService = loansService;
    }


    @PostMapping("/performPayment/{id}")
    public ResponseEntity<Payment> createPayment(@PathVariable(value = "id", required = false) final Long id) throws URISyntaxException, ExecutionException, InterruptedException {
        log.debug("REST request to save Payment : {}", id);
        Optional<Payment> payment = paymentService.findOne(id);

        if( payment.isPresent() &&
                payment.get().getTransactionStatus().equalsIgnoreCase(PaymentStatus.PENDING.toString()) &&
                payment.get().getLoans().getStatus().equals(LoanStatus.CONFIRMED)){

            Payment paymentRsp = paymentService.postSingleLoanClaimsEvents(payment.get());
            return ResponseEntity.ok().body(paymentRsp);

        }
        return ResponseEntity.badRequest().body(new Payment());
    }


    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getAllPayments(@RequestParam(required = false) String filter) {
        log.debug("REST request to get all Payments");
        return ResponseEntity.ok().body(paymentService.findAll());
    }

    @GetMapping("/payments/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        log.debug("REST request to get Payment : {}", id);
        Optional<Payment> payment = paymentService.findOne(id);
        return ResponseEntity.ok().body(payment.get());
    }

    @GetMapping("/payments/loan/{id}")
    public ResponseEntity<List<Payment>> getPaymentByLoan(@PathVariable Long id) {
        log.debug("REST request to get Payment : {}", id);
        List<Payment> payment = paymentService.findByLoanId(id);
        return ResponseEntity.ok().body(payment);
    }
}
