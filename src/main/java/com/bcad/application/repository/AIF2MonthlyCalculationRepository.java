package com.bcad.application.repository;

import com.bcad.application.domain.AIF2MonthlyCalculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AIF2MonthlyCalculationRepository extends JpaRepository<AIF2MonthlyCalculation, Long> {

    @Query(value = "select * FROM `aif2_monthly_calc` where from_date between :fromDate and :toDate and aif2_client_id =:client", nativeQuery = true)
    AIF2MonthlyCalculation findAIF2Client(@Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("client") Long client);

    @Modifying
    @Query(value="delete from aif2_monthly_calc where dist_id=:distId and from_date between :fromDate and :toDate",nativeQuery = true)
    void findDeleteReports(@Param("fromDate")String sFrom,@Param("toDate") String sTo,@Param("distId") Long id);

    @Query(value = "select sum(dist_share) FROM `aif2_monthly_calc` where from_date=:startDate and dist_id=:distId" ,nativeQuery = true)
    Float getDistShare(@Param("distId") Long id1,@Param("startDate") String monthlyDate);

    // AIF2MonthlyCalculation getMonthBefore(Long id, Long id1, String monthlyBefore);
}
