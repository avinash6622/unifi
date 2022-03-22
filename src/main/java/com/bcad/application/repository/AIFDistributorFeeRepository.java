package com.bcad.application.repository;

import com.bcad.application.domain.AIFDistributorFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AIFDistributorFeeRepository extends JpaRepository<AIFDistributorFee, Long> {

    @Query(value="select * from generic_aif_fee where dist_id=:distId and product_id=:productId and " +
        "start_date<:startDate order by start_date  desc limit 1",nativeQuery = true)
    AIFDistributorFee getMonthsCalcultionBefore(@Param("productId")Long id,@Param("distId") Long id1,@Param("startDate") String monthlyBefore);


    @Query(value="select * from generic_aif_fee where dist_id=:distId and product_id=:productId and " +
        "start_date=:startDate ",nativeQuery = true)
    AIFDistributorFee getMonthsCalculation(@Param("productId")Long id,@Param("distId") Long id1,@Param("startDate") String monthlyBefore);

    @Modifying
    @Query(value="delete from generic_aif_fee where dist_id=:distId and start_date between :fromDate and :toDate",nativeQuery = true)
    void findDeleteReports(@Param("fromDate")String sFrom,@Param("toDate") String sTo,@Param("distId") Long id);
}
