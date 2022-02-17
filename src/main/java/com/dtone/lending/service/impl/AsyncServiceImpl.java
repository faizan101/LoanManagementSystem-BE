package com.dtone.lending.service.impl;

import com.dtone.lending.constants.PaymentStatus;
import com.dtone.lending.constants.ReportedStatus;
import com.dtone.lending.domain.Payment;
import com.dtone.lending.repository.LoansRepository;
import com.dtone.lending.repository.PaymentRepository;
import com.dtone.lending.service.AsyncService;
import com.dtone.lending.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AsyncServiceImpl implements AsyncService {

    private LoansRepository loansRepository;
    private PaymentRepository paymentRepository;
    public AsyncServiceImpl(
            LoansRepository loansRepository,
            PaymentRepository paymentRepository
    ) {
        this.loansRepository = loansRepository;
        this.paymentRepository = paymentRepository;
    }

    @Async("AsyncThreadPool")
    @Override
    public CompletableFuture<Payment> confirmPayment(Payment payment) {

        // Assuming the payment system is being called here and based on the status loan status is being marked.
        ResponseUtil.ServiceResponse serviceResponse = ResponseUtil.ServiceResponse.of(ReportedStatus.SUCCESS, "Response Body");

        if (serviceResponse.getStatus().equals(ReportedStatus.SUCCESS)) {
            payment.setTransactionStatus(PaymentStatus.SUCCESS.toString());
            payment.setTransactionId("Returned From Response");
            paymentRepository.save(payment);
        } else {
            log.info("FAILED {[]}", serviceResponse.toString());
            //TODO Maybe do retry.
        }
        return CompletableFuture.completedFuture(payment);
    }
}