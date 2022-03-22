package com.bcad.application.repository;


import com.bcad.application.domain.TransactionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionReportRepository extends JpaRepository<TransactionReport, Integer> {

    @Query(value = "select * FROM `transaction_report` where tran_date <= :endDate and ws_account_code =:code", nativeQuery = true)
    List<TransactionReport> findUmbrellaManagement(@Param("endDate") String endDate, @Param("code") String clientCode);

    TransactionReport findByFileLocationAndIsDeleted(String filePath, Integer isDeleted);

    @Query(value = "select * from transaction_report where tran_date like %:Date%", nativeQuery = true)
    List<TransactionReport> getByDate(@Param("Date") String Date);


    @Query(value= "select * from transaction_report where tran_date <=:tranDate and ws_client_code = (select client_id from bcad_client_master_cd where client_id= :clientId)", nativeQuery = true)
    List<TransactionReport> findSeriesIdBasedOnClient(@Param("clientId") Integer clientId,@Param("tranDate") String tranDate);


    @Query(value = "SELECT * FROM `transaction_report` where tran_date = :endDate",nativeQuery = true)
    List<TransactionReport> findMonthlyCheck(@Param("endDate") String endDate);



}

