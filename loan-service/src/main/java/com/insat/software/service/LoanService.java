package com.insat.software.service;

import brave.Span;
import brave.Tracer;

import com.insat.software.dto.EligibilityResult;
import com.insat.software.dto.LoanRequestDto;
import com.insat.software.dto.LoanWithScoreDto;
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

    public LoanRequestDto convertToLoanRequestDto(Loan loan) {
        LoanRequestDto loanRequestDto = new LoanRequestDto();
        loanRequestDto.setFirstName(loan.getClientFirstName());
        loanRequestDto.setLastName(loan.getClientLastName());
        loanRequestDto.setReason(loan.getReason());
        loanRequestDto.setAmount(loan.getAmount());
        loanRequestDto.setDuration(loan.getDuration());
        return loanRequestDto;
    }


    public Loan createLoanRequest(LoanRequestDto loanRequest) {
        Loan loan = Loan.builder()
                .clientFirstName(loanRequest.getFirstName())
                .clientLastName(loanRequest.getLastName())
                .reason(loanRequest.getReason())
                .amount(loanRequest.getAmount())
                .duration(loanRequest.getDuration())
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
        LoanRequestDto loanRequestDto = convertToLoanRequestDto(loan);
        // Call Commercial service
        EligibilityResult commercialScore = tracedPost(
                webClientBuilder.build().post()
                        .uri("http://commercial-service/api/commercial/calculate")
                        .body(Mono.just(loanRequestDto), LoanRequestDto.class)
                        .retrieve()
                        .bodyToMono(EligibilityResult.class),
                "commercial-service"
        );
        loan.setCommercialScore(commercialScore.getCommercialScore());
        boolean isEligible = commercialScore.isEligible();
        // if the client is eligible, we call the risk service
        if (isEligible) {
            LoanWithScoreDto loanWithScoreDto = new LoanWithScoreDto(loanRequestDto.getFirstName(), loanRequestDto.getLastName(),
                    loanRequest.getReason(), loanRequest.getAmount(), loanRequest.getDuration(), commercialScore.getCommercialScore());
            // Call Risk service
            Double riskScore = tracedPost(
                    webClientBuilder.build().post()
                            .uri("http://risk-management-service/api/risk-management/calculate")
                            .body(Mono.just(loanWithScoreDto), LoanWithScoreDto.class)
                            .retrieve()
                            .bodyToMono(Double.class),
                    "risk-management-service"
            );
            loan.setRiskScore(riskScore);
            loan.setStatus(riskScore > 3000 ? Loan.Status.APPROVED : Loan.Status.REJECTED);
            loan.setInterestRate(riskScore / 3000 * 10 + 2);
        }
        else{
            loan.setRiskScore(0.0);
            loan.setStatus(Loan.Status.REJECTED);
        }
        // Save the loan after the review
        loanRepository.save(loan);
        // Send the loan request to a Kafka topic
        kafkaTemplate.send("reviewed-loans", loan);

        return loan;
    }

    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

}
