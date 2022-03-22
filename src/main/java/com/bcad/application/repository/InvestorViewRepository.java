package com.bcad.application.repository;

import com.bcad.application.domain.InvestorView;
import com.bcad.application.domain.SeriesMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestorViewRepository extends JpaRepository<InvestorView, Long> {

    List<InvestorView> findByMonthYear(String monthYear);
    InvestorView findByMonthYearAndSeriesMaster(String monthYear, SeriesMaster seriesMaster);

    @Query(value="SELECT sum(no_of_units) FROM investor_view where mnth_yr in (:aifDate)  and series_id=:seriesId", nativeQuery = true)
    Double getInvestorSum(@Param("seriesId") Long seriesId,@Param("aifDate") List<String> aifDate);
}
