package com.bcad.application.repository;
import com.bcad.application.domain.FeeTrailUpfrontTrans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface FeeTrailUpfrontTransRepository extends JpaRepository<FeeTrailUpfrontTrans,Long> {

    @Query(value="select sum(t.upfront_amt) from fee_trail_upfront_trans t where t.dist_id=:distId and" +
        " month(t.trans_date)=:sMonth and year(t.trans_date)=:sYear and t.int_details_updated_flag=0",nativeQuery = true)
    Double getTotalUpfrontMonthVice(@Param("distId") Long distId,@Param("sMonth") Integer month,@Param("sYear") Integer year);

    @Query(value="select sum(t.payable_amt) from fee_trail_upfront_trans t where t.dist_id=:distId and" +
        " month(t.trans_date)=:sMonth and year(t.trans_date)=:sYear and t.int_details_updated_flag=0",nativeQuery = true)
    Double getTotalPayableMonthVice(@Param("distId") Long distId,@Param("sMonth") Integer month,@Param("sYear") Integer year);

    @Query(value="select sum(t.paid_amt) from fee_trail_upfront_trans t where t.dist_id=:distId and" +
        " month(t.trans_date)=:sMonth and year(t.trans_date)=:sYear and t.int_details_updated_flag=0",nativeQuery = true)
    Double getTotalPaidMonthVice(@Param("distId") Long distId,@Param("sMonth") Integer month,@Param("sYear") Integer year);

    @Query(value="select sum(t.trail_amt) from fee_trail_upfront_trans t where " +
        "t.dist_id=:distId and t.trans_date<=:transDate and t.int_details_updated_flag=0",nativeQuery = true)
    Double getTotalTrail(@Param("distId") Long distId,@Param("transDate") Date transDate);

    @Query(value="select sum(t.upfront_amt) from fee_trail_upfront_trans t where " +
        "t.dist_id=:distId and t.trans_date<=:transDate and t.int_details_updated_flag=0",nativeQuery = true)
    Double getTotalUpfront(@Param("distId") Long distId,@Param("transDate") Date transDate);

    @Query(value="select sum(t.payable_amt) from fee_trail_upfront_trans t where " +
        "t.dist_id=:distId and t.trans_date<=:transDate and t.int_details_updated_flag=0",nativeQuery = true)
    Double getTotalPayable(@Param("distId") Long distId,@Param("transDate") Date transDate);

    @Query(value="select sum(t.paid_amt) from fee_trail_upfront_trans t where " +
        "t.dist_id=:distId and t.trans_date<=:transDate and t.int_details_updated_flag=0",nativeQuery = true)
    Double getTotalPaid(@Param("distId") Long distId,@Param("transDate") Date transDate);

    @Query(value="select sum(t.upfront_amt) from fee_trail_upfront_trans t where " +
        "t.dist_id=:distId and t.trans_date between :startDate and :toDate and t.int_details_updated_flag=0",nativeQuery = true)
    Double getCurrentUpfront(@Param("distId") Long distId,@Param("startDate") Date startDate,@Param("toDate") Date toDate);


}
