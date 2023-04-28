package com.insat.software;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoanReviewedEvent {
    public enum Status {
        NEW, APPROVED, REJECTED
    }
    private Long id;

    // Client related information
    private String clientId;
    private String clientFirstName;
    private String clientLastName;
    private String reason;
    private Double amount; // in TND

    // Documents related information
    private String data;
    private Double commercialScore; // 0 - 10
    private Double riskScore; // 0 - 10

    private Status status = Status.NEW;
}
