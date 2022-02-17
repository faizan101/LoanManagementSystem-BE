package com.dtone.lending.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "transaction_amount")
    private BigDecimal transactionAmount;

    @Column(name = "transaction_status")
    private String transactionStatus;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @JsonIgnoreProperties(value = { "payment", "loanProducts", "userProfile" }, allowSetters = true)
    @ManyToOne
    private Loans loans;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Payment id(Long id) {
        this.id = id;
        return this;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public Payment transactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getTransactionAmount() {
        return this.transactionAmount;
    }

    public Payment transactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
        return this;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionStatus() {
        return this.transactionStatus;
    }

    public Payment transactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
        return this;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }


    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public Payment dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }


    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public Payment createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }


    public Loans getLoans() {
        return this.loans;
    }

    public Payment loans(Loans loans) {
        this.setLoans(loans);
        return this;
    }

    public void setLoans(Loans loans) {
        this.loans = loans;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", transactionId='" + getTransactionId() + "'" +
            ", transactionAmount='" + getTransactionAmount() + "'" +
            ", transactionStatus='" + getTransactionStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }

    @PrePersist
    protected void onCreate() {
        final LocalDate now = LocalDate.now();
        createdDate = now;
    }
}
