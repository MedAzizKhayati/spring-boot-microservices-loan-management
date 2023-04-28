package com.insat.software.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "t_loans")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    public enum Status {
        NEW, APPROVED, REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Client related information
    private String clientId;
    private String clientFirstName;
    private String clientLastName;
    private String reason;
    @Column(nullable = false)
    private Double amount; // in TND

    // Documents related information
    @Lob
    private String data;
    private Double commercialScore; // 0 - 10
    private Double riskScore; // 0 - 10

    @Enumerated(EnumType.STRING)
    private Status status;

}
