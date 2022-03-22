package com.bcad.application.repository;
import com.bcad.application.domain.MasterType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterTypeRepository extends JpaRepository<MasterType,Long> {

    MasterType findByFileName(String fileName);
}
