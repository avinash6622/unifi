package com.bcad.application.repository;

import com.bcad.application.domain.DistributorMaster;
import com.bcad.application.domain.ReportGeneration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReportGenerationRepository extends JpaRepository<ReportGeneration, Long> {

    @Query(value = "select * from report_fee where start_date=:startDate and end_date=:toDate and details_updated_flag=:dFlag and dist_id=:distId",nativeQuery = true)
    List<ReportGeneration> findDistributorReport(@Param("dFlag")int dFlag,@Param("startDate")String startDate,@Param("toDate")String endDate,@Param("distId")Long distId);


}
