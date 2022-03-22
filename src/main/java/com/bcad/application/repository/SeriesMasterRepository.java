package com.bcad.application.repository;

import com.bcad.application.domain.SeriesMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesMasterRepository extends JpaRepository<SeriesMaster,Long> {

    SeriesMaster findBySeriesCode(String seriesCode);

}
