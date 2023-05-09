package com.insat.software.repository;
import com.insat.software.model.LoanAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanAgreementRepository extends JpaRepository<LoanAgreement, Long> {

}
