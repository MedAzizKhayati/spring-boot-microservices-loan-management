package com.insat.software.controller;
 import com.insat.software.dto.LoanRequestDto;
 import com.insat.software.service.RiskManagementService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpStatus;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RestController;
 import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk-management")
public class RiskManagementController {

    private final RiskManagementService riskManagementService;

    @Autowired
    public RiskManagementController(RiskManagementService riskManagementService){
        this.riskManagementService = riskManagementService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/calculate")
    public Double calculateMetrics(@RequestBody LoanRequestDto loanRequest) {
        Double result = riskManagementService.calculateMetrics(loanRequest);
        return result;
    }

}

