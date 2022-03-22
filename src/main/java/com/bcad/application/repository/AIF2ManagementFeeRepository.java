package com.bcad.application.repository;

import java.util.List;

import com.bcad.application.domain.AIF2ManagementFee;
import com.bcad.application.domain.AIF2MonthlyCalculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AIF2ManagementFeeRepository extends JpaRepository<AIF2ManagementFee,Long> {

    @Query(value = "select * FROM `aif2_management_fee` where mnth_year between :fromDate and :toDate and series_master =:series and product_id=:product", nativeQuery = true)
    AIF2ManagementFee findAIF2Management(@Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("series") Long series, @Param("product") Long product);


    @Query(value = "select * FROM `aif2_management_fee` where mnth_year between :fromDate and :toDate  and product_id=:product", nativeQuery = true)
    List<AIF2ManagementFee> findAIF2Managements(@Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("product") Long product);


}

