package com.insat.software;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@SpringBootApplication
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "reviewed-loans")
    public void handleNotification(LoanReviewedEvent loanReviewedEvent) {
        log.info(
                "Received Notification for Loan Review #{} with a status of {}",
                loanReviewedEvent.getId(),
                loanReviewedEvent.getStatus()
        );
    }
}
