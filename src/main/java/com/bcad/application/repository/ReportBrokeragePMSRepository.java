package com.bcad.application.repository;

import com.bcad.application.domain.ReportBrokeragePMS;
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
public interface ReportBrokeragePMSRepository extends JpaRepository<ReportBrokeragePMS,Long> {

    List<ReportBrokeragePMS> findByReportGeneration(ReportGeneration reportGeneration);

    @Modifying
    @Query(value = "delete FROM `report_brok_pms` where report_id =:reportId",nativeQuery = true)
    void deleteBrokerage(@Param("reportId") Long id);
}
