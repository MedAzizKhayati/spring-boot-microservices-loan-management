package com.insat.software.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanAgreementDto {
    private Long id;
    private Double amount;
    private Double interestRate;
    private Integer duration;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private double monthlyPayment;
    private double totalPayment;
    private LocalDateTime createdAt;
    private List<AmortizationTableDto> amortizationTable;
}
