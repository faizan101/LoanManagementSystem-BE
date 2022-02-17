package com.dtone.lending.repository;

import com.dtone.lending.domain.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
/**
 * Spring Data SQL repository for the Loans entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long>, JpaSpecificationExecutor<SubCategory> {

}
