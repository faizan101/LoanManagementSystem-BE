package com.dtone.lending.controller.web;

import com.dtone.lending.constants.PaymentStatus;
import com.dtone.lending.domain.*;
import com.dtone.lending.domain.enumeration.LoanStatus;
import com.dtone.lending.dto.web.ApplyLoanRequestDTO;
import com.dtone.lending.dto.web.loan.LoansResponseDTO;
import com.dtone.lending.repository.LoansRepository;
import com.dtone.lending.service.LoanProductsService;
import com.dtone.lending.service.LoansService;
import com.dtone.lending.service.PaymentService;
import com.dtone.lending.service.SubCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class LoansResource {

    private final Logger log = LoggerFactory.getLogger(LoansResource.class);


    private final LoansService loansService;
    private final LoansRepository loansRepository;

    private final LoanProductsService loanProductsService;

    private final PaymentService paymentService;

    public LoansResource(
            LoansService loansService,
            LoansRepository loansRepository,
            LoanProductsService loanProductsService,
            PaymentService paymentService
    ) {
        this.loansService = loansService;
        this.loansRepository = loansRepository;
        this.loanProductsService = loanProductsService;
        this.paymentService = paymentService;
    }

    @PostMapping("/loans")
    public ResponseEntity<LoansResponseDTO> createLoans(@RequestBody ApplyLoanRequestDTO requestDTO, Authentication authentication) throws URISyntaxException {
        log.debug("REST request to save Loans : {}", requestDTO);

        UserProfile userProfile = (UserProfile) authentication.getPrincipal();

        LoanProducts loanProducts = loanProductsService.findOne(requestDTO.getProductId()).get();
//        Optional<SubCategory> subCategory = subCategoryService.findOne(requestDTO.getCatId());

        BigDecimal perTransaction = requestDTO.getAmount().divide(new BigDecimal(loanProducts.getDuration()), RoundingMode.HALF_UP);
        Loans loans = new Loans();
        loans.setUserProfile(userProfile);
        loans.setLoanProducts(loanProducts);
        loans.setAmount(requestDTO.getAmount());
        loans.setStatus(LoanStatus.INITIATED);
        loans.setDueDate(LocalDate.now().plusMonths(loanProducts.getDuration()));
        Loans result = loansService.save(loans);
        Set<Payment> paymentList = IntStream.range(0, loanProducts.getDuration())
                .mapToObj(i -> {
                    Payment payment = new Payment();
                    payment.setTransactionAmount(perTransaction);
                    payment.setTransactionStatus(PaymentStatus.PENDING.toString());
                    payment.setDueDate(LocalDate.now().plusMonths(i));
                    payment.setLoans(loans);
                    return payment;
                })
                .collect(toSet());

        paymentService.saveAll(paymentList);
        return ResponseEntity
            .created(new URI("/api/loans/" + result.getId()))
            .body(loansService.generateResponse(result));
    }

    @PutMapping("/loans/{id}")
    public ResponseEntity<Loans> updateLoans(@PathVariable(value = "id", required = false) final Long id, @RequestBody Loans loans)
        throws URISyntaxException {
        log.debug("REST request to update Loans : {}, {}", id, loans);
        if (loans.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!Objects.equals(id, loans.getId())) {
            return ResponseEntity.badRequest().build();
        }

        if (!loansRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Loans result = loansService.save(loans);
        return ResponseEntity
            .ok()
            .body(result);
    }

    @PatchMapping(value = "/loans/{id}")
    public ResponseEntity<LoansResponseDTO> partialUpdateLoans(@PathVariable(value = "id", required = false) final Long id, @RequestBody Loans loans)
        throws URISyntaxException {
        log.debug("REST request to partial update Loans partially : {}, {}", id, loans);
        if (loans.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!Objects.equals(id, loans.getId())) {
            return ResponseEntity.badRequest().build();
        }

        if (!loansRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Loans> result = loansService.partialUpdate(loans);
        if(result.isPresent())
            return ResponseEntity.ok().body(loansService.generateResponse(result.get()));
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/loans")
    public ResponseEntity<List<LoansResponseDTO>> getAllLoans(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            UserProfile userProfile = (UserProfile) authentication.getPrincipal();
            return ResponseEntity.ok().body(loansService.generateResponse(
                    loansService.findAllByUserID(userProfile.getId()))
            );
        }
        return ResponseEntity.ok().body(loansService.generateResponse(
                loansService.findAll())
        );
    }


    @GetMapping("/loans/status/{status}")
    public ResponseEntity<List<Loans>> getAllLoansByUserID(@PathVariable(value = "status", required = true) final LoanStatus status) {
        return ResponseEntity.ok().body(loansService.findAllByStatus(status));
    }

    @GetMapping("/loans/{id}")
    public ResponseEntity<LoansResponseDTO> getLoans(@PathVariable Long id) {
        log.debug("REST request to get Loans : {}", id);
        Optional<Loans> loans = loansService.findOne(id);
        if(loans.isPresent())
            return ResponseEntity.ok().body(loansService.generateResponse(loans.get()));
        return ResponseEntity.notFound().build();
    }

}
