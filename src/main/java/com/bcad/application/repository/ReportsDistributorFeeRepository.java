package com.bcad.application.repository;
import com.bcad.application.domain.DistributorMaster;
import com.bcad.application.domain.ReportsDistributorFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
@Transactional
public interface ReportsDistributorFeeRepository extends JpaRepository<ReportsDistributorFee,Long> {

    @Query(value="select * from reports_dist_fee where dist_id=:distId and start_date=:startDate and int_details_updated_flag=0",nativeQuery = true)
    ReportsDistributorFee findCurrentOpeningBalance(@Param("startDate")String startDate,@Param("distId") Long distributorMaster);

    @Query(value="select sum(t.aif_perf_total) from reports_dist_fee t where t.dist_id=:distId and t.start_date between :startDate and :endDate and t.int_details_updated_flag=0",nativeQuery = true)
    Double getQuarter(@Param("distId") Long distId,@Param("startDate")Date startDate,@Param("endDate")Date endDate);

    @Query(value="select r.* from reports_dist_fee r where r.dist_id=:distId and month(r.start_date)=:sMonth and " +
        "year(r.start_date)=:sYear and r.int_details_updated_flag=0",nativeQuery = true)
    ReportsDistributorFee getCurrentTrans(@Param("distId") Long distId,@Param("sMonth") Integer sMonth,@Param("sYear") Integer sYear);

    @Query(value="select sum(r.upfront_amount) from reports_dist_fee r where " +
        "r.dist_id=:distId and r.start_date between :startDate and :endDate and r.int_details_updated_flag=0",nativeQuery = true)
    Double getUpfront(@Param("distId") Long distId,@Param("startDate")Date startDate,@Param("endDate")Date endDate);

    @Query(value="select sum(r.pms_total) from reports_dist_fee r where " +
        "r.dist_id=:distId and r.start_date between :startDate and :endDate and r.int_details_updated_flag=0",nativeQuery = true)
    Double getTrail(@Param("distId") Long distId,@Param("startDate")Date startDate,@Param("endDate")Date endDate);

    @Query(value="select sum(r.payable_amount) from reports_dist_fee r where " +
        "r.dist_id=:distId and r.start_date<=:endDate and r.int_details_updated_flag=0",nativeQuery = true)
    Double getPayable(@Param("distId") Long distId,@Param("endDate")Date endDate);

    @Query(value="select sum(r.paid_amount) from reports_dist_fee r where " +
        "r.dist_id=:distId and r.start_date<=:endDate and r.int_details_updated_flag=0",nativeQuery = true)
    Double getPaid(@Param("distId") Long distId,@Param("endDate")Date endDate);

    @Query(value="select sum(r.paid_amount) from reports_dist_fee r where " +
        "r.dist_id=:distId and r.start_date between :startDate and :endDate and r.int_details_updated_flag=0",nativeQuery = true)
    Double getCurrentPaid(@Param("distId") Long distId,@Param("startDate")Date startDate,@Param("endDate")Date endDate);

    @Modifying
    @Query(value="delete FROM `reports_dist_fee` where dist_id=:distId and start_date between :sFrom and :sTo",nativeQuery = true)
    void deleteReports(@Param("sFrom") String sFrom,@Param("sTo") String sTo,@Param("distId") Long id);

    @Query(value="select r.payable_amount from reports_dist_fee r where " +
        "r.dist_id=:distId and r.start_date<:endDate and r.int_details_updated_flag=0 order by start_date desc limit 1",nativeQuery = true)
    Double getClosedUpfront(@Param("distId")Long id,@Param("endDate") Date startDate);
}
