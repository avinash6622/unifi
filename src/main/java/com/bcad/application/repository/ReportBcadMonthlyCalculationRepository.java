package com.bcad.application.repository;

import com.bcad.application.domain.ClientManagement;
import com.bcad.application.domain.ReportBcadMonthlyCalculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface ReportBcadMonthlyCalculationRepository extends JpaRepository<ReportBcadMonthlyCalculation,Long> {

    ReportBcadMonthlyCalculation findByFromDateAndClientManagement(Date fromDate, ClientManagement clientManagement);

    @Query(value = "SELECT * FROM `bcad_monthly_fee` where from_date< :fromDate  and client_id =:client order by from_date  desc limit 1",nativeQuery = true)
    ReportBcadMonthlyCalculation findByBeforeDate(@Param("fromDate") String fromDate, @Param("client") Long client);

    @Modifying
    @Query(value = "delete FROM `bcad_monthly_fee` where from_date between :fromDate and :toDate and dist_id =:dist",nativeQuery = true)
    void findDeleteReports(@Param("fromDate") String fromDate,@Param("toDate") String toDate, @Param("dist") Long dist);

    @Query(value = "SELECT * FROM `bcad_monthly_fee` where from_date between :startDate and :toDate and dist_id =:distId and client_id in(:clientId)",nativeQuery = true)
    List<ReportBcadMonthlyCalculation> findMonthlyReports(@Param("startDate")String fromDate, @Param("toDate")String endDate,
                                                          @Param("distId") Long distId, @Param("clientId")List<Long> clientIds);

    @Query(value="select IF(from_date is null,:fromDate,from_date) as from_date from bcad_monthly_fee  where client_id=:clientId " +
        "and from_date in(select IF(management_fee is null,null,from_date)as from_date from bcad_monthly_fee where client_id=:clientId " +
        "and from_date<:fromDate) order by from_date desc limit 1",nativeQuery = true)
    Date managementDate(@Param("clientId")Long clientId, @Param("fromDate")Date fromDate);

    @Query(value="select sum(adj_upfront_fee) FROM `bcad_monthly_fee` where client_id=:clientId and from_date<=:fromDate",nativeQuery = true)
    Double managementFeeDate(@Param("clientId")Long clientId,@Param("fromDate")String fromDate);

    @Query(value="select sum(adj_upfront_fee) FROM `bcad_monthly_fee` where client_id=:clientId and from_date between :fromDate and :toDate",nativeQuery = true)
    Double managementFeeSelectedDate(@Param("clientId")Long clientId,@Param("fromDate")String fromDate,@Param("toDate")String toDate);

    @Query(value = "SELECT sum(adj_upfront_fee) FROM `bcad_monthly_fee` where from_date= :startDate and dist_id =:distId and client_id in(:clientId)",nativeQuery = true)
      Float getDistributorShare1(@Param("startDate") String sStart,@Param("distId") Long id,@Param("clientId") List<Long> idList);

    @Query(value = "SELECT sum(net_trial_payable) FROM `bcad_monthly_fee` where from_date= :startDate and dist_id =:distId and client_id in(:clientId)",nativeQuery = true)
    Float getDistributorShare2(@Param("startDate") String sStart,@Param("distId") Long id,@Param("clientId") List<Long> idList);

    @Query(value="SELECT from_date FROM `bcad_monthly_fee` where client_id=:clientId order by from_date asc limit 1",nativeQuery = true)
    Date kotakUpfront(@Param("clientId")Long clientId);

   }
