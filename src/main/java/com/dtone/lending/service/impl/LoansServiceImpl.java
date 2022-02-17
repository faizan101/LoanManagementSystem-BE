package com.dtone.lending.service.impl;

import com.dtone.lending.domain.Loans;
import com.dtone.lending.domain.enumeration.LoanStatus;
import com.dtone.lending.dto.web.loan.LoansResponseDTO;
import com.dtone.lending.dto.web.loan.LoansResponseDTO.PaymentDTO;

import com.dtone.lending.repository.LoansRepository;
import com.dtone.lending.service.LoansService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Loans}.
 */
@Service
@Transactional
public class LoansServiceImpl implements LoansService {

    private final Logger log = LoggerFactory.getLogger(LoansServiceImpl.class);

    private final LoansRepository loansRepository;

    public LoansServiceImpl(LoansRepository loansRepository) {
        this.loansRepository = loansRepository;
    }

    @Override
    public Loans save(Loans loans) {
        log.debug("Request to save Loans : {}", loans);
        return loansRepository.save(loans);
    }

    @Override
    public Optional<Loans> partialUpdate(Loans loans) {
        log.debug("Request to partially update Loans : {}", loans);
        return loansRepository
            .findById(loans.getId())
            .map(
                existingLoans -> {
                    if (loans.getAmount() != null) {
                        existingLoans.setAmount(loans.getAmount());
                    }
                    if (loans.getStatus() != null) {
                        existingLoans.setStatus(loans.getStatus());
                    }
                    if (loans.getFee() != null) {
                        existingLoans.setFee(loans.getFee());
                    }
                    if (loans.getCreatedDate() != null) {
                        existingLoans.setCreatedDate(loans.getCreatedDate());
                    }
                    if (loans.getUpdatedDate() != null) {
                        existingLoans.setUpdatedDate(loans.getUpdatedDate());
                    }

                    return existingLoans;
                }
            )
            .map(loansRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loans> findAll() {
        log.debug("Request to get all Loans");
        return loansRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loans> findAllByUserID(Long userId) {
        log.debug("Request to get all Loans by User Id");
        return loansRepository.findAllByUserProfileId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loans> findAllByStatus(LoanStatus status) {
        log.debug("Request to get all Loans by Status");
        return loansRepository.findAllByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Loans> findOne(Long id) {
        log.debug("Request to get Loans : {}", id);
        return loansRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Loans : {}", id);
        loansRepository.deleteById(id);
    }

    @Override
    public List<LoansResponseDTO> generateResponse(List<Loans> loans){

        return loans.stream().map(loan -> new LoansResponseDTO(
                loan.getId(),
                loan.getStatus(),
                loan.getFee(),
                loan.getAmount(),
                loan.getDueDate(),
                loan.getCreatedDate(),
                loan.getUpdatedDate(),
                loan.getLoanProducts() != null ? new LoansResponseDTO.LoanProductDTO(
                        loan.getLoanProducts().getServiceName(),
                        loan.getLoanProducts().getServiceDescription(),
                        loan.getLoanProducts().getDuration(),
                        loan.getLoanProducts().getInterestRate(),
                        loan.getLoanProducts().getGracePeriod()
                ) : null,
                loan.getUserProfile() != null ? new LoansResponseDTO.UserProfileDTO(
                        loan.getUserProfile().getFirstName(),
                        loan.getUserProfile().getLastName(),
                        loan.getUserProfile().getEmail(),
                        loan.getUserProfile().getPhone(),
                        loan.getUserProfile().getTaxID(),
                        loan.getUserProfile().getCreditRating()
                ) : null)).collect(Collectors.toList());
    }

    @Override
    public LoansResponseDTO generateResponse(Loans loan){
        return new LoansResponseDTO(
                loan.getId(),
                loan.getStatus(),
                loan.getFee(),
                loan.getAmount(),
                loan.getDueDate(),
                loan.getCreatedDate(),
                loan.getUpdatedDate(),
                loan.getLoanProducts() != null ? new LoansResponseDTO.LoanProductDTO(
                        loan.getLoanProducts().getServiceName(),
                        loan.getLoanProducts().getServiceDescription(),
                        loan.getLoanProducts().getDuration(),
                        loan.getLoanProducts().getInterestRate(),
                        loan.getLoanProducts().getGracePeriod()
                ) : null,
                loan.getUserProfile() != null ? new LoansResponseDTO.UserProfileDTO(
                        loan.getUserProfile().getFirstName(),
                        loan.getUserProfile().getLastName(),
                        loan.getUserProfile().getEmail(),
                        loan.getUserProfile().getPhone(),
                        loan.getUserProfile().getTaxID(),
                        loan.getUserProfile().getCreditRating()
                ) : null);
    }
}
