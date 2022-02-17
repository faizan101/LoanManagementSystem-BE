package com.dtone.lending.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * A UserProfile.
 */
@Entity
@Table(name = "user_profile")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "tax_id")
    private String taxID;

    @Column(name = "credit_rating")
    private Integer creditRating;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @OneToMany(mappedBy = "userProfile")
    @JsonIgnoreProperties(value = { "payment", "loanProducts", "userProfile" }, allowSetters = true)
    @Builder.Default
    private Set<Loans> loans = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserProfile id(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public UserProfile firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public UserProfile lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public UserProfile email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public UserProfile phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTaxID() {
        return this.taxID;
    }

    public UserProfile taxID(String taxID) {
        this.taxID = taxID;
        return this;
    }

    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    public Integer getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(Integer creditRating) {
        this.creditRating = creditRating;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public UserProfile createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getUpdatedDate() {
        return this.updatedDate;
    }

    public UserProfile updatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Set<Loans> getLoans() {
        return this.loans;
    }

    public UserProfile loans(Set<Loans> loans) {
        this.setLoans(loans);
        return this;
    }

    public UserProfile addLoans(Loans loans) {
        this.loans.add(loans);
        loans.setUserProfile(this);
        return this;
    }

    public UserProfile removeLoans(Loans loans) {
        this.loans.remove(loans);
        loans.setUserProfile(null);
        return this;
    }

    public void setLoans(Set<Loans> loans) {
        if (this.loans != null) {
            this.loans.forEach(i -> i.setUserProfile(null));
        }
        if (loans != null) {
            loans.forEach(i -> i.setUserProfile(this));
        }
        this.loans = loans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfile)) {
            return false;
        }
        return id != null && id.equals(((UserProfile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", taxID='" + taxID + '\'' +
                ", creditRating=" + creditRating +
                '}';
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
