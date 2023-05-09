package com.insat.software.service;

import brave.Tracer;
import com.insat.software.LoanReviewedEvent;
import com.insat.software.model.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final KafkaTemplate<String, LoanReviewedEvent> kafkaTemplate;

    public void sendNotification(LoanReviewedEvent loan) {
        kafkaTemplate.send("credit-service-loans", loan);
    }


}
