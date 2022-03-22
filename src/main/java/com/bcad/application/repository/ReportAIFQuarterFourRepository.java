package com.bcad.application.repository;
import com.bcad.application.domain.DistributorMaster;
import com.bcad.application.domain.ReportAIFQuarterFour;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ReportAIFQuarterFourRepository extends JpaRepository<ReportAIFQuarterFour,Long> {

    List<ReportAIFQuarterFour> findByDistributorMasterAndMnthYear(DistributorMaster distributorMaster,String mnthYear);

    @Query(value="select i.* from report_aif_q4 i where i.dist_id=:distId and i.mnth_yr in(:mnthYear) ORDER BY i.client_id ASC",nativeQuery = true)
    List<ReportAIFQuarterFour> managQFour(@Param("distId")Long distId,@Param("mnthYear")List<String> mnthYear);

    @Modifying
    @Query(value="delete from `report_aif_q4` where dist_id=:distId and mnth_yr in(:monthYr)",nativeQuery = true)
    void deleteAIF(@Param("distId") Long id,@Param("monthYr") List<String> aifDate);
}
