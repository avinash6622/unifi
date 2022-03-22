package com.bcad.application.repository;
import com.bcad.application.domain.ReportGeneration;
import com.bcad.application.domain.ReportProfitShare;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ReportProfitShareRepository extends JpaRepository<ReportProfitShare,Long> {

    List<ReportProfitShare> findByReportGeneration(ReportGeneration reportGeneration);

    @Modifying
    @Query(value = "delete FROM `report_profit_share` where report_id =:reportId",nativeQuery = true)
    void deleteProfit(@Param("reportId") Long id);
}
