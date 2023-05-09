package com.insat.software.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanWithScoreDto {
    private String firstName;
    private String lastName;
    private String reason;
    private String amount;
    private String duration;
    private Double commercialScore;
    }

