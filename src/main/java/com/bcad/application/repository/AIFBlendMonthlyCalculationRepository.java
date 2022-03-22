package com.bcad.application.repository;

import com.bcad.application.domain.AIF2MonthlyCalculation;
import com.bcad.application.domain.AIFBlendMonthlyCalculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AIFBlendMonthlyCalculationRepository extends JpaRepository<AIFBlendMonthlyCalculation,Long> {

    @Query(value = "select * FROM `aif_blend_monthly_calc` where from_date between :fromDate and :toDate and aif2_client_id =:client", nativeQuery = true)
    AIFBlendMonthlyCalculation findAIFBlendClient(@Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("client") Long client);

    @Modifying
    @Query(value="delete from aif_blend_monthly_calc where dist_id=:distId and from_date between :fromDate and :toDate",nativeQuery = true)
    void findDeleteReports(@Param("fromDate")String sFrom,@Param("toDate") String sTo,@Param("distId") Long id);

    @Query(value = "select sum(dist_share) FROM `aif_blend_monthly_calc` where from_date between :startDate and :toDate and dist_id =:distId", nativeQuery = true)
    Float getDistributorShare(@Param("startDate") String sStart, @Param("toDate") String sEnd,@Param("distId") Long id);
}
