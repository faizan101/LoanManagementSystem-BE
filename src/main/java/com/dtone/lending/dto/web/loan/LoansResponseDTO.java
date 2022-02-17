package com.dtone.lending.dto.web.loan;

import com.dtone.lending.domain.enumeration.LoanStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoansResponseDTO {

    private Long id;
    private LoanStatus status;
    private BigDecimal fee;
    private BigDecimal amount;
    private LocalDate dueDate;
    private LocalDate createdDate;
    private LocalDate updatedDate;
//    private PaymentDTO paymentDTO;
    private LoanProductDTO loanProductDTO;
    private UserProfileDTO userProfileDTO;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentDTO{
        private String transactionId;
        private BigDecimal transactionAmount;
        private String transactionStatus;
        private LocalDate createdDate;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoanProductDTO{
        private String serviceName;
        private String serviceDescription;
        private Integer duration;
        private Integer interestRate;
        private Integer gracePeriod;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserProfileDTO{
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String taxID;
        private Integer creditRating;

    }

}
