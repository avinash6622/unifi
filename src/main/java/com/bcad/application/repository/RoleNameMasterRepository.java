package com.bcad.application.repository;

import com.bcad.application.domain.RoleNameMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleNameMasterRepository extends JpaRepository<RoleNameMaster, Long> {
}
