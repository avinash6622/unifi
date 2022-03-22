package com.bcad.application.repository;

import com.bcad.application.domain.BCADMakerPMSNav;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BCADMakerPMSNavRepository extends JpaRepository<BCADMakerPMSNav, Long> {
}
