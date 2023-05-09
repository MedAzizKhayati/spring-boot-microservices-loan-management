package com.insat.software;

import com.insat.software.model.Loan;
import com.insat.software.service.NotificationService;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {
    @Autowired
    private NotificationService notificationService;

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "reviewed-loans")
    public void handleNotification(LoanReviewedEvent loanReviewedEvent) {
        notificationService.sendNotification(loanReviewedEvent);
        log.info(
                "Received Notification for Loan Review #{} with a status of {}",
                loanReviewedEvent.getId(),
                loanReviewedEvent.getStatus()
        );
    }
}
