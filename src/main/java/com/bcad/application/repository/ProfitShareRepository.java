package com.bcad.application.repository;

import com.bcad.application.domain.ProfitShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface ProfitShareRepository extends JpaRepository<ProfitShare, Long> {

    @Query(value = "select ps.* from fee_pms_profit_share ps where int_deleted=0  and receipt_date between :startDate and :endDate" +
        " and client_id in(select id from pms_client_master where dist_id=:distId)",nativeQuery = true)
    List<ProfitShare> getProfitShareByDistBetween(@Param("distId")Long distId,@Param("startDate") Date startDate,@Param("endDate") Date endDate);

    @Query(value = "select ps.* from fee_pms_profit_share ps where int_deleted=0 and start_date=:startDate and client_id in(select id from pms_client_master where dist_id=:distId)",nativeQuery = true)
    List<ProfitShare> getProfitShareByDist(@Param("distId") Long distId,@Param("startDate") Date startDate);

    @Query(value="select * from fee_pms_profit_share where fileuploaded_id in(290,292)",nativeQuery = true)
    List<ProfitShare> getInvestment();

    @Query(value="SELECT sum(profit_share_income) FROM fee_pms_profit_share where client_id=:clientId and investment_id=:investId and receipt_date\n" +
        " between :startDate and :endDate and int_deleted=0;",nativeQuery = true)
    Float getProfitShare(@Param("clientId")Long clientId, @Param("investId")Long investId, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
