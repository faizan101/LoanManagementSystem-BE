package com.dtone.lending.controller.web;

import com.dtone.lending.domain.LoanProducts;
import com.dtone.lending.domain.UserProfile;
import com.dtone.lending.dto.web.EligibilityResponseDTO;
import com.dtone.lending.repository.LoanProductsRepository;
import com.dtone.lending.service.LoanProductsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class LoanProductsResource {

    private final Logger log = LoggerFactory.getLogger(LoanProductsResource.class);

    private final LoanProductsService loanProductsService;

    private final LoanProductsRepository loanProductsRepository;

    public LoanProductsResource(LoanProductsService loanProductsService, LoanProductsRepository loanProductsRepository) {
        this.loanProductsService = loanProductsService;
        this.loanProductsRepository = loanProductsRepository;
    }

    @PostMapping("/loan-products")
    public ResponseEntity<LoanProducts> createLoanProducts(@RequestBody LoanProducts loanProducts) throws URISyntaxException {
        log.debug("REST request to save LoanProducts : {}", loanProducts);
        LoanProducts result = loanProductsService.save(loanProducts);
        return ResponseEntity
            .created(new URI("/api/loan-products/" + result.getId()))
            .body(result);
    }

    @PutMapping("/loan-products/{id}")
    public ResponseEntity<LoanProducts> updateLoanProducts(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LoanProducts loanProducts
    ) throws URISyntaxException {
        log.debug("REST request to update LoanProducts : {}, {}", id, loanProducts);

        LoanProducts result = loanProductsService.save(loanProducts);
        return ResponseEntity
            .ok()
            .body(result);
    }


    @PatchMapping(value = "/loan-products/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LoanProducts> partialUpdateLoanProducts(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LoanProducts loanProducts
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoanProducts partially : {}, {}", id, loanProducts);

        Optional<LoanProducts> result = loanProductsService.partialUpdate(loanProducts);

        return new ResponseEntity<LoanProducts>(result.get(), HttpStatus.OK);
    }

    @GetMapping("/loan-products")
    public List<LoanProducts> getAllLoanProducts(@RequestParam(required = false) String filter) {
        log.debug("REST request to get all LoanProducts");
        return loanProductsService.findAll();
    }


    @GetMapping("/loan-eligibility")
    public List<EligibilityResponseDTO> getAllEligibleLoanProducts(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            UserProfile userProfile = (UserProfile) authentication.getPrincipal();
            return loanProductsService.findAllEligible(userProfile.getId());
        }
        log.debug("REST request to get all LoanProducts");
        return new ArrayList<>();
    }

    @GetMapping("/loan-products/{id}")
    public ResponseEntity<LoanProducts> getLoanProducts(@PathVariable Long id) throws JsonProcessingException {
        log.debug("REST request to get LoanProducts : {}", id);
        Optional<LoanProducts> loanProducts = loanProductsService.findOne(id);
        return new ResponseEntity<>(loanProducts.get(), HttpStatus.OK);
    }
}
