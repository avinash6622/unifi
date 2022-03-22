package com.bcad.application.repository;

import com.bcad.application.domain.ClientFeeCommission;
import com.bcad.application.domain.PMSClientMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientFeeCommissionRepository extends JpaRepository<ClientFeeCommission, Long> {


    ClientFeeCommission findByPmsClientMaster( PMSClientMaster pmsClientMaster);

    @Query(value = "SELECT c.* FROM `client_fee_commision` c where pms_client_id =:clientId and update_required=0", nativeQuery = true)
    ClientFeeCommission findPmsClientMaster(@Param("clientId") Long clientId);

    @Query(value = "SELECT c.* FROM `client_fee_commision` c where pms_client_id =:clientId", nativeQuery = true)
    ClientFeeCommission getClient(@Param("clientId")Long clientId);




}
