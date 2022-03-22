package com.bcad.application.repository;

import com.bcad.application.domain.AIFClientMaster;
import com.bcad.application.domain.AIFUpdatedUnits;
import com.bcad.application.domain.DistributorMaster;
import com.bcad.application.domain.SeriesMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIFUpdatedUnitsRepository extends JpaRepository<AIFUpdatedUnits, Long> {
    List<AIFUpdatedUnits> findByMonthYear(String monthYear);
    AIFUpdatedUnits findByMonthYearAndAifClientMasterAndSeriesMaster(String monthYear, AIFClientMaster aifClientMaster, SeriesMaster seriesMaster);
    List<AIFUpdatedUnits> findByDistributorMasterAndMonthYear(DistributorMaster distributorMaster,String monthYear);

}
