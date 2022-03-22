package com.bcad.application.repository;

import com.bcad.application.domain.ClientCommission;

import com.bcad.application.domain.ClientManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientCommissionRepository extends JpaRepository<ClientCommission, Long> {

	@Query(value = "SELECT c.* FROM `bcad_client_commision` c where client_id =:clientId and update_required=0", nativeQuery = true)
	ClientCommission findbcadClientMaster(@Param("clientId") Long clientId);

    @Query(value = "SELECT c.* FROM `bcad_client_commision` c where client_id =:clientId", nativeQuery = true)
	ClientCommission findClient(@Param("clientId") Integer clientId);

}
