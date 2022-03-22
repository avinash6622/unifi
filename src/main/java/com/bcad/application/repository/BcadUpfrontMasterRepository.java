package com.bcad.application.repository;

import com.bcad.application.domain.BcadUpfrontMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BcadUpfrontMasterRepository extends JpaRepository<BcadUpfrontMaster, Long> {

    List<BcadUpfrontMaster> findByTransDateBetweenAndIsDeleted(Date startDate, Date endDate, Integer isDeleted);

    @Query(value = "SELECT sum(initial_fund)+ sum(additional_fund)+sum(inter_ac_transfers) FROM `bcad_upfront_master` where start_date< :fromDate  and client_id =:client and int_deleted=0 order by  start_date desc limit 1", nativeQuery = true)
    Float findByBeforeDate(@Param("fromDate") String fromDate, @Param("client") Long client);

    @Query(value = "select sum(initial_fund) from bcad_upfront_master where trans_date between :fromDate and :toDate and client_id=:id and int_deleted=0 group by client_id", nativeQuery = true)
    Float findInitialFund(@Param("fromDate") String fromDate, @Param("toDate") String todate, @Param("id") Long id);

    @Query(value = "select sum(additional_fund) from bcad_upfront_master where trans_date between :fromDate and :toDate and client_id=:id and int_deleted=0 group by client_id", nativeQuery = true)
    Float findAdditionalFund(@Param("fromDate") String fromDate, @Param("toDate") String todate, @Param("id") Long id);

    @Query(value = "select sum(inter_ac_transfers) from bcad_upfront_master where trans_date between :fromDate and :toDate and client_id=:id and int_deleted=0 group by client_id", nativeQuery = true)
    Float findInterAcTransfers(@Param("fromDate") String fromDate, @Param("toDate") String todate, @Param("id") Long id);

    @Query(value = "select sum(capital_payout) from bcad_upfront_master where trans_date between :fromDate and :toDate and client_id=:id and int_deleted=0 group by client_id", nativeQuery = true)
    Float findCapitalPayout(@Param("fromDate") String fromDate, @Param("toDate") String todate, @Param("id") Long id);

    @Query(value = "select sum(profit_payout) from bcad_upfront_master where trans_date between :fromDate and :toDate and client_id=:id and int_deleted=0 group by client_id", nativeQuery = true)
    Float findProfitPayout(@Param("fromDate") String fromDate, @Param("toDate") String todate, @Param("id") Long id);
}
