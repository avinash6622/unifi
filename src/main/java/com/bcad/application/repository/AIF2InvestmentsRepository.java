package com.bcad.application.repository;

import com.bcad.application.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIF2InvestmentsRepository extends JpaRepository<AIF2Investments, Long> {

    AIF2Investments findByClientManagementAndProduct(ClientManagement clientManagement, Product product);

    @Query(value = "select * FROM `aif2_calculations` where aif2_client_id = :clientManagementId and product_id=:productId", nativeQuery = true)
    List<AIF2Investments> getByClientManagementAndProduct(@Param("clientManagementId") Integer clientManagementId,@Param("productId") Long productId);


    @Query(value = "select sum(no_of_units) FROM `aif2_calculations` where aif2_series_id = :seriesId and product_id=:product", nativeQuery = true)
    Float findSeriesUnits(@Param("seriesId")Long aif2Series, @Param("product")Long product);




}
