package com.bcad.application.repository;

import com.bcad.application.domain.MakerCapitalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MakerCapitalTransactionRepository extends JpaRepository<MakerCapitalTransaction,Long> {
}
