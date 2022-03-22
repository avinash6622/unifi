package com.bcad.application.repository;

import com.bcad.application.domain.DistributorMaster;
import com.bcad.application.domain.DistributorOpeningBal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributorOpeningBalRepository extends JpaRepository<DistributorOpeningBal,Long> {

    DistributorOpeningBal findByDistributorMaster(DistributorMaster distributorMaster);
}
