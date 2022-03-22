package com.bcad.application.repository;

import com.bcad.application.domain.BCADDistributorFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface BCADDistributorFeeRepository extends JpaRepository<BCADDistributorFee, Long> {
    @Query(value="select * from  bcad_dist_fee where dist_id=:distId and start_date=:startDate",nativeQuery = true)
    BCADDistributorFee getMonthlyFee(@Param("startDate")String sStart,@Param("distId") Long id);

    @Query(value="select * from  bcad_dist_fee where dist_id=:distId and start_date<:startDate order by start_date  desc limit 1",nativeQuery = true)
    BCADDistributorFee getMonthlyFeeBefore(@Param("startDate") String sStart,@Param("distId") Long id);

    @Query(value="select sum(dist_share_opt1) from  bcad_dist_fee where dist_id=:distId and start_date between :startDate and :toDate",nativeQuery = true)
    Float getDistributorOption1(@Param("startDate")String sStart,@Param("toDate")String sTo,@Param("distId") Long id);

    @Query(value="select sum(trail_share_opt2) from  bcad_dist_fee where dist_id=:distId and start_date between :startDate and :toDate",nativeQuery = true)
    Float getDistributorOption2(@Param("startDate")String sStart,@Param("toDate")String sTo,@Param("distId") Long id);

    @Modifying
    @Query(value="delete from bcad_dist_fee where dist_id=:distId and start_date between :fromDate and :toDate",nativeQuery = true)
    void findDeleteReports(@Param("fromDate")String sFrom,@Param("toDate") String sTo,@Param("distId") Long id);

}
