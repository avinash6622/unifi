package com.bcad.application.repository;

import com.bcad.application.domain.DistributorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributorTypeRepository extends JpaRepository<DistributorType,Long> {
    DistributorType findByDistTypeName(String distTypeName);
    
}
