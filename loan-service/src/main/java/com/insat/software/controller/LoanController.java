package com.insat.software.controller;

import com.insat.software.dto.LoanRequestDto;
import com.insat.software.model.Loan;
import com.insat.software.service.LoanService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/loan")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "loan", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "loan")
    @Retry(name = "loan")
    public CompletableFuture<Loan> requestLoan(@RequestBody LoanRequestDto loanRequestDto) {
        return CompletableFuture.supplyAsync(()-> loanService.createLoanRequest(loanRequestDto));
    }

    public CompletableFuture<String> fallbackMethod(RuntimeException runtimeException){
        runtimeException.printStackTrace();
        return CompletableFuture.supplyAsync(()-> "Oops! Something went wrong, please try again later!");
    }


    @GetMapping
    public List<Loan> getMyLoans(@RequestHeader("X-ClientId") String clientId) {
        return loanService.findLoanByClientId(clientId);
    }

}
