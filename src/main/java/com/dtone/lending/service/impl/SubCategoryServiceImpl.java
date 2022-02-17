package com.dtone.lending.service.impl;

import com.dtone.lending.domain.LoanProducts;
import com.dtone.lending.domain.SubCategory;
import com.dtone.lending.domain.UserProfile;
import com.dtone.lending.dto.web.EligibilityResponseDTO;
import com.dtone.lending.repository.LoanProductsRepository;
import com.dtone.lending.repository.SubCategoryRepository;
import com.dtone.lending.repository.UserProfileRepository;
import com.dtone.lending.service.LoanProductsService;
import com.dtone.lending.service.SubCategoryService;
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
public class SubCategoryServiceImpl implements SubCategoryService {

    private final Logger log = LoggerFactory.getLogger(SubCategoryServiceImpl.class);

    private final SubCategoryRepository subCategoryRepository;


    public SubCategoryServiceImpl(SubCategoryRepository subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubCategory> findOne(Long id) {
        log.debug("Request to get SubCategory : {}", id);
        return subCategoryRepository.findById(id);
    }

}
