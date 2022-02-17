package com.dtone.lending.service.impl;

import com.dtone.lending.domain.UserProfile;
import com.dtone.lending.repository.UserProfileRepository;
import com.dtone.lending.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link UserProfile}.
 */
@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final Logger log = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    private final UserProfileRepository userProfileRepository;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        log.debug("Request to save UserProfile : {}", userProfile);
        return userProfileRepository.save(userProfile);
    }

    @Override
    public Optional<UserProfile> partialUpdate(UserProfile userProfile) {
        log.debug("Request to partially update UserProfile : {}", userProfile);

        return userProfileRepository
            .findById(userProfile.getId())
            .map(
                existingUserProfile -> {
                    if (userProfile.getFirstName() != null) {
                        existingUserProfile.setFirstName(userProfile.getFirstName());
                    }
                    if (userProfile.getLastName() != null) {
                        existingUserProfile.setLastName(userProfile.getLastName());
                    }
                    if (userProfile.getEmail() != null) {
                        existingUserProfile.setEmail(userProfile.getEmail());
                    }
                    if (userProfile.getPhone() != null) {
                        existingUserProfile.setPhone(userProfile.getPhone());
                    }
                    if (userProfile.getTaxID() != null) {
                        existingUserProfile.setTaxID(userProfile.getTaxID());
                    }
                    if (userProfile.getCreditRating() != null) {
                        existingUserProfile.setCreditRating(userProfile.getCreditRating());
                    }
                    if (userProfile.getCreatedDate() != null) {
                        existingUserProfile.setCreatedDate(userProfile.getCreatedDate());
                    }
                    if (userProfile.getUpdatedDate() != null) {
                        existingUserProfile.setUpdatedDate(userProfile.getUpdatedDate());
                    }

                    return existingUserProfile;
                }
            )
            .map(userProfileRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfile> findAll() {
        log.debug("Request to get all UserProfiles");
        return userProfileRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfile> findOne(Long id) {
        log.debug("Request to get UserProfile : {}", id);
        return userProfileRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserProfile : {}", id);
        userProfileRepository.deleteById(id);
    }
}
