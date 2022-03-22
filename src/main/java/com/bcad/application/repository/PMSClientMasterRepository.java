package com.bcad.application.repository;

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
import java.util.Optional;


@Repository
public interface PMSClientMasterRepository  extends JpaRepository<PMSClientMaster,Long> {

    Page<PMSClientMaster> findByDistributorMaster(DistributorMaster distributorMaster, Pageable pageable);
    Page<PMSClientMaster> findByRelationshipManager(RelationshipManager relationshipManager, Pageable pageable);

    @Query(value = "select * FROM `pms_client_master` where dist_id = :distributorId and invest_id=:investId and slab=:slab", nativeQuery = true)
    List<PMSClientMaster> findDistributor(@Param("distributorId")Long distributorId,@Param("investId")Long investId,@Param("slab")String slab);

    PMSClientMaster findByClientCode(String clientCode);
    List<PMSClientMaster> findByClientCodeAndDistributorMaster(String clientCode,DistributorMaster distributorMaster);

    @Query(value="select * from pms_client_master where client_code=:code",nativeQuery = true)
    List<PMSClientMaster> findClientCode(@Param("code") String clientCode);

    @Query(value="select distinct(id) from pms_client_master where dist_id in (:code)",nativeQuery = true)
    List<Integer> findDistributorPMSId(@Param("code") List<Long> distId);

    List<PMSClientMaster> findByDistributorMaster(DistributorMaster distributorMaster);


}
