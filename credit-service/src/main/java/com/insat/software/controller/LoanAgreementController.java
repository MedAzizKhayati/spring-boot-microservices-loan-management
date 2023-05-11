package com.insat.software.controller;

import com.insat.software.dto.LoanAgreementDto;
import com.insat.software.service.LoanAgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/loan-agreement")
@RequiredArgsConstructor
public class LoanAgreementController {

    private final LoanAgreementService loanAgreementService;

    @GetMapping
    public List<LoanAgreementDto> getMyLoanAgreements() {
        return loanAgreementService.findAll();
    }
}


