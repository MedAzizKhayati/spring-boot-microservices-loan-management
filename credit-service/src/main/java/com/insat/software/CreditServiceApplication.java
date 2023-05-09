package com.insat.software;
import com.insat.software.controller.LoanAgreementController;
import com.insat.software.model.Loan;
import com.insat.software.service.LoanAgreementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class CreditServiceApplication {
    @Autowired
    private LoanAgreementController loanAgreementController;
    public static void main(String[] args) {
        SpringApplication.run(CreditServiceApplication.class, args);
    }

    @KafkaListener(topics = "credit-service-loans")
    public void handleNotification(Loan loan) {
        loanAgreementController.generateLoanAgreement(loan);
    }

}
