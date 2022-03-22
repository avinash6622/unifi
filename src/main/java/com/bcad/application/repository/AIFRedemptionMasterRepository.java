package com.bcad.application.repository;

import com.bcad.application.domain.AIFRedemptionMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AIFRedemptionMasterRepository extends JpaRepository<AIFRedemptionMaster,Long> {

}
