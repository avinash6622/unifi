package com.bcad.application.repository;

import java.util.List;

import com.bcad.application.domain.InvestmentMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentMasterRepository extends JpaRepository<InvestmentMaster,Long> {

    InvestmentMaster findByInvestmentName(String investName);
   
}
