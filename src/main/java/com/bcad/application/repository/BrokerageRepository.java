package com.bcad.application.repository;

import com.bcad.application.domain.Brokerage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BrokerageRepository extends JpaRepository<Brokerage, Long> {

    @Query(value = "SELECT sum(brokerage_amt) FROM fee_brokerage where start_date between :startDate and :toDate and int_deleted=0 and pms_client_id=:clientId group by pms_client_id", nativeQuery = true)
    Double getPeriodBrokerage(@Param("startDate")String startDate,@Param("toDate") String toDate,@Param("clientId") Long clientId);

    @Query(value = "SELECT sum(brokerage_amt) FROM fee_brokerage where start_date=:startDate and int_deleted=0 and pms_client_id=:clientId group by pms_client_id", nativeQuery = true)
    Double getPeriodBrokerageMonthVice(@Param("startDate")String startDate,@Param("clientId") Long clientId);
}
