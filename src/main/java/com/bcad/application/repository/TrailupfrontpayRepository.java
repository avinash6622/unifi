package com.bcad.application.repository;

import com.bcad.application.domain.DistributorMaster;
import com.bcad.application.domain.Trailupfrontpay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TrailupfrontpayRepository extends JpaRepository<Trailupfrontpay, Long> {

      @Query(value = "SELECT * FROM `pay_trail_upfront` where pay_amt=:amt and pay_date=:payDate and dist_id=:distId", nativeQuery = true)
      Trailupfrontpay findPaymentRepeation(@Param("payDate")String paymentDate,@Param("amt")Float payAmount,@Param("distId")Long distId);

      @Query(value = "SELECT * FROM `pay_trail_upfront` where dist_id =:distId and pay_date between :startDate and :endDate", nativeQuery = true)
      List<Trailupfrontpay> getTrailUpfrontPay(@Param("distId")Long distributorMaster,@Param("startDate")String startDate,
                                             @Param("endDate")String endDate);
}
