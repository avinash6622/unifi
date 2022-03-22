package com.bcad.application.repository;

import com.bcad.application.domain.AIFClientMaster;
import com.bcad.application.domain.InvestorProtfolio;
import com.bcad.application.domain.SeriesMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvestorProtfolioRepository extends JpaRepository<InvestorProtfolio, Long> {

    List<InvestorProtfolio> findByMonthYr(String monthYear);

    InvestorProtfolio findByMonthYrAndAifClientMaster(String monthYear, AIFClientMaster aifClientMaster);
    InvestorProtfolio findBySeriesMasterAndAifClientMaster(SeriesMaster seriesMaster, AIFClientMaster aifClientMaster);
}
