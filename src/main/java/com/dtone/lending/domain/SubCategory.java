package com.dtone.lending.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A Loans.
 */
@Entity
@Table(name = "sub_category")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long catId;

    @Column(name = "minCreditScore")
    private Integer minCreditScore;

    @Column(name = "maxCreditScore")
    private Integer maxCreditScore;

    @Column(name = "maxAmount", precision = 21, scale = 2)
    private BigDecimal maxAmount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "loans", "userCreditProfiles" }, allowSetters = true)
    private LoanProducts loanProducts;

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public Integer getMinCreditScore() {
        return minCreditScore;
    }

    public void setMinCreditScore(Integer minCreditScore) {
        this.minCreditScore = minCreditScore;
    }

    public Integer getMaxCreditScore() {
        return maxCreditScore;
    }

    public void setMaxCreditScore(Integer maxCreditScore) {
        this.maxCreditScore = maxCreditScore;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public LoanProducts getLoanProducts() {
        return this.loanProducts;
    }

    public SubCategory loanProducts(LoanProducts loanProducts) {
        this.setLoanProducts(loanProducts);
        return this;
    }

    public void setLoanProducts(LoanProducts loanProducts) {
        this.loanProducts = loanProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubCategory)) {
            return false;
        }
        return catId != null && catId.equals(((SubCategory) o).catId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "SubCategory{" +
                "catId=" + catId +
                ", minCreditScore=" + minCreditScore +
                ", maxCreditScore=" + maxCreditScore +
                ", maxAmount=" + maxAmount +
                ", loanProducts=" + loanProducts +
                '}';
    }
}
