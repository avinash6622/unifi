package com.bcad.application.repository;

import com.bcad.application.domain.AIF2Investments;
import com.bcad.application.domain.AIF2MonthlyCalculation;
import com.bcad.application.domain.AIFUmbrella;
import com.bcad.application.domain.ClientManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface AifUmbrellaCalculationRepository extends JpaRepository<AIFUmbrella ,Integer> {

    @Query(value = "select * FROM `aif_umbrella_monthly_calc` where from_date between :fromDate and :toDate and aif2_client_id =:client", nativeQuery = true)
    List<AIFUmbrella> findAIFUmbrellaClient(@Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("client") Long client);


    @Modifying
    @Query(value="delete from aif_umbrella_monthly_calc where dist_id=:distId and from_date between :fromDate and :toDate",nativeQuery = true)
    void findUmbrellaDeleteReports(@Param("fromDate")String sFrom,@Param("toDate") String sTo,@Param("distId") Long id);


    List<AIFUmbrella> findByClientManagementAndFromDateAndTotOfUnits(ClientManagement clientManagement, Date fromDate,Double totalUnits);


    @Query(value = "SELECT * FROM `aif_umbrella_monthly_calc` where month(from_date)=month(:fromDate)-1  and aif2_client_id =:client order by from_date  desc",nativeQuery = true)
    List<AIFUmbrella> findByBeforeDateUmbrella(@Param("fromDate") String fromDate, @Param("client") Long client);



    @Query(value = "SELECT * FROM `aif_umbrella_monthly_calc` where from_date <:fromDate  and aif2_client_id =:client order by from_date  desc limit 1",nativeQuery = true)
    AIFUmbrella findByBeforeDateUmbrellas(@Param("fromDate") String fromDate, @Param("client") Long client);

}
