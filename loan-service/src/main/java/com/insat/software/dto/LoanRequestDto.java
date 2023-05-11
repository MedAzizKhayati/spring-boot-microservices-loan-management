package com.insat.software.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequestDto {
    private String firstName;
    private String lastName;
    private String reason;
    private Double amount;
    private Integer duration;
}
