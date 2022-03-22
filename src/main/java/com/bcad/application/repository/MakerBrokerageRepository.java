package com.bcad.application.repository;

import com.bcad.application.domain.Brokerage;
import com.bcad.application.domain.FileUpload;
import com.bcad.application.domain.MakerBrokerage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MakerBrokerageRepository extends JpaRepository<MakerBrokerage,Long> {

}
