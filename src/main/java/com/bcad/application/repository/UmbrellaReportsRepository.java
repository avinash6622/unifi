package com.bcad.application.repository;

import com.bcad.application.domain.TransactionReport;
import com.bcad.application.domain.UmbrellaReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UmbrellaReportsRepository extends JpaRepository<UmbrellaReports,Long> {


    @Query(value = "select * from transaction_report where ws_account_code =:clientCode", nativeQuery = true)
    List<UmbrellaReports> getClientDetails(@Param("clientCode") String clientCode);

}
