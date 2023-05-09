package com.insat.software.controller;

import com.insat.software.dto.AmortizationTableDto;
import com.insat.software.dto.LoanAgreementDto;
import com.insat.software.model.Loan;
import com.insat.software.service.LoanAgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/loan-agreement")
public class LoanAgreementController {

    private final LoanAgreementService loanAgreementService;

    @PostMapping("/generate")
    public ResponseEntity<LoanAgreementDto> generateLoanAgreement(@RequestBody Loan loan) {
        LoanAgreementDto loanAgreement = loanAgreementService.generateLoanAgreement(loan);
        return ResponseEntity.ok(loanAgreement);
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<LoanAgreementDto> getLoanAgreement(@PathVariable Long loanId) {
        LoanAgreementDto loanAgreement = loanAgreementService.getLoanAgreement(loanId);
        if (loanAgreement == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(loanAgreement);
    }

    @GetMapping("/{loanId}/amortization-table")
    public ResponseEntity<List<AmortizationTableDto>> getAmortizationTable(@PathVariable Long loanId) {
        LoanAgreementDto loanAgreement = loanAgreementService.getLoanAgreement(loanId);
        if (loanAgreement == null) {
            return ResponseEntity.notFound().build();
        }
        List<AmortizationTableDto> amortizationTable = loanAgreementService.generateAmortizationTable(loanAgreement);
        return ResponseEntity.ok(amortizationTable);
    }
}


