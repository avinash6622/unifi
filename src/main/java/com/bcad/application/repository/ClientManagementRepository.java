package com.bcad.application.repository;

import com.bcad.application.domain.*;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientManagementRepository extends JpaRepository<ClientManagement, Long> {
    Optional<ClientManagement> findById(Long id);
    ClientManagement findByClientCode(String clientcode);
    Page<ClientManagement> findByDistributorMaster(DistributorMaster distributorMaster, Pageable pageable);
    Page<ClientManagement> findByRelationshipManager(RelationshipManager relationshipManager, Pageable pageable);
    List<ClientManagement> findByClientName(String clientName);
    List<ClientManagement> findByDistributorMaster(DistributorMaster distributorMaster);
    List<ClientManagement> findByDistributorMasterAndProductAndDistributorOption(DistributorMaster distributorMaster,
                                                                                 Product product,DistributorOption distributorOption);
    List<ClientManagement> findByDistributorMasterAndProduct(DistributorMaster distributorMaster,Product product);
    List<ClientManagement> findByRelationshipManager(RelationshipManager relationshipManagers);

    @Query(value="select * from bcad_client_master_cd where client_code=:code",nativeQuery = true)
    List<ClientManagement> findClientCode(@Param("code") String clientCode);

    @Query(value="select * from bcad_client_master_cd where product_id=1",nativeQuery = true)
    List<ClientManagement> findClientList();

    @Query(value = "select * FROM bcad_client_master_cd where dist_master_id = :distributorId and product_id=:productId and SLAB=:slab", nativeQuery = true)
    List<ClientManagement> findDistributor(@Param("distributorId")Long distributorId,@Param("productId")Long productId,@Param("slab")String slab);

    @Query(value="select distinct(id) from bcad_client_master_cd where dist_master_id in (:code)",nativeQuery = true)
    List<Integer> findDistributorCMId(@Param("code") List<Long> distId);


}

