package com.bcad.application.repository;

import com.bcad.application.domain.DistributorMaster;
import com.bcad.application.domain.ReportAIFQuarterFourPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ReportAIFQuarterFourPerformanceRepository extends JpaRepository<ReportAIFQuarterFourPerformance, Long> {

    List<ReportAIFQuarterFourPerformance> findByDistributorMasterAndMnthYear(DistributorMaster distributorMaster, String mnthYear);

    @Query(value="select i.* from report_aif_q4_perf i where i.dist_id=:distId and i.mnthyr in(:mnthYear) ORDER BY i.client_id ASC",nativeQuery = true)
    List<ReportAIFQuarterFourPerformance> perfQFour(@Param("distId")Long distId, @Param("mnthYear")List<String> mnthYear);

    @Modifying
    @Query(value="delete from `report_aif_q4_perf` where dist_id=:distId and mnthyr in(:monthYr)",nativeQuery = true)
    void deleteAIF(@Param("distId") Long id,@Param("monthYr") List<String> aifDate);
}
