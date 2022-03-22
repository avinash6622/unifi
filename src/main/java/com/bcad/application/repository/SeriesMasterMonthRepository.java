package com.bcad.application.repository;

import com.bcad.application.domain.SeriesMaster;
import com.bcad.application.domain.SeriesMasterMonth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesMasterMonthRepository extends JpaRepository<SeriesMasterMonth, Long> {

    SeriesMasterMonth findBySeriesMasterAndMonthYearAndIsDeleted(SeriesMaster seriesMaster, String monthYear, Integer isDeleted);
}
