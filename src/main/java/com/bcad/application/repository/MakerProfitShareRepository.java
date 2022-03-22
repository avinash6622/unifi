package com.bcad.application.repository;

import com.bcad.application.domain.MakerProfitShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MakerProfitShareRepository extends JpaRepository<MakerProfitShare, Long> {
}
