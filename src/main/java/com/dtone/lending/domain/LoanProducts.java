package com.dtone.lending.domain;

import com.dtone.lending.dto.web.EligibilityResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A LoanProducts.
 */
@Entity
@Table(name = "loan_products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedNativeQuery(
        name = "findEligiblePlans",
        query = "Select " +
                "lp.id as productId, " +
                "lp.service_name as serviceName, " +
                "lp.service_description as serviceDescription, " +
                "lp.duration as duration, " +
                "lp.interest_rate as interestRate, " +
                "lp.grace_period as gracePeriod, " +
                "sc.cat_id as catId, " +
                "sc.max_amount as maxAmount " +
                "from lending.loan_products lp left join lending.sub_category sc " +
                "on lp.id = sc.loan_products_id where :creditScore " +
                "between sc.min_credit_score and sc.max_credit_score;",
        resultSetMapping = "EligibilityResponseDTO"
)
@SqlResultSetMapping(
        name = "EligibilityResponseDTO",
        classes = @ConstructorResult(
                targetClass = EligibilityResponseDTO.class,
                columns = {
                        @ColumnResult(name = "productId", type = Long.class),
                        @ColumnResult(name = "serviceName", type = String.class),
                        @ColumnResult(name = "serviceDescription", type = String.class),
                        @ColumnResult(name = "duration", type = Integer.class),
                        @ColumnResult(name = "interestRate", type = Integer.class),
                        @ColumnResult(name = "gracePeriod", type = Integer.class),
                        @ColumnResult(name = "catId", type = Long.class),
                        @ColumnResult(name = "maxAmount", type = BigDecimal.class)
                }
        )
)
public class LoanProducts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "service_description")
    private String serviceDescription;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "interest_rate")
    private Integer interestRate;

    @Column(name = "grace_period")
    private Integer gracePeriod;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "required_documents")
    private Boolean requiredDocuments;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @OneToMany(mappedBy = "loanProducts")
    @JsonIgnoreProperties(value = { "payment", "userProfile", "loanProducts" }, allowSetters = true)
    @Builder.Default
    private Set<Loans> loans = new HashSet<>();

    @OneToMany(mappedBy = "loanProducts")
    @JsonIgnoreProperties(value = { "loanProducts" }, allowSetters = true)
    @Builder.Default
    private Set<SubCategory> subCategories = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoanProducts id(Long id) {
        this.id = id;
        return this;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public LoanProducts serviceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return this.serviceDescription;
    }

    public LoanProducts serviceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
        return this;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public LoanProducts duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getInterestRate() {
        return this.interestRate;
    }

    public LoanProducts interestRate(Integer interestRate) {
        this.interestRate = interestRate;
        return this;
    }

    public void setInterestRate(Integer interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getGracePeriod() {
        return this.gracePeriod;
    }

    public LoanProducts gracePeriod(Integer gracePeriod) {
        this.gracePeriod = gracePeriod;
        return this;
    }

    public void setGracePeriod(Integer gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public LoanProducts enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getRequiredDocuments() {
        return this.requiredDocuments;
    }

    public LoanProducts requiredDocuments(Boolean requiredDocuments) {
        this.requiredDocuments = requiredDocuments;
        return this;
    }

    public void setRequiredDocuments(Boolean requiredDocuments) {
        this.requiredDocuments = requiredDocuments;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public LoanProducts createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getUpdatedDate() {
        return this.updatedDate;
    }

    public LoanProducts updatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Set<Loans> getLoans() {
        return this.loans;
    }

    public LoanProducts loans(Set<Loans> loans) {
        this.setLoans(loans);
        return this;
    }

    public LoanProducts addLoans(Loans loans) {
        this.loans.add(loans);
        loans.setLoanProducts(this);
        return this;
    }

    public LoanProducts removeLoans(Loans loans) {
        this.loans.remove(loans);
        loans.setLoanProducts(null);
        return this;
    }

    public void setLoans(Set<Loans> loans) {
        if (this.loans != null) {
            this.loans.forEach(i -> i.setLoanProducts(null));
        }
        if (loans != null) {
            loans.forEach(i -> i.setLoanProducts(this));
        }
        this.loans = loans;
    }

    //

    public Set<SubCategory> getSubCategory() {
        return this.subCategories;
    }

    public LoanProducts subCategories(Set<SubCategory> subCategories) {
        this.setSubCategory(subCategories);
        return this;
    }

    public LoanProducts addSubCategory(SubCategory subCategory) {
        this.subCategories.add(subCategory);
        subCategory.setLoanProducts(this);
        return this;
    }

    public LoanProducts removeSubCategory(SubCategory subCategory) {
        this.subCategories.remove(subCategory);
        subCategory.setLoanProducts(null);
        return this;
    }

    public void setSubCategory(Set<SubCategory> subCategories) {
        if (this.subCategories != null) {
            this.subCategories.forEach(i -> i.setLoanProducts(null));
        }
        if (loans != null) {
            subCategories.forEach(i -> i.setLoanProducts(this));
        }
        this.subCategories = subCategories;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanProducts)) {
            return false;
        }
        return id != null && id.equals(((LoanProducts) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanProducts{" +
                "id=" + getId() +
                ", serviceName='" + getServiceName() + "'" +
                ", serviceDescription='" + getServiceDescription() + "'" +
                ", duration=" + getDuration() +
                ", interestRate=" + getInterestRate() +
                ", gracePeriod=" + getGracePeriod() +
                ", enabled='" + getEnabled() + "'" +
                ", requiredDocuments='" + getRequiredDocuments() + "'" +
                ", createdDate='" + getCreatedDate() + "'" +
                ", updatedDate='" + getUpdatedDate() + "'" +
                "}";
    }

    @PrePersist
    protected void onCreate() {
        final LocalDate now = LocalDate.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDate.now();
    }
}
