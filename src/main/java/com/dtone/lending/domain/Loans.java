package com.dtone.lending.domain;

import com.dtone.lending.domain.enumeration.LoanStatus;
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
 * A Loans.
 */
@Entity
@Table(name = "loans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loans implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LoanStatus status;

    @Column(name = "fee", precision = 21, scale = 2)
    private BigDecimal fee;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @JsonIgnoreProperties(value = { "loans" }, allowSetters = true)
    @OneToMany(mappedBy = "loans")
    @Builder.Default
    private Set<Payment> payment = new HashSet<>();


    @OneToMany(mappedBy = "userProfile")
    @JsonIgnoreProperties(value = { "payment", "loanProducts", "userProfile" }, allowSetters = true)
    @Builder.Default
    private Set<Loans> loans = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "loans", "userCreditProfiles" }, allowSetters = true)
    private UserProfile userProfile;

    @ManyToOne
    @JsonIgnoreProperties(value = { "loans", "userCreditProfiles" }, allowSetters = true)
    private LoanProducts loanProducts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Loans id(Long id) {
        this.id = id;
        return this;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Loans amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LoanStatus getStatus() {
        return this.status;
    }

    public Loans status(LoanStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public BigDecimal getFee() {
        return this.fee;
    }

    public Loans fee(BigDecimal fee) {
        this.fee = fee;
        return this;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public Loans createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getUpdatedDate() {
        return this.updatedDate;
    }

    public Loans updatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Set<Payment> getPayment() {
        return this.payment;
    }

    public Loans payment(Set<Payment> payment) {
        this.setPayment(payment);
        return this;
    }

    public Loans addPayment(Payment payment) {
        this.payment.add(payment);
        payment.setLoans(this);
        return this;
    }

    public Loans removePayment(Payment payment) {
        this.payment.remove(payment);
        payment.setLoans(null);
        return this;
    }

    public void setPayment(Set<Payment> payment) {
        if (this.payment != null) {
            this.payment.forEach(i -> i.setLoans(null));
        }
        if (payment != null) {
            payment.forEach(i -> i.setLoans(this));
        }
        this.payment = payment;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public Loans userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public LoanProducts getLoanProducts() {
        return this.loanProducts;
    }

    public Loans loanProducts(LoanProducts loanProducts) {
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
        if (!(o instanceof Loans)) {
            return false;
        }
        return id != null && id.equals(((Loans) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Loans{" +
                "id=" + getId() +
                ", amount=" + getAmount() +
                ", status='" + getStatus() + "'" +
                ", fee=" + getFee() +
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
