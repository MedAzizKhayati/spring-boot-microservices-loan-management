package com.insat.software.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_agreements")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanAgreement {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long loanId;
            private String clientId;
            private String clientFirstName;
            private String clientLastName;
            private double amount;
            private double interestRate;
            private double totalInterest;
            private int duration;
            private double monthlyPayment;
            private double totalPayment;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;
    }


