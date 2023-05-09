package com.insat.software.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AmortizationTableDto {
    private Integer installmentNumber;
    private LocalDateTime paymentDate;
    private Double beginningBalance;
    private Double principalPayment;
    private Double interestPayment;
    private Double endingBalance;
    private Double totalPayment;
}
