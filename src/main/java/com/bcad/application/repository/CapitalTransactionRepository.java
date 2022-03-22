package com.bcad.application.repository;

import com.bcad.application.domain.CapitalTransaction;
import com.bcad.application.domain.InvestmentMaster;
import com.bcad.application.domain.PMSClientMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CapitalTransactionRepository extends JpaRepository<CapitalTransaction,Long> {

    List<CapitalTransaction> findByStartDateAndEndDateAndIntDeleted(Date startDate, Date endDate,Integer isDeleted);

    List<CapitalTransaction> findByInvestmentMasterAndPmsClientMasterAndTransDescIn(InvestmentMaster investmentMaster, PMSClientMaster pmsClientMaster,
                                                                                    List<String> descriptions);
}
