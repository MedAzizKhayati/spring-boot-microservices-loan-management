package com.insat.software.service;

import brave.Span;
import brave.Tracer;

import com.insat.software.dto.LoanRequestDto;
import com.insat.software.model.Loan;
import com.insat.software.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanService {

    private final LoanRepository loanRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String, Loan> kafkaTemplate;

    public <T> T tracedPost(Mono<T> mono, String serviceName) {
        Span span = tracer.nextSpan().name(serviceName + "-lookup");
        T response = null;
        try (Tracer.SpanInScope isLookup = tracer.withSpanInScope(span.start())) {
            span.tag("call", serviceName.toLowerCase());
            response = mono.block();
        } finally {
            span.flush();
        }
        return response;
    }


    public Loan createLoanRequest(LoanRequestDto loanRequest) {
        Loan loan = Loan.builder()
                .clientFirstName(loanRequest.getFirstName())
                .clientLastName(loanRequest.getLastName())
                .reason(loanRequest.getReason())
                .amount(Double.valueOf(loanRequest.getAmount()))
                .build();

        // Save image to AWS S3 bucket

        // Call OCR service
        String response = tracedPost(
                webClientBuilder.build().post()
                        .uri("http://ocr-service/api/ocr/analyse")
                        .retrieve()
                        .bodyToMono(String.class),
                "ocr-service"
        );
        loan.setData(response);

        // Call Commercial service

        // Call Risk service

        // Save the loan after the review
        loanRepository.save(loan);

        // Send the loan request to a Kafka topic
        kafkaTemplate.send("reviewed-loans", loan);

        return loan;
    }


    public List<Loan> findLoanByClientId(String clientId) {
        return loanRepository.findByClientId(clientId);
    }

}
