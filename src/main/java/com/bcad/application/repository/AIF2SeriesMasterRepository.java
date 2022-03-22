package com.bcad.application.repository;

import java.util.List;

import com.bcad.application.domain.AIF2SeriesMaster;
import com.bcad.application.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIF2SeriesMasterRepository extends JpaRepository<AIF2SeriesMaster,Long> {

    List<AIF2SeriesMaster> findByProduct(Product product);

    AIF2SeriesMaster findByClassTypeAndProduct(String classType,Product product);

    AIF2SeriesMaster findByClassType(String classType);

}

