package com.dtone.lending.service.impl;

import com.dtone.lending.domain.LoanProducts;
import com.dtone.lending.domain.UserProfile;
import com.dtone.lending.dto.web.EligibilityResponseDTO;
import com.dtone.lending.repository.LoanProductsRepository;
import com.dtone.lending.repository.UserProfileRepository;
import com.dtone.lending.service.LoanProductsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link LoanProducts}.
 */
@Service
@Transactional
public class LoanProductsServiceImpl implements LoanProductsService {

    private final Logger log = LoggerFactory.getLogger(LoanProductsServiceImpl.class);

    private final LoanProductsRepository loanProductsRepository;
    private final UserProfileRepository userProfileRepository;


    public LoanProductsServiceImpl(LoanProductsRepository loanProductsRepository, UserProfileRepository userProfileRepository) {
        this.loanProductsRepository = loanProductsRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public LoanProducts save(LoanProducts loanProducts) {
        log.debug("Request to save LoanProducts : {}", loanProducts);
        return loanProductsRepository.save(loanProducts);
    }

    @Override
    public Optional<LoanProducts> partialUpdate(LoanProducts loanProducts) {
        log.debug("Request to partially update LoanProducts : {}", loanProducts);

        return loanProductsRepository
            .findById(loanProducts.getId())
            .map(
                existingLoanProducts -> {
                    if (loanProducts.getServiceName() != null) {
                        existingLoanProducts.setServiceName(loanProducts.getServiceName());
                    }
                    if (loanProducts.getServiceDescription() != null) {
                        existingLoanProducts.setServiceDescription(loanProducts.getServiceDescription());
                    }
                    if (loanProducts.getDuration() != null) {
                        existingLoanProducts.setDuration(loanProducts.getDuration());
                    }
                    if (loanProducts.getInterestRate() != null) {
                        existingLoanProducts.setInterestRate(loanProducts.getInterestRate());
                    }
                    if (loanProducts.getGracePeriod() != null) {
                        existingLoanProducts.setGracePeriod(loanProducts.getGracePeriod());
                    }
                    if (loanProducts.getEnabled() != null) {
                        existingLoanProducts.setEnabled(loanProducts.getEnabled());
                    }
                    if (loanProducts.getRequiredDocuments() != null) {
                        existingLoanProducts.setRequiredDocuments(loanProducts.getRequiredDocuments());
                    }
                    if (loanProducts.getCreatedDate() != null) {
                        existingLoanProducts.setCreatedDate(loanProducts.getCreatedDate());
                    }
                    if (loanProducts.getUpdatedDate() != null) {
                        existingLoanProducts.setUpdatedDate(loanProducts.getUpdatedDate());
                    }

                    return existingLoanProducts;
                }
            )
            .map(loanProductsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanProducts> findAll() {
        log.debug("Request to get all LoanProducts");
        return loanProductsRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public List<EligibilityResponseDTO> findAllEligible(Long userId) {
        log.debug("Request to get all LoanProducts");
        UserProfile userProfile = userProfileRepository.getById(userId);
        return loanProductsRepository.findEligiblePlans(userProfile.getCreditRating());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoanProducts> findOne(Long id) {
        log.debug("Request to get LoanProducts : {}", id);
        return loanProductsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LoanProducts : {}", id);
        loanProductsRepository.deleteById(id);
    }
}
