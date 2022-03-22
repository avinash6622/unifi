package com.bcad.application.repository;

import com.bcad.application.domain.BCADMakerProfitShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BCADMakerProfitShareRepository extends JpaRepository<BCADMakerProfitShare, Long> {
}
