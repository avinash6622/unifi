package com.bcad.application.repository;

import java.util.List;

import com.bcad.application.domain.DistributorMaster;
import com.bcad.application.domain.DistributorType;
import com.bcad.application.domain.RelationshipManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributorMasterRepository extends JpaRepository<DistributorMaster,Long> {
    Page<DistributorMaster> findByRelationshipManager(RelationshipManager relationshipManager, Pageable pageable);
    List<DistributorMaster> findByDistName(String distName);

    @Query(value = "select * from dist_master where dist_name = :distName", nativeQuery = true)
    DistributorMaster findByDistributorName ( @Param("distName") String distName);

    @Query(value = "select * from dist_master where rm_id=:rm", nativeQuery = true)
    List<DistributorMaster> findRelationshipManager ( @Param("rm") Long rm);

    @Query(value = "select * from dist_master where id not in(select dist_id from bcad_comm_dist_map)", nativeQuery = true)
    List<DistributorMaster> findCommissionDefinition();

    @Query(value = "select * from dist_master where id not in(select dist_id from bcad_comm_dist_map where comm_id in " +
        "(select id from bcad_commission_def where product_id=:productId))", nativeQuery = true)
    List<DistributorMaster> findProductMappings(@Param("productId") Long productId);

    @Query(value = "select * from dist_master where id not in(select dist_id from bcad_comm_dist_opt3 where comm_id in " +
        "(select id from bcad_commission_def where product_id=:productId and pms_investment=:investId))", nativeQuery = true)
    List<DistributorMaster> findProductOption3Mappings(@Param("productId") Long productId, @Param("investId") Long investId);


    @Query(value="SELECT * FROM `dist_master` where dist_name in(:distName) and rm_id in(:rmId) and dist_model_type in(:ModelType)",nativeQuery = true)
    List<DistributorMaster> findDistributorSearch(@Param("distName")List<String> distName,@Param("rmId")List<Long> rmId,@Param("ModelType")List<String> modelType);

    List<DistributorMaster> findByDistNameInAndRelationshipManagerIn(List<String> distName,List<RelationshipManager> relationshipManagers);

    @Query(value = "select * from dist_master where id not in(select dist_id from bcad_comm_dist_map where comm_id in " +
        "(select id from bcad_commission_def where product_id=:productId and pms_investment=:investId))", nativeQuery = true)
    List<DistributorMaster> findPMSMappings(@Param("productId") Long productId, @Param("investId") Long investId);
}

