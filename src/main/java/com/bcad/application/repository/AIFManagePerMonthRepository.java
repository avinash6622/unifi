package com.bcad.application.repository;

import com.bcad.application.domain.AIFManagePerMonth;
import com.bcad.application.domain.SeriesMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AIFManagePerMonthRepository extends JpaRepository<AIFManagePerMonth,Long> {

    @Query(value="SELECT sum(management_fee) FROM fee_mange_perf_month where as_on_date between :startDate and :endDate and int_deleted=0 and series_id=:seriesId", nativeQuery = true)
    Double getManage(@Param("seriesId") Long seriesId,@Param("startDate") Date startDate,@Param("endDate") Date endDate);

    @Query(value="SELECT sum(performance_fee) FROM fee_mange_perf_month where as_on_date between :startDate and :endDate and int_deleted=0 and series_id=:seriesId ", nativeQuery = true)
    Double getPerform(@Param("seriesId") Long seriesId,@Param("startDate") Date startDate,@Param("endDate") Date endDate);

    @Query(value="select a.* from fee_mange_perf_month a where a.series_id= :seriesId and a.int_deleted=0 and year(a.as_on_date)=:sYear and month(a.as_on_date)= :sMonth", nativeQuery = true)
    AIFManagePerMonth getFeeSeriesViceAsOn(@Param("seriesId")Long seriesId,@Param("sYear") Integer sYear,@Param("sMonth") Integer sMonth);
}
