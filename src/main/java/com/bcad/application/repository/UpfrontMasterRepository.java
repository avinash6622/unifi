package com.bcad.application.repository;

import com.bcad.application.domain.InvestmentMaster;
import com.bcad.application.domain.PMSClientMaster;
import com.bcad.application.domain.UpfrontMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UpfrontMasterRepository extends JpaRepository<UpfrontMaster, Long> {

    List<UpfrontMaster> findByTransDateBetweenAndIsDeleted(Date startDate, Date endDate, Integer isDeleted);
    List<UpfrontMaster> findByPmsClientMasterAndTransDateBetweenAndIsDeleted(PMSClientMaster pmsClientMaster,Date startDate, Date endDate, Integer isDeleted);

   // List<UpfrontMaster> findByPmsClientMasterAndTransDateBetweenAndInvestmentMasterAndIsDeleted(PMSClientMaster pmsClientMaster, Date startDate,
    //                                                                                            Date endDate, InvestmentMaster investmentMaster, Integer isDeleted);

    @Query(value="select distinct(trans_date) from upfront_master b where b.client_id=:clientId and b.trans_date between :startDate and :endDate and b.int_deleted=0", nativeQuery = true)
    List<Date> getTrans(@Param("clientId")Long clientId,@Param("startDate")Date startDate,@Param("endDate")Date toDate);

   /* @Query(value="select distinct(trans_date) from upfront_master b where b.client_id=:clientId and b.trans_date between :startDate and :endDate and b.int_deleted=0", nativeQuery = true)
    List<Date> getTransStrategy(@Param("clientId")Long clientId,@Param("startDate")Date startDate,@Param("endDate")Date toDate);*/
}
