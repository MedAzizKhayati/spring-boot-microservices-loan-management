package com.insat.software.service;

import com.insat.software.dto.AmortizationTableDto;
import com.insat.software.dto.LoanAgreementDto;
import com.insat.software.model.Loan;
import com.insat.software.model.LoanAgreement;
import com.insat.software.repository.LoanAgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanAgreementService {

    private final LoanAgreementRepository loanAgreementRepository;

    public LoanAgreementDto generateLoanAgreement(Loan loan) {
        LoanAgreement loanAgreement = new LoanAgreement();
        loanAgreement.setClientId(loan.getClientId());
        loanAgreement.setClientFirstName(loan.getClientFirstName());
        loanAgreement.setClientLastName(loan.getClientLastName());
        loanAgreement.setAmount(loan.getAmount());
        loanAgreement.setInterestRate(loan.getInterestRate());
        loanAgreement.setDuration(loan.getDuration());
        loanAgreement.setCreatedAt(LocalDateTime.now());
        loanAgreement.setUpdatedAt(LocalDateTime.now());

        double monthlyPayment = calculateMonthlyPayment(loan.getAmount(), loan.getDuration(),loan.getInterestRate());
        loanAgreement.setMonthlyPayment(monthlyPayment);

        double totalAmount = monthlyPayment * loan.getDuration();
        loanAgreement.setTotalPayment(totalAmount);

        double totalInterest = totalAmount - loan.getAmount();
        loanAgreement.setTotalInterest(totalInterest);

        loanAgreementRepository.save(loanAgreement);
        LoanAgreementDto loanAgreementDto = convertToDto(loanAgreement);
        return loanAgreementDto;
    }
    private double calculateMonthlyPayment(double amount, int duration, double interestRate) {
        double monthlyInterestRate = interestRate / 12;
        double monthlyPayment = amount * (monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -duration)));
        return monthlyPayment;
    }

    public LoanAgreementDto getLoanAgreement(Long loanId) {
        LoanAgreement loanAgreement = loanAgreementRepository.findById(loanId).orElse(null);
        if (loanAgreement == null) {
            return null;
        }
        LoanAgreementDto loanAgreementDto = convertToDto(loanAgreement);
        return loanAgreementDto;
    }
    // generate Amortization Table
    public List<AmortizationTableDto> generateAmortizationTable(LoanAgreementDto loanAgreement) {
        List<AmortizationTableDto> amortizationTable = new ArrayList<>();

        double monthlyInterestRate = loanAgreement.getInterestRate() / 12;
        double monthlyPayment = loanAgreement.getMonthlyPayment();
        double remainingBalance = loanAgreement.getAmount();
        LocalDateTime paymentDate = loanAgreement.getCreatedAt().plusMonths(1);

        for (int i = 1; i <= loanAgreement.getDuration(); i++) {
            double interestPayment = remainingBalance * monthlyInterestRate;
            double principalPayment = monthlyPayment - interestPayment;

            if (i == loanAgreement.getDuration()) {
                principalPayment = remainingBalance;
                monthlyPayment = principalPayment + interestPayment;
            }

            AmortizationTableDto row = AmortizationTableDto.builder()
                    .installmentNumber(i)
                    .paymentDate(paymentDate)
                    .beginningBalance(remainingBalance)
                    .principalPayment(principalPayment)
                    .interestPayment(interestPayment)
                    .endingBalance(remainingBalance - principalPayment)
                    .totalPayment(monthlyPayment)
                    .build();

            amortizationTable.add(row);

            remainingBalance -= principalPayment;
            paymentDate = paymentDate.plusMonths(1);
        }

        return amortizationTable;
    }
    public LoanAgreementDto convertToDto(LoanAgreement loanAgreement) {
        LoanAgreementDto loanAgreementDto = new LoanAgreementDto();
        loanAgreementDto.setId(loanAgreement.getLoanId());
        loanAgreementDto.setAmount(loanAgreement.getAmount());
        loanAgreementDto.setInterestRate(loanAgreement.getInterestRate());
        loanAgreementDto.setDuration(loanAgreement.getDuration());
        loanAgreementDto.setMonthlyPayment(loanAgreement.getMonthlyPayment());
        loanAgreementDto.setTotalPayment(loanAgreement.getTotalPayment());
        loanAgreementDto.setStartDate(loanAgreement.getCreatedAt().toLocalDate());
        loanAgreementDto.setEndDate(loanAgreement.getCreatedAt().plusMonths(loanAgreement.getDuration()).toLocalDate());
        loanAgreementDto.setStatus("Active");

        List<AmortizationTableDto> amortizationTableDtoList = generateAmortizationTable(loanAgreementDto);
        loanAgreementDto.setAmortizationTable(amortizationTableDtoList);

        return loanAgreementDto;
    }

}

