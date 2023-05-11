package com.insat.software.controller;

import com.insat.software.dto.EligibilityResult;
import com.insat.software.dto.LoanRequestDto;
import com.insat.software.service.CommercialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commercial")
public class CommercialController {

    private final CommercialService commercialService;

    @Autowired
    public CommercialController(CommercialService commercialService){
        this.commercialService = commercialService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/calculate")
    public EligibilityResult calculateMetrics(@RequestBody LoanRequestDto loanRequest) {
        Double commercialScore = commercialService.calculateInitialScore(loanRequest);
        boolean isEligible = commercialService.isEligible(loanRequest);
        EligibilityResult eligibilityResult = new EligibilityResult(isEligible, commercialScore);
        return eligibilityResult;
    }

}

