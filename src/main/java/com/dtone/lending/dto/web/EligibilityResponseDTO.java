package com.dtone.lending.dto.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligibilityResponseDTO {
    private Long productId;

    private String serviceName;

    private String serviceDescription;

    private Integer duration;

    private Integer interestRate;

    private Integer gracePeriod;

    private Long catId;

    private BigDecimal maxAmount;

}
