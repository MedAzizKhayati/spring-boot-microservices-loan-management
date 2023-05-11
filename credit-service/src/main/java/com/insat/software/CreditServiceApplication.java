package com.insat.software;

import com.insat.software.model.LoanReviewedEvent;
import com.insat.software.service.LoanAgreementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;


@Slf4j
@SpringBootApplication
public class CreditServiceApplication {
    @Autowired
    private LoanAgreementService loanAgreementService;

    public static void main(String[] args) {
        SpringApplication.run(CreditServiceApplication.class, args);
    }

    @KafkaListener(topics = "reviewed-loans")
    public void handleNotification(LoanReviewedEvent loan) {
        log.info(
                "Received Notification for Loan Review #{} with a status of {}"
        );
        if(loan.getStatus() == LoanReviewedEvent.Status.REJECTED) {
            log.info("Loan #{} was rejected", loan.getId());
            return;
        }
        loanAgreementService.generateLoanAgreement(loan);
    }

}
