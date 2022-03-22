package com.bcad.application.repository;

import com.bcad.application.domain.GenericPayTrailUpfront;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenericPayTrailUpfrontRepository extends JpaRepository<GenericPayTrailUpfront, Long> {

    @Query(value = "SELECT * FROM `generic_pay_trail_upfront` where pay_amt=:amt and pay_date=:payDate and dist_id=:distId and product_id=:productId", nativeQuery = true)
    GenericPayTrailUpfront findPaymentRepeation(@Param("payDate") String payDate, @Param("amt") Float paymentAmount, @Param("distId") Long distId,
                                                @Param("productId") Long productId);

    @Query(value = "SELECT * FROM `generic_pay_trail_upfront` where dist_id =:distId and pay_date between :startDate and :endDate", nativeQuery = true)
    List<GenericPayTrailUpfront> getTrailUpfrontPay(@Param("distId") Long distributorMaster, @Param("startDate") String startDate,
                                                    @Param("endDate") String endDate);
    @Query(value = "SELECT sum(pay_amt) FROM `generic_pay_trail_upfront` where dist_id =:distId and pay_date between :startDate and :endDate and pay_type=:modelType" +
        " and product_id=:productId", nativeQuery = true)
    Float getProductPaid(@Param("startDate") String sStart,@Param("endDate") String sEnd,@Param("productId") Long id,
                         @Param("modelType") String upfront,@Param("distId") Long id1);
}
