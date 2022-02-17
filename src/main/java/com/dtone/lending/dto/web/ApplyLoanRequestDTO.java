package com.dtone.lending.dto.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyLoanRequestDTO {
    private BigDecimal amount;
    private Long productId;
    private Long catId;
}
