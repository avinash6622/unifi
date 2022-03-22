package com.bcad.application.repository;

import java.util.List;
import com.bcad.application.domain.SubRM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubRMRepository extends JpaRepository<SubRM, Long> {
    List<SubRM> findBySubName(String subName);
}
