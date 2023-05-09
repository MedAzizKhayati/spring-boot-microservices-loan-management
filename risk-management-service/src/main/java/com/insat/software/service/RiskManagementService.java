package com.insat.software.service;

import com.insat.software.dto.LoanRequestDto;
import org.springframework.stereotype.Service;
@Service
    public class RiskManagementService {

        public Double calculateMetrics(LoanRequestDto loanRequest) {
            if (loanRequest.getDuration() == null || loanRequest.getAmount() == null) {
                return null;
            } else {
                // Calculate metrics
                Double commercialScore = loanRequest.getCommercialScore();
                Double risk = calculateRisk(commercialScore, Integer.parseInt(loanRequest.getDuration()), Double.parseDouble(loanRequest.getAmount()));

                return risk;
            }
        }

        private Double calculateRisk(Double commercialScore, Integer duration, Double amount) {
            // Perform some calculation based on the commercial score, loan duration, and loan amount
            // For example, here's a simplified calculation
            double factor = commercialScore > 1000 ? 1.2 : 1.0;
            double risk = (duration * amount * 0.01) * factor;
            return risk;
        }
    }

