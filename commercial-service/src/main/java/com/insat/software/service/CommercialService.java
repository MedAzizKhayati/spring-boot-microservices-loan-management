package com.insat.software.service;

import com.insat.software.dto.EligibilityResult;
import com.insat.software.dto.LoanRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CommercialService {

    private final double INCOME_FACTOR = 0.5;
    private final double EXPENSES_FACTOR = 0.3;
    private final double INITIAL_SCORE_FACTOR = 0.2;
    private final double MIN_SCORE = 200;

    public Double calculateInitialScore(LoanRequestDto loanRequest) {
        // Generate mock data for income and expenses
        Double income = 5000.0;
        Double expenses = 3000.0;
        if (income == null || expenses == null) {
            return null;
        } else {

            double initialScore = (income * INCOME_FACTOR) -
                    (expenses * EXPENSES_FACTOR);
            return initialScore * INITIAL_SCORE_FACTOR;
        }
    }
    public boolean  isEligible(LoanRequestDto loanRequest) {
        Double initialScore = calculateInitialScore(loanRequest);
        if (initialScore == null) {
            return false;
        }else{
            return initialScore >= MIN_SCORE;
        }
    }

}