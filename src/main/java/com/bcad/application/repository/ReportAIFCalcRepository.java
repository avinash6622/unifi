package com.bcad.application.repository;

import com.bcad.application.domain.ReportAIFCalc;
import com.bcad.application.domain.ReportGeneration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ReportAIFCalcRepository extends JpaRepository<ReportAIFCalc,Long> {

    List<ReportAIFCalc> findByReportGeneration(ReportGeneration reportGeneration);

    @Modifying
    @Query(value = "delete FROM `report_aif_calc` where report_id =:reportId",nativeQuery = true)
    void deleteAIF(@Param("reportId") Long id);
}
