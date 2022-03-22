package com.bcad.application.repository;

import com.bcad.application.domain.AIFClientMaster;
import com.bcad.application.domain.DistributorMaster;
import com.bcad.application.domain.PMSClientMaster;
import com.bcad.application.domain.RelationshipManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AIFClientMasterRepository extends JpaRepository<AIFClientMaster,Long> {
    Page<AIFClientMaster> findByDistributorMaster(DistributorMaster distributorMaster, Pageable pageable);
    Page<AIFClientMaster> findByRelationshipManager(RelationshipManager relationshipManager,Pageable pageable);
    AIFClientMaster findByClientCode(String clientCode);

    @Query(value="select * from aif_client_master where client_code=:code",nativeQuery = true)
    List<AIFClientMaster> findClientCode(@Param("code") String clientCode);
}
