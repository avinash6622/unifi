package com.bcad.application.repository;

import com.bcad.application.domain.MakerAIFManagePerMonth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MakerAIFManagePerMonthRepository extends JpaRepository<MakerAIFManagePerMonth, Long> {
}
