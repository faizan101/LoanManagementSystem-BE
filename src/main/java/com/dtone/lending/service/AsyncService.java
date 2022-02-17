package com.dtone.lending.service;

import com.dtone.lending.domain.Loans;
import com.dtone.lending.domain.Payment;;

import java.util.concurrent.CompletableFuture;

public interface AsyncService {

    CompletableFuture<Payment> confirmPayment(Payment payment);

}
